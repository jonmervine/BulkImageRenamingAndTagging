package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Shirobako on 4/24/2016.
 */
public class FileNavigatorTest {
    @Test
    public void testScanDirectory() {
//        FileNavigator fileNavigator = new FileNavigator(new File("D:\\P\\Fiction"));
        FileNavigator fileNavigator = new FileNavigator(new File("D:\\Downloads\\derp"));
        int count = fileNavigator.scanDirectories();
//        Assert.assertEquals(36821, count);
    }


    @Test
    public void testBooruLookup() {
        try {
            File file = new File("D:\\Downloads\\derp\\Album Fqedd - Imgur\\029 - UESTINJ.jpg");
            if (file.exists() && file.isFile()) {
                Document doc = Jsoup.connect("http://iqdb.org/").data("file", file.getName(), new FileInputStream(file)).post();

                Elements elements = doc.getElementsByTag("table");

                for (Element element : elements) {
                    Elements similarity = element.getElementsContainingText("Best Match");
                    if (similarity.size() > 0) {
                        for (Element ele2 : element.getAllElements()) {


                            if (ele2.getElementsByAttributeValueContaining("src", "danbooru.donmai").size() > 0) {
                                Elements derpy = ele2.getElementsByAttributeValueContaining("src", "danbooru.donmai");
                                System.out.println("derppppp");
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("shit fucked up");
        }

    }

    @Test
    public void testDanbooruAPIGet() {
        //https://danbooru.donmai.us/posts/1.json
    }
}