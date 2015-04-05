package fvs.taxe.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;

public class StationActor extends Image implements Selectable {
    private static int width = 20;
    private static int height = 20;
    private Drawable normalDrawable;
    private Drawable selectedDrawable;
    private Rectangle bounds;
    private static Station station;

    public StationActor(IPositionable location, Station station) {
        super(new Texture(Gdx.files.internal("station_dot.png")));
        normalDrawable = getDrawable();
        selectedDrawable = new Image(new Texture(Gdx.files.internal("station_dot_selected.png"))).getDrawable();
        setSize(width, height);
        setPosition(location.getX() - width / 2, location.getY() - height / 2);
        bounds = new Rectangle();
        bounds.set(getX(), getY(), getWidth(), getHeight());
        this.station = station;
    }

    public static Station getStation() {
        return station;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) setDrawable(selectedDrawable);
        else setDrawable(normalDrawable);
    }
}
