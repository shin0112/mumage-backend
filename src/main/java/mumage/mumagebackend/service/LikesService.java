//package mumage.mumagebackend.service;
//
//import lombok.RequiredArgsConstructor;
//import mumage.mumagebackend.domain.Likes;
//import mumage.mumagebackend.domain.Posts;
//import mumage.mumagebackend.domain.User;
//import mumage.mumagebackend.repository.LikesRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class LikesService {
//
//    private final LikesRepository likesRepository;
//
//    @Transactional
//    public void save(Likes likes){
//        Optional<Likes> byPostsAndUser = likesRepository.findByPostsAndUser(likes.getPosts(), likes.getUser());
//        if(byPostsAndUser.isPresent()) throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
//
//        likesRepository.save(likes);
//
//        likes.getUser().getLikesList().add(likes);
//        likes.getPosts().getLikesList().add(likes);
//    }
//
//    @Transactional
//    public void delete(Likes likes) {
//
//        likes.getUser().getLikesList().remove(likes);
//        likes.getPosts().getLikesList().remove(likes);
//
//        likesRepository.delete(likes);
//    }
//
//    // 게시글과 유저 정보로 좋아요 눌렀는지 확인
//    public Optional<Likes> findByPostsAndUser(Posts posts, User user){
//        return likesRepository.findByPostsAndUser(posts, user);
//    }
//
//    // 게시글에 좋아요 누른 user id 목록 반환
//    public Set<Long> findLikeUserIdList(Posts posts){
//        Set<Likes> byPosts = posts.getLikesList(); // posts에 fetch join을 통해 미리 가져온 좋아요 목록을 활용한다.
//        Set<Long> likesUserIdList = new HashSet<>();
//        for (Likes likes : byPosts) {
//            likesUserIdList.add(likes.getUser().getUserId());
//        }
//        return likesUserIdList;
//    }
//}
