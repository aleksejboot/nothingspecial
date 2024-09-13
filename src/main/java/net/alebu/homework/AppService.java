package net.alebu.homework;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.alebu.homework.itunesclients.SearchAlbumsClient;
import net.alebu.homework.itunesclients.SearchAlbumsResponseDto;
import net.alebu.homework.itunesclients.SearchArtistClient;
import net.alebu.homework.itunesclients.SearchArtistResponseDto;
import net.alebu.homework.persistence.AlbumRepository;
import net.alebu.homework.persistence.ArtistRepository;
import net.alebu.homework.persistence.UserRepository;
import net.alebu.homework.persistence.entities.AlbumEntity;
import net.alebu.homework.persistence.entities.ArtistEntity;
import net.alebu.homework.persistence.entities.UserEntity;
import net.alebu.homework.web.AppMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AppService {
    // TODO refactor by introducing separate UserService, and maybe other services

    private static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SearchArtistClient searchArtistClient;
    private final SearchAlbumsClient searchAlbumsClient;
    private final AppMapper appMapper;

    // USER support

    public UserEntity requireUserById(Long userId) {
        return userRepository.requireById(userId);
    }

    public Optional<UserEntity> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserEntity createNewUser(UserEntity user) {
        validateUserEmail(user.getEmail());
        return userRepository.save(user);
    }

    private void validateUserEmail(String email) {
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        } else if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email " + email + " already in use");
        }
    }

    public List<ArtistEntity> findArtistsByTermAndPersistLocally(String term) {
        List<ArtistEntity> foundArtists = this.findArtistsByTerm(term);
        List<Long> alreadyPersistedArtistIds =
                artistRepository.findAllById(foundArtists.stream().map(ArtistEntity::getAmgId).toList())
                        .stream().map(ArtistEntity::getAmgId).toList();
                ;
        List<ArtistEntity> needToPersist = foundArtists.stream()
                .filter(a -> !alreadyPersistedArtistIds.contains(a.getAmgId()))
                .toList();

        artistRepository.saveAll(needToPersist);
        return foundArtists;
    }

    public List<ArtistEntity> findArtistsByTerm(String term) {
        return searchArtistClient.search(term)
                .getResults()
                .stream()
                .filter(itm -> itm.getAmgArtistId() != null)
                .map(this::mapITunesArtistToDomain)
                .toList();
    }

    private ArtistEntity mapITunesArtistToDomain(SearchArtistResponseDto.Item dto) {
        ArtistEntity artistEntity = new ArtistEntity();
        artistEntity.setAmgId(dto.getAmgArtistId());
        artistEntity.setName(dto.getArtistName());
        return artistEntity;
    }

    public void setUserFavoriteArtist(
            Long userId,
            Long artistId
    ) {
        UserEntity user = userRepository.requireById(userId);
        ArtistEntity artist = artistRepository.requireById(artistId);

        user.setFavoriteArtistId(artist.getAmgId());
        artistRepository.save(artist);
        artistRepository.flush();
    }

    @Transactional
    public List<AlbumEntity> getArtistTopAlbums(
            Long artistAmgId,
            int numberOfAlbums
    ) {
        ArtistEntity favoriteArtist = artistRepository.requireById(artistAmgId);
        List<AlbumEntity> locallyAvailableAlbums = albumRepository.findByArtistAmgId(
                artistAmgId,
                Sort.by(Sort.Direction.ASC, "ord")
        );

        if (!locallyAvailableAlbums.isEmpty()) {
            return locallyAvailableAlbums;
        }
        // no albums currently persisted for artist, so need to get them from itunes and save for artist
        List<AlbumEntity> result = tryToFetchAlbumsFromITunes(favoriteArtist, numberOfAlbums);
        // also update artist's lastAlbumUpdateMoment
        artistRepository.setArtistLastAlbumsUpdateToNow(favoriteArtist.getAmgId());

        artistRepository.flush();
        return result;
    }

    private List<AlbumEntity> tryToFetchAlbumsFromITunes(
            ArtistEntity artist,
            int numberOfAlbums
    ) {
        SearchAlbumsResponseDto itunesResponse = searchAlbumsClient.search(
                artist.getAmgId(),
                numberOfAlbums
        );

        List<AlbumEntity> unsavedAlbumEntities = itunesResponse.getResults().stream()
                .filter(SearchAlbumsResponseDto.Item::isAlbum)
                .map(appMapper::mapToAlbumEntity)
                .toList();

        List<AlbumEntity> indexedAlbums = new ArrayList<>();
        for (int n = 0; n < unsavedAlbumEntities.size(); n++) {
            indexedAlbums.add(unsavedAlbumEntities.get(n).toBuilder().ord(n + 1).build());
        }

        return albumRepository.saveAllAndFlush(indexedAlbums);
    }
}
