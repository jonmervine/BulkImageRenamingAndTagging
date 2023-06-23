package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class TagCategories {
    private final Map<TagCategory, Set<String>> tagCategories = Maps.newHashMap();

    public Map<TagCategory, Set<String>> getTagCategories() {
        return tagCategories;
    }

    public TagCategories mergeCategories(TagCategories passedCategories) {
        tagCategories.putAll(passedCategories.tagCategories);
        return this;
    }

    public void add(TagCategory category, Set<String> tags) {
        tagCategories.put(category, tags);
    }
}
