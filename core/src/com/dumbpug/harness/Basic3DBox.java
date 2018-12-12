package com.dumbpug.harness;

import com.dumbpug.nbp.Bloom;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Sensor;

/**
 * A simple 3d box.
 */
public class Basic3DBox extends Box {

	/**
	 * Create a new instance of the Basic3DBox class.
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 * @param type
	 */
	public Basic3DBox(float x, float y, float z, float width, float height, float depth, BoxType type) {
		super(x, y, z, width, height, depth, type);
	}

	@Override
	protected void onCollisionWithDynamicBox(Box collidingBox) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCollisionWithStaticBox(Box collidingBox) {
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
