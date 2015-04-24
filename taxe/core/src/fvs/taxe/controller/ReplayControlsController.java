package fvs.taxe.controller;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
import gameLogic.GameState;
import gameLogic.listeners.GameStateListener;
import gameLogic.listeners.ReplayToggleListener;
import gameLogic.listeners.TurnListener;
import gameLogic.player.Player;

public class ReplayControlsController {
	
	private Table controlTable;
	private Context context;
	private Stage controlStage;
	public boolean advancing = false;
	private ImageButton advance, nextTurn, play;
	private SelectBox<Integer> jump;
	
	private GameStateListener changeState = new GameStateListener() {
		@Override
		public void changed(GameState state) {
			if(state == GameState.ANIMATING || advancing) {
				//Set all controls which advance game to be disabled
				advance.setDisabled(true);
				nextTurn.setDisabled(true);
			} else {
				//Set all controls which advance game to be enabled
				advance.setDisabled(false);
				nextTurn.setDisabled(false);
			}
		}
		
	};
	
	private ReplayToggleListener toggle = new ReplayToggleListener() {
		@Override
		public void toggled(boolean replaying) {
			advancing = replaying;
			
			if(!replaying) {
				changeState.changed(context.getGameLogic().getState());
			}
		}
	};
	
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
		controlTable.setHeight(controlTable.getHeight() + 5.0f);
		controlTable.setWidth(controlTable.getWidth() + 10.0f);
		controlTable.setPosition(TaxeGame.WIDTH - SideBarController.CONTROLS_WIDTH - controlTable.getWidth(), 0);
		
		if(context.getTaxeGame().getScreen() instanceof ReplayScreen) {
			//Place on control stage if in replay so input cannot overlap with that being used in replay
			controlStage = new Stage(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT));
			controlStage.addActor(controlTable);
			context.getSoundController().addSettingsButton(controlStage, context.getSkin());
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

				context.getReplayManager().setGame(context.getGameLogic());
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
		advance = new ImageButton(context.getSkin(), "play");
		advance.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!((ImageButton) event.getListenerActor()).isDisabled()) {
					if(!context.getReplayManager().isEnd()) {
						context.getReplayManager().playSingle();
					} else {
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
		nextTurn = new ImageButton(context.getSkin(), "fastforward");
		nextTurn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!((ImageButton) event.getListenerActor()).isDisabled()) {
					boolean goAgain = true;
					int startTurn = context.getGameLogic().getPlayerManager().getTurnNumber();
					while(goAgain && context.getGameLogic().getPlayerManager().getTurnNumber() == startTurn) {
						if(!context.getReplayManager().isEnd()) {
							context.getReplayManager().playSingle();
						} else {
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

		//Move slowly through all clicks until stopped
		play = new ImageButton(context.getSkin(), "loop");
		play.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!context.getReplayManager().isEnd()) {
					context.getReplayManager().replayingToggle();
				} else {
					UnifiedDialog nomore = new UnifiedDialog("End of Replay", context.getSkin(), "redwin");
					nomore.text("You have reached the end of the replay");
					nomore.button("OK");
					nomore.show(controlStage);
				}
				
			}
		});
		addActor(play);
		context.getReplayManager().subscribeToggleListener(toggle);
		
		//Add option to Jump to a turn in the future
		jump = new SelectBox<Integer>(context.getSkin(), "replay");
		Array<Integer> turnNums = new Array<Integer>();
		for(int i = 0; i <= context.getReplayManager().getAvailableTurns(); i++) {
			turnNums.add(i + 1);
		}
		jump.setItems(turnNums);
		jump.setSelectedIndex(0);
		jump.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				@SuppressWarnings("unchecked")
				final int jumpTo = ((SelectBox<Integer>) actor).getSelected() - 1;
				//Test to do nothing if same turn
				if (jumpTo > context.getGameLogic().getPlayerManager().getTurnNumber()) {
					context.getGameLogic().getPlayerManager().subscribeTurnChanged(new TurnListener(){
						@Override
						public void changed() {
							if(context.getGameLogic().getPlayerManager().getTurnNumber() == jumpTo) {
								context.getReplayManager().replayingToggle();
								play.setDisabled(false);
								jump.setDisabled(false);
							}
						}
					});
					context.getReplayManager().replayingToggle();
					play.setDisabled(true);
					jump.setDisabled(true);
				} else if(jumpTo < context.getGameLogic().getPlayerManager().getTurnNumber()) {
					//Rewinding is hard, create a new replay window and when it is ready fast forward
					ArrayList<String> playerNames = new ArrayList<String>();
	                for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
	                	playerNames.add(player.getName());
	                }
	                context.getReplayManager().exitReplay();
					context.getReplayManager().setGame(context.getGameLogic());
					ReplayScreen newScreen = new ReplayScreen(context.getTaxeGame(), context.getReplayManager(), playerNames, context.getGameLogic().getTotalTurns());
	                context.getTaxeGame().setScreen(newScreen);
	                //Jump to correct turn
	                newScreen.jumpToTurn(jumpTo);
				}
			}
		});
		addActor(new Label("Jump to ", context.getSkin()));
		addActor(jump);
		
		//Add listener to update Jump each turn
		context.getGameLogic().getPlayerManager().subscribeTurnChanged(new TurnListener() {
			@Override
			public void changed() {
				if(!advancing) {
					jump.setSelectedIndex(jump.getSelectedIndex() + 1);
				}
			}
		});
		
		//Slider to set speed
		Slider speed = new Slider(0.1f, 2.0f, 0.1f, false, context.getSkin(), "settings");
		speed.setValue(context.getReplayManager().getClickInterval());
		speed.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				context.getReplayManager().setClickInterval(((Slider) actor).getValue());
			}
		});
		addActor(new Label("    Fast", context.getSkin()));
		addActor(speed);
		addActor(new Label("Slow", context.getSkin()));
		
		//Add listener to prevent advancing clicks whilst animation is happening
		context.getGameLogic().subscribeStateChanged(changeState);
	}
	
	public void disablePlay(boolean disabled) {
		play.setDisabled(disabled);
	}
	
	public void disableJump(boolean disabled) {
		jump.setDisabled(disabled);
	}
}
