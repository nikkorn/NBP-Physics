package com.dumbpug.main.gamedevicestesting.maps;

import java.util.ArrayList;
import org.json.*;

/**
 * Represents a map.
 * Each map is 29x22 tiles in size.
 * @author nikolas.howard
 *
 */
public class Map {
	// List of all map tiles.
	private ArrayList<Integer> tiles = new ArrayList<Integer>();
	// List of all Weapon Points.
	private ArrayList<WeaponPoint> weaponPoints = new ArrayList<WeaponPoint>();
	// Store the original JSON.
	private JSONObject rawMapJSON;
	// The name of the map.
	private String mapName;
	
	/**
	 * Build our map from the raw map JSON.
	 * @param mapJson
	 * @param mapName
	 * @throws JSONException 
	 */
	public Map(JSONObject mapJson, String mapName) throws JSONException{
		// Store the raw map JSON.
		this.rawMapJSON = mapJson;
		// Store the map name.
		this.mapName = mapName;
		// Get our tiles from the JSON.
		readTilesFromMapJSON();
		// Get our weapon points from the JSON.
		readWeaponPointsFromMapJSON();
	}

	/**
	 * Get our tiles from the JSON.
	 * @throws JSONException 
	 */
	private void readTilesFromMapJSON() throws JSONException {
		JSONArray tilesArray = rawMapJSON.getJSONArray("tiles");
		for (int i = 0; i < tilesArray.length(); i++)
		{
			tiles.add(tilesArray.getInt(i));
		}
	}
	
	/**
	 * Get our weapon points from the JSON.
	 * @throws JSONException 
	 */
	private void readWeaponPointsFromMapJSON() throws JSONException {
		JSONArray weaponPointArray = rawMapJSON.getJSONArray("weapon_points");
		for (int i = 0; i < weaponPointArray.length(); i++)
		{
			weaponPoints.add(new WeaponPoint(weaponPointArray.getJSONObject(i)));
		}
	}

	/**
	 * Get the raw map JSON.
	 * @return raw map JSON.
	 */
	public JSONObject getRawMapJSON() {
		return rawMapJSON;
	}

	/**
	 * Get the name of this map.
	 * @return map name.
	 */
	public String getMapName() {
		return mapName;
	}
	
	// TODO public int getTileValueAtPosition(int x, int y);
	// TODO public WeaponPoint getWeaponPointAtPosition(int x, int y);
}
