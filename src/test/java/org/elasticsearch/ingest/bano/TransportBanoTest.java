package org.elasticsearch.ingest.bano;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Before;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * Created by johngqjiang on 2017/6/1.
 */
@ESIntegTestCase.ClusterScope(transportClientRatio = 0.9)
public class TransportBanoTest extends ESIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return Collections.singletonList(IngestBanoPlugin.class);
    }

    @Override
    protected Collection<Class<? extends Plugin>> transportClientPlugins() {
        return Collections.singletonList(IngestBanoPlugin.class);
    }

    private int numIndices;

    @Before
    public void createIndices() {
        // We first create some indices
        numIndices = randomIntBetween(1, 10);
        for (int i = 0; i < numIndices; i++) {
            createIndex(".bano-" + i);
        }

        // We create some manual indices
        createIndex(".bano-17", ".bano-29", ".bano-95");

        // We create some other indices
        int numOtherIndices = randomIntBetween(1, 10);
        for (int i = 0; i < numOtherIndices; i++) {
            createIndex(randomAsciiOfLengthBetween(6, 10).toLowerCase(Locale.getDefault()));
        }

        ensureYellow();
    }

    public void testBanoNoDept() throws ExecutionException, InterruptedException {
        BanoRequest request = new BanoRequest();
        BanoResponse response = client().execute(BanoAction.INSTANCE, request).get();
        for (int i = 0; i < numIndices; i++) {
            assertThat(response.getIndices(), hasItem(".bano-" + i));
        }
        assertThat(response.getIndices(), hasItem(".bano-17"));
        assertThat(response.getIndices(), hasItem(".bano-29"));
        assertThat(response.getIndices(), hasItem(".bano-95"));
    }

    public void testBanoOneDept() throws ExecutionException, InterruptedException {
        BanoRequest request = new BanoRequest();
        request.setDept("17");
        BanoResponse response = client().execute(BanoAction.INSTANCE, request).get();
        assertThat(response.getIndices(), iterableWithSize(1));
        assertThat(response.getIndices(), hasItem(".bano-17"));
    }

    public void testBanoNonExistingDept() throws ExecutionException, InterruptedException {
        BanoRequest request = new BanoRequest();
        request.setDept("idontbelieverandomizedtestingwillgeneratethat");
        BanoResponse response = client().execute(BanoAction.INSTANCE, request).get();
        assertThat(response.getIndices(), iterableWithSize(0));
    }
}
