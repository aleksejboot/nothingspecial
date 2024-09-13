package net.alebu.zedgehomework.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column
    private Long favoriteArtistId;
}
