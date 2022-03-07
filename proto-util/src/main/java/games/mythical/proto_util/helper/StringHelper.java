package games.mythical.proto_util.helper;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class StringHelper {
  public static String snakeToCamel(final String snake) {
    if (snake == null) {
      return null;
    }
    final var allCapped = Arrays.stream(StringUtils.split(snake.trim(), '_'))
        .map(StringUtils::capitalize)
        .collect(Collectors.joining());
    return StringUtils.uncapitalize(allCapped);
  }
}
