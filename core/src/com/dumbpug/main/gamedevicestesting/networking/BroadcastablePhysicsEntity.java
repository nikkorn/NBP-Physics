package com.dumbpug.main.gamedevicestesting.networking;

import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;

/**
 * Represents a physics box which is capable of transmitting state via a broadcaster.
 * @author nikolas.howard
 */
public abstract class BroadcastablePhysicsEntity extends NBPBox {

	public BroadcastablePhysicsEntity(float x, float y, float width, float height, NBPBoxType type) {
		super(x, y, width, height, type);
	}
}
