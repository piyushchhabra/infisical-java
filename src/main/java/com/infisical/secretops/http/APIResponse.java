package com.infisical.secretops.http;

import lombok.*;

import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class APIResponse {
    private Status status;
    private int responseCode;
    private String responseBody;

    public boolean isSuccess() {
        return Objects.nonNull(status) && Status.SUCCESS.equals(status);
    }
}
