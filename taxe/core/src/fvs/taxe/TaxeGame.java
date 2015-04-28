package fvs.taxe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.controller.SoundController;


public class TaxeGame extends Game {


    // Using native res of the map image we are using at the moment
    //Did not change this to allow resizing as this was deemed to be too much work
    public static final int WIDTH = 1272, HEIGHT = 678;

    public SpriteBatch batch;
    public Skin skin;
    public BitmapFont font;
    public BitmapFont fontMedium;
	public BitmapFont fontSmall;
	public BitmapFont fontTiny;
    public ShapeRenderer shapeRenderer;
    public SoundController soundController;
    
    public GameScreen gamescreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        //Sets the skin
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        // font size 50pt
        font = skin.getFont("play-50");
        //font size 30pt
        fontMedium = skin.getFont("play-30");
        //font size 20pt
        fontSmall = skin.getFont("play-20");
		//font size 14pt
		fontTiny = skin.getFont("play-14");
		
		//Set game sounds
		soundController = new SoundController();

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