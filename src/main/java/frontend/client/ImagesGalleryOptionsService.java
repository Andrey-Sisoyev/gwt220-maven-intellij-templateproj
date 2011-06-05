package frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import frontend.shared.ImageGalleryOptionsFEObject;

@RemoteServiceRelativePath("ImagesGalleryOptionsService")
public interface ImagesGalleryOptionsService extends RemoteService {
    ImageGalleryOptionsFEObject getCurrentOptions();
    void updateOptions(ImageGalleryOptionsFEObject new_opts);
    /**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ImagesGalleryOptionsServiceAsync instance;
		public static ImagesGalleryOptionsServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ImagesGalleryOptionsService.class);
			}
			return instance;
		}
	}
}
