package org.elasticsearch.ingest.bano;

import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.test.rest.ESRestTestCase.entityAsMap;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoProcessorIT extends AbstractITCase {

    @SuppressWarnings("unchecked")
    public void testSimulateProcessor() throws Exception {
        String json = jsonBuilder().startObject()
                .startObject("pipeline")
                .startArray("processors")
                .startObject()
                .startObject("bano")
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                .startArray("docs")
                .startObject()
                .field("_index", "index")
                .field("_type", "type")
                .field("_id", "id")
                .startObject("_source")
                .field("foo", "bar")
                .endObject()
                .endObject()
                .endArray()
                .endObject().string();

        Map<String, Object> expected = new HashMap<>();
        expected.put("foo", "bar");
        expected.put("new_foo", "bar");

        Response response = client.performRequest("POST", "/_ingest/pipeline/_simulate",
                Collections.emptyMap(), new NStringEntity(json, ContentType.APPLICATION_JSON));

        Map<String, Object> responseMap = entityAsMap(response);
        assertThat(responseMap, hasKey("docs"));
        List<Map<String, Object>> docs = (List<Map<String, Object>>) responseMap.get("docs");
        assertThat(docs.size(), equalTo(1));
        Map<String, Object> doc1 = docs.get(0);
        assertThat(doc1, hasKey("doc"));
        Map<String, Object> doc = (Map<String, Object>) doc1.get("doc");
        assertThat(doc, hasKey("_source"));
        Map<String, Object> docSource = (Map<String, Object>) doc.get("_source");

        assertThat(docSource, is(expected));
    }

    @SuppressWarnings("unchecked")
    private void simulatePipeline(String json, Map<String, Object> expected) throws IOException {
        Response response = client.performRequest("POST", "/_ingest/pipeline/_simulate",
                Collections.emptyMap(), new NStringEntity(json, ContentType.APPLICATION_JSON));

        Map<String, Object> responseMap = entityAsMap(response);
        assertThat(responseMap, hasKey("docs"));
        List<Map<String, Object>> docs = (List<Map<String, Object>>) responseMap.get("docs");
        assertThat(docs.size(), equalTo(1));
        Map<String, Object> doc1 = docs.get(0);
        assertThat(doc1, hasKey("doc"));
        Map<String, Object> doc = (Map<String, Object>) doc1.get("doc");
        assertThat(doc, hasKey("_source"));
        Map<String, Object> docSource = (Map<String, Object>) doc.get("_source");

        assertThat(docSource, is(expected));
    }

    public void testSimulateProcessorConfigSource() throws Exception {
        String json = jsonBuilder().startObject()
                .startObject("pipeline")
                .startArray("processors")
                .startObject()
                .startObject("bano")
                .field("source", "anotherfoo")
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                .startArray("docs")
                .startObject()
                .field("_index", "index")
                .field("_type", "type")
                .field("_id", "id")
                .startObject("_source")
                .field("anotherfoo", "bar")
                .endObject()
                .endObject()
                .endArray()
                .endObject().string();

        Map<String, Object> expected = new HashMap<>();
        expected.put("anotherfoo", "bar");
        expected.put("new_anotherfoo", "bar");

        simulatePipeline(json, expected);
    }

    public void testSimulateProcessorConfigTarget() throws Exception {
        String json = jsonBuilder().startObject()
                .startObject("pipeline")
                .startArray("processors")
                .startObject()
                .startObject("bano")
                .field("source", "anotherfoo")
                .field("target", "another_new_foo")
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                .startArray("docs")
                .startObject()
                .field("_index", "index")
                .field("_type", "type")
                .field("_id", "id")
                .startObject("_source")
                .field("anotherfoo", "bar")
                .endObject()
                .endObject()
                .endArray()
                .endObject().string();

        Map<String, Object> expected = new HashMap<>();
        expected.put("anotherfoo", "bar");
        expected.put("another_new_foo", "bar");

        simulatePipeline(json, expected);
    }

    public void testSimulateProcessorConfigRemove() throws Exception {
        String json = jsonBuilder().startObject()
                .startObject("pipeline")
                .startArray("processors")
                .startObject()
                .startObject("bano")
                .field("remove", true)
                .endObject()
                .endObject()
                .endArray()
                .endObject()
                .startArray("docs")
                .startObject()
                .field("_index", "index")
                .field("_type", "type")
                .field("_id", "id")
                .startObject("_source")
                .field("foo", "bar")
                .endObject()
                .endObject()
                .endArray()
                .endObject().string();

        Map<String, Object> expected = new HashMap<>();
        expected.put("new_foo", "bar");

        simulatePipeline(json, expected);
    }
}
