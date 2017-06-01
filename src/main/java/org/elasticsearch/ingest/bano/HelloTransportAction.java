package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Created by johngqjiang on 2017/6/1.
 */

public class HelloTransportAction extends HandledTransportAction<HelloRequest, HelloResponse> {

    @Inject
    public HelloTransportAction(Settings settings, ThreadPool threadPool, ActionFilters actionFilters,
                                IndexNameExpressionResolver resolver, TransportService transportService) {
        super(settings, HelloAction.NAME, threadPool, transportService, actionFilters, resolver, HelloRequest::new);
    }

    @Override
    protected void doExecute(HelloRequest request, ActionListener<HelloResponse> listener) {
        try {
            String name = request.getName();
            if (name == null) {
                name = "World";
            }
            HelloResponse response = new HelloResponse();
            response.setMessage("Hello " + name + "!");
            listener.onResponse(response);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }
}
