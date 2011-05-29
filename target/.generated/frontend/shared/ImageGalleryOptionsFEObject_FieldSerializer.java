package frontend.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

@SuppressWarnings("deprecation")
public class ImageGalleryOptionsFEObject_FieldSerializer implements com.google.gwt.user.client.rpc.impl.TypeHandler {
  private static native java.util.Set getImgAllowedTypes(frontend.shared.ImageGalleryOptionsFEObject instance) /*-{
    return instance.@frontend.shared.ImageGalleryOptionsFEObject::imgAllowedTypes;
  }-*/;
  
  private static native void  setImgAllowedTypes(frontend.shared.ImageGalleryOptionsFEObject instance, java.util.Set value) /*-{
    instance.@frontend.shared.ImageGalleryOptionsFEObject::imgAllowedTypes = value;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native long getImgMaxSize_bytes(frontend.shared.ImageGalleryOptionsFEObject instance) /*-{
    return instance.@frontend.shared.ImageGalleryOptionsFEObject::imgMaxSize_bytes;
  }-*/;
  
  @com.google.gwt.core.client.UnsafeNativeLong
  private static native void  setImgMaxSize_bytes(frontend.shared.ImageGalleryOptionsFEObject instance, long value) /*-{
    instance.@frontend.shared.ImageGalleryOptionsFEObject::imgMaxSize_bytes = value;
  }-*/;
  
  public static void deserialize(SerializationStreamReader streamReader, frontend.shared.ImageGalleryOptionsFEObject instance) throws SerializationException {
    setImgAllowedTypes(instance, (java.util.Set) streamReader.readObject());
    setImgMaxSize_bytes(instance, streamReader.readLong());
    
  }
  
  public static frontend.shared.ImageGalleryOptionsFEObject instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return new frontend.shared.ImageGalleryOptionsFEObject();
  }
  
  public static void serialize(SerializationStreamWriter streamWriter, frontend.shared.ImageGalleryOptionsFEObject instance) throws SerializationException {
    streamWriter.writeObject(getImgAllowedTypes(instance));
    streamWriter.writeLong(getImgMaxSize_bytes(instance));
    
  }
  
  public Object create(SerializationStreamReader reader) throws SerializationException {
    return frontend.shared.ImageGalleryOptionsFEObject_FieldSerializer.instantiate(reader);
  }
  
  public void deserial(SerializationStreamReader reader, Object object) throws SerializationException {
    frontend.shared.ImageGalleryOptionsFEObject_FieldSerializer.deserialize(reader, (frontend.shared.ImageGalleryOptionsFEObject)object);
  }
  
  public void serial(SerializationStreamWriter writer, Object object) throws SerializationException {
    frontend.shared.ImageGalleryOptionsFEObject_FieldSerializer.serialize(writer, (frontend.shared.ImageGalleryOptionsFEObject)object);
  }
  
}
