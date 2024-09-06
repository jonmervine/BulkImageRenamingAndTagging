package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.JavaHttpClient;
import com.darkmage530.birat.posts.Safety;
import com.darkmage530.birat.tags.TagCategory;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GelbooruApiTest {

    @Mock
    private JavaHttpClient mockClient;

    @Before
    public void init() { MockitoAnnotations.openMocks(this);}

    @Test
    public void getMd5AndParseResults() {
        Map<String, Object> expectedGelbooruResponse = new Gson().fromJson(ApiExampleResponses.gelbooruResponse, Map.class);
        Map<String, Object> expectedTagResponse = new Gson().fromJson(ApiExampleResponses.gelbooruTagsResponse, Map.class);
        when(mockClient.execute(any()))
                .thenReturn(Optional.of(expectedGelbooruResponse))
                .thenReturn(Optional.of(expectedTagResponse));

        GelbooruApi api = new GelbooruApi(mockClient);
        Optional<BooruResponse> optResult = api.getByMd5("8a95e541a5ea7b3792f201985ba566c7");

        assertTrue(optResult.isPresent());
        BooruResponse result = optResult.get();

        assertEquals(Safety.safe, result.getSafety());
        assertEquals("https://www.pixiv.net/artworks/100418319", result.getSource());

        assertEquals(4, result.getTagCategories().entrySet().size());
        assertEquals(2, (result.getTagCategories().get(TagCategory.Copyright)).size());
        assertEquals(1, ( result.getTagCategories().get(TagCategory.Artist)).size());
        assertEquals(1, (result.getTagCategories().get(TagCategory.Meta)).size());
        assertEquals(1, (result.getTagCategories().get(TagCategory.Character)).size());
        assertEquals("saijo_juri", (result.getTagCategories().get(TagCategory.Character)).toArray()[0].toString());
        assertEquals("8n0_g", (result.getTagCategories().get(TagCategory.Artist)).toArray()[0].toString());
        assertEquals("highres", (result.getTagCategories().get(TagCategory.Meta)).toArray()[0].toString());
        assertEquals(Sets.newHashSet("idolmaster", "idolmaster_shiny_colors"),
                (result.getTagCategories().get(TagCategory.Copyright)));


    }
}
