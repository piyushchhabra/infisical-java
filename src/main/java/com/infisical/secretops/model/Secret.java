package com.infisical.secretops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Secret {
    private String secretName;
    private String secretValue;
    private int version;
    private String workspace;
    private String environment;
    private String type;
    private String createdAt;
    private String updatedAt;
    private boolean isFallback;
    private long lastFetchedAt;
}
