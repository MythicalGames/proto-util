package games.mythical.proto_util.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyHelper {
  public static final int CURRENCY_DIGITS = 8;

  public static String bigDecimalToString(BigDecimal value) {
    return value.setScale(CURRENCY_DIGITS, RoundingMode.HALF_UP).toPlainString();
  }
}
