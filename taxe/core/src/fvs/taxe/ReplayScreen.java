package fvs.taxe;

import gameLogic.RandomSingleton;
import gameLogic.replay.ReplayManager;

public class ReplayScreen extends GameScreen {
    private ReplayManager replayManager;

    public ReplayScreen(TaxeGame game, ReplayManager replayManager) {
        this.replayManager = replayManager;
        this.game = game;

        init(replayManager);
    }

    @Override
    protected void setRandomSeed(ReplayManager rm) {
        RandomSingleton.setFromSeed(rm.getSeed());
    }
}
