package mumage.mumagebackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.MessageDto;
import mumage.mumagebackend.dto.UserJoinDto;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.repository.UserRepository;
import mumage.mumagebackend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    @Tag("user_signup")
    void 회원가입() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("회원 가입 성공");

    }

    @Test
    @Tag("user_signup")
    void 회원가입_유효하지않은아이디() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId!!");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{joinId=\"^[0-9a-zA-Z]*$\"와 일치해야 합니다, 입력된 값 : [" + userJoinDto.getJoinId() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_유효하지않은비밀번호() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw//");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{password=\"^[0-9a-zA-Z!@#$%^&+=]*$\"와 일치해야 합니다, 입력된 값 : [" + userJoinDto.getPassword() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_유효하지않은이름() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지ss");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{name=\"^[a-zA-Z]+|[가-힣]+$\"와 일치해야 합니다, 입력된 값 : [" + userJoinDto.getName() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_유효하지않은닉네임() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅!!!");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{nickname=\"^[0-9a-zA-Z가-힣]*$\"와 일치해야 합니다, 입력된 값 : [" + userJoinDto.getNickname() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_범위가넘어선아이디() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId000000000000");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{joinId=크기가 5에서 15 사이여야 합니다, 입력된 값 : [" + userJoinDto.getJoinId() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_범위가넘어선비밀번호() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw000000000");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{password=크기가 8에서 15 사이여야 합니다, 입력된 값 : [" + userJoinDto.getPassword() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_범위가넘어선이름() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지안녕하십니까반가워요");
        userJoinDto.setNickname("뮤미지화이팅");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{name=크기가 2에서 10 사이여야 합니다, 입력된 값 : [" + userJoinDto.getName() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_signup")
    void 회원가입_범위가넘어선닉네임() {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumageId");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅000000000000000");

        String url = "http://localhost:" + port + "/signup";
        ResponseEntity<MessageDto> responseEntity = restTemplate.postForEntity(url, userJoinDto, MessageDto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        String data = "{nickname=크기가 1에서 15 사이여야 합니다, 입력된 값 : [" + userJoinDto.getNickname() + "]}";
        assertThat(responseEntity.getBody().getData().toString()).isEqualTo(data);

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원정보_반환() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);
        usertest.setEmail("mumage@naver.com");
        usertest.setProfileUrl("profileUrl");

        mockMvc.perform(get("/user/" + usertest.getUserId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.userId", usertest.getUserId()).exists())
                .andExpect(jsonPath("data.loginId", usertest.getLoginId()).exists())
                .andExpect(jsonPath("data.password", usertest.getPassword()).exists())
                .andExpect(jsonPath("data.name", usertest.getName()).exists())
                .andExpect(jsonPath("data.nickname", usertest.getNickname()).exists())
                .andExpect(jsonPath("data.email", usertest.getEmail()).exists())
                .andExpect(jsonPath("data.profileUrl", usertest.getProfileUrl()).exists());

    }


    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_비밀번호() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "passwordtest!");
        req.put("name", "뮤미지");
        req.put("nickname", "뮤미지화이팅");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.password").value(req.get("password")));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이름() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "mumagepw");
        req.put("name", "이름테스트");
        req.put("nickname", "뮤미지화이팅");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk()).andExpect(jsonPath("data.name").value(req.get("name")));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_닉네임() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "mumagepw");
        req.put("name", "뮤미지");
        req.put("nickname", "닉네임테스트");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk()).andExpect(jsonPath("data.nickname").value(req.get("nickname")));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이메일() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "mumagepw");
        req.put("name", "뮤미지");
        req.put("nickname", "닉네임테스트");
        req.put("email", "mumage@naver.com");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk()).andExpect(jsonPath("data.email").value(req.get("mumage@naver.com")));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이미지() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "mumagepw");
        req.put("name", "뮤미지");
        req.put("nickname", "닉네임테스트");
        req.put("profileUrl", "url");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isOk())
                .andExpect(jsonPath("data.profileUrl").value(req.get("url")));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이름and닉네임() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", usertest.getPassword());
        req.put("name", "안녕하심");
        req.put("nickname", "닉네임테스트");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name", "안녕하심").exists())
                .andExpect(jsonPath("data.nickname", "닉네임테스트").exists());

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이름and비밀번호() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "password!!");
        req.put("name", "안녕하심");
        req.put("nickname", usertest.getNickname());

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.name", "안녕하심").exists())
                .andExpect(jsonPath("data.password", "password!!").exists());

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_닉네임and비밀번호() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Map<String, String> req = new HashMap<>();
        req.put("password", "password!!");
        req.put("name", usertest.getName());
        req.put("nickname", "testnickname");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.nickname", "testnickname").exists())
                .andExpect(jsonPath("data.password", "password!!").exists());

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_비밀번호_특수문자() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("password", "반가워요///////!");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.password").value("must match \"^[0-9a-zA-Z!@#$%^&+=]*$\", 입력된 값 : [" + req.get("password") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이름_특수문자() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("name", "반가워요!");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.name").value("must match \"^[a-zA-Z]+|[가-힣]+$\", 입력된 값 : [" + req.get("name") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_닉네임_특수문자() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("nickname", "뮤미지ㅜㅜ!");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.nickname").value("must match \"^[0-9a-zA-Z가-힣]*$\", 입력된 값 : [" + req.get("nickname") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이메일_특수문자() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("password", usertest.getPassword());
        req.put("name", usertest.getName());
        req.put("nickname", usertest.getNickname());
        req.put("email", "email");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("data.email")
                        .value("must match \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\", 입력된 값 : [" + req.get("email") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_비밀번호_길이제한() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("password", "hi00000000000000000000000!");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.password").value("size must be between 8 and 15, 입력된 값 : [" + req.get("password") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_이름_길이제한() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("name", "helloooooooooooooooooooo");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.name").value("size must be between 2 and 10, 입력된 값 : [" + req.get("name") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_modify")
    void 회원수정_닉네임_길이제한() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);


        Map<String, String> req = new HashMap<>();
        req.put("nickname", "뮤미지000000000000000000000");

        String content = new ObjectMapper().writeValueAsString(req);

        mockMvc.perform(patch("/user/" + usertest.getUserId().toString()).contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isBadRequest()).andExpect(jsonPath("data.nickname").value("size must be between 1 and 15, 입력된 값 : [" + req.get("nickname") + "]"));

    }

    @Test
    @Tag("user_management")
    @Tag("user_delete")
    void 회원탈퇴_실패() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        Long id = 10000L;

        try {
            mockMvc.perform(delete("/user/" + id.toString())).andExpect(status().isNotFound());
        } catch (CustomException e) {
            Assertions.assertEquals("존재하지 않는 회원입니다.", e.getMessage());
        }

    }

    @Test
    @Tag("user_management")
    @Tag("user_delete")
    void 회원탈퇴_성공() throws Exception {

        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage");
        userJoinDto.setPassword("mumagepw");
        userJoinDto.setName("뮤미지");
        userJoinDto.setNickname("뮤미지화이팅");
        User usertest = userService.join(userJoinDto);

        mockMvc.perform(delete("/user/" + usertest.getUserId().toString())).andExpect(status().isOk()).andExpect(jsonPath("message").value("회원 탈퇴 성공"));

    }

}