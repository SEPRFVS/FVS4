package test;


import gameLogic.map.Position;
import gameLogic.map.Station;
import gameLogic.resource.Train;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TrainTest {
    Train train;
    Station station1;
    Station station2;
    ArrayList<Station> route;

	@Before
    public void trainSetup() throws Exception {
        train = new Train("RedTrain", "NuclearTrain.png", 250);
        station1 = new Station("station1", new Position(5, 5));
        station2 = new Station("station2", new Position(6, 6));
        route = new ArrayList<Station>();
        route.add(station1);
        route.add(station2);
    }

    @Test
    public void finalDestinationTest() throws Error {
        ArrayList<Station> route = new ArrayList<Station>();
        route.add(station1);
        route.add(station2);

        train.setRoute(route);
        assertTrue("Setting a train route was not succesful", train.getRoute().size() == 2);
        assertTrue("Final destination wasn't set", train.getFinalDestination() == station2);
    }
    
    @Test
    public void rotationTest() {
    	train.setRoute(route);
    	train.addHistory(station1, 0);
    	double angle = 360.0f - Math.toDegrees(Math.atan2(train.getNextStation().getLocation().getX() - train.getLastStation().getLocation().getX(), train.getNextStation().getLocation().getY() - train.getLastStation().getLocation().getY()));
    	assertTrue(315.0f == (float) angle);
    }


}
