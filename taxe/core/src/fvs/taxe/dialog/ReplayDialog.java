package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import gameLogic.replay.ReplayManager;

public class ReplayDialog extends UnifiedDialog {
    private ReplayManager replayManager;

    public ReplayDialog(String title, Skin skin, String windowStyleName, ReplayManager replayManager) {
        super(title, skin, windowStyleName);

        // dialogs must not be modal when replaying, as the user will click buttons not in the dialog box
        //this.setModal(false);
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