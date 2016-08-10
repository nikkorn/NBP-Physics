package com.dumbpug.main.gamedevicestesting.maps;

import java.util.ArrayList;
import org.json.*;
import com.dumbpug.main.gamedevicestesting.C;

/**
 * Represents a map.
 * Each map is 29x22 tiles in size.
 * @author nikolas.howard
 *
 */
public class Map {
	// List of all map tiles. Starts from Bottom-Left and goes X before Y.
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
		// TODO Error if tile count is wrong.
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
	
	/**
	 * Get the tile value at a position.
	 * @param x
	 * @param y
	 * @return Tile value.
	 */
	public int getTileValueAtPosition(int x, int y){
		// Need to convert our X and Y cords to an index in our tile list.
		int tileListIndex = 0;
		tileListIndex += ((C.WORLD_TILE_HEIGHT - 1) - y) * C.WORLD_TILE_WIDTH;
		tileListIndex += x;
		return tiles.get(tileListIndex);
	}
	
	/**
	 * Get all weapon points.
	 * @return weapon points.
	 */
	public ArrayList<WeaponPoint> getWeaponPoints() {
		return this.weaponPoints;
	}
}
