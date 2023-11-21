//package mumage.mumagebackend.repository;
//
//
//import mumage.mumagebackend.domain.User;
//import mumage.mumagebackend.domain.Posts;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jdbc.repository.query.Modifying;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Repository
//public interface PostsRepository extends JpaRepository<Posts, Long> {
//
//    // fetch join으로 게시글과 작성자, 좋아요 목록까지 한 번에 가져온다.
//    // 좋아요 목록은 empty가 될 수 있기 때문에 left join을 해야한다.
//    @Query("select p From Posts p join fetch p.User left join fetch p.likesList where p.id = ?1")
//    Posts findByPostId(Long PostId);
//
//    Page<Posts> findAllByUser(User user, Pageable pageable); // User가 작성한 게시글 목록 pageable로 페이징 후 반환
//
//    @Query(value = "SELECT l.posts FROM Likes l WHERE l.User.id = ?1",
//            countQuery = "SELECT count(l.posts) FROM Likes l WHERE l.User.id = ?1")
//    Page<Posts> getLikeListPageable(Long userId, Pageable pageable); // user가 좋아요 누른 게시글 목록 페이징 후 반환
//
//    @Query(value = "SELECT p FROM Posts p WHERE p.User in ?1",
//            countQuery = "SELECT count(p) FROM Posts p WHERE p.User in ?1")
//    Page<Posts> getPostsCntByUseret(Set<User> User, Pageable pageable); // user 목록에 있는 user가 작성한 게시글 목록 페이징 후 반환
//
//    @Query(value = "SELECT count(p) FROM Posts p WHERE p.User in ?1")
//    Long getPostsCntByUseret(Set<User> User); // user 목록에 있는 user가 작성한 모든 게시글의 수 반환
//
//    @Query(value = "SELECT count(p) FROM Posts p WHERE p.User = ?1")
//    Long getPostsCntByUser(User user); // user가 작성한 게시글 목록 반환
//
//
//    @Query(value = "SELECT count(p) FROM Posts p WHERE p.User = ?1 and p.createdDate >= ?2 and p.createdDate <= ?3")
//    Long getPostsCntByUserToday(User user, LocalDateTime todayDate, LocalDateTime tomorrowDate); // user가 오늘 작성한 글의 수 반환
//}
