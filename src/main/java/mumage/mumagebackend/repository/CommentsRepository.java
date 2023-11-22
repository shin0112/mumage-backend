package mumage.mumagebackend.repository;


import mumage.mumagebackend.domain.Comments;
import mumage.mumagebackend.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    //JpaRepository<Entity 클래스, PK 타입>를 상속하면 기본적인 데이터베이스 CRUD 메소드가 자동으로 생성, JPA 관련 메소드를 사용 가능

    List<Comments> findCommentsByPosts(Posts posts); // 특정 게시글에 해당하는 댓글 가져오기

    // fetch join ->  댓글을 작성한 user 정보까지
    @Query("select c from Comments c join fetch c.user where c.posts = ?1")
    Set<Comments> findCommentsAndUserByPosts(Posts posts);
}
