package com.infisical.secretops.model.options;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOptions {
    OptionType type;

    public static GetOptions defaultOptions() {
        return new GetOptions(OptionType.PERSONAL);
    }
}
