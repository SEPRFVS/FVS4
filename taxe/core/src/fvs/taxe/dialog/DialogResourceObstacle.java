package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.replay.ReplayManager;
import gameLogic.resource.Obstacle;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceObstacle extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceObstacle(Obstacle obstacle, Skin skin, ReplayManager replayManager) {
        super(obstacle.toString(), skin, "bluewin", replayManager);

        //Generates all the buttons that allow the user to interact with the dialog
        text("What do you want to do with this obstacle?");
        button("Place on a connection", "PLACE");
        button("Drop", "DROP");
        button("Cancel", "CLOSE");
    }

    private void clicked(Button button) {
        //Informs all listeners which button has been pressed
        for (ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        //Adds listener to the dialog
        clickListeners.add(listener);
    }

    @Override
    public void result(Object obj) {
        super.result(obj);

        //Tells the clicked routine which button has been pressed
        if (obj == "CLOSE") {
            this.remove();
        } else if (obj == "DROP") {
            clicked(Button.OBSTACLE_DROP);
        } else if (obj == "PLACE") {
            clicked(Button.OBSTACLE_USE);
        }
    }
}
