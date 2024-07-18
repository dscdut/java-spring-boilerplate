package com.gdsc.boilerplate.springboot.payment.momo.shared.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

import com.gdsc.boilerplate.springboot.payment.momo.models.HttpRequest;
import com.gdsc.boilerplate.springboot.payment.momo.models.HttpResponse;

@Slf4j
public class Execute {

    OkHttpClient client = new OkHttpClient();

    public HttpResponse sendToMoMo(String endpoint, String payload) {

        try {
            HttpRequest httpRequest = new HttpRequest("POST", endpoint, payload, "application/json");

            Request request = createRequest(httpRequest);

            log.debug("[HttpPostToMoMo] Endpoint:: {}, RequestBody:: {}", httpRequest.getEndpoint(), httpRequest.getPayload());

            Response result = client.newCall(request).execute();
            HttpResponse response = new HttpResponse(result.code(), result.body().string(), result.headers());

            log.info("[HttpResponseFromMoMo] {}", response.toString());

            return response;
        } catch (Exception e) {
            log.error("[ExecuteSendToMoMo] {}", e);
        }

        return null;
    }

    public static Request createRequest(HttpRequest request) {
        RequestBody body = RequestBody.create(MediaType.get(request.getContentType()), request.getPayload());
        return new Request.Builder()
                .method(request.getMethod(), body)
                .url(request.getEndpoint())
                .build();
    }

    public String getBodyAsString(Request request) throws IOException {
        Buffer buffer = new Buffer();
        RequestBody body = request.body();
        body.writeTo(buffer);
        return buffer.readUtf8();
    }
}
