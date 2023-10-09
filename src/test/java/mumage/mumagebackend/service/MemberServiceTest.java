package mumage.mumagebackend.service;

import mumage.mumagebackend.domain.Member;
import mumage.mumagebackend.dto.MemberJoinDto;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @Test
    public void 회원가입() throws Exception {

        // post로 memberJoinDto를 받는다.
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setMemberId("mumage_id");
        memberJoinDto.setPassword("mumage_pw");
        memberJoinDto.setName("mumage");
        memberJoinDto.setNickname("mumage_nn");

        // join 실행 / join return -> Member / 결과값에 getId()로 id를 가져온다.
        Long saveId = memberService.join(memberJoinDto).getId();

        // 저장된 회원 정보 sout
        System.out.println("회원아이디 = " + memberJoinDto.getMemberId());
        System.out.println("회원이름 = " + memberJoinDto.getName());
        System.out.println("회원닉네임 = " + memberJoinDto.getNickname());
        System.out.println("회원패스워드 = " + memberJoinDto.getPassword());

        // saveId로 member를 찾아서 저장한 member와 같은지 확인한다.
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(memberJoinDto.getName(), findMember.getName());

    }

    @Test
    public void 회원가입_아이디_중복() {

        MemberJoinDto member1 = new MemberJoinDto();
        member1.setMemberId("mumage_id");
        member1.setPassword("mumage_pw1");
        member1.setName("mumage1");
        member1.setNickname("mumage_nn1");

        MemberJoinDto member2 = new MemberJoinDto();
        member2.setMemberId("mumage_id");
        member2.setPassword("mumage_pw2");
        member2.setName("mumage2");
        member2.setNickname("mumage_nn2");

        memberService.join(member1);

        CustomException exception = assertThrows(CustomException.class, () -> memberService.join(member2));
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 아이디입니다.");

    }

    @Test
    public void 회원가입_중복닉네임() {

        MemberJoinDto member1 = new MemberJoinDto();
        member1.setMemberId("mumage_id1");
        member1.setPassword("mumage_pw1");
        member1.setName("mumage1");
        member1.setNickname("mumage_nn");

        MemberJoinDto member2 = new MemberJoinDto();
        member2.setMemberId("mumage_id2");
        member2.setPassword("mumage_pw2");
        member2.setName("mumage2");
        member2.setNickname("mumage_nn");

        memberService.join(member1);

        CustomException exception = assertThrows(CustomException.class, () -> memberService.join(member2));
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");

    }

    public void 닉네임변경() {

    }

    public void 닉네임변경_중복닉네임() {

    }

}