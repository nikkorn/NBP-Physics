package com.dumbpug.nbp;

import java.util.ArrayList;

public class NBPBox {
    // Position
    private float x;
    private float y;
    // Previous Position
    private float lastPosX;
    private float lastPosY;
    // Velocity
    private float velx;
    private float vely;
    // TODO implement max y/x velocity JUST for this box.
    // private float maxVelX;
    // private float maxVelY;
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
    // List of sensors that are attached to this box.
    private ArrayList<NBPSensor> attachedSensors;
    // Point of origin
    private NBPPoint originPoint;
    // Last point of origin
    private NBPPoint lastOriginPoint;
    // Defines whether this box should be removed from the world at the end of the next physics step.
    private boolean isMarkedForDeletion = false;
    // Defines whether this box has been deleted.
    private boolean isDeleted = false;
    
    public NBPBox(float x, float y, float width, float height, NBPBoxType type) {
        this.x = x;
        this.y = y;
        this.lastPosX = x;
        this.lastPosY = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.originPoint = new NBPPoint(x + (width/2), y + (height/2));
        this.lastOriginPoint = new NBPPoint(lastPosX + (width/2), y + (height/2));
        attachedSensors = new ArrayList<NBPSensor>();
    }

    public void applyImpulse(float x, float y) {
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
            // If the velocity is a super small number (between -0.0005 and 0.0005) just set it to zero
            // to stop our boxes infinitely floating around.
            if(velx > -0.0005 && velx < 0.0005) {
                velx = 0f;
            }
            if(vely > -0.0005 && vely < 0.0005) {
                vely = 0f;
            }
            // Clamp our velocity to worlds max.
            clampVelocity();
            // Alter Position
            this.setX(this.x + velx);
            this.setY(this.y + vely);
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

    // ----------------------------------------------------------------
    // ------------- Methods that the user should override ------------
    public void onCollisonWithKineticBox(NBPBox collidingBox, NBPIntersectionPoint kinematicBoxOriginAtCollision) {}

    public void onCollisonWithStaticBox(NBPBox collidingBox, NBPIntersectionPoint originAtCollision) {}

    public void onSensorEntry(NBPSensor sensor, NBPBox enteredBox) {}

    public void onSensorExit(NBPSensor sensor, NBPBox exitedBox) {}
    
    public void onDeletion() {}
    // ----------------------------------------------------------------
    // ----------------------------------------------------------------

    /**
     * Get all attached sensors.
     * @return sensors
     */
    public ArrayList<NBPSensor> getAttachedSensors() { return this.attachedSensors; }

    /**
     * Attach a sensor.
     * @param sensor
     */
    public void attachSensor(NBPSensor sensor) {
        // Don't bother if this sensor is already attached.
        if(!attachedSensors.contains(sensor)) {
            attachedSensors.add(sensor);
            // Set the parent of the sensor to be this box.
            sensor.setParent(this);
        }
    }

    /**
     * Remove a sensor.
     * @param sensor
     */
    public void removeSensor(NBPSensor sensor) {
        // Make sure that this sensor is attached.
        if(attachedSensors.contains(sensor)) {
            attachedSensors.remove(sensor);
        }
    }

    public float getX() { return x; }

    public void setX(float newX) {
        // Move attached sensors along with this box.
        if(newX > this.x) {
            for(NBPSensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() + (newX - this.x));
            }
        } else if(newX < this.x) {
            for(NBPSensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() - (this.x - newX));
            }
        }
        this.lastPosX = x;
        this.x = newX;
    }

    public float getY() { return y; }

    public void setY(float newY) {
        // Move attached sensors along with this box.
        if(newY > this.y) {
            for(NBPSensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() + (newY - this.y));
            }
        } else if(newY < this.y) {
            for(NBPSensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() - (this.y - newY));
            }
        }
        this.lastPosY = y;
        this.y = newY;
    }

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
    
    public float getLastPosX() { return lastPosX; }
	
	public float getLastPosY() { return lastPosY; }
	
	public boolean isMarkedForDeletion() { return isMarkedForDeletion; }

	public void markForDeletion() { this.isMarkedForDeletion = true; }
	
	public boolean isDeleted() { return isDeleted; }

	public void setDeleted() { this.isDeleted = true; }

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

	public NBPPoint getCurrentOriginPoint() {
		originPoint.setX(this.x + (width/2));
		originPoint.setY(this.y + (height/2));
		return originPoint;
	}
	
	public NBPPoint getLastOriginPoint() {
		lastOriginPoint.setX(this.lastPosX + (width/2));
		lastOriginPoint.setY(this.lastPosY + (height/2));
		return lastOriginPoint;
	}
}
