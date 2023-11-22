package mumage.mumagebackend.controller;

import lombok.RequiredArgsConstructor;
import mumage.mumagebackend.domain.Likes;
import mumage.mumagebackend.domain.Posts;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.FollowListDto;
import mumage.mumagebackend.exception.NoResultException;
import mumage.mumagebackend.service.FollowService;
import mumage.mumagebackend.service.LikesService;
import mumage.mumagebackend.service.PostsService;
import mumage.mumagebackend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final UserService UserService;
    private final PostsService postsService;
    private final LikesService likesService;
    private final FollowService followService;

    // 특정 게시물에 좋아요 컨트롤러
    @GetMapping("/likes/read/{postId}&{userId}")
    public String saveLikesGet(@PathVariable("postId") Long postId,
                               @PathVariable("userId") Long userId) throws NoResultException {
        User user = UserService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다."));
        Likes likes = Likes.builder() // 유저와 게시글 id에 맞게 like 객체 생성
                .posts(posts)
                .user(user)
                .build();

        likesService.save(likes);

        return "redirect:/post/read/{postId}";
    }

    // 특정 게시물에 좋아요 취소 컨트롤러
    @GetMapping("/likes/delete/{postId}&{userId}")
    public String deleteLikesGet(@PathVariable("postId") Long postId,
                                 @PathVariable("userId") Long userId) throws NoResultException {
        User user = UserService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Posts posts = postsService.findById(postId).orElseThrow(() -> new NoResultException("잘못된 Post 정보 입니다."));
        Likes likes = likesService.findByPostsAndUser(posts, user).orElseGet(Likes::new); // 게시글에 user가 누른 좋아요 객체 가져오기

        likesService.delete(likes); // 해당 좋아요 정보 db에서 삭제

        return "redirect:/post/read/{postId}";
    }

    // 게시글에 좋아요 누른 사람 목록 보여주기
    @GetMapping("/likes/userList/{postId}&{userId}")
    public String likeUserListGet(@PathVariable("postId") Long postId,
                                  @PathVariable("userId") Long userId,
                                  Model model) throws NoResultException {
        Posts posts = postsService.findById(postId).orElseGet(Posts::new);
        User loginUser = UserService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        Set<Long> likeUserIdList = likesService.findLikeUserIdList(posts); // 게시글에 좋아요 누른 user id 목록 가져오기

        Set<FollowListDto> userDto = new HashSet<>();
        for (Long likeUserId : likeUserIdList) {
            User user = UserService.findById(likeUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다.")); // 좋아요 누른 user id로 user 가져오기
            userDto.add(followService.convertDto(loginUser, user)); // 좋아요 누른 user 정보들 dto로 전환, 팔로우 여부까지 표시
        }

        model.addAttribute("userDto",userDto);
        model.addAttribute("postId",postId);

        return "likes/likeUserList";
    }



}

