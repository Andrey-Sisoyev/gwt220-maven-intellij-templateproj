package backend.entities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import middle.ImageType;

import utils.CloneableFixed;
import utils.ImpossibleException;
import utils.InvalidDataException;

public class ImageEntity implements CloneableFixed {

    // ================================
    // NON-STATIC STUFF

    private int id;
    private String url;
    private String title;
    private ImageType type;
    private long size;
    private boolean builtin;

    private boolean deleted; 

    // ================================
    // CONSTRUCTORS

    public ImageEntity(int id, String url, String title, ImageType type,
            long size, boolean builtin) {
        super();
        this.id = id;
        this.url = url;
        this.title = title;
        this.type = type;
        this.size = size;
        this.builtin = builtin;
    }
    
    public ImageEntity() {
        super();
    }

    // ================================
    // GETTERS/SETTERS

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String _url) {
        url = _url;
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

    public boolean isBuiltin() {
        return builtin;
    }

    public void setBuiltin(boolean _builtin) {
        builtin = _builtin;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean _deleted) {
        deleted = _deleted;
    }

    // ================================
    // METHODS
    
    // TODO: not really a rational solution against hacking
    // backends usually are isolated from WWW
    public boolean validate() throws InvalidDataException {
        try { 
            URL url = new URL(this.getUrl()); 
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            throw new InvalidDataException(e); // the URL is not in a valid form
        } catch (IOException e) {
            throw new InvalidDataException(e); // the connection couldn't be established
        }
        
        if(title.length() == 0) 
            throw new InvalidDataException("Title is not allowed to be empty!");
        return true;
    }

    // ================================
    // LOW-LEVEL OVERRIDES

    @Override
    public ImageEntity clone() {
        try {
            return (ImageEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ImpossibleException(e);
        }
    }
    
    @Override
    public String toString() {
        return "ImageEntity [id=" + id + ", url=" + url + ", title=" + title
                + ", builtin=" + builtin + ", deleted=" + deleted + "]";
    }
    
}
