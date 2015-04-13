package fvs.taxe;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import gameLogic.RandomSingleton;
import gameLogic.listeners.TurnListener;
import gameLogic.replay.ReplayManager;

public class ReplayScreen extends GameScreen {
    private ReplayManager replayManager;
    protected ArrayList<String> playerNames;
    protected int totalTurns;
    private Stage controlStage;

    public ReplayScreen(TaxeGame game, ReplayManager replayManager, ArrayList<String> playerNames, int totalTurns) {
        this.replayManager = replayManager;
        this.game = game;
        this.playerNames = playerNames;
        this.totalTurns = totalTurns;

        init(replayManager);

        //ReplayController replayController = new ReplayController(context, replayManager);
    }

    @Override
    protected void setRandomSeed(ReplayManager rm) {
        RandomSingleton.setFromSeed(rm.getSeed());
    }
    
    @Override
    public void show() {
    	for (int i = 0; i < gameLogic.getPlayerManager().getAllPlayers().size(); i++) {
			gameLogic.getPlayerManager().getAllPlayers().get(i).setName(((ReplayScreen) this).playerNames.get(i));
		}
    	gameLogic.setTotalTurns(((ReplayScreen) this).totalTurns);
    	super.show();
    	
    	if(controlStage != null) {
    		Gdx.input.setInputProcessor(controlStage);
    	}
    }
    
    @Override
    public void render(float delta) {
    	super.render(delta);

        replayManager.frame();
    	
    	if(controlStage != null) {
    		controlStage.act();
    		controlStage.draw();
    	}
    }
    
    public void setControlStage(Stage controlStage) {
    	this.controlStage = controlStage;
    }
    
    public void jumpToTurn(final int turn) {
    	//Don't do anything to get to turn 1
    	if(turn == 0) {
    		return;
    	}
    	
    	context.getGameLogic().getPlayerManager().subscribeTurnChanged(new TurnListener(){
			@Override
			public void changed() {
				if(context.getGameLogic().getPlayerManager().getTurnNumber() == turn) {
					context.getReplayManager().replayingToggle();
					context.getReplayControlsController().disablePlay(false);
					context.getReplayControlsController().disableJump(false);
				}
			}
		});
		context.getReplayManager().replayingToggle();
		context.getReplayControlsController().disablePlay(true);
		context.getReplayControlsController().disableJump(true);
    }
}
