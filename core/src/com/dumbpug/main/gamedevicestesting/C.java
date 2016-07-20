package com.dumbpug.main.gamedevicestesting;

/**
 * Game constants.
 * @author Nikolas Howard
 *
 */
public class C {
	// World
	public static final float WORLD_GRAVITY                       = 0.09f;
	public static final float WORLD_TILE_SIZE                     = 20f;
	
	// Player
	public static final float PLAYER_SIZE_WIDTH                   = 16f;
	public static final float PLAYER_SIZE_HEIGHT                  = 38f;
	public static final float PLAYER_MAX_VELOCITY                 = 5f;
	public static final float PLAYER_FRICTION                     = 0.92f;
	public static final float PLAYER_RESTITUTION                  = 0f;
	public static final float PLAYER_MAX_WALKING_VELOCITY         = 2f;
	public static final float PLAYER_WALKING_IMPULSE_VALUE        = 0.2f;
	public static final float PLAYER_JUMPING_IMPULSE              = 3.5f;
	public static final long PLAYER_WEAPON_COOLDOWN               = 500l;
	
	public static final int PLAYER_MAX_HEALTH                     = 10;
	
	// Grenade
	public static final float GRENADE_SIZE                        = 10f;
	public static final float GRENADE_FRICTION                    = 0.6f;
	public static final float GRENADE_RESTITUTION                 = 0.6f;
	public static final float GRENADE_MAX_VELOCITY                = 6f;
	public static final float GRENADE_INITIAL_VELOCITY            = 6f;
	public static final float GRENADE_EXPLOSION_FORCE             = 6f;
	public static final float GRENADE_EXPLOSION_RADIUS            = 60f;
	public static final long GRENADE_FUSE_MAX                     = 5000l;
	public static final long GRENADE_FUSE_MED                     = 3000l;
	public static final long GRENADE_FUSE_MIN                     = 1500l;
	
	// Rubber Grenade
	public static final float RUBBER_GRENADE_FRICTION             = 0.96f;
	public static final float RUBBER_GRENADE_RESTITUTION          = 0.96f;
	public static final float RUBBER_GRENADE_MAX_VELOCITY         = 7f;
	public static final float RUBBER_GRENADE_INITIAL_VELOCITY     = 7f;
	
	// Cluster Grenade
	public static final float GRENADE_CLUSTER_SIZE                = GRENADE_SIZE / 2f;
	public static final float GRENADE_CLUSTER_FORCE               = GRENADE_EXPLOSION_FORCE / 2f;
	public static final float GRENADE_CLUSTER_RADIUS              = GRENADE_EXPLOSION_RADIUS / 2f;
	
	// Mine
	public static final float MINE_SIZE                           = 10f;
	public static final float MINE_MAX_VELOCITY                   = 6f;
	public static final float MINE_INITIAL_VELOCITY               = 6f;
	public static final float MINE_FRICTION                       = 0f;
	public static final float MINE_RESTITUTION                    = 0f;
	
	// Proximity Mine
	public static final float PROXIMITY_MINE_REACH                = 30f;
	public static final float PROXIMITY_MINE_FORCE                = 10f;
	public static final float PROXIMITY_MINE_RADIUS               = 50f;
	
	// Rocket
	public static final float ROCKET_MAX_VELOCITY                 = 8f;
	public static final float ROCKET_INITIAL_VELOCITY             = 8f;
}
