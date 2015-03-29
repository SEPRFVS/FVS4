package fvs.taxe.dialog;

import com.badlogic.gdx.Gdx;
import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import fvs.taxe.controller.Context;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceSkipped extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceSkipped(Context context) {
        super("Skip", context.getSkin(), "bluewin", context.getReplayManager());

        text("What do you want to do with this resource?");
        //Generates all the buttons required to allow the user to interact with the dialog
        button("Use", "USE");
        button("Drop", "DROP");
        button("Cancel", "CLOSE");
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        //Adds listeners to the result of the dialog
        clickListeners.add(listener);
    }

    private void clicked(Button button) {
        //Informs all listeners what the result of the dialog is
        for (ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }


    @Override
    public void result(Object obj) {
        super.result(obj);

        //Calls the clicked routine and passes it the button that the user clicked
        if (obj == "EXIT") {
            Gdx.app.exit();
        } else if (obj == "DROP") {
            clicked(Button.SKIP_DROP);
        } else if (obj == "USE") {
            clicked(Button.SKIP_RESOURCE);
        }
    }

}
