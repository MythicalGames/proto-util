package games.mythical.proto_util.testmodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleTypesDto {
  private String strValue;
  private int intValue;
  private long bigIntValue;
  private int uintValue;
  private long bigUintValue;
  private boolean boolValue;
  private double doubleValue;
}
