package fvs.taxe.controller;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import fvs.taxe.TaxeGame;
import fvs.taxe.clickListener.NotificationButtonClickListener;
import fvs.taxe.clickListener.NotificationScrollPaneClickListener;
import gameLogic.goal.Goal;
import gameLogic.map.Connection;
import gameLogic.map.Station;
import gameLogic.player.Player;
import gameLogic.resource.Train;

public class NotificationController {
	private Context context;
	private ScrollPane scrollPane;
	private Table notification;
	private ImageButton button;
	public boolean autoShow = false;
	
	public static final float PANE_WIDTH = 300.0f;
	public static final float PANE_HEIGHT = 160.0f;
	
	public NotificationController(Context context) {
		this.context = context;
		//Set-up table to hold notification
		notification = new Table();
		notification.setWidth(PANE_WIDTH);
		notification.left().top();
		
		//Set-up scroll pane to hold table and allow scrolling to historic messages
		scrollPane = new ScrollPane(notification, context.getSkin(), "notification");
		scrollPane.setWidth(PANE_WIDTH);
		scrollPane.setHeight(PANE_HEIGHT);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setSmoothScrolling(true);
		scrollPane.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - scrollPane.getHeight()); //Set initial position
		context.getStage().addActor(scrollPane);
		scrollPane.addAction(alpha(0));
		
		//Set-up button to access panel
		button = new ImageButton(context.getSkin(), "notification");
		button.setHeight(54);
		button.setWidth(54);
		button.setPosition(10.0f, TaxeGame.HEIGHT - 10.0f - button.getHeight());
		button.addListener(new NotificationButtonClickListener(button, scrollPane));
		context.getStage().addActor(button);
		
		//Prevent fade-away if scrollPane is clicked or entered
		scrollPane.addListener(new NotificationScrollPaneClickListener(scrollPane, button, this));
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
	
	public void showObstacleMessage(Connection connection) {
		String message = "@RailControl: #delays between #" + connection.getStation1().getName() + " and #" + connection.getStation2().getName() + "";
		showNotification(message);
	}
	
	public void showObstacleCause(Station station, Player player) {
		String message = "@RailControl: Issues near #" + station.getName() + " caused by a track failiure.  @" + player.getName().replace(" ", "") + " may know more";
		showNotification(message);
	}
	
	public void showEngineerMessage(Connection connection, Player player) {
		String message = "@" + player.getName().replace(" ", "") + ": Keeping @RailControl happy by fixing track problems between #" + connection.getStation1().getName() + " & #" + connection.getStation2().getName();
		showNotification(message);
	}
	
	public void showNotification(String message) {
		//Give a default duration if none provided
		showNotification(message, 1.75f);
	}
	
	public void showNotification(String message, float displaytime) {
		//Create label for notification
		Label label = new Label(message, context.getSkin(), "notification");
		label.setWrap(true);
		//Add label to table
		notification.add(label).pad(5.0f).width(PANE_WIDTH - 12.0f);
		notification.row();
		//Animate
		label.addAction(sequence(alpha(0), delay(0.1f), fadeIn(0.25f)));
		button.addAction(sequence(fadeOut(0.25f), delay(5f), fadeIn(0.25f), new RunnableAction(){public void run(){button.clearActions();}}));
		scrollPane.addAction(sequence(delay(0.1f), new RunnableAction(){public void run(){scrollPane.setScrollPercentY(100);autoShow = true;}}, fadeIn(0.25f), delay(5f), fadeOut(0.25f), new RunnableAction(){public void run(){scrollPane.clearActions();autoShow = false;}}));
	}
}
