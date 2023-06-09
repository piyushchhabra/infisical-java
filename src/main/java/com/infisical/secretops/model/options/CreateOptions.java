package com.infisical.secretops.model.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOptions {
    OptionType type;

    public static CreateOptions defaultOptions() {
        return new CreateOptions(OptionType.SHARED);
    }
}
