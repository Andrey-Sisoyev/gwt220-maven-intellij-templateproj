package middle;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum ImageType {
    JPG, GIF, PNG, NET;
    public String getTitle() {
        switch(this) {
            case NET: return "www.url";
            case JPG: return "jpg/jpeg";
            case GIF: return "gif";
            case PNG: return "png";
        }
        throw new UnsupportedOperationException(this.name());
    }
    // TODO: exploit functional dependency on getAssociatedExtensions()
    public static ImageType fromTitle(String imgTypeTitle) {
        if("jpg/jpeg".equalsIgnoreCase(imgTypeTitle))
            return JPG;
        else if("gif".equalsIgnoreCase(imgTypeTitle))
            return GIF;
        else if("png".equalsIgnoreCase(imgTypeTitle))
            return PNG;
        else if("www.url".equalsIgnoreCase(imgTypeTitle))
            return NET;
        else return null;
    }
    
    public List<String> getAssociatedExtensions() {
        switch(this) {
            case JPG: return Arrays.asList("jpg", "jpeg");
            case GIF: return Arrays.asList("gif");
            case PNG: return Arrays.asList("png");
            case NET: return Arrays.asList();
        } 
        throw new UnsupportedOperationException(this.name());
    }
 // TODO: exploit functional dependency on getMime()
    public static ImageType fromMime(String text) {
        if("image/jpeg".equalsIgnoreCase(text) || "image/pjpeg".equalsIgnoreCase(text))
            return JPG;
        else if("image/gif".equalsIgnoreCase(text))
            return GIF;
        else if("image/png".equalsIgnoreCase(text))
            return PNG;
        else if("".equalsIgnoreCase(text))
            return NET;
        else return null;
    }
    
}