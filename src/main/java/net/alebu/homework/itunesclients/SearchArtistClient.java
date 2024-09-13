package net.alebu.homework.itunesclients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * WebClient for perform a call to ITunes for artists based on term
 */
@Component
@RequiredArgsConstructor
public class SearchArtistClient {
    private final RestClient itunesRestClient;

    private final ObjectMapper objectMapper;

    public SearchArtistResponseDto search(String term) {
        String responseBody = itunesRestClient.post()
                .uri(uri -> uri
                        .path("/search")
                        .queryParam("entity", "allArtist")
                        .queryParam("term", URLEncoder.encode(term, StandardCharsets.UTF_8))
                        .build()
                )
                .retrieve()
//                .onStatus(HttpStatusCode::is5xxServerError,
//                        (httpRequest) -> throw new IllegalStateException("Failed to fetch artists from ITunes"))
                .body(String.class);
        try {
            return objectMapper.readValue(responseBody, SearchArtistResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
