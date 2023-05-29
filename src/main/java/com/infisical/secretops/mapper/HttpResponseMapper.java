package com.infisical.secretops.mapper;

import com.infisical.secretops.http.APIResponse;
import okhttp3.Response;

import java.util.function.Function;

public class HttpResponseMapper implements Function<Response, APIResponse> {
    @Override
    public APIResponse apply(Response response) {
        return null;
    }
}
