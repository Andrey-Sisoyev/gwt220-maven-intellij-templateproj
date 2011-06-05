package middle;

import backend.entities.ImageEntity;
import backend.ImagesDB;
import utils.ConfiguredConstraintsViolatedException;
import utils.EntityExistsNotException;
import utils.InvalidDataException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class ImageDao_mimicDB implements IImageDao {
    // ================================
    // NON-STATIC STUFF

    // ================================
    // CONSTRUCTORS
    public ImageDao_mimicDB() {
    }

    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

    public List<ImageEntity> findAllSorted() {
        List<ImageEntity> ret = new ArrayList<ImageEntity>(ImagesDB.IMAGES.size());
        
        TreeMap<Integer,ImageEntity> sortedImagesMap = new TreeMap<Integer,ImageEntity>(ImagesDB.IMAGES);
        for(Map.Entry<Integer,ImageEntity> imageEntry : sortedImagesMap.entrySet())
            ret.add(imageEntry.getValue().clone());
        
        return ret;
    }
    
    public ImageEntity findFirst() throws EntityExistsNotException {
        try {
            return new TreeMap<Integer,ImageEntity>(ImagesDB.IMAGES).entrySet().iterator().next().getValue().clone();
        } catch(NoSuchElementException e) {
            throw new EntityExistsNotException();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
    }

    public ImageEntity findByPK(Integer id) throws EntityExistsNotException {
        ImageEntity entity = ImagesDB.IMAGES.get(id);
        if(entity == null) throw new EntityExistsNotException();

        ImageEntity clone = entity.clone();
        if(clone.isDeleted()) throw new EntityExistsNotException();

        return clone;
    }

    public int create(ImageEntity entity) throws InvalidDataException, ConfiguredConstraintsViolatedException {
        ImageGalleryOptions.getSingletonInstance().validate(entity);
        entity.validate();

        int new_id;

        new_id = ImagesDB.IMAGES_SEQ.next();
        ImageEntity clone = entity.clone();

        clone.setId(new_id);
        clone.setDeleted(false);
        ImagesDB.IMAGES.put(new_id, clone);

        entity.setId(new_id);
        entity.setDeleted(false);
        return new_id;
    }

    public ImageEntity updateByPK(ImageEntity entity) throws InvalidDataException, ConfiguredConstraintsViolatedException, EntityExistsNotException {
        ImageGalleryOptions.getSingletonInstance().validate(entity);
        entity.validate();

        ImageEntity clone = entity.clone();
        if(ImagesDB.IMAGES.get(clone.getId()) == null) throw new EntityExistsNotException();
        
        return ImagesDB.IMAGES.put(clone.getId(), clone);
    }

    public ImageEntity deleteByPK(Integer pk) {
        return ImagesDB.IMAGES.remove(pk);
    }

    // ================================
    // LOW-LEVEL OVERRIDES

}
