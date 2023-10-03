package mumage.mumagebackend.service;

import mumage.mumagebackend.domain.Member;
import mumage.mumagebackend.dto.MemberJoinDto;
import mumage.mumagebackend.exception.CustomException;
import mumage.mumagebackend.exception.ErrCode;
import mumage.mumagebackend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /*
    회원 가입 : create
     */
    public Member join(MemberJoinDto memberJoinDto) {

        Member member = new Member();
        member.setMemberId(memberJoinDto.getMemberId());
        member.setPassword(memberJoinDto.getPassword());
        member.setName(memberJoinDto.getName());
        member.setNickname(memberJoinDto.getNickname());

        // 중복 조회
        validateDuplicateId(member); // 아이디
        validateDuplicateNickname(member); // 닉네임


        memberRepository.save(member);
        return member;
    }

    /*
    회원 탈퇴 : delete
     */
    public Boolean withdraw(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            memberRepository.delete(member.get());
            return true;
        } else {
            return false;
        }
    }

    /*
    중복 조회
     */

    // 아이디
    private void validateDuplicateId(Member member) {
        memberRepository.findByMemberId(member.getMemberId()).ifPresent(m -> {
            throw new CustomException(ErrCode.DUPLICATED_ID, HttpStatus.BAD_REQUEST);
        });
    }

    // 닉네임
    private void validateDuplicateNickname(Member member) {
        memberRepository.findByNickname(member.getNickname()).ifPresent(m -> {
            throw new CustomException(ErrCode.DUPLICATED_NICKNAME, HttpStatus.BAD_REQUEST);
        });
    }


    /*
    전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /*
    특정 회원 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
