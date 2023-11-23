package mumage.mumagebackend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "song_id")
    private Long songId; //기본키


    @Column(nullable = false)
    private String songName; //노래이름
    @Column
    private String trackUrl; //노래 재생 Url
    @Column
    private String singer; //가수명
    @Column
    private String albumName; //앨범 이름

    @OneToMany(mappedBy = "song")
    @JsonManagedReference
    private List<Posts> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "song_genre",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @Builder
    public Song(String songName, String singer, String trackUrl, String albumName){
        this.trackUrl=trackUrl;
        this.songName=songName;
        this.albumName=albumName;
        this.singer=singer;
    }

}
