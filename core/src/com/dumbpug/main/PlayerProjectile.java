package com.dumbpug.main;

import com.dumbpug.nbp.NBPBloom;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;
import com.dumbpug.nbp.NBPIntersectionPoint;
import com.dumbpug.nbp.NBPSensor;

public class PlayerProjectile  extends NBPBox {
   
    public PlayerProjectile(float x, float y, boolean movingRight) {
        super(x, y, 10, 10, NBPBoxType.KINETIC);
        // Set various properties for the player.
        setName("bullet");
        setFriction(0.6f);
        setRestitution(0.9f);
        // Apply initial impulse
        this.applyImpulse(movingRight ? 8 : -8, 0f);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDeletion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean onBloomPush(NBPBloom bloom, float angleOfForce, float force, float distance) {
		// TODO Auto-generated method stub
		return true;
	}
}
