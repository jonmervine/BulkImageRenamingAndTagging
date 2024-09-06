package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.JavaHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class DanbooruApi {
    private static final Logger log = LoggerFactory.getLogger(DanbooruApi.class);
    private final JavaHttpClient client;

    public DanbooruApi(JavaHttpClient httpClient) {
        client = httpClient;
    }

    public Optional<BooruResponse> getByMd5(String md5) {
        try {
            HttpGet httpGet = new HttpGet("https://danbooru.donmai.us/posts?md5=" + md5);
            httpGet.addHeader("accept", "application/json");

            Optional<Map<String, Object>> jsonResponse = client.execute(httpGet);
            if (!jsonResponse.isPresent()) {
                return Optional.empty();
            }

            return parseBooruResponse(jsonResponse.get());

        } catch (Exception ex) {
            log.error("Something failed while getting by md5 with Danbooru", ex);
            return Optional.empty();
        }
    }

    private Optional<BooruResponse> parseBooruResponse(Map<String, Object> jsonResponse) {
        try {
            DanbooruResponse danbooruResponse = new DanbooruResponse(jsonResponse);
            return Optional.of(danbooruResponse);
        } catch (Exception ex) {
            log.error("Exception creating DanbooruResponse", ex);
            return Optional.empty();
        }
    }


}

