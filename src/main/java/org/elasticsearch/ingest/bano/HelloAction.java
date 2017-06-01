package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class HelloAction extends Action<HelloRequest, HelloResponse, HelloRequestBuilder> {

    public static final HelloAction INSTANCE = new HelloAction();
    public static final String NAME = "cluster:admin/hello";

    private HelloAction() {
        super(NAME);
    }

    @Override
    public HelloResponse newResponse() {
        return new HelloResponse();
    }

    @Override
    public HelloRequestBuilder newRequestBuilder(ElasticsearchClient elasticsearchClient) {
        return new HelloRequestBuilder(elasticsearchClient, INSTANCE);
    }
}
