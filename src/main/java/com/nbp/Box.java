package com.nbp;

import com.nbp.point.IntersectionPoint;
import com.nbp.point.Point;
import java.util.ArrayList;

/**
 * Represents a box in a physics environment.
 */
public abstract class Box {
    /**
     * The position.
     */
    private float x, y;
    /**
     * The position of this box before the most recent position update.
     */
    private float lastPosX, lastPosY;
    /**
     * The velocity.
     */
    private float velX, velY;
    /**
     * The maximum velocity for this box.
     */
    private float maxVelX = 50f, maxVelY = 50f;
    /**
     * The size.
     */
    private float width, height;
    /**
     * The type of this box, which defines its physics behaviour.
     */
    private BoxType type;
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
    private Point originPoint;
    /**
     * The point of origin of this box before the most recent position update.
     */
    private Point lastOriginPoint;
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
     * Creates a new instance of the Box class.
     * @param x      The X position of the box.
     * @param y      The Y position of the box.
     * @param width  The width of the box.
     * @param height The height of the box.
     * @param type   The type of the box which defines its physics behaviour.
     */
    public Box(float x, float y, float width, float height, BoxType type) {
        this.name            = null;
        this.x               = x;
        this.y               = y;
        this.lastPosX        = x;
        this.lastPosY        = y;
        this.width           = width;
        this.height          = height;
        this.type            = type;
        this.originPoint     = new Point(x + (width / 2), y + (height / 2));
        this.lastOriginPoint = new Point(lastPosX + (width / 2), y + (height / 2));
        attachedSensors      = new ArrayList<Sensor>();
    }

    /**
     * Apply an impulse to this box.
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
     * Do our physics update along the X axis.
     * @param gravity The gravity to apply.
     */
    public void updateAxisX(Gravity gravity) {
        // If this box is static then do nothing!
        if (this.type == BoxType.DYNAMIC) {
            // Add our gravity to X velocity only if this box is affected by gravity.
            if (this.isAffectedByGravity && gravity != null) {
                switch (gravity.getDirection()) {
                    case RIGHT:
                        this.velX += gravity.getForce();
                        break;
                    case LEFT:
                        this.velX -= gravity.getForce();
                        break;
                }
            }
            // If the velocity is a super small number (between -0.0005 and 0.0005)
            // just set it to zero to stop our boxes infinitely floating around.
            if (velX > -0.0005 && velX < 0.0005) {
                velX = 0f;
            }
            // Clamp our velocity to our max.
            clampVelocity();
            // Alter Position.
            this.setX(this.x + velX);
        }
    }

    /**
     * Do our physics update along the Y axis.
     * @param gravity The gravity to apply.
     */
    public void updateAxisY(Gravity gravity) {
        // If this box is static then do nothing!
        if (this.type == BoxType.DYNAMIC) {
            // Add our gravity to Y velocity only if this box is affected by gravity.
            if (this.isAffectedByGravity && gravity != null) {
                switch (gravity.getDirection()) {
                    case UP:
                        this.velY += gravity.getForce();
                        break;
                    case DOWN:
                        this.velY -= gravity.getForce();
                        break;
                }
            }
            // If the velocity is a super small number (between -0.0005 and 0.0005)
            // just set it to zero to stop our boxes infinitely floating around.
            if (velY > -0.0005 && velY < 0.0005) {
                velY = 0f;
            }
            // Clamp our velocity to our max.
            clampVelocity();
            // Alter Position.
            this.setY(this.y + velY);
        }
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
        // Get the distance between the bloom and this box.
        float distance = NBPMath.getDistanceBetweenPoints(bloomPoint, getCurrentOriginPoint());
        // Check to see if the box is even in the range of the bloom.
        if (distance <= bloom.getRadius()) {
            // Our box was in the bloom, get angle difference between our bloom and the current box.
            float angleBetweenBloomAndBox = NBPMath.getAngleBetweenPoints(getCurrentOriginPoint(), bloomPoint);
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
     * Clamp the velocity of this box so that it does not exceed its max.
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
    }

    /**
     * Get all attached sensors.
     * @return sensors The list of attached sensors.
     */
    public ArrayList<Sensor> getAttachedSensors() {
        return this.attachedSensors;
    }

    /**
     * Attach a sensor.
     * @param sensor The sensor to attach.
     */
    public void attachSensor(Sensor sensor) {
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
        this.x = newX;
    }

    /**
     * Get the Y position of this box.
     * @return The y position.
     */
    public float getY() {
        return y;
    }

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
        this.y = newY;
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
    public Point getCurrentOriginPoint() {
        originPoint.setX(this.x + (width / 2));
        originPoint.setY(this.y + (height / 2));
        return originPoint;
    }

    /**
     * Get the last origin point of this box.
     * @return The last origin point of this box.
     */
    public Point getLastOriginPoint() {
        lastOriginPoint.setX(this.lastPosX + (width / 2));
        lastOriginPoint.setY(this.lastPosY + (height / 2));
        return lastOriginPoint;
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
