package fvs.taxe.controller;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.ReplayScreen;
import fvs.taxe.TaxeGame;
import fvs.taxe.dialog.UnifiedDialog;
import gameLogic.Game;
import gameLogic.player.Player;

public class ReplayControlsController {
	
	private Table controlTable;
	private Context context;
	private static Game playingLogic;
	
	public ReplayControlsController(Context context) {
		this.context = context;
		
		controlTable = new Table(context.getSkin()).background("greysemitrans");
	}
	
	public void showReplayControls() {
		//Test type of screen
		if(context.getTaxeGame().getScreen() instanceof ReplayScreen) {
			//Add replay buttons
			addReplayScreenControls();
		} else {
			//Add start replay button
			addGameScreenControls();
		}
		
		//Display controls with correct sizing
		controlTable.setHeight(controlTable.getHeight() + 20.0f);
		controlTable.setWidth(controlTable.getWidth() + 20.0f);
		controlTable.setPosition(TaxeGame.WIDTH - SideBarController.CONTROLS_WIDTH - controlTable.getWidth(), 0);
		context.getStage().addActor(controlTable);
	}
	
	private void addActor(Actor actor) {
		//Custom add actor to ensure table sizes correctly
		controlTable.add(actor).pad(2.0f).align(Align.center);
		controlTable.setWidth(actor.getWidth() + 4.0f + controlTable.getWidth());
		if(actor.getHeight() + 4.0f > controlTable.getHeight()) {
			controlTable.setHeight(actor.getHeight() + 4.0f);
		}
	}
	
	//Add controls for use when not in replay mode
	//Don't use ReplayClickListener as we don't want this click to be captured
	private void addGameScreenControls() {
		TextButton replay = new TextButton("Start Replay", context.getSkin());
    	replay.addListener(new ClickListener() {
    		@SuppressWarnings("static-access")
			@Override
    		public void clicked(InputEvent event, float x, float y) {
    			ArrayList<String> playerNames = new ArrayList<String>();
                for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
                	playerNames.add(player.getName());
                }
                playingLogic = context.getGameLogic().getInstance();
                context.getGameLogic().dispose();
                context.getTaxeGame().setScreen(new ReplayScreen(context.getTaxeGame(), context.getReplayManager(), playerNames, context.getGameLogic().getTotalTurns()));
    		}
    	});
    	addActor(replay);
	}
	
	//Add controls to use whilst replay is taking place
	private void addReplayScreenControls() {
		//Exit replay mode
		TextButton exit = new TextButton("End Replay", context.getSkin());
		exit.addListener(new ClickListener() {
			@SuppressWarnings("static-access")
			@Override
			public void clicked(InputEvent event, float x, float y) {
				context.getGameLogic().dispose();
				context.getGameLogic().setInstance(playingLogic);
				context.getReplayManager().exitReplay();
				context.getTaxeGame().setScreen(context.getTaxeGame().gamescreen);
			}
		});
		addActor(exit);
		
		//Act the next click
		TextButton advance = new TextButton(">", context.getSkin());
		advance.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					context.getReplayManager().playSingle();
				} catch (IndexOutOfBoundsException e) {
					UnifiedDialog nomore = new UnifiedDialog("End of Replay", context.getSkin(), "redwin");
					nomore.text("You have reached the end of the replay");
					nomore.button("OK");
					nomore.show(context.getStage());
				}
			}
		});
		addActor(advance);
	}
	
	public void moveToTop() {
		controlTable.toFront();
	}

}
