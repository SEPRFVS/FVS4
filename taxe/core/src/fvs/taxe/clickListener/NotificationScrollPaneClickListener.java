package fvs.taxe.clickListener;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import fvs.taxe.controller.NotificationController;

public class NotificationScrollPaneClickListener extends ReplayClickListener {
	
	private ScrollPane scrollPane;
	private ImageButton button;
	private NotificationController nc;
	
	public NotificationScrollPaneClickListener(ScrollPane scrollPane, ImageButton button, NotificationController nc) {
		this.scrollPane = scrollPane;
		this.button = button;
		this.nc = nc;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		super.clicked(event, x, y);
		revertListener();
	}
	
	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		revertListener();
	}
	
	public void revertListener() {
		if(nc.autoShow == true) {
			button.clearActions();
			scrollPane.clearActions();
			button.addAction(fadeOut(0.25f));
			scrollPane.addAction(fadeIn(0.25f));
			nc.autoShow = false;
		}
	}
	
	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		if (event.getStageX() > scrollPane.getX() && event.getStageX() < scrollPane.getX() + scrollPane.getWidth() && event.getStageY() > scrollPane.getY() && event.getStageY() < scrollPane.getY() + scrollPane.getHeight()) {
			return;
		} else {
			scrollPane.addAction(fadeOut(0.25f));
			button.addAction(fadeIn(0.25f));
		}
	}
}
