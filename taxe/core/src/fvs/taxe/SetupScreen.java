package fvs.taxe;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fvs.taxe.controller.Context;
import gameLogic.player.Player;

public class SetupScreen extends ScreenAdapter {
	
	final private TaxeGame game;
	private Stage stage;
	private Context context;
	private Texture mapTexture;
	private Skin skin;
	private ArrayList<TextField> inputs = new ArrayList<TextField>();
	
	public SetupScreen(TaxeGame game, Context context) {
		this.game = game;
		this.context = context;
		stage = new Stage(new FitViewport(TaxeGame.WIDTH, TaxeGame.HEIGHT));
		
		this.skin = game.skin;
		Gdx.input.setInputProcessor(stage);
		
		mapTexture = new Texture(Gdx.files.internal("mainmenumap.jpg"));
	}
	
	 @Override
	 public void resize(int width, int height) {
		 stage.getViewport().update(width, height);
	 }
	 
	 @Override
	 public void render(float delta) {
		 Gdx.gl.glClearColor(1, 1, 1, 1);
	     Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	     game.batch.begin();
	     Color c = game.batch.getColor();
	     game.batch.setColor(c.r, c.g, c.b, (float) 0.3);
	     game.batch.draw(mapTexture, 0, 0, TaxeGame.WIDTH, TaxeGame.HEIGHT);
	     game.batch.setColor(c);
	     game.batch.end();
	     
	     game.batch.begin();
	     game.font.setColor(Color.BLACK);
	     game.font.draw(game.batch, "Team Names", TaxeGame.WIDTH/2 - 125.0f, TaxeGame.HEIGHT - 10.0f);
	     game.batch.end();
	     
	     stage.draw();
	 }
	 
	 @Override
	 public void show() {
		 float row = 0.0f;
		 for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
			 TextField text = new TextField("Player " + player.getPlayerNumber(), skin);
			 text.setName("name" + player.getPlayerNumber());
			 text.setPosition((TaxeGame.WIDTH/2) - (text.getWidth()/2), TaxeGame.HEIGHT - 100.0f - row);
			 row += text.getHeight() + 10.0f;
			 stage.addActor(text);
			 inputs.add(text);
		 }
		 TextButton button = new TextButton("OK", skin);
		 button.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y) {
				 for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
					 for(TextField text : inputs) {
						 if(text.getName().equals("name" + player.getPlayerNumber())) {
							 player.setName(text.getText());
						 }
					 }
				 }
				 game.setScreen(game.gamescreen);
				 return;
			 }
		 });
		 button.setWidth(60.0f);
		 button.setPosition((TaxeGame.WIDTH/2) - (button.getWidth()/2), TaxeGame.HEIGHT - 100.0f - row);
		 stage.addActor(button);
	 }
	 
	 @Override
	 public void dispose() {
	     mapTexture.dispose();
	     stage.dispose();
	 }
}
