package com.darkmage530.birat.BulkImager.Iqdb;

import com.darkmage530.birat.BulkImager.Metadata.RatingSearch;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by DarkMage530 on 3/5/2017. For BulkImageRenamingAndTagging
 * Current User Shirobako
 */
//package-private
class IqdbDocument {

    private static final Logger log = LoggerFactory.getLogger(IqdbDocument.class);

    //Document is the entire raw html page
    private Document doc;
    private List<IqdbElement> elements;

    public IqdbDocument(Document doc) {
        this.doc = doc;
    }

    public RatingSearch explode() {
        //Elements is a set of all of the <table> elements
        Elements elements = doc.getElementsByTag("table");
        IqdbMatch bestMatch = null;
        List<IqdbMatch> additionalMatches = new LinkedList<>();
        for (int i = 1; i < elements.size(); i++) {
            //iterating through all the table elements. First one is your uploaded image

            //Each table is one "card" on iqdb so only the second one will be the "best match"
            Element element = elements.get(i);
            IqdbElement iqdbElement = new IqdbElement(element);

            Elements noMatch = element.getElementsContainingText("No relevant matches");
            if (noMatch.size() > 0) {
                log.warn("There was no relevant match for this document");
//                break;
                return null;
            }
//            TODO I don't think we can determine if these are even relevant. So once we see 'No relevent matches' we can
//            just call it there. We won't need 'Possible Match' yet
//            Elements possibleMatchElement = element.getElementsContainingText("Possible match");
//            if (possibleMatchElement.size() > 0) {
//                additionalMatches.add(iqdbElement.explode(IqdbMatchType.NO_RELEVANT_BUT_POSSIBLE));
//            }

            //Second element will provide all the information you need. going into the similarity and all that is a waste of time
            //Use the similarity search for Best Match to identify the element that is best match if you don't trust the 2nd.
            Elements bestMatchElement = element.getElementsContainingText("Best Match");
            if (bestMatchElement.size() > 0) {
                bestMatch = iqdbElement.explode(IqdbMatchType.BEST);
            }

            //can similarly search for "Additional match" to find all teh additional matches. Anything beyond additional matches is a waste of time.
            Elements additionalMatchElements = element.getElementsContainingText("Additional match");
            if (additionalMatchElements.size() > 0) {
                additionalMatches.add(iqdbElement.explode(IqdbMatchType.ADDITIONAL));
            }
        }

        if (bestMatch == null && !additionalMatches.isEmpty()) {
            log.warn("No Best Match found, taking first additionalMatch, does this ever happen?");
            bestMatch = additionalMatches.remove(0);
        }

        return null;//new RatingSearch(bestMatch, additionalMatches);
    }
}
