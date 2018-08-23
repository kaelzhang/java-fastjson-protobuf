package ai.ost.demo;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.*;

class ProtoMessageJsonConverter implements HttpMessageConverter<Message> {
  private List<MediaType> supportedMediaTypes = Arrays.asList(
    MediaType.APPLICATION_JSON,
    MediaType.APPLICATION_JSON_UTF8
  );

  private JsonFormat.Printer printer = JsonFormat
    .printer()
    .omittingInsignificantWhitespace()
    .preservingProtoFieldNames();

  private JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();

  private boolean isNotProtobufMessage (Class<?> clazz) {
    return !GeneratedMessageV3.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
    if (isNotProtobufMessage(clazz)) {
      return false;
    }

    if (mediaType == null) {
      return true;
    }
    for (MediaType supportedMediaType : supportedMediaTypes) {
      if (supportedMediaType.includes(mediaType)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
    if (isNotProtobufMessage(clazz)) {
      return false;
    }

    if (mediaType == null || MediaType.ALL.equals(mediaType)) {
      return true;
    }
    for (MediaType supportedMediaType : getSupportedMediaTypes()) {
      if (supportedMediaType.isCompatibleWith(mediaType)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<MediaType> getSupportedMediaTypes() {
    return supportedMediaTypes;
  }

  @Override
  public Message read(
    Class<? extends Message> clazz,
    HttpInputMessage inputMessage
  ) throws HttpMessageNotReadableException {
    InputStream is;

    try {
      is = inputMessage.getBody();
    } catch (IOException e) {
      throw new HttpMessageNotReadableException("can not get message body");
    }

    StringWriter writer = new StringWriter();

    try {
      IOUtils.copy(is, writer, "utf8");
    } catch (IOException e) {
      throw new HttpMessageNotReadableException("can not read input stream");
    }

    Message.Builder builder;

    try {
      builder = (GeneratedMessageV3.Builder) clazz.getMethod("newBuilder").invoke(clazz);
    } catch (NoSuchMethodException e) {
      throw new HttpMessageNotReadableException("invalid instance");
    } catch (IllegalArgumentException e) {
      throw new HttpMessageNotReadableException("invalid arguments");
    } catch (InvocationTargetException e) {
      throw new HttpMessageNotReadableException("invalid target");
    } catch (IllegalAccessException e) {
      throw new HttpMessageNotReadableException("invalid access");
    }

    try {
      parser.merge(writer.toString(), builder);
    } catch (InvalidProtocolBufferException e) {
      throw new HttpMessageNotReadableException("can not convert to protobuf");
    }

    return builder.build();
  }

  @Override
  public void write(
    Message o,
    @Nullable MediaType contentType,
    HttpOutputMessage outputMessage
  ) throws HttpMessageNotWritableException {
    String json;

    try {
      json = printer.print(o);
    } catch (InvalidProtocolBufferException e) {
      throw new HttpMessageNotWritableException("invalid protobuf");
    }

    try {
      outputMessage.getBody().write(json.getBytes(Charset.forName("UTF-8")));
    } catch (IOException e) {
      throw new HttpMessageNotWritableException("can not write to output");
    }
  }
}
