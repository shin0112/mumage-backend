package mumage.mumagebackend.dto;

import lombok.*;
import mumage.mumagebackend.domain.Genre;

import java.util.Set;

@Data
@Getter
@RequiredArgsConstructor
public class PostsResponseDto {

    private Long postId;
    private String content;
    private String imageUrl;
    private Long userId;
    private String songName;
    private Set<Genre> genres;

    @Builder
    public PostsResponseDto(Long postId, String content, String imageUrl, Long userId, String songName, Set<Genre> genres) {
        this.postId = postId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.songName = songName;
        this.genres = genres;
    }

}

