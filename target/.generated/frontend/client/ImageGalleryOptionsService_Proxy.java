package frontend.client;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.RpcToken;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class ImageGalleryOptionsService_Proxy extends RemoteServiceProxy implements frontend.client.ImageGalleryOptionsServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "frontend.client.ImageGalleryOptionsService";
  private static final String SERIALIZATION_POLICY ="C99D1236738BBBB9738737CC4A2777AC";
  private static final frontend.client.ImageGalleryOptionsService_TypeSerializer SERIALIZER = new frontend.client.ImageGalleryOptionsService_TypeSerializer();
  
  public ImageGalleryOptionsService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "ImageGalleryOptionsService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void getCurrentOptions(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImageGalleryOptionsService_Proxy.getCurrentOptions", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("getCurrentOptions");
      streamWriter.writeInt(0);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImageGalleryOptionsService_Proxy.getCurrentOptions",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "ImageGalleryOptionsService_Proxy.getCurrentOptions", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void updateOptions(frontend.shared.ImageGalleryOptionsFEObject new_opts, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImageGalleryOptionsService_Proxy.updateOptions", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("updateOptions");
      streamWriter.writeInt(1);
      streamWriter.writeString("frontend.shared.ImageGalleryOptionsFEObject/3393113316");
      streamWriter.writeObject(new_opts);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImageGalleryOptionsService_Proxy.updateOptions",  "requestSerialized"));
      doInvoke(ResponseReader.VOID, "ImageGalleryOptionsService_Proxy.updateOptions", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  @Override
  public SerializationStreamWriter createStreamWriter() {
    ClientSerializationStreamWriter toReturn =
      (ClientSerializationStreamWriter) super.createStreamWriter();
    if (getRpcToken() != null) {
      toReturn.addFlags(ClientSerializationStreamWriter.FLAG_RPC_TOKEN_INCLUDED);
    }
    return toReturn;
  }
}
