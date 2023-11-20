package mumage.mumagebackend.domain;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Likes {

    @Id @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts; // 좋아요 누른 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    private User user; // 좋아요 누른 사용자

    @Builder
    public Likes(Posts posts, User user){
        this.posts = posts;
        this.user = user;
    }
}
