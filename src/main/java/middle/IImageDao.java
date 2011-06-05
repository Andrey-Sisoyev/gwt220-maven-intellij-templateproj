package middle;

import java.util.List;

import backend.entities.ImageEntity;
import utils.ConfiguredConstraintsViolatedException;
import utils.EntityExistsNotException;
import utils.InvalidDataException;

public interface IImageDao {
    public static final IImageDao DFLT_SERVICE_PROVIDER = new ImageDao_mimicDB(); // assummed to be thread-safe and stateless
    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

    // ================================
    // METHODS
    public List<ImageEntity> findAllSorted();
    public ImageEntity findFirst() throws EntityExistsNotException;
    public ImageEntity findByPK(Integer id) throws EntityExistsNotException;
    public int create(ImageEntity entity) throws InvalidDataException, ConfiguredConstraintsViolatedException;
    public ImageEntity updateByPK(ImageEntity entity) throws InvalidDataException, ConfiguredConstraintsViolatedException, EntityExistsNotException;
    public ImageEntity deleteByPK(Integer pk);
}
