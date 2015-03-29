package fvs.taxe.clickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import gameLogic.replay.ReplayManager;

public class ReplayClickListener extends ClickListener {
    private ReplayManager replayManager;
    private Actor actor;

    public ReplayClickListener(ReplayManager replayManager, Actor actor) {
        this.replayManager = replayManager;
        this.actor = actor;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        replayManager.addClick(actor.getName());
    }
}
