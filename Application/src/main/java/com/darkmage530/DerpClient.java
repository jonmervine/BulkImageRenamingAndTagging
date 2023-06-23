package com.darkmage530;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class DerpClient {

    private CloseableHttpClient httpClient;

    public void start() {
        CloseableHttpClient httpClient = HttpClients.custom()
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> execute(HttpRequestBase request) {
        Map<String, Object> jsonResponse;
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity1 = response.getEntity();
            InputStreamReader inputStreamReader = new InputStreamReader(entity1.getContent());
            jsonResponse = new Gson().fromJson(inputStreamReader, Map.class);
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } catch (Throwable e) {
            System.out.println("Error calling danbooru");
            throw new RuntimeException(e);
        }
        return jsonResponse;
    }
}
