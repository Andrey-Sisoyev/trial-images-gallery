package utils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ConfiguredConstraintsViolatedException_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, utils.ConfiguredConstraintsViolatedException instance) throws SerializationException {
    
    utils.InvalidDataException_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static utils.ConfiguredConstraintsViolatedException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new utils.ConfiguredConstraintsViolatedException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, utils.ConfiguredConstraintsViolatedException instance) throws SerializationException {
    
    utils.InvalidDataException_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return utils.ConfiguredConstraintsViolatedException_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    utils.ConfiguredConstraintsViolatedException_FieldSerializer.deserialize(reader, (utils.ConfiguredConstraintsViolatedException)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    utils.ConfiguredConstraintsViolatedException_FieldSerializer.serialize(writer, (utils.ConfiguredConstraintsViolatedException)object);
  }
  
}
