package fvs.taxe.controller;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fvs.taxe.StageNamedActor;
import fvs.taxe.TaxeGame;
import gameLogic.Game;
import gameLogic.replay.ReplayManager;

public class Context {
    //Context appears to be a class that allows different aspects of the system access parts that they otherwise logically shouldn't have access to.
    //While this is a bit of a workaround to make implementation easier, it does weaken encapsulation somewhat, however a full system overhaul would be unfeasible to remedy this.
    private TaxeGame taxeGame;
    private StageNamedActor stage;
    private Skin skin;
    private Game gameLogic;
    private ReplayManager replayManager;
    private RouteController routeController;
    private SideBarController sideBarController;
    private SoundController soundController;
    private NotificationController notificationController;
    private ReplayControlsController replayControlsController;

    public Context(StageNamedActor stage, Skin skin, TaxeGame taxeGame, Game gameLogic) {
        this.stage = stage;
        this.skin = skin;
        this.taxeGame = taxeGame;
        this.gameLogic = gameLogic;
        this.soundController = taxeGame.soundController;
    }

    //Getters and setters: pretty self-explanatory
    public StageNamedActor getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }

    public TaxeGame getTaxeGame() {
        return taxeGame;
    }

    public Game getGameLogic() {
        return gameLogic;
    }

    public RouteController getRouteController() {
        return routeController;
    }

    public void setRouteController(RouteController routeController) {
        this.routeController = routeController;
    }

    public SideBarController getSideBarController() {
        return sideBarController;
    }

    public void setSideBarController(SideBarController sideBarController) {
        this.sideBarController = sideBarController;
    }

	public SoundController getSoundController() {
		return soundController;
	}

	public void setSoundController(SoundController soundController) {
		this.soundController = soundController;
	}

    public ReplayManager getReplayManager() {
        return replayManager;
    }

    public void setReplayManager(ReplayManager replayManager) {
        this.replayManager = replayManager;
    }

	public NotificationController getNotificationController() {
		return notificationController;
	}

	public void setNotificationController(NotificationController notificationController) {
		this.notificationController = notificationController;
	}

	public ReplayControlsController getReplayControlsController() {
		return replayControlsController;
	}

	public void setReplayControlsController(ReplayControlsController replayControlsController) {
		this.replayControlsController = replayControlsController;
	}
}
