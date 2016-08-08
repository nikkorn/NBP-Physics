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
	
	/**
	 * Build our map from the raw map JSON.
	 * @param mapJson
	 */
	public Map(JSONObject mapJson){
		// Store the raw map JSON.
		this.setRawMapJSON(mapJson);
		// Get our tiles from the JSON.
		try {
			JSONArray tilesArray = mapJson.getJSONArray("tiles");
			for (int i = 0; i < tilesArray.length(); i++)
			{
				tiles.add(tilesArray.getInt(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Get our weapon points from the JSON.
		try {
			JSONArray weaponPointArray = mapJson.getJSONArray("weapon_points");
			for (int i = 0; i < weaponPointArray.length(); i++)
			{
				weaponPoints.add(new WeaponPoint(weaponPointArray.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// public int getTileValueAtPosition(int x, int y);
	// public WeaponPoint getWeaponPointAtPosition(int x, int y);

	/**
	 * Set the raw map JSON.
	 * @return raw map JSON.
	 */
	public JSONObject getRawMapJSON() {
		return rawMapJSON;
	}

	/**
	 * Get the raw map JSON.
	 * @param rawMapJSON
	 */
	public void setRawMapJSON(JSONObject rawMapJSON) {
		this.rawMapJSON = rawMapJSON;
	}
}
