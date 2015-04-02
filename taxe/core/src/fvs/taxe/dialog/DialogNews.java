package fvs.taxe.dialog;

import java.util.ArrayList;

import fvs.taxe.controller.Context;
import gameLogic.goal.Goal;
import gameLogic.listeners.GameStateListener;
import gameLogic.map.Connection;
import gameLogic.player.Player;
import gameLogic.replay.ReplayManager;
import gameLogic.resource.Engineer;
import gameLogic.resource.Resource;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class DialogNews extends ReplayDialog {
	
	private GameStateListener exitListener = null;
	private Context context;
	
	public static final int SHOW_EVERY = 5;

	public DialogNews(Context context, ReplayManager replayManager) {
		//Generates a dialog giving news based on scores and events in the past turns
	    super("THE DAILY RAIL", context.getSkin(), "news", replayManager);
	    
	    Table content = getContentTable();
	    if(context.getGameLogic().getPlayerManager().getTurnNumber() == context.getGameLogic().TOTAL_TURNS) {
	    	//Show end game news
	    	double highScore = 0;
	        Player winner = null;
	        for (Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
	            //Checks each player's score
	            if (player.getScore() > highScore) {
	                highScore = player.getScore();
	                //Need to add one as playerNumber is 0-based indexing
	                winner = player;
	            }else if (player.getScore() == highScore){
	                player = null;
	            }
	        }
	        if(winner != null) {
	        	int goalsComplete = 0;
	        	for(Goal goal : winner.getGoals()) {
	        		if(goal.getComplete()) {
	        			goalsComplete++;
	        		}
	        	}
	        	String col1 = "Since starting operation " + winner.getName() + " have become a force to be reckoned with, delivering " + goalsComplete + " trains to their destination.";
	        	ArrayList<String> names = new ArrayList<String>();
		    	for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
		    		if(player != winner) {
		    			names.add(player.getName());
		    		}
		    	}
		    	String col2 = "They have left their rivals ";
		    	if (names.size() > 1) {
		    		col2 += String.join(", ", names.subList(0, names.size() - 2)) + " and " + names.get(names.size() - 1);
		    	} else {
		    		col2 += names.get(0);
		    	}
		    	col2 += " to go bust as their market dominance increases.  Allegedly, they have profits in excess of $" + winner.getScore() + ".";
		    	String col3 = "Notorious social media commentator, @DwileFlonking, was happy at the news saying \"#HappyDays for @" + winner.getName().replace(" ", "") + " with their global domination\".";
	        	setStory(context.getSkin(), winner.getName() + " emerges victorious", "Other companies left for dead by their success", col1, col2, col3);
	        } else {
	        	ArrayList<String> names = new ArrayList<String>();
		    	for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
		    		names.add(player.getName());
		    	}
		    	String col1 = "Since ";
		    	if (names.size() > 1) {
		    		col1 += String.join(", ", names.subList(0, names.size() - 2)) + " and " + names.get(names.size() - 1);
		    	} else {
		    		col1 += names.get(0);
		    	}
		    	col1 += "entered the market, no breakout transit player has emerged.  This has left rail users in limbo.";
		    	String col2 = "European Rail Control have said that they're considering re-nationalising the european rail operators to re-invigorate the industry.";
		    	ArrayList<String> socialNames = new ArrayList<String>();
		    	for(String name : names) {
		    		socialNames.add("@" + name.replace(" ", ""));
		    	}
		    	String col3 = "Outspoken social media user, @DwileFlonking, helpfully interjected \"" + String.join(" ", socialNames) + "What now? #future\"";
	        	setStory(context.getSkin(), "Rail transit market stagnates", "No clear winner in battle to be the best", col1, col2, col3);
	        }
	    } else if(context.getGameLogic().getPlayerManager().getTurnNumber() == 0) {
	       	//Show start game news
	    	ArrayList<String> names = new ArrayList<String>();
	    	for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
	    		names.add(player.getName());
	    	}
	    	String col1 = "";
	    	if (names.size() > 1) {
	    		col1 = String.join(", ", names.subList(0, names.size() - 1)) + " and " + names.get(names.size() - 1);
	    	} else {
	    		col1 = names.get(0);
	    	}
	    	col1 += " have entered the rail market with the hopes of making it big in the transportation industry.";
	    	String col2 = "There isn't much room left in the industry so we don't know how long they'll survive.  Social media had it's own opinion.";
	    	String col3 = "User @DwileFlonking commented \"New people on the tracks?  Hope they don't crash #awkward\".  Rail Control said they remain \"optimistic\".";
	    	setStory(context.getSkin(), "Big dreams for small companies","Will newcomers take over the rail market?", col1, col2, col3);
	    } else {
	       	//Show regular news
	    	int connectionsBlocked = 0;
	    	for(Connection connection : context.getGameLogic().getMap().getConnections()) {
	    		if(connection.isBlocked()){
	    			connectionsBlocked++;
	    		}
	    	}
	    	if(connectionsBlocked > 4) {
	    		String col1 = "Today, " + connectionsBlocked + " pieces of track were completely closed to trains as maintainance has fallen behind.";
	    		String col2 = "In response to accusations, Rail Control said \"";
	    		Player mostEngineers = null;
	    		int mostEngineersNum = 0;
	    		for (Player player: context.getGameLogic().getPlayerManager().getAllPlayers()) {
	    			int engineers = 0;
	    			for (Resource resource : player.getResources()) {
	    				if(resource instanceof Engineer) {
	    					engineers++;
	    				}
	    			}
	    			if(engineers > mostEngineersNum) {
	    				mostEngineers = player;
	    				mostEngineersNum = engineers;
	    			}
	    		}
	    		if(mostEngineers != null){
	    			col2 += mostEngineers.getName() + " has been underutilising it's engineers.  They have " + mostEngineersNum + " sat doing nothing.";
	    		} else {
	    			col2 += "All our engineers are flat out with repairs.  Years of underinvestment have created this situation.";
	    		}
	    		col2 += "\"";
	    		String col3 = "There have been accusations from an industry insider that train companies have been deliberatly damaging tracks used by their competitors.";
	    		setStory(context.getSkin(), "Poor Maintainance Record Shows", "European Rail Control slammed by passengers", col1, col2, col3);
	    	} else {
	    		Player aheadPlayer = null;
	    		for (Player player:  context.getGameLogic().getPlayerManager().getAllPlayers()) {
	    			if(aheadPlayer == null) {
	    				aheadPlayer = player;
	    			} else if(player.getScore() > aheadPlayer.getScore()) {
	    				aheadPlayer = player;
	    			}
	    		}
	    		String col1 = "The rail war continues as " + aheadPlayer.getName() + " shows they mean business by delivering their best results yet.";
	    		int completedGoals = 0;
	    		for(Goal goal : aheadPlayer.getGoals()) {
	    			if(goal.getComplete()){
	    				completedGoals++;
	    			}
	    		}
	    		String col2 = completedGoals + " trains have arrived at their destination over the past few days.  We don't yet know about other operators performance but it's a good start.";
	    		String col3 = "Can they maintain their momentum though?  Their chairman certainly thinks so, \"We always strive to be the best and won't stop until we are.\"";
	    		setStory(context.getSkin(), aheadPlayer.getName() + " deliver results", "Company posts it's best results to date", col1, col2, col3);
	    	}
	    }
	     
	    content.pad(20.0f).align(Align.topLeft);
	       
	    button("Close", "CLOSE");
	}
	
	private void setStory(Skin skin, String title, String subtitle, String col1, String col2, String col3) {
		Table content = this.getContentTable();
		
       	content.add(new Label(title, skin, "newsheadline")).colspan(3);
       	content.row();
       	content.add(new Label(subtitle, skin, "newssub")).colspan(3);
       	content.row();
       	Label content1 = new Label(col1, skin, "news");
       	content1.setWrap(true);
       	content1.setAlignment(Align.topLeft);
       	content.add(content1).fill();
       	Label content2 = new Label(col2, skin, "news");
       	content2.setWrap(true);
       	content2.setAlignment(Align.topLeft);
       	content.add(content2).fill();
       	Label content3 = new Label(col3, skin, "news");
       	content3.setWrap(true);
       	content3.setAlignment(Align.topLeft);
       	content.add(content3).fill();
	}
	
	public DialogNews removeListenerOnExit(final GameStateListener listener, final Context context) {
		this.exitListener = listener;
		this.context = context;
		return this;
	}
	
	@Override
	public void result(Object obj) {
		super.result(obj);
		if(obj == "CLOSE") {
			//Remove listener (if present) and remove
			if(exitListener != null) {
				context.getGameLogic().unsubscribeStateChanged(exitListener);
				this.remove();
			}
		}
	}
}
