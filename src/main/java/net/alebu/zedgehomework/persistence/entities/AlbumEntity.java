package net.alebu.zedgehomework.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "album")
@Table(name = "albums")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AlbumEntity {
    @Id
    private Long collectionId;

    @Column(nullable = false)
    private Long artistAmgId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer ord;
}
