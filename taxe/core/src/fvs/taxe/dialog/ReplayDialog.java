package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gameLogic.replay.ReplayManager;

public class ReplayDialog extends Dialog {
    private ReplayManager replayManager;

    public ReplayDialog(String title, Skin skin, ReplayManager replayManager) {
        super(title, skin);

        this.replayManager = replayManager;
    }

    @Override
    protected void result(Object o) {
        replayManager.addDialogClick(o.toString());
    }
}