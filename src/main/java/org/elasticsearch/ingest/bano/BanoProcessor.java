package org.elasticsearch.ingest.bano;

/**
 * Created by johngqjiang on 2017/6/1.
 */

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.util.Map;

import static org.elasticsearch.ingest.ConfigurationUtils.readBooleanProperty;
import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public final class BanoProcessor extends AbstractProcessor {

    public static final class BanoFactory implements Processor.Factory {
        @Override
        public Processor create(Map<String, Processor.Factory> processorFactories, String tag, Map<String, Object> config)
                throws Exception {
            String source = readStringProperty(NAME, tag, config, "source", "foo");
            String target = readStringProperty(NAME, tag, config, "target", "new_" + source);
            Boolean remove = readBooleanProperty(NAME, tag, config, "remove", false);
            return new BanoProcessor(tag, source, target, remove);
        }
    }

    public final static String NAME = "bano";

    private final String sourceField;
    private final String targetField;
    private final Boolean removeOption;
    protected BanoProcessor(String tag, String sourceField, String targetField, Boolean removeOption) {
        super(tag);
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.removeOption = removeOption;
    }

    @Override
    public String getType() {
        return NAME;
    }

    @Override
    public void execute(IngestDocument ingestDocument) throws Exception {
        if (ingestDocument.hasField(sourceField)) {
            ingestDocument.setFieldValue(targetField, ingestDocument.getFieldValue(sourceField, String.class));
            if (removeOption) {
                ingestDocument.removeField(sourceField);
            }
        }
    }
}