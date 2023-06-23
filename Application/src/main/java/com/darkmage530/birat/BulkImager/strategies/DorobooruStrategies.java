package com.darkmage530.birat.BulkImager.strategies;

import com.darkmage530.DerpClient;
import com.darkmage530.birat.BulkImager.BirtConfiguration;
import com.darkmage530.birat.BulkImager.Boorus.BooruResponse;
import com.darkmage530.birat.BulkImager.Boorus.DanbooruApi;
import com.darkmage530.birat.BulkImager.Boorus.TagCategories;
import com.darkmage530.birat.BulkImager.Driver;
import com.darkmage530.birat.BulkImager.Output.PictureOutput;
import com.darkmage530.birat.BulkImager.PictureFile;
import com.darkmage530.birat.DorobooruService;
import com.darkmage530.birat.posts.PostFile;
import com.darkmage530.birat.posts.PostRequest;
import com.darkmage530.birat.posts.PostResponse;
import com.darkmage530.birat.tags.TagCategory;
import com.darkmage530.birat.tags.TagRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DorobooruStrategies implements Activity {

    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    private BirtConfiguration config;
    private DorobooruService dorobooruService = new DorobooruService();
    private PictureOutput outputResult;

    public DorobooruStrategies(BirtConfiguration config, PictureOutput outputResult) {
        this.config = config;
        this.outputResult = outputResult;
    }

    @Override
    public void foundPicture(File file) {
        foundPicture(file, config.getEndLocation());

        for (Map.Entry<TagCategory, Set<String>> tagCategory : allCategoryTags.entrySet()) {
            for (String tag : tagCategory.getValue()) {
                dorobooruService.updateTag(tag, new TagRequest(tagCategory.getKey(), 1));
            }
        }
    }

    private final DanbooruApi danbooruApi = new DanbooruApi(new DerpClient());
    private Map<TagCategory, Set<String>> allCategoryTags = new HashMap<>();
    private int count = 0;
    private void foundPicture(File file, File moveRoot) {
        try {
            try {
                Thread.sleep(100);
            } catch (
                    InterruptedException e) {
                throw new RuntimeException(e);
            }

            PictureFile pictureFile = new PictureFile(file, moveRoot);
            Pair<BooruResponse, TagCategories> pairOfPostInfo = danbooruApi.getByMd5(pictureFile.getMd5());
            if (pairOfPostInfo == null) {
                return;
            }
            PostFile postFile = PostFile.Companion.invoke(pictureFile.asPath().toString());
            BooruResponse booruResponse = pairOfPostInfo.getLeft();
            PostRequest postRequest = new PostRequest(booruResponse.getTags(), booruResponse.getSafety(), booruResponse.getSource());
            PostResponse response = dorobooruService.createPost(postRequest, postFile);
            if (response.getStatus() == 200) {
                for (Map.Entry<TagCategory, Set<String>> tagCategory : pairOfPostInfo.getRight().getTagCategories().entrySet()) {
                    if (tagCategory.getKey() != null || tagCategory.getValue() != null) {
                        if (allCategoryTags.containsKey(tagCategory.getKey())) {
                            allCategoryTags.get(tagCategory.getKey()).addAll(tagCategory.getValue());
                        } else {
                            allCategoryTags.put(tagCategory.getKey(), tagCategory.getValue());
                        }
                    }
                }
            } else {
                System.out.println("Returned back " + response.getStatus() + " for image " + file.getName() + " could be duplicate");
            }

            if (count > 300) {
                for (Map.Entry<TagCategory, Set<String>> tagCategory : allCategoryTags.entrySet()) {
                    for (String tag : tagCategory.getValue()) {
                        dorobooruService.updateTag(tag, new TagRequest(tagCategory.getKey(), 1));
                    }
                }
                allCategoryTags = new HashMap<>();
                count = 0;
            }

//            }

            //move picture
            String directoryName;
            Set<String> copyrights = pairOfPostInfo.getRight().getTagCategories().get(TagCategory.Copyright);
            if (copyrights == null) {
                directoryName = "2. Unknown Copyrights";
            }
            else if (copyrights.size() > 1) {
                if (copyrights.size() > 3) {
                    directoryName = "1. Various Copyrights";
                } else {
                    directoryName = copyrights.stream().sorted().collect(Collectors.joining(", "));
                }
            } else if (copyrights.size() == 1) {
                directoryName = copyrights.stream().findFirst().get();
            } else {
                directoryName = "2. Unknown Copyrights";
            }

            count++;

            outputResult.move(pictureFile, directoryName.replaceAll("[<>:\"\\\\\\/|?*]", "_").replaceAll("__", "_"));
        } catch (Throwable e) {
            System.out.println("Swallowing exception for image " + file.getName() + " error: " + e.toString());
        }
    }
}
