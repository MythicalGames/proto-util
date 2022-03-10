package games.mythical.proto_util.transform;

import java.util.UUID;

public class Uuid2String implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof UUID && toProto;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        return source.toString();
    }
}
