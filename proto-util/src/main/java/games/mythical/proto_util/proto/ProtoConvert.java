package games.mythical.proto_util.proto;

import com.google.protobuf.MessageOrBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ProtoConvert {
  Class<? extends MessageOrBuilder> value();
}
