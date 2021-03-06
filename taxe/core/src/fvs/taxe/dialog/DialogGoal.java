package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.goal.Goal;
import gameLogic.replay.ReplayManager;

import java.util.ArrayList;
import java.util.List;

public class DialogGoal extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogGoal(Goal goal, Skin skin, ReplayManager replayManager) {
        //Generates a dialog allowing the player to select what they want to do with the goal
        super(goal.toString(), skin, "bluewin", replayManager);

        text("What do you want to do with this goal?");

        button("Drop", "DROP");
        button("Cancel", "CLOSE");
    }

    private void clicked(Button button) {
        //Informs all listeners that the dialog has been pressed, and which button has been pressed
        for (ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        //Adds listeners to the dialog, which want to know which button the user pressed
        clickListeners.add(listener);
    }

    @Override
    public void result(Object obj) {
        super.result(obj);

        //Does things based on which button was pressed
        if (obj == "CLOSE") {
            //Closes the dialog if close was pressed
            this.remove();

        } else if (obj == "DROP") {
            //Removes the goal if the drop button is pressed
            clicked(Button.GOAL_DROP);
        }
    }
}
