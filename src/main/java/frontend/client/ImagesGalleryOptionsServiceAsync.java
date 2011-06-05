package frontend.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import frontend.shared.ImageGalleryOptionsFEObject;

public interface ImagesGalleryOptionsServiceAsync {

    void getCurrentOptions(AsyncCallback<ImageGalleryOptionsFEObject> callback);

    void updateOptions(ImageGalleryOptionsFEObject new_opts, AsyncCallback<Void> callback);

}
