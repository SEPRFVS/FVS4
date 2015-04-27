package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.clickListener.TrainClicked;
import fvs.taxe.controller.Context;
import gameLogic.player.Player;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;

public class DialogStationMultitrain extends ReplayDialog {
    //This class is used to create a dialog for when there are multiple trains in one location
    private Context context;

    public DialogStationMultitrain(ArrayList<Train> trains, Skin skin, Context context) {

        //This constructor is called when there are multiple blocked trains sitting on top of each other
        super("Select Train", skin, "bluewin", context.getReplayManager());
        this.context = context;
        text("Choose which train you would like");

        //Generates the text string for each of the trains passed to the method and creates a button for them
        for (Train train : trains) {
            String destination = "";
            if (train.getFinalDestination() != null) {
                destination = " to " + train.getFinalDestination().getName();
            }


            button(train.getName() + destination + " (Player " + train.getPlayer().getPlayerNumber() + ")", trainString(train));
            getButtonTable().row();
        }

        button("Cancel", "CANCEL");
    }

    private String trainString(Train train) {
        return train.getName() + ":" + train.getPlayer().getPlayerNumber() + ":" + train.getPosition().getX()
                + ":" + train.getPosition().getY();
    }

    private Train getTrainFromString(String s) {
//        String[] parts = s.split(":");
//        int playerNumber = Integer.parseInt(parts[1]);

        for (Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
            for (Resource resource : player.getResources()) {
                if (!(resource instanceof Train))
                    continue;

                Train train = (Train) resource;

                if (train.getPosition() == null)
                    continue;

                String trainString = trainString(train);

                if (trainString.equals(s))
                    return train;
            }
        }

        throw new RuntimeException("Train couldn't be found");
    }

    @Override
    public void result(Object obj) {
        super.result(obj);

        if (obj == "CANCEL") {
            //If the user clicks cancel then it deletes the dialog
            this.remove();
        } else {
            Train train = getTrainFromString((String) obj);

            //Simulate click on the train
            TrainClicked clicker = new TrainClicked(context, train, null);
            //This is a small hack, by setting the value of the simulated x value to -1, we can use this to check whether or not
            //This dialog has been opened before. If this was not here then this dialog and trainClicked would get stuck in an endless loop!
            clicker.clicked(null, -1, 0);
        }
    }
}
