package ai.ost.protobuf_disabled_converter_test;

import ai.ost.fastjson_protobuf.FastJsonProtobufHttpMessageConverter;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@EnableWebMvc
@Configuration
class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void configureMessageConverters(
    List<HttpMessageConverter<?>> converters
  ) {
    // Disable with non-fastjson-protobuf configs
    FastJsonProtobufHttpMessageConverter converterUnused =
      new FastJsonProtobufHttpMessageConverter();

    FastJsonConfig config = converterUnused.getFastJsonConfig();
    config.setParserConfig(new ParserConfig());
    config.setSerializeConfig(new SerializeConfig());

    converterUnused.disableProtobuf();

    // Disable on fastjson-protobuf config
    FastJsonProtobufHttpMessageConverter converter =
      new FastJsonProtobufHttpMessageConverter();

    converter.disableProtobuf();

    converter.setSupportedMediaTypes(
      Arrays.asList(
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_JSON_UTF8
      )
    );

    converters.add(converter);
  }
}
