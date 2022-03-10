package games.mythical.proto_util.transform;

public interface Transformer {
    boolean condition(Object source, Class<?> target, boolean toProto);
    Object transform(Object source, Class<?> target, boolean toProto);
}
