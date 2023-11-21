package mumage.mumagebackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue
    @Column(name = "song_id")
    public Long songId; //기본키


    @Column(nullable = false)
    public String songName; //노래이름
    public String trackUrl; //노래 재생 Url
    public String singer; //가수명
    public String albumName; //앨범 이름

    @OneToMany(mappedBy="song")
    private List<Posts> posts = new ArrayList<>();

    @Builder
    public Song(String songName, String singer, String trackUrl, String albumName){
        this.trackUrl=trackUrl;
        this.songName=songName;
        this.albumName=albumName;
        this.singer=singer;
    }

}
