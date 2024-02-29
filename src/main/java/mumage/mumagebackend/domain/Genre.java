package mumage.mumagebackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private final Set<Song> songs = new HashSet<>();

    @ManyToMany(mappedBy = "genres")
    private final Set<User> users = new HashSet<>();

}
