package net.alebu.zedgehomework.itunesclients;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
@ToString
public class SearchArtistResponseDto {
    private final Integer resultCount;
    private final List<Item> results;

    @Builder
    @Getter
    @Jacksonized
    @ToString
    @EqualsAndHashCode
    public static class Item{
//        //"wrapperType": "artist",
//        private final String wrapperType;
//
//        //"artistType": "Artist",
//        private final String artistType;

        //"artistName": "ABBA",
        private final String artistName;

        //"artistLinkUrl": "https://music.apple.com/us/artist/abba/372976?uo=4",
        private final String artistLinkUrl;

        //"artistId": 372976,
        private final Long artistId;

        //"amgArtistId": 3492,
        private final Long amgArtistId;

        //"primaryGenreName": "Pop",
        private final String primaryGenreName;

        //"primaryGenreId": 14
        private final Long primaryGenreId;
    }
}
