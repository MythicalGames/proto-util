package games.mythical.proto_util.testmodels;

import games.mythical.proto_util.proto.ProtoField;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AltSimpleDto {
  @ProtoField("value")
  private String maker;
}
