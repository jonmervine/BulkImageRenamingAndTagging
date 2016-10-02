package main.BooruScraper;

import main.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Shirobako on 6/20/2016.
 */
public class IqdbSearcher {

    private static final Logger log = LoggerFactory.getLogger(IqdbSearcher.class);

    public static final String IQDB_URL = "http://iqdb.org/";

    public Image processFile(File file) throws IqdbException {
        if (file.exists() && file.isFile()) {
            return SearchIqdb(file);
        } else {
            throw new IqdbException("File: " + file.getAbsolutePath() + " does not exist or is not a file");
        }
    }

    private Image SearchIqdb(File file) throws IqdbException {
        Image image = null;
        try {
            Document doc = Jsoup.connect(IQDB_URL).data("file", file.getName(), new FileInputStream(file)).post();

            //doc is the entire raw html page
            Elements elements = doc.getElementsByTag("table");
            //elements is a set of all of the <table> elements

            for (Element element : elements) {
                //iterating through all the table elements. First one is your uploaded image
                //Last <td> is the image dimension

                //Each table is one "card" on iqdb so only the second one will be the "best match"
                //Second element will provide all the information you need. going into the similarity and all that is a waste of time

                //Use the similarity search for Best Match to identify the element that is best match if you don't trust the 2nd.

                //can similarly search for "Additional match" to find all teh additional matches. Anything beyond additional matches is a waste of time.

                Elements noMatch = element.getElementsContainingText("No relevant matches");
                if (noMatch.size() > 0) {
                    log.error("There was no relevant match for this");
                    return null;
                    //todo we should probably move this image to a holding folder of some kind
                }

                Elements bestMatch = element.getElementsContainingText("Best Match");
                if (bestMatch.size() > 0) {
                    image = getImage(element);
                    if (image != null) {
                        break;
                    } else {
                        continue;
                    }
                }

                Elements additionalMatches = element.getElementsContainingText("Additional match");
                if (additionalMatches.size() > 0) {
                    image = getImage(element);
                    if(image != null) {
                        break;
                    } else {
                        continue;
                    }
                }
            }

        } catch (IOException e) {

        }
        if (image != null) {
            image.setLocation(file);
        }
        return image;
    }

    private Image getImage(Element element) throws IqdbException {
        IqdbParser parser = new IqdbParser();
        Image image = parser.processTable(element);
        if (image == null) {
            return null;
        } else {
            return image;
        }
    }
}
