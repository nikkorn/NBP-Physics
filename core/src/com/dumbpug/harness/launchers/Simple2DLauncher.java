package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.harness.Basic2DBox;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.Environment;
import com.dumbpug.nbp.Gravity;

/**
 * A simple launcher displaying a bouncing dynamic 2D box on a static one.
 */
public class Simple2DLauncher extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture wimg;
	
	/** The physcis entities. */
	private Environment environment;
	private Box box1;
	private Box movingBox;
	private ArrayList<Box> cluster = new ArrayList<Box>();
	
	@Override
	public void create () {
	    batch = new SpriteBatch();
	    wimg  = new Texture("white_box.png");
	
	    environment = new Environment(Dimension.TWO_DIMENSIONS, 200f, new Gravity(Axis.Y, -0.09f));
	    
	    box1 = new Basic2DBox(200, 100, 100, 100, BoxType.STATIC);
	    box1.setName("wall");
	
	    movingBox = new Basic2DBox(200, 200, 100, 100, BoxType.DYNAMIC);
	    movingBox.setName("bullet");
	
	    environment.addBox(movingBox);
	    environment.addBox(box1);
	    cluster.add(box1);
	    cluster.add(movingBox);
	}
	
	@Override
	public void render () {
	    Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    // Apply impulse up on the Y axis.
	    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
	    	movingBox.applyImpulse(Axis.Y, 0.8f);
	    }
	    
	    // Apply impulse down on the Y axis.
	    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
	    	movingBox.applyImpulse(Axis.Y, -0.8f);
	    }
	
	    // Update the physics environment.
	    environment.update();
	
	    // Draw all boxes in the cluster.
	    batch.begin();
	    for(Box box : cluster) {
	        batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
	    }
	    batch.end();
	}
}
