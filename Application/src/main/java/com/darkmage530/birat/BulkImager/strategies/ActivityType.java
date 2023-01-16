package com.darkmage530.birat.BulkImager.strategies;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

interface Activity {
    void foundPicture(File file);
}

public enum ActivityType {
    FIND_WALLPAPERS("findWallpapers"),
    DEDUPLICATE("deduplicate"),
    IMPORT_TO_DOROBOORU("importToDorobooru"),
    UNKNOWN("unknown");

    final String configName;

    private static final Map<String, ActivityType> lookupMap = Maps.newHashMap();

    ActivityType(String configName) {
        this.configName = configName;
    }

    static {
        for (ActivityType activityType : ActivityType.values()) {
            lookupMap.put(activityType.configName.toUpperCase(), activityType);
        }
    }

    public static ActivityType lookupActivityType(String configName) {
        return lookupMap.getOrDefault(configName.toUpperCase(), UNKNOWN);
    }
}
