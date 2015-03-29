package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import fvs.taxe.TaxeGame;
import gameLogic.goal.Goal;
import gameLogic.player.Player;
import gameLogic.resource.Train;

public class NotificationController {
	private Context context;
	private Table notification;
	
	public NotificationController(Context context) {
		this.context = context;
		notification = new Table();
		notification.setWidth(300.0f);
		notification.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - notification.getHeight()); //Set initial position
		context.getStage().addActor(notification);
	}
	
	public void showGoalComplete(Player player, Goal goal) {
		String message = "@" + player.getName().replace(" ", "") + ": Just got a train to #" + goal.getDestination().getName() + "! #winning";
		showNotification(message);
	}
	
	public void showCollisionMessage(Train train1, Train train2) {
		//Show Message from resident
		String message1 = "@DwileFlonking: Heard ";
		if(train1.getPlayer() == train2.getPlayer()) {
			message1 += "two @" + train1.getPlayer().getName().replace(" ", "") + " trains";
		} else {
			message1 += "a @" + train1.getPlayer().getName().replace(" ", "") + " & @" + train2.getPlayer().getName().replace(" ", "") + " train"; 
		}
		message1 += " crashed near #" + train1.getNextStation().getName() + ".  Any news @RailControl? #concerned";
		showNotification(message1);
		
		//Show message from rail controllers
		String message2 = "@RailControl: Line closed from #" + train1.getLastStation().getName() + " - #" + train1.getNextStation().getName() + " following a crash. #delays";
		showNotification(message2);
	}
	
	public void showNotification(String message) {
		//Give a default duration if none provided
		showNotification(message, 1.75f);
	}
	
	public void showNotification(String message, float displaytime) {
		//Create label for notification
		Label label = new Label(message, context.getSkin(), "notification");
		label.setWrap(true);
		label.setWidth(notification.getWidth());
		label.setHeight(label.getStyle().font.getWrappedBounds(message, label.getWidth()).height + 10.0f);
		//Add label to table
		notification.row();
		notification.addActor(label);
		notification.setHeight(label.getHeight() + notification.getHeight());
		notification.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - notification.getHeight());
		label.addAction(sequence(fadeIn(0.25f), delay(displaytime), fadeOut(0.25f), removeUsedLabel(label)));
	}
	
	private RunnableAction removeUsedLabel(final Label label) {
		return new RunnableAction() {
			public void run() {
				//Remove label so that previous labels move up
				notification.setHeight(notification.getHeight() - label.getHeight());
				notification.removeActor(label);
			}
		};
	}
}
