package org.elasticsearch.ingest.bano;

import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.elasticsearch.test.rest.ESRestTestCase.entityAsMap;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoRestIT extends AbstractITCase {
    public void testHelloWorld() throws Exception {
        Response response = client.performRequest("GET", "/_hello");
        assertThat(entityAsMap(response), hasEntry("message", "Hello World!"));
    }

    public void testHelloDaviddWithQueryParameters() throws Exception {
        Response response = client.performRequest("GET", "/_hello?name=David");
        assertThat(entityAsMap(response), hasEntry("message", "Hello David!"));
    }

    public void testHelloDavidWithURLParameters() throws Exception {
        Response response = client.performRequest("GET", "/_hello/David");
        assertThat(entityAsMap(response), hasEntry("message", "Hello David!"));
    }

    public void testHelloDavidWithGetBody() throws Exception {
        Response response = client.performRequest("GET", "/_hello",
                Collections.emptyMap(), new StringEntity("{\"name\":\"David\"}"));
        assertThat(entityAsMap(response), hasEntry("message", "Hello David!"));
    }

    public void testHelloDavidWithPostBody() throws Exception {
        Response response = client.performRequest("POST", "/_hello",
                Collections.emptyMap(), new StringEntity("{\"name\":\"David\"}"));
        assertThat(entityAsMap(response), hasEntry("message", "Hello David!"));
    }

    @SuppressWarnings("unchecked")
    public void testBano() throws Exception {
        // We first create some indices
        int numIndices = randomIntBetween(1, 10);
        for (int i = 0; i < numIndices; i++) {
            client.performRequest("PUT", "/.bano-" + i);
        }
        // We create some other indices
        int numOtherIndices = randomIntBetween(1, 10);
        for (int i = 0; i < numOtherIndices; i++) {
            client.performRequest("PUT", "/" + randomAsciiOfLengthBetween(6, 10).toLowerCase(Locale.getDefault()));
        }

        client.performRequest("GET", "/_cluster/health", Collections.singletonMap("wait_for_status", "yellow"));

        Response response = client.performRequest("GET", "/_bano");
        Map<String, Object> responseMap = entityAsMap(response);
        assertThat(responseMap, hasKey("bano"));
        List<String> bano = (List<String>) responseMap.get("bano");
        for (int i = 0; i < numIndices; i++) {
            assertThat(bano, hasItem(".bano-" + i));
        }
    }
}
