package com.dumbpug.nbp.harness;

import com.dumbpug.nbp.Bloom;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Sensor;
import com.dumbpug.nbp.point.IntersectionPoint;

/**
 * A simple 2d box.
 */
public class Basic2DBox extends Box {

	/**
	 * Create a new instance of the Basic2DBox class.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param type
	 */
	public Basic2DBox(float x, float y, float width, float height, BoxType type) {
		super(x, y, width, height, type);
	}

	@Override
	protected void onCollisionWithDynamicBox(Box collidingBox, IntersectionPoint dynamicBoxOriginAtCollision) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCollisionWithStaticBox(Box collidingBox, IntersectionPoint originAtCollision) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onSensorEntry(Sensor sensor, Box enteredBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onSensorExit(Sensor sensor, Box exitedBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean onBloomPush(Bloom bloom, float angleOfForce, float force, float distance) {
		// TODO Auto-generated method stub
		return false;
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
}
