package games.mythical.proto_util.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;

public class DateHelperTest {
    @Test
    public void testDateFromString() {
        final var parseDate = DateHelper.dateFromString("2011-12-03T10:15:30Z");
        assertEquals(LocalDate.of(2011, 12, 3), parseDate);
    }

    @Test
    public void testInstantFromString() {
        final var inst = DateHelper.isoDateTimeToInstant("2011-12-03T10:15:30Z");
        assertEquals(1322907330000L, inst.toEpochMilli());
    }

    @Test
    public void testBadString() {
        assertThrows(DateTimeParseException.class, () -> DateHelper.dateFromString("2011-12-03T"));
    }

    @Test
    public void testPre1970() {
        LocalDate parseDate = DateHelper.dateFromString("1960-12-03T10:15:30Z");
        assertEquals(LocalDate.of(1960, 12, 3), parseDate);
    }

    @Test
    public void testHoursEOD() {
        LocalDate parseDate = DateHelper.dateFromString("2011-12-03T23:59:59Z");
        assertEquals(LocalDate.of(2011, 12, 3), parseDate);

    }

    @Test
    public void testHoursBOD() {
        LocalDate parseDate = DateHelper.dateFromString("2011-12-03T00:00:00Z");
        assertEquals(LocalDate.of(2011, 12, 3), parseDate);
    }
}
