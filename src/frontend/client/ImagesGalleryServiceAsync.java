package frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import frontend.shared.ImageFEObject;

public interface ImagesGalleryServiceAsync {
    void findByPK(Integer id, AsyncCallback<ImageFEObject> callback);
    void findFirst(AsyncCallback<ImageFEObject> callback);
    void findAllSorted(AsyncCallback<List<ImageFEObject>> callback);
    void create(ImageFEObject entity, AsyncCallback<Integer> callback);
    void deleteByPK(Integer pk, AsyncCallback<ImageFEObject> callback);
}
