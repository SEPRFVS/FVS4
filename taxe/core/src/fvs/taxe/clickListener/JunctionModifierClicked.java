package fvs.taxe.clickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fvs.taxe.controller.Context;
import fvs.taxe.dialog.DialogResourceJunctionModifier;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.player.Player;
import gameLogic.resource.JunctionModifier;

public class JunctionModifierClicked extends ClickListener {
    Context context;
    JunctionModifier junctionModifier;
    private boolean displayingMessage;

    public JunctionModifierClicked(Context context, JunctionModifier junctionModifier) {
        this.context = context;
        this.junctionModifier = junctionModifier;
        displayingMessage = false;
    }


    public void clicked(InputEvent event, float x, float y) {
        //When skip is clicked it checks whether the game is in the normal state
        if (Game.getInstance().getState() == GameState.NORMAL) {

            // current player can't be passed in as it changes so find out current player at this instant
            Player currentPlayer = Game.getInstance().getPlayerManager().getCurrentPlayer();

            //Creates a dialog when connection modifier is clicked allowing the user to select what they want to do with the resource
            DialogButtonClicked listener = new DialogButtonClicked(context, currentPlayer, junctionModifier);
            DialogResourceJunctionModifier dia = new DialogResourceJunctionModifier(junctionModifier, context.getSkin());
            dia.show(context.getStage());
            dia.subscribeClick(listener);
        }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor trainActor) {
        //This is used for mouseover events for Connection modifier
        //This shows the message if there is not one currently being displayed
        if (!displayingMessage) {
            displayingMessage = true;
            if (Game.getInstance().getState() == GameState.NORMAL) {
                context.getSideBarController().displayMessage("Add or remove a junction on the map.");


            }
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor trainActor) {
        //This is used for mouseover events for Skips
        //This hides the message currently in the topBar if one is being displayed
        if (displayingMessage) {
            displayingMessage = false;
            if (Game.getInstance().getState() == GameState.NORMAL) {
                //If the game state is normal then the topBar is cleared by passing it an empty string to display for 0 seconds
                context.getSideBarController().clearMessage();
            }
        }
    }
}
