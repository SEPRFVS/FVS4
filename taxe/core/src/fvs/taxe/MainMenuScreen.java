package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen extends ScreenAdapter {
	private Stage stage;
    TaxeGame game;
    OrthographicCamera camera;
    Rectangle playBounds;
    Rectangle exitBounds;
    Vector3 touchPoint;
    Texture mapTexture;

    public MainMenuScreen(final TaxeGame game) {
        //This sets all the relevant variables for the menu screen
        //Did not understand this fully so did not change anything
        this.game = game;
        stage = new Stage(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT));
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false);

        //Loads the gameMap in
        mapTexture = new Texture(Gdx.files.internal("mainmenumap.jpg"));
        
        //Create the menu buttons
        TextButton playButton = new TextButton("Start Game", game.skin, "biggreen");
        playButton.setWidth(400);
        playButton.setHeight(100);
        playButton.setPosition((TaxeGame.WIDTH / 2) - (playButton.getWidth() / 2), 350);
        playButton.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		game.gamescreen = new GameScreen(game);
                game.setScreen(game.gamescreen);
        	}
        });
        stage.addActor(playButton);
        
        TextButton exitButton = new TextButton("Exit", game.skin, "bigred");
        exitButton.setWidth(400);
        exitButton.setHeight(100);
        exitButton.setPosition((TaxeGame.WIDTH / 2) - (playButton.getWidth() / 2), 200);
        exitButton.addListener(new ClickListener(){
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		Gdx.app.exit();
        	}
        });
        stage.addActor(exitButton);
        
        //Load background music
        game.soundController.playBackgroundMusic();
        game.soundController.addSettingsButton(stage, game.skin);
    }

    public void draw() {
        //This method draws the menu
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw transparent map in the background
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        Color c = game.batch.getColor();
        game.batch.setColor(c.r, c.g, c.b, (float) 0.3);
        game.batch.draw(mapTexture, 0, 0, TaxeGame.WIDTH, TaxeGame.HEIGHT);
        game.batch.setColor(c);
        game.batch.end();
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void render(float delta) {
        draw();
    }
    
    @Override
    public void resize(int width, int height) {
    	stage.getViewport().update(width, height);
    }
}