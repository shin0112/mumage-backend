package mumage.mumagebackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor
@Table(name="Posts")
public class Posts extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posts_id")
    private Long postId;

    @Column(length = 500)
    private String content; // 글 내용

    @Column
    private String imageUrl; // 이미지 파일 url

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //FK
    @JsonBackReference
    private User user; // 작성자 클래스를 참조

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comments> commentsList; // 게시글에 달린 댓글 목록

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Likes> likesList; // 게시글에 누른 좋아요 목록

    //곡정보 테이블 매핑 -> 아티스트, 곡제목, 트랙 Url
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="song_id") //FK
    @JsonBackReference
    private Song song; //Song 클래스를 참조

    //장르 테이블 매핑

    @Builder
    public Posts(String content, String imageUrl
            , User user, Song song){
        this.content = content;
        this.imageUrl = imageUrl;
        this.user = user;
        this.song=song;
        likesList = new HashSet<>();
        commentsList = new HashSet<>();
    }

    // 게시글 수정 기능시 사용
    public void update(String content){
        this.content = content;
    }
}
