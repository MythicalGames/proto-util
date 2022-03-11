package games.mythical.proto_util.transform;

import com.google.protobuf.Timestamp;
import games.mythical.proto_util.helper.DateHelper;
import org.apache.commons.lang3.StringUtils;

public class String2ProtoTimestamp implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof String && target == Timestamp.class;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        if (StringUtils.isNotBlank((String) source)) {
            return DateHelper.stringToProtoTimestamp((String) source);
        }
        return null;
    }
}
