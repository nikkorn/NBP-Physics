package com.dumbpug.harness.launchers;

import java.util.ArrayList;
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
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.BoxType;

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
	
	public ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

	float boxSize = 5f;
	
	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
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
        
        // Create some AABBs to process.
        ArrayList<Basic3DBox> boxes = getRandomBoxes(10, 10, 10, 12345);
        
 		for (AABB box : boxes) {	
 			ModelInstance instance = new ModelInstance(nonIntersectingModel);
 			
 			instance.transform.setToTranslation(box.getX(), box.getY(), box.getZ());
 			
 			instances.add(instance);
 		}
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
	}
	
	@Override
	public void render () {
		camController.update();
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
        modelBatch.begin(cam);
        for (ModelInstance instance : this.instances) {
        	modelBatch.render(instance, environment);
        }
        modelBatch.end();
	}
	
	/**
	 * Create some random AABBs
	 * @param numberOfBoxes
	 * @param seed
	 * @return
	 */
	private ArrayList<Basic3DBox> getRandomBoxes(int width, int height, int depth, long seed) {
        ArrayList<Basic3DBox> boxes = new ArrayList<Basic3DBox>();
        Random ran = new Random(seed);
        
        for (int x = 0; x < width; x++) {
        	for (int z = 0; z < depth; z++) {
        		for (int y = 0; y < height; y++) {
        			// Lets have about a 33% chance of creating a box.
        			if (ran.nextDouble() <= 0.05) {
        				boxes.add(new Basic3DBox((x * boxSize), (y * boxSize), (z * boxSize), boxSize, boxSize, boxSize, BoxType.STATIC));
        			}
         		}
     		}
 		}
 		
 		return boxes;
	}
}