package com.infisical.secretops.model.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateOptions {
    String type;

    public static UpdateOptions defaultOptions() {
        return new UpdateOptions("shared");
    }
}
