package games.mythical.proto_util.transform;

import games.mythical.proto_util.helper.CurrencyHelper;
import java.math.BigDecimal;

public class BigDecimal2String implements Transformer {

    @Override
    public boolean condition(Object source, Class<?> target, boolean toProto) {
        return source instanceof BigDecimal && toProto;
    }

    @Override
    public Object transform(Object source, Class<?> target, boolean toProto) {
        return CurrencyHelper.bigDecimalToString((BigDecimal) source);
    }
}
