package ai.ost.demo;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.serializer.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.protobuf.GeneratedMessageV3;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
  @Override
  public void configureMessageConverters(
    List<HttpMessageConverter<?>> converters
  ) {
    converters.add(new ProtoMessageJsonConverter());

    FilteredFastJsonConverter fastJsonConverter = new FilteredFastJsonConverter();
    FastJsonConfig config = new FastJsonConfig();

    GenericSerializeConfig serializeConfig = new GenericSerializeConfig();
    serializeConfig.genericPut(GeneratedMessageV3.class, ProtoMessageCodec.instance);
    config.setSerializeConfig(serializeConfig);

    config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

    fastJsonConverter.setFastJsonConfig(config);
    fastJsonConverter.setSupportedMediaTypes(
      Arrays.asList(
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_JSON_UTF8
      )
    );

    converters.add(fastJsonConverter);
  }
}

class FilteredFastJsonConverter extends FastJsonHttpMessageConverter {
  private boolean isProtobufMessage (Class<?> clazz) {
    return GeneratedMessageV3.class.isAssignableFrom(clazz);
  }

  @Override
  public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
    if (isProtobufMessage(clazz)) {
      return false;
    }

    return super.canWrite(clazz, mediaType);
  }

  @Override
  public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
    if (isProtobufMessage(clazz)) {
      return false;
    }

    return super.canRead(clazz, mediaType);
  }
}
