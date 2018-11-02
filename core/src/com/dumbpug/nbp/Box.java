package com.dumbpug.nbp;

import java.util.ArrayList;
import com.dumbpug.nbp.point.IntersectionPoint;
import com.dumbpug.nbp.point.Point;

/**
 * Represents either a 2D or 3D box in a physics environment.
 */
public abstract class Box {
    /**
     * The position.
     */
    private float x, y, z;
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
     * The size.
     */
    private float width, height, depth;
    /**
     * The type of this box, which defines its physics behaviour.
     */
    private BoxType type;
    /**
     * The dimension of the box.
     */
    private Dimension dimension = Dimension.TWO_DIMENSIONS;
    /**
     * The name of the box.
     */
    private String name;
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
    private ArrayList<Sensor> attachedSensors;
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
        this.name       = null;
        this.x          = x;
        this.y          = y;
        this.width      = width;
        this.height     = height;
        this.type       = type;
        attachedSensors = new ArrayList<Sensor>();
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
        this(x, y, width, height, type);
        this.z         = z;
        this.depth     = depth;
        this.type      = type;
        this.dimension = Dimension.THREE_DIMENSIONS;
    }

    /**
     * Apply an impulse to this box in 2D space.
     * @param x The amount of velocity to apply on the X axis.
     * @param y The amount of velocity to apply on the Y axis.
     */
    public void applyImpulse(float x, float y) {
        this.velX += x;
        this.velY += y;
        // Clamp our velocity to our max.
        clampVelocity();
    }

    /**
     * Apply an impulse to this box in 3D space.
     * @param x The amount of velocity to apply on the X axis.
     * @param y The amount of velocity to apply on the Y axis.
     * @param z The amount of velocity to apply on the Z axis.
     */
    public void applyImpulse(float x, float y, float z) {
        this.velX += x;
        this.velY += y;
        this.velZ += z;
        // Clamp our velocity to our max.
        clampVelocity();
    }
    
    /**
     * Do our physics update along every axis relevant for the box dimension.
     * @param gravity The gravity to apply.
     */
    public void update(Gravity gravity) {
        // If this box is static then do nothing!
        if (this.type != BoxType.DYNAMIC) {
            return;
        }
        // Update this box on the x,y and potentially z axis.
        this.updateOnAxis(Axis.X, gravity);
        this.updateOnAxis(Axis.Y, gravity);
        if (this.dimension == Dimension.THREE_DIMENSIONS) {
        	this.updateOnAxis(Axis.Z, gravity);
        }
        // Clamp our velocity to our max.
        clampVelocity();
    }

    /**
     * Do our physics update along the specified axis.
     * @param axis The axis to do the update on.
     * @param gravity The gravity to apply.
     */
    private void updateOnAxis(Axis axis, Gravity gravity) {
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
        // Clamp our velocity to our max.
        clampVelocity();
        // Alter the box's position on the current axis.
        this.setPosition(axis, this.getPosition(axis) + this.getVelocity(axis));
    }

    /**
     * Apply change in velocity to this Box instance using an angle of direction and a force of velocity.
     * @param angle The angle at which to apply the force in degrees.
     * @param force The amount of force to apply.
     */
    public void applyVelocityInDirection(double angle, float force) {
        double impulseX = Math.cos(Math.toRadians(angle)) * force;
        double impulseY = Math.sin(Math.toRadians(angle)) * force;
        this.applyImpulse((float) impulseX, (float) impulseY);
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
        float distance = NBPMath.getDistanceBetweenPoints(bloomPoint, origin);
        // Check to see if the box is even in the range of the bloom.
        if (distance <= bloom.getRadius()) {
            // Our box was in the bloom, get angle difference between our bloom and the current box.
            float angleBetweenBloomAndBox = NBPMath.getAngleBetweenPoints(origin, bloomPoint);
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
     * Clamp the velocity of this box on every axis so that it does not exceed its max for that axis.
     */
    private void clampVelocity() {
        // Clamp the velocity on the X axis.
        if (velX < -this.getMaxVelocityX()) {
            velX = -this.getMaxVelocityX();
        } else if (velX > this.getMaxVelocityX()) {
            velX = this.getMaxVelocityX();
        }
        // Clamp the velocity on the Y axis.
        if (velY < -this.getMaxVelocityY()) {
            velY = -this.getMaxVelocityY();
        } else if (velY > this.getMaxVelocityY()) {
            velY = this.getMaxVelocityY();
        }
        // Clamp the velocity on the Z axis if this box is in 3D space.
        if (this.dimension == Dimension.THREE_DIMENSIONS) {
            if (velZ < -this.getMaxVelocityZ()) {
                velZ = -this.getMaxVelocityZ();
            } else if (velZ > this.getMaxVelocityZ()) {
                velZ = this.getMaxVelocityZ();
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
     * Get the position of the box on the specified axis.
     * @param axis The axis for which to get the box position.
     */
    public float getPosition(Axis axis) {
        switch (axis) {
            case X:
                return this.getX();
            case Y:
                return this.getY();
            case Z:
                return this.getZ();
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }

    /**
     * Set the position of the box on the specified axis.
     * @param axis The axis for which to set the box position.
     * @param position The position of the box on the specified axis.
     */
    public void setPosition(Axis axis, float position) {
        switch (axis) {
            case X:
                this.setX(position);
                break;
            case Y:
                this.setY(position);
                break;
            case Z:
                this.setZ(position);
                break;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }

    /**
     * Get the X position of this box.
     * @return The x position.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the X position of this box.
     * @param newX The nex X position.
     */
    public void setX(float newX) {
        // Move attached sensors along with this box.
        if (newX > this.x) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() + (newX - this.x));
            }
        } else if (newX < this.x) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setX(sensor.getX() - (this.x - newX));
            }
        }
        this.lastPosX = x;
        this.x        = newX;
    }

    /**
     * Get the Y position of this box.
     * @return The y position.
     */
    public float getY() { return y; }

    /**
     * Set the Y position of this box.
     * @param newY position
     */
    public void setY(float newY) {
        // Move attached sensors along with this box.
        if (newY > this.y) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() + (newY - this.y));
            }
        } else if (newY < this.y) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setY(sensor.getY() - (this.y - newY));
            }
        }
        this.lastPosY = y;
        this.y        = newY;
    }

    /**
     * Get the Z position of this box.
     * @return The Z position.
     */
    public float getZ() { return z; }

    /**
     * Set the Z position of this box.
     * @param newZ position
     */
    public void setZ(float newZ) {
        // Move attached sensors along with this box.
        if (newZ > this.z) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setZ(sensor.getZ() + (newZ - this.z));
            }
        } else if (newZ < this.z) {
            for (Sensor sensor : this.attachedSensors) {
                sensor.setZ(sensor.getZ() - (this.z - newZ));
            }
        }
        this.lastPosZ = z;
        this.z        = newZ;
    }

    /**
     * Get the velocity of the box on the specified axis.
     * @param axis The axis for which to get the box velocity.
     */
    public float getVelocity(Axis axis) {
        switch (axis) {
            case X:
                return this.getVelX();
            case Y:
                return this.getVelY();
            case Z:
                return this.getVelZ();
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
                this.setVelX(velocity);
                break;
            case Y:
                this.setVelY(velocity);
                break;
            case Z:
                this.setVelZ(velocity);
                break;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }

    /**
     * Get the current velocity of this box on the X axis.
     * @return The velocity on the X axis.
     */
    public float getVelX() {
        return this.velX;
    }

    /**
     * Set the current velocity of this box on the X axis.
     * @param velX The velocity on the X axis.
     */
    public void setVelX(float velX) {
        this.velX = velX;
    }

    /**
     * Get the current velocity of this box on the Y axis.
     * @return The velocity on the Y axis.
     */
    public float getVelY() {
        return this.velY;
    }

    /**
     * Set the current velocity of this box on the Y axis.
     * @param velY The velocity on the Y axis.
     */
    public void setVelY(float velY) {
        this.velY = velY;
    }

    /**
     * Get the current velocity of this box on the Z axis.
     * @return The velocity on the Z axis.
     */
    public float getVelZ() {
        return this.velZ;
    }

    /**
     * Set the current velocity of this box on the Z axis.
     * @param velZ The velocity on the Z axis.
     */
    public void setVelZ(float velZ) {
        this.velZ = velZ;
    }

    /**
     * Get the dimension of this box.
     * @return The dimension of this box.
     */
    public Dimension getDimension() { return this.dimension; }

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
     * Gets whether this box is affected by gravity.
     * @return Whether this box is affected by gravity.
     */
    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    /**
     * Sets whether this box is affected by gravity.
     * @param isAffectedByGravity Whether this box is affected by gravity.
     */
    public void setAffectedByGravity(boolean isAffectedByGravity) {
        this.isAffectedByGravity = isAffectedByGravity;
    }

    /**
     * Get the width of this box.
     * @return The width of this box.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Get the height of this box.
     * @return The height of this box.
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Get the depth of this box.
     * @return The depth of this box.
     */
    public float getDepth() {
        return this.depth;
    }

    public BoxType getType() {
        return this.type;
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

    public float getMaxVelocityX() {
        return maxVelX;
    }

    public void setMaxVelocityX(float maxVelX) {
        this.maxVelX = maxVelX;
    }

    public float getMaxVelocityY() {
        return maxVelY;
    }

    public void setMaxVelocityY(float maxVelY) {
        this.maxVelY = maxVelY;
    }

    public float getMaxVelocityZ() {
        return maxVelZ;
    }

    public void setMaxVelocityZ(float maxVelZ) {
        this.maxVelZ = maxVelZ;
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
    		if (this.dimension == Dimension.THREE_DIMENSIONS) {
    			// The origin is a point in 3D space.
    			this.origin = new Point(this.getX(), this.getY(), this.getZ());
    		} else {
    			// The origin is a point in 2D space.
    			this.origin = new Point(this.getX(), this.getY());
    		}
    	} else {
    		// We already have an origin, update and return it.
    		origin.setX(this.getX());
    		origin.setY(this.getY());
    		// If this is a 3D box then we need ot set the Z value too.
    		if (this.dimension == Dimension.THREE_DIMENSIONS) {
    			origin.setZ(this.getZ());
    		}
    	}
        // Return the up-to-date origin.
        return origin;
    }

    protected abstract void onCollisionWithDynamicBox(Box collidingBox, IntersectionPoint dynamicBoxOriginAtCollision);

    protected abstract void onCollisionWithStaticBox(Box collidingBox, IntersectionPoint originAtCollision);

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
