package net.alebu.zedgehomework.web;

import net.alebu.zedgehomework.itunesclients.SearchAlbumsResponseDto;
import net.alebu.zedgehomework.persistence.entities.AlbumEntity;
import net.alebu.zedgehomework.persistence.entities.ArtistEntity;
import net.alebu.zedgehomework.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AppMapper {

    public UserEntity mapToEntity(UserCreateDto userCreateDto) {
        return UserEntity.builder()
                .email(userCreateDto.getEmail())
                .build();
    }

    public ArtistDto mapToArtistDto(ArtistEntity artistEntity) {
        return ArtistDto.builder()
                .amgId(artistEntity.getAmgId())
                .name(artistEntity.getName())
                .build();
    }

    public AlbumEntity mapToAlbumEntity(SearchAlbumsResponseDto.Item input) {
        return AlbumEntity.builder()
                .collectionId(input.getCollectionId())
                .artistAmgId(input.getAmgArtistId())
                .name(input.getCollectionCensoredName())
                .build();
    }

    public AlbumDto mapToAlbumDto(AlbumEntity input) {
        return AlbumDto.builder()
                .collectionId(input.getCollectionId())
                .name(input.getName())
                .build();
    }
}