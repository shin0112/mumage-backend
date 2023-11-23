package mumage.mumagebackend.repository;


import mumage.mumagebackend.domain.Posts;
import mumage.mumagebackend.domain.Song;
import mumage.mumagebackend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    // fetch join으로 게시글과 작성자, 좋아요 목록까지 한 번에 가져온다.
    // 좋아요 목록은 empty가 될 수 있기 때문에 left join을 해야한다.
    @Query("select p From Posts p join fetch p.user left join fetch p.likesList where p.postId = ?1")
    Posts findByPostId(Long PostId);

    Page<Posts> findAllByUser(User user, Pageable pageable); // User가 작성한 게시글 목록 pageable로 페이징 후 반환

    @Query(value = "SELECT l.posts FROM Likes l WHERE l.user.userId = ?1",
            countQuery = "SELECT count(l.posts) FROM Likes l WHERE l.user.userId = ?1")
    Page<Posts> getLikeListPageable(Long userId, Pageable pageable); // user가 좋아요 누른 게시글 목록 페이징 후 반환

    @Query(value = "SELECT p FROM Posts p WHERE p.user in ?1",
            countQuery = "SELECT count(p) FROM Posts p WHERE p.user in ?1")
    Page<Posts> getPostsCntByUseret(Set<User> User, Pageable pageable); // user 목록에 있는 user가 작성한 게시글 목록 페이징 후 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.user in ?1")
    Long getPostsCntByUseret(Set<User> User); // user 목록에 있는 user가 작성한 모든 게시글의 수 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.user = ?1")
    Long getPostsCntByUser(User user); // user가 작성한 게시글 목록 반환

    @Query(value = "SELECT count(p) FROM Posts p WHERE p.user = ?1 and p.createdDate >= ?2 and p.createdDate <= ?3")
    Long getPostsCntByUserToday(User user, LocalDateTime todayDate, LocalDateTime tomorrowDate); // user가 오늘 작성한 글의 수 반환

    @Query(value = "SELECT p, count(l) as like_count FROM Posts p LEFT JOIN p.likesList l WHERE p.song IN ?1 GROUP BY p ORDER BY like_count desc ")
    Page<Posts> findBySongIn(Set<Song> songs, Pageable pageable); // songs 안에 있는 song 찾아서 반환, likesList로 정렬

    @Query(value = "SELECT p, count(l) as like_count FROM Posts p LEFT JOIN p.likesList l WHERE p.song = ?1 GROUP BY p ORDER BY like_count desc ")
    Page<Posts> findBySong(Song song, Pageable pageable); // songs 안에 있는 song 찾아서 반환, likesList로 정렬

    @Query(value = "SELECT p, count(l) as like_count FROM Posts p LEFT JOIN p.likesList l WHERE p.user = ?1 GROUP BY p ORDER BY like_count desc ")
    Page<Posts> findByUser(User user, Pageable pageable); // user로 검색

    @Query(value = "SELECT p, count(l) as like_count FROM Posts p LEFT JOIN p.likesList l WHERE p.user in ?1 GROUP BY p ORDER BY like_count desc ")
    Page<Posts> findByUsers(Set<User> users, Pageable pageable); // users 내부의 user로 검색

}

