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

    // this method is called when the user clicks a button on a dialog, obj is used to differentiate which
    // button was clicked. when replaying the game, we need this method to be public, as we're calling this method
    // from outside this class and subclasses
    @Override
    public void result(Object o) {
        replayManager.addDialogClick(o.toString());
    }
}