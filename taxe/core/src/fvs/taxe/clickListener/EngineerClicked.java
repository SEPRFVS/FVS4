package fvs.taxe.clickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import fvs.taxe.controller.Context;
import fvs.taxe.dialog.DialogResourceEngineer;
import gameLogic.GameState;
import gameLogic.player.Player;
import gameLogic.resource.Engineer;

//Responsible for checking whether then engineer has been clicked
public class EngineerClicked extends ReplayClickListener {
    private Engineer engineer;
    private Context context;
    private boolean displayingMessage;

    public EngineerClicked(Context context, Engineer engineer, Actor actor) {
        super(context.getReplayManager(), actor);

        this.engineer = engineer;
        this.context = context;
        displayingMessage = false;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (context.getGameLogic().getState() == GameState.NORMAL) {
            // current player can't be passed in as it changes so find out current player at this instant
            Player currentPlayer = context.getGameLogic().getPlayerManager().getCurrentPlayer();

            //Creates a dialog and a listener for the result of it
            DialogButtonClicked listener = new DialogButtonClicked(context, currentPlayer, engineer);
            DialogResourceEngineer dia = new DialogResourceEngineer(engineer, context.getSkin(), context.getReplayManager());
            dia.show(context.getStage());
            dia.subscribeClick(listener);
        }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor trainActor) {
        //This is used for mouseover events for Engineer
        //This shows the message if there is not one currently being displayed
        if (!displayingMessage) {
            displayingMessage = true;
            if (context.getGameLogic().getState() == GameState.NORMAL) {
                context.getSideBarController().displayMessage("Your Engineer. Used to fix damaged connections.");


            }
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor trainActor) {
        //This is used for mouseover events for Engineer
        //This hides the message currently in the topBar if one is being displayed
        if (displayingMessage) {
            displayingMessage = false;
            if (context.getGameLogic().getState() == GameState.NORMAL) {
                //If the game state is normal then the topBar is cleared by passing it an empty string to display for 0 seconds
                context.getSideBarController().clearMessage();
            }
        }
    }
}
