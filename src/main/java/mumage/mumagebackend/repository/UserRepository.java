package mumage.mumagebackend.repository;

import mumage.mumagebackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByName(String name);

    Optional<User> findByNickname(String nickname);

}
