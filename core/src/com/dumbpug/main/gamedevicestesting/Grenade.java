package com.dumbpug.main.gamedevicestesting;

import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;

/**
 * Represents a grenade.
 * @author nikolas.howard
 *
 */
public class Grenade extends NBPBox {
	// The time the grenade was thrown.
	private long timeThrownInMillis;
	// Fuse time of the grenade.
	private long fuseTimeMillis;
	   
	/**
	 * Create a new grenade.
	 * @param owner
	 */
    public Grenade(PlayerBox owner, long fuseTimeMillis) {
        super(owner.getCurrentOriginPoint().getX(), owner.getCurrentOriginPoint().getY(), 10, 10, NBPBoxType.KINETIC);
        // Set the grenade fuse time.
        this.fuseTimeMillis = fuseTimeMillis;
        // Set the thrown time.
        this.timeThrownInMillis = System.currentTimeMillis();
        // Set various properties for the grenade.
        setName("grenade");
        setFriction(0.6f);
        setRestitution(0.9f);
        // Set max velocity. 
        setMaxVelocityX(5);
        setMaxVelocityY(5);
        // Apply initial impulse in the players facing direction
        this.applyImpulse(owner.facingRight ? 8 : -8, 0f);
    }
    
    @Override
    public void update() {
    	// Do physics step.
    	super.update();
    	// Has the fuse run out?
    	if ((System.currentTimeMillis() - timeThrownInMillis) >= fuseTimeMillis) {
    		// Explode! Add a world bloom.
    		NBPBloom grenadeExplosionBloom = new NBPBloom(this.getCurrentOriginPoint().getX(), 
    				this.getCurrentOriginPoint().getY(), 60f, 6f);
    		this.getWrappingWorld().addBloom(grenadeExplosionBloom);
    		// Remove this grenade box on next update.
    		this.markForDeletion();
    	}
    }
}
