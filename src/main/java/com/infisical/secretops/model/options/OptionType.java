package com.infisical.secretops.model.options;

import lombok.Getter;

public enum OptionType {
    SHARED("shared"),
    PERSONAL("personal");

    @Getter
    public final String type;

    OptionType(String type) {
        this.type = type;
    }
}
