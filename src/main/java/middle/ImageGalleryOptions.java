package middle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import backend.entities.ImageEntity;

import utils.ConfiguredConstraintsViolatedException;

// singleton
public class ImageGalleryOptions {
    private static final           long CFG__DFLT_MAX_IMG_SIZE = 10*1024*1024;
    private static final Set<ImageType> CFG__DFLT_ALLOWED_TYPES = new HashSet(Arrays.asList(ImageType.JPG, ImageType.PNG, ImageType.NET));
    
    private static final ImageGalleryOptions singletonInstance = new ImageGalleryOptions(); 
    public  static ImageGalleryOptions getSingletonInstance() { return singletonInstance; }
    
    // ================================
    // NON-STATIC STUFF
    
    private long imgMaxSize_bytes;
    private Set<ImageType> imgAllowedTypes; 
    
    // ================================
    // CONSTRUCTORS
    private ImageGalleryOptions() {
        imgAllowedTypes = new HashSet<ImageType>(CFG__DFLT_ALLOWED_TYPES);
        imgMaxSize_bytes = CFG__DFLT_MAX_IMG_SIZE;
    }
    
    // ================================
    // GETTERS/SETTERS
    public long getImgMaxSize_bytes() {
        return imgMaxSize_bytes;
    }

    public void setImgMaxSize_bytes(long imgMaxSize_bytes) {
        this.imgMaxSize_bytes = imgMaxSize_bytes;
    }

    public Set<ImageType> getImgAllowedTypes() {
        return imgAllowedTypes;
    }

    public void setImgAllowedTypes(Set<ImageType> imgAllowedTypes) {
        this.imgAllowedTypes = imgAllowedTypes;
    }
    // ================================
    // METHODS
    
    private final static String ERR__TOOBIG_IMAGE = "Image size (%1$d bytes) is too big! Maximum allowed is %2$d bytes.";
    private final static String ERR__IMAGE_TYPE_NOT_ALLOWED = "Image type (%1$s) is not allowed! Allowed images types: %2$s.";
    
    public boolean validate(ImageEntity img) throws ConfiguredConstraintsViolatedException {
        if(img.getSize() > this.getImgMaxSize_bytes()) 
            throw new ConfiguredConstraintsViolatedException(String.format( ERR__TOOBIG_IMAGE, img.getSize(), this.getImgMaxSize_bytes()));
        if(!this.getImgAllowedTypes().contains(img.getType())) 
            throw new ConfiguredConstraintsViolatedException(String.format(ERR__IMAGE_TYPE_NOT_ALLOWED, img.getType(), Arrays.toString(this.getImgAllowedTypes().toArray())));
        return true;
    }
}
