package com.darkmage530.BulkImager.Csv;

import com.darkmage530.BulkImager.ImageRating;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by DarkMage530 on 10/15/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */

//This is old and only used for converting the original csv to our format. going forward we should just read and write from our format.
public class OriginalCsvEntry {

    private String md5; //0
    private Long booruId; //1
    private String booru; //2
    //    downloaded; //3
//    created_at; //4
    private String fileExt; //5
    private Long fileSize; //6
    //    has_children; //7
    private Integer height; //8
    private Integer width; //9
    //    md5; //10
    private String parentId; //11
    private String rating; //12
    private Integer score; //13
    private String source; //14
    private List<String> tags; //15
    //    uploader_id; //16
//    approver_id; //17
//    down_score; //18
//    up_score; //19
//    updated_at; //20
//    fav_array; //21
//    is_banned; //22
    private Boolean deleted; //23
    //    is_flagged; //24
//    is_pending; //25
//    is_rating_locked; //26
//    has_active_children; //27
    private Long pixivId; //28
    private List<String> pools; //29
    //    last_comment_bumped_at; //30
//    last_commented_at; //31
//    last_noted_at; //32
    private Long changeSeq; //33
    private Long frames; //34
//    frames_pending; //35
//    is_held; //36
//    status //37

    public OriginalCsvEntry(String[] entry) {
        md5 = String.valueOf(entry[0]);
        booruId = entry[1].isEmpty() ? null : Long.valueOf(entry[1]);
        booru = String.valueOf(entry[2]);
        fileExt = String.valueOf(entry[5]);
        fileSize = entry[6].isEmpty() ? null : Long.valueOf(entry[6]);
        height = entry[8].isEmpty() ? null : Integer.valueOf(entry[8]);
        width = entry[9].isEmpty() ? null : Integer.valueOf(entry[9]);
        parentId = String.valueOf(entry[11]);
        rating = String.valueOf(entry[12]);
        score = entry[13].isEmpty() ? null : Integer.valueOf(entry[13]);
        source = String.valueOf(entry[14]);
        tags = Lists.newArrayList(entry[15].split("\"\\{|,|\\}\""));
        deleted = entry[23].isEmpty() ? null : Boolean.valueOf(entry[23]);
        pixivId = entry[28].isEmpty() ? null : Long.valueOf(entry[28]);
        pools = Lists.newArrayList(entry[29].split("\"\\{|,|\\}\""));
        changeSeq = entry[33].isEmpty() ? null : Long.valueOf(entry[33]);
        frames = entry[34].isEmpty() ? null : Long.valueOf(entry[34]);
    }

    public String getMd5() {
        return md5;
    }

    public ImageRating getRating() {
        return ImageRating.getImageRating(rating);
    }

    public String[] csvLine() {
        return new String[]{md5, String.valueOf(booruId), booru, fileExt,
                String.valueOf(fileSize), String.valueOf(height), String.valueOf(width),
                parentId, rating, String.valueOf(score), source, tags.toString(), String.valueOf(deleted),
                String.valueOf(pixivId), pools.toString(), String.valueOf(changeSeq), String.valueOf(frames)};
    }

}
