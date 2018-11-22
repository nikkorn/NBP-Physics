package com.dumbpug.nbp;

import java.util.ArrayList;
import com.dumbpug.nbp.point.Point;

/**
 * Represents either a 2D or 3D box in a physics environment.
 */
public abstract class Box extends AABB {
    /**
     * The position of this box before the most recent position update.
     */
    private float lastPosX, lastPosY, lastPosZ;
    /**
     * The velocity.
     */
    private float velX, velY, velZ;
    /**
     * The maximum velocity for this box.
     */
    private float maxVelX = 50f, maxVelY = 50f, maxVelZ = 50f;
    /**
     * The type of this box, which defines its physics behaviour.
     */
    private BoxType type;
    /**
     * The projection of the box, based on its position and velocity.
     */
    private AABB projection;
    /**
     * The name of the box.
     */
    private String name = null;
    /**
     * The friction value of this box.
     */
    private float friction = 0f;
    /**
     * The restitution value of this box.
     */
    private float restitution = 0f;
    /**
     * The point of origin.
     */
    private Point origin = null;
    /**
     * Defines whether this box should be removed from the environment at the end of the next physics step.
     */
    private boolean isMarkedForDeletion = false;
    /**
     * Defines whether this box has been deleted.
     */
    private boolean isDeleted = false;
    /**
     * Defines whether this box is affected by gravity.
     */
    private boolean isAffectedByGravity = true;
    /**
     * The list of sensors that are attached to this box.
     */
    private ArrayList<Sensor> attachedSensors = new ArrayList<Sensor>();
    /**
     * The user data.
     */
    private Object userData = null;

    /**
     * Creates a new instance of a 2D box.
     * @param x      The X position of the box.
     * @param y      The Y position of the box.
     * @param width  The width of the box.
     * @param height The height of the box.
     * @param type   The type of the box which defines its physics behaviour.
     */
    public Box(float x, float y, float width, float height, BoxType type) {
    	super(x, y, width, height);
        this.type       = type;
        this.projection = Utilities.createProjection(this);
    }

    /**
     * Creates a new instance of a 3D box.
     * @param x      The X position of the box.
     * @param y      The Y position of the box.
     * @param z      The Z position of the box.
     * @param width  The width of the box.
     * @param height The height of the box.
     * @param depth  The depth of the box.
     * @param type   The type of the box which defines its physics behaviour.
     */
    public Box(float x, float y, float z, float width, float height, float depth, BoxType type) {
        super(x, y, z, width, height, depth);
        this.type       = type;
        this.projection = Utilities.createProjection(this);
    }

    /**
     * Apply an impulse to this box on the specified axis.
     * @param axis The axis to apply the impulse on.
     * @param value The amount of velocity to apply on the axis.
     */
    public void applyImpulse(Axis axis, float value) {
        // Get the current velocity on this axis.
        float velocity = this.getVelocity(axis);
        // Apply the impulse velocity.
        this.setVelocity(axis, velocity + value);
    }

    /**
     * Do our physics update along the specified axis.
     * @param axis The axis to do the update on.
     * @param gravity The gravity to apply.
     */
    public void updateOnAxis(Axis axis, Gravity gravity) {
        // Add our gravity to the axis velocity only if this box is affected by gravity.
        if (this.isAffectedByGravity && gravity != null && gravity.getAxis() == axis) {
            this.setVelocity(axis, this.getVelocity(axis) + gravity.getForce());
        }
        // Get the box's current velocity on this axis.
        float currentVelocityOnAxis = this.getVelocity(axis);
        // If the velocity is a super small number (between -0.0005 and 0.0005)
        // just set it to zero to stop our boxes infinitely floating around.
        if (currentVelocityOnAxis > -0.0005 && currentVelocityOnAxis < 0.0005) {
            this.setVelocity(axis, 0f);
        }
        // Alter the box's position on the current axis.
        this.setPosition(axis, this.getPosition(axis) + this.getVelocity(axis));
    }

    /**
     * Apply change in velocity to this Box instance using an angle of direction and a force of velocity.
     * @param angle The angle at which to apply the force in degrees.
     * @param force The amount of force to apply.
     */
    public void applyVelocityInDirection(double angle, float force) {
    	// We can only do this operation in 2D space, for now.
    	if (this.getDimension() == Dimension.THREE_DIMENSIONS) {
    		throw new RuntimeException("Cannot apply veclocity by angle in 3D space.");
    	}
        double impulseX = Math.cos(Math.toRadians(angle)) * force;
        this.setVelocity(Axis.X, (float)impulseX);
        double impulseY = Math.sin(Math.toRadians(angle)) * force;
        this.setVelocity(Axis.Y, (float)impulseY);
    }

    /**
     * Apply a bloom to this box.
     * @param bloom The bloom to apply.
     */
    public void applyBloom(Bloom bloom) {
        // Get the point of the bloom as a Point object.
        Point bloomPoint = new Point(bloom.getX(), bloom.getY());
        // Get the origin of this box.
        Point origin = this.getOrigin();
        // Get the distance between the bloom and this box.
        float distance = Utilities.getDistanceBetweenPoints(bloomPoint, origin);
        // Check to see if the box is even in the range of the bloom.
        if (distance <= bloom.getRadius()) {
            // Our box was in the bloom, get angle difference between our bloom and the current box.
            float angleBetweenBloomAndBox = Utilities.getAngleBetweenPoints(origin, bloomPoint);
            // Recalculate force based on the distance between our box and bloom. Avoid a divide by zero.
            float force;
            if (distance == 0) {
                force = bloom.getForce() * 1;
            } else {
                force = bloom.getForce() * (1 - (distance / bloom.getRadius()));
            }
            // Apply the force of the bloom to this box.
            if (onBloomPush(bloom, angleBetweenBloomAndBox, force, distance)) {
                applyVelocityInDirection(angleBetweenBloomAndBox, force);
            }
        }
    }

    /**
     * Get all attached sensors.
     * @return sensors The list of attached sensors.
     */
    public ArrayList<Sensor> getAttachedSensors() { return this.attachedSensors; }

    /**
     * Attach a sensor.
     * @param sensor The sensor to attach.
     */
    public void attachSensor(Sensor sensor) {
        // Any sensor attached to this box must match its dimension.
        if (this.getDimension() != sensor.getDimension()) {
            throw new RuntimeException("Cannot add sensor to box as dimensions do not match");
        }
        // Do not bother if the sensor is already attached to this box.
        if (!attachedSensors.contains(sensor)) {
            attachedSensors.add(sensor);
            // Set the parent of the sensor to be this box.
            sensor.setParent(this);
        }
    }

    /**
     * Remove a sensor.
     * @param sensor The sensor to remove.
     */
    public void removeSensor(Sensor sensor) {
        // Make sure that this sensor is attached.
        if (attachedSensors.contains(sensor)) {
            attachedSensors.remove(sensor);
        }
    }

    /**
     * Set the X position of this box, updating any attached sensors.
     * @param x The X position.
     */
    @Override
    public void setX(float x) {
        // Move attached sensors along with this box.
        if (x > this.getX()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() + (x - this.getX()));
            }
        } else if (x < this.getX()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() - (this.getX() - x));
            }
        }
        this.lastPosX = this.getX();
        super.setX(x);
        // Update projection on this axis.
        this.updateProjection(Axis.X);
    }

    /**
     * Set the Y position of this box, updating any attached sensors.
     * @param y The Y position.
     */
    @Override
    public void setY(float y) {
        // Move attached sensors along with this box.
        if (y > this.getY()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() + (y - this.getY()));
            }
        } else if (y < this.getY()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() - (this.getY() - y));
            }
        }
        this.lastPosY = getY();
        super.setY(y);
        // Update projection on this axis.
        this.updateProjection(Axis.Y);
    }

    /**
     * Set the Z position of this box, updating any attached sensors.
     * @param z The Z position.
     */
    @Override
    public void setZ(float z) {
        // Move attached sensors along with this box.
        if (z > this.getZ()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setZ(sensor.getZ() + (z - this.getZ()));
            }
        } else if (z < this.getZ()) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setZ(sensor.getZ() - (this.getZ() - z));
            }
        }
        this.lastPosZ = getZ();
        super.setZ(z);
        // Update projection on this axis.
        this.updateProjection(Axis.Z);
    }

    /**
     * Get the velocity of the box on the specified axis.
     * @param axis The axis for which to get the box velocity.
     * @return The velocity of the box on the specified axis.
     */
    public float getVelocity(Axis axis) {
        switch (axis) {
            case X:
                return this.velX;
            case Y:
                return this.velY;
            case Z:
                return this.velZ;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }

    /**
     * Set the velocity of the box on the specified axis.
     * @param axis The axis for which to set the box velocity.
     * @param velocity The velocity of the box on the specified axis.
     */
    public void setVelocity(Axis axis, float velocity) {
        switch (axis) {
            case X:
                this.velX = velocity;
                break;
            case Y:
            	this.velY = velocity;
                break;
            case Z:
            	this.velZ = velocity;
                break;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
        // Clamp our velocity on this axis to its max.
        clampVelocity(axis);
        // Update projection on this axis.
        this.updateProjection(axis);
    }
    
    /**
     * Get the max velocity of the box on the specified axis.
     * @param axis The axis for which to get the max box velocity.
     * @return The max velocity of the box on the specified axis.
     */
    public float getMaxVelocity(Axis axis) {
    	switch (axis) {
	        case X:
	            return this.maxVelX;
	        case Y:
	            return this.maxVelY;
	        case Z:
	            return this.maxVelZ;
	        default:
	            throw new RuntimeException("Invalid axis: " + axis);
	    }
    }
    
    /**
     * Set the mx velocity of the box on the specified axis.
     * @param axis The axis for which to set the box max velocity.
     * @param max The max velocity of the box on the specified axis.
     */
    public void setMaxVelocity(Axis axis, float max) {
        switch (axis) {
            case X:
                this.maxVelX = max;
                break;
            case Y:
            	this.maxVelY = max;
                break;
            case Z:
            	this.maxVelZ = max;
                break;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }
    
    /**
     * Clamp the velocity of this box on the specified axis.
     * @param axis The axis for which to clamp the box velocity.
     */
    private void clampVelocity(Axis axis) {
    	// Get the max velocity for the axis.
    	float maxVelocity = this.getMaxVelocity(axis);
    	// Get the current velocity for the axis.
    	float velocity = this.getVelocity(axis);
        // Clamp the velocity on the axis.
        if (velocity < -maxVelocity) {
        	this.setVelocity(axis, -maxVelocity);
        } else if (velocity > maxVelocity) {
        	this.setVelocity(axis, maxVelocity);
        }
    }

    /**
     * Get user data.
     * @return The user data.
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * Set the user data.
     * @param userData The user data
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }

    /**
     * Get whether this box is affected by gravity.
     * @return Whether this box is affected by gravity.
     */
    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    /**
     * Set whether this box is affected by gravity.
     * @param isAffectedByGravity Whether this box is affected by gravity.
     */
    public void setAffectedByGravity(boolean isAffectedByGravity) {
        this.isAffectedByGravity = isAffectedByGravity;
    }

    /**
     * Get the type of this box.
     * @return The type of this box.
     */
    public BoxType getType() {
        return this.type;
    }

    /**
     * Get the projection for this box.
     * @return The projection for this box.
     */
    public AABB getProjection() { 
    	return this.projection; 
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLastPosX() {
        return lastPosX;
    }

    public float getLastPosY() {
        return lastPosY;
    }

    public float getLastPosZ() {
        return lastPosZ;
    }

    public boolean isMarkedForDeletion() {
        return isMarkedForDeletion;
    }

    public void markForDeletion() {
        this.isMarkedForDeletion = true;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted() {
        this.isDeleted = true;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        // Friction must be a float value between 0 and 1.
        if (friction < 0) {
            this.friction = 0f;
        } else if (friction > 1f) {
            this.friction = 1f;
        } else {
            this.friction = friction;
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        // Restitution must be a float value between 0 and 1.
        if (restitution < 0) {
            this.restitution = 0f;
        } else if (restitution > 1f) {
            this.restitution = 1f;
        } else {
            this.restitution = restitution;
        }
    }

    /**
     * Get the current origin point of this box.
     * @return The current origin point of this box.
     */
    public Point getOrigin() {
    	// Create or update the box origin.
    	if (this.origin == null) {
    		// Are we creating a 2D or 3D point?
    		if (this.getDimension() == Dimension.THREE_DIMENSIONS) {
    			// The origin is a point in 3D space.
    			this.origin = new Point(this.getX() + (this.getWidth() / 2f), this.getY() + (this.getHeight() / 2f), this.getZ() + (this.getDepth() / 2f));
    		} else {
    			// The origin is a point in 2D space.
    			this.origin = new Point(this.getX() + (this.getWidth() / 2f), this.getY() + (this.getHeight() / 2f));
    		}
    	} else {
    		// We already have an origin, update and return it.
    		origin.setX(this.getX() + (this.getWidth() / 2f));
    		origin.setY(this.getY() + (this.getHeight() / 2f));
    		// If this is a 3D box then we need ot set the Z value too.
    		if (this.getDimension() == Dimension.THREE_DIMENSIONS) {
    			origin.setZ(this.getZ() + (this.getDepth() / 2f));
    		}
    	}
        // Return the up-to-date origin.
        return origin;
    }
    
    /**
     * Update the projection of this box on the specified axis.
     * @param axis The axis on which to update the projection.
     */
    private void updateProjection(Axis axis) {
    	// Get the current position of the box on this axis.
    	float position = this.getPosition(axis);
    	// Find the x/y position of the box after the physics update.
    	float destination = position + this.getVelocity(axis);
    	// Find the position/length of the projection on the axis.
    	float projectionPosition = Math.min(position, destination);
		float projectionLength   = (Math.max(position, destination) + this.getLength(axis)) - projectionPosition;
		// Update the position/length of the projection on the axis.
		this.projection.setPosition(axis, projectionPosition);
		this.projection.setLength(axis, projectionLength);	
    }

    protected abstract void onCollisionWithDynamicBox(Box collidingBox);

    protected abstract void onCollisionWithStaticBox(Box collidingBox);

    protected abstract void onSensorEntry(Sensor sensor, Box enteredBox);

    protected abstract void onSensorExit(Sensor sensor, Box exitedBox);

    /**
     * Handle having this box be affected by a bloom.
     * @param bloom The affecting bloom.
     * @param angleOfForce The angle of force.
     * @param force The force applied.
     * @param distance The distance from the affecting bloom.
     * @return Whether this box should be affected by this bloom.
     */
    protected abstract boolean onBloomPush(Bloom bloom, float angleOfForce, float force, float distance);

    protected abstract void onBeforeUpdate();

    protected abstract void onAfterUpdate();

    protected abstract void onDeletion();
}
