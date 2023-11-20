package mumage.mumagebackend.controller;

import mumage.mumagebackend.domain.Comments;
import mumage.mumagebackend.domain.Posts;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.CommentDto;
import mumage.mumagebackend.dto.PageDto;
import mumage.mumagebackend.dto.PostDto;
import mumage.mumagebackend.exception.NoResultException;
import mumage.mumagebackend.service.CommentsService;
import mumage.mumagebackend.service.PostsService;
import mumage.mumagebackend.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


//게시글
@Controller
@RequiredArgsConstructor
public class PostsController {

    private final UserService UserService;
    private final PostsService postsService;
    private final CommentsService commentsService;

    //글 전체 조회


    //게시글 작성 페이지 맵핑
    @GetMapping("/post/write")
    public String writeGet(@ModelAttribute("postDto") PostDto postDto) {
        return "post";
    }

    //게시글 작성 처리 컨트롤러
    @PostMapping("/post/write")
    public String writePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result,
                            @RequestParam("files") MultipartFile files,
                            HttpServletRequest request,
                            HttpSession session, Model model) throws IOException {

        if (result.hasErrors()) { // 내용에 오류가 있을 시 다시 작성
            return "post";
        }

        User loginUser = (User) session.getAttribute("loginUser"); // loginUser 세션에서 가져오기

        String picture = addFile(files);
        Posts posts = postsService.makePost(postDto, loginUser.getUserId(), picture); // Posts 객체 생성
        postsService.save(posts); // db에 저장

        return "redirect:/";
    }

    // fetch join 사용
    // 게시글 확인하는 컨트롤러
    @GetMapping("/post/read/{postId}")
    public String readGet(@PathVariable("postId") Long postId,
                          @ModelAttribute("commentDto") CommentDto commentDto,
                          Model model) {
        Posts posts = postsService.findByPostId(postId); // fetch join으로 작성자와 좋아요 목록까지 가져오기
        PostDto postDto = postsService.convertToDto(posts); // 게시글 보여줄 dto로 전환
        model.addAttribute("post", postDto); //model에 dto 추가

        Set<Comments> commentsList = commentsService.findCommentsAndUserByPosts(posts); // 댓글 목록과 사용자 정보 한번에 가져오기

        Set<CommentDto> commentDtoList = new HashSet<>(); //dto로 전환해서 반환할 list
        for (Comments comments : commentsList)
            commentDtoList.add(commentsService.convertToDto(comments)); // dto 전환
        model.addAttribute("commentsList", commentDtoList); // model에 댓글 dto 추가


        return "post/read";
    }

    // 게시글 업데이트 화면으로 연결하는 컨트롤러
    @GetMapping("/post/update/{postId}")
    public String updateGet(@PathVariable("postId") Long postId, @ModelAttribute("postDto") PostDto postDto, Model model) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 id로 찾아오기

        postDto.setContent(posts.getContent()); // dto에 post 정보 넣어서 기존 정보 제공

        model.addAttribute("postId", postId);

        return "post/update";
    }

    // 게시글 업데이트 처리 컨트롤러
    @PostMapping("/post/update/{postId}")
    public String updatePost(@PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("postDto") PostDto postDto, BindingResult result, Model model) throws NoResultException {

        if (result.hasErrors()) {
            model.addAttribute("postId", postId); //에러 있을시 다시 update 화면으로
            return "post/update";
        }

        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 찾아와서
        posts.update(postDto.getContent()); // update 한 후
        postsService.update(posts); // db에 저장

        return "redirect:/post/read/{postId}";
    }

//    // 좋아요 누른 목록 보여주는 controller
//    @GetMapping("/post/likeList/{userId}")
//    public String likeListGet(@RequestParam(value = "page", defaultValue = "1") int page,
//                              @PathVariable("userId") Long userId, Model model) throws NoResultException {
//        List<PostDto> postDtoList = postsService.getUserLikeListPage(userId, page, 5); // 유저가 좋아요 한 게시글 postDto로 전환 후 가져오기
//        model.addAttribute("postDtoList", postDtoList);
//
//        User user = Userervice.findById(userId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 유저가 누른 좋아요 size 구하기 위해 user 가져옴
//        PageDto pageDto = new PageDto(page, 5, user.getLikesList().size(), 5); // 페이지네이션
//        model.addAttribute("pageDto", pageDto);
//
//        return "post/likeList";
//    }

    // 피드(팔로잉) 팔로우 한 사람의 게시글 목록 가져오기
    @GetMapping("/post/followList/{userId}")
    public String followListGet(@RequestParam(value = "page", defaultValue = "1") int page,
                                @PathVariable("userId") Long userId, Model model) {
        List<PostDto> postDtoList = postsService.getFollowListPage(userId, page, 5); //유저가 팔로우 한 사람의 게시글 페이지에 맞게 5개 가져오기
        model.addAttribute("postDtoList", postDtoList);

        PageDto pageDto = new PageDto(page, 5, Math.toIntExact(postsService.getFollowPostCount(userId)), 5); //페이지네이션
        model.addAttribute("pageDto", pageDto);

        return "post/followList";
    }

    // 게시글 삭제 기능
    @GetMapping("/post/delete/{postId}")
    public String deleteGet(@PathVariable("postId") Long postId) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다.")); // 게시글 찾기

        removeFile(posts.getImageUrl());
        postsService.delete(posts); // 게시글 db에서 삭제

        return "post/delete";
    }

//    //추천 탭 (격자 형태), 사용자가 UI 선택 => 장르에 따른 게시물 표시 (장르별&페이징)
//    /* /{category_name}/page={pageNo}&orderby={orderCriteria} */
//    @GetMapping("/post/recommend/{genre_name}")
//    public String RecommendListGet(@PathVariable String genre_name,
//                                   @PathVariable("userId") Long userId,
//                                   @RequestParam(value="orderby",defaultValue = "id") String orderCriteria, //정렬 기준.  뷰에서 정렬 순서 파라미터를 보내주지 않아도 기본적으로 id(최신순) 으로 정렬
//                                   @RequestParam(value="page", defaultValue="0") int pageNo, //페이지 번호
//                                   Pageable pageable,
//                                   Model model){
////        //페이징 처리
////        Page<PostDto.ResponsePageDto> postPageList =
////                postsService.getPageList(pageable, pageNo, genre_name, orderCriteria); // 페이지 객체 생성
//
//    }


    // 권한 없이 접근 했을시
    @GetMapping("/post/noAuthority")
    public String noAuthorityGet() {
        return "post/noAuthority";
    } // 작성자가 아닐시 수정 & 삭제 불가능

    //파일 서버에 업로드
    public String addFile(MultipartFile files) throws IOException {
        if (files.isEmpty()) return null;
        //범용 고유 식별자 UUID 객체 생성
        UUID uuid = UUID.randomUUID();
        //UUID가 적용된 이름 지정 -> return
        String newName = uuid + "_" + files.getOriginalFilename();
        //파일 저장할 기본 디렉토리
        String baseDir = "C:\\git\\Mumage-backend\\uploads\\post\\";
        //지정된 경로에 파일을 저장
        files.transferTo(new File(baseDir + newName));
        return newName;
    }



    //파일 삭제
    public void removeFile(String path) {
        String originalPath = "C:\\git\\Mumage-backend\\uploads\\post\\" + path;
        File file = new File(originalPath);
        if (file.delete()) {
            System.out.println("delete Success");
        }
    }

}
