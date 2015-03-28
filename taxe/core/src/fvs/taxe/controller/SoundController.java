package fvs.taxe.controller;

import java.util.ArrayList;
import java.util.List;

import Util.Tuple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
	private ArrayList<Tuple<String, Sound>> sounds = new ArrayList<Tuple<String, Sound>>();
	private Music backgroundMusic;
	private float soundVolume = 1.0f;
	private float musicVolume = 0.2f;
	
	public SoundController() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/noise.mp3"));
		sounds.add(new Tuple<String, Sound>("crash", Gdx.audio.newSound(Gdx.files.internal("sound/crash.mp3"))));
		sounds.add(new Tuple<String, Sound>("engineer", Gdx.audio.newSound(Gdx.files.internal("sound/engineer.mp3"))));
		sounds.add(new Tuple<String, Sound>("obstacle", Gdx.audio.newSound(Gdx.files.internal("sound/obstacle.mp3"))));
		sounds.add(new Tuple<String, Sound>("open", Gdx.audio.newSound(Gdx.files.internal("sound/open.mp3"))));
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
	}
}
