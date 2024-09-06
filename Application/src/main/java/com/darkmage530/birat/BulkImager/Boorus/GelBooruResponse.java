package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.posts.Safety;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GelBooruResponse implements BooruResponse {
    private final List<String> tags;
    private final Safety safety;
    private final String source;
    private TagCategories tagCategories;

    public GelBooruResponse(Map<String, Object> jsonResponse) {
        // TODO handle cases where no md5 match
        Map<String, Object> innerPostResponse = ((List<Map<String, Object>>) jsonResponse.get("post")).get(0);

        tags = Arrays.asList(innerPostResponse.get("tags").toString().split(" "));
        safety = Safety.Companion.fromGelbooru(innerPostResponse.get("rating").toString());
        source = innerPostResponse.get("source").toString();
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public Safety getSafety() {
        return safety;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public TagCategories getTagCategories() {
        return tagCategories;
    }
}
