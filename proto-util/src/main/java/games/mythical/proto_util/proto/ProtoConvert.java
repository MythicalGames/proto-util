package games.mythical.proto_util.proto;

import com.google.protobuf.MessageOrBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the Protocol Buffer class that this field should be converted to.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ProtoConvert {
  Class<? extends MessageOrBuilder> value();
}
