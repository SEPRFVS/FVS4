package fvs.taxe.clickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class NotificationButtonClickListener extends ReplayClickListener {
	
	private ImageButton button;
	private ScrollPane scrollPane;
	
	public NotificationButtonClickListener(ImageButton button, ScrollPane scrollPane) {
		this.button = button;
		this.scrollPane = scrollPane;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		super.clicked(event, x, y);
		viewNotifications();
	}
	
	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		viewNotifications();
	}
	
	public void viewNotifications() {
		button.addAction(fadeOut(0.25f));
		scrollPane.addAction(fadeIn(0.25f));
	}
}
