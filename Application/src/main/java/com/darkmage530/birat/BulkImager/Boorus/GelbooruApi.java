package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.JavaHttpClient;
import com.darkmage530.birat.posts.Safety;
import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.*;

/*
gelbooru
https://gelbooru.com/index.php?page=post&s=list&md5=a1de1c88e3b84fe1af04895dd2d87ca6
https://gelbooru.com/index.php?page=dapi&s=post&q=index&tags=md5:a1de1c88e3b84fe1af04895dd2d87ca6&json=1
 */

public class GelbooruApi {
    private static final Logger log = LoggerFactory.getLogger(GelbooruApi.class);
    private JavaHttpClient client;

    public GelbooruApi(JavaHttpClient httpClient) { client = httpClient; }

    public Optional<BooruResponse> getByMd5(String md5) {
        HttpGet httpGet = new HttpGet("https://gelbooru.com/index.php?page=dapi&s=post&q=index&tags=md5:" + md5 + "&json=1");
        httpGet.addHeader("Accept", "application/json");

        Optional<Map<String, Object>> jsonResponse = client.execute(httpGet);
        if (!jsonResponse.isPresent()) {
            return Optional.empty();
        }

        return parseBooruResponse(jsonResponse.get());
    }
//https://gelbooru.com/index.php?page=dapi&s=tag&q=index&json=1&names=1girl 8n0_g blonde_hair blurry blurry_background blush braid earrings festival floral_print hair_ornament hairclip highres idolmaster idolmaster_shiny_colors japanese_clothes jewelry kimono lantern long_sleeves looking_at_viewer looking_to_the_side obi paper_lantern purple_eyes saijo_juri sash short_hair solo summer_festival yukata

    private Optional<BooruResponse> parseBooruResponse(Map<String, Object> jsonResponse) {
        try {
            GelBooruResponse gelbooruResponse = new GelBooruResponse(jsonResponse);

            String tags = "";
            tags = URLEncoder.encode(gelbooruResponse.getTags().stream().reduce("", (partialString, element) -> partialString + element + " "), "UTF-8");
            tags = tags.substring(0, tags.length()-1);

            return Optional.of(gelbooruResponse);
        } catch (Exception ex) {
            log.error("Exception creating GelbooruResponse", ex);
            return Optional.empty();
        }
    }

    public Optional<TagCategories> getTagCategories(String tags) {
        /*
            type 4 = character
            type 0 = general
            type 3 = copyright
            type 5 = meta
            type 1 = artist
         */
        TagCategories tagCategories = new TagCategories();

        HttpGet httpGet = new HttpGet("https://gelbooru.com/index.php?page=dapi&s=tag&q=index&json=1&names=" +
                tags);
        httpGet.addHeader("Accept", "application/json");

        Optional<Map<String, Object>> tagsJsonResponse = client.execute(httpGet);

        if (!tagsJsonResponse.isPresent()) {
            return Optional.empty();
        }

        List<Map<String, Object>> innerPostResponse = ((List<Map<String, Object>>) tagsJsonResponse.get().get("tag"));
        for (Map<String, Object> tag : innerPostResponse) {
            int type = (int) ((double) tag.get("type"));
            switch (type) {
                case 1:
                    if (tagCategories.containsKey(TagCategory.Artist)) {
                        tagCategories.get(TagCategory.Artist).add(tag.get("name").toString());
                    } else {
                        tagCategories.add(TagCategory.Artist, Sets.newHashSet(tag.get("name").toString()));
                    }
                    break;
                case 3:
                    if (tagCategories.containsKey(TagCategory.Copyright)) {
                        tagCategories.get(TagCategory.Copyright).add(tag.get("name").toString());
                    } else {
                        tagCategories.add(TagCategory.Copyright, Sets.newHashSet(tag.get("name").toString()));
                    }
                    break;
                case 4:
                    if (tagCategories.containsKey(TagCategory.Character)) {
                        tagCategories.get(TagCategory.Character).add(tag.get("name").toString());
                    } else {
                        tagCategories.add(TagCategory.Character, Sets.newHashSet(tag.get("name").toString()));
                    }
                    break;
                case 5:
                    if (tagCategories.containsKey(TagCategory.Meta)) {
                        tagCategories.get(TagCategory.Meta).add(tag.get("name").toString());
                    } else {
                        tagCategories.add(TagCategory.Meta, Sets.newHashSet(tag.get("name").toString()));
                    }
                    break;
            }
        }

        return Optional.of(tagCategories);
    }
}

