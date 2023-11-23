package mumage.mumagebackend.repository;

import mumage.mumagebackend.domain.Genre;
import mumage.mumagebackend.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query(value = "select s from Song s where ?1 member of s.genres")
    Set<Song> findByGenreIn(Genre genre);

    Set<Song> findBySinger(String singer);

    Set<Song> findBySongName(String songName);

    Optional<Song> findBySongNameAndSinger(String songName, String singer);
}
