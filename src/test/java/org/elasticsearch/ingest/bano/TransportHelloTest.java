package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.core.Is.is;

/**
 * Created by johngqjiang on 2017/6/1.
 */
@ESIntegTestCase.ClusterScope(transportClientRatio = 0.9)
public class TransportHelloTest extends ESIntegTestCase {
    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(IngestBanoPlugin.class);
    }

    @Override
    protected Collection<Class<? extends Plugin>> transportClientPlugins() {
        return Collections.singletonList(IngestBanoPlugin.class);
    }

    public void testHelloWithFuture() throws ExecutionException, InterruptedException {
        HelloRequest request = new HelloRequest();
        request.setName("David");
        ActionFuture<HelloResponse> future = client().execute(HelloAction.INSTANCE, request);

        // Do something else if you wish...

        HelloResponse response = future.get();
        assertThat(response.getMessage(), is("Hello David!"));
    }

    public void testHelloWithFutureInlined() throws ExecutionException, InterruptedException {
        HelloRequest request = new HelloRequest();
        request.setName("David");
        HelloResponse response = client().execute(HelloAction.INSTANCE, request).get();
        assertThat(response.getMessage(), is("Hello David!"));
    }

    public void testHelloWithListener() throws ExecutionException, InterruptedException {
        HelloRequest request = new HelloRequest();
        request.setName("David");

        final Boolean[] success = {false};

        client().execute(HelloAction.INSTANCE, request, new ActionListener<HelloResponse>() {
            @Override
            public void onResponse(HelloResponse helloResponse) {
                assertThat(helloResponse.getMessage(), is("Hello David!"));
                success[0] = true;
            }

            @Override
            public void onFailure(Exception e) {
                fail("We got an error: " + e.getMessage());
            }
        });

        awaitBusy(() -> success[0]);
    }
}
