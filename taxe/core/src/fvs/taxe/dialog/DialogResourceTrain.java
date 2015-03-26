package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.replay.ReplayManager;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;

public class DialogResourceTrain extends ReplayDialog {
    private List<ResourceDialogClickListener> clickListeners = new ArrayList<ResourceDialogClickListener>();

    public DialogResourceTrain(Train train, Skin skin, boolean trainPlaced, ReplayManager replayManager) {
        super(train.toString(), skin, replayManager);
        text("What do you want to do with this train?");

        //Generates the buttons required to allow the user to interact with the dialog
        if (!trainPlaced) {
            //If the train is not placed, generate button allowing placement
            button("Place at a station", "PLACE");

        } else if (!train.isMoving()) {
            //If the train is not moving then generate button to specify a route
            button("Choose a route", "ROUTE");

        } else if (train.getRoute() != null) {
            //If the train has a route then generate button to change the route
            button("Change route", "CHANGE_ROUTE");

            //Generate button to view the route
            button("View Route", "VIEWROUTE");
        }

        button("Drop", "DROP");

        button("Cancel", "CLOSE");
    }

    @Override
    public Dialog show(Stage stage) {
        show(stage, null);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    @Override
    public void hide() {
        hide(null);
    }

    private void clicked(Button button) {
        for (ResourceDialogClickListener listener : clickListeners) {
            listener.clicked(button);
        }
    }

    public void subscribeClick(ResourceDialogClickListener listener) {
        clickListeners.add(listener);
    }

    @Override
    protected void result(Object obj) {
        super.result(obj);

        if (obj == "CLOSE") {
            this.remove();
        } else if (obj == "DROP") {
            clicked(Button.TRAIN_DROP);
        } else if (obj == "PLACE") {
            clicked(Button.TRAIN_PLACE);
        } else if (obj == "ROUTE") {
            clicked(Button.TRAIN_ROUTE);
        } else if (obj == "VIEWROUTE") {
            clicked(Button.VIEW_ROUTE);
        } else if (obj == "CHANGE_ROUTE") {
            clicked(Button.TRAIN_CHANGE_ROUTE);
        }
    }
}
