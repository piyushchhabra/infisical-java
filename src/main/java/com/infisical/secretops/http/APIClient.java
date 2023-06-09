package com.infisical.secretops.http;

import com.infisical.secretops.mapper.HttpResponseMapper;
import com.infisical.secretops.util.ObjectMapperUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class APIClient {

    private OkHttpClient httpClient;
    private HttpResponseMapper responseMapper;
    private String BASE_URL;
    private String SERVICE_TOKEN;

    private static final MediaType JSON_MEDIA_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    public APIClient(String token, String siteUrl) {
        this.BASE_URL = siteUrl;
        this.SERVICE_TOKEN = token;
        this.responseMapper = new HttpResponseMapper();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public APIResponse doGetRequest(String path, Map<String, String> queryParams) throws IOException {
        String url = prepareUrl(path, queryParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "InfisicalJavaSDK")
                .addHeader("Authorization", "Bearer " + SERVICE_TOKEN)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return responseMapper.apply(response);
    }

    public APIResponse doPostRequest(String path, Map<String, String> queryParams, Map<String, Object> body) throws IOException {
        String url = prepareUrl(path, queryParams);
        String payload = ObjectMapperUtil.getMapper().writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(payload, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "InfisicalJavaSDK")
                .addHeader("Authorization", "Bearer " + SERVICE_TOKEN)
                .post(requestBody)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return responseMapper.apply(response);
    }

    public APIResponse doDeleteRequest(String path, Map<String, String> queryParams, Map<String, Object> body) throws IOException {
        String url = prepareUrl(path, queryParams);
        String payload = ObjectMapperUtil.getMapper().writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(payload, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "InfisicalJavaSDK")
                .addHeader("Authorization", "Bearer " + SERVICE_TOKEN)
                .delete(requestBody)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return responseMapper.apply(response);
    }


    public APIResponse doPatchRequest(String path, Map<String, String> queryParams, Map<String, Object> body) throws IOException {
        String url = prepareUrl(path, queryParams);
        String payload = ObjectMapperUtil.getMapper().writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(payload, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "InfisicalJavaSDK")
                .addHeader("Authorization", "Bearer " + SERVICE_TOKEN)
                .patch(requestBody)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return responseMapper.apply(response);
    }


    private String prepareUrl(String path, Map<String, String> queryParams) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + path).newBuilder();
        if (queryParams != null && queryParams.size() > 0) {
            for (String key: queryParams.keySet()) {
                urlBuilder.addQueryParameter(key, queryParams.get(key));
            }
        }
        return urlBuilder.build().toString();
    }


}
