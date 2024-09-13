package net.alebu.homework.web;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@ToString
@EqualsAndHashCode
public class AlbumDto {
    private final Long collectionId;
    private final String name;
}
