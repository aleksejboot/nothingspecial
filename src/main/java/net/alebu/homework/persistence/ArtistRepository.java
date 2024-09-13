package net.alebu.homework.persistence;

import net.alebu.homework.persistence.entities.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    default ArtistEntity requireById(Long artistId) {
        return findById(artistId).orElseThrow(
                () -> new IllegalArgumentException("Artist with ID " + artistId + " not found")
        );
    }

    @Modifying
    @Query("update artist set lastAlbumUpdateMoment = now() where amgId = :artistAmgId")
    void setArtistLastAlbumsUpdateToNow(Long artistAmgId);

}
