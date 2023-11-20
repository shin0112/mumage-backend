//package mumage.mumagebackend.service;
//
//import mumage.mumagebackend.domain.Posts;
//import mumage.mumagebackend.domain.User;
//import mumage.mumagebackend.dto.PostDto;
//import mumage.mumagebackend.repository.PostsRepository;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class PostsService {
//
//    private final PostsRepository postsRepository;
//    private final LikesService likesService;
//    private final FollowService followService;
//    private final UserService UserService;
//
//    @Transactional
//    public void save(Posts posts){
//        postsRepository.save(posts);
//        posts.getUser().getPostsList().add(posts);
//    }
//
//    @Transactional
//    public void update(Posts posts) {
//        postsRepository.save(posts);
//    }
//
//    @Transactional
//    public void delete(Posts posts){
//        posts.getUser().getPostsList().remove(posts); //유저가 작성한 게시글 목록에서 이 글 삭제
//        postsRepository.delete(posts);
//    }
//
//    //페치 조인으로 게시글, 사용자, 좋아요까지 가져오기
//    public Posts findByPostId(Long postId){
//        return postsRepository.findByPostId(postId);
//    }
//
//    //data jpa 기본 메서드
//    public Optional<Posts> findById(Long id){
//        return postsRepository.findById(id);
//    }
//
//    // db에 저장된 모든 게시글의 수를 반환하는 메서드
//    public Long getPostsCount(){
//        return postsRepository.count();
//    }
//
//    //index 페이지용 모든 게시글 목록
//    public List<PostDto> getPostList(int page, int contentPageCnt) {
//        Page<Posts> posts = postsRepository.findAll(PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
//        List<Posts> postsList = posts.getContent();
//        List<PostDto> dtoList = new ArrayList<>();
//        for (Posts post : postsList) {
//            dtoList.add(this.convertToDto(post));
//        }
//        return dtoList;
//    }
//
//    //유저가 작성한 게시글 목록
//    public List<PostDto> getUserPostList(User user, int page, int contentPageCnt){
//        Page<Posts> posts = postsRepository.findAllByUser(user, PageRequest.of(page-1,contentPageCnt,Sort.by("createdDate").descending()));
//        List<Posts> postsList = posts.getContent();
//        List<PostDto> dtoList = new ArrayList<>();
//        for (Posts post : postsList) {
//            dtoList.add(this.convertToDto(post));
//        }
//        return dtoList;
//    }
//
//    //유저가 좋아요 누른 게시글 목록
//    public List<PostDto> getUserLikeListPage(Long userId, int page, int contentPageCnt){
//        Page<Posts> posts = postsRepository.getLikeListPageable(userId,PageRequest.of(page-1,contentPageCnt,Sort.by("posts.createdDate").descending()));
//        List<Posts> content = posts.getContent();
//        List<PostDto> dtoList = new ArrayList<>();
//        for (Posts posts1 : content) {
//            dtoList.add(this.convertToDto(posts1));
//        }
//        return dtoList;
//    }
//
//    //팔로우 한 사람들의 게시글 목록
//    public List<PostDto> getFollowListPage(Long userId, int page, int contentPageCnt){
//        Set<User> User = followService.getFollowing(userId);
//        Page<Posts> posts = postsRepository.getPostsCntByUseret(User, PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
//        List<Posts> content = posts.getContent();
//        List<PostDto> dtoList = new ArrayList<>();
//        for (Posts posts1 : content) {
//            dtoList.add(this.convertToDto(posts1));
//        }
//        return dtoList;
//    }
//
//
//    // 유저가 팔로우하는 사람들의 모든 게시글 가져오기
//    public Long getFollowPostCount(Long userId){
//        Set<User> User = followService.getFollowing(userId);
//        return postsRepository.getPostsCntByUseret(User);
//    }
//
//    // 유저가 작성한 총 게시글 수 구하는 함수
//    public Long getPostsCntByUser(User user){
//        return postsRepository.getPostsCntByUser(user);
//    }
//
//    // Posts 객체 생성해서 반환
//    public Posts makePost(@Valid PostDto postDto, Long userId, String picture){
//        User user = UserService.findById(userId).orElseGet(User::new);
//        return Posts.builder() // 게시글 생성
//                .content(postDto.getContent())
//                .imageUrl(picture)
//                .user(user)
//                .build();
//    }
//
//    //게시글 dto로 전환
//    public PostDto convertToDto(Posts posts){
//
//        return PostDto.builder()
//                .postId(posts.getPostId())
//                .singer(posts.getSong().getSinger())
//                .songName(posts.getSong().getSongName())
//                .content(posts.getContent())
//                .imageUrl(posts.getImageUrl())
//                .createdDate(posts.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
//                .userId(posts.getUser().getUserId())
//                .userName(posts.getUser().getName())
//                .userPicture(posts.getUser().getProfileUrl())
//                .likeUserIdList(likesService.findLikeUserIdList(posts))
//                .build();
//    }
//
//}
