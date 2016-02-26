package com.dumbpug.nbp;

public class NBPBox {
	// Position
	private float x;
	private float y;
	// Velocity
	private float velx;
	private float vely;
	// Acceleration
	private float accx;
	private float accy;
	// Size
	private float width;
	private float height;
	// Box Type
	private NBPBoxType type;
	// Entity name
	private String name = "undefined";
	// Friction
	private float friction = 0f;
	// Restitution
	private float restitution = 0f;
	// Reference of world this object is in.
	private NBPWorld wrappingWorld = null;
	
	public NBPBox(float x, float y, float width, float height, NBPBoxType type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.type = type;
	}

    public void addVelImpulse(float x, float y) {
        this.velx += x;
        this.vely += y;
        // Clamp the velocity
        clampVelocity();
    }
	
	public void update(NBPWorld world) {
		// If this box is static then do nothing!
		if(this.type == NBPBoxType.KINETIC) {
			// Reset our acceleration 
			this.accy = 0f;
			this.accx = 0f;
			// Add our gravity to Y acceleration
			this.accy -= wrappingWorld.getWorldGravity();
			// Apply Acceleration to Velocity
			this.velx += accx;
			this.vely += accy;
			// Clamp our velocity to worlds max.
            clampVelocity();
			// Alter Position
			this.x += velx;
			this.y += vely;
		}
	}
	
	private void clampVelocity() {
		 // Clamp our velocity to worlds max.
        if(velx < -wrappingWorld.getWorldMaxBoxVelocity()) {
            velx = -wrappingWorld.getWorldMaxBoxVelocity();
        } else if(velx > wrappingWorld.getWorldMaxBoxVelocity()) {
            velx = wrappingWorld.getWorldMaxBoxVelocity();
        }
        if(vely < -wrappingWorld.getWorldMaxBoxVelocity()) {
            vely = -wrappingWorld.getWorldMaxBoxVelocity();
        } else if(vely > wrappingWorld.getWorldMaxBoxVelocity()) {
            vely = wrappingWorld.getWorldMaxBoxVelocity();
        }
	}

    public void onCollisonWithKineticBox(NBPBox collidingBox) {}
	
	public float getX() { return x; }
	
	public void setX(float x) { this.x = x; }
	
	public float getY() { return y; }
	
	public void setY(float y) { this.y = y; }

	public float getAccx() { return accx; }

	public void setAccx(float accx) { this.accx = accx; }

	public float getAccy() { return accy; }

	public void setAccy(float accy) { this.accy = accy; }

    public float getVelx() { return this.velx; }

	public void setVelx(float velx) { this.velx = velx; }

	public void setVely(float vely) { this.vely = vely; }

    public float getVely() { return this.vely; }

    public float getWidth() { return this.width; }

    public float getHeight() { return this.height; }

	public NBPBoxType getType() { return this.type; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

	public float getFriction() { return friction; }

	public void setFriction(float friction) {
		// Friction must be a float value between 0 and 1.
		if(friction < 0) {
			this.friction = 0f;
		} else if(friction > 1f) {
			this.friction = 0f;
		} else {
			this.friction = friction;
		}
	}

	public float getRestitution() { return restitution; }

	public void setRestitution(float restitution) {
		// Restitution must be a float value between 0 and 1.
		if(restitution < 0) {
			this.restitution = 0f;
		} else if(restitution > 1f) {
			this.restitution = 0f;
		} else {
			this.restitution = restitution;
		}
	}

	public NBPWorld getWrappingWorld() { return wrappingWorld; }

	public void setWrappingWorld(NBPWorld wrappingWorld) { this.wrappingWorld = wrappingWorld; }
}
