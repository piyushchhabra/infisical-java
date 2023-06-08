package com.infisical.secretops.model.options;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteOptions {
    String type;

    public static DeleteOptions defaultOptions() {
        return new DeleteOptions("shared");
    }
}
