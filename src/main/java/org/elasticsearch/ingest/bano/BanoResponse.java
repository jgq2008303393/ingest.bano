package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoResponse extends ActionResponse implements ToXContent {

    private final List<String> indices;

    public BanoResponse() {
        indices = new ArrayList<>();
    }

    public void setIndices(List<String> indices) {
        for (String index : indices) {
            this.indices.add(index);
        }
    }

    public void addIndex(String index) {
        this.indices.add(index);
    }

    public List<String> getIndices() {
        return indices;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        Long len = in.readLong();
        for (int i = 0; i < len; ++i) {
            indices.add(in.readString());
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeLong(indices.size());
        for (String index : indices) {
            out.writeString(index);
        }
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startArray("bano");
        for (String index : indices) {
            builder.value(index);
        }
        return builder.endArray();
    }
}