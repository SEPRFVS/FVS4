package test;

import static org.junit.Assert.*;
import gameLogic.replay.ReplayManager;

import org.junit.Before;
import org.junit.Test;

public class ReplayTest {
	private ReplayManager replayManager;
	
	@Before
	public void setUp() {
		replayManager = new ReplayManager();
	}
	
	@Test
	public void addClickToReplayTest() {
		String actorId = "1234";
		
		assertTrue(replayManager.isEnd());
		replayManager.addClick(actorId);
		assertFalse(replayManager.isEnd());
	}
}
