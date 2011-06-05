package utils.frontend.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

public class ErrorsControl {
    // ================================
    // NON-STATIC STUFF
    final HTML lbErrors = new HTML(); // content set dynamically

    // ================================
    // CONSTRUCTORS
    public ErrorsControl() {
        super();
        lbErrors.setStylePrimaryName("error");
    }

    // ================================
    // GETTERS/SETTERS
    public HTML getLbErrors() {
        return lbErrors;
    }

    // ================================
    // METHODS
    public void addError(String str) { lbErrors.setHTML(str + "<br>" + lbErrors.getText()); }
    public void addError(Throwable caught) { addError(caught.getMessage() + "(" + caught.getClass().getName() + ")"); }
    public void clearErrors() { lbErrors.setText(""); }

    public void addToPanel(Panel container) {
        container.add(lbErrors);
    }

    // ================================
    // LOW-LEVEL OVERRIDES

}
