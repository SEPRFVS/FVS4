package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.RandomSingleton;
import gameLogic.listeners.GameStateListener;
import gameLogic.listeners.TurnListener;
import gameLogic.map.Map;
import gameLogic.replay.ReplayManager;

import java.awt.event.InputEvent;
import java.util.EventListener;


public class GameScreen extends ScreenAdapter {
    protected TaxeGame game;
    private Stage stage;
    private Texture mapTexture;
    protected Game gameLogic;
    private Skin skin;
    private Map map;
    private float timeAnimated = 0;
    public static final int ANIMATION_TIME = 2;
    private Tooltip tooltip;
    private Context context;
    private int actorId = 1000;

    private StationController stationController;
    private TopBarController topBarController;
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
        // magic, do not touch, every time an actor is added to the stage, modify its click listener
        // to one that records its click events
        stage = new Stage(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT)) {
            @Override
            public void addActor(final Actor actor) {
                actor.setName((String.valueOf(actorId)));

                for (final com.badlogic.gdx.scenes.scene2d.EventListener listener : actor.getListeners()) {
                    if (listener instanceof ReplayClickListener) {
                        // don't edit listeners which are of this type (they already record click events)
                        continue;
                    }

                    if (listener instanceof ClickListener) {
                        if (!actor.removeListener(listener)) {
                            System.out.println("OLD LISTENER NOT REMOVED");
                        }

                        if (listener.getClass() != ClickListener.class) {
                            // throw new RuntimeException("Subclass methods will be lost");
                        }

                        boolean added = actor.addListener(new ClickListener() {
                            @Override
                            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                                rm.addClick(actor.getName());
                                ((ClickListener) listener).clicked(event, x, y);
                            }
                        });

                        // only override a single click listener, we don't want to be storing each click twice
                        // on actors with multiple listeners
                        if (added) {
                            break;
                        }
                    }
                }

                // uniquely name each actor
                actorId++;
                super.addActor(actor);
            }
        };

        Gdx.input.setInputProcessor(stage);

        //Sets the skin
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        setRandomSeed(rm);

        //Initialises the game
        gameLogic = Game.getInstance();
        context = new Context(stage, skin, game, gameLogic);

        rm.setStage(stage);

        context.setReplayManager(rm);

        //Draw background
        mapTexture = new Texture(Gdx.files.internal("gamemap.png"));
        map = gameLogic.getMap();

        tooltip = new Tooltip(skin);
        stage.addActor(tooltip);

        //Initialises all of the controllers for the UI
        stationController = new StationController(context, tooltip);
        topBarController = new TopBarController(context);
        resourceController = new ResourceController(context);
        goalController = new GoalController(context);
        routeController = new RouteController(context);
        context.setRouteController(routeController);
        context.setTopBarController(topBarController);

        //Adds a listener that displays a flash message whenever the turn ends
        gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
            @Override
            public void changed() {
                //The game will not be set into the animating state for the first turn to prevent player 1 from gaining an inherent advantage by gaining an extra turn of movement.
                if (context.getGameLogic().getPlayerManager().getTurnNumber()!=1) {
                    gameLogic.setState(GameState.ANIMATING);
                    topBarController.displayFlashMessage("Time is passing...", Color.BLACK);
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
            gameLogic.dispose();
            game.setScreen(new ReplayScreen(game, context.getReplayManager()));
        }

        game.batch.begin();

        //Draws the map background
        game.batch.draw(mapTexture, 0, 0, TaxeGame.WIDTH, TaxeGame.HEIGHT);
        game.batch.end();

        topBarController.drawBackground();

        stationController.renderConnections(map.getConnections(), Color.GRAY);
        if (gameLogic.getState() == GameState.PLACING_TRAIN || gameLogic.getState() == GameState
                .ROUTING) {
            stationController.renderStationGoalHighlights();
            //This colours the start and end nodes of each goal to allow the player to easily see where they need to route
        }

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

        game.batch.begin();
        //If statement checks whether the turn is above 30, if it is then display 30 anyway
        game.fontSmall.draw(game.batch, "Turn " + ((gameLogic.getPlayerManager().getTurnNumber() + 1 < gameLogic.TOTAL_TURNS) ? gameLogic.getPlayerManager().getTurnNumber() + 1 : gameLogic.TOTAL_TURNS) + "/" + gameLogic.TOTAL_TURNS, (float) TaxeGame.WIDTH - 90.0f, 20.0f);
        game.batch.end();

        resourceController.drawHeaderText();
        goalController.drawHeaderText();
    }
    
    @Override
    public void resize(int width, int height) {
    	stage.getViewport().update(width, height);
    }

    @Override
    // Called when GameScreen becomes current screen of the game
    public void show() {
        //We only render this once a turn, this allows the buttons generated to be clickable.
        //Initially some of this functionality was in the draw() routine, but it was found that when the player clicked on a button a new one was rendered before the input could be handled
        //This is why the header texts and the buttons are rendered separately, to prevent these issues from occuring
        stationController.renderStations();
        topBarController.addEndTurnButton();
        goalController.showCurrentPlayerGoals();
        resourceController.drawPlayerResources(gameLogic.getPlayerManager().getCurrentPlayer());
    }


    @Override
    public void dispose() {
        mapTexture.dispose();
        stage.dispose();
    }

}