package mumage.mumagebackend.controller;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import mumage.mumagebackend.domain.User;
import mumage.mumagebackend.dto.FollowListDto;
import mumage.mumagebackend.service.FollowService;
import mumage.mumagebackend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService UserService;

    // 팔로우
    @GetMapping("/follow/{userId}&{loginUserId}")
    public String followGet(@PathVariable("userId") Long userId,
                            @PathVariable("loginUserId") Long loginUserId,
                            HttpServletRequest request) throws NoResultException {
        User from = UserService.findById(loginUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        User to = UserService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        followService.save(from,to); //로그인 유저가 선택한 유저를 팔로우

        // 이전 페이지로 복귀
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    // 언팔로우
    @GetMapping("/unfollow/{userId}&{loginUserId}")
    public String unfollowGet(@PathVariable("userId") Long userId,
                              @PathVariable("loginUserId") Long loginUserId,
                              HttpServletRequest request) throws NoResultException {

        User from = UserService.findById(loginUserId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));
        User to = UserService.findById(userId).orElseThrow(() -> new NoResultException("잘못된 User 정보 입니다."));

        followService.delete(from,to); //로그인 유저가 선택한 유저를 언팔로우

        // 이전 페이지로 복귀
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    // 팔로워 리스트 조회
    @GetMapping("/followerList/{userId}")
    public String followerListGet(@PathVariable("userId") Long userId,
                                  HttpSession session, Model model) {

        User loginUser = (User) session.getAttribute("loginUser");

        Set<User> follower = followService.getFollower(userId); // 선택한 유저의 팔로워 목록
        Set<FollowListDto> followDto = new HashSet<>(); // dto list에 담아서 view에 전환
        for (User User : follower) {
            followDto.add(followService.convertDto(loginUser, User)); // 팔로워 유저들 dto로 전환
        }

        model.addAttribute("followDto",followDto); // 팔로워 유저들 dto list
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId

        return "follow/followerList";
    }

    // 팔로잉 리스트 조회
    @GetMapping("/followingList/{userId}")
    public String followingListGet(@PathVariable("userId") Long userId,
                                   HttpSession session, Model model) {

        User loginUser = (User) session.getAttribute("loginUser");

        Set<User> follower = followService.getFollowing(userId); // 선택 유저의 팔로잉 목록
        Set<FollowListDto> followDto = new HashSet<>();
        for (User User : follower) {
            followDto.add(followService.convertDto(loginUser, User)); // 팔로잉 유저들 dto로 전환
        }

        model.addAttribute("followDto",followDto); // 팔로잉 유저 dto list
        model.addAttribute("userId",userId); //다시 프로필로 되돌아오기 위한 userId

        return "follow/followingList";
    }

}

