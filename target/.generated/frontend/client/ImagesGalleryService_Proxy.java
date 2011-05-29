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

public class ImagesGalleryService_Proxy extends RemoteServiceProxy implements frontend.client.ImagesGalleryServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "frontend.client.ImagesGalleryService";
  private static final String SERIALIZATION_POLICY ="648531B10540C1B3C1CDFBDA1D7E92F8";
  private static final frontend.client.ImagesGalleryService_TypeSerializer SERIALIZER = new frontend.client.ImagesGalleryService_TypeSerializer();
  
  public ImagesGalleryService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "ImagesGalleryService", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void create(frontend.shared.ImageFEObject entity, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.create", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("create");
      streamWriter.writeInt(1);
      streamWriter.writeString("frontend.shared.ImageFEObject/163636196");
      streamWriter.writeObject(entity);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.create",  "requestSerialized"));
      doInvoke(ResponseReader.INT, "ImagesGalleryService_Proxy.create", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void deleteByPK(java.lang.Integer pk, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.deleteByPK", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("deleteByPK");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.Integer/3438268394");
      streamWriter.writeObject(pk);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.deleteByPK",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "ImagesGalleryService_Proxy.deleteByPK", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void findAllSorted(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findAllSorted", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("findAllSorted");
      streamWriter.writeInt(0);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findAllSorted",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "ImagesGalleryService_Proxy.findAllSorted", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void findByPK(java.lang.Integer id, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findByPK", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("findByPK");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.Integer/3438268394");
      streamWriter.writeObject(id);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findByPK",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "ImagesGalleryService_Proxy.findByPK", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
  
  public void findFirst(com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findFirst", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      if (getRpcToken() != null) {
        streamWriter.writeObject(getRpcToken());
      }
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("findFirst");
      streamWriter.writeInt(0);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("ImagesGalleryService_Proxy.findFirst",  "requestSerialized"));
      doInvoke(ResponseReader.OBJECT, "ImagesGalleryService_Proxy.findFirst", statsContext, payload, callback);
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
