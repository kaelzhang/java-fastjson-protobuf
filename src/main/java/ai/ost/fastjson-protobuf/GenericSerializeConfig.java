package ai.ost.demo;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GenericSerializeConfig extends SerializeConfig {
  private final HashMap<Class<?>, ObjectSerializer> genericSerializer;

  GenericSerializeConfig () {
    super();
    genericSerializer = new HashMap<>();
  }

  @Override
  public ObjectSerializer getObjectWriter(Class<?> clazz) {
    ObjectSerializer writer = genericSerializer.get(clazz);

    if (writer == null) {
      for (Map.Entry<Class<?>, ObjectSerializer> entry: genericSerializer.entrySet()) {
        if (entry.getKey().isAssignableFrom(clazz)) {
          return entry.getValue();
        }
      }
    }

    return super.getObjectWriter(clazz);
  }

  boolean genericPut (Type type, ObjectSerializer serializer) {
    genericSerializer.put((Class<?>) type, serializer);
    return true;
  }
}
