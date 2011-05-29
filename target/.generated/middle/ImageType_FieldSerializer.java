package middle;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ImageType_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  public static void deserialize(SerializationStreamReader streamReader, middle.ImageType instance) throws SerializationException {
    // Enum deserialization is handled via the instantiate method
  }
  
  public static middle.ImageType instantiate(SerializationStreamReader streamReader) throws SerializationException {
    int ordinal = streamReader.readInt();
    middle.ImageType[] values = middle.ImageType.values();
    assert (ordinal >= 0 && ordinal < values.length);
    return values[ordinal];
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, middle.ImageType instance) throws SerializationException {
    assert (instance != null);
    streamWriter.writeInt(instance.ordinal());
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return middle.ImageType_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    middle.ImageType_FieldSerializer.deserialize(reader, (middle.ImageType)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    middle.ImageType_FieldSerializer.serialize(writer, (middle.ImageType)object);
  }
  
}
