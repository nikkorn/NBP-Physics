package com.dumbpug.main.gamedevicestesting.maps;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class UTLocalMaps {

	@Test
	public void getTestMap() {
		ArrayList<Map> maps = LocalMaps.getLocalMaps();
		assertTrue(maps.size() > 0 );
	}
}
