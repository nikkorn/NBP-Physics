package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
public class Cluster3DStaticTowerLauncher extends ApplicationAdapter {
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
		cam.position.set(50f, 50f, 50f);
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
        
        // Create some static AABBs to process.
        ArrayList<Basic3DBox> boxes = getRandomStaticBoxes(10, 10, 10, 12345);
        
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
	 * Create some random static AABBs
	 * @param numberOfBoxes
	 * @param seed
	 * @return
	 */
	private ArrayList<Basic3DBox> getRandomStaticBoxes(int width, int height, int depth, long seed) {
        ArrayList<Basic3DBox> boxes = new ArrayList<Basic3DBox>();
        Random ran = new Random(seed);
        
        float yOffset = height * -boxSize;
        float xOffset = (width / 2f) * -boxSize;
        float zOffset = (depth / 2f) * -boxSize;
        
        for (int x = 0; x < width; x++) {
        	for (int z = 0; z < depth; z++) {
        		for (int y = 0; y < height; y++) {
        			// Lets have about a 33% chance of creating a box.
        			if (ran.nextDouble() <= 0.05) {
        				boxes.add(new Basic3DBox((x * boxSize) + xOffset, (y * boxSize) + yOffset, (z * boxSize) + zOffset, boxSize, boxSize, boxSize, BoxType.STATIC));
        			}
         		}
     		}
 		}
 		
 		return boxes;
	}
	
	/**
	 * Create a dynamic AABB.
	 * @param seed
	 * @return
	 */
	private Basic3DBox getDynamicBox(long seed) {
		Random random = new Random(seed);
		
		// Create our dynamic box.
		Basic3DBox box = new Basic3DBox(0, 20f, 0, boxSize, boxSize, boxSize, BoxType.DYNAMIC);
		
		box.applyImpulse(Axis.X, (random.nextFloat()*4f) - 2f);
		box.applyImpulse(Axis.Z, (random.nextFloat()*4f) - 2f);
		box.setRestitution(0.5f);
		box.setFriction(0.5f);
		
		return box;
	}
}