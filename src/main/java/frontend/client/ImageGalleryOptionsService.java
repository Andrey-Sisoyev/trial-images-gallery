package frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import frontend.shared.ImageGalleryOptionsFEObject;

@RemoteServiceRelativePath("ImageGalleryOptionsService")
public interface ImageGalleryOptionsService extends RemoteService {
    ImageGalleryOptionsFEObject getCurrentOptions();
    void updateOptions(ImageGalleryOptionsFEObject new_opts);
    /**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ImageGalleryOptionsServiceAsync instance;
		public static ImageGalleryOptionsServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ImageGalleryOptionsService.class);
			}
			return instance;
		}
	}
}
