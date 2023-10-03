package mumage.mumagebackend.controller;

import jakarta.validation.Valid;
import mumage.mumagebackend.domain.Member;
import mumage.mumagebackend.dto.MemberJoinDto;
import mumage.mumagebackend.repository.MemberRepository;
import mumage.mumagebackend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(value = "/api/signup")
    public Member joinMember(@Valid @RequestBody MemberJoinDto memberJoinDto) {

        Member member = memberService.join(memberJoinDto);
        return member;

    }

    @DeleteMapping(value = "/api/user/{id}")
    public ResponseEntity<Void> withdrawMember(@PathVariable Long id) {
        if (memberService.withdraw(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
