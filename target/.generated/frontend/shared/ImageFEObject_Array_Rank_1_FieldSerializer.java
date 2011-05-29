package frontend.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ImageFEObject_Array_Rank_1_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, frontend.shared.ImageFEObject[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.deserialize(streamReader, instance);
  }
  
  public static frontend.shared.ImageFEObject[] instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int size = streamReader.readInt();
    return new frontend.shared.ImageFEObject[size];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, frontend.shared.ImageFEObject[] instance) throws SerializationException {
    com.google.gwt.user.client.rpc.core.java.lang.Object_Array_CustomFieldSerializer.serialize(streamWriter, instance);
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return frontend.shared.ImageFEObject_Array_Rank_1_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    frontend.shared.ImageFEObject_Array_Rank_1_FieldSerializer.deserialize(reader, (frontend.shared.ImageFEObject[])object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    frontend.shared.ImageFEObject_Array_Rank_1_FieldSerializer.serialize(writer, (frontend.shared.ImageFEObject[])object);
  }
  
}
