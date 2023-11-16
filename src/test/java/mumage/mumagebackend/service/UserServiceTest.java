package mumage.mumagebackend.service;

import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.MessageDto;
import mumage.mumagebackend.dto.UserJoinDto;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.exception.RestControllerAdvisor;
import mumage.mumagebackend.repository.UserRepository;
import org.apache.coyote.Response;
import org.aspectj.bridge.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    public void 회원가입() throws Exception {

        // post로 memberJoinDto를 받는다.
        UserJoinDto userJoinDto = new UserJoinDto();
        userJoinDto.setJoinId("mumage_id");
        userJoinDto.setPassword("mumage_pw");
        userJoinDto.setName("mumage");
        userJoinDto.setNickname("mumage_nn");

        // join 실행 / join return -> User / 결과값에 getId()로 id를 가져온다.
        Long saveId = userService.join(userJoinDto).getUserId();

        // 저장된 회원 정보 sout
        System.out.println("회원아이디 = " + userJoinDto.getJoinId());
        System.out.println("회원이름 = " + userJoinDto.getName());
        System.out.println("회원닉네임 = " + userJoinDto.getNickname());
        System.out.println("회원패스워드 = " + userJoinDto.getPassword());

        // saveId로 member를 찾아서 저장한 member와 같은지 확인한다.
        User findUser = userRepository.findById(saveId).get();
        assertEquals(userJoinDto.getName(), findUser.getName());

    }

}