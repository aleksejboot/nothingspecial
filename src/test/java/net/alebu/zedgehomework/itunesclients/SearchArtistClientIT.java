package net.alebu.zedgehomework.itunesclients;

import net.alebu.zedgehomework.util.AbstractIT;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

@DirtiesContext
class SearchArtistClientIT extends AbstractIT {

    @Autowired
    private SearchArtistClient searchArtistClient;

    @Test
    void shouldPerformCallToITunesAndTranslateResult() throws IOException, InterruptedException {
        // GIVEN ITunes has some data about "abba" based on classpath file
        mockOkResponseWithJson(
                loadFileFromResources("/allArtist-search-response-for-abba.json")
        );

        // WHEN calling ITunes artists search with "abba" term
        SearchArtistResponseDto response = searchArtistClient.search("abba");

        // THEN getting "abba" related search result mapped to DTOs
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResultCount()).isEqualTo(50);
        final List<SearchArtistResponseDto.Item> items = response.getResults();
        Assertions.assertThat(items).size().isEqualTo(50);
        final SearchArtistResponseDto.Item item = items.get(0);
        Assertions.assertThat(item).isEqualTo(
                SearchArtistResponseDto.Item.builder()
                        .artistId(372976L) // "artistId": 372976
                        .artistName("ABBA") // "artistName": "ABBA"
                        .amgArtistId(3492L) // "amgArtistId": 3492
                        .primaryGenreName("Pop") // "primaryGenreName": "Pop"
                        .primaryGenreId(14L) // "primaryGenreId": 14
                        .artistLinkUrl("https://music.apple.com/us/artist/abba/372976?uo=4") // "artistLinkUrl": "https://music.apple.com/us/artist/abba/372976?uo=4"
                        .build()
        );

        // AND correct call was done to ITunes
        Assertions.assertThat(mockServer.takeRequest().getPath()).isEqualTo("/search?entity=allArtist&term=abba");
    }
}