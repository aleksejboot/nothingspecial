package net.alebu.zedgehomework.itunesclients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Jacksonized
@ToString
public class SearchAlbumsResponseDto {

    private final Integer resultCount;

    private final List<Item> results;

    @Builder
    @Getter
    @Jacksonized
    @ToString
    @EqualsAndHashCode
    public static class Item {
        //"wrapperType": "collection",
        private final String wrapperType;

        //"collectionType": "Album",
        private final String collectionType;

        //"artistId": 372976,
        private final Long artistId;

        //"collectionId": 1422648512,
        private final Long collectionId;

        //"amgArtistId": 3492,
        private final Long amgArtistId;

        //"artistName": "ABBA",
        private final String artistName;

        //"collectionName": "ABBA Gold: Greatest Hits",
        private final String collectionName;

        //"collectionCensoredName": "ABBA Gold: Greatest Hits",
        private final String collectionCensoredName;

        //"artistViewUrl": "https://music.apple.com/us/artist/abba/372976?uo=4",
        private final String artistViewUrl;

        //"collectionViewUrl": "https://music.apple.com/us/album/abba-gold-greatest-hits/1422648512?uo=4",
        private final String collectionViewUrl;

        //"artworkUrl60": "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/60x60bb.jpg",
        private final String artworkUrl60;

        //"artworkUrl100": "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/60/f8/a6/60f8a6bc-e875-238d-f2f8-f34a6034e6d2/14UMGIM07615.rgb.jpg/100x100bb.jpg",
        private final String artworkUrl100;

        //"collectionPrice": 9.99,
        private final BigDecimal collectionPrice;

        //"collectionExplicitness": "notExplicit",
        private final String collectionExplicitness;

        //"trackCount": 19,
        private final Integer trackCount;

        //"copyright": "This Compilation ℗ 2014 Polar Music International AB",
        private final String copyright;

        //"country": "USA",
        private final String country;

        //"currency": "USD",
        private final String currency;

        //"releaseDate": "1992-09-21T07:00:00Z",
        private final String releaseDate;

        //"primaryGenreName": "Pop"
        private final String primaryGenreName;

        @JsonIgnore
        public boolean isAlbum() {
            return Objects.equals(this.wrapperType, "collection")
                    && Objects.equals(this.collectionType, "Album");
        }

    }
}
