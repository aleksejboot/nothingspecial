package net.alebu.homework.itunesclients;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * As is documented on ITunes website.
 * Looks like it has a superset of all possible fields for different entity types in search.
 * So fields provided are based on wrapperType field, which acts like determinant.
 */
@Builder
@Jacksonized
public class ITunesResponseDto {
    //"wrapperType":"track",
    private final String wrapperType;

    //"kind":"song",
    private final String kind;

    //"artistId":909253,
    private final String artistId;

    //"collectionId":120954021,
    private final String collectionId;

    //"trackId":120954025,
    private final String trackId;

    //"artistName":"Jack Johnson",
    private final String artistName;

    //"collectionName":"Sing-a-Longs and Lullabies for the Film Curious George",
    private final String collectionName;

    //"trackName":"Upside Down",
    private final String trackName;

    //"collectionCensoredName":"Sing-a-Longs and Lullabies for the Film Curious George",
    private final String collectionCensoredName;

    //"trackCensoredName":"Upside Down",
    private final String trackCensoredName;

    //"artistViewUrl":"https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewArtist?id=909253",
    private final String artistViewUrl;

    //"collectionViewUrl":"https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewAlbum?i=120954025&id=120954021&s=143441",
    private final String collectionViewUrl;

    //"trackViewUrl":"https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewAlbum?i=120954025&id=120954021&s=143441",
    private final String trackViewUrl;

    //"previewUrl":"http://a1099.itunes.apple.com/r10/Music/f9/54/43/mzi.gqvqlvcq.aac.p.m4p",
    private final String previewUrl;

    //"artworkUrl60":"http://a1.itunes.apple.com/r10/Music/3b/6a/33/mzi.qzdqwsel.60x60-50.jpg",
    private final String artworkUrl60;

    //"artworkUrl100":"http://a1.itunes.apple.com/r10/Music/3b/6a/33/mzi.qzdqwsel.100x100-75.jpg",
    private final String artworkUrl100;

    //"collectionPrice":10.99,
    private final String collectionPrice;

    //"trackPrice":0.99,
    private final String trackPrice;

    //"collectionExplicitness":"notExplicit",
    private final String collectionExplicitness;

    //"trackExplicitness":"notExplicit",
    private final String trackExplicitness;

    //"discCount":1,
    private final String discCount;

    //"discNumber":1,
    private final String discNumber;

    //"trackCount":14,
    private final String trackCount;

    //"trackNumber":1,
    private final String trackNumber;

    //"trackTimeMillis":210743,
    private final String trackTimeMillis;

    //"country":"USA",
    private final String country;

    //"currency":"USD",
    private final String currency;

    //"primaryGenreName":"Rock"
    private final String primaryGenreName;

}
