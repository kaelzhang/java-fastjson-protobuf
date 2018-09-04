package ai.ost.fastjson_protobuf;

import java.io.IOException;

public class InvalidProtocolBufferException extends RuntimeException {
  public InvalidProtocolBufferException (final String message) {
    super(message);
  }

  public InvalidProtocolBufferException(IOException e) {
    super(e.getMessage(), e);
  }
}
