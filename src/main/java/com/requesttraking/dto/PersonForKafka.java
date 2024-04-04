package com.requesttraking.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Value
@RequiredArgsConstructor
@Jacksonized
public class PersonForKafka {
    Integer id;
    String username;
    List<Error> errors;

    @Builder
    @Value
    @RequiredArgsConstructor
    @Jacksonized
    public static class Error {
        String code;
        String message;
    }
}
