package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.dumbpug.harness.Basic3DBox;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.Gravity;

/**
 * A simple 3D launcher with lots of individual dynamic boxes.
 */
public class Simple3DCollision extends ApplicationAdapter {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model intersectingModel;
	public Model nonIntersectingModel;
	
	private com.dumbpug.nbp.Environment world;
	
	public ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();
	
	private HashMap<Basic3DBox, ModelInstance> boxToModelInstanceMap = new HashMap<Basic3DBox, ModelInstance>();

	float boxSize = 5f;
	
	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(30f, 30f, 30f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		
        ModelBuilder modelBuilder = new ModelBuilder();
        intersectingModel = modelBuilder.createBox(5f, 5f, 5f, 
            new Material(ColorAttribute.createDiffuse(Color.RED)),
            Usage.Position | Usage.Normal);
        nonIntersectingModel = modelBuilder.createBox(5f, 5f, 5f, 
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
        
        world = new com.dumbpug.nbp.Environment(Dimension.THREE_DIMENSIONS, 20f, new Gravity(Axis.Y, -0.09f));
        
        // Create some AABBs to process.
        ArrayList<Basic3DBox> boxes = new ArrayList<Basic3DBox>();
        
        // Create and add a static box.
        boxes.add(getStaticBox());
        
        // Create and add a dynamic box.
        boxes.add(getDynamicBox(12345));
        
 		for (Basic3DBox box : boxes) {	
 			ModelInstance instance = new ModelInstance(box.getType() == BoxType.DYNAMIC ? intersectingModel : nonIntersectingModel);
 			
 			instance.transform.setToTranslation(box.getX(), box.getY(), box.getZ());
 			
 			boxToModelInstanceMap.put(box, instance);
 			
 			world.addBox(box);
 		}
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
	}
	
	@Override
	public void render () {
		camController.update();
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        // Update the physics environment.
        world.update();
 
        modelBatch.begin(cam);
        for (Basic3DBox box : this.boxToModelInstanceMap.keySet()) {
        	// Get the model instance for the box.
        	ModelInstance instance = this.boxToModelInstanceMap.get(box);
        	
        	instance.transform.setToTranslation(box.getX(), box.getY(), box.getZ());
        	
        	modelBatch.render(instance, environment);
        }
        modelBatch.end();
	}
	
	/**
	 * Create a static AABB.
	 * @return A static AABB.
	 */
	private Basic3DBox getStaticBox() {
		// Create our dynamic box.
		// TODO If y < -boxSize then no collision!
		Basic3DBox box = new Basic3DBox(0, -6f, 0, boxSize, boxSize, boxSize, BoxType.STATIC);
		
		box.setRestitution(0.4f);
		box.setFriction(0.4f);
		
		return box;
	}
	
	/**
	 * Create a dynamic AABB.
	 * @param seed
	 * @return
	 */
	private Basic3DBox getDynamicBox(long seed) {
		// Create our dynamic box.
		Basic3DBox box = new Basic3DBox(0, 15f, 0, boxSize, boxSize, boxSize, BoxType.DYNAMIC);
		
		// Random random = new Random(seed);
		box.applyImpulse(Axis.X, 0.01f);
		box.applyImpulse(Axis.Z, 0.01f);
		
		box.setRestitution(0.4f);
		box.setFriction(0.4f);
		
		return box;
	}
}