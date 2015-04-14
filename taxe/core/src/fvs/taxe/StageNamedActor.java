package fvs.taxe;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StageNamedActor extends Stage {
    private int actorId = 1000;

    public StageNamedActor(Viewport viewport) {
        super(viewport);
    }

    // uniquely name actors before they're added to the stage, this unique name is then used to locate the
    // actor from within the stage when in replay mode
    public void addNamedActor(final Actor actor) {
        actor.setName((String.valueOf(actorId)));
        // uniquely name each actor
        actorId++;
        super.addActor(actor);
    }
}
