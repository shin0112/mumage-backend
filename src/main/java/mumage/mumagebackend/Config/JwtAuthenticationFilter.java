package mumage.mumagebackend.Config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mumage.mumagebackend.service.JwtService;
import mumage.mumagebackend.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtservice;
    private final UserService userService;
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // 로그인(/user/login)은 자동 필터 통과
        if (request.getRequestURI().equals("/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // req에서 토큰 추출 => jwt
        String jwt = jwtservice.extractAccessToken(request);
        log.info("token 값 유효성 체크 시작 토큰 : " + jwt);

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtservice.validateToken(jwt) && StringUtils.hasText(jwt)) {
            String loginId = jwtservice.extractLoginId(jwt);

            String isLogout = (String) redisTemplate.opsForValue().get(jwt);

            if (ObjectUtils.isEmpty(isLogout)) {
                UserDetails userDetails = userService.loadUserByUsername(loginId);
                Authentication authentication = jwtservice.getAuthentication(userDetails);

                log.info("auth 발급 성공");

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }

        }

        filterChain.doFilter(request, response);
    }

}
