package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.ingest.Processor;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.IngestPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IngestBanoPlugin extends Plugin implements ActionPlugin, IngestPlugin {
    @Override
    public List<Class<? extends RestHandler>> getRestHandlers() {
        return Arrays.asList(HelloRestAction.class, BanoRestAction.class);
    }

    @Override
    public Map<String, Processor.Factory> getProcessors(Processor.Parameters parameters) {
        return Collections.singletonMap("bano", new BanoProcessor.BanoFactory());
    }

    @Override
    public List<ActionHandler<? extends ActionRequest<?>, ? extends ActionResponse>> getActions() {
        return Arrays.asList(
                new ActionHandler<>(HelloAction.INSTANCE, HelloTransportAction.class),
                new ActionHandler<>(BanoAction.INSTANCE, BanoTransportAction.class));
    }
}
