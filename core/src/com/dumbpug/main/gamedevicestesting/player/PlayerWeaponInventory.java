package com.dumbpug.main.gamedevicestesting.player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;

/**
 * A players weapon inventory.
 * @author nikolas.howard
 *
 */
public class PlayerWeaponInventory {
	// Map of weapon types to the amount of ammunition carried by the player.
	private LinkedHashMap<WeaponType, Integer> weaponInventory = new LinkedHashMap<WeaponType, Integer>();
	// Keys of inventory item positions.
	private List<WeaponType> inventoryKeys;
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
		// Get keys of inventory item positions.
		inventoryKeys = new ArrayList<WeaponType>(weaponInventory.keySet());
		// Default the active weapon type to be the first.
		this.setActiveWeaponType(inventoryKeys.get(0));
	}
	
	/**
	 * Constructor where we set the initial active weapon.
	 */
	PlayerWeaponInventory(WeaponType activeWeaponType) {
		// The player will be assigned an ammo level of 0 for each weapon type.
		for(WeaponType weaponType : WeaponType.values()) {
			weaponInventory.put(weaponType, 0);
		}
		// Get keys of inventory item positions.
		inventoryKeys = new ArrayList<WeaponType>(weaponInventory.keySet());
		// Set the active weapon type.
		this.setActiveWeaponType(activeWeaponType);
	}
	
	/**
	 * Iterates forwards through the inventory until we find a weapon with ammo and sets this as active
	 * @return weapon with ammo available
	 */
	public boolean goForwardToNextWeaponWithAmmunition() {
		if(!doesWeaponWithAmmoExist()) {
			// There are no weapons with ammo.
			return false;
		}
		// Get current position in inventory
		int inventoryPos = (activeWeaponType != null) ? activeWeaponType.ordinal() : 0;
		// Check weapon ammo counts after the current position.
		for(int inventoryIndex = (inventoryPos + 1); inventoryIndex < weaponInventory.size(); inventoryIndex++) {
			if(weaponInventory.get(inventoryKeys.get(inventoryIndex)) != 0) {
				this.setActiveWeaponType(inventoryKeys.get(inventoryIndex));
				return true;
			}
		}
		// Check weapon ammo counts up to the current position.
		for(int inventoryIndex = 0; inventoryIndex <= inventoryPos; inventoryIndex++) {
			if(weaponInventory.get(inventoryKeys.get(inventoryIndex)) != 0) {
				this.setActiveWeaponType(inventoryKeys.get(inventoryIndex));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Iterates backwards through the inventory until we find a weapon with ammo and sets this as active
	 * @return weapon with ammo available
	 */
    public boolean goBackwardToNextWeaponWithAmmunition() {
    	if(!doesWeaponWithAmmoExist()) {
    		// There are no weapons with ammo.
			return false;
		}
    	// Get current position in inventory
		int inventoryPos = (activeWeaponType != null) ? activeWeaponType.ordinal() : 0;
		// Check weapon ammo counts before the current position.
		for(int inventoryIndex = (inventoryPos - 1); inventoryIndex >= 0; inventoryIndex--) {
			if(weaponInventory.get(inventoryKeys.get(inventoryIndex)) != 0) {
				this.setActiveWeaponType(inventoryKeys.get(inventoryIndex));
				return true;
			}
		}
		// Check weapon ammo counts after the current position.
		for(int inventoryIndex = (weaponInventory.size() - 1); inventoryIndex >= inventoryPos; inventoryIndex--) {
			if(weaponInventory.get(inventoryKeys.get(inventoryIndex)) != 0) {
				this.setActiveWeaponType(inventoryKeys.get(inventoryIndex));
				return true;
			}
		}
		return false;
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
		if(activeWeaponType != this.activeWeaponType) {
			System.out.println("Weapon Set To: " + activeWeaponType);
		}
		this.activeWeaponType = activeWeaponType;
	}
}
