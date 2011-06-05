package utils.frontend.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public abstract class SimpleFormStateMachine {
    private static int CFG__FORMFIELDS_WIDTH = 300;

    public enum FormState {
        UI, LOADING, STATUS;

        public Panel getFormPanel(SimpleFormStateMachine _sm) {
            switch(this) {
                case UI:      return _sm.panFormUI;
                case LOADING: return _sm.panFormLoading;
                case STATUS:  return _sm.panFormAnswerStatus;
            }
            throw new UnsupportedOperationException(this.name());
        }
        public boolean mayBeInitialState() {
            switch(this) {
                case UI:
                case LOADING: return true;
                case STATUS: return false;
            }
            throw new UnsupportedOperationException(this.name());
        }
        public void toggleIitialState(SimpleFormStateMachine mach) {
            assert mayBeInitialState();

            switch(this) {
                case UI: mach.toggleUI(); break;
                case LOADING: mach.toggleLoading(); break;
                case STATUS: mach.toggleStatus(null); break;
                default: throw new UnsupportedOperationException(this.name());
            }
        }
    }

    // ================================
    // NON-STATIC STUFF

    private volatile FormState state;

    private final Panel panForm             = new HorizontalPanel();
    private final Panel panFormUI           = new HorizontalPanel();
    private final Panel panFormLoading      = new VerticalPanel();
    private final Panel panFormAnswerStatus = new VerticalPanel();

    private final VerticalPanel panFormFieldsC = new VerticalPanel(); // content set dynamically
    private final Panel panFormFields  = new FormPanel(); // content set dynamically
    private final ErrorsControl ctlErrors = new ErrorsControl();
    VerticalPanel formFieldsWrapperPanel = new VerticalPanel();

    private final Panel panSplitBetweenFieldsAndButtons = new SimplePanel();
    private final Panel panFormButtons = new VerticalPanel();

    private final Label lbOperationFeedback = new Label(); // content set dynamically
    private final Image imgLoading = new Image("/loading.gif");

    private final Button btnFormCommit = new Button("Commit");
    private final Button btnFormCancel = new Button("Cancel");
    // TODO: a safer approach would be to have separate buttons for each form
    // despite they would be identical, the control flow would be more secured

    protected abstract void onFormCommit();

    // ================================
    // CONSTRUCTORS

    public SimpleFormStateMachine() {
        super();

        final SimpleFormStateMachine FSM_SUPER = this;

        // ==================================
        // LAYOUT

        panForm.add(panFormUI);
        panForm.add(panFormLoading);
        panForm.add(panFormAnswerStatus);

        panFormUI.add(panFormFields);
        panFormUI.add(panSplitBetweenFieldsAndButtons);
        panFormUI.add(panFormButtons);

        panFormFields.add(formFieldsWrapperPanel);

        ctlErrors.addToPanel(formFieldsWrapperPanel);
        formFieldsWrapperPanel.add(panFormFieldsC);

        panFormLoading.add(imgLoading);
        panFormAnswerStatus.add(lbOperationFeedback);

        panFormButtons.add(btnFormCommit);
        panFormButtons.add(btnFormCancel);

        // ==================================
        // CONTROLS STYLES AND INITIAL CONTENTS

        panSplitBetweenFieldsAndButtons.setWidth("10px");
        panSplitBetweenFieldsAndButtons.setHeight("1px");

        panFormFields.setWidth(CFG__FORMFIELDS_WIDTH + "px");
        panFormFieldsC.setWidth(CFG__FORMFIELDS_WIDTH + "px");

        panFormUI.setVisible(false);
        panFormLoading.setVisible(false);
        panFormAnswerStatus.setVisible(false);

        lbOperationFeedback.setStyleName("success"); // TODO: how do we assign CSS class???

        // ==================================
        // CONTROLS BEHAVIOUR

        btnFormCommit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                FSM_SUPER.onFormCommit(); // abstract
            }
        });


        btnFormCancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                FSM_SUPER.cancel();
            }
        });

    }

    // ================================
    // GETTERS/SETTERS
    public Panel getRootFormPanel() {
        return panForm;
    }

    public VerticalPanel getPanFormFields() {
        return panFormFieldsC;
    }

    public boolean isLoading() {
        return state == FormState.LOADING;
    }

    public boolean isOperational() {
        FormState curState = state;
        if(curState == null) return false;
        switch(curState) {
            case LOADING:
            case UI:     return true;
            case STATUS: return false;
            default: throw new UnsupportedOperationException(curState.name());
        }
    }

    public ErrorsControl getCtlErrors() {
        return ctlErrors;
    }

    // ================================
    // METHODS

    public void cancel() {        // TODO rename to reset
        FormState oldState = state;
        if(oldState != null) oldState.getFormPanel(this).setVisible(false);
        state = null;

        ctlErrors.clearErrors();
    }

    @Deprecated
    public void forward() {
        FormState oldState = state;
        FormState newState = null;
        if(oldState == null) {
            newState = FormState.UI;
        } else {
            oldState.getFormPanel(this).setVisible(false);
            switch(oldState) {
                case UI: newState = FormState.LOADING; break;
                case LOADING: newState = FormState.STATUS; break;
                case STATUS:
                default: throw new UnsupportedOperationException(oldState.name());
            }
        }
        state = newState;
        newState.getFormPanel(this).setVisible(true);

        ctlErrors.clearErrors();
    }

    @Deprecated
    public void back() {
        FormState oldState = state;
        FormState newState = null;
        if(oldState == null) {
            throw new UnsupportedOperationException(oldState.name());
        } else {
            oldState.getFormPanel(this).setVisible(false);
            switch(oldState) {
                case LOADING: newState = FormState.UI; break;
                case UI:
                case STATUS:
                default: throw new UnsupportedOperationException(oldState.name());
            }
        }
        state = newState;
        newState.getFormPanel(this).setVisible(true);

        ctlErrors.clearErrors();
    }

    public void toggleLoading() {
        FormState oldState = state;
        FormState newState = FormState.LOADING;
        if(oldState != null)
            oldState.getFormPanel(this).setVisible(false);
        state = newState;
        newState.getFormPanel(this).setVisible(true);

        ctlErrors.clearErrors();
    }

    public void toggleUI() {
        FormState oldState = state;
        FormState newState = FormState.UI;
        if(oldState != null)
            oldState.getFormPanel(this).setVisible(false);
        state = newState;
        newState.getFormPanel(this).setVisible(true);
    }

    public void toggleStatus(String msg) {
        FormState oldState = state;
        FormState newState = FormState.STATUS;
        if(oldState != null)
            oldState.getFormPanel(this).setVisible(false);

        // assert FormState.LOADING.equals(oldState);

        lbOperationFeedback.setText(msg);
        state = newState;
        newState.getFormPanel(this).setVisible(true);
    }

    public void init(FormState initialState) {
        this.cancel();
        initialState.toggleIitialState(this);
    }

    // ================================
    // LOW-LEVEL OVERRIDES

}
