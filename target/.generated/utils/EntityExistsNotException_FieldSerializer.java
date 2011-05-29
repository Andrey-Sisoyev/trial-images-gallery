package utils;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class EntityExistsNotException_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, utils.EntityExistsNotException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.deserialize(streamReader, instance);
  }
  
  public static utils.EntityExistsNotException instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new utils.EntityExistsNotException();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, utils.EntityExistsNotException instance) throws SerializationException {
    
    com.google.gwt.user.client.rpc.core.java.lang.Exception_FieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return utils.EntityExistsNotException_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    utils.EntityExistsNotException_FieldSerializer.deserialize(reader, (utils.EntityExistsNotException)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    utils.EntityExistsNotException_FieldSerializer.serialize(writer, (utils.EntityExistsNotException)object);
  }
  
}
