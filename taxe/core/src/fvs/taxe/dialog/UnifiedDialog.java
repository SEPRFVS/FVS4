package fvs.taxe.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class UnifiedDialog extends Dialog {
	
	private Skin skin;
	private String windowStyleName;
	
	public UnifiedDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
		this.skin = skin;
		this.windowStyleName = windowStyleName;
	}
	
	/** Creates a button using the windowStyleName skin for the button (if it exists) */
	@Override
	public Dialog button(String text, Object object) {
		if (skin == null)
			throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
		if (skin.has(windowStyleName, TextButtonStyle.class)) {
			return button(text, object, skin.get(windowStyleName, TextButtonStyle.class));
		} else {
			return button(text, object, skin.get(TextButtonStyle.class));
		}
	}
	
	/** Creates a label using the windowStyleName skin for the label (if it exists) */
	@Override
	public Dialog text (String text) {
		if (skin == null)
			throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
		if (skin.has(windowStyleName, LabelStyle.class)) {
			return text(text, skin.get(windowStyleName, LabelStyle.class));
		} else {
			return text(text, skin.get(LabelStyle.class));
		}
	}

}
