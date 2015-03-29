package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.replay.ReplayManager;
import gameLogic.resource.Engineer;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceEngineer extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceEngineer(Engineer engineer, Skin skin, ReplayManager replayManager) {
        super(engineer.toString(), skin, "bluewin", replayManager);
        //Generates all the buttons that allow the user to interact with the dialog
        text("What do you want to do with this engineer?");
        button("Repair a blocked connection", "PLACE");
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
            clicked(Button.ENGINEER_DROP);
        } else if (obj == "PLACE") {
            clicked(Button.ENGINEER_USE);
        }
    }
}
