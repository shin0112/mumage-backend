package mumage.mumagebackend.service;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mumage.mumagebackend.Config.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtService {

    private final static String HEADER_KEY = "Authorization";
    private final long ACCESS_TOKEN_VALIDITY_TIME = 604800 * 1000L; // 1주일
    private String secret_key;
    private final RedisUtil redisUtil;

    public JwtService(@Value("${jwt.secret}") String secret_key, RedisUtil redisUtil) {
        this.secret_key = secret_key;
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    protected void init() {
        log.info("secret_key Base64 인코딩");
        log.info("Original Secret_Key : " + secret_key);
        this.secret_key = Base64.getEncoder().encodeToString(secret_key.getBytes(StandardCharsets.UTF_8));
        log.info("Encoded Base64 Secret_Key : " + secret_key);
        log.info("secret_key 초기화 완료");
    }

    public String extractAccessToken(HttpServletRequest request) throws IOException, SecurityException {
        return request.getHeader(HEADER_KEY);
    }

    public String extractLoginId(String jwt) {
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(jwt).getBody().getSubject();
    }

    public Long getExpireTime(String token) {
        Date expirationDate = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody().getExpiration();
        long now = new Date().getTime();
        return ((expirationDate.getTime() - now) % 1000) + 1;
    }


    public String generateToken(String loginId, String role) {
        Claims claims = Jwts.claims().setSubject(loginId);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_TIME))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    public Authentication getAuthentication(UserDetails userDetails) {
        log.info("토큰 인증 정보 조회 시작");
        log.info("UserDetails UserName : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token);
            log.info("토큰 유효 체크 완료");

            if (redisUtil.hasKeyBlackList(token)) {
                throw new RuntimeException("로그아웃");
            }

            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (SignatureException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 서명");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰");
        } catch (Exception e) {
            log.info("이상한 JWT 토큰");
        }
        return false;
    }
}
