package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoTransportAction extends HandledTransportAction<BanoRequest, BanoResponse> {

    private final ClusterService clusterService;

    @Inject
    public BanoTransportAction(Settings settings, ThreadPool threadPool, ActionFilters actionFilters,
                               IndexNameExpressionResolver resolver, TransportService transportService,
                               ClusterService clusterService) {
        super(settings, BanoAction.NAME, threadPool, transportService, actionFilters, resolver, BanoRequest::new);
        this.clusterService = clusterService;
    }

    @Override
    protected void doExecute(BanoRequest request, ActionListener<BanoResponse> listener) {
        String indices = ".bano-";

        if (request.getDept() != null) {
            indices += request.getDept();
        } else {
            indices += "*";
        }

        BanoResponse response = new BanoResponse();

        try {
            GetIndexRequest indexRequest = new GetIndexRequest().indices(indices);
            String[] concreteIndices = indexNameExpressionResolver.concreteIndexNames(clusterService.state(), indexRequest);

            for (String index : concreteIndices) {
                response.addIndex(index);
            }

            listener.onResponse(response);
        } catch (IndexNotFoundException e) {
            listener.onResponse(response);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }
}