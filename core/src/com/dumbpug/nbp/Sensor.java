package com.dumbpug.nbp;

import com.dumbpug.nbp.projection.Projection;
import com.dumbpug.nbp.projection.ProjectionType;

/**
 * A sensor which can be attached to dynamic and static boxes.
 */
public class Sensor extends Projection {
    /**
     * The name of the sensor.
     */
    private String name;
    /**
     * The box that this sensor is attached to.
     */
    private Box parent;

    /**
     * Creates a new instance of a 2D sensor.
     * @param x      The X position.
     * @param y      The Y position.
     * @param width  The width of the sensor.
     * @param height The height of the sensor.
     */
    public Sensor(float x, float y, float width, float height) {
    	super(ProjectionType.SENSOR, x, y, width, height);
    }
    
    /**
     * Creates a new instance of a 3D sensor.
     * @param x      The X position.
     * @param y      The Y position.
     * @param z      The Z position.
     * @param width  The width of the sensor.
     * @param height The height of the sensor.
     * @param depth  The depth of the sensor.
     */
    public Sensor(float x, float y, float z, float width, float height, float depth) {
    	super(ProjectionType.SENSOR, x, y, z, width, height, depth);
    }

    /**
     * Get the box that this sensor is attached to.
     * @return The box that this sensor is attached to.
     */
    public Box getParent() { return this.parent; }

    /**
     * Set the box that this sensor is attached to.
     * @param parent The box that this sensor is attached to.
     */
    public void setParent(Box parent) { this.parent = parent; }

    /**
     * Get the sensor name.
     * @return The sensor name.
     */
    public String getName() { return this.name; }

    /**
     * Set the sensor name.
     * @param name The sensor name.
     */
    public void setName(String name) { this.name = name; }
}
