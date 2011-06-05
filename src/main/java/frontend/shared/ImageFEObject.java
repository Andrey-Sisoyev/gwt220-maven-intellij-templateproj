package frontend.shared;

import middle.ImageType;

import com.google.gwt.user.client.rpc.IsSerializable;

// needed, because GWT constraints classpath visibility 
// it's impossible to import ImageEntity, because it won't accept cloning exceptions
public class ImageFEObject implements IsSerializable {
    // ================================
    // NON-STATIC STUFF
    private int id;
    private String url;
    private String title;
    private ImageType type;
    private long size;

    // ================================
    // CONSTRUCTORS
    public ImageFEObject() {
        super();
    }

    // ================================
    // GETTERS/SETTERS

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
