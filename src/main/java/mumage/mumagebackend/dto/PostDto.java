package mumage.mumagebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class PostDto {

    private Long postId;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String imageUrl;

    private String createdDate;

    private Long userId;
    private String userName;
    private String userPicture;
    private Set<Long> likeUserIdList;

    private String songName;
    private String singer;
    private String trackUrl;
    private String albumName;

    @Builder
    public PostDto(Long postId, String singer, String songName, String trackUrl, String albumName, String content, String imageUrl, String createdDate, Long userId, String userName, String userPicture, Set<Long> likeUserIdList) {
        this.postId = postId;
        this.singer = singer;
        this.songName = songName;
        this.trackUrl = trackUrl;
        this.albumName=albumName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.userId = userId;
        this.userName = userName;
        this.userPicture = userPicture;
        this.likeUserIdList = likeUserIdList;
    }
}

