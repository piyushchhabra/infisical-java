package com.infisical.secretops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfisicalClientOptions {
    private String token;
    private Long cacheTtlInSeconds;
    private String siteURL;
    private boolean debugMode;
}
