package mumage.mumagebackend.controller;

import mumage.mumagebackend.dto.MessageDto;
import mumage.mumagebackend.dto.UserJoinDto;
import mumage.mumagebackend.repository.UserRepository;
import mumage.mumagebackend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @AfterEach
    public void tearDown() throws Exception {
        userRepository.deleteAll();
    }

    @Test
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
    void withdrawMember() {
    }
}