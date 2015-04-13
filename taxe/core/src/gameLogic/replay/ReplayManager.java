package gameLogic.replay;

import Util.Tuple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.dialog.ReplayDialog;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.listeners.ReplayToggleListener;

import java.util.ArrayList;
import java.util.List;

public class ReplayManager {
    private int playPosition = 0;
    private boolean replayingClick = false;
    private boolean replaying = false;
    // time in ms between clicks when replaying automatically
    private float clickInterval = 0.5f;
    private float timeSinceClick = 0;
    private long seed;
    private Stage stage;
    private List<Tuple<ReplayType, String>> clicks = new ArrayList<Tuple<ReplayType, String>>();
    private int availableTurns = 0;
    private Game game;
    private List<ReplayToggleListener> toggleListeners = new ArrayList<ReplayToggleListener>();

    public void setGame(Game game) {
        this.game = game;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    private void addClick(ReplayType type, String s) {
        if (replayingClick) return;

        clicks.add(new Tuple<ReplayType, String>(type, s));
    }

    public void addClick(String actorId) {
        if (replayingClick) return;

        System.out.println("Click on "+ actorId +" added");
        addClick(ReplayType.ACTOR_CLICK, actorId);
    }

    public void addDialogClick(String buttonId) {
        System.out.println("Click on "+ buttonId +" dia button added");
        addClick(ReplayType.DIALOG_BUTTON_CLICK, buttonId);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void replayingToggle() {
        replaying = !replaying;
        
        for(ReplayToggleListener listener : toggleListeners) {
        	listener.toggled(replaying);
        }
    }
    
    public void subscribeToggleListener(ReplayToggleListener listener) {
    	toggleListeners.add(listener);
    }
    
    public void unsubscribeToggleListener(ReplayToggleListener listener) {
    	toggleListeners.remove(listener);
    }

    /**
     * this method is called by ReplayScreen on each frame render
     */
    public void frame() {
        if (!replaying) {
            return;
        } else if(playPosition >= clicks.size()) {
        	replayingToggle();
        	return;
        }

        timeSinceClick += Gdx.graphics.getDeltaTime();

        if (timeSinceClick >= clickInterval) {
            playSingle();
            timeSinceClick = 0;
        }
    }

    public void playSingle() {
        if (game.getState() == GameState.ANIMATING) {
            System.out.println("Replay click blocked - game is in animating state.");
            return;
        }

        replayingClick = true;

        if (playPosition >= clicks.size()) {
            System.out.println("Played all clicks");
            return;
        }

        Tuple<ReplayType, String> click = clicks.get(playPosition);

        System.out.println("replayingClick single..." + String.valueOf(playPosition) + ", actor: " + click.getSecond());


        switch (click.getFirst()) {
            case ACTOR_CLICK:
                clickActorInStage(click.getSecond());
                break;
            case DIALOG_BUTTON_CLICK:
                clickDialogButton(click.getSecond());
                break;
        }

        playPosition++;
        replayingClick = false;
    }

    private void clickActorInStage(String name) {
        for(Actor actor : stage.getActors()) {
            // if an actor has no name, then it isn't one that we are interested in replaying it's clicked event
            // e.g. tooltip
            if (actor.getName() == null) continue;
            if (!actor.getName().equals(name)) continue;

            System.out.println("Actor found in stage");

            for (EventListener listener : actor.getListeners()) {
                if (listener instanceof ClickListener) {
                    System.out.println("Click listener found in actor");

                    ((ClickListener) listener).clicked(null, 0, 0);
                }
            }

            return;
        }

        throw new RuntimeException("Actor " + name + " not found");
    }

    private void clickDialogButton(String id) {
        for(Actor actor : stage.getActors()) {
            if (actor instanceof ReplayDialog) {
                System.out.println("found dialog, now clicking..");

                ReplayDialog dialog = (ReplayDialog) actor;
                dialog.result(id);
                dialog.remove();
            }
        }
    }
    
    public void exitReplay() {
    	//Reset variables to pre-replay state
    	playPosition = 0;
    	toggleListeners.clear();
    }
    
    public void setAvailableTurns(int turnNumber) {
    	//Test to prevent updating whilst replaying as listener will be active
    	if(turnNumber > availableTurns) {
    		availableTurns = turnNumber;
    	}
    }
    
    public int getAvailableTurns() {
    	return availableTurns;
    }
}