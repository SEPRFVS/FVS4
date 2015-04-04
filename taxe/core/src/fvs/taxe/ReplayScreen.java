package fvs.taxe;

import java.util.ArrayList;

import gameLogic.RandomSingleton;
import gameLogic.replay.ReplayManager;

public class ReplayScreen extends GameScreen {
    private ReplayManager replayManager;
    protected ArrayList<String> playerNames;
    protected int totalTurns;

    public ReplayScreen(TaxeGame game, ReplayManager replayManager, ArrayList<String> playerNames, int totalTurns) {
        this.replayManager = replayManager;
        this.game = game;
        this.playerNames = playerNames;
        this.totalTurns = totalTurns;

        init(replayManager);
    }

    @Override
    protected void setRandomSeed(ReplayManager rm) {
        RandomSingleton.setFromSeed(rm.getSeed());
    }
}
