package net.alebu.homework.persistence;

import net.alebu.homework.persistence.entities.AlbumEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    List<AlbumEntity> findByArtistAmgId(Long amgId, Sort sortBy);
}
