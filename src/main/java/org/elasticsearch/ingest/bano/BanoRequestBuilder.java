package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoRequestBuilder extends ActionRequestBuilder<BanoRequest, BanoResponse, BanoRequestBuilder> {

    public BanoRequestBuilder(ElasticsearchClient client, Action<BanoRequest, BanoResponse, BanoRequestBuilder> action) {
        super(client, action, new BanoRequest());
    }

}