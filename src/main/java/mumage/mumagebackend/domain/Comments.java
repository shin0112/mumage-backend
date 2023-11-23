package mumage.mumagebackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*; //javax->jarkarta

@Entity
@Getter
@NoArgsConstructor
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comments_id")
    private Long id;

    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    @JsonBackReference
    private Posts posts; // 댓글 작성된 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user; // 댓글 작성자

    @Builder
    public Comments(String content, Posts posts, User user) {
        this.content = content;
        this.posts = posts;
        this.user = user;
    }
}