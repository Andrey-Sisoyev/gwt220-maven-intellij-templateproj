package frontend.server;

import java.util.HashSet;

import middle.ImageGalleryOptions;
import middle.ImageType;

import frontend.client.ImagesGalleryOptionsService;
import frontend.shared.ImageGalleryOptionsFEObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ImagesGalleryOptionsServiceImpl extends RemoteServiceServlet implements ImagesGalleryOptionsService {
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
