package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fvs.taxe.clickListener.ReplayClickListener;
import fvs.taxe.controller.*;
import fvs.taxe.dialog.DialogEndGame;
import fvs.taxe.dialog.DialogNews;
import fvs.taxe.dialog.DialogSettings;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.RandomSingleton;
import gameLogic.listeners.GameStateListener;
import gameLogic.listeners.TurnListener;
import gameLogic.map.Connection;
import gameLogic.map.Map;
import gameLogic.player.Player;
import gameLogic.replay.ReplayManager;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.EventListener;


public class GameScreen extends ScreenAdapter {
    protected TaxeGame game;
    private StageNamedActor stage;
    private Texture mapTexture;
    protected Game gameLogic;
    private Skin skin;
    private Map map;
    private float timeAnimated = 0;
    public static final int ANIMATION_TIME = 2;
    private Tooltip tooltip;
    private Context context;

    private StationController stationController;
    private SideBarController sideBarController;
    private ResourceController resourceController;
    private GoalController goalController;
    private RouteController routeController;

    public GameScreen() {
    }

    public GameScreen(TaxeGame game) {
        this.game = game;

        init(new ReplayManager());
    }

    protected void init(final ReplayManager rm) {
        stage = new StageNamedActor(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT));

        Gdx.input.setInputProcessor(stage);

        //Sets the skin
        skin = game.skin;

        setRandomSeed(rm);

        //Initialises the game
        gameLogic = Game.getInstance();
        context = new Context(stage, skin, game, gameLogic);

        rm.setStage(stage);

        context.setReplayManager(rm);

        //Draw background
        mapTexture = new Texture(Gdx.files.internal("gamemap.jpg"));
        map = gameLogic.getMap();

        tooltip = new Tooltip(context);
        stage.addActor(tooltip);

        //Initialises all of the controllers for the UI
        stationController = new StationController(context, tooltip);
        sideBarController = new SideBarController(context);
        resourceController = new ResourceController(context);
        goalController = new GoalController(context);
        routeController = new RouteController(context);
        context.setRouteController(routeController);
        context.setSideBarController(sideBarController);
        context.setNotificationController(new NotificationController(context));

        //Adds a listener that displays a flash message whenever the turn ends
        gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
            @Override
            public void changed() {
                //The game will not be set into the animating state for the first turn to prevent player 1 from gaining an inherent advantage by gaining an extra turn of movement.
                if (context.getGameLogic().getPlayerManager().getTurnNumber()!=1) {
                    gameLogic.setState(GameState.ANIMATING);
                    sideBarController.displayFlashMessage("Time is passing...");
                }
            }
        });

        //Adds a listener that checks certain conditions at the end of every turn
        gameLogic.subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                if ((gameLogic.getPlayerManager().getTurnNumber() == gameLogic.TOTAL_TURNS || gameLogic.getPlayerManager().getCurrentPlayer().getScore() >= gameLogic.MAX_POINTS) && state == GameState.NORMAL) {
                    //If the game should end due to the turn number or points total then the appropriate dialog is displayed
                    DialogEndGame dia = new DialogEndGame(GameScreen.this.game, gameLogic.getPlayerManager(), skin);
                    dia.show(stage);
                } else if (gameLogic.getState() == GameState.ROUTING || gameLogic.getState() == GameState.PLACING_TRAIN) {
                    //If the player is routing or place a train then the goals and nodes are colour coded
                    goalController.setColours(StationController.colours);
                } else if (gameLogic.getState() == GameState.NORMAL) {
                    //If the game state is normal then the goal colour are reset to grey
                    goalController.setColours(new Color[3]);
                }
            }
        });
        
        //Add a listener to determine if a connection has become blocked this turn and play sounds
        gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
        	@Override
        	public void changed() {
        		for (Connection connection : gameLogic.getMap().getConnections()) {
        			if(connection.isBlocked() && connection.getTurnsBlocked() == 5) {
        				context.getSoundController().playSound("obstacle");
        				context.getNotificationController().showObstacleMessage(connection);
        			}
        		}
        	}
        });
        
        //Add a listener to determine if the news should be shown after routing for that turn
        gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
        	@Override
        	public void changed() {
        		if(gameLogic.getPlayerManager().getTurnNumber() % DialogNews.SHOW_EVERY == 0 || gameLogic.getPlayerManager().getTurnNumber() == context.getGameLogic().TOTAL_TURNS) {
        			gameLogic.subscribeStateChanged(new GameStateListener() {
        				@Override
        				public void changed(GameState state) {
        					if(state == GameState.NORMAL){
        						new DialogNews(context, context.getReplayManager()).removeListenerOnExit(this, context).show(stage);
        					}
        				}
        			});
        		}
        	}
        });
    }

    protected void setRandomSeed(ReplayManager rm) {
        // store the seed in replay manager so same seed can be used in future
        long seed = System.currentTimeMillis();
        rm.setSeed(seed);
        RandomSingleton.setFromSeed(seed);
    }

    // called every frame
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            context.getReplayManager().playSingle();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ArrayList<String> playerNames = new ArrayList<String>();
            for(Player player : gameLogic.getPlayerManager().getAllPlayers()) {
            	playerNames.add(player.getName());
            }
            gameLogic.dispose();
            game.setScreen(new ReplayScreen(game, context.getReplayManager(), playerNames));
        }

        game.batch.begin();

        //Draws the map background
        game.batch.draw(mapTexture, 0, 0, TaxeGame.WIDTH - SideBarController.CONTROLS_WIDTH, TaxeGame.HEIGHT);
        game.batch.end();

        sideBarController.drawBackground();

        stationController.renderConnections(map.getConnections(), Color.GRAY);
        if (gameLogic.getState() == GameState.PLACING_TRAIN || gameLogic.getState() == GameState
                .ROUTING) {
            stationController.renderStationGoalHighlights();
            //This colours the start and end nodes of each goal to allow the player to easily see where they need to route
        }

        //Draw station acronyms
        stationController.displayStationAcronyms();
        
        //Draw routing
        if (gameLogic.getState() == GameState.ROUTING) {
            routeController.drawRoute(Color.BLACK);

        } else
            //Draw train moving
            if (gameLogic.getState() == GameState.ANIMATING) {
                timeAnimated += delta;
                if (timeAnimated >= ANIMATION_TIME) {
                    gameLogic.setState(GameState.NORMAL);
                    timeAnimated = 0;
                }
            }

        //Draw the number of trains at each station
        if (gameLogic.getState() == GameState.NORMAL || gameLogic.getState() == GameState.PLACING_TRAIN) {
            stationController.displayNumberOfTrainsAtStations();
        }

        //Causes all the actors to perform their actions (i.e trains to move)
        stage.act(Gdx.graphics.getDeltaTime());

        stage.draw();

        sideBarController.drawContent();
    }
    
    @Override
    public void resize(int width, int height) {
    	stage.getViewport().update(width, height);
    }

    @Override
    // Called when GameScreen becomes current screen of the game
    public void show() {
    	if(gameLogic.getPlayerManager().getCurrentPlayer().getName().equals("")) {
    		if(this instanceof ReplayScreen) {
    			for (int i = 0; i < gameLogic.getPlayerManager().getAllPlayers().size(); i++) {
    				gameLogic.getPlayerManager().getAllPlayers().get(i).setName(((ReplayScreen) this).playerNames.get(i));
    			}
    		} else {
    			game.setScreen(new SetupScreen(game, context));
    			return;
    		}
    	}
    	
    	Gdx.input.setInputProcessor(stage);
        //We only render this once a turn, this allows the buttons generated to be clickable.
        //Initially some of this functionality was in the draw() routine, but it was found that when the player clicked on a button a new one was rendered before the input could be handled
        //This is why the header texts and the buttons are rendered separately, to prevent these issues from occuring
        stationController.renderStations();
        sideBarController.addEndTurnButton();
        goalController.showCurrentPlayerGoals();
        resourceController.drawPlayerResources(gameLogic.getPlayerManager().getCurrentPlayer());
        
        //Load background music
        game.soundController.addSettingsButton(stage, skin);
        game.soundController.playBackgroundMusic();
        
        if(gameLogic.getPlayerManager().getTurnNumber() == 0) {
            //Show news at start of game only
            new DialogNews(context, context.getReplayManager()).show(stage);
        }
    }


    @Override
    public void dispose() {
        mapTexture.dispose();
        game.soundController.stopBackgroundMusic();
        stage.dispose();
    }

}