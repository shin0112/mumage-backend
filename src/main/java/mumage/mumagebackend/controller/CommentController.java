package mumage.mumagebackend.controller;

import lombok.RequiredArgsConstructor;
import mumage.mumagebackend.dto.CommentDto;
import mumage.mumagebackend.service.CommentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentsService commentsService;

    // 댓글 등록
    @PostMapping("/comment/write/{postId}&{userId}")
    public String commentWritePost(@PathVariable("postId") Long postId,
                                   @PathVariable("userId") Long userId,
                                   @ModelAttribute("commentDto") CommentDto commentDto){
        commentsService.save(postId,userId,commentDto);
        return "redirect:/post/read/{postId}";
    }

    // 댓글 삭제
    @GetMapping("/comment/delete/{commentId}&{postId}")
    public String commentDeleteGet(@PathVariable("commentId") Long commentId,
                                   @PathVariable("postId") Long postId){
        commentsService.delete(commentId);
        return "redirect:/post/read/{postId}";
    }

    // 댓글 삭제 실패 : 댓글 작성자와 사용자가 다른 경우
    @GetMapping("/comment/noAuthority")
    public String notDeleteGet(){
        return "comment/noAuthority";
    }
}
