package com.dumbpug.nbp;

public class NBPPoint {
    private float x;
    private float y;
    
    public NBPPoint() { this(0f, 0f); }
    
    public NBPPoint(float x, float y) {
    	this.x = x;
    	this.y = y;
    }
   
	public float getY() { return y; }
	
	public void setY(float y) { this.y = y; }
	
	public float getX() { return x; }
	
	public void setX(float x) { this.x = x; }
}
