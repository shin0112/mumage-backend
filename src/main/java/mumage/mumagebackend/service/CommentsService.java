//package mumage.mumagebackend.service;
//
//import lombok.RequiredArgsConstructor;
//import mumage.mumagebackend.domain.Comments;
//import mumage.mumagebackend.repository.CommentsRepository;
//import mumage.mumagebackend.domain.Posts;
//import mumage.mumagebackend.domain.User;
//import mumage.mumagebackend.dto.CommentDto;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//@Service
//public class CommentsService {
//
//    private final CommentsRepository commentsRepository;
//    private final PostsService postsService;
//    private final UserService userService;
//
//    //유저, 게시글 정보 가져와서 댓글 생성 -> db에 저장
//    @Transactional
//    public void save(Long postId, Long userId, CommentDto commentDto) {
//
//        Posts posts = postsService.findById(postId).orElseGet(Posts::new);
//        User user = userService.findById(userId).orElseGet(User::new);
//
//        Comments comments = Comments.builder()
//                .content(commentDto.getComment())
//                .user(user)
//                .posts(posts)
//                .build();
//
//        commentsRepository.save(comments);
//
//        posts.getCommentsList().add(comments);
//        user.getCommentsList().add(comments);
//    }
//
//    // 아이디에 해당하는 댓글 db에서 삭제
//    @Transactional
//    public void delete(Long commentId){
//        Comments comments = commentsRepository.findById(commentId).orElseGet(Comments::new);
//
//        comments.getPosts().getCommentsList().remove(comments);
//        comments.getUser().getCommentsList().remove(comments);
//
//        commentsRepository.delete(comments);
//    }
//
//    //id로 댓글 가져오기
//    public Optional<Comments> findById(Long commentId){
//        return commentsRepository.findById(commentId);
//    }
//
//    //해당 게시글에 있는 댓글 리스트 가져오기
//    public List<Comments> findCommentsByPosts(Posts posts){
//        return commentsRepository.findCommentsByPosts(posts);
//    }
//
//    //해당 게시글에 있는 댓글 리스트 가져오기
//    public Set<Comments> findCommentsAndUserByPosts(Posts posts){
//        return commentsRepository.findCommentsAndUserByPosts(posts);
//    }
//
//    //댓글 dto로 전환 후 반환
//    public CommentDto convertToDto(Comments comments){
//        return CommentDto.builder()
//                .comment(comments.getContent())
//                .commentId(comments.getId())
//                .userId(comments.getUser().getUserId())
//                .userName(comments.getUser().getName())
//                .userPicture(comments.getUser().getProfileUrl())
//                .createdDate(comments.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
//                .build();
//    }
//}
