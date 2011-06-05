package frontend.client;

import java.util.List;

import utils.ConfiguredConstraintsViolatedException;
import utils.EntityExistsNotException;
import utils.InvalidDataException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import frontend.shared.ImageFEObject;

@RemoteServiceRelativePath("ImagesGalleryService")
public interface ImagesGalleryService extends RemoteService {
    ImageFEObject findByPK(Integer id) throws EntityExistsNotException;
    ImageFEObject findFirst() throws EntityExistsNotException;
    List<ImageFEObject> findAllSorted();
    int create(ImageFEObject entity) throws InvalidDataException, ConfiguredConstraintsViolatedException;
    ImageFEObject deleteByPK(Integer pk);
    /**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ImagesGalleryServiceAsync instance;
		public static ImagesGalleryServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ImagesGalleryService.class);
			}
			return instance;
		}
	}
}
