package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.posts.Safety;
import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DanbooruResponse implements BooruResponse {
    private final List<String> tags;
    private final Safety safety;
    private final String source;
    private final TagCategories tagCategories;

    public DanbooruResponse(Map<String, Object> jsonResponse) {
        tags = Arrays.asList(jsonResponse.get("tag_string").toString().split(" "));
        safety = Safety.Companion.fromDanbooru(jsonResponse.get("rating").toString());
        source = jsonResponse.get("source").toString();
        tagCategories = parseTagCategories(jsonResponse);
    }

    private TagCategories parseTagCategories(Map<String, Object> jsonResponse) {
        TagCategories tagCategories = new TagCategories();

        if (!jsonResponse.get("tag_string_character").toString().isEmpty()) {
            tagCategories.add(TagCategory.Character, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_character").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_copyright").toString().isEmpty()) {
            tagCategories.add(TagCategory.Copyright, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_copyright").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_artist").toString().isEmpty()) {
            tagCategories.add(TagCategory.Artist, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_artist").toString().split(" "))));
        }
        if (!jsonResponse.get("tag_string_meta").toString().isEmpty()) {
            tagCategories.add(TagCategory.Meta, Sets.newHashSet(Arrays.asList(jsonResponse.get("tag_string_meta").toString().split(" "))));
        }
        return tagCategories;
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

    public TagCategories getTagCategories() { return tagCategories; }
}
