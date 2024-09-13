package net.alebu.zedgehomework.web;

import lombok.RequiredArgsConstructor;
import net.alebu.zedgehomework.AppService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing favorite artist.
 * Should allow to search for authors based on the given term and set/get favorite artist for user.
 */
@RestController
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;
    private final AppMapper appMapper;

    @GetMapping(path = "/artists/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ArtistDto> findArtists(
            @RequestParam(name = "term") String term
    ) {
        return appService.findArtistsByTermAndPersistLocally(term).stream()
                .map(appMapper::mapToArtistDto)
                .toList();
    }
}
