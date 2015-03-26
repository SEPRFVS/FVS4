package gameLogic.replay;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class ReplayManager {
    private int playPosition = 0;
    private boolean playing = false;
    private Stage stage;
    private List<String> clicks = new ArrayList<String>();

    public void addClick(String actorId) {
        if (playing) return;

        System.out.println("Click on "+ actorId +" added");
        clicks.add(actorId);
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

        clickActorInStage(clicks.get(playPosition));
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
    }
}