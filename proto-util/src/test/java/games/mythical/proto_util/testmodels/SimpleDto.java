package games.mythical.proto_util.testmodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleDto {
  private String value;
}
