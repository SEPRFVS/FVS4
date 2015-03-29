package fvs.taxe;

import java.util.ArrayList;

import gameLogic.RandomSingleton;
import gameLogic.replay.ReplayManager;

public class ReplayScreen extends GameScreen {
    private ReplayManager replayManager;
    protected ArrayList<String> playerNames;

    public ReplayScreen(TaxeGame game, ReplayManager replayManager, ArrayList<String> playerNames) {
        this.replayManager = replayManager;
        this.game = game;
        this.playerNames = playerNames;

        init(replayManager);
    }

    @Override
    protected void setRandomSeed(ReplayManager rm) {
        RandomSingleton.setFromSeed(rm.getSeed());
    }
}
