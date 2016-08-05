package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.C;
import com.dumbpug.main.gamedevicestesting.player.Player;
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
public class ClusterGrenadeSegment extends NBPBox {
	// The time the grenade was thrown.
	private long timeThrownInMillis;
	// Fuse time of the grenade.
	private long fuseTimeMillis;
	// The player who threw the grenade.
	private Player owner;
	
	   
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public ClusterGrenadeSegment(Player owner, long fuseTimeMillis) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), C.GRENADE_CLUSTER_SIZE, C.GRENADE_CLUSTER_SIZE, NBPBoxType.KINETIC);
        // Set the grenade fuse time.
        this.fuseTimeMillis = fuseTimeMillis;
        // Set the thrown time.
        this.timeThrownInMillis = System.currentTimeMillis();
        // Set various properties for the grenade.
        setName("CLUSTER_GRENADE_SEGMENT");
        setFriction(C.GRENADE_FRICTION);
        setRestitution(C.GRENADE_RESTITUTION);
        // Set max velocity. 
        setMaxVelocityX(C.GRENADE_MAX_VELOCITY);
        setMaxVelocityY(C.GRENADE_MAX_VELOCITY);
        // Set owner
        this.owner = owner;
    }
    
    /**
     * Get the player who threw this.
     * @return thrower
     */
    public Player getOwner() {
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
    		// Explode! Add a world bloom.
    		NBPBloom grenadeExplosionBloom = new NBPBloom(owner.getPlayerPhysicsBox(), this.getCurrentOriginPoint().getX(), 
    				this.getCurrentOriginPoint().getY(), C.GRENADE_CLUSTER_RADIUS, C.GRENADE_CLUSTER_FORCE);
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
