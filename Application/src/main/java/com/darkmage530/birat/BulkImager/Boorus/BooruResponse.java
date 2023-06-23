package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.posts.Safety;

import java.util.List;

public interface BooruResponse {
    List<String> getTags();
    Safety getSafety();
    String getSource();
}
