package com.infisical.secretops.model.crypt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecryptOutput {
    private String value;
    private String tag;
}
