package utils.frontend.client;

import com.google.gwt.user.client.ui.Panel;
import static utils.frontend.client.SimpleOperationTypeMachine.*;

public interface ISimpleFormOperation<G_Obj> {

    public boolean initOperationRequiresExistingObject();

    public String toHumanReadable();
    public String buttonTitle();

    public G_Obj getOperatedObject();

    public void initFormFieldsPanelControls(Panel formFieldsPanel);
    // public void initFormButtonsPanelControls(Panel formButtonsPanel); // TODO: in future version

    public SimpleFormStateMachine.FormState getFormInitialState();

    public void initOp(G_Obj obj, ConversationMarker _currentMarker);  // initOperationRequiresExistingObject()
    public void initOp(ConversationMarker _currentMarker);

    public void commitOperation(ErrorsControl errorsControl, ConversationMarker _currentMarker);


}
