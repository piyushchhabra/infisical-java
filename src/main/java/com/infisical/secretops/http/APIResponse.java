package com.infisical.secretops.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private Status status;
    private int responseCode;
    private String responseBody;
}
