package frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import utils.ConfiguredConstraintsViolatedException;
import utils.EntityExistsNotException;
import utils.InvalidDataException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import frontend.shared.ImageFEObject;

public interface ImagesGalleryServiceAsync {
    void findByPK(Integer id, AsyncCallback<ImageFEObject> async);

    void findFirst(AsyncCallback<ImageFEObject> async);

    void findAllSorted(AsyncCallback<List<ImageFEObject>> async);

    void create(ImageFEObject entity, AsyncCallback<Integer> async);

    void deleteByPK(Integer pk, AsyncCallback<ImageFEObject> async);
}
