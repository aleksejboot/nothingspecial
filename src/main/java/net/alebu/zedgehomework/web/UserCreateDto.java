package net.alebu.zedgehomework.web;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@ToString
public class UserCreateDto {
    private final String email;
}
