package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.DerpClient;
import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.HttpGet;

import java.util.Arrays;
import java.util.Map;

public class DanbooruApi {

    private DerpClient client;

    public DanbooruApi(DerpClient httpClient) {
        client = httpClient;
    }

    public Pair<BooruResponse, TagCategories> getByMd5(String md5) {

        HttpGet httpGet = new HttpGet("https://danbooru.donmai.us/posts?md5=" + md5);
        httpGet.addHeader("accept", "application/json");

        Map<String, Object> jsonResponse = client.execute(httpGet);

        return parseJsonResponse(jsonResponse);
    }

    private Pair<BooruResponse, TagCategories> parseJsonResponse(Map<String, Object> jsonResponse) {
        DanbooruResponse danbooruResponse = new DanbooruResponse(jsonResponse);

        TagCategories tagCategories = new TagCategories();

        tagCategories = getTagCategories(tagCategories, "tag_string_character", jsonResponse);
        tagCategories = getTagCategories(tagCategories, "tag_string_copyright", jsonResponse);
        tagCategories = getTagCategories(tagCategories, "tag_string_artist", jsonResponse);
        tagCategories = getTagCategories(tagCategories, "tag_string_meta", jsonResponse);
        return Pair.of(danbooruResponse, tagCategories);
    }

    private TagCategories getTagCategories(TagCategories tagCategories, String tagCategory, Map<String, Object> jsonResponse) {
        if (!jsonResponse.get(tagCategory).toString().isEmpty()) {
            tagCategories.add(TagCategory.Meta, Sets.newHashSet(Arrays.asList(jsonResponse.get(tagCategory).toString().split(" "))));
        }
        return tagCategories;
    }
}

