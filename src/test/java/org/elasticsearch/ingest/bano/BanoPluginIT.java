package org.elasticsearch.ingest.bano;

import org.elasticsearch.client.Response;

import java.util.List;
import java.util.Map;

import static org.elasticsearch.test.rest.ESRestTestCase.entityAsMap;
import static org.hamcrest.core.Is.is;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoPluginIT extends AbstractITCase {

    @SuppressWarnings("unchecked")
    public void testPluginIsLoaded() throws Exception {

        Response response = client.performRequest("GET", "/_nodes/plugins");

        Map<String, Object> nodes = (Map<String, Object>) entityAsMap(response).get("nodes");
        for (String nodeName : nodes.keySet()) {
            boolean pluginFound = false;
            Map<String, Object> node = (Map<String, Object>) nodes.get(nodeName);
            List<Map<String, Object>> plugins = (List<Map<String, Object>>) node.get("plugins");
            for (Map<String, Object> plugin : plugins) {
                String pluginName = (String) plugin.get("name");
                if (pluginName.equals("ingest-bano")) {
                    pluginFound = true;
                    break;
                }
            }
            assertThat(pluginFound, is(true));
        }
    }
}
