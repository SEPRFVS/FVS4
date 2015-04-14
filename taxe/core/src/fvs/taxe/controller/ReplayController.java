package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import gameLogic.replay.ReplayManager;

public class ReplayController {
    private Context context;
    private ReplayManager replayManager;

    public ReplayController(Context context, ReplayManager replayManager) {
        this.context = context;
        this.replayManager = replayManager;

        addControls();
    }

    private void addControls() {
        TextButton playSingle = new TextButton("Single Click", context.getSkin());
        playSingle.setColor(Color.PINK);
        playSingle.setPosition(250, 100);
        playSingle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                replayManager.playSingle();
            }
        });

        context.getStage().addActor(playSingle);

        TextButton playSlow = new TextButton("Toggle Play Slow", context.getSkin());
        playSlow.setPosition(50, 100);
        playSlow.setColor(Color.PINK);
        playSlow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                replayManager.replayingToggle();
            }
        });

        context.getStage().addActor(playSlow);
    }
}
