package fvs.taxe;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fvs.taxe.controller.Context;
import fvs.taxe.dialog.UnifiedDialog;
import gameLogic.player.Player;

public class SetupScreen extends ScreenAdapter {
	
	final private TaxeGame game;
	private Stage stage;
	private Context context;
	private Texture mapTexture;
	private Skin skin;
	private ArrayList<TextField> inputs = new ArrayList<TextField>();
	private TextField numTurns;
	private TextButton endless;
	
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
	    
	    stage.draw();
	}
	 
	@Override
	public void show() {
		//Background
		Image background = new Image(mapTexture);
		background.setPosition(0, 0);
		background.setSize(TaxeGame.WIDTH, TaxeGame.HEIGHT);
		background.setColor(1.0f, 1.0f, 1.0f, 0.3f);
		stage.addActor(background);
		
		//Title
		Label label = new Label("Enter Team Names", skin, "play-50", Color.BLACK);
		label.setPosition((TaxeGame.WIDTH / 2) - (label.getWidth() / 2), TaxeGame.HEIGHT - 200.0f - label.getHeight());
		stage.addActor(label);
		
		//Input player names
		float row = 0.0f;
		for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
			TextField text = new TextField("Player " + player.getPlayerNumber(), skin, "names");
			text.setName("name" + player.getPlayerNumber());
			text.setPosition((TaxeGame.WIDTH/2) - (text.getWidth()/2), TaxeGame.HEIGHT - 330.0f - row);
			row += text.getHeight() + 10.0f;
			stage.addActor(text);
			inputs.add(text);
		}
		 
		//Set number of turns
		Table turnTable = new Table();
		numTurns = new TextField("30", skin, "names");
		numTurns.setTextFieldListener(new TextFieldListener(){
			@Override
			public void keyTyped(TextField textField, char c) {
				if(c < '0' || c > '9') {
					//Remove last character if not numeric
					if(textField.getText().length() > 1){
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					} else {
						textField.setText("");
					}
				}
			}
		});
		turnTable.add(numTurns).width(30.0f);
		turnTable.add(new Label("turns or ", skin, "defaultblack"));
		endless = new TextButton("Endless Mode", skin, "onoff");
		turnTable.add(endless);
		
		turnTable.setPosition((TaxeGame.WIDTH/2) - (turnTable.getWidth()/2), TaxeGame.HEIGHT - 330.0f - row);
		row += turnTable.getHeight() + 10.0f;
		stage.addActor(turnTable);
		
		//Add OK and cancel buttons
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
				//Test number of turns input
				if(endless.isChecked()) {
					context.getGameLogic().setTotalTurns(0);
				} else {
					if(numTurns.getText().equals("")) {
						UnifiedDialog dialog = new UnifiedDialog("Check Number of Turns", skin, "redwin");
						dialog.text("Please make sure the number of turns is a positive, even number");
						dialog.button("OK");
						dialog.show(stage);
						return;
					} else {
						if(Integer.parseInt(numTurns.getText()) == 0 || Integer.parseInt(numTurns.getText()) % 2 != 0) {
							//Prevent running further
							UnifiedDialog dialog = new UnifiedDialog("Check Number of Turns", skin, "redwin");
							dialog.text("Please make sure the number of turns is a positive, even number");
							dialog.button("OK");
							dialog.show(stage);
						 	return;
					 	} else {
						 	context.getGameLogic().setTotalTurns(Integer.parseInt(numTurns.getText()));
					 	}
					}
				}
				game.setScreen(game.gamescreen);
				return;
			}
		});
		button.setWidth(60.0f);
		button.setPosition((TaxeGame.WIDTH/2) - (button.getWidth()/2), TaxeGame.HEIGHT - 350.0f - row - button.getHeight());
		stage.addActor(button);
		
		context.getSoundController().addSettingsButton(stage, skin);
	}
	
	@Override
	public void dispose() {
		mapTexture.dispose();
	    stage.dispose();
	}
}
