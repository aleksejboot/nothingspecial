package net.alebu.homework.itunesclients;

import net.alebu.homework.util.AbstractIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@DirtiesContext
class SearchAlbumsClientIT extends AbstractIT {
    @Autowired
    private SearchAlbumsClient searchAlbumsClient;

    @Test
    void shouldPerformCallToITunesAndTranslateResult() throws IOException, InterruptedException {
        // GIVEN ITunes has some data about "abba" based on classpath file
        mockOkResponseWithJson(
                loadFileFromResources("/album-response-for-abba.json")
        );

        // WHEN calling ITunes artists search with "abba" term
        SearchAlbumsResponseDto response = searchAlbumsClient.search(
                3492L,
                5
        );

        // THEN getting "abba" related search result mapped to DTOs
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResultCount()).isEqualTo(6); // yes, 6, not 5, because 1st result is an artist object and has different format and should be ignored
        List<SearchAlbumsResponseDto.Item> albums = response.getResults().stream()
                .filter(SearchAlbumsResponseDto.Item::isAlbum)
                .toList();
        Assertions.assertThat(albums).hasSize(5);
        SearchAlbumsResponseDto.Item album = albums.get(0);
        Assertions.assertThat(album).isEqualTo(
                SearchAlbumsResponseDto.Item.builder()
                        .wrapperType("collection") //"wrapperType": "collection",
                        .collectionType("Album")//"collectionType": "Album",
                        .artistId(372976L)//"artistId": 372976,
                        .collectionId(1422648512L)//"collectionId": 1422648512,
                        .amgArtistId(3492L)//"amgArtistId": 3492,
                        .artistName("ABBA")//"artistName": "ABBA",
                        .collectionName("ABBA Gold: Greatest Hits")//"collectionName": "ABBA Gold: Greatest Hits",
                        .collectionCensoredName("ABBA Gold: Greatest Hits")//"collectionCensoredName": "ABBA Gold: Greatest Hits",
                        .artistViewUrl("https://music.apple.com/us/artist/abba/372976?uo=4")//"artistViewUrl": "https://music.apple.com/us/artist/abba/372976?uo=4",
                        .collectionViewUrl("https://music.apple.com/us/album/abba-gold-greatest-hits/1422648512?uo=4")//"collectionViewUrl": "https://music.apple.com/us/album/abba-gold-greatest-hits/1422648512?uo=4",
                        .artworkUrl60("https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/60x60bb.jpg")//"artworkUrl60": "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/60x60bb.jpg",
                        .artworkUrl100("https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/100x100bb.jpg")//"artworkUrl100": "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/100x100bb.jpg",
                        .collectionPrice(BigDecimal.valueOf(9.99))//"collectionPrice": 9.99,
                        .collectionExplicitness("notExplicit")//"collectionExplicitness": "notExplicit",
                        .trackCount(19)//"trackCount": 19,
                        .copyright("This Compilation ℗ 2014 Polar Music International AB")//"copyright": "This Compilation ℗ 2014 Polar Music International AB",
                        .country("USA")//"country": "USA",
                        .currency("USD")//"currency": "USD",
                        .releaseDate("1992-09-21T07:00:00Z")//"releaseDate": "1992-09-21T07:00:00Z",
                        .primaryGenreName("Pop")//"primaryGenreName": "Pop"
                        .build()
        );
        // AND correct call was done to ITunes
        Assertions.assertThat(mockServer.takeRequest().getPath()).isEqualTo("/lookup?entity=album&amgArtistId=3492&limit=5");
    }

}