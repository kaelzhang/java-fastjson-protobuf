package ai.ost.fastjson_protobuf;

import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.protobuf.GeneratedMessageV3;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SerializeConfig extends com.alibaba.fastjson.serializer.SerializeConfig {
  private final HashMap<Class<?>, ObjectSerializer> genericSerializer;

  public SerializeConfig () {
    super();
    genericSerializer = new HashMap<>();
    genericPut(GeneratedMessageV3.class, GeneratedMessageV3Codec.instance);
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

 void disableProtobuf () {
   genericSerializer.clear();
 }

  void genericPut (Type type, ObjectSerializer serializer) {
    genericSerializer.put((Class<?>) type, serializer);
  }
}
