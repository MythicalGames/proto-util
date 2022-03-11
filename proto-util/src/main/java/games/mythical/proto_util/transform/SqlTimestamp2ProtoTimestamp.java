package games.mythical.proto_util.transform;

import games.mythical.proto_util.helper.DateHelper;

public class SqlTimestamp2ProtoTimestamp implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof java.sql.Timestamp && target == com.google.protobuf.Timestamp.class;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        return DateHelper.sqlTimestamptoProtoTimestamp((java.sql.Timestamp) source);
    }
}
