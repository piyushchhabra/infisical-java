package com.infisical.secretops.model;

import lombok.*;

@Data
@Builder
@ToString
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
