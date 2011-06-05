package utils.frontend.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;


import java.util.HashMap;

public class SimpleOperationTypeMachine<G_Obj> {
    public static class ConversationMarker {
        private final Integer conversationID;
        private final ConversationMarkerGenerator issuer;

        private ConversationMarker(Integer _conversationID, ConversationMarkerGenerator _issuer) {
            conversationID = _conversationID;
            issuer = _issuer;
        }

        public boolean isCurrent() { return issuer.isCurrent(this); }
    }
    private static class ConversationMarkerGenerator {
        private volatile Integer conversationID = 0;
        private volatile ConversationMarker currentMarker;
        public void next() {
            currentMarker = new ConversationMarker(++conversationID, this);
        }
        public boolean isCurrent(ConversationMarker marker) {
            if(marker == null) return false;
            return marker.conversationID.equals(this.conversationID);
        }
        public ConversationMarker getCurrentMarker() {
            return currentMarker;
        }
    }

    // ================================
    // NON-STATIC STUFF

    private final Panel opButtonsPanel;
    private final SimpleFormStateMachine machSimpleFormState;
    private volatile Integer currentOperationID; // javascript is threadsafe (single-threaded)
    private final HashMap<Integer, ISimpleFormOperation<G_Obj>> operations = new HashMap<Integer, ISimpleFormOperation<G_Obj>>();
    private final HashMap<Integer, Panel> operationsPanels = new HashMap<Integer, Panel>();

    private final ConversationMarkerGenerator conversationMarkerGenerator = new ConversationMarkerGenerator();

    // ================================
    // CONSTRUCTORS
    public SimpleOperationTypeMachine(SimpleFormStateMachine _machSimpleFormState, Panel _opButtonsPanel) {
        machSimpleFormState = _machSimpleFormState;
        opButtonsPanel = _opButtonsPanel;
    }

    // ================================
    // GETTERS/SETTERS

    public ISimpleFormOperation<G_Obj> getCurrentOperation() {
        return operations.get(currentOperationID);
    }

    public Panel getCurrentOperationPanel() {
        return operationsPanels.get(currentOperationID);
    }

    // ================================
    // METHODS
    public void addOperation(final ISimpleFormOperation<G_Obj> op) {
        final SimpleOperationTypeMachine super_this = this;
        final Integer op_id = operations.size();

        operations.put(op_id, op);
        SimplePanel fieldsPan = new SimplePanel();
        operationsPanels.put(op_id, fieldsPan);
        fieldsPan.setVisible(false);
        op.initFormFieldsPanelControls(fieldsPan);
        machSimpleFormState.getPanFormFields().add(fieldsPan);

        Button opButton = new Button(op.buttonTitle());
        opButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    super_this.initState(op_id, op.initOperationRequiresExistingObject() ? op.getOperatedObject() : null);
                }
            });
        opButtonsPanel.add(opButton);
    }

    public void setOperationVisible(boolean visibility) {
        ISimpleFormOperation<G_Obj> curOp = this.getCurrentOperation();

        if(curOp == null) return;
        getCurrentOperationPanel().setVisible(visibility);
    }

    public void initState(Integer op_id, G_Obj perhaps_obj) {
        this.setOperationVisible(false);

        if(op_id == null) {
            assert perhaps_obj == null;
            return;
        }

        ISimpleFormOperation<G_Obj> newOp = operations.get(op_id);
        assert newOp != null;

        conversationMarkerGenerator.next();

        machSimpleFormState.init(newOp.getFormInitialState());

        boolean requiresObj = newOp.initOperationRequiresExistingObject();

        if(requiresObj != (perhaps_obj != null)) {
            this.machSimpleFormState.toggleStatus("Operation \"" + newOp.toHumanReadable() + "\" not possible: target not ready.");
        } else {
            if(requiresObj)
                 newOp.initOp(perhaps_obj, conversationMarkerGenerator.getCurrentMarker());
            else newOp.initOp(conversationMarkerGenerator.getCurrentMarker());
        }


        this.currentOperationID = op_id;
        this.setOperationVisible(true);
    }

    public void commitOperation() {
        ISimpleFormOperation<G_Obj> curOp = this.getCurrentOperation();
        assert curOp != null;

        machSimpleFormState.toggleLoading();

        curOp.commitOperation(machSimpleFormState.getCtlErrors(), conversationMarkerGenerator.getCurrentMarker());
    }

    // ================================
    // LOW-LEVEL OVERRIDES

}
