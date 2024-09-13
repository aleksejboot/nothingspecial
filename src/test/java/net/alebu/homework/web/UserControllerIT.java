package net.alebu.homework.web;

import net.alebu.homework.persistence.AlbumRepository;
import net.alebu.homework.persistence.ArtistRepository;
import net.alebu.homework.persistence.UserRepository;
import net.alebu.homework.persistence.entities.AlbumEntity;
import net.alebu.homework.persistence.entities.ArtistEntity;
import net.alebu.homework.persistence.entities.UserEntity;
import net.alebu.homework.util.AbstractIT;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class UserControllerIT extends AbstractIT {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    AlbumRepository albumRepository;

    private static final String USER_LOCATION_REGEXP = "^.*/users/(\\d+)$";

    @Test
    void shouldCreateUser() throws Exception {
        final String email = "user@email.com";
        // GIVEN no user with given email exists
        // WHEN creating user with given email
        String locationHeader = mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        UserCreateDto.builder().email(email).build())
                                )
                )
                // THEN response code is 201 CREATED
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // AND Location header is set correctly
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, Matchers.matchesRegex(USER_LOCATION_REGEXP)))
                // AND response has body of new user
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.not(Matchers.nullValue())),
                        MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo(email))
                ).andReturn().getResponse().getHeader(HttpHeaders.LOCATION);
        Assertions.assertThat(locationHeader).isNotNull();
        Matcher matcher = Pattern.compile(USER_LOCATION_REGEXP).matcher(locationHeader);
        Long userId = matcher.matches() ? Long.valueOf(matcher.group(1)) : null;
        Assertions.assertThat(userId).isNotNull();
        // AND user is created in database
        Assertions.assertThat(userRepository.findById(userId)).isPresent();
//        Assertions.assertNotNull(userRepository.findById(userId).orElse(null));
    }

    @Test
    void shouldReturnExistingUserById() throws Exception {
        // GIVEN user with given ID exists
        UserEntity userToFind = userRepository.save(UserEntity.builder().email("user@email.com").build());

        // WHEN making GET request to find user by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userToFind.getId()))
                // THEN response code is 200 OK
                .andExpect(MockMvcResultMatchers.status().isOk())
                // AND response body contains user as JSON
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(userToFind.getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$.email", Matchers.equalTo("user@email.com"))
                );
    }

    @Test
    void shouldSetUserFavoriteArtist() throws Exception {
        // GIVEN user with given user ID exists
        final Long existingUserId = userRepository.save(UserEntity.builder().email("user@email.com").build()).getId();
        // AND artist with given artist ID exists
        final Long existingArtistId = artistRepository.save(ArtistEntity.builder().amgId(123L).name("abba").build()).getAmgId();

        // WHEN making a POST to assign artist as a favorite for a user
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/{userId}/favorite-artist", existingUserId)
                        .param("artistId", String.valueOf(existingArtistId))
                )
                // THEN response code is 200 OK
                .andExpect(MockMvcResultMatchers.status().isOk());
        // AND user is modified in database with favorite artist set to given artist
        Assertions.assertThat(
                userRepository.findById(existingUserId).map(UserEntity::getFavoriteArtistId).orElse(null)
        ).isEqualTo(existingArtistId);
    }

    @Test
    void shouldReturnAlbumsForArtistFromLocalStorage() throws Exception {
        // GIVEN an artist
        ArtistEntity existingArtist = artistRepository.save(
                ArtistEntity.builder()
                        .amgId(3492L)
                        .name("ABBA")
                        .build()
        );
        // AND user with favorite artist set to given artist
        UserEntity existingUserWithFavoriteArtist = userRepository.save(
                UserEntity.builder()
                        .email("user@email.com")
                        .favoriteArtistId(existingArtist.getAmgId())
                        .build()
        );
        // AND albums for given artist persisted locally
        albumRepository.saveAllAndFlush(
                Stream.of(
                        AlbumEntity.builder().collectionId(1422648512L).artistAmgId(3492L).name("ABBA Gold: Greatest Hits").ord(1),
                        AlbumEntity.builder().collectionId(1380464428L).artistAmgId(3492L).name("Mamma Mia! Here We Go Again (The Movie Soundtrack feat. the Songs of ABBA)").ord(2),
                        AlbumEntity.builder().collectionId(1440767912L).artistAmgId(3492L).name("Mamma Mia! (The Movie Soundtrack feat. the Songs of ABBA) [Bonus Track Version]").ord(3)
                ).map(AlbumEntity.AlbumEntityBuilder::build).toList()
        );

        // WHEN making GET request to fetch user's favorite albums
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{userId}/favorite-artist-top-albums", existingUserWithFavoriteArtist.getId())
                )
                // THEN status code is 200 OK
                .andExpect(MockMvcResultMatchers.status().isOk())
                // AND content type should be application/json
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, Matchers.equalTo(MediaType.APPLICATION_JSON_VALUE)))
                // AND 3 items are returned in response body
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.equalTo(3)))
                // AND 1st item should be as expected
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$[0].collectionId", Matchers.equalTo(1422648512)),
                        MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("ABBA Gold: Greatest Hits"))
                );
    }

    @Test
    void shouldReturnAlbumsForArtistByMakingCallToRemoteStorageAndSavingResultLocally() throws Exception {
        // GIVEN an artist
        ArtistEntity existingArtist = artistRepository.save(
                ArtistEntity.builder()
                        .amgId(3492L)
                        .name("ABBA")
                        .build()
        );
        // AND user with favorite artist set to given artist
        UserEntity existingUserWithFavoriteArtist = userRepository.save(
                UserEntity.builder()
                        .email("user@email.com")
                        .favoriteArtistId(existingArtist.getAmgId())
                        .build()
        );

        // AND albums for given artist are available remotely
        mockOkResponseWithJson(
                loadFileFromResources("/album-response-for-abba.json")
        );

        // WHEN making GET request to fetch user's favorite albums
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/{userId}/favorite-artist-top-albums",
                                existingUserWithFavoriteArtist.getId())
                )
                // THEN status code is 200 OK
                .andExpect(MockMvcResultMatchers.status().isOk())
                // AND content type should be application/json
                .andExpect(MockMvcResultMatchers.header()
                        .string(HttpHeaders.CONTENT_TYPE, Matchers.equalTo(MediaType.APPLICATION_JSON_VALUE)))
                // AND 5 items are returned in response body
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.equalTo(5)))
                // AND 1st item should be as expected
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$[0].collectionId", Matchers.equalTo(1422648512)),
                        MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("ABBA Gold: Greatest Hits"))
                );
        // AND request to ITunes was performed
        Assertions.assertThat(mockServer.takeRequest().getPath()).isEqualTo("/lookup?entity=album&amgArtistId=3492&limit=5");
    }
}