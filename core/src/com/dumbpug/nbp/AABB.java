package com.dumbpug.nbp;

import java.util.UUID;

/**
 * A 2D/3D AABB.
 */
public class AABB {
	/**
	 * The unique id of the AABB.
	 */
	private String id;
	/**
	 * The position of the AABB.
	 */
	private float x, y, z;
	/**
	 * The size of the AABB.
	 */
	private float width, height, depth;
	/**
	 * The dimension of the AABB.
	 */
	private Dimension dimension;

	/**
	 * Creates a new instance of a 2D AABB.
	 * @param x The X position of the AABB.
	 * @param y The Y position of the AABB.
	 * @param width The width of the AABB.
	 * @param height The height of the AABB.
	 */
	public AABB(float x, float y, float width, float height) {
		this.id        = UUID.randomUUID().toString();
		this.x         = x;
		this.y         = y;
		this.width     = width;
		this.height    = height;
		this.dimension = Dimension.TWO_DIMENSIONS;
	}

	/**
	 * Creates a new instance of a 3D AABB.
	 * @param x The X position of the AABB.
	 * @param y The Y position of the AABB.
	 * @param z The Z position of the AABB.
	 * @param width The width of the AABB.
	 * @param height The height of the AABB.
	 * @param depth The depth of the AABB.
	 */
	public AABB(float x, float y, float z, float width, float height, float depth) {
		this.id        = UUID.randomUUID().toString();
		this.x         = x;
		this.y         = y;
		this.z         = z;
		this.width     = width;
		this.height    = height;
		this.depth     = depth;
		this.dimension = Dimension.THREE_DIMENSIONS;
	}
	
	/**
	 * Get the unique id of the AABB.
	 * @return The unique id of the AABB.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Get the X position of this AABB.
	 * @return The x position.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the X position of this AABB.
	 * @param x The X position of this AABB.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Get the Y position of this AABB.
	 * @return The y position.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the Y position of this AABB.
	 * @param y The Y position of this AABB.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Get the Z position of this AABB.
	 * @return The Z position.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Set the Z position of this AABB.
	 * @param z The Z position of this AABB.
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	/**
     * Get the position of the AABB on the specified axis.
     * @param axis The axis for which to get the AABB position.
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
     * Set the position of the AABB on the specified axis.
     * @param axis The axis for which to set the AABB position.
     * @param position The position of the AABB on the specified axis.
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
	 * Get the width of this AABB.
	 * @return The width of this AABB.
	 */
	public float getWidth() {
		return this.width;
	}

	/**
	 * Get the height of this AABB.
	 * @return The height of this AABB.
	 */
	public float getHeight() {
		return this.height;
	}

	/**
	 * Get the depth of this AABB.
	 * @return The depth of this AABB.
	 */
	public float getDepth() {
		return this.depth;
	}
	
	/**
     * Get the length of the AABB on the specified axis.
     * @param axis The axis for which to get the AABB length.
     * @return The length of the AABB on the specified axis. 
     */
    public float getLength(Axis axis) {
        switch (axis) {
            case X:
                return this.getWidth();
            case Y:
                return this.getHeight();
            case Z:
                return this.getDepth();
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }
    
    /**
     * Set the length of the AABB on the specified axis.
     * @param axis The axis for which to set the AABB length.
     * @param length The length of the AABB on the specified axis.
     */
    public void setLength(Axis axis, float length) {
        switch (axis) {
            case X:
            	this.width = length;
            	break;
            case Y:
            	this.height = length;
            	break;
            case Z:
            	this.depth = length;
            	break;
            default:
                throw new RuntimeException("Invalid axis: " + axis);
        }
    }

	/**
	 * Get the dimension of this AABB.
	 * @return The dimension of this AABB.
	 */
	public Dimension getDimension() {
		return this.dimension;
	}

	/**
	 * Gets whether this AABB intersects another AABB. The dimensions of both must match.
	 * @param other The other AABB to check for an intersection against.
	 * @return Whether this AABB intersects another AABB.
	 */
	public boolean intersects(AABB other) {
		// If we are dealing with a 2D intersection then we only care about the x/y axis, otherwise we care about the z axis too.
		if (this.getDimension() == Dimension.TWO_DIMENSIONS) {
			return 
				this.getX() < (other.getX() + other.getWidth()) && (this.getX() + this.getWidth()) > other.getX() &&
				this.getY() < (other.getY() + other.getHeight()) && (this.getY() + this.getHeight()) > other.getY();
		} else {
			return 
				this.getX() < (other.getX() + other.getWidth()) && (this.getX() + this.getWidth()) > other.getX() &&
				this.getY() < (other.getY() + other.getHeight()) && (this.getY() + this.getHeight()) > other.getY() &&
				this.getZ() < (other.getZ() + other.getDepth()) && (this.getZ() + this.getDepth()) > other.getZ();
		}
	}
}
