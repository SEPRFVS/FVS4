package fvs.taxe.controller;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import fvs.taxe.TaxeGame;

public class NotificationController {
	private Context context;
	private Table notification;
	
	public NotificationController(Context context) {
		this.context = context;
		notification = new Table();
		notification.setWidth(300.0f);
		notification.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - notification.getHeight());
		context.getStage().addActor(notification);
	}
	
	public void showNotification(String message, float displaytime) {
		Label label = new Label(message, context.getSkin(), "notification");
		label.setWrap(true);
		label.setWidth(notification.getWidth());
		label.setHeight(label.getStyle().font.getWrappedBounds(message, label.getWidth()).height + 10.0f);
		notification.row();
		notification.addActor(label);
		notification.setHeight(label.getHeight() + notification.getHeight());
		notification.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - notification.getHeight());
		label.addAction(sequence(fadeIn(0.25f), delay(displaytime), fadeOut(0.25f), removeUsedLabel(label)));
	}
	
	private RunnableAction removeUsedLabel(final Label label) {
		return new RunnableAction() {
			public void run() {
				notification.setHeight(notification.getHeight() - label.getHeight());
				notification.removeActor(label);
			}
		};
	}
}
