package com.dumbpug.nbp;

import java.util.ArrayList;

/**
 * Sensors attach to Kinetic and Static NBPBox objects.
 * Created by nik on 27/02/16.
 */
public class NBPSensor {
    // Name of sensor.
    private String name;
    // Parent of sensor, each sensor can have only one parent.
    private NBPBox parent;
    // Position
    private float x;
    private float y;
    // Size
    private float width;
    private float height;
    // List of intersecting boxes.
    private ArrayList<NBPBox> intersectingBoxes;

    public NBPSensor(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        intersectingBoxes = new ArrayList<NBPBox>();
    }

    /**
     * Takes a list of all world boxes and reviews intersections.
     * @param worldBoxes
     */
    public void reviewIntersections(ArrayList<NBPBox> worldBoxes) {
        for(NBPBox box : worldBoxes) {
            // Ignore this sensors parent.
            if(box != parent) {
                // Does this sensor intersect with the box?
                if(NBPMath.doesSensorCollideWithBox(this, box)){
                    // We only care if this is the first time we are hearing of the intersection.
                    if(!intersectingBoxes.contains(box)) {
                        // Add this as an intersecting box.
                        intersectingBoxes.add(box);
                        // Notify the sensors parent that this sensor has entered another box.
                        parent.onSensorEntry(this, box);
                    }
                } else {
                    // Had this box been colliding and now it is not?
                    if(intersectingBoxes.contains(box)) {
                        // The sensor was intersecting this box, notify the parent.
                        intersectingBoxes.remove(box);
                        parent.onSensorExit(this, box);
                    }
                }
            }
        }
    }

    public ArrayList<NBPBox> getIntersectingBoxes() { return this.intersectingBoxes; }

    public void setParent(NBPBox parent) { this.parent = parent; }

    public NBPBox getParent() { return this.parent; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public float getX() { return x; }

    public void setX(float x) { this.x = x; }

    public float getY() { return y; }

    public void setY(float y) { this.y = y; }

    public float getWidth() { return this.width; }

    public float getHeight() { return this.height; }
}
