package mumage.mumagebackend.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mumage.mumagebackend.domain.*;
import mumage.mumagebackend.dto.PostDto;
import mumage.mumagebackend.dto.PostsResponseDto;
import mumage.mumagebackend.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final SongRepository songRepository;
    private final GenreRepository genreRepository;
    private final FollowsRepository followsRepository;
    private final LikesService likesService;
    private final FollowService followService;
    private final UserService UserService;

    @Transactional
    public void save(Posts posts){
        postsRepository.save(posts);
        posts.getUser().getPostsList().add(posts);
    }

    @Transactional
    public void update(Posts posts) {
        postsRepository.save(posts);
    }

    @Transactional
    public void delete(Posts posts){
        posts.getUser().getPostsList().remove(posts); //유저가 작성한 게시글 목록에서 이 글 삭제
        postsRepository.delete(posts);
    }

    //페치 조인으로 게시글, 사용자, 좋아요까지 가져오기
    public Posts findByPostId(Long postId){
        return postsRepository.findByPostId(postId);
    }

    //data jpa 기본 메서드
    public Optional<Posts> findById(Long id){
        return postsRepository.findById(id);
    }

    // db에 저장된 모든 게시글의 수를 반환하는 메서드
    public Long getPostsCount(){
        return postsRepository.count();
    }

    //index 페이지용 모든 게시글 목록
    public List<PostDto> getPostList(int page, int contentPageCnt) {
        Page<Posts> posts = postsRepository.findAll(PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

    //유저가 작성한 게시글 목록
    public List<PostDto> getUserPostList(User user, int page, int contentPageCnt){
        Page<Posts> posts = postsRepository.findAllByUser(user, PageRequest.of(page-1,contentPageCnt,Sort.by("createdDate").descending()));
        List<Posts> postsList = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts post : postsList) {
            dtoList.add(this.convertToDto(post));
        }
        return dtoList;
    }

    //유저가 좋아요 누른 게시글 목록
    public List<PostDto> getUserLikeListPage(Long userId, int page, int contentPageCnt){
        Page<Posts> posts = postsRepository.getLikeListPageable(userId,PageRequest.of(page-1,contentPageCnt,Sort.by("posts.createdDate").descending()));
        List<Posts> content = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts posts1 : content) {
            dtoList.add(this.convertToDto(posts1));
        }
        return dtoList;
    }

    //팔로우 한 사람들의 게시글 목록
    public List<PostDto> getFollowListPage(Long userId, int page, int contentPageCnt){
        Set<User> User = followService.getFollowing(userId);
        Page<Posts> posts = postsRepository.getPostsCntByUseret(User, PageRequest.of(page - 1, contentPageCnt, Sort.by("createdDate").descending()));
        List<Posts> content = posts.getContent();
        List<PostDto> dtoList = new ArrayList<>();
        for (Posts posts1 : content) {
            dtoList.add(this.convertToDto(posts1));
        }
        return dtoList;
    }


    // 유저가 팔로우하는 사람들의 모든 게시글 가져오기
    public Long getFollowPostCount(Long userId){
        Set<User> User = followService.getFollowing(userId);
        return postsRepository.getPostsCntByUseret(User);
    }

    // 유저가 작성한 총 게시글 수 구하는 함수
    public Long getPostsCntByUser(User user){
        return postsRepository.getPostsCntByUser(user);
    }

    // Posts 객체 생성해서 반환
    public Posts makePost(@Valid PostDto postDto, Long userId, String picture){
        User user = UserService.findById(userId).orElseGet(User::new);
        return Posts.builder() // 게시글 생성
                .content(postDto.getContent())
                .imageUrl(picture)
                .user(user)
                .song(songRepository.findBySongNameAndSinger(postDto.getSongName(), postDto.getSinger()).get())
                .build();
    }

    //게시글 dto로 전환
    public PostDto convertToDto(Posts posts){

        return PostDto.builder()
                .postId(posts.getPostId())
                .singer(posts.getSong().getSinger())
                .songName(posts.getSong().getSongName())
                .trackUrl(posts.getSong().getTrackUrl())
                .albumName(posts.getSong().getAlbumName())
                .content(posts.getContent())
                .imageUrl(posts.getImageUrl())
                .createdDate(posts.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .userId(posts.getUser().getUserId())
                .userName(posts.getUser().getName())
                .userPicture(posts.getUser().getProfileUrl())
                .likeUserIdList(likesService.findLikeUserIdList(posts))
                .build();
    }

    public Page<Posts> readPostsByGenresSortingLikes(Set<Genre> genres, Pageable pageable) {

        Set<Song> songs = new HashSet<>();
        for (Genre genre : genres) {
            Set<Song> byGenreIn = songRepository.findByGenreIn(genre);
            for (Song song : byGenreIn) {
                songs.add(song);
            }
        }

        return postsRepository.findBySongIn(songs, pageable);

    }

    public Page<Posts> readPostsByFollowsSortingLikes(Set<Follow> follows, Pageable pageable) {

        Set<User> users = new HashSet<>();
        for (Follow follow : follows) {
            users.add(follow.getTo());
        }

        return postsRepository.findByUsers(users, pageable);
    }

    public Page<Posts> searchPostsByGenre(String genreName, Pageable pageable) {

        Set<Song> byGenreIn = songRepository.findByGenreIn(genreRepository.findByGenreName(genreName).get());
        return postsRepository.findBySongIn(byGenreIn, pageable);

    }

    public Page<Posts> searchPostsBySinger(String singer, Pageable pageable) {

        Set<Song> bySinger = songRepository.findBySinger(singer);
        return postsRepository.findBySongIn(bySinger, pageable);

    }

    public Page<Posts> searchPostsBySongName(String songName, Pageable pageable) {

        Set<Song> bySongName = songRepository.findBySongName(songName);
        return postsRepository.findBySongIn(bySongName, pageable);

    }

    public Page<Posts> searchPostsByUserNickname(String nickname, Pageable pageable) {

        return postsRepository.findByUser(userRepository.findByNickname(nickname).get(), pageable);

    }
}
