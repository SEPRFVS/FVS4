package gameLogic.replay;

import Util.Tuple;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fvs.taxe.dialog.ReplayDialog;

import java.util.ArrayList;
import java.util.List;

public class ReplayManager {
    private int playPosition = 0;
    private boolean playing = false;
    private long seed;
    private Stage stage;
    private List<Tuple<ReplayType, String>> clicks = new ArrayList<Tuple<ReplayType, String>>();

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    private void addClick(ReplayType type, String s) {
        clicks.add(new Tuple<ReplayType, String>(type, s));
    }

    public void addClick(String actorId) {
        if (playing) return;

        System.out.println("Click on "+ actorId +" added");
        addClick(ReplayType.ACTOR_CLICK, actorId);
    }

    public void addDialogClick(String buttonId) {
        System.out.println("Click on "+ buttonId +" dia button added");
        addClick(ReplayType.DIALOG_BUTTON_CLICK, buttonId);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void playSingle() {
        playing = true;
        System.out.println("playing single..." + String.valueOf(playPosition));

        if (playPosition > clicks.size()) {
            System.out.println("Played all clicks");
            return;
        }

        Tuple<ReplayType, String> click = clicks.get(playPosition);

        switch (click.getFirst()) {
            case ACTOR_CLICK:
                clickActorInStage(click.getSecond());
                break;
            case DIALOG_BUTTON_CLICK:
                clickDialogButton(click.getSecond());
                break;
        }

        playPosition++;
        playing = false;
    }

    private void clickActorInStage(String name) {
        for(Actor actor : stage.getActors()) {
            if (!actor.getName().equals(name)) continue;

            System.out.println("Actor found in stage");

            for (EventListener listener : actor.getListeners()) {
                if (listener instanceof ClickListener) {
                    System.out.println("Click listener found in actor");

                    ((ClickListener) listener).clicked(null, 0, 0);
                }
            }

            return;
        }

        throw new RuntimeException("Actor " + name + " not found");
    }

    private void clickDialogButton(String id) {
        for(Actor actor : stage.getActors()) {
            if (actor instanceof ReplayDialog) {
                System.out.println("found dialog, now clicking..");

                ReplayDialog dialog = (ReplayDialog) actor;
                dialog.result(id);
                dialog.remove();
            }
        }
    }
}