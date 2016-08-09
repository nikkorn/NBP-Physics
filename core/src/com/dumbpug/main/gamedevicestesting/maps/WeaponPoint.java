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
	// Weapon ammo generation token count.
	private HashMap<WeaponType, Integer> ammoGenerationTokens = new HashMap<WeaponType, Integer>();

	/**
	 * Build our weapon point from the raw JSON.
	 * @param weaponPointJson
	 * @throws JSONException 
	 */
	public WeaponPoint(JSONObject weaponPointJson) throws JSONException{
		// Get position.
		tilePositionX = weaponPointJson.getInt("x");
		tilePositionY = weaponPointJson.getInt("y");
		// Get ammo generation tokens from the JSON.
		JSONObject weaponAmmoTokens = weaponPointJson.getJSONObject("weapon_tokens");
		for(WeaponType weaponType : WeaponType.values()) {
			String weaponTypeName = weaponType.toString();
			// If a weapon type token count is not defined in the JSON then it simply gets zero tokens.
			int tokensForWeaponType = (weaponAmmoTokens.has(weaponTypeName)) ? weaponAmmoTokens.getInt(weaponTypeName) : 0;
			ammoGenerationTokens.put(weaponType, tokensForWeaponType);
		}  
	}

	public int getTilePositionX() {
		return tilePositionX;
	}

	public int getTilePositionY() {
		return tilePositionY;
	}
}
