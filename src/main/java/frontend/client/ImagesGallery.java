package frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import frontend.shared.ImageFEObject;
import utils.frontend.client.ErrorsControl;
import utils.frontend.client.SimpleFormStateMachine;
import utils.frontend.client.SimpleOperationTypeMachine;

import java.util.List;

public class ImagesGallery {
    private static int CFG__TABLE_WIDTH = 500;
    private static int CFG__TABLE_HEIGHT = 350;
    private static int CFG__IMG_WIDTH = 400; // TODO: externalize

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    private static final String ERR__NoImages = "No images in DB";
    private static final String ERR__NoErrors = "No errors";

    // ==================================
    // COMMUNICATION WITH SERVER
    public static class CommunicationsWithServer {
        private final ImagesGalleryServiceAsync galleryService = GWT.create(ImagesGalleryService.class);
        private final ImagesGalleryOptionsServiceAsync optionsService = GWT.create(ImagesGalleryOptionsService.class);

        public ImagesGalleryServiceAsync getGalleryService() {
            return galleryService;
        }
        public ImagesGalleryOptionsServiceAsync getOptionsService() {
            return optionsService;
        }
    }

    private final CommunicationsWithServer comWithServer = new CommunicationsWithServer();

    // ==================================
    // CONTROLS

    final ErrorsControl ctrlViewErrors = new ErrorsControl();

    final Panel panRootWithErrors = new VerticalPanel();
    final Panel panRoot     = new HorizontalPanel();
    final Panel panCrudoComplect
                            = new VerticalPanel();
    final Panel panControls = new VerticalPanel();
    final Panel panCrudoButtons = new HorizontalPanel();

    final Panel panTable = new VerticalPanel();

    private final CellTable<ImageFEObject> tabImages = new CellTable<ImageFEObject>(); // content set dynamically
    private final SimplePager simplePager = new SimplePager();
    private final SingleSelectionModel<ImageFEObject> tabImagesSelectionModel = new SingleSelectionModel<ImageFEObject>();
    private final Panel panSplitBetweenTableAndImage = new SimplePanel();
    private final Panel panImage = new SimplePanel(); // content set dynamically
    private final Button btnRefresh    = new Button("Refresh table");

    SimpleFormStateMachine machSimpleFormState = new SimpleFormStateMachine() {
        @Override protected void onFormCommit() { machOperationsType.commitOperation(); }
    };
    private final SimpleOperationTypeMachine<ImageFEObject> machOperationsType =
            new SimpleOperationTypeMachine<ImageFEObject>(machSimpleFormState, panCrudoButtons);

    // ================================
    // CONSTRUCTORS


    public void onModuleLoad() {
        final ImagesGallery SUPER_THIS = this;

        // ==================================
        // LAYOUT

        // panRoot gets added to the RootPanel after all the setup is done

        panRootWithErrors.add(ctrlViewErrors.getLbErrors());
        panRootWithErrors.add(panRoot);

        panRoot.add(panCrudoComplect);
        panRoot.add(panSplitBetweenTableAndImage);
        panRoot.add(panImage);

        panCrudoComplect.add(panTable);
        panCrudoComplect.add(panTable);
        panCrudoComplect.add(panControls);

        panControls.add(panCrudoButtons);
        panCrudoButtons.add(btnRefresh);
        panControls.add(machSimpleFormState.getRootFormPanel());

        panTable.add(tabImages);
        panTable.add(simplePager);

        // ==================================
        // CONTROLS STYLES AND INITIAL CONTENTS
        for(ImagesGalleryOperationType opType : ImagesGalleryOperationType.values())
            this.machOperationsType.addOperation(opType.getFormOperation(this));

        panTable.setWidth(CFG__TABLE_WIDTH + "px");
        panTable.setHeight(CFG__TABLE_HEIGHT + "px");

        panSplitBetweenTableAndImage.setWidth("10px");
        panSplitBetweenTableAndImage.setHeight("1px");

        // table
        tabImages.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        TextColumn<ImageFEObject> colImageId = new TextColumn<ImageFEObject>() {
            @Override public String getValue(ImageFEObject object) { return String.valueOf(object.getId()); }
        };
        TextColumn<ImageFEObject> colImageTitle = new TextColumn<ImageFEObject>() {
            @Override public String getValue(ImageFEObject object) { return object.getTitle(); }
        };
        TextColumn<ImageFEObject> colImageURL = new TextColumn<ImageFEObject>() {
            @Override public String getValue(ImageFEObject object) { return object.getUrl(); }
        };
        tabImages.addColumn(colImageId, "Id");
        tabImages.addColumn(colImageTitle, "Title");
        tabImages.addColumn(colImageURL, "Url");

        simplePager.setDisplay(tabImages);
        simplePager.setPageSize(5);
        final ListDataProvider<ImageFEObject> dataProvider = new ListDataProvider<ImageFEObject>();
        dataProvider.addDataDisplay(tabImages);

        // ==================================
        // CONTROLS BEHAVIOUR

        // Add a selection model to handle user selection.
        tabImages.setSelectionModel(tabImagesSelectionModel);
        tabImagesSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                ImageFEObject selected = tabImagesSelectionModel.getSelectedObject();
                if (selected != null) {
                    SUPER_THIS.showImage(selected.getUrl());
                    // sharing single image between different URLs shows strange blinking in firefox
                }
            }
        });

        btnRefresh.addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent event) {
                // TODO add loading animation
                AsyncCallback<List<ImageFEObject>> getFirstRowsAsync = new AsyncCallback<List<ImageFEObject>>() {

                    @Override public void onFailure(Throwable caught) {
                        SUPER_THIS.getViewErrorsControl().addError(caught);
                    }

                    @Override public void onSuccess(List<ImageFEObject> result) {
                        tabImages.setRowData(0, result);
                        tabImages.setRowCount(result.size(), true);
                        tabImages.redraw();

                        dataProvider.setList(result);
                    }
                };

                SUPER_THIS.getCommunicationWithServer().getGalleryService().findAllSorted(getFirstRowsAsync);
            }
        });

        // ==================================
        // FINISH
        RootPanel.get("mainPanelContainer").add(panRootWithErrors);
        this.refreshTable();
    }

    // ================================
    // GETTERS/SETTERS
    public ImageFEObject getSelectedImage() {
        return tabImagesSelectionModel.getSelectedObject();
    }

    public CommunicationsWithServer getCommunicationWithServer() {
        return this.comWithServer;
    }

    public ErrorsControl getViewErrorsControl() {
        return this.ctrlViewErrors;
    }

    public SimpleFormStateMachine getMachSimpleFormState() {
        return machSimpleFormState;
    }


    // ================================
    // METHODS
    public void refreshTable() {
        btnRefresh.click();
    }

    public void showImage(String url) {
        Image img = new Image(url);
        img.setWidth(CFG__IMG_WIDTH + "px");
        panImage.clear();
        panImage.add(img);
    }

    // ================================
    // LOW-LEVEL OVERRIDES

}
