package games.mythical.proto_util.helper;

import com.google.protobuf.util.Timestamps;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;

public class DateHelper {
  public static LocalDate dateFromString(String dateString) throws DateTimeParseException {
    if (StringUtils.isEmpty(dateString)) {
      throw new DateTimeParseException("DateString is null.", "", 0);
    }
    // TODO: use regex to determine what kind of formatter is required and support different date formats.
    DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    return LocalDate.ofInstant(Instant.from(formatter.parse(dateString)), ZoneId.of("UTC"));
  }

  public static com.google.protobuf.Timestamp epochMillisToProtoTimestamp(long epochMillis) {
    return Timestamps.fromMillis(epochMillis);
  }

  public static com.google.protobuf.Timestamp instantToProtoTimestamp(Instant instant) {
    return com.google.protobuf.Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }

  public static com.google.protobuf.Timestamp stringToProtoTimestamp(String isoFormattedDateTime) {
    return instantToProtoTimestamp(isoDateTimeToInstant(isoFormattedDateTime));
  }

  public static Instant isoDateTimeToInstant(String isoFormattedDateTime) {
    return Instant.parse(isoFormattedDateTime);
  }

  public static Instant protoTimestampToInstant(com.google.protobuf.Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  public static long protoTimestampToEpochMillis(com.google.protobuf.Timestamp timestamp) {
    return protoTimestampToInstant(timestamp).toEpochMilli();
  }

  public static String protoTimestampToIsoDateTimeStr(com.google.protobuf.Timestamp timestamp) {
    var instant = protoTimestampToInstant(timestamp);
    ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
    return zdt.toString();
  }

  public static com.google.protobuf.Timestamp sqlTimestamptoProtoTimestamp(java.sql.Timestamp timestamp) {
    return com.google.protobuf.util.Timestamps.fromMillis(timestamp.getTime());
  }
}
