package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import gameLogic.Game;
import gameLogic.replay.ReplayManager;

public class DialogTurnSkipped extends ReplayDialog {

    public DialogTurnSkipped(Skin skin, ReplayManager replayManager) {
        super("Miss a turn", skin, replayManager);
        //Informs player that they have missed their turn.
        text("Due to circumstances outside our control \n Network Rail would like to apologise for you missing your turn.");
        button("OK", "EXIT");
        align(Align.center);
    }

    @Override
    public Dialog show(Stage stage) {
        //Shows the dialog
        show(stage, null);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    @Override
    public void hide() {
        //Hides the dialog
        hide(null);
    }

    @Override
    public void result(Object obj) {
        super.result(obj);

        //When the button is clicked
        Game.getInstance().getPlayerManager().turnOver(null);
        this.remove();
    }

}
