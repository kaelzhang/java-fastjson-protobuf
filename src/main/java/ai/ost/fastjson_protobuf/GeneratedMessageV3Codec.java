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

public class GeneratedMessageV3Codec implements ObjectSerializer, ObjectDeserializer {
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
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw new InvalidProtocolBufferException(e);
    }

    serializer.out.write(json);
  }

  @SuppressWarnings("unchecked")
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
    int endToken;

    if (startToken == JSONToken.LBRACE) {
      endToken = JSONToken.RBRACE;
    } else if (startToken == JSONToken.LBRACKET) {
      endToken = JSONToken.RBRACKET;
    } else {
      String str = subString(lexer, startToken);
      throw new JSONException(
        str.isEmpty()
          ? "Expect message object"
          : "Expect message object but got: " + str
      );
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

  // Get the subString of the current token
  private String subString (JSONLexerBase lexer, int token) {
    switch (token) {
      case JSONToken.TRUE           : return "true";
      case JSONToken.FALSE          : return "false";
      case JSONToken.LITERAL_STRING : return "\"" + lexer.stringVal() + "\"";

      // We can't even get the substring of the current token,
      //   due to the limilation of fastjson
      case JSONToken.LITERAL_FLOAT  : return "a float number";
      case JSONToken.LITERAL_INT    : return "an integer";
    }

    return "";
  }

  @Override
  public int getFastMatchToken() {
    return 0;
  }
}
