package test;

import static org.junit.Assert.*;
import fvs.taxe.controller.SoundController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Gdx;

public class SettingsTest extends LibGdxTest{

	private SoundController soundController;
	private String currentSettings = null;
	
	@Before
	public void setUpSettings() {
		if(Gdx.files.local("taxesettings.json").exists()) {
			currentSettings = Gdx.files.local("taxesettings.json").readString();
			Gdx.files.local("taxesettings.json").delete();
		}
		soundController = new SoundController(false);
	}
	
	@Test
	public void defaultVolumeTest() {
		assertTrue(soundController.getMusicVolume() == 0.2f);
		assertTrue(soundController.getSoundVolume() == 1.0f);
	}
	
	@Test
	public void saveSettingsAndLoadTest() {
		float soundVolume = 0.5f, musicVolume = 0.5f;
		soundController.setSoundVolume(soundVolume);
		soundController.setMusicVolume(musicVolume);
		soundController.saveSettings();
		soundController = null;
		soundController = new SoundController(false);
		assertTrue(soundController.getMusicVolume() == musicVolume);
		assertTrue(soundController.getSoundVolume() == soundVolume);
	}
	
	@After
	public void afterTest() {
		if(currentSettings != null) {
			Gdx.files.local("taxesettings.json").writeString(currentSettings, false);
		}
	}
}
