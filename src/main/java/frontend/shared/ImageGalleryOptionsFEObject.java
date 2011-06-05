package frontend.shared;

import java.util.Set;

import middle.ImageType;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImageGalleryOptionsFEObject implements IsSerializable {
    private long imgMaxSize_bytes;
    private Set<ImageType> imgAllowedTypes;
    
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
}
