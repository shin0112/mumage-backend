package mumage.mumagebackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Likes {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    @JsonBackReference
    private Posts posts; // 좋아요 누른 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id")
    @JsonBackReference
    private User user; // 좋아요 누른 사용자

    @Builder
    public Likes(Posts posts, User user){
        this.posts = posts;
        this.user = user;
    }
}


