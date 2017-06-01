package org.elasticsearch.ingest.bano;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.RestToXContentListener;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;

/**
 * Created by johngqjiang on 2017/6/1.
 */

public class BanoRestAction extends BaseRestHandler {

    @Inject
    public BanoRestAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "/_bano", this);
    }

    /*
    class Indices implements ToXContent {

        private final List<String> indices;

        public Indices() {
            indices = new ArrayList<>();
        }

        public void addIndex(String index) {
            indices.add(index);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            builder.startArray("bano");
            for (String index : indices) {
                builder.value(index);
            }
            return builder.endArray();
        }
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
        return channel -> client.admin().indices().prepareGetIndex()
                .setIndices(".bano*")
                .execute(new RestBuilderListener<GetIndexResponse>(channel) {
                    @Override
                    public RestResponse buildResponse(GetIndexResponse getIndexResponse, XContentBuilder builder) throws Exception {
                        Indices indices = new Indices();
                        for (String index : getIndexResponse.getIndices()) {
                            indices.addIndex(index);
                        }
                        builder.startObject();
                        indices.toXContent(builder, restRequest);
                        builder.endObject();
                        return new BytesRestResponse(RestStatus.OK, builder);
                    }
                });
    }*/
    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
        BanoRequest request = new BanoRequest();

        String dept = restRequest.param("dept");
        if (dept != null) {
            request.setDept(dept);
        }

        return channel -> client.execute(BanoAction.INSTANCE, request, new RestToXContentListener<>(channel));
    }
}
