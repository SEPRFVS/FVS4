package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import gameLogic.Game;
import gameLogic.player.PlayerManager;
import gameLogic.replay.ReplayManager;

public class DialogTurnSkipped extends ReplayDialog {
	
	private PlayerManager pm;

    public DialogTurnSkipped(PlayerManager pm, Skin skin, ReplayManager replayManager) {
        super("Miss a turn", skin, "redwin", replayManager);
        //Informs player that they have missed their turn.
        text("Your turn has been skipped.\nWhat a rotten bit of luck.");
        button("OK", "EXIT");
        align(Align.center);
        
        this.pm = pm;
    }

    @Override

    public void result(Object obj) {
        super.result(obj);

        //When the button is clicked
        pm.turnOver(null);
        this.remove();
    }

}
