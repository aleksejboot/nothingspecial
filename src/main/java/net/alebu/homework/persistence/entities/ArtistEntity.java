package net.alebu.homework.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "artist")
@Table(name = "artists")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistEntity {
    @Id
    private Long amgId;

    @Column(length = 100, nullable = false)
    private String name;

    private LocalDateTime lastAlbumUpdateMoment;
}
