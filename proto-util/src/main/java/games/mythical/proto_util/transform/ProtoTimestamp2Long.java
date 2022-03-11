package games.mythical.proto_util.transform;

import com.google.protobuf.Timestamp;
import games.mythical.proto_util.helper.DateHelper;

public class ProtoTimestamp2Long implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof Timestamp && (target == Long.class || target == Long.TYPE);
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        return DateHelper.protoTimestampToEpochMillis((Timestamp) source);
    }
}
