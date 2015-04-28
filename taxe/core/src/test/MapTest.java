package test;

import gameLogic.map.Map;
import gameLogic.map.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MapTest extends LibGdxTest {
    private Map map;

    @Before
    public void mapSetup() throws Exception {
        map = new Map();
    }

    @Test
    public void addStationAndConnectionTest() throws Exception {
        String name1 = "station1";
        String name2 = "station2";

        int previousSize = map.getStations().size();

        map.addStation(name1, new Position(9999, 9999));
        map.addStation(name2, new Position(200, 200));

        assertTrue("Failed to add stations", map.getStations().size() - previousSize == 2);

        map.addConnection(name1, name2);
        assertTrue("Connection addition failed", map.doesConnectionExist(name2, name1));

        // Should throw an error by itself
        map.getStationFromPosition(new Position(9999, 9999));
    }
    
    @Test
    public void dijkstraUpdateTest() {
    	map.addStation("station1", new Position(100, 100));
    	map.addStation("station2", new Position(200, 200));
    	map.addStation("station3", new Position(200, 100));
    	
    	map.addConnection("station1", "station3");
    	map.addConnection("station3", "station2");
    	
    	map.updateDijkstra();
    	
    	assertTrue(map.getShortestDistance(map.getStationByName("station1"), map.getStationByName("station2")) == 200);
    	
    	map.addConnection("station1", "station2");
    	
    	map.updateDijkstra();
    	
    	//Use int as game works based on pixels so decimals not needed
    	assertTrue((int) map.getShortestDistance(map.getStationByName("station1"), map.getStationByName("station2")) == (int) Math.sqrt(Math.pow(100, 2) * 2));
    }
}
