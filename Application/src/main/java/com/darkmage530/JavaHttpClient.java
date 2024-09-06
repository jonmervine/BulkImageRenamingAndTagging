package com.darkmage530;

import com.darkmage530.birat.BulkImager.Boorus.DanbooruApi;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class JavaHttpClient {
    private static final Logger log = LoggerFactory.getLogger(JavaHttpClient.class);
    private CloseableHttpClient httpClient;

    public void start() {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setCookieSpec(CookieSpecs.STANDARD)
                                .build()
                ).build();
        this.httpClient = httpClient;
    }

    public void shutdown() {
        try {
            httpClient.close();
        } catch (IOException ex) {
            log.error("Failed to close JavaHttpClient", ex);
            throw new RuntimeException(ex);
        }
    }

    public Optional<Map<String, Object>> execute(HttpRequestBase request) {
        Map<String, Object> jsonResponse;

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                return Optional.empty();
            }
            HttpEntity entity = response.getEntity();
            InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent());
            jsonResponse = new Gson().fromJson(inputStreamReader, Map.class);
            EntityUtils.consume(entity);
        } catch (Throwable e) {
            log.error("Error executing HttpRequest", e);
            return Optional.empty();
        }
        return Optional.of(jsonResponse);
    }
}
