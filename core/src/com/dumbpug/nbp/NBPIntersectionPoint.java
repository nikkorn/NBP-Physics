package com.dumbpug.nbp;

import com.dumbpug.nbp.NBPMath.NBPIntersectionDirection;

/**
 * 
 * @author nikolas.howard
 *
 */
public class NBPIntersectionPoint extends NBPPoint {
	// Direction of intersection.
	private NBPIntersectionDirection dir;
	
	public NBPIntersectionPoint(float x, float y, NBPIntersectionDirection direction) {
		super(x, y);	    	
		this.dir = direction;
	}
	
	public NBPIntersectionPoint(NBPPoint p, NBPIntersectionDirection direction) {
		super(p.getX(), p.getY());	    	
		this.dir = direction;
	}
	
	public NBPIntersectionDirection getIntersectionDir() {
		return this.dir;
	}
}
