package frontend.server;

import java.util.ArrayList;
import java.util.List;

import middle.IImageDao;
import utils.ConfiguredConstraintsViolatedException;
import utils.EntityExistsNotException;
import utils.InvalidDataException;
import frontend.shared.ImageFEObject;
import frontend.client.ImagesGalleryService;
import backend.ImagesDB;
import backend.entities.ImageEntity;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ImagesGalleryServiceImpl extends RemoteServiceServlet implements ImagesGalleryService {
    
    // backend -> frontend convertation  
    public static ImageFEObject be2fe(ImageEntity be_img) {
        ImageFEObject ret = new ImageFEObject();
        
        ret.setId(be_img.getId());
        ret.setTitle(be_img.getTitle());
        ret.setUrl(be_img.getUrl());
        ret.setSize(be_img.getSize());
        ret.setType(be_img.getType());
        
        return ret;
    }
    
 // backend -> frontend convertation  
    public static ImageEntity fe2be(ImageFEObject be_img) {
        ImageEntity ret = new ImageEntity();
        
        ret.setId(be_img.getId());
        ret.setTitle(be_img.getTitle());
        ret.setUrl(be_img.getUrl());
        ret.setSize(be_img.getSize());
        ret.setType(be_img.getType());
        ret.setBuiltin(false);
        ret.setDeleted(false);
        
        return ret;
    }
    
    // ================================
    // EXPOSED SERVICES
    
    public ImageFEObject findByPK(Integer id) throws EntityExistsNotException {
        System.out.println("ImagesGalleryServiceImpl.findByPK");
        return be2fe(IImageDao.DFLT_SERVICE_PROVIDER.findByPK(id));
    }
    public ImageFEObject findFirst() throws EntityExistsNotException {
        System.out.println("ImagesGalleryServiceImpl.findFirst");
        ImageEntity be_img = IImageDao.DFLT_SERVICE_PROVIDER.findFirst();
        System.out.println(be_img.toString());
        return be2fe(be_img);
    }
    
    public List<ImageFEObject> findAllSorted() {
        System.out.println("ImagesGalleryServiceImpl.findAllSorted");
        List<ImageFEObject> ret = new ArrayList<ImageFEObject>();
        for(ImageEntity img_be : IImageDao.DFLT_SERVICE_PROVIDER.findAllSorted())
            ret.add(be2fe(img_be));
        return ret;
    }
    
    public int create(ImageFEObject entity) throws InvalidDataException, ConfiguredConstraintsViolatedException {
        System.out.println("ImagesGalleryServiceImpl.create");
        return IImageDao.DFLT_SERVICE_PROVIDER.create(fe2be(entity));
    }
    
    public ImageFEObject deleteByPK(Integer pk) {
        System.out.println("ImagesGalleryServiceImpl.deleteByPK");
        return be2fe(ImagesDB.IMAGES.remove(pk));
    }
}
