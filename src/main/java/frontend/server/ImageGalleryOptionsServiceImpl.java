package frontend.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import middle.IImageDao;
import middle.ImageGalleryOptions;
import middle.ImageType;

import utils.EntityExistsNotException;
import frontend.client.ImageGalleryOptionsService;
import frontend.shared.ImageFEObject;
import frontend.shared.ImageGalleryOptionsFEObject;
import backend.entities.ImageEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ImageGalleryOptionsServiceImpl extends RemoteServiceServlet implements ImageGalleryOptionsService {
    // middle tier -> frontend convertation  
    public static ImageGalleryOptionsFEObject m2fe(ImageGalleryOptions m_opt) {
        ImageGalleryOptionsFEObject ret = new ImageGalleryOptionsFEObject();
        
        ret.setImgAllowedTypes(new HashSet<ImageType>(m_opt.getImgAllowedTypes()));
        ret.setImgMaxSize_bytes(m_opt.getImgMaxSize_bytes());
        
        return ret;
    }
    
    // ================================
    // EXPOSED SERVICES
    
    @Override
    public ImageGalleryOptionsFEObject getCurrentOptions() {
        return m2fe(ImageGalleryOptions.getSingletonInstance());
    }
    
    @Override
    public void updateOptions(ImageGalleryOptionsFEObject new_opts) {
        ImageGalleryOptions opts = ImageGalleryOptions.getSingletonInstance(); 
        
        opts.setImgAllowedTypes(new_opts.getImgAllowedTypes());
        opts.setImgMaxSize_bytes(new_opts.getImgMaxSize_bytes());
    }
    
}
