package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoAction extends Action<BanoRequest, BanoResponse, BanoRequestBuilder> {

    public static final BanoAction INSTANCE = new BanoAction();
    public static final String NAME = "cluster:admin/bano";

    private BanoAction() {
        super(NAME);
    }

    @Override
    public BanoResponse newResponse() {
        return new BanoResponse();
    }

    @Override
    public BanoRequestBuilder newRequestBuilder(ElasticsearchClient elasticsearchClient) {
        return new BanoRequestBuilder(elasticsearchClient, INSTANCE);
    }
}
