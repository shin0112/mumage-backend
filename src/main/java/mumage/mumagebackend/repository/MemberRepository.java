package mumage.mumagebackend.repository;

import mumage.mumagebackend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByName(String name);

    Optional<Member> findByNickname(String nickname);


}
