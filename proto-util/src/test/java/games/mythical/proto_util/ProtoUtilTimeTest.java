package games.mythical.proto_util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.protobuf.util.Timestamps;
import games.mythical.proto_util.helper.DateHelper;
import games.mythical.proto_util.testmodels.TimeSimpleLongDto;
import games.mythical.proto_util.testmodels.TimeSimpleSqlDto;
import games.mythical.proto_util.testmodels.TimeSimpleStringDto;
import games.mythical.proto_util.util.time.SimpleTime;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class ProtoUtilTimeTest {

    @Test
    public void testSimpleTimeSqlToProto() {
        final var now = Instant.now();
        final var dto = new TimeSimpleSqlDto();
        dto.setTimeValue(Timestamp.from(now));
        final var proto = ProtoUtil.toProto(dto, SimpleTime.class);
        assertEquals(now.toEpochMilli(), DateHelper.protoTimestampToEpochMillis(proto.getTimeValue()));
    }

    @Test
    public void testSimpleTimeStringToProto() {
        final var dto = new TimeSimpleStringDto();
        dto.setTimeValue("2011-12-03T10:15:30Z");
        final var proto = ProtoUtil.toProto(dto, SimpleTime.class);
        assertEquals(1322907330000L, DateHelper.protoTimestampToEpochMillis(proto.getTimeValue()));
    }

    @Test
    public void testSimpleTimeLongFromProto() {
        final var now = Instant.now();
        final var proto = SimpleTime.newBuilder()
            .setTimeValue(Timestamps.fromMillis(now.toEpochMilli()))
            .build();
        final var dto = ProtoUtil.toDtoBean(proto, TimeSimpleLongDto.class);
        assertEquals(DateHelper.protoTimestampToEpochMillis(proto.getTimeValue()), dto.getTimeValue());
    }
}
