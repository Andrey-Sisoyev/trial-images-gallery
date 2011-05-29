package utils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class InvalidDataException_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, utils.InvalidDataException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static utils.InvalidDataException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new utils.InvalidDataException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, utils.InvalidDataException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return utils.InvalidDataException_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    utils.InvalidDataException_FieldSerializer.deserialize(reader, (utils.InvalidDataException)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    utils.InvalidDataException_FieldSerializer.serialize(writer, (utils.InvalidDataException)object);
  }
  
}
