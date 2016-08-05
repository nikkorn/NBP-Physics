package com.dumbpug.main.gamedevicestesting.player;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPPoint;

/**
 * Represents a player.
 * Created by nik on 28/02/16.
 */
public class Player {
    // Health of the player
    private int health = C.PLAYER_MAX_HEALTH; 
    // Is the player alive?
    private boolean isAlive = true;
    // Time that the player last fired a weapon
    private long lastFireTime = System.currentTimeMillis();
    // The angle of focus for this player (where we are looking).
    private float angleOfFocus = 0f;
    // The weapon inventory of this player.
    private PlayerWeaponInventory playerWeaponInventory;
    // The physics box for this player.
    private PlayerBox playerPhysicsBox;

    public Player(float x, float y, float width, float height, int playerNumber) {
        // Initialise a default player weapon inventory.
        playerWeaponInventory = new PlayerWeaponInventory();
        // Initialise our players physics box.
        playerPhysicsBox = new PlayerBox(this, x, y, width, height, NBPBoxType.KINETIC);
        playerPhysicsBox.setName("PLAYER_ " + playerNumber);
    }
    
    /**
     * Move the player to the left.
     */
    public void moveLeft() {
    	playerPhysicsBox.moveLeft();
    }

    /**
     * Move the player to the right.
     */
    public void moveRight() {
    	playerPhysicsBox.moveRight();
    }

    /**
     * Make the player jump if he can.
     */
    public void jump() {
    	playerPhysicsBox.jump();
    }
    
    /**
     * Is the player still alive (has health greater than 0).
     * @return isAlive
     */
    public boolean isAlive() {
    	return this.isAlive;
    }
    
    /**
     * Returns whether the player can fire their weapon (cooldown has finished).
     * @return can fire weapon
     */
    public boolean canFireWeapon() {
    	return (System.currentTimeMillis() - this.lastFireTime) >= C.PLAYER_WEAPON_COOLDOWN;
    }
    
    /**
     * Called when player fires weapon to reset the cooldown.
     */
    public void restartWeaponCooldown() {
    	this.lastFireTime = System.currentTimeMillis();
    }

	/**
	 * Get the angle of focus for this player.
	 * @return angle of focus.
	 */
	public float getAngleOfFocus() {
		return angleOfFocus;
	}

	/**
	 * Set the angle of focus for this player.
	 * @param angle of focus.
	 */
	public void setAngleOfFocus(float angleOfFocus) {
		this.angleOfFocus = angleOfFocus;
	}

	/**
	 * Get this players weapon inventory.
	 * @return playerWeaponInventory
	 */
	public PlayerWeaponInventory getPlayerWeaponInventory() {
		return playerWeaponInventory;
	}

	/**
	 * Set this players weapon inventory.
	 * @param playerWeaponInventory
	 */
	public void setPlayerWeaponInventory(PlayerWeaponInventory playerWeaponInventory) {
		this.playerWeaponInventory = playerWeaponInventory;
	}
	
	/**
	 * Deal damage to this player, reducing health and possibly killing them.
	 * @param pointsOfDamage
	 */
	public void dealDamage(int pointsOfDamage) {
		// Remove health.
		this.health -= pointsOfDamage;
		if(health <= 0) {
			// Can't have minus number for health. 
			health = 0;
			// The player is no longer alive.
			this.isAlive = false;
		} 
	}
	
	/**
	 * Get this players health.
	 * @return health
	 */
	public int getPlayerHealth() {
		return this.health;
	}
	
	/**
	 * Set this players health.
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**
	 * Get this players physics box.
	 * @return players physics box.
	 */
	public PlayerBox getPlayerPhysicsBox() {
		return this.playerPhysicsBox;
	}
	
	/**
	 * Get this players current point of origin.
	 * @return players point of origin.
	 */
	public NBPPoint getCurrentOriginPoint() {
		// This players point of origin will match the origin of its physics box.
		return playerPhysicsBox.getCurrentOriginPoint();
	}
}
