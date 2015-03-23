package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import fvs.taxe.TaxeGame;
import fvs.taxe.clickListener.GoalClickListener;
import gameLogic.player.Player;
import gameLogic.listeners.PlayerChangedListener;
import gameLogic.player.PlayerManager;
import gameLogic.goal.Goal;

import java.text.DecimalFormat;

public class GoalController {
    //This class is in control of drawing all the goals
    private Context context;
    private Group goalButtons = new Group();
    private Color[] colours = new Color[3];

    public GoalController(Context context) {
        this.context = context;
        //Makes the system redraw the currentGoals whenever the player changes.
        context.getGameLogic().getPlayerManager()
                .subscribePlayerChanged(new PlayerChangedListener() {
					@Override
					public void changed() {
						showCurrentPlayerGoals();
					}
				});
    }

    public void setColours(Color[] colours) {
        //This method sets the button colours to be whatever is passed in the parameters
        this.colours = colours;
        //then redraws the current player goals with the new colours
        showCurrentPlayerGoals();
    }

    public void showCurrentPlayerGoals() {
        //This method displays the player's current goals
        //First the current goals are cleared so that the other player's goals are not displayed too.
        goalButtons.remove();
        goalButtons.clear();

        PlayerManager pm = context.getGameLogic().getPlayerManager();
        Player currentPlayer = pm.getCurrentPlayer();

        float top = (float) TaxeGame.HEIGHT;
        float x = TaxeGame.WIDTH - SideBarController.CONTROLS_WIDTH + 10.0f;
        //This value is set by subtracting the total height of the player header and the goal header, change this if you want to adjust the position of the goals or other elements in the GUI
        float y = top - 110.0f;

        int index = 0;

        for (Goal goal : currentPlayer.getGoals()) {
            //Necessary to check whether the goals are complete as completed goals are not removed from the player's list of goals, without this check complete goals would also be displayed.
            if (!goal.getComplete()) {

                y -= 40;
                TextButton button = new TextButton(
                        goal.baseGoalString() + "\n" + goal.bonusString(), context.getSkin());
                button.getLabel().setAlignment(Align.left);
                //The goal buttons are scaled so that they do not overlap nodes on the map, this was found to be necessary after changing the way goals were displayed
                float scaleFactor = 0.8f;
                button.getLabel().setFontScale(scaleFactor, scaleFactor);
                button.setWidth(SideBarController.CONTROLS_WIDTH - 20.0f);
                button.setHeight(scaleFactor * button.getHeight());

                //Adds the listener to the button so that it will inform the correct parts of the system
                GoalClickListener listener = new GoalClickListener(context, goal);
                button.setPosition(x, y);

                if (colours[index++] != null) {
                    //Sets the colour based on the values in the array. If the train is routing then these colours will match nodes on the map, otherwise they are all grey.
                    button.setColor(colours[index - 1]);
                }
                button.addListener(listener);
                goalButtons.addActor(button);
            }
        }
        context.getStage().addActor(goalButtons);
    }
}
