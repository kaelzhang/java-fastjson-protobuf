package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

class FastJsonProtobufHttpMessageConverter extends FastJsonHttpMessageConverter {
  private JsonFormat.Printer printer = JsonFormat
    .printer()
    .omittingInsignificantWhitespace()
    .preservingProtoFieldNames();

  private JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();

  private Boolean protobufReadEnabled = true;

  public FastJsonProtobufHttpMessageConverter () {
    super();

    this.getFastJsonConfig().setSerializeConfig(new SerializeConfig());
  }

  public void disableProtobuf () {
    disableProtobufWriter(this.getFastJsonConfig().getSerializeConfig());
    protobufReadEnabled = false;
  }

  private void disableProtobufWriter (com.alibaba.fastjson.serializer.SerializeConfig serializeConfig) {
    // do nothing
  }

  protected void disableProtobufWriter (SerializeConfig serializeConfig) {
    serializeConfig.disableProtobuf();
  }

  private boolean isNotGeneratedMessageV3 (Class<?> clazz) {
    return !GeneratedMessageV3.class.isAssignableFrom(clazz);
  }

  @Override
  public Object read(
    Type type,
    Class<?> contextClass,
    HttpInputMessage inputMessage
  ) throws IOException, HttpMessageNotReadableException {
    Class<?> clazz = (Class<?>) type;

    if (isNotGeneratedMessageV3(clazz)) {
      return super.read(type, contextClass, inputMessage);
    }

    return readInternal(clazz, inputMessage);
  }

  // JSON -> Given Entity
  @Override
  protected Object readInternal(
    Class<?> clazz,
    HttpInputMessage inputMessage
  ) throws IOException, HttpMessageNotReadableException {
    if (!protobufReadEnabled || isNotGeneratedMessageV3(clazz)) {
      return super.readInternal(clazz, inputMessage);
    }

    InputStream is;

    try {
      is = inputMessage.getBody();
    } catch (IOException e) {
      throw new IOException("can not get message body");
    }

    StringWriter writer = new StringWriter();

    try {
      IOUtils.copy(is, writer, "utf8");
    } catch (IOException e) {
      throw new IOException("can not read input stream");
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
}
