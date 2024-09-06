package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.JavaHttpClient;
import com.darkmage530.birat.posts.Safety;
import com.darkmage530.birat.tags.TagCategory;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DanbooruApiTest {

    @Mock
    private JavaHttpClient mockClient;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getMd5AndParseResults() {
        Map<String, Object> expectedJsonResponse = new Gson().fromJson(ApiExampleResponses.danbooruResponse, Map.class);
        when(mockClient.execute(any())).thenReturn(Optional.of(expectedJsonResponse));

        DanbooruApi api = new DanbooruApi(mockClient);
        Optional<BooruResponse> optResult = api.getByMd5("8a95e541a5ea7b3792f201985ba566c7");

        assertTrue(optResult.isPresent());
        BooruResponse result = optResult.get();

        assertEquals(Safety.safe, result.getSafety());
        assertEquals("https://i.pximg.net/img-original/img/2022/08/12/06/03/28/100418319_p0.jpg", result.getSource());

        assertEquals(4, result.getTagCategories().entrySet().size());
        assertEquals(2, (result.getTagCategories().get(TagCategory.Copyright)).size());
        assertEquals("8n0_g", (result.getTagCategories().get(TagCategory.Artist)).toArray()[0].toString());
    }

    @Test
    public void getMd5FailsToFindOnDanbooru() {
        Map<String, Object> expectedJsonResponse = new Gson().fromJson(ApiExampleResponses.danbooruFailResponse, Map.class);
        when(mockClient.execute(any())).thenReturn(Optional.of(expectedJsonResponse));

        DanbooruApi api = new DanbooruApi(mockClient);
        Optional<BooruResponse> optResult = api.getByMd5("8a95e541a5ea7b3792f201985ba566c7");

        assertFalse(optResult.isPresent());
    }

    @Test
    public void exceptionThrownFromClient() {
        when(mockClient.execute(any())).thenThrow(new RuntimeException("this is a test exception"));

        DanbooruApi api = new DanbooruApi(mockClient);
        Optional<BooruResponse> optResult = api.getByMd5("8a95e541a5ea7b3792f201985ba566c7");

        assertFalse(optResult.isPresent());
    }
}
