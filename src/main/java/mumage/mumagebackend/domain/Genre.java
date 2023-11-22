package mumage.mumagebackend.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;
    @Column
    private String genreName;

    @ManyToMany(mappedBy = "genres")
    private Set<Song> songs;

    @ManyToMany(mappedBy = "genres")
    private Set<User> users;

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }


}
