package gameLogic.replay;

import java.util.ArrayList;
import java.util.List;

public class ReplayManager {
    List<String> clicks = new ArrayList<String>();

    public void addClick(String actorId) {
        clicks.add(actorId);
    }
}