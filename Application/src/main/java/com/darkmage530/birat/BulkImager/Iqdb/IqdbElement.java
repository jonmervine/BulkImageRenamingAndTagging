package com.darkmage530.birat.BulkImager.Iqdb;

import com.darkmage530.birat.BulkImager.ImageRating;
import com.darkmage530.birat.BulkImager.ImageDimensions;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by DarkMage530 on 3/5/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
//package-private
class IqdbElement {

    private static final Logger log = LoggerFactory.getLogger(IqdbElement.class);

    private Element element;

    public IqdbElement(Element element) {
        this.element = element;
    }

    public IqdbMatch explode(IqdbMatchType matchType) {
        Elements tableDatas = element.getElementsByTag("td");

        //Get URL and tags
        Element image = tableDatas.get(0);
        String url = image.getElementsByTag("a").get(0).attr("href");
        String altText = image.getElementsByTag("img").get(0).attr("alt");
        String tags = altText.substring(altText.indexOf("Tags: ") + 6, altText.length());

        String sourceName = tableDatas.get(1).text();

        //Get rating and resoultion
        Element resolutionAndRating = tableDatas.get(2); //Safe, Ero, Explicit
        String resolutionAndRatingText = resolutionAndRating.text();
        ImageRating rating = getImageRating(resolutionAndRatingText);
        ImageDimensions resolution = getImageRatio(resolutionAndRatingText);

        int similarityPercent = getSimilarity(tableDatas.get(3));

        return new IqdbMatch(matchType, sourceName, rating, resolution, url, tags, similarityPercent);
    }

    private ImageRating getImageRating(String resolutionAndRating) {
        String rating = resolutionAndRating.substring(resolutionAndRating.indexOf("[") + 1, resolutionAndRating.indexOf("]"));
        return ImageRating.getImageRating(rating);
    }

    private ImageDimensions getImageRatio(String resolutionAndRating) {
        String resolution = resolutionAndRating.substring(0, resolutionAndRating.indexOf(" ["));
        return ImageDimensions.getImageDimensions(resolution);
    }

    private int getSimilarity(Element similarityElement) {
        int similarity = -1;
        try {
            String similarityText = similarityElement.text();
            similarity = Integer.parseInt(similarityText.substring(0, similarityText.indexOf("%")));
        } catch (NumberFormatException ex) {
            log.warn("NumberFormatException while parsing SimilarityPercent: " + similarityElement.text());
        }
        return similarity;
    }
}

/*
<table><th>Best match</th>
<td class='image'><a href="//konachan.com/post/show/194975"><img src='/konachan/a/4/f/a4fea7399aad1d398047020b06c954e3.jpg' alt="Rating: s Score: 164 Tags: animal bikini_top breasts bubbles cleavage fish kagayan1096 long_hair mermaid purple_eyes purple_hair twintails underwater vocaloid voiceroid water yuzuki_yukari" title="Rating: s Score: 164 Tags: animal bikini_top breasts bubbles cleavage fish kagayan1096 long_hair mermaid purple_eyes purple_hair twintails underwater vocaloid voiceroid water yuzuki_yukari" width='150' height='105'></a></td>
<td><img alt="icon" src="//konachan.com/favicon.ico" class="service-icon">Konachan</td>
<td>1800�1254 [Safe]</td>
<td>96% similarity</td></tr></table></div>

<table><tr><th>Additional match</th>
<td class='image'><a href="http://www.zerochan.net/1881288"><img src='/zerochan/5/0/d/50d7365a05e981ec7480116ef4b94fe6.jpg' alt="Rating: q Tags: Female, Ecchi, Fanart, Water, Fish, Long Hair, Ocean, Purple Eyes, Animal, Purple Hair, Clenched Hand, Vocaloid, Mermaid, Beads, Bubble, Underwater, Jewelry, Coral, Pearls, Heart, Kemonomimi, Open Mouth, Outdoors, Pixiv, Midriff, Solo, Frame, Bikini Top, Text, Head Fins, Alternate Setting, Sidelocks, Hand on Chest, Sleeveless, Black Border, Fanart From Pixiv, Looking At Camera, English Text, Coral Reef, Blurry, Water Bubbles, Yuzuki Yukari, Voiceroid, Cleavage, Shoulder Strap, Pixiv Id 4703380, Exposed Shoulders" title="Rating: q Tags: Female, Ecchi, Fanart, Water, Fish, Long Hair, Ocean, Purple Eyes, Animal, Purple Hair, Clenched Hand, Vocaloid, Mermaid, Beads, Bubble, Underwater, Jewelry, Coral, Pearls, Heart, Kemonomimi, Open Mouth, Outdoors, Pixiv, Midriff, Solo, Frame, Bikini Top, Text, Head Fins, Alternate Setting, Sidelocks, Hand on Chest, Sleeveless, Black Border, Fanart From Pixiv, Looking At Camera, English Text, Coral Reef, Blurry, Water Bubbles, Yuzuki Yukari, Voiceroid, Cleavage, Shoulder Strap, Pixiv Id 4703380, Exposed Shoulders" width='150' height='116'></a></td>
<td><img alt="icon" src="http://zerochan.net/favicon.ico" class="service-icon">Zerochan</td>
<td>1800�1398 [Ero]</td>
<td>65% similarity</td></tr></table></div>

<table><tr><th>Additional match</th>
<td class='image'><a href="//danbooru.donmai.us/posts/1905445"><img src='/danbooru/5/0/d/50d7365a05e981ec7480116ef4b94fe6.jpg' alt="Rating: s Tags: 1girl bikini_top coral fish highres kagayan1096 long_hair mermaid monster_girl purple_eyes purple_hair twintails underwater vocaloid voiceroid yuzuki_yukari" title="Rating: s Tags: 1girl bikini_top coral fish highres kagayan1096 long_hair mermaid monster_girl purple_eyes purple_hair twintails underwater vocaloid voiceroid yuzuki_yukari"></a></td>
<td><img alt="icon" src="//danbooru.donmai.us/favicon.ico" class="service-icon">Danbooru <span class="el"><a href="http://gelbooru.com/index.php?page=post&s=list&md5=50d7365a05e981ec7480116ef4b94fe6"><img alt="icon" src="http://gelbooru.com/favicon.png" class="service-icon">Gelbooru</a></span></td>
<td>1800�1398 [Safe]</td>
<td>64% similarity</td></tr></table></div>

<div><table><tr><th>Additional match</th>
<td class='image'><a href="//chan.sankakucomplex.com/post/show/4376911"><img src='/sankaku/5/0/d/50d7365a05e981ec7480116ef4b94fe6.jpg' alt="Rating: s Tags: {464292,79222,337507,599,49,455,454379,6878,9081,47632,130462,446,399,26640,7068,1356,2818,330,3885,322,97161,142,5154,1349,107573,73719,21749,1090,34240,124887,264387,40213,161268,563,8503,163,1091,771,5909,2084,5212,95705,43623,100652,64721,108,555145,582845,171086,79775,571}" title="Rating: s Tags: {464292,79222,337507,599,49,455,454379,6878,9081,47632,130462,446,399,26640,7068,1356,2818,330,3885,322,97161,142,5154,1349,107573,73719,21749,1090,34240,124887,264387,40213,161268,563,8503,163,1091,771,5909,2084,5212,95705,43623,100652,64721,108,555145,582845,171086,79775,571}" width='150' height='116'></a></td>
<td><img alt="icon" src="//images.sankakucomplex.com/gfx/favicon.png" class="service-icon">Sankaku Channel</td>
<td>1800�1398 [Safe]</td>
<td>63% similarity</td></tr></table>

NO TAGS
<table><tr><th>Additional match</th>
<td class='image'><a href="http://e-shuushuu.net/image/559324/"><img src='/e-shuushuu/a/a/c/aac4bc16f0672c493063a42fd23f80a4.jpg' alt="[IMG]" width='106' height='150'></a></td>
<td><img alt="icon" src="http://e-shuushuu.net/special/favicon.ico" class="service-icon">e-shuushuu</td>
<td>640�905 [Safe]</td>
<td>96% similarity</td></tr></table></div>

NO TAGS
<table><tr><th>Additional match</th>
<td class='image'><a href="http://www.theanimegallery.com/image/39945"><img src='/anigal/f/0/2/f02ef2955da8f80523b0bb2f86da5dca.jpg' alt="Rating: s Score: 9.40" title="Rating: s Score: 9.40" width='150' height='112'></a></td>
<td><img alt="icon" src="http://www.theanimegallery.com/favicon.ico" class="service-icon">The Anime Gallery</td>
<td>800�600 [Safe]</td>
<td>97% similarity</td></tr></table></div>

<table><tr><th>Additional match</th>
<td class='image'><a href="http://gelbooru.com/index.php?page=post&s=view&id=2030833"><img src='/gelbooru/2/7/2/272cc7cf8419b9399a8adc74c9837e8d.jpg' alt="Rating: s Score: 5 Tags: blue_hair blush breast_smother breasts eyes_closed huge_breasts lying ryofu_housen_(sangoku_hime) sangoku_hime_3 silver_hair uwami_risuke" title="Rating: s Score: 5 Tags: blue_hair blush breast_smother breasts eyes_closed huge_breasts lying ryofu_housen_(sangoku_hime) sangoku_hime_3 silver_hair uwami_risuke" width='150' height='84'></a></td>
<td><img alt="icon" src="http://gelbooru.com/favicon.png" class="service-icon">Gelbooru</td>
<td>1280�720 [Safe]</td>
<td>94% similarity</td></tr></table></div>

<table><tr><th>Additional match</th>
<td class='image'><a href="https://yande.re/post/show/335286"><img src='/moe.imouto/c/3/a/c3a8f653c4c86cacf00a8dd9da9539c8.jpg' alt="Rating: q Score: 17 Tags: ass bandaid kirisaki_chitoge nisekoi onodera_kosaki school_swimsuit swimsuits watanabe_akio" title="Rating: q Score: 17 Tags: ass bandaid kirisaki_chitoge nisekoi onodera_kosaki school_swimsuit swimsuits watanabe_akio" width='150' height='84'></a></td>
<td><img alt="icon" src="https://yande.re/favicon.ico" class="service-icon">yande.re</td>
<td>1920�1080 [Ero]</td>
<td>95% similarity</td></tr></table></div>
 */
