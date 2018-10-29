package nbp;

/**
 * The gravity applied to a physics world.
 */
public class Gravity {
    /**
     * The gravitational force to apply.
     */
    private float force;
    /**
     * The axis on which to apply gravity.
     */
    private Axis axis;
    /**
     * The flag defining whether the gravity is enabled.
     */
    private boolean isEnabled = true;

    /**
     * Create a new instance of the Gravity class.
     * @param axis   The axis on which gravity is applied.
     * @param force  The gravitational force to apply.
     */
    public Gravity(Axis axis, float force) {
        this.axis  = axis;
        this.force = force;
    }

    /**
     * Get the gravitational force to apply.
     * @return The gravitational force to apply.
     */
    public float getForce() {
        return this.force;
    }

    /**
     * Set the gravitational force to apply.
     * @param force The gravitational force to apply.
     */
    public void setForce(float force) {
        this.force = force;
    }

    /**
     * Get the axis on which to apply gravity.
     * @return The axis on which to apply gravity.
     */
    public Axis getAxis() {
        return this.axis;
    }

    /**
     * Set the axis on which to apply gravity.
     * @param axis The axis on which to apply gravity.
     */
    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    /**
     * Get whether the gravity is enabled.
     * @return Whether the gravity is enabled.
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Set whether the gravity is enabled.
     * @param isEnabled Whether the gravity is enabled.
     */
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
