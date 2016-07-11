package com.dumbpug.nbp;

/**
 * Used as part of our collision resolution phase.
 * @author Nikolas Howard
 *
 */
public class NBPEnlargedBox extends NBPBox {

	public NBPEnlargedBox(float x, float y, float width, float height, NBPBoxType type) {
		super(x, y, width, height, type);
	}

	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
	}

	@Override
	protected void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {
	}

	@Override
	protected void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {
	}

	@Override
	protected void onBeforeUpdate() {
	}

	@Override
	protected void onAfterUpdate() {
	}

	@Override
	protected void onDeletion() {
	}
}
