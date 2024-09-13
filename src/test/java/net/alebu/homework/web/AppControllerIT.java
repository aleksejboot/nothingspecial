package net.alebu.homework.web;

import net.alebu.homework.util.AbstractIT;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

@DirtiesContext
class AppControllerIT extends AbstractIT {
    @Test
    void shouldFindArtistsByTerm() throws Exception {
        // GIVEN ITunes are ready to serve us with data for a given term
        mockOkResponseWithJson(
                loadFileFromResources("/allArtist-search-response-for-abba.json")
        );
        // WHEN performing GET request for artists search with given term
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/artists/search").param("term", "abba")
                )
                // THEN response code is 200 OK
                .andExpect(MockMvcResultMatchers.status().isOk())
                // AND result is an array of 50 items
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.equalTo(7))) // not 50 because we filter out artists without amgId
                // AND 1st item is {amgId:3492, name: ABBA}
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$[0].keys()").value(Set.of("amgId", "name")),
                        MockMvcResultMatchers.jsonPath("$[0].amgId", Matchers.equalTo(3492)),
                        MockMvcResultMatchers.jsonPath("$[0].name", Matchers.equalTo("ABBA"))
                );

        // AND correct call was done to ITunes
        Assertions.assertThat(mockServer.takeRequest().getPath()).isEqualTo("/search?entity=allArtist&term=abba");
    }
}