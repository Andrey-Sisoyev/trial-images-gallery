package frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ImagesGalleryServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImagesGalleryService
     */
    void create( frontend.shared.ImageFEObject p0, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImagesGalleryService
     */
    void findByPK( java.lang.Integer p0, AsyncCallback<frontend.shared.ImageFEObject> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImagesGalleryService
     */
    void findFirst( AsyncCallback<frontend.shared.ImageFEObject> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImagesGalleryService
     */
    void findAllSorted( AsyncCallback<java.util.List> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see frontend.client.ImagesGalleryService
     */
    void deleteByPK( java.lang.Integer p0, AsyncCallback<frontend.shared.ImageFEObject> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ImagesGalleryServiceAsync instance;

        public static final ImagesGalleryServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ImagesGalleryServiceAsync) GWT.create( ImagesGalleryService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "ImagesGalleryService" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
