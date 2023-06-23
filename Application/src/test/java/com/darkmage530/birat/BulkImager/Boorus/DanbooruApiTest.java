package com.darkmage530.birat.BulkImager.Boorus;

import com.darkmage530.DerpClient;
import com.darkmage530.birat.posts.Safety;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DanbooruApiTest {

    @Mock
    private DerpClient derpyderp;

    @Test
    public void getMd5AndParseResults() {
        Map<String, Object> expectedJsonResponse = new Gson().fromJson(DanbooruExampleResponse.exampleResponse, Map.class);
        when(derpyderp.execute(any())).thenReturn(expectedJsonResponse);

        DanbooruApi api = new DanbooruApi(derpyderp);
        Pair<BooruResponse, TagCategories> result = api.getByMd5("8a95e541a5ea7b3792f201985ba566c7");

        assertEquals(Safety.safe, result.getLeft().getSafety());
        assertEquals("https://i.pximg.net/img-original/img/2022/08/12/06/03/28/100418319_p0.jpg", result.getLeft().getSource());
    }
}
