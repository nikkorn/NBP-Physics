package com.dumbpug.main.gamedevicestesting.weapons;

import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPIntersectionPoint;

public class StickyGrenade extends Grenade  {

	public StickyGrenade(Player owner, long fuseTimeMillis) {
		super(owner, fuseTimeMillis);
		this.setName("STICKY_GRENADE");
	}

	@Override
	protected void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {
		// Sticky grenades should only stick to players who aren't the grenades owner.
		if(collidingBox instanceof Player && (collidingBox != this.getOwner())) {
			// Kill this box.
			this.kill();
			// TODO Make a join!
		}
	}

	@Override
	protected void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {
		// Kill this box.
		this.kill();
	}
}
