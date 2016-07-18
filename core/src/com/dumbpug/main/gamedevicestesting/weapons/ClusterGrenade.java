package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.PlayerBox;
import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

/**
 * Represents a grenade.
 * @author nikolas.howard
 *
 */
public class ClusterGrenade extends NBPBox {
	// The time the grenade was thrown.
	private long timeThrownInMillis;
	// Fuse time of the grenade.
	private long fuseTimeMillis;
	// The player who threw the grenade.
	private PlayerBox owner;
	
	   
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public ClusterGrenade(PlayerBox owner, long fuseTimeMillis) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), C.GRENADE_SIZE, C.GRENADE_SIZE, NBPBoxType.KINETIC);
        // Set the grenade fuse time.
        this.fuseTimeMillis = fuseTimeMillis;
        // Set the thrown time.
        this.timeThrownInMillis = System.currentTimeMillis();
        // Set various properties for the grenade.
        setName("CLUSTER_GRENADE");
        setFriction(C.GRENADE_FRICTION);
        setRestitution(C.GRENADE_RESTITUTION);
        // Set max velocity. 
        setMaxVelocityX(C.GRENADE_MAX_VELOCITY);
        setMaxVelocityY(C.GRENADE_MAX_VELOCITY);
        // Set owner
        this.owner = owner;
        // Apply initial impulse in the players facing direction
        this.applyImpulse(owner.facingRight ? C.GRENADE_INITIAL_VELOCITY : -C.GRENADE_INITIAL_VELOCITY, 0f);
    }
    
    /**
     * Get the player who threw this.
     * @return thrower
     */
    public PlayerBox getOwner() {
    	return this.owner;
    }

	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onBeforeUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onAfterUpdate() {
		// Has the fuse run out?
    	if ((System.currentTimeMillis() - timeThrownInMillis) >= fuseTimeMillis) {
    		float clusterSegmentOffset = C.GRENADE_CLUSTER_SIZE / 2f;
    		// Create 4 new grenades.
    		ClusterGrenadeSegment g1 = new ClusterGrenadeSegment(this.owner, fuseTimeMillis);
    		g1.setX(this.getX() + clusterSegmentOffset);
    		g1.setY(this.getY() + clusterSegmentOffset);
    		
    		ClusterGrenadeSegment g2 = new ClusterGrenadeSegment(this.owner, fuseTimeMillis);
    		g2.setX(this.getX() + clusterSegmentOffset);
    		g2.setY(this.getY() - clusterSegmentOffset);
    		
    		ClusterGrenadeSegment g3 = new ClusterGrenadeSegment(this.owner, fuseTimeMillis);
    		g3.setX(this.getX() -  clusterSegmentOffset);
    		g3.setY(this.getY() + clusterSegmentOffset);
    		
    		ClusterGrenadeSegment g4 = new ClusterGrenadeSegment(this.owner, fuseTimeMillis);
    		g4.setX(this.getX() - clusterSegmentOffset);
    		g4.setY(this.getY() - clusterSegmentOffset);
    		
    		// Explode! Create a world bloom.
    		NBPBloom grenadeExplosionBloom = new NBPBloom(owner, this.getCurrentOriginPoint().getX(), 
    				this.getCurrentOriginPoint().getY(), C.GRENADE_EXPLOSION_RADIUS, C.GRENADE_EXPLOSION_FORCE);
    		this.getWrappingWorld().addBloom(grenadeExplosionBloom);
    		// Add everything to our physics world
    		this.getWrappingWorld().addBox(g1);
    		this.getWrappingWorld().addBox(g2);
    		this.getWrappingWorld().addBox(g3);
    		this.getWrappingWorld().addBox(g4);
    		this.getWrappingWorld().addBloom(grenadeExplosionBloom);
    		// Remove this grenade box on next update.
    		this.markForDeletion();
    	}
	}

	@Override
	protected void onDeletion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) {
		return true;
	}
}
