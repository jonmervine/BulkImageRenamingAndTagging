package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.posts.PostRequest;
import com.darkmage530.birat.posts.Safety;
import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DanbooruApi {

    public Pair<PostRequest, Map<TagCategory, Set<String>>> getByMd5(String md5) {
        Map<String, Object> jsonResponse;

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
        HttpGet httpGet = new HttpGet("https://danbooru.donmai.us/posts?md5=" + md5);
        httpGet.addHeader("accept", "application/json");

        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            if (response1.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity1 = response1.getEntity();
            InputStreamReader inputStreamReader = new InputStreamReader(entity1.getContent());
            jsonResponse = new Gson().fromJson(inputStreamReader, Map.class);
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } catch (Throwable e) {
            System.out.println("Error calling danbooru");
            throw new RuntimeException(e);
        }

        return createPostRequest(jsonResponse);
    }

    private Pair<PostRequest, Map<TagCategory, Set<String>>> createPostRequest(Map<String, Object> jsonResponse) {
        List<String> tags = Arrays.asList(jsonResponse.get("tag_string").toString().split(" "));
        PostRequest postRequest = new PostRequest(
                tags,
                Safety.Companion.fromDanbooru(jsonResponse.get("rating").toString()),
                jsonResponse.get("source").toString()
        );

        Map<TagCategory, Set<String>> tagCategories = Maps.newHashMap();

        if (!jsonResponse.get("tag_string_character").toString().isEmpty()) {
            tagCategories.put(TagCategory.Character, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_character").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_copyright").toString().isEmpty()) {
            tagCategories.put(TagCategory.Copyright, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_copyright").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_artist").toString().isEmpty()) {
            tagCategories.put(TagCategory.Artist, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_artist").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_meta").toString().isEmpty()) {
            tagCategories.put(TagCategory.Meta, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_meta").toString().split(" "))));
        }
        return Pair.of(postRequest, tagCategories);
    }
}
