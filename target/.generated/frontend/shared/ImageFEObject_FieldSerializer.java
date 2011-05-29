package frontend.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ImageFEObject_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native int getId(frontend.shared.ImageFEObject instance) /*-{
    return instance.@frontend.shared.ImageFEObject::id;
  }-*/;
  
  private static native void  setId(frontend.shared.ImageFEObject instance, int value) /*-{
    instance.@frontend.shared.ImageFEObject::id = value;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native long getSize(frontend.shared.ImageFEObject instance) /*-{
    return instance.@frontend.shared.ImageFEObject::size;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native void  setSize(frontend.shared.ImageFEObject instance, long value) /*-{
    instance.@frontend.shared.ImageFEObject::size = value;
  }-*/;
  
  private static native java.lang.String getTitle(frontend.shared.ImageFEObject instance) /*-{
    return instance.@frontend.shared.ImageFEObject::title;
  }-*/;
  
  private static native void  setTitle(frontend.shared.ImageFEObject instance, java.lang.String value) /*-{
    instance.@frontend.shared.ImageFEObject::title = value;
  }-*/;
  
  private static native middle.ImageType getType(frontend.shared.ImageFEObject instance) /*-{
    return instance.@frontend.shared.ImageFEObject::type;
  }-*/;
  
  private static native void  setType(frontend.shared.ImageFEObject instance, middle.ImageType value) /*-{
    instance.@frontend.shared.ImageFEObject::type = value;
  }-*/;
  
  private static native java.lang.String getUrl(frontend.shared.ImageFEObject instance) /*-{
    return instance.@frontend.shared.ImageFEObject::url;
  }-*/;
  
  private static native void  setUrl(frontend.shared.ImageFEObject instance, java.lang.String value) /*-{
    instance.@frontend.shared.ImageFEObject::url = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, frontend.shared.ImageFEObject instance) throws SerializationException {
    setId(instance, streamReader.readInt());
    setSize(instance, streamReader.readLong());
    setTitle(instance, streamReader.readString());
    setType(instance, (middle.ImageType) streamReader.readObject());
    setUrl(instance, streamReader.readString());
    
  }
  
  public static frontend.shared.ImageFEObject instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new frontend.shared.ImageFEObject();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, frontend.shared.ImageFEObject instance) throws SerializationException {
    streamWriter.writeInt(getId(instance));
    streamWriter.writeLong(getSize(instance));
    streamWriter.writeString(getTitle(instance));
    streamWriter.writeObject(getType(instance));
    streamWriter.writeString(getUrl(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return frontend.shared.ImageFEObject_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    frontend.shared.ImageFEObject_FieldSerializer.deserialize(reader, (frontend.shared.ImageFEObject)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    frontend.shared.ImageFEObject_FieldSerializer.serialize(writer, (frontend.shared.ImageFEObject)object);
  }
  
}
