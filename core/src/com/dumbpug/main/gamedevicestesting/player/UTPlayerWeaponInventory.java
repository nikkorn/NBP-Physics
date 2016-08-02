package com.dumbpug.main.gamedevicestesting.player;

import static org.junit.Assert.*;
import org.junit.Test;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;

public class UTPlayerWeaponInventory {

	@Test
	public void canCreateAnEmptyInventory() {
		PlayerWeaponInventory inventory = new PlayerWeaponInventory(); 
		assertTrue(inventory.getWeaponAmmunition(WeaponType.GRENADE) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.CLUSTER_GRENADE) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.LASER_MINE) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.ROCKET) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.RUBBER_GRENADE) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.STICKY_GRENADE) == 0);
		assertTrue(inventory.getWeaponAmmunition(WeaponType.PROXIMITY_MINE) == 0);
	}
	
	@Test
	public void canCycleForwardsThroughInventory() {
		PlayerWeaponInventory inventory = new PlayerWeaponInventory(); 
		inventory.setWeaponAmmunition(WeaponType.GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.STICKY_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.CLUSTER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.RUBBER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.PROXIMITY_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.LASER_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.ROCKET, 1);
		// Set the active weapon.
		inventory.setActiveWeaponType(WeaponType.GRENADE);
		// Cycle through inventory.
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.STICKY_GRENADE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.CLUSTER_GRENADE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.RUBBER_GRENADE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.PROXIMITY_MINE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.LASER_MINE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.ROCKET);
		// Check that we loop back round.
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.GRENADE);
	}
	
	@Test
	public void canCycleForwardsThroughInventoryWithGaps() {
		PlayerWeaponInventory inventory = new PlayerWeaponInventory(); 
		inventory.setWeaponAmmunition(WeaponType.GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.CLUSTER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.PROXIMITY_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.ROCKET, 1);
		// Set the active weapon.
		inventory.setActiveWeaponType(WeaponType.GRENADE);
		// Cycle through inventory.
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.CLUSTER_GRENADE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.PROXIMITY_MINE);
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.ROCKET);
		// Check that we loop back round.
		inventory.goForwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.GRENADE);
	}
	
	@Test
	public void canCycleBackwardsThroughInventory() {
		PlayerWeaponInventory inventory = new PlayerWeaponInventory(); 
		inventory.setWeaponAmmunition(WeaponType.GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.STICKY_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.CLUSTER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.RUBBER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.PROXIMITY_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.LASER_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.ROCKET, 1);
		// Set the active weapon.
		inventory.setActiveWeaponType(WeaponType.ROCKET);
		// Cycle through inventory.
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.LASER_MINE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.PROXIMITY_MINE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.RUBBER_GRENADE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.CLUSTER_GRENADE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.STICKY_GRENADE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.GRENADE);
		// Check that we loop back round.
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.ROCKET);
	}
	
	@Test
	public void canCycleBackwardsThroughInventoryWithGaps() {
		PlayerWeaponInventory inventory = new PlayerWeaponInventory(); 
		inventory.setWeaponAmmunition(WeaponType.GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.CLUSTER_GRENADE, 1);
		inventory.setWeaponAmmunition(WeaponType.PROXIMITY_MINE, 1);
		inventory.setWeaponAmmunition(WeaponType.ROCKET, 1);
		// Set the active weapon.
		inventory.setActiveWeaponType(WeaponType.ROCKET);
		// Cycle through inventory.
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.PROXIMITY_MINE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.CLUSTER_GRENADE);
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.GRENADE);
		// Check that we loop back round.
		inventory.goBackwardToNextWeaponWithAmmunition();
		assertTrue(inventory.getActiveWeaponType() == WeaponType.ROCKET);
	}
}