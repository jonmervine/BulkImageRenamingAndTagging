package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.birat.BulkImager.Metadata.RatingSearch;
import com.darkmage530.birat.BulkImager.Image;

/**
 * Created by Shirobako on 10/1/2016.
 */
public class BooruImageFactory {

    private enum BooruList {
        ANIME_GALLERY("anigal"),
        E_SHUUSHUU("e-shuushuu"),
        SANKAKU_COMPLEX("sankaku"),
        ANIME_PICTURES("anime-pictures"),
        DANBOORU("danbooru"),
        KONACHAN("konachan"),
        ZEROCHAN("zerochan"),
        GELBOORU("gelbooru"),
        YANDERE("moe.imouto");

        private String name;

        BooruList(String name) {
            this.name = name;
        }
    }

    private BooruImageFactory() {
    }

    public static Image getBooru(RatingSearch image) {
/*
        if (image.getSource().equalsIgnoreCase(BooruList.ANIME_GALLERY.name)
                || image.getSource().equalsIgnoreCase(BooruList.E_SHUUSHUU.name)
                || image.getSource().equalsIgnoreCase(BooruList.SANKAKU_COMPLEX.name)
                || image.getSource().equalsIgnoreCase(BooruList.ANIME_PICTURES.name)
                || image.getSource().isEmpty()) {
            return null;

        } else if (image.getSource().equalsIgnoreCase(BooruList.DANBOORU.name)
                || image.getSource().equalsIgnoreCase(BooruList.KONACHAN.name)
                || image.getSource().equalsIgnoreCase(BooruList.GELBOORU.name)
                || image.getSource().equalsIgnoreCase(BooruList.YANDERE.name)) {
            return new DanbooruImage(image);
        } else if (image.getSource().equalsIgnoreCase(BooruList.ZEROCHAN.name)) {
            return new ZerochanImage(image);
        } else {
            return null;
        }

*/
        return null;
    }
}
