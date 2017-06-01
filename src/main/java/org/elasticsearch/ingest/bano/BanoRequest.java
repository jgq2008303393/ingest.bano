package org.elasticsearch.ingest.bano;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.XContentHelper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by johngqjiang on 2017/6/1.
 */
public class BanoRequest extends ActionRequest<BanoRequest> {
    private String dept;

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setRestContent(BytesReference restContent) {
        // Let's try to find the dept from the body
        Map<String, Object> map = XContentHelper.convertToMap(restContent, false).v2();
        if (map.containsKey("dept")) {
            dept = (String) map.get("dept");
        }
    }

    public String getDept() {
        return dept;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        dept = in.readOptionalString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeOptionalString(dept);
    }

    @Override
    public ActionRequestValidationException validate() {
        /*
        ActionRequestValidationException validationException = null;
        if (dept == null) {
            validationException = new ActionRequestValidationException();
            validationException.addValidationError("You must provide a dept");
            return validationException;
        }*/
        return null;
    }
}