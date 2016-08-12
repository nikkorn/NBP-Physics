package com.dumbpug.main.gamedevicestesting.player;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.input.IPlayerInput;
import com.dumbpug.main.gamedevicestesting.maps.SpawnPoint;
import com.dumbpug.main.gamedevicestesting.stage.Stage;
import com.dumbpug.main.gamedevicestesting.weapons.WeaponType;
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
    // The provider for player input.
    private IPlayerInput playerInput = null;

    public Player(float x, float y, int playerNumber) {
        // Initialise a default player weapon inventory.
        playerWeaponInventory = new PlayerWeaponInventory();
        // Initialise our players physics box.
        playerPhysicsBox = new PlayerBox(this, x, y, C.PLAYER_SIZE_WIDTH, C.PLAYER_SIZE_HEIGHT, NBPBoxType.KINETIC);
        playerPhysicsBox.setName("PLAYER_ " + playerNumber);
    }
    
    /**
     * Process a players input.
     * @param stage The active stage which called this method. 
     */
	public void processInput(Stage activeStage) {
		// Get the player's input provider (if they have one).
		IPlayerInput playerInputProvider = getPlayerInputProvider();
		
		// Do they have one? If not then we cannot do anything.
		if(playerInputProvider == null) 
			return;
		
		// Set the players angle of focus.
 		setAngleOfFocus(playerInputProvider.getAngleOfFocus());
 		
 		// Check for presses of number 1-7, this represents a change in active weapon.
 		if(playerInputProvider.isNum1Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.GRENADE);
 		}
 		if(playerInputProvider.isNum2Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.STICKY_GRENADE);
 		}
 		if(playerInputProvider.isNum3Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.CLUSTER_GRENADE);
 		}
 		if(playerInputProvider.isNum4Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.RUBBER_GRENADE);
 		}
 		if(playerInputProvider.isNum5Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.PROXIMITY_MINE);
 		}
 		if(playerInputProvider.isNum6Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.LASER_MINE);
 		}
 		if(playerInputProvider.isNum7Pressed()) {
 			getPlayerWeaponInventory().setActiveWeaponType(WeaponType.ROCKET);
 		}
        
        // Only allow player to do stuff while he is alive.
        if(!isAlive()) {
        	return;
        } 
        	
    	// Test player horizontal movement.
        if (playerInputProvider.isLeftPressed()) {
            moveLeft();
        } else if (playerInputProvider.isRightPressed()) {
            moveRight();
        }

        // Test player jump
        if (playerInputProvider.isUpPressed()) {
            jump();
        }
        
        // Handle weapon fire.
        if(playerInputProvider.isLeftMouseButtonDown()) {
        	// Can the player even fire his weapon? (cool-down over)
        	if(canFireWeapon()) {
        		// Fire our weapon.
        		fireWeapon(activeStage); 
        	}
        }
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
     * Fire our weapon.
     * @param activeStage
     */
    private void fireWeapon(Stage activeStage) {
    	// Get the players active weapon.
		WeaponType activeWeaponType = getPlayerWeaponInventory().getActiveWeaponType();
		// Does the player have the required ammo?
		int ammoCount = getPlayerWeaponInventory().getWeaponAmmunition(activeWeaponType);
		if(ammoCount != 0) {
			// An ammo count of -1 represents infinite ammo, if we don't have infinite ammo 
			// then we need to remove some ammo from the inventory.
			if(ammoCount != -1) {
				getPlayerWeaponInventory().setWeaponAmmunition(activeWeaponType, ammoCount - 1);
			}
			// We can fire now!
			activeStage.weaponFiredByPlayer(this);
    		// We have fired our weapon, restart the weapon cool-down.
			this.lastFireTime = System.currentTimeMillis();
		} 
    }
    
    /**
     * Move this player directly to the the provided spawn point.
     * @param spawnPoint
     */
    public void moveToSpawnPoint(SpawnPoint spawnPoint) {
    	playerPhysicsBox.setX(spawnPoint.getX());
    	playerPhysicsBox.setY(spawnPoint.getY());
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

	/**
	 * Get the player input provider.
	 * @return player input provider.
	 */
	public IPlayerInput getPlayerInputProvider() {
		return playerInput;
	}
	
	/**
	 * Set the player input provider.
	 * @param playerInput
	 */
	public void setPlayerInputProvider(IPlayerInput playerInput) {
		this.playerInput = playerInput;
	}
}
