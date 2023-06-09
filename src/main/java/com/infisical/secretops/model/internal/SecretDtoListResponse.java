package com.infisical.secretops.model.internal;

import com.infisical.secretops.model.internal.SecretDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SecretDtoListResponse {
    List<SecretDto> secrets;
}
