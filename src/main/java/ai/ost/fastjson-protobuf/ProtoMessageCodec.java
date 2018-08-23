package ai.ost.demo;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.lang.reflect.Type;

class ProtoMessageCodec implements ObjectSerializer {
  public final static ProtoMessageCodec instance = new ProtoMessageCodec();

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
