package fvs.taxe.dialog;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.replay.ReplayManager;
import gameLogic.resource.ConnectionModifier;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceConnectionModifier extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceConnectionModifier(ConnectionModifier connectionModifier, Skin skin, ReplayManager rm) {
        super(connectionModifier.toString(), skin, "bluewin", rm);
        //Generates all the buttons that allow the user to interact with the dialog
        text("What do you want to do with this connection modifier?");
        button("Place a new connection", "PLACE");
        button("Remove an old connection", "REMOVE");
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
	public void result(Object obj) {
    	super.result(obj);
        //Does things based on which button the user presses
        if (obj == "CLOSE") {
            this.remove();
        } else if (obj == "DROP") {
            clicked(Button.CONNECTION_DROP);
        } else if (obj == "PLACE") {
            clicked(Button.CONNECTION_PLACE);
        } else if (obj == "REMOVE") {
            clicked(Button.CONNECTION_REMOVE);
        }
    }
}
