package com.dumbpug.main.gamedevicestesting.player;

import java.util.HashMap;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;

/**
 * A players weapon inventory.
 * @author nikolas.howard
 *
 */
public class PlayerWeaponInventory {
	// Map of weapon types to the amount of ammunition carried by the player.
	private HashMap<WeaponType, Integer> weaponInventory = new HashMap<WeaponType, Integer>();
	// The active weapon type.
	private WeaponType activeWeaponType = null;
	
	/**
	 * Default constructor.
	 */
	PlayerWeaponInventory() {
		// The player will be assigned an ammo level of 0 for each weapon type.
		for(WeaponType weaponType : WeaponType.values()) {
			weaponInventory.put(weaponType, 0);
		}
	}
	
	/**
	 * Constructor where we set the initial active weapon.
	 */
	PlayerWeaponInventory(WeaponType activeWeaponType) {
		// The player will be assigned an ammo level of 0 for each weapon type.
		for(WeaponType weaponType : WeaponType.values()) {
			weaponInventory.put(weaponType, 0);
		}
		// Set the active weapon type.
		this.setActiveWeaponType(activeWeaponType);
	}
	
	public void goForwardToNextWeaponWithAmmunition() {
		if(!doesWeaponWithAmmoExist()) {
			// There are no weapons with ammo.
			return;
		}
		// Get current position in inventory
		int inventoryPos = (activeWeaponType != null) ? activeWeaponType.ordinal() : 0;
		// TODO Check weapon ammo counts after the current position.
		// TODO Check weapon ammo counts before the current position.
	}
	
    public void goBackwardToNextWeaponWithAmmunition() {
    	if(!doesWeaponWithAmmoExist()) {
    		// There are no weapons with ammo.
			return;
		}
    	// Get current position in inventory
		int inventoryPos = (activeWeaponType != null) ? activeWeaponType.ordinal() : 0;
		// TODO Check weapon ammo counts before the current position.
		// TODO Check weapon ammo counts after the current position.
	}
    
    /**
     * Return true if we have any ammo for any weapons.
     * @return have ammo for any weapons.
     */
    private boolean doesWeaponWithAmmoExist() {
    	for(Integer ammoCount : weaponInventory.values()) {
    		if(ammoCount != 0) {
    			// We found a weapon with ammo.
    			return true;
    		}
    	}
    	// We did not find any ammo.
    	return false;
    }
	
	/**
	 * Set the level of ammunition for a weapon.
	 * @param weaponType
	 * @param ammo
	 */
	public void setWeaponAmmunition(WeaponType weaponType, int ammo) {
		weaponInventory.put(weaponType, ammo);
	}
	
	/**
	 * Get the level of ammunition for a weapon.
	 * @param weaponType
	 * @return ammunition.
	 */
	public int getWeaponAmmunition(WeaponType weaponType) {
		return weaponInventory.get(weaponType);
	}

	/**
	 * Gets the active weapon type.
	 * @return active weapon type.
	 */
	public WeaponType getActiveWeaponType() {
		return activeWeaponType;
	}

	/**
	 * Sets the active weapon type.
	 * @param activeWeaponType
	 */
	public void setActiveWeaponType(WeaponType activeWeaponType) {
		this.activeWeaponType = activeWeaponType;
	}
}
