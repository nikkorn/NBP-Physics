package com.dumbpug.main.gamedevicestesting.maps;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;

/**
 * Represents a tile upon which weapon ammo pickups will be generated.
 * @author nikolas.howard
 *
 */
public class WeaponPoint {
	// X tile position.
	private int tilePositionX = 0;
	// Y tile position.
	private int tilePositionY = 0;
	// last pickup time.
	private long lastPickup   = 0l;
	// Weapon ammo generation token count.
	private HashMap<WeaponType, Integer> ammoGenerationTokens = new HashMap<WeaponType, Integer>();

	/**
	 * Build our weapon point from the raw JSON.
	 * @param weaponPointJson
	 */
	public WeaponPoint(JSONObject weaponPointJson){
		try {
			// Get position.
			tilePositionX = weaponPointJson.getInt("x");
			tilePositionY = weaponPointJson.getInt("y");
			// TODO Get ammo generation tokens.
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getTilePositionX() {
		return tilePositionX;
	}

	public int getTilePositionY() {
		return tilePositionY;
	}
}
