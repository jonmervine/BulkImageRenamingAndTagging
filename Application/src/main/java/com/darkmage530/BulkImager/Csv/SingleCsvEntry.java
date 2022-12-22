package com.darkmage530.BulkImager.Csv;

import com.darkmage530.BulkImager.ImageRating;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by DarkMage530 on 7/1/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
public class SingleCsvEntry {

    private String md5;
    private Long booruId;
    private String booru;
    private String fileExt;
    private Long fileSize;
    private Integer height;
    private Integer width;
    private String parentId;
    private String rating;
    private Integer score;
    private String source;
    private List<String> tags;
    private Boolean deleted;
    private Long pixivId;
    private List<String> pools;
    private Long changeSeq;
    private Long frames;

    public SingleCsvEntry(String[] entry) {
        md5 = String.valueOf(entry[0]);
        booruId = entry[1].isEmpty() ? null : Long.valueOf(entry[1]);
        booru = String.valueOf(entry[2]);
        fileExt = String.valueOf(entry[3]);
        fileSize = entry[4].isEmpty() ? null : Long.valueOf(entry[4]);
        height = entry[5].isEmpty() ? null : Integer.valueOf(entry[5]);
        width = entry[6].isEmpty() ? null : Integer.valueOf(entry[6]);
        parentId = String.valueOf(entry[7]);
        rating = String.valueOf(entry[8]);
        score = entry[9].isEmpty() ? null : Integer.valueOf(entry[9]);
        source = String.valueOf(entry[10]);
        tags = Lists.newArrayList(entry[11].split("\"\\{|,|\\}\""));
        deleted = entry[12].isEmpty() ? null : Boolean.valueOf(entry[12]);
        pixivId = entry[13].isEmpty() ? null : Long.valueOf(entry[13]);
        pools = Lists.newArrayList(entry[14].split("\"\\{|,|\\}\""));
        changeSeq = entry[15].isEmpty() ? null : Long.valueOf(entry[15]);
        frames = entry[16].isEmpty() ? null : Long.valueOf(entry[16]);
    }

    public String getMd5() {
        return md5;
    }

    public ImageRating getRating() { return ImageRating.getImageRating(rating); }

    public String[] csvLine() {
        return new String[]{md5, String.valueOf(booruId), booru, fileExt,
                String.valueOf(fileSize), String.valueOf(height), String.valueOf(width),
                parentId, rating, String.valueOf(score), source, tags.toString(), String.valueOf(deleted),
                String.valueOf(pixivId), pools.toString(), String.valueOf(changeSeq), String.valueOf(frames)};
    }

}
