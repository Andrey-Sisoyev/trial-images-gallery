package frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ImageGalleryOptionsServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImageGalleryOptionsService
     */
    void getCurrentOptions( AsyncCallback<frontend.shared.ImageGalleryOptionsFEObject> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImageGalleryOptionsService
     */
    void updateOptions( frontend.shared.ImageGalleryOptionsFEObject p0, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ImageGalleryOptionsServiceAsync instance;

        public static final ImageGalleryOptionsServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ImageGalleryOptionsServiceAsync) GWT.create( ImageGalleryOptionsService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "ImageGalleryOptionsService" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
