package com.infisical.secretops.model.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceConfig {
    String workspaceId;
    String environment;
    String workspaceKey;
}
