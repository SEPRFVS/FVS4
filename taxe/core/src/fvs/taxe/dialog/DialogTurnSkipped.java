package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import gameLogic.Game;

public class DialogTurnSkipped extends UnifiedDialog {

    public DialogTurnSkipped(Skin skin) {
        super("Miss a turn", skin, "redwin");
        //Informs player that they have missed their turn.
        text("Your turn has been skipped./nWhat a rotten bit of luck.");
        button("OK", "EXIT");
        align(Align.center);
    }

    @Override
    protected void result(Object obj) {
        //When the button is clicked
        Game.getInstance().getPlayerManager().turnOver(null);
        this.remove();
    }

}
