package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Tooltip extends Label {
	
    public Tooltip(TaxeGame game) {
    	super("", new LabelStyle(game.fontSmall, Color.WHITE));
    	LabelStyle style = new LabelStyle(game.fontSmall, Color.WHITE);
    	style.background = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("ui/tooltip.png"))));
    	setStyle(style);
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
