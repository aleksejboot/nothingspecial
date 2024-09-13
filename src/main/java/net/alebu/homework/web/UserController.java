package net.alebu.homework.web;

import lombok.RequiredArgsConstructor;
import net.alebu.homework.AppService;
import net.alebu.homework.persistence.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for managing favorite artist.
 * Should allow to search for authors based on the given term and set/get favorite artist for user.
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    private final AppService appService;
    private final AppMapper appMapper;

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<UserEntity> findUserById(
            @PathVariable Long userId
    ) {
        return appService.findUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/users")
    public ResponseEntity<UserEntity> createUser(
            @RequestBody UserCreateDto userCreateDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UserEntity userToCreate = appMapper.mapToEntity(userCreateDto);
        final UserEntity createdUser = appService.createNewUser(userToCreate);
        return ResponseEntity.created(
                uriComponentsBuilder.path("/users/{userId}").build(Map.of("userId", createdUser.getId()))
        ).body(createdUser);
    }

    @PostMapping(path = "/users/{userId}/favorite-artist")
    public void setUserFavoriteArtist(
            @PathVariable Long userId,
            @RequestParam Long artistId
    ) {
        appService.setUserFavoriteArtist(userId, artistId);
    }

    @GetMapping(path = "/users/{userId}/favorite-artist-top-albums")
    public List<AlbumDto> getFavoriteArtistTopAlbums(
            @PathVariable Long userId
    ) {
        final UserEntity currentUser = appService.requireUserById(userId);

        Long favoriteArtistId =
                Optional.ofNullable(currentUser.getFavoriteArtistId())
                        .orElseThrow(() -> new IllegalArgumentException("User has no favorite artist set"));

        return appService.getArtistTopAlbums(
                        favoriteArtistId,
                        5
                ).stream()
                .map(appMapper::mapToAlbumDto)
                .toList();
    }

}
