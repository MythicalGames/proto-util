package games.mythical.proto_util.transform;

import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class String2Uuid implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof String && target == UUID.class;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        if (StringUtils.isNotBlank((String) source)) {
            return UUID.fromString((String) source);
        }
        return null;
    }
}
