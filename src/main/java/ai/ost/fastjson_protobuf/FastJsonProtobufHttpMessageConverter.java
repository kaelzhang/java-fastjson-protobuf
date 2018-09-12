package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;

public class FastJsonProtobufHttpMessageConverter extends FastJsonHttpMessageConverter {
  public FastJsonProtobufHttpMessageConverter () {
    super();

    FastJsonConfig config = this.getFastJsonConfig();
    config.setSerializeConfig(new SerializeConfig());
    config.setParserConfig(new ParserConfig());
  }

  public void disableProtobuf () {
    FastJsonConfig config = this.getFastJsonConfig();

    disableProtobufWriter(config.getSerializeConfig());
    disableProtobufParser(config.getParserConfig());
  }

  private void disableProtobufWriter (com.alibaba.fastjson.serializer.SerializeConfig serializeConfig) {
    if (SerializeConfig.class.isAssignableFrom(serializeConfig.getClass())) {
      ((SerializeConfig) serializeConfig).disableProtobuf();
    }
  }

  private void disableProtobufParser (com.alibaba.fastjson.parser.ParserConfig parserConfig) {
    if (ParserConfig.class.isAssignableFrom(parserConfig.getClass())) {
      ((ParserConfig) parserConfig).disableProtobuf();
    }
  }

  @Override
  public Object read(
    Type type,
    Class<?> contextClass,
    HttpInputMessage inputMessage
  ) throws IOException, HttpMessageNotReadableException {
    return readType(getType(type, contextClass), inputMessage);
  }

  @Override
  protected Object readInternal(
    Class<?> clazz,
    HttpInputMessage inputMessage
  ) throws IOException, HttpMessageNotReadableException {
    return readType(getType(clazz, null), inputMessage);
  }

  private Object readType(Type type, HttpInputMessage inputMessage) throws IOException {
    InputStream is;

    try {
      is = inputMessage.getBody();
    } catch (IOException e) {
      throw new IOException("I/O error while getting input body", e);
    }

    FastJsonConfig config = this.getFastJsonConfig();
    StringWriter writer = new StringWriter();

    try {
      IOUtils.copy(is, writer, config.getCharset());
    } catch (IOException e) {
      throw new IOException("I/O error while reading input message", e);
    }

    try {
      return JSON.parseObject(writer.toString(), type, config.getParserConfig(), config.getFeatures());
    } catch (JSONException e) {
      throw new HttpMessageNotReadableException("JSON parse error: " + e.getMessage(), e);
    }
  }
}
