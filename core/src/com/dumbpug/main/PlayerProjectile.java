package com.dumbpug.main;

import com.dumbpug.nbp.NBPBox;
import com.dumbpug.nbp.NBPBoxType;

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
}
