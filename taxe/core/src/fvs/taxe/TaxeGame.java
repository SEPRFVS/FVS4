package fvs.taxe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class TaxeGame extends Game {


    // Using native res of the map image we are using at the moment
    //Did not change this to allow resizing as this was deemed to be too much work
    public static final int WIDTH = 1272, HEIGHT = 678;

    public SpriteBatch batch;
    public BitmapFont font;
	public BitmapFont fontSmall;
	public BitmapFont fontTiny;
    public ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // font size 50pt
        font = new BitmapFont(Gdx.files.internal("data/play-50.fnt"));

        //font size 20pt
        fontSmall = new BitmapFont(Gdx.files.internal("data/play-20.fnt"));

		//font size 14pt
		fontTiny = new BitmapFont(Gdx.files.internal("data/play-14.fnt"));

        //Sets the main screen to be the menu
        setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }


}