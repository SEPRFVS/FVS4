package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fvs.taxe.controller.SoundController;

public class DialogSettings extends UnifiedDialog {
	
	private SoundController soundController;
	private float origMusic;
	private float origSound;

	public DialogSettings(final SoundController soundController, Skin skin) {
		super("Settings", skin, "greywin");
		this.soundController = soundController;
		
		//Create Labels, Sliders and handlers (and store original values to allow restoration)
		origMusic = soundController.getMusicVolume();
		text("Music Volume");
		Slider music = new Slider(0.0f, 1.0f, 0.01f, false, skin, "settings");
		music.setValue(origMusic);
		music.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soundController.setMusicVolume(((Slider) actor).getValue());
			}
		});
		getContentTable().add(music);
		
		getContentTable().row();
		
		origSound = soundController.getSoundVolume();
		text("Sound Volume");
		Slider sound = new Slider(0.0f, 1.0f, 0.01f, false, skin, "settings");
		sound.setValue(origSound);
		sound.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soundController.setSoundVolume(((Slider) actor).getValue());
			}
		});
		getContentTable().add(sound);
		
		//Create buttons
		button("OK", "OK");
		button("Cancel", "CANCEL");
	}
	
	@Override
    public void result(Object obj) {
        super.result(obj);
        
        if(obj == "CANCEL") {
        	//Reset SoundController to previous values
        	soundController.setMusicVolume(origMusic);
        	soundController.setSoundVolume(origSound);
        } else if(obj == "OK") {
        	//Save values
        	soundController.saveSettings();
        }
        
        this.remove();
	}

}
