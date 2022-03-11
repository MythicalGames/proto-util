package games.mythical.proto_util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import games.mythical.proto_util.helper.CurrencyHelper;
import games.mythical.proto_util.testmodels.TypesBigDecimalDto;
import games.mythical.proto_util.testmodels.TypesUuidDto;
import games.mythical.proto_util.util.types.SimpleBigDecimal;
import games.mythical.proto_util.util.types.SimpleUuid;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class ProtoUtilTypesTest {
    @Test
    public void testSimpleTypesUuidToProto() {
        final var uuid = UUID.randomUUID();
        final var dto = new TypesUuidDto();
        dto.setTestId(uuid);
        final var proto = ProtoUtil.toProto(dto, SimpleUuid.class);
        assertEquals(uuid, UUID.fromString(proto.getTestId()));
    }

    @Test
    public void testSimpleTypesUuidFromProto() {
        final var uuid = UUID.randomUUID();
        final var proto = SimpleUuid.newBuilder()
            .setTestId(uuid.toString())
            .build();
        final var dto = ProtoUtil.toDtoBean(proto, TypesUuidDto.class);
        assertEquals(uuid, dto.getTestId());
    }

    @Test
    public void testSimpleTypesBigDecimalToProto() {
        final var decimal = BigDecimal.valueOf(19.95);
        final var dto = new TypesBigDecimalDto();
        dto.setAmount(decimal);
        final var proto = ProtoUtil.toProto(dto, SimpleBigDecimal.class);
        assertEquals(CurrencyHelper.bigDecimalToString(decimal), proto.getAmount());
    }

    @Test
    public void testSimpleTypesBigDecimalFromProto() {
        final var decimalStr = CurrencyHelper.bigDecimalToString(BigDecimal.valueOf(19.95));
        final var proto = SimpleBigDecimal.newBuilder()
            .setAmount(decimalStr)
            .build();
        final var dto = ProtoUtil.toDtoBean(proto, TypesBigDecimalDto.class);
        assertEquals(decimalStr, CurrencyHelper.bigDecimalToString(dto.getAmount()));
    }
}
