package com.dumbpug.main.gamedevicestesting.maps;

import org.json.JSONException;
import org.json.JSONObject;
import com.dumbpug.nbp.NBPPoint;

/**
 * Represents a player spawn point.
 * @author nikolas.howard
 *
 */
public class SpawnPoint extends NBPPoint {

	/**
	 * Build our spawn point from the raw JSON.
	 * @param spawnPointJson
	 * @throws JSONException 
	 */
	public SpawnPoint(JSONObject spawnPointJson) throws JSONException {
		super(spawnPointJson.getInt("x"), spawnPointJson.getInt("y"));
	}
}
