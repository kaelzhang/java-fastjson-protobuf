package ai.ost.fastjson_protobuf;

import java.io.IOException;

public class InvalidProtocolBufferMessageClass extends RuntimeException {
  public InvalidProtocolBufferMessageClass (final String message) {
    super(message);
  }

  public InvalidProtocolBufferMessageClass(IOException e) {
    super(e.getMessage(), e);
  }
}
