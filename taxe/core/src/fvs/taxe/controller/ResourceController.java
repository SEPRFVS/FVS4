package fvs.taxe.controller;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import fvs.taxe.TaxeGame;
import fvs.taxe.clickListener.*;
import gameLogic.listeners.PlayerChangedListener;
import gameLogic.player.Player;
import gameLogic.resource.*;

public class ResourceController {
    private Context context;
    private Group resourceButtons = new Group();

    public ResourceController(final Context context) {
        this.context = context;
        //Subscribes to the listener so that the resources are redrawn whenever the player changes.
        context.getGameLogic().getPlayerManager().subscribePlayerChanged(new PlayerChangedListener() {
            @Override
            public void changed() {
                drawPlayerResources(context.getGameLogic().getPlayerManager().getCurrentPlayer());
            }
        });
    }

    public void drawPlayerResources(Player player) {
        //This method draws the buttons representing the player's resources, alter this method if you want to change how resources are represented.
        float top = (float) TaxeGame.HEIGHT;
        float x = TaxeGame.WIDTH - SideBarController.CONTROLS_WIDTH + 10.0f;
        //The value of y is set based on how much space the header texts and goals have taken up (assumed that 3 goals are always present for a consistent interface)
        float y = top - 260.0f;
        y -= 50;

        //Clears the resource buttons so that the other player's resources are not displayed
        resourceButtons.remove();
        resourceButtons.clear();
        
        for (final Resource resource : player.getResources()) {
        	TextButton button = null;
            //This if statement is used to determine what type of resource is being drawn. This is necessary as each resource needs to have a different click listener assigned to its button.
            if (resource instanceof Train) {
                Train train = (Train) resource;

                // Don't show a button for trains that have been placed, trains placed are still part of the 7 total upgrades
                //If a train is not placed then its position is null so this is used to check
                if (train.getPosition() == null) {
                    //Creates a clickListener for the button and adds it to the list of buttons
                    TrainClicked listener = new TrainClicked(context, train);
                    button = new TextButton(resource.toString(), context.getSkin(), "unplaced-resource");
                    button.addListener(listener);
                }

            } else if (resource instanceof Obstacle) {
                //Creates a clickListener for the button and adds it to the list of buttons
                Obstacle obstacle = (Obstacle) resource;
                ObstacleClicked listener = new ObstacleClicked(context, obstacle);
                button = new TextButton("Obstacle", context.getSkin(), "unplaced-resource");
                button.addListener(listener);
            } else if (resource instanceof Skip) {
                //Creates a clickListener for the button and adds it to the list of buttons
                Skip skip = (Skip) resource;
                SkipClicked listener = new SkipClicked(context, skip);
                button = new TextButton("Skip", context.getSkin(), "unplaced-resource");
                button.addListener(listener);
            } else if (resource instanceof Engineer) {
                //Creates a clickListener for the button and adds it to the list of buttons
                Engineer engineer = (Engineer) resource;
                EngineerClicked listener = new EngineerClicked(context, engineer);
                button = new TextButton("Engineer", context.getSkin(), "unplaced-resource");
                button.addListener(listener);
            } else if (resource instanceof ConnectionModifier) {
                //Creates a clickListener for the button and adds it to the list of buttons
                ConnectionModifier connectionModifier = (ConnectionModifier) resource;
                ConnectionClicked listener = new ConnectionClicked(context, connectionModifier);
                button = new TextButton("Connection modifier", context.getSkin(), "unplaced-resource");
                button.addListener(listener);
            }
            
            if (button != null) {
            	button.setPosition(x, y);
            	button.setWidth(SideBarController.CONTROLS_WIDTH - 20.0f);
            	resourceButtons.addActor(button);
            	y -= 30;
            }

        }
        //Adds all generated buttons to the stage
        context.getStage().addActor(resourceButtons);
    }

}
