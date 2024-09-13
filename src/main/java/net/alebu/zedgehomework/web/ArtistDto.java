package net.alebu.zedgehomework.web;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@Jacksonized
public class ArtistDto {
    private final Long amgId;
    private final String name;
}
