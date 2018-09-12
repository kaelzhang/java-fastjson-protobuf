package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.google.protobuf.GeneratedMessageV3;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ParserConfig extends com.alibaba.fastjson.parser.ParserConfig {
  private final HashMap<Class<?>, ObjectDeserializer> genericDeserializer;

  public ParserConfig () {
    super();
    genericDeserializer = new HashMap<>();
    genericPut(GeneratedMessageV3.class, GeneratedMessageV3Codec.instance);
  }

  private ObjectDeserializer getGenericDeserializer (Class<?> clazz) {
    ObjectDeserializer parser = genericDeserializer.get(clazz);

    if (parser == null) {
      for (Map.Entry<Class<?>, ObjectDeserializer> entry: genericDeserializer.entrySet()) {
        if (entry.getKey().isAssignableFrom(clazz)) {
          ObjectDeserializer deserializer = entry.getValue();
          genericDeserializer.put(clazz, deserializer);
          return deserializer;
        }
      }
    }

    return parser;
  }

  @Override
  public ObjectDeserializer getDeserializer(Type type) {
    Class<?> clazz = (Class<?>) type;
    ObjectDeserializer serializer = getGenericDeserializer(clazz);

    if (serializer != null) {
      return serializer;
    }

    return super.getDeserializer(type);
  }

  @Override
  public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
    ObjectDeserializer serializer = getGenericDeserializer(clazz);

    if (serializer != null) {
      return serializer;
    }

    return super.getDeserializer(clazz, type);
  }

  void disableProtobuf () {
    genericDeserializer.clear();
  }

  public void genericPut (Type type, ObjectDeserializer serializer) {
    genericDeserializer.put((Class<?>) type, serializer);
  }
}
