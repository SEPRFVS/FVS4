package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.resource.JunctionModifier;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceJunctionModifier extends UnifiedDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceJunctionModifier(JunctionModifier connectionModifier, Skin skin) {
        super(connectionModifier.toString(), skin, "bluewin");
        //Generates all the buttons that allow the user to interact with the dialog
        text("What do you want to do with this connection modifier?");
        button("Place a new junction", "PLACE");
        button("Remove an old junction", "REMOVE");
        button("Drop", "DROP");
        button("Cancel", "CLOSE");
    }

    private void clicked(Button button) {
        //Informs all listeners which button has been clicked
        for (ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        //Adds an external listener to the result of the dialog
        clickListeners.add(listener);
    }

    @Override
    protected void result(Object obj) {
        //Does things based on which button the user presses
        if (obj == "CLOSE") {
            this.remove();
        } else if (obj == "DROP") {
            clicked(Button.JUNCTION_DROP);
        } else if (obj == "PLACE") {
            clicked(Button.JUNCTION_PLACE);
        } else if (obj == "REMOVE") {
            clicked(Button.JUNCTION_REMOVE);
        }
    }
}
