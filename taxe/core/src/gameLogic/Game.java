package gameLogic;

import gameLogic.listeners.GameStateListener;
import gameLogic.listeners.TurnListener;
import gameLogic.goal.GoalManager;
import gameLogic.map.Map;
import gameLogic.player.Player;
import gameLogic.player.PlayerManager;
import gameLogic.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    //This is sort of a super-class that can be accessed throughout the system as many of its methods are static
    //This is a useful tool to exploit to make implementing certain features easier
    private static Game instance;
    private PlayerManager playerManager;
    private GoalManager goalManager;
    private ResourceManager resourceManager;
    private Map map;
    private GameState state;
    private List<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();

    //Number of players, not sure how much impact this has on the game at the moment but if you wanted to add more players you would use this attributes
    private final int CONFIG_PLAYERS = 2;

    //Number of turns in the game
    private int totalTurns = 30;
    
    //Maximum points for each turn
    public final int MAX_POINTS = 10000;

    private Game() {
        //Creates players
        playerManager = new PlayerManager();
        playerManager.createPlayers(CONFIG_PLAYERS);

        //Give them starting resources and goals
        resourceManager = new ResourceManager();
        goalManager = new GoalManager(resourceManager);

        map = new Map();

        state = GameState.NORMAL;

        //Adds all the subscriptions to the game which gives players resources and goals at the start of each turn.
        //Also decrements all connections and blocks a random one
        //The checking for whether a turn is being skipped is handled inside the methods, this just always calls them
        playerManager.subscribeTurnChanged(new TurnListener() {
            @Override
            public void changed() {
                Player currentPlayer = playerManager.getCurrentPlayer();
                goalManager.addRandomGoalToPlayer(currentPlayer);
                resourceManager.addRandomResourceToPlayer(currentPlayer);
                resourceManager.addRandomResourceToPlayer(currentPlayer);
                map.decrementBlockedConnections();
                map.blockRandomConnection();
            }
        });
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
            // initialisePlayers gives them a goal, and the GoalManager requires an instance of game to exist so this
            // method can't be called in the constructor
            instance.initialisePlayers();
        }

        return instance;
    }

    public void dispose() {
        instance = null;
    }

    // Only the first player should be given goals and resources during init
    // The second player gets them when turn changes!
    private void initialisePlayers() {
        Player player = playerManager.getAllPlayers().get(0);
        goalManager.addRandomGoalToPlayer(player);
        resourceManager.addRandomResourceToPlayer(player);
        resourceManager.addRandomResourceToPlayer(player);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GoalManager getGoalManager() {
        return goalManager;
    }

    public Map getMap() {
        return map;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        //Informs all listeners that the state has changed
        stateChanged();
    }

    public void subscribeStateChanged(GameStateListener listener) {
        gameStateListeners.add(listener);
    }
    
    public void unsubscribeStateChanged(GameStateListener listener) {
    	gameStateListeners.remove(listener);
    }

    private void stateChanged() {
        for (GameStateListener listener : gameStateListeners) {
            listener.changed(state);
        }
    }
    
    public void setTotalTurns(int totalTurns) {
    	if(totalTurns > 0 && totalTurns % 2 == 0) {
    		this.totalTurns = totalTurns;
    	}
    }
    
    public int getTotalTurns() {
    	return totalTurns;
    }
}