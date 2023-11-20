package mumage.mumagebackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
public class CommentDto {

    @NotNull(message = "내용을 입력해주세요")
    private String comment;
    private Long commentId;
    private Long userId;
    private String userName;
    private String userPicture;
    private String createdDate;

    @Builder
    public CommentDto(String comment, Long commentId, Long userId, String userName, String userPicture, String createdDate) {

        this.comment = comment;

        //댓글 기본키
        this.commentId = commentId;

        //회원 기본키
        this.userId = userId;

        //회원 이름
        this.userName = userName;

        //회원 프로필 이미지
        this.userPicture = userPicture;

        //댓글 생성 날짜
        this.createdDate = createdDate;

    }
}
