package fvs.taxe.clickListener;

import fvs.taxe.Button;
import fvs.taxe.clickListener.ResourceDialogClickListener;
import gameLogic.player.Player;
import gameLogic.goal.Goal;

public class DialogGoalButtonClicked implements ResourceDialogClickListener {
    private Player currentPlayer;
    private Goal goal;

    public DialogGoalButtonClicked(Player player, Goal goal) {
        this.currentPlayer = player;
        this.goal = goal;
    }

    @SuppressWarnings("incomplete-switch")
	@Override
    public void clicked(Button button) {
        switch (button) {
            case GOAL_DROP:
                currentPlayer.removeGoal(goal);
                //simulate mouse exiting goal button to remove tooltips

                break;
        }
    }
}
