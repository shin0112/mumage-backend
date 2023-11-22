package mumage.mumagebackend.repository;


import mumage.mumagebackend.domain.Likes;
import mumage.mumagebackend.domain.Posts;
import mumage.mumagebackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostsAndUser(Posts posts, User user); // 게시글과 작성자로 좋아요 반환
    Set<Likes> findByPosts(Posts posts); // 게시글에 누른 좋아요 목록 반환

}
