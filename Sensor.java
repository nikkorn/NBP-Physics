package com.dumbpug.ohm.nbp;

import java.util.ArrayList;

/**
 * A sensor which can be attached to kinematic and static physics boxes.
 */
public class Sensor {
    /**
     * The name of the sensor.
     */
    private String name;
    /**
     * The box that this sensor is attached to.
     */
    private Box parent;
    /**
     * The sensor position.
     */
    private float x, y;
    /**
     * The sensor size.
     */
    private float width, height;
    /**
     * The list of intersecting boxes.
     */
    private ArrayList<Box> intersectingBoxes;

    /**
     * Creates a new instance of the Sensor class.
     * @param x      The X position.
     * @param y      The Y position.
     * @param width  The width of the sensor.
     * @param height The height of the sensor.
     */
    public Sensor(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        intersectingBoxes = new ArrayList<Box>();
    }

    /**
     * Takes a list of all physics boxes and reviews intersections.
     * @param boxes The boxes to review.
     */
    public void reviewIntersections(ArrayList<Box> boxes) {
        for (Box box : boxes) {
            // Ignore this sensors parent.
            if (box != parent) {
                // Does this sensor intersect with the box?
                if (NBPMath.doesSensorCollideWithBox(this, box)) {
                    // We only care if this is the first time we are hearing of the intersection.
                    if (!intersectingBoxes.contains(box)) {
                        // Add this as an intersecting box.
                        intersectingBoxes.add(box);
                        // Notify the sensors parent that this sensor has entered another box.
                        parent.onSensorEntry(this, box);
                    }
                } else {
                    // Had this box been colliding and now it is not?
                    if (intersectingBoxes.contains(box)) {
                        // The sensor was intersecting this box, notify the parent.
                        intersectingBoxes.remove(box);
                        parent.onSensorExit(this, box);
                    }
                }
            }
        }
    }

    /**
     * Check whether this sensor is intersecting a box with the specified name.
     * @param boxName The name of the box to check for.
     * @return Whether this sensor is intersecting a box with the specified name.
     */
    public boolean isIntersecting(String boxName) {
        for (Box box : this.intersectingBoxes) {
            if (box.getName().equals(boxName)) {
                return true;
            }
        }
        // There was no intersecting box with the specified name.
        return false;
    }

    /**
     * Get all of the boxes that are currently intersecting with the sensor.
     * @return The boxes which are intersecting with the sensor.
     */
    public ArrayList<Box> getIntersectingBoxes() {
        return this.intersectingBoxes;
    }

    /**
     * Set the box that this sensor is attached to.
     * @param parent The box that this sensor is attached to.
     */
    public void setParent(Box parent) {
        this.parent = parent;
    }

    /**
     * Get the sensor name.
     * @return The sensor name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the sensor name.
     * @param name The sensor name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the x position.
     * @return The x position.
     */
    public float getX() {
        return x;
    }

    /**
     * Set the x position.
     * @param x The X position.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the y position.
     * @return The y position.
     */
    public float getY() {
        return y;
    }

    /**
     * Set the y position.
     * @param y The Y position.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Get the width of the sensor.
     * @return The width of the sensor.
     */
    public float getWidth() {
        return this.width;
    }

    /**
     * Get the height of the sensor.
     * @return the height of the sensor.
     */
    public float getHeight() {
        return this.height;
    }
}
