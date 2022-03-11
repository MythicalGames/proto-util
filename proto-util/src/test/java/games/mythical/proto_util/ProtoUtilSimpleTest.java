package games.mythical.proto_util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import games.mythical.proto_util.testmodels.AltSimpleDto;
import games.mythical.proto_util.testmodels.SimpleBeanDto;
import games.mythical.proto_util.testmodels.SimpleDto;
import games.mythical.proto_util.testmodels.SimpleTypesDto;
import games.mythical.proto_util.util.simple.SimpleString;
import games.mythical.proto_util.util.simple.SimpleTypes;
import org.junit.jupiter.api.Test;

public class ProtoUtilSimpleTest {

  @Test
  public void testSimpleToProto() {
    final var dto = SimpleDto.builder()
        .value("Bristol")
        .build();
    final var proto = ProtoUtil.toProto(dto, SimpleString.class);

    assertEquals(dto.getValue(), proto.getValue());
  }

  @Test
  public void testSimpleFromProtoViaBuilder() {
    final var proto = SimpleString.newBuilder()
        .setValue("Avro")
        .build();
    final var dto = ProtoUtil.toDto(proto, SimpleDto.class);

    assertEquals(proto.getValue(), dto.getValue());
  }

  @Test
  public void testSimpleFromProtoViaBean() {
    final var proto = SimpleString.newBuilder()
        .setValue("Avro")
        .build();
    final var dto = ProtoUtil.toDtoBean(proto, SimpleBeanDto.class);

    assertEquals(proto.getValue(), dto.getValue());
  }

  @Test
  public void testSimpleWithDifferentNameToProto() {
    final var dto = AltSimpleDto.builder()
        .maker("Vickers")
        .build();
    final var proto = ProtoUtil.toProto(dto, SimpleString.class);

    assertEquals(dto.getMaker(), proto.getValue());
  }

  @Test
  public void testSimpleWithDifferentNameFromProto() {
    final var proto = SimpleString.newBuilder()
        .setValue("Hadley-Page")
        .build();
    final var dto = ProtoUtil.toDto(proto, AltSimpleDto.class);

    assertEquals(proto.getValue(), dto.getMaker());
  }

  @Test
  public void testSimpleTypesToProto() {
    final var dto = SimpleTypesDto.builder()
        .strValue("Hawker")
        .intValue(1937)
        .bigIntValue(-1234567)
        .uintValue(1938)
        .bigUintValue(123456789)
        .boolValue(true)
        .doubleValue(19.38)
        .build();
    final var proto = ProtoUtil.toProto(dto, SimpleTypes.class);

    assertEquals(dto.getStrValue(), proto.getStrValue());
    assertEquals(dto.getIntValue(), proto.getIntValue());
    assertEquals(dto.getBigIntValue(), proto.getBigIntValue());
    assertEquals(dto.getUintValue(), proto.getUintValue());
    assertEquals(dto.getBigUintValue(), proto.getBigUintValue());
    assertEquals(dto.isBoolValue(), proto.getBoolValue());
    assertEquals(dto.getDoubleValue(), proto.getDoubleValue());
  }

  @Test
  public void testSimpleTypesFromProto() {
    final var proto = SimpleTypes.newBuilder()
        .setStrValue("Hawker")
        .setIntValue(1937)
        .setBigIntValue(-1234567)
        .setUintValue(1938)
        .setBigUintValue(123456789)
        .setBoolValue(true)
        .setDoubleValue(19.38)
        .build();
    final var dto = ProtoUtil.toDto(proto, SimpleTypesDto.class);

    assertEquals(proto.getStrValue(), dto.getStrValue());
    assertEquals(proto.getIntValue(), dto.getIntValue());
    assertEquals(proto.getBigIntValue(), dto.getBigIntValue());
    assertEquals(proto.getUintValue(), dto.getUintValue());
    assertEquals(proto.getBigUintValue(), dto.getBigUintValue());
    assertEquals(proto.getBoolValue(), dto.isBoolValue());
    assertEquals(proto.getDoubleValue(), dto.getDoubleValue());
  }
}
