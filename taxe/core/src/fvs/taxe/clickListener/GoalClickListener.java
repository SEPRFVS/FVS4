package fvs.taxe.clickListener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import fvs.taxe.Tooltip;
import fvs.taxe.controller.Context;
import fvs.taxe.dialog.DialogGoal;
import gameLogic.GameState;
import gameLogic.goal.Goal;
import gameLogic.map.Station;
import gameLogic.player.Player;

//Responsible for checking whether the goal is clicked
public class GoalClickListener extends ReplayClickListener {
    private Context context;
    private Goal goal;
    private Tooltip tooltip1;
    private Tooltip tooltip2;
    private Tooltip tooltip3;

    //This boolean was necessary to check whether tooltips are currently being displayed or not. Otherwise tooltips got constantly re-rendered
    private boolean showingTooltips;

    public GoalClickListener(Context context, Goal goal, Actor actor) {
        super(context.getReplayManager(), actor);

        this.goal = goal;
        this.context = context;
        this.showingTooltips = false;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        //A check was necessary as to whether tooltips were currently being shown
        //This is due to the odd way that the events work
        //When clicking on a goal, it simultaneously performs the enter and exit methods
        //This led to some unintended behaviour where the tooltips were permanently rendered
        //Therefore they are only hidden if they are being shown
        if (showingTooltips) {

            //This hides the currently shown tooltips as otherwise they get stuck
            tooltip1.hide();
            tooltip2.hide();

            //Tooltip3 might not always exist, therefore by enclosing this in a try catch, if tooltip3 is null then the program does not crash
            try {
                tooltip3.hide();
            } catch (Exception e) {
            }

            //Resets the tooltip flag to false
            showingTooltips = false;
        }


        if (context.getGameLogic().getState() == GameState.NORMAL) {
            //If the current game state is normal then a dialog is displayed allowing the user to interact with their goal
            Player currentPlayer = context.getGameLogic().getPlayerManager().getCurrentPlayer();
            DialogGoalButtonClicked listener = new DialogGoalButtonClicked(currentPlayer,
                    goal);
            DialogGoal dia = new DialogGoal(goal, context.getSkin(), context.getReplayManager());
            dia.show(context.getStage());
            dia.subscribeClick(listener);
        }
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (!showingTooltips) {
            //Need to check whether tooltips are currently being shown as otherwise it redraws them instantly after the clicked routine has ended
            tooltip1 = new Tooltip(context);
            Station origin = goal.getOrigin();
            Image originActor = (Image) origin.getActor();

            //Sets the tooltip to have the origin's name and to be shown to the top right of the station
            tooltip1.setPosition(originActor.getX() + 20, originActor.getY() + 20);
            tooltip1.show(origin.getName());
            context.getStage().addActor(tooltip1);

            //Sets the tooltip to have the destination's name and to be shown to the top right of the station
            tooltip2 = new Tooltip(context);
            Station destination = goal.getDestination();
            Image destinationActor = (Image) destination.getActor();
            context.getStage().addActor(tooltip2);
            tooltip2.setPosition(destinationActor.getX() + 20, destinationActor.getY() + 20);
            tooltip2.show(destination.getName());

            //If there is an intermediary station then a tooltip is also drawn for this station in the same way as the others
            Station intermediary = goal.getIntermediary();
            if (!intermediary.getName().equals(origin.getName())) {
                tooltip3 = new Tooltip(context);
                Image intermediaryActor = (Image) intermediary.getActor();
                context.getStage().addActor(tooltip3);
                tooltip3.setPosition(intermediaryActor.getX() + 20, intermediaryActor.getY() + 20);
                tooltip3.show(intermediary.getName());
            }

            //Indicates that toolTips are currently being displayed
            showingTooltips = true;
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        //If tooltips are currently being displayed then it hides them all
        if (showingTooltips) {
            tooltip1.hide();
            tooltip2.hide();

            //Tooltip3 might not exist and hence be a null value. By enclosing this in a try catch, it prevents the program from crashing if this is the case
            try {
                tooltip3.hide();
            } catch (Exception e) {
            }

            //Indicates that tooltips are currently not being displayed
            showingTooltips = false;
        }
    }

}
