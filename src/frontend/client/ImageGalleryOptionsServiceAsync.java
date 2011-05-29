package frontend.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import frontend.shared.ImageGalleryOptionsFEObject;

public interface ImageGalleryOptionsServiceAsync {

    void getCurrentOptions(AsyncCallback<ImageGalleryOptionsFEObject> callback);

    void updateOptions(ImageGalleryOptionsFEObject new_opts, AsyncCallback<Void> callback);

}
