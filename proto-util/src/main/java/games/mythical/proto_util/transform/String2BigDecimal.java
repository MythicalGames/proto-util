package games.mythical.proto_util.transform;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;

public class String2BigDecimal implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof String && target == BigDecimal.class;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        if (StringUtils.isNotBlank((String) source)) {
            return new BigDecimal(source.toString());
        }
        return null;
    }
}
