package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class HelloRequestBuilder extends ActionRequestBuilder<HelloRequest, HelloResponse, HelloRequestBuilder> {

    public HelloRequestBuilder(ElasticsearchClient client, Action<HelloRequest, HelloResponse, HelloRequestBuilder> action) {
        super(client, action, new HelloRequest());
    }

}