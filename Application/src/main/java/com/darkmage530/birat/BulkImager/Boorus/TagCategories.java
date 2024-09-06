package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TagCategories extends HashMap<TagCategory, Set<String>> {

    public TagCategories mergeCategories(TagCategories passedCategories) {
        this.putAll(passedCategories);
        return this;
    }

    public void add(TagCategory category, Set<String> tags) {
        this.put(category, tags);
    }
}
