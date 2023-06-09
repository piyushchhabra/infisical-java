package com.infisical.secretops.mapper;

import com.infisical.secretops.http.APIResponse;
import com.infisical.secretops.http.Status;
import lombok.SneakyThrows;
import okhttp3.Response;

import java.util.function.Function;

public class HttpResponseMapper implements Function<Response, APIResponse> {
    @SneakyThrows
    @Override
    public APIResponse apply(Response response) {
        int responseCode = response.code();
        Status status = Status.UNKNOWN_ERROR;
        if (response.isSuccessful()) {
            status = Status.SUCCESS;
        } else if (responseCode >= 400 && responseCode < 500) {
            status = Status.BAD_REQUEST;
        } else if (responseCode >= 500) {
            status = Status.INTERNAL_ERROR;
        }
        return APIResponse.builder()
                .responseCode(responseCode)
                .status(status)
                .responseBody(response.body().string())
                .build();
    }
}
