package fvs.taxe.dialog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.MainMenuScreen;
import fvs.taxe.ReplayScreen;
import fvs.taxe.TaxeGame;
import fvs.taxe.controller.Context;
import gameLogic.player.Player;
import gameLogic.player.PlayerManager;

public class DialogEndGame extends UnifiedDialog {
    private TaxeGame game;
    private Context context;

    public DialogEndGame(TaxeGame game, PlayerManager pm, Skin skin, Context context) {
        super("GAME OVER", skin, "greenwin");
        this.game = game;
        this.context = context;

        double highScore = 0;
        int playerNum = 0;
        for (Player player : pm.getAllPlayers()) {
            //Checks each player's score
            if (player.getScore() > highScore) {
                highScore = player.getScore();
                //Need to add one as playerNumber is 0-based indexing
                playerNum = player.getPlayerNumber();
            }else if (player.getScore() == highScore){
                playerNum =0;
            }
        }

        //Declares the winner based on who received the highest score
        //If adding multiple players then this would need to be changed to reflect that
        if (playerNum != 0) {
            text("PLAYER " + playerNum + " WINS!");
        } else {
            //If no player has the high score then a tie is declared
            text("IT'S A TIE!");
        }
        
        button("Main Menu", "MENU");
        getButtonTable().row();
        button("Replay", "REPLAY");
        getButtonTable().row();
        button("Exit", "EXIT");
    }

    @Override
    protected void result(Object obj) {
        if (obj == "EXIT") {
            //Closes the app and disposes any machine resources used
            Gdx.app.exit();
        } else if (obj == "REPLAY") {
        	//Use copy of code from Start Replay button (could reference in the future)
        	ArrayList<String> playerNames = new ArrayList<String>();
            for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
            	playerNames.add(player.getName());
            }

			context.getReplayManager().setGame(context.getGameLogic());
            context.getTaxeGame().setScreen(new ReplayScreen(context.getTaxeGame(), context.getReplayManager(), playerNames, context.getGameLogic().getTotalTurns()));
            cancel();
        } else {
        	game.setScreen(new MainMenuScreen(game));
        }
    }
}
