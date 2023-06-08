package com.infisical.secretops.model.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOptions {
    String type;

    public static GetOptions defaultOptions() {
        return new GetOptions("personal");
    }
}
