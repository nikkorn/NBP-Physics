package com.dumbpug.harness.launchers;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.dumbpug.harness.Player3DBox;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;
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
	
	private HashMap<Box, ModelInstance> boxToModelInstanceMap = new HashMap<Box, ModelInstance>();

	float boxSize = 5f;
	
	Player3DBox player = new Player3DBox(0, 60f, 0, boxSize, boxSize, boxSize);
	
	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(40f, 80f, 40f);
		cam.lookAt(0, 50f, 0);
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
        ArrayList<Box> boxes = getStaticBoxes(10, 10, 10);
        
        // Create and add a dynamic box.
        boxes.add(player);
        
 		for (Box box : boxes) {	
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
		// Put the camera at the player box position.
		// cam.position.set(player.getX() + (player.getWidth() / 2), player.getY() + (player.getHeight() / 2), player.getZ() + (player.getDepth() / 2));
		// cam.update();
		
		camController.update();
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        // Test moving player
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        	player.moveUp();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        	player.moveDown();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        	player.moveRight();
        }

        // Test jumping player
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	player.jump();
        }
        
        // Update the physics environment.
        world.update();
 
        modelBatch.begin(cam);
        for (Box box : this.boxToModelInstanceMap.keySet()) {
        	// Do not draw the player box at the moment.
        	// if (box == player) continue;
        	
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
	private ArrayList<Box> getStaticBoxes(int width, int height, int depth) {
        ArrayList<Box> boxes = new ArrayList<Box>();
        
        float yOffset = -5f;
        float xOffset = (width / 2f) * -boxSize;
        float zOffset = (depth / 2f) * -boxSize;
        
        for (int x = 0; x < width; x++) {
        	for (int z = 0; z < depth; z++) {
        		for (int y = 0; y < height; y++) {
        			if (y == 0 || x == 0 || x == (width - 1) || z == 0 || z == (depth - 1))
        				boxes.add(new Basic3DBox((x * boxSize) + xOffset, (y * boxSize) + yOffset, (z * boxSize) + zOffset, boxSize, boxSize, boxSize, BoxType.STATIC));
         		}
     		}
 		}
 		
 		return boxes;
	}
}