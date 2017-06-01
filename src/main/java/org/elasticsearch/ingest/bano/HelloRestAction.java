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
import static org.elasticsearch.rest.RestRequest.Method.POST;

/**
 * Created by johngqjiang on 2017/6/1.
 */

public class HelloRestAction extends BaseRestHandler {

    @Inject
    public HelloRestAction(Settings settings, RestController controller) {
        super(settings);
        // Register your handlers here
        controller.registerHandler(GET, "/_hello", this);
        controller.registerHandler(GET, "/_hello/{name}", this);
        controller.registerHandler(POST, "/_hello", this);
    }

    /*
    class Message implements ToXContent {

        private final String name;

        public Message(String name) {
            if (name == null) {
                this.name = "World";
            } else {
                this.name = name;
            }
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            return builder.field("message", "Hello " + name + "!");
        }
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
        // Implement the REST logic here
        String name = restRequest.param("name");
        if (name == null && restRequest.content().length() > 0) {
            // Let's try to find the name from the body
            Map<String, Object> map = XContentHelper.convertToMap(restRequest.content(), false).v2();
            if (map.containsKey("name")) {
                name = (String) map.get("name");
            }
        }

        String finalName = name;
        return channel -> {
            Message message = new Message(finalName);
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            message.toXContent(builder, restRequest);
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }*/

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
        HelloRequest request = new HelloRequest();

        String name = restRequest.param("name");
        if (name != null) {
            request.setName(name);
        } else if (restRequest.hasContent()){
            request.setRestContent(restRequest.content());
        }

        return channel -> client.execute(HelloAction.INSTANCE, request, new RestToXContentListener<>(channel));
    }
}
