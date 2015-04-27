package fvs.taxe.controller;

import java.util.ArrayList;
import Util.Tuple;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import fvs.taxe.TaxeGame;
import fvs.taxe.dialog.DialogSettings;

public class SoundController {
	private ArrayList<Tuple<String, Sound>> sounds = new ArrayList<Tuple<String, Sound>>();
	private Music backgroundMusic;
	private float soundVolume = 1.0f;
	private float musicVolume = 0.2f;
	
	public SoundController() {
		this(true);		
	}
	
	public SoundController(boolean loadAudio) {
		loadSettings();
		if(loadAudio) {
			loadAudio();
		}
	}
	
	private void loadAudio() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/noise.mp3"));
		sounds.add(new Tuple<String, Sound>("crash", Gdx.audio.newSound(Gdx.files.internal("sound/crash.mp3"))));
		sounds.add(new Tuple<String, Sound>("engineer", Gdx.audio.newSound(Gdx.files.internal("sound/engineer.mp3"))));
		sounds.add(new Tuple<String, Sound>("obstacle", Gdx.audio.newSound(Gdx.files.internal("sound/obstacle.mp3"))));
		sounds.add(new Tuple<String, Sound>("open", Gdx.audio.newSound(Gdx.files.internal("sound/open.mp3"))));
		sounds.add(new Tuple<String, Sound>("modify", Gdx.audio.newSound(Gdx.files.internal("sound/modify.mp3"))));
	}
	
	public void playBackgroundMusic(float volume) {
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(volume);
		backgroundMusic.play();
	}
	
	public void playBackgroundMusic() {
		playBackgroundMusic(musicVolume);
	}
	
	public void stopBackgroundMusic() {
		backgroundMusic.stop();
	}
	
	public void playSound(String soundName, float volume) {
		for(Tuple<String, Sound> sound : sounds) {
			if(sound.getFirst().equals(soundName)) {
				sound.getSecond().play(volume);
				return;
			}
		}
	}
	
	public void playSound(String soundName) {
		playSound(soundName, soundVolume);
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		if(soundVolume >= 0.0f && soundVolume <= 1.0f) {
			this.soundVolume = soundVolume;
		}
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		if(musicVolume >= 0.0f && musicVolume <= 1.0f) {
			this.musicVolume = musicVolume;
		}
		if(backgroundMusic != null) {
			backgroundMusic.setVolume(musicVolume);
		}
	}
	
	public void loadSettings() {
		if(Gdx.files.isLocalStorageAvailable()) {
			if(Gdx.files.local("taxesettings.json").exists()) {
				JsonReader jsonReader = new JsonReader();
				JsonValue jsonVal = jsonReader.parse(Gdx.files.local("taxesettings.json"));
				
				if(jsonVal.has("music")) {
					setMusicVolume(jsonVal.get("music").asFloat());
				}
				if(jsonVal.has("sound")) {
					setSoundVolume(jsonVal.get("sound").asFloat());
				}
			}
		}
	}
	
	public void saveSettings() {
		if(Gdx.files.isLocalStorageAvailable()) {
			Object jsonOutput = new Object(){
				@SuppressWarnings("unused")
				private float music = getMusicVolume();
				@SuppressWarnings("unused")
				private float sound = getSoundVolume();
			};
			Json json = new Json();
			json.setOutputType(OutputType.json);
			Gdx.files.local("taxesettings.json").writeString(json.toJson(jsonOutput), false);
		}
	}
	
	public void addSettingsButton(final Stage stage, final Skin skin) {
		ImageButton button = new ImageButton(skin, "settings");
		button.setWidth(34.0f);
		button.setHeight(34.0f);
		button.setPosition(TaxeGame.WIDTH - 10.0f - button.getWidth(), 10.0f);
		final SoundController self = this;
		button.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				new DialogSettings(self, skin).show(stage);
			}
		});
		stage.addActor(button);
	}
}
