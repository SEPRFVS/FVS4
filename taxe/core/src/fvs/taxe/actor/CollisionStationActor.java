package fvs.taxe.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import gameLogic.map.IPositionable;

public class CollisionStationActor extends Image implements Selectable {
    private static int width = 16;
    private static int height = 16;
    private Drawable normalDrawable;
    private Drawable selectedDrawable;

    public CollisionStationActor(IPositionable location) {
        //Places the actor
        super(new Texture(Gdx.files.internal("junction_dot.png")));
        normalDrawable = getDrawable();
        selectedDrawable = new Image(new Texture(Gdx.files.internal("station_dot_selected.png"))).getDrawable();
        setSize(width, height);
        setPosition(location.getX() - width / 2, location.getY() - height / 2);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) setDrawable(selectedDrawable);
        else setDrawable(normalDrawable);
    }
}
