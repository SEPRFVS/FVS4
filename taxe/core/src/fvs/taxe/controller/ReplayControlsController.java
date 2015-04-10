package fvs.taxe.controller;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fvs.taxe.ReplayScreen;
import fvs.taxe.TaxeGame;
import fvs.taxe.dialog.UnifiedDialog;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.listeners.GameStateListener;
import gameLogic.player.Player;

public class ReplayControlsController {
	
	private Table controlTable;
	private Context context;
	private Stage controlStage;
	
	public ReplayControlsController(Context context) {
		this.context = context;
		
		controlTable = new Table(context.getSkin()).background("greysemitrans");
	}
	
	public void showReplayControls() {
		//Remove old buttons and actors from stage
		context.getStage().getActors().removeValue(controlTable, true);
		controlTable.clear();
		controlTable.setHeight(0);
		controlTable.setWidth(0);
		
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
		
		if(context.getTaxeGame().getScreen() instanceof ReplayScreen) {
			//Place on control stage if in replay so input cannot overlap with that being used in replay
			controlStage = new Stage(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT));
			controlStage.addActor(controlTable);
			((ReplayScreen) context.getTaxeGame().getScreen()).setControlStage(controlStage);
		} else {
			context.getStage().addActor(controlTable);
		}
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
			@Override
    		public void clicked(InputEvent event, float x, float y) {
    			ArrayList<String> playerNames = new ArrayList<String>();
                for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
                	playerNames.add(player.getName());
                }
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
			@Override
			public void clicked(InputEvent event, float x, float y) {
				context.getReplayManager().exitReplay();
				context.getTaxeGame().setScreen(context.getTaxeGame().gamescreen);
			}
		});
		addActor(exit);
		
		//Act the next click
		final TextButton advance = new TextButton(">", context.getSkin());
		advance.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!((TextButton) event.getListenerActor()).isDisabled()) {
					try {
						context.getReplayManager().playSingle();
					} catch (IndexOutOfBoundsException e) {
						UnifiedDialog nomore = new UnifiedDialog("End of Replay", context.getSkin(), "redwin");
						nomore.text("You have reached the end of the replay");
						nomore.button("OK");
						nomore.show(controlStage);
					}
				}
			}
		});
		addActor(advance);
		
		//Jump through the rest of the turn
		final TextButton nextTurn = new TextButton(">>", context.getSkin());
		nextTurn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!((TextButton) event.getListenerActor()).isDisabled()) {
					boolean goAgain = true;
					int startTurn = context.getGameLogic().getPlayerManager().getTurnNumber();
					while(goAgain && context.getGameLogic().getPlayerManager().getTurnNumber() == startTurn) {
						try {
							context.getReplayManager().playSingle();
						} catch (IndexOutOfBoundsException e) {
							UnifiedDialog nomore = new UnifiedDialog("End of Replay", context.getSkin(), "redwin");
							nomore.text("You have reached the end of the replay");
							nomore.button("OK");
							nomore.show(controlStage);
							goAgain = false;
						}
					}
				}
			}
		});
		addActor(nextTurn);
		
		//Go to a specific turn
		final SelectBox<Integer> turns = new SelectBox<Integer>(context.getSkin());
		Array<Integer> turnNumbers = new Array<Integer>();
		for(int i = 0; i <= context.getReplayManager().getAvailableTurns(); i++) {
			turnNumbers.add(i + 1);
		}
		turns.setItems(turnNumbers);
		turns.setSelectedIndex(0);
		turns.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//Display a dialog alerting to the fact fast forward is happening and prevent naughty clicking
				UnifiedDialog ff = new UnifiedDialog("Please wait", context.getSkin(), "greenwin");
				ff.text("Fast forwarding");
				ff.show(controlStage);
				
				//Move forward to selected turn
				for(int i = context.getGameLogic().getPlayerManager().getTurnNumber(); i < turns.getSelected(); i++) {
					while(i == context.getGameLogic().getPlayerManager().getTurnNumber()) {
						context.getReplayManager().playSingle();
					}
					while(context.getGameLogic().getState() == GameState.ANIMATING) {
						//Holding loop to prevent advancing through animation
					}
				}
				
				//Update selection box
				@SuppressWarnings("unchecked")
				SelectBox<Integer> select = ((SelectBox<Integer>) actor);
				Array<Integer> turnNumbers = new Array<Integer>();
				for(int i = 0; i <= context.getReplayManager().getAvailableTurns(); i++) {
					turnNumbers.add(i + 1);
				}
				select.setItems(turnNumbers);
				select.setSelectedIndex(0);
				
				//Remove fast forward message
				ff.hide();
			}			
		});
		//addActor(new Label("Go to turn ", context.getSkin()));
		//addActor(turns);
		
		//Add listener to prevent advancing clicks whilst animation is happening
		context.getGameLogic().subscribeStateChanged(new GameStateListener() {
			@Override
			public void changed(GameState state) {
				if(state == GameState.ANIMATING) {
					//Set all controls which advance game to be disabled
					advance.setDisabled(true);
					nextTurn.setDisabled(true);
				} else {
					//Set all controls which advance game to be enabled
					advance.setDisabled(false);
					nextTurn.setDisabled(false);
				}
			}
			
		});
	}
}
