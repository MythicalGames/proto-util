package games.mythical.proto_util;

import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.Timestamp;
import games.mythical.proto_util.dto.DtoConvert;
import games.mythical.proto_util.dto.DtoExclude;
import games.mythical.proto_util.dto.DtoJson;
import games.mythical.proto_util.helper.CurrencyHelper;
import games.mythical.proto_util.helper.DateHelper;
import games.mythical.proto_util.helper.JsonHelper;
import games.mythical.proto_util.helper.StringHelper;
import games.mythical.proto_util.proto.ProtoConvert;
import games.mythical.proto_util.proto.ProtoExclude;
import games.mythical.proto_util.proto.ProtoField;
import games.mythical.proto_util.proto.ProtoJson;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class will convert protocol buffer classes to Java beans using Lombok builder.
 */
public class ProtoUtil {
  private static final Logger log = LoggerFactory.getLogger(ProtoUtil.class);

  private static final String PROTO_BUILDER_NAME = "newBuilder";
  private static final String LOMBOK_BUILDER_NAME = "builder";
  private static final String BUILD_METHOD_NAME = "build";

  /**
   * Converts protocol buffer object to Lombok builder for Java bean. This method
   * should be used when some manual conversion needs to be done.
   *
   * @param source protocol buffer object to convert
   * @param dtoBuilderClass class of builder to create
   * @param <P> protocol buffer object type
   * @param <T> builder class
   * @return Lombok builder for Java bean
   */
  public static <P, T> T toDtoBuilder(final P source, final Class<T> dtoBuilderClass) {
    //noinspection unchecked
    return (T) convertToBuilder(source, getBuiltClassFromBuilder(dtoBuilderClass), false);
  }

  /**
   * Converts protocol buffer object to Lombok-based Java bean.
   *
   * @param source protocol buffer object to convert
   * @param resultClass class of Java bean
   * @param <P> protocol buffer object type
   * @param <T> Java bean type
   * @return Instance of Java bean populated with protocol buffer object data
   */
  public static <P, T> T toDto(final P source, final Class<T> resultClass) {
    return convert(source, resultClass, false);
  }

  /**
   * Converts Lombok-based Java bean to protocol buffer builder. This method
   * should be used when some manual conversion needs to be done.
   *
   * @param source Lombok-based Java bean to convert
   * @param protoBuilderClass class of builder to create
   * @param <S> Lombok-based Java bean type
   * @param <P> protocol buffer builder type
   * @return Protocol buffer builder
   */
  public static <S, P extends MessageOrBuilder> P toProtoBuilder(final S source, final Class<P> protoBuilderClass) {
    //noinspection unchecked
    return (P) convertToBuilder(source, getBuiltClassFromBuilder(protoBuilderClass), true);
  }

  /**
   * Converts Lombok-based Java bean to protocol buffer object.
   *
   * @param source Lombok-based Java bean object
   * @param protoClass class of protocol buffer type to create
   * @param <S> Lombok-based Java bean type
   * @param <P> protocol buffer object type
   * @return Instance of Protocol Buffer type populated with Java bean object data
   */
  public static <S, P extends MessageOrBuilder> P toProto(final S source, final Class<P> protoClass) {
    return convert(source, protoClass, true);
  }

  private static Object processValue(final Object sourceValue, final Field field, final Field targetField, final boolean toProto) {
    if (field.getAnnotation(ProtoJson.class) != null) {
      return JsonHelper.toJsonString(sourceValue);
    }
    if (field.getAnnotation(ProtoConvert.class) != null) {
      final var protoClass = field.getAnnotation(ProtoConvert.class).value();
      return toProto(sourceValue, protoClass);
    }
    if (targetField.getAnnotation(DtoConvert.class) != null) {
      final var dtoClass = targetField.getAnnotation(DtoConvert.class).value();
      return toDto(sourceValue, dtoClass);
    }
    if (targetField.getAnnotation(DtoJson.class) != null && sourceValue instanceof String) {
      if (StringUtils.isNotBlank((String)sourceValue)) {
        return JsonHelper.toStringObjectMap((String)sourceValue);
      }
      return Collections.emptyMap();
    }

    // Note: The proto's type might not match what is expected. Strings are objects, Enums are ints, etc...
    final var targetClass = targetField.getType();
    // Catch Enum to Enum cases before turning the Enum into a String
    if ((sourceValue.getClass() == targetClass) || (sourceValue instanceof ProtocolMessageEnum
        && targetClass == Integer.TYPE)) {
      return sourceValue;
    }
    if (sourceValue.getClass().isEnum()) {
      return ((Enum<?>)sourceValue).name();
    }
    if (sourceValue instanceof String && targetClass == Timestamp.class) {
      if (StringUtils.isNotBlank((String) sourceValue)) {
        return DateHelper.stringToProtoTimestamp((String) sourceValue);
      }
      return null;
    }
    if (sourceValue instanceof java.sql.Timestamp && targetClass == Timestamp.class) {
      return DateHelper.sqlTimestamptoProtoTimestamp((java.sql.Timestamp) sourceValue);
    }
    if (sourceValue instanceof Timestamp && (targetClass == Long.class || targetClass == Long.TYPE)) {
      return DateHelper.protoTimestampToEpochMillis((Timestamp) sourceValue);
    }
    if (sourceValue instanceof String && targetClass == UUID.class) {
      if (StringUtils.isNotBlank((String) sourceValue)) {
        return UUID.fromString((String) sourceValue);
      }
      return null;
    }
    if (sourceValue instanceof BigDecimal && toProto) {
      return CurrencyHelper.bigDecimalToString((BigDecimal) sourceValue);
    }
    if (sourceValue instanceof UUID && toProto) {
      return sourceValue.toString();
    }
    if (sourceValue instanceof String && targetClass == BigDecimal.class) {
      if (StringUtils.isNotBlank((String) sourceValue)) {
        return new BigDecimal(sourceValue.toString());
      }
      return null;
    }
    if (targetClass == String.class) {
      return sourceValue.toString();
    }
    return sourceValue;
  }

  private static Class<?> getBuiltClassFromBuilder(final Class<?> builderClass) {
    try {
      final Method buildMethod = builderClass.getDeclaredMethod("build");
      return buildMethod.getReturnType();
    } catch (NoSuchMethodException ex) {
      log.error("Unable to determine class being built during conversion", ex);
      throw new RuntimeException("Unable to determine class being built during conversion", ex);
    }
  }

  private static <T, R> Object convertToBuilder(final T source, final Class<R> resultClass, final boolean toProto) {
    try {
      final var builder = createBuilder(resultClass, toProto);
      final var srcProps = Introspector.getBeanInfo(source.getClass(), Object.class).getPropertyDescriptors();
      for (final var prop : srcProps) {
        try {
          processProp(source, prop, builder, resultClass, toProto);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | NoSuchFieldException ex) {
          final var msg = String.format("Unable to convert property %s to proto for %s", prop.getName(), resultClass.getSimpleName());
          log.error(msg, ex);
          throw new RuntimeException(msg, ex);
        }
      }
      return builder;
    } catch (IntrospectionException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
      final var msg = String.format("Unable to convert %s to %s", source.getClass().getSimpleName(), resultClass.getSimpleName());
      log.error(msg, ex);
      throw new RuntimeException(msg, ex);
    }
  }

  private static <T, R> R convert(final T source, final Class<R> resultClass, final boolean toProto) {
    final var builder = convertToBuilder(source, resultClass, toProto);
    try {
      final var buildMethod = builder.getClass().getMethod(BUILD_METHOD_NAME);
      //noinspection unchecked
      return (R) buildMethod.invoke(builder);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
      final var msg = String.format("Unable to convert %s to %s", source.getClass().getSimpleName(), resultClass.getSimpleName());
      log.error(msg, ex);
      throw new RuntimeException(msg, ex);
    }
  }

  private static <R> Object createBuilder(final Class<R> resultClass, final boolean toProto)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final var builderCreator = resultClass.getMethod(toProto ? PROTO_BUILDER_NAME : LOMBOK_BUILDER_NAME);
    return builderCreator.invoke(null);
  }

  private static Optional<Field> getTargetField(final Field srcField, final Class<?> targetClass, final boolean isProto) {
    if (isProto) {
      return getTargetFieldInProto(srcField, targetClass);
    }
    return getTargetFieldInDto(srcField, targetClass);
  }

  private static Optional<Field> getTargetFieldInDto(final Field srcField, final Class<?> targetClass) {
    final var name = stripUnderscore(srcField.getName());
    final var fields = targetClass.getDeclaredFields();
    for (final var field : fields) {
      final var anno = field.getAnnotation(ProtoField.class);
      if (anno != null) {
        final var annotationValue = anno.value();
        if (name.equals(annotationValue)) {
          return Optional.of(field);
        }
      }
    }
    return getFirstField(targetClass, name);
  }

  private static String getFieldName(final Field srcField) {
    final String fieldName;
    if (srcField.getAnnotation(ProtoField.class) == null) {
      fieldName = srcField.getName();
    } else {
      final var annotationValue = srcField.getAnnotation(ProtoField.class).value();
      fieldName = StringHelper.snakeToCamel(annotationValue);
    }
    return fieldName;
  }

  private static Optional<Field> getTargetFieldInProto(final Field srcField, final Class<?> targetClass) {
    final String fieldName;
    if (srcField.getAnnotation(ProtoField.class) == null) {
      fieldName = srcField.getName();
    } else {
      final var annotationValue = srcField.getAnnotation(ProtoField.class).value();
      fieldName = StringHelper.snakeToCamel(annotationValue);
    }
    return getFirstField(targetClass, StringUtils.uncapitalize(fieldName) + "_");
  }

  // TODO: Optimize by using toProto value.
  private static Optional<Field> getField(final Object source, final String name) {
    return getFirstField(source.getClass(),
        name,
        name + "_",
        StringUtils.capitalize(name),
        StringUtils.capitalize(name) + "_",
        "is" + StringUtils.capitalize(name),
        "is" + StringUtils.capitalize(name) + "_");
  }

  private static Optional<Field> getFirstField(final Class<?> sourceClass, final String... names) {
    if (names != null && names.length > 0) {
      for (final var name : names) {
        try {
          return Optional.of(sourceClass.getDeclaredField(name));
        } catch (NoSuchFieldException e) {
          //This is okay. Go onto next field name candidate.
        }
      }
    }
    return Optional.empty();
  }

  private static Class<?> getType(final Class<?> type) {
    if (type == Integer.class) {
      return Integer.TYPE;
    }
    if (type == Long.class) {
      return Long.TYPE;
    }
    if (type == Double.class) {
      return Double.TYPE;
    }
    if (type == Boolean.class) {
      return Boolean.TYPE;
    }
    if (type == Float.class) {
      return Float.TYPE;
    }
    if (type == Short.class) {
      return Short.TYPE;
    }
    if (type == Byte.class) {
      return Byte.TYPE;
    }
    if (List.class.isAssignableFrom(type)) {
      return List.class;
    }
    if (Map.class.isAssignableFrom(type)) {
      return Map.class;
    }
    if (Set.class.isAssignableFrom(type)) {
      return Set.class;
    }
    return type;
  }

  private static Method getReadMethod(final PropertyDescriptor prop, final Object source) throws NoSuchMethodException {
    final var method = prop.getReadMethod();
    if (method != null) {
      return method;
    }
    // A property without a read method indicates a repeating proto property.
    return source.getClass().getDeclaredMethod("get" + StringUtils.capitalize(prop.getName()) + "List");
  }

  private static void processProp(final Object source,
                                  final PropertyDescriptor prop,
                                  final Object builder,
                                  final Class<?> resultClass,
                                  final boolean toProto)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
    final var fieldOpt = getField(source, prop.getName());
    if (fieldOpt.isPresent()) {
      final var field = fieldOpt.get();
      final var value = getReadMethod(prop, source).invoke(source);
      final var targetFieldOpt = getTargetField(field, resultClass, toProto);
      if (targetFieldOpt.isPresent()
          && field.getAnnotation(ProtoExclude.class) == null
          && isNotExcludedInDto(targetFieldOpt.get(), toProto)
          && value != null) {
        final var targetField = targetFieldOpt.get();
        final Object convertedValue;
        final boolean repeating;
        if (value instanceof Collection) {
          repeating = true;
          final var list = new ArrayList<>();
          for (final var item : (Collection<?>) value) {
            final var converted = processValue(item, field, targetField, toProto);
            if (converted != null) {
              list.add(converted);
            }
          }
          convertedValue = list;
        } else {
          repeating = false;
          convertedValue = processValue(value, field, targetField, toProto);
        }
        if (convertedValue != null) {
          final var setterMethod = getBuilderMethod(
              builder,
              stripUnderscore(targetField.getName()),
              convertedValue.getClass(),
              repeating,
              toProto);
          setterMethod.invoke(builder, convertedValue);
        }
      }
    }
  }

  private static String stripUnderscore(final String name) {
    return name.endsWith("_") ? name.substring(0, name.length() - 1) : name;
  }

  private static boolean isNotExcludedInDto(final Field targetField, final boolean toProto) {
    return toProto || targetField.getAnnotation(DtoExclude.class) == null;
  }

  public static <T, R> void asMappedList(final List<T> list, final Function<T, R> mapper, final Consumer<List<R>> consumer) {
    if (list != null && !list.isEmpty()) {
      final var items = list.stream().map(mapper).collect(Collectors.toList());
      consumer.accept(items);
    }
  }

  private static Method getBuilderMethod(final Object builder,
                                         final String name,
                                         final Class<?> type,
                                         final boolean repeating,
                                         final boolean toProto) throws NoSuchMethodException {
    final String methodName;
    final Class<?> argType;
    if (toProto) {
      final var prefix = repeating ? "addAll" : "set";
      methodName = prefix + StringUtils.capitalize(name);
      argType = repeating ? Iterable.class : getType(type);
    } else {
      methodName = name;
      argType = getType(type);
    }
    return builder.getClass().getDeclaredMethod(methodName, argType);
  }
}
