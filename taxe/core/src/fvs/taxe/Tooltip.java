package fvs.taxe;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import fvs.taxe.controller.Context;

public class Tooltip extends Label {
	
    public Tooltip(Context context) {
    	super("", context.getSkin(), "tooltip");
        setSize(150, 20);
        setVisible(false);
        setAlignment(Align.center);
    }

    public void show(String content) {
    	setText(content);
        setVisible(true);
        toFront();
    }

    public void hide() {
        setVisible(false);
    }
}
