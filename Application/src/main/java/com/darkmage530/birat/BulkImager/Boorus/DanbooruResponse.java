package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.posts.Safety;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DanbooruResponse implements BooruResponse {
    private final List<String> tags;
    private final Safety safety;
    private final String source;

    public DanbooruResponse(Map<String, Object> jsonResponse) {
        tags = Arrays.asList(jsonResponse.get("tag_string").toString().split(" "));
        safety = Safety.Companion.fromDanbooru(jsonResponse.get("rating").toString());
        source = jsonResponse.get("source").toString();
    }

    public List<String> getTags() {
        return tags;
    }

    public Safety getSafety() {
        return safety;
    }

    public String getSource() {
        return source;
    }
}
