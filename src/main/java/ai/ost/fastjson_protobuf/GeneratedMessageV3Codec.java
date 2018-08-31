package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.lang.reflect.Type;

class GeneratedMessageV3Codec implements ObjectSerializer {
  public final static GeneratedMessageV3Codec instance = new GeneratedMessageV3Codec();

  private static JsonFormat.Printer printer = JsonFormat
    .printer()
    .omittingInsignificantWhitespace()
    .preservingProtoFieldNames();

  @Override
  public void write(
    JSONSerializer serializer,
    Object object,
    Object fieldName,
    Type fieldType,
    int features
  ) throws IOException {
    String json;

    GeneratedMessageV3 o = (GeneratedMessageV3) object;

    try {
      json = printer.print(o);
    } catch (InvalidProtocolBufferException e) {
      throw new IOException("invalid protobuf");
    }

    serializer.out.write(json);
  }
}
