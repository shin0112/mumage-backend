package mumage.mumagebackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mumage.mumagebackend.Config.RedisUtil;
import mumage.mumagebackend.domain.Follow;
import mumage.mumagebackend.domain.Genre;
import mumage.mumagebackend.domain.Role;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.*;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.exception.ErrCode;
import mumage.mumagebackend.repository.GenreRepository;
import mumage.mumagebackend.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtservice;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtil redisUtil;

    /*
        회원 가입 : create
         */
    public User join(UserJoinDto userJoinDto) {

        User user = new User();
        user.setLoginId(userJoinDto.getJoinId());
        user.setPassword(passwordEncoder.encode(userJoinDto.getPassword()));
        user.setName(userJoinDto.getName());
        user.setNickname(userJoinDto.getNickname());

        if (userJoinDto.getRole().equals("ROLE_ADMIN")) {
            user.setRole(Role.ROLE_ADMIN.getRole());
        } else {
            user.setRole(Role.ROLE_USER.getRole());
        }

        userRepository.save(user);
        return user;
    }

    /*
    회원 탈퇴 : delete
     */
    public boolean withdraw(Long id) {
        Optional<User> user = userRepository.findByUserId(id);
        if (user.isPresent()) {
            log.info("삭제");
            userRepository.delete(user.get());
            return true;
        } else {
            throw new CustomException(ErrCode.NOT_EXIST_USER, HttpStatus.NOT_FOUND);
        }
    }

    /*
    중복 조회
     */

    // 아이디
    public boolean existLoginId(String loginId) {
        Optional<User> byLoginId = userRepository.findByLoginId(loginId);
        if (byLoginId.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // 닉네임
    public boolean existNickname(String nickname) {
        Optional<User> byNickname = userRepository.findByNickname(nickname);
        if (byNickname.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public UserResponseDto update(Long userId, UserRequestDto userRequestDto) {

        Optional<User> oUser = userRepository.findByUserId(userId);

        if (oUser.isPresent()) {
            new NotFoundException("해당 유저 없음");
        }

        User user = oUser.get();

        if(userRequestDto.getPassword() != null)
            user.setPassword(userRequestDto.getPassword());
        if(userRequestDto.getName() != null)
            user.setName(userRequestDto.getName());
        if(userRequestDto.getNickname() != null)
            user.setNickname(userRequestDto.getNickname());

        Set<String> genres = userRequestDto.getGenres();
        Set<Genre> userGenres = new HashSet<Genre>();

        for (String genre : genres) {
            Optional<Genre> byGenreName = genreRepository.findByGenreName(genre);
            if (byGenreName.isEmpty())
                throw new CustomException(ErrCode.NOT_EXIST_GENRE, HttpStatus.NOT_FOUND);
            userGenres.add(byGenreName.get());
        }


        user.setGenres(userGenres);

        UserResponseDto response = UserResponseDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .password(user.getPassword())
                .name(user.getName()).nickname(user.getNickname()).email(user.getEmail()).profileUrl(user.getProfileUrl()).genres(user.getGenres())
                .build();

        return response;
    }

    public UserResponseDto read(Long userId) {

        Optional<User> user = userRepository.findByUserId(userId);

        UserResponseDto res = UserResponseDto.builder()
                .userId(user.get().getUserId())
                .loginId(user.get().getLoginId())
                .password(user.get().getPassword())
                .name(user.get().getName())
                .nickname(user.get().getNickname())
                .email(user.get().getEmail())
                .profileUrl(user.get().getProfileUrl())
                .build();
        return res;

    }
    /*
    전체 회원 조회
     */
    public List<User> findMembers() {
        return userRepository.findAll();
    }

    /*
    특정 회원 조회
     */
    public Optional<User> findById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public Set<Genre> findByGenres(Long memberId) {
        Optional<User> user = findById(memberId);
        return user.get().getGenres();
    }

    public Set<Follow> findByFollowings(Long userId) {
        Optional<User> user = findById(userId);
        return user.get().getFollowing();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("사용자 아이디 : " + username);
        return userRepository.findByLoginId(username).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없음"));
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        // loginId 동일 시, 유저 정보 반환
        User user = userRepository.findByLoginId(loginRequestDto.getLoginId())
                .orElseThrow(() -> new CustomException(ErrCode.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND));

        // 패스워드 불일치 시, 에러 발생
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
        } else {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setLoginId(user.getLoginId());
            loginResponseDto.setJWT(jwtservice.generateToken(user.getLoginId(), user.getRole()));

            redisTemplate.opsForValue().set("RT:" +user.getLoginId(), loginResponseDto.getJWT());
            log.info("로그인");

            return loginResponseDto;
        }

    }

    public void logout(String token) {

//        if (!jwtservice.validateToken(token)) {
//            throw new IllegalStateException("로그아웃");
//        }

        Authentication authentication = jwtservice.getAuthentication(loadUserByUsername(jwtservice.extractLoginId(token)));

        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

            redisUtil.setBlackList(token, "accessToken", 5);

    }

    public String test(Principal principal) {
        Optional<User> user = userRepository.findByLoginId(principal.getName());
        if(user.isPresent()){
            return new String("test");
        } else return new String("fault test");
    }
}
