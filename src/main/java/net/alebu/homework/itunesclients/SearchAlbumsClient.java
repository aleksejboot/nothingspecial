package net.alebu.homework.itunesclients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * WebClient for perform a call to ITunes for albums of given artist
 */
@Component
@RequiredArgsConstructor
public class SearchAlbumsClient {
    private final RestClient itunesRestClient;
    private final ObjectMapper objectMapper;


    // https://itunes.apple.com/lookup?amgArtistId=3492&entity=album&limit=5
    public SearchAlbumsResponseDto search(
            Long artistId,
            int limit
    ) {
        String json = itunesRestClient.post()
                .uri(uri -> uri
                        .path("/lookup")
                        .queryParam("entity", "album")
                        .queryParam("amgArtistId", artistId)
                        .queryParam("limit", limit)
                        .build()
                )
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(json, SearchAlbumsResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
