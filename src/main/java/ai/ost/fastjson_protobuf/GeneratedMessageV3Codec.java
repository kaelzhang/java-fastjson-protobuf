package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

class GeneratedMessageV3Codec implements ObjectSerializer, ObjectDeserializer {
  final static GeneratedMessageV3Codec instance = new GeneratedMessageV3Codec();

  private static JsonFormat.Printer printer = JsonFormat
    .printer()
    .omittingInsignificantWhitespace()
    .preservingProtoFieldNames();

  private static JsonFormat.Parser parser = JsonFormat
    .parser()
    .ignoringUnknownFields();

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

  private Message empty (Message.Builder builder, JSONLexerBase lexer) {
    lexer.nextToken();
    return builder.build();
  }

  @Override
  public <T> T deserialze(
    DefaultJSONParser jsonParser,
    Type type,
    Object fieldName
  ) {
    Message.Builder builder;
    Class<?> clazz = (Class<?>) type;

    try {
      builder = (Message.Builder) clazz.getMethod("newBuilder").invoke(clazz);
    } catch (NoSuchMethodException e) {
      throw new InvalidProtocolBufferMessageClass("invalid instance");
    } catch (IllegalArgumentException e) {
      throw new InvalidProtocolBufferMessageClass("invalid arguments");
    } catch (InvocationTargetException e) {
      throw new InvalidProtocolBufferMessageClass("invalid target");
    } catch (IllegalAccessException e) {
      throw new InvalidProtocolBufferMessageClass("invalid access");
    }

    JSONLexerBase lexer = (JSONLexerBase) jsonParser.getLexer();

    int pos = lexer.pos();
    int startToken = lexer.token();
    if (startToken == JSONToken.NULL) {
      return (T) empty(builder, lexer);
    }

    int endToken;

    if (startToken == JSONToken.LBRACE) {
      endToken = JSONToken.RBRACE;
    } else if (startToken == JSONToken.LBRACKET) {
      endToken = JSONToken.RBRACKET;
    } else {
      throw new JSONException("Expect '{' or '[', but got '" + lexer.getCurrent() + "'");
    }

    lexer.nextToken();

    // The JSONLexer of fastjson is buggy about the pos.
    // For now, we must get the pos after `nextToken()`
    int startPos = lexer.pos() - 1;
    int endPos = 0;

    int current;
    int expectR = 1;

    while (true) {
      current = lexer.token();

      if (current == endToken && -- expectR == 0) {
        endPos = lexer.pos();
        lexer.nextToken(JSONToken.COMMA);
        break;
      }

      if (current == startToken) {
        ++ expectR;
      }

      lexer.nextToken();
    }

    String json = lexer.subString(startPos, endPos - startPos + 1);

    try {
      parser.merge(json, builder);
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw new InvalidProtocolBufferException(e);
    }

    return (T) builder.build();
  }

  @Override
  public int getFastMatchToken() {
    return 0;
  }
}
