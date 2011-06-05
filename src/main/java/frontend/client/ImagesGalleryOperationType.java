package frontend.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import frontend.shared.ImageFEObject;
import frontend.shared.ImageGalleryOptionsFEObject;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.SingleUploader;
import middle.ImageType;
import utils.InvalidDataException;
import utils.frontend.client.ErrorsControl;
import utils.frontend.client.ISimpleFormOperation;
import utils.frontend.client.SimpleFormStateMachine;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static utils.frontend.client.SimpleFormStateMachine.*;
import static utils.frontend.client.SimpleOperationTypeMachine.*;

public enum ImagesGalleryOperationType {
    ADD_IMG, REMOVE_IMG, OPTIONS;

    private static int CFG__THUMBNAIL_SIZE = 250; // TODO: busioness specific

    // GWT 2.2.0 core won't support String.format(...)
    // private static final String MSG__RemovalTargetImgId = "Image with ID: %1$d";
    // private static final String MSG__CRUD_IMGOP_SUCCESS = "Successfull operation \"%1$s\" with image ID: %2$d!";
    // private static final String MSG__CRUD_OP_SUCCESS = "Successfull operation \"%1$s\"!";
    public static String MSG__RemovalTargetImgId(Integer imgId) { return "Target image ID: " + imgId; }
    public static String MSG__CRUD_IMGOP_SUCCESS(String op, Integer imgId) { return "Successfull operation \"" + op + "\" with image ID: " + imgId + "!"; }
    public static String MSG__CRUD_OP_SUCCESS(String op) { return "Successfull operation \"" + op + "\"!"; }

    public ISimpleFormOperation<ImageFEObject> getFormOperation(final ImagesGallery host) {
        final ImagesGalleryOperationType THIS_OPERATION = this;
        switch(this) {
            case ADD_IMG: return new ISimpleFormOperation<ImageFEObject>() {

                // ===========================
                // CONTROLS

                private final Label   lbImageName = new Label("Image title: ");
                private final TextBox tbImageName = new TextBox();
                private final Panel  panImageName = new HorizontalPanel();

                private final Label   lbImageURL = new Label("Image URL: ");
                private final TextBox tbImageURL = new TextBox();
                private final Panel  panImageURL = new HorizontalPanel();

                private final Label  lbImageSize = new Label("Image size (bytes): ");
                private final Label  tbImageSize = new Label(); // content set dynamically
                private final Panel panImageSize = new HorizontalPanel();

                private final Label  lbImageType = new Label("Image type: ");
                private final Label  tbImageType = new Label(); // content set dynamically
                private final Panel panImageType = new HorizontalPanel();

                private final Panel panThumbnail = new SimplePanel();
                private final SingleUploader suplImageUploader = new SingleUploader();

                public boolean commitWouldRequireTableRefresh() { return true; }

                // ===========================
                // IMPLEMENTATION

                @Override public boolean initOperationRequiresExistingObject() { return false; }
                @Override public String toHumanReadable() { return "Add Image"; }
                @Override public String buttonTitle() { return toHumanReadable(); }
                @Override public ImageFEObject getOperatedObject() { throw new UnsupportedOperationException(); }

                @Override public void initFormFieldsPanelControls(Panel formFieldsPanel) {
                    VerticalPanel localRootPanel = new VerticalPanel();
                    formFieldsPanel.add(localRootPanel);

                    localRootPanel.add(panImageName);
                    localRootPanel.add(panImageURL);
                    localRootPanel.add(panImageSize);
                    localRootPanel.add(panImageType);
                    localRootPanel.add(suplImageUploader);
                    localRootPanel.add(panThumbnail);

                    panImageName.add(lbImageName);
                    panImageName.add(tbImageName);

                    panImageURL.add(lbImageURL);
                    panImageURL.add(tbImageURL);

                    panImageSize.add(lbImageSize);
                    panImageSize.add(tbImageSize);

                    panImageType.add(lbImageType);
                    panImageType.add(tbImageType);

                    suplImageUploader.avoidRepeatFiles(false);

                    panThumbnail.setStylePrimaryName("thumbnail");

                    // Load the image in the document and in the case of success attach it to the viewer
                    // TODO solve problem: attach marker??
                    IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
                        public void onFinish(IUploader uploader) {
                            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {

                                // The server sends useful information to the client by default
                                IUploader.UploadedInfo info = uploader.getServerInfo();

                                tbImageName.setFocus(true);
                                tbImageURL.setText(Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/" + uploader.fileUrl());
                                tbImageSize.setText(String.valueOf(info.size));
                                tbImageType.setText(info.ctype);

                                new PreloadedImage(uploader.fileUrl(), new PreloadedImage.OnLoadPreloadedImageHandler() {
                                        public void onLoad(PreloadedImage image) {
                                            panThumbnail.clear();
                                            image.setWidth(CFG__THUMBNAIL_SIZE + "px");
                                            panThumbnail.add(image);
                                        }
                                    }
                                );

                                System.out.println("File name " + info.name);
                                System.out.println("File content-type " + info.ctype);
                                System.out.println("File size " + info.size);

                                // You can send any customized message and parse it
                                System.out.println("Server message " + info.message);
                            }
                        }
                    };

                    // Add a finish handler which will load the image once the upload finishes
                    suplImageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
                }

                @Override public SimpleFormStateMachine.FormState getFormInitialState() { return FormState.LOADING; }

                @Override public void initOp(ImageFEObject obj, ConversationMarker _currentMarker) { throw new UnsupportedOperationException(); }
                @Override public void initOp(final ConversationMarker _currentMarker) {
                    System.out.println(THIS_OPERATION.name() +".initOp(ConversationMarker)");
                    this.tbImageName.setText("");
                    this.tbImageURL.setText("");
                    this.tbImageSize.setText("");
                    this.tbImageType.setText("");
                    this.panThumbnail.clear();

                    host.getCommunicationWithServer().getOptionsService().getCurrentOptions(new AsyncCallback<ImageGalleryOptionsFEObject>() {
                        @Override public void onFailure(Throwable caught) {
                            System.out.println(THIS_OPERATION.name() +".initOp(ConversationMarker).getCurrentOptions(): failed");
                            assert host.getMachSimpleFormState().isLoading();

                            host.getViewErrorsControl().addError(caught);
                            host.getMachSimpleFormState().cancel();
                        }
                        @Override
                        public void onSuccess(ImageGalleryOptionsFEObject result) {
                            if(! _currentMarker.isCurrent()) return; // obsolete operation

                            assert host.getMachSimpleFormState().isLoading();

                            List<String> allowedTypesExts = new LinkedList<String>();
                            for(ImageType it : result.getImgAllowedTypes()) {
                                allowedTypesExts.addAll(it.getAssociatedExtensions());
                            }
                            suplImageUploader.setValidExtensions(allowedTypesExts.toArray(new String[allowedTypesExts.size()]));

                            host.getMachSimpleFormState().toggleUI();
                        }
                    });

                }

                @Override public void commitOperation(final ErrorsControl errorsControl, final ConversationMarker _currentMarker) {
                    ImageFEObject imgToSave = null;
                    try {
                        imgToSave = getImgFromForm_forCreation();
                    } catch (InvalidDataException e) {
                        System.out.println(THIS_OPERATION.name() +".commitOperation(...): InvalidDataException");
                        e.printStackTrace();
                        errorsControl.addError(e);
                        host.getMachSimpleFormState().toggleUI();
                        System.out.println(THIS_OPERATION.name() +".commitOperation(...): InvalidDataException catch end");
                        return;
                    }

                    final ISimpleFormOperation<ImageFEObject> FormOperation_THIS = this;

                    if(imgToSave != null)
                        host.getCommunicationWithServer().getGalleryService().create(imgToSave, new AsyncCallback<Integer>(){
                            @Override public void onFailure(Throwable caught) {
                                if(_currentMarker.isCurrent()) {
                                    assert host.getMachSimpleFormState().isLoading();

                                    errorsControl.addError(caught);
                                    host.getMachSimpleFormState().toggleUI();
                                }
                            }
                            @Override
                            public void onSuccess(Integer result) {
                                if(_currentMarker.isCurrent()) {
                                    assert host.getMachSimpleFormState().isLoading();

                                    if(commitWouldRequireTableRefresh()) host.refreshTable();
                                    host.getMachSimpleFormState().toggleStatus(MSG__CRUD_IMGOP_SUCCESS(FormOperation_THIS.toHumanReadable(), result));
                                }
                            }
                        });
                }

                private ImageFEObject getImgFromForm_forCreation() throws InvalidDataException {
                    ImageFEObject imgToSave = new ImageFEObject();

                    imgToSave.setId(-1); // will be autognerated by the back-end
                    imgToSave.setTitle(tbImageName.getText());
                    imgToSave.setUrl(tbImageURL.getText());
                    if(tbImageSize.getText() != "")
                        try { imgToSave.setSize(Long.parseLong(tbImageSize.getText()));
                        } catch(NumberFormatException e) { throw new InvalidDataException(e); }
                    else imgToSave.setSize(0);
                    if(tbImageType.getText() != "")
                         imgToSave.setType(ImageType.fromMime(tbImageType.getText()));
                    else imgToSave.setType(ImageType.NET);

                    return imgToSave;
                }
            };


            // ==============================================================================
            // ==============================================================================
            // ==============================================================================


            case REMOVE_IMG: return new ISimpleFormOperation<ImageFEObject>() {

                // ===========================
                // CONTROLS

                private final Label lbTargetImageInfo = new Label(); // content set dynamically
                private final Label lbDeleteQuestion = new Label("Are you sure to delete this image?");
                private Integer imgPK;

                public boolean commitWouldRequireTableRefresh() { return true; }

                // ===========================
                // IMPLEMENTATION

                @Override public boolean initOperationRequiresExistingObject() { return true; }
                @Override public String toHumanReadable() { return "Remove Image"; }
                @Override public String buttonTitle() { return toHumanReadable(); }
                @Override public ImageFEObject getOperatedObject() { return host.getSelectedImage(); }

                @Override public void initFormFieldsPanelControls(Panel formFieldsPanel) {
                    VerticalPanel localRootPanel = new VerticalPanel();
                    formFieldsPanel.add(localRootPanel);

                    localRootPanel.add(lbTargetImageInfo);
                    localRootPanel.add(lbDeleteQuestion);
                }

                @Override public SimpleFormStateMachine.FormState getFormInitialState() { return FormState.UI; }

                @Override public void initOp(ImageFEObject obj, ConversationMarker _currentMarker) {
                    assert obj != null;

                    imgPK = obj.getId();
                    lbTargetImageInfo.setText(MSG__RemovalTargetImgId(imgPK));
                }
                @Override public void initOp(final ConversationMarker _currentMarker) { throw new UnsupportedOperationException(); }

                @Override public void commitOperation(final ErrorsControl errorsControl, final ConversationMarker _currentMarker) {
                    final Integer tagetImgId = imgPK;
                    final ISimpleFormOperation<ImageFEObject> FormOperation_THIS = this;

                    host.getCommunicationWithServer().getGalleryService().deleteByPK(tagetImgId, new AsyncCallback<ImageFEObject>(){
                        @Override public void onFailure(Throwable caught) {
                            if(_currentMarker.isCurrent()) {
                                assert host.getMachSimpleFormState().isLoading();

                                errorsControl.addError(caught);
                                host.getMachSimpleFormState().toggleUI();
                            }
                        }
                        @Override
                        public void onSuccess(ImageFEObject result) {
                            if(_currentMarker.isCurrent()) {
                                    assert host.getMachSimpleFormState().isLoading();

                                    if(commitWouldRequireTableRefresh()) host.refreshTable();
                                    host.getMachSimpleFormState().toggleStatus(MSG__CRUD_IMGOP_SUCCESS(FormOperation_THIS.toHumanReadable(), result.getId()));
                                }
                        }
                    });
                }
            };


            // ==============================================================================
            // ==============================================================================
            // ==============================================================================


            case OPTIONS:    return new ISimpleFormOperation<ImageFEObject>() {

                // ===========================
                // CONTROLS

                private final Label lbImgSizeCnstr = new Label("Image max size (in bytes): ");
                private final TextBox tbImgSizeCnstr = new TextBox();
                private final Panel panImgSizeCnstr = new HorizontalPanel();

                private final Label lbImgTypeCnstr = new Label("Allowed (for upload) images types: ");
                private final ListBox libImgTypeCnstr = new ListBox(true);
                private final Panel panImgTypeCnstr = new HorizontalPanel();

                public boolean commitWouldRequireTableRefresh() { return false; }

                // ===========================
                // IMPLEMENTATION

                @Override public boolean initOperationRequiresExistingObject() { return false; }
                @Override public String toHumanReadable() { return "Image Upload Options"; }
                @Override public String buttonTitle() { return toHumanReadable(); }
                @Override public ImageFEObject getOperatedObject() { throw new UnsupportedOperationException(); }

                @Override public void initFormFieldsPanelControls(Panel formFieldsPanel) {
                    VerticalPanel localRootPanel = new VerticalPanel();
                    formFieldsPanel.add(localRootPanel);

                    localRootPanel.add(panImgSizeCnstr);
                    localRootPanel.add(panImgTypeCnstr);

                    panImgSizeCnstr.add(lbImgSizeCnstr);
                    panImgSizeCnstr.add(tbImgSizeCnstr);

                    panImgTypeCnstr.add( lbImgTypeCnstr);
                    panImgTypeCnstr.add(libImgTypeCnstr);

                    for(ImageType it : ImageType.values())
                        libImgTypeCnstr.addItem(it.getTitle());
                    libImgTypeCnstr.setVisibleItemCount(6);
                }

                @Override public SimpleFormStateMachine.FormState getFormInitialState() { return FormState.LOADING; }

                @Override public void initOp(ImageFEObject obj, ConversationMarker _currentMarker) { throw new UnsupportedOperationException(); }
                @Override public void initOp(final ConversationMarker _currentMarker) {
                    System.out.println(THIS_OPERATION.name() +".initOp(ConversationMarker)");

                    host.getCommunicationWithServer().getOptionsService().getCurrentOptions(new AsyncCallback<ImageGalleryOptionsFEObject>() {
                        @Override public void onFailure(Throwable caught) {
                            if(_currentMarker.isCurrent()) {
                                assert host.getMachSimpleFormState().isLoading();

                                host.getViewErrorsControl().addError(caught);
                                host.getMachSimpleFormState().cancel();
                            }
                        }
                        @Override
                        public void onSuccess(ImageGalleryOptionsFEObject result) {
                            if(_currentMarker.isCurrent()) {
                                assert host.getMachSimpleFormState().isLoading();

                                tbImgSizeCnstr.setText(String.valueOf(result.getImgMaxSize_bytes()));

                                int itemsCount = libImgTypeCnstr.getItemCount();

                                for(int i = 0; i < itemsCount; i++) {
                                    ImageType lbImageType = ImageType.fromTitle(libImgTypeCnstr.getItemText(i));
                                    boolean selected = result.getImgAllowedTypes().contains(lbImageType);
                                    libImgTypeCnstr.setItemSelected(i, selected);
                                }

                                host.getMachSimpleFormState().toggleUI();
                            }
                        }
                    });
                }

                @Override public void commitOperation(final ErrorsControl errorsControl, final ConversationMarker _currentMarker) {
                    ImageGalleryOptionsFEObject optsToSave = null;
                    try {
                        optsToSave = getOptionsFromForm_forUpdate();
                    } catch (InvalidDataException e) {
                        e.printStackTrace();
                        errorsControl.addError(e);
                        host.getMachSimpleFormState().toggleUI();
                        return;
                    }

                    if(optsToSave != null) {
                        final ImageGalleryOptionsFEObject final_optsToSave = optsToSave;
                        final ISimpleFormOperation<ImageFEObject> FormOperation_THIS = this;

                        host.getCommunicationWithServer().getOptionsService().updateOptions(optsToSave, new AsyncCallback<Void>(){
                            @Override public void onFailure(Throwable caught) {
                                if(_currentMarker.isCurrent()) {
                                    assert host.getMachSimpleFormState().isLoading();

                                    errorsControl.addError(caught);
                                    host.getMachSimpleFormState().toggleUI();
                                }
                            }
                            @Override
                            public void onSuccess(Void result) {
                                if(_currentMarker.isCurrent()) {
                                    assert host.getMachSimpleFormState().isLoading();

                                    if(commitWouldRequireTableRefresh()) host.refreshTable();
                                    host.getMachSimpleFormState().toggleStatus(MSG__CRUD_OP_SUCCESS(FormOperation_THIS.toHumanReadable()));
                                }
                            }
                        });
                    }
                }

                public ImageGalleryOptionsFEObject getOptionsFromForm_forUpdate() throws InvalidDataException {
                    ImageGalleryOptionsFEObject optsToSave = new ImageGalleryOptionsFEObject();

                    try {
                        optsToSave.setImgMaxSize_bytes(Long.parseLong(tbImgSizeCnstr.getText()));
                    } catch (NumberFormatException e) {
                        throw new InvalidDataException("Iamge size constraint field should contain a number!", e);
                    }

                    Set<ImageType> newAllowedTypes = new HashSet<ImageType>();
                    Integer itemsCount = this.libImgTypeCnstr.getItemCount();
                    for(int i = 0; i < itemsCount; i++) {
                        if(this.libImgTypeCnstr.isItemSelected(i))
                            newAllowedTypes.add(ImageType.fromTitle(libImgTypeCnstr.getItemText(i))); // TODO: it's actually safe to get ImageType by index from enum
                    }
                    optsToSave.setImgAllowedTypes(newAllowedTypes);

                    return optsToSave;
                }
            };
        }
        throw new UnsupportedOperationException(this.name());
    }
    public boolean commitWouldRequireTableRefresh() {
        switch(this) {
            case ADD_IMG:
            case REMOVE_IMG: return true;
            case OPTIONS:    return false;
        }
        throw new UnsupportedOperationException(this.name());
    }
    public String toHumanReadable() {
        switch(this) {
            case ADD_IMG:    return "Image addition";
            case REMOVE_IMG: return "Image removal";
            case OPTIONS:    return "Options change";
        }
        throw new UnsupportedOperationException(this.name());
    }
}
