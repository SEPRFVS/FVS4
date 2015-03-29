package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import gameLogic.Game;
import gameLogic.replay.ReplayManager;

public class DialogTurnSkipped extends ReplayDialog {

    public DialogTurnSkipped(Skin skin, ReplayManager replayManager) {
        super("Miss a turn", skin, "redwin", replayManager);
        //Informs player that they have missed their turn.
        text("Your turn has been skipped.\nWhat a rotten bit of luck.");
        button("OK", "EXIT");
        align(Align.center);
    }

    @Override

    public void result(Object obj) {
        super.result(obj);

        //When the button is clicked
        Game.getInstance().getPlayerManager().turnOver(null);
        this.remove();
    }

}
