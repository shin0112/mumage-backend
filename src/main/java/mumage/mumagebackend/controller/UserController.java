package mumage.mumagebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.LoginRequestDto;
import mumage.mumagebackend.dto.LoginResponseDto;
import mumage.mumagebackend.dto.MessageDto;
import mumage.mumagebackend.dto.UserJoinDto;
import mumage.mumagebackend.dto.UserRequestDto;
import mumage.mumagebackend.dto.UserResponseDto;
import mumage.mumagebackend.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<MessageDto> joinMember(@Valid @RequestBody UserJoinDto userJoinDto) {

        User user = userService.join(userJoinDto);
        MessageDto messageDto = new MessageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        messageDto.setStatus(HttpStatus.OK.value());
        messageDto.setMessage("회원 가입 성공");
        messageDto.setData(user);
        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

    }

    @PostMapping(value = "/user/login")
    public ResponseEntity<MessageDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        LoginResponseDto responseDto = userService.login(loginRequestDto);
        MessageDto messageDto = new MessageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        messageDto.setStatus(HttpStatus.OK.value());
        messageDto.setMessage("로그인 성공");
        messageDto.setData(responseDto);
        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

    }

    @PostMapping("/user/logout")
    public ResponseEntity<MessageDto> logout(@RequestHeader HttpHeaders header) {

        userService.logout(header.getFirst("Authorization"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity<>(MessageDto.builder()
                .status(200)
                .message("로그아웃 성공")
                .data(null).build(), headers, HttpStatus.OK);

    }

    @GetMapping(value = "/signup/id/{loginId}")
    public ResponseEntity<MessageDto> validateDuplicateLoginId(@PathVariable String loginId) {

        MessageDto messageDto = new MessageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if (userService.existLoginId(loginId)) {
            boolean existLoginId = false;

            messageDto.setStatus(HttpStatus.BAD_REQUEST.value());
            messageDto.setMessage("중복 아이디 존재");
            messageDto.setData(existLoginId);
            return new ResponseEntity<>(messageDto, headers, HttpStatus.BAD_REQUEST);

        } else {
            boolean existLoginId = true;

            messageDto.setStatus(HttpStatus.OK.value());
            messageDto.setMessage("중복 아이디 없음");
            messageDto.setData(existLoginId);
            return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

        }
    }

    @GetMapping(value = "/signup/nickname/{nickname}")
    public ResponseEntity<MessageDto> validateDuplicateNickname(@PathVariable String nickname) {

        MessageDto messageDto = new MessageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if (userService.existNickname(nickname)) {
            boolean existLoginId = false;

            messageDto.setStatus(HttpStatus.BAD_REQUEST.value());
            messageDto.setMessage("중복 닉네임 존재");
            messageDto.setData(existLoginId);
            return new ResponseEntity<>(messageDto, headers, HttpStatus.BAD_REQUEST);

        } else {
            boolean existLoginId = true;

            messageDto.setStatus(HttpStatus.OK.value());
            messageDto.setMessage("중복 닉네임 없음");
            messageDto.setData(existLoginId);
            return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

        }
    }

    @GetMapping(value = "/test")
    public ResponseEntity<String> test(Principal principal) {
        log.info("principal : " + principal.getName());
        return new ResponseEntity<>(userService.test(principal), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<MessageDto> readUser(@PathVariable Long userId) {

        UserResponseDto response = userService.read(userId);
        MessageDto messageDto = MessageDto.builder()
                .status(HttpStatus.OK.value())
                .message("유저 정보 반환 성공")
                .data(response).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<MessageDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        UserResponseDto response = userService.update(userId, userRequestDto);
        MessageDto messageDto = MessageDto.builder()
                .status(HttpStatus.OK.value())
                .message("유저 정보 수정 성공")
                .data(response).build();

        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

    }

    @DeleteMapping(value = "/user/{userId}")
    public ResponseEntity<MessageDto> withdrawMember(@PathVariable(value = "userId") Long userId) {

        MessageDto messageDto = new MessageDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        userService.withdraw(userId);
        messageDto.setStatus(HttpStatus.OK.value());
        messageDto.setMessage("회원 탈퇴 성공");
        return new ResponseEntity<>(messageDto, headers, HttpStatus.OK);

    }

}
