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
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.sap.SAP;

/**
 * A simple launcher for 3D visualisation of the SAP implementation.
 */
public class SAP3DTestLauncher extends ApplicationAdapter {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model intersectingModel;
	public Model nonIntersectingModel;
	
	public SAP sap = new SAP(Dimension.THREE_DIMENSIONS);
	
	public ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

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
        
        // TODO 2 is fine, 3 is not!
        // ArrayList<AABB> boxes = getRandomBoxes(2, 345);
        
        ArrayList<AABB> boxes = getThreeIntersectingBoxes();
        
        // Add the boxes to the SAP.
 		for (AABB box : boxes) {
 			sap.add(box);
 		}
 		
 		// Get the intersection map of all of these boxes.
 		HashMap<AABB, ArrayList<AABB>> intersections = sap.getIntersections();
        
 		for (AABB box : boxes) {	
 			ModelInstance instance = new ModelInstance(intersections.containsKey(box) ? intersectingModel : nonIntersectingModel);
 			
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
	private ArrayList<AABB> getRandomBoxes(int numberOfBoxes, long seed) {
        ArrayList<AABB> boxes = new ArrayList<AABB>();
        Random ran            = new Random(seed);
        
 		for (int i = 0; i < numberOfBoxes; i++) {
 			boxes.add(new AABB(ran.nextInt(10), ran.nextInt(10), ran.nextInt(10), 5, 5, 5));
 		}
 		
 		return boxes;
	}
	
	private ArrayList<AABB> getThreeIntersectingBoxes() {
		// Create some random AABBs to process.
        ArrayList<AABB> boxes = new ArrayList<AABB>();
 		
        boxes.add(new AABB(0, 0, 0, 5, 5, 5));
        boxes.add(new AABB(5, 5, 5, 5, 5, 5));
        boxes.add(new AABB(10, 10, 10, 5, 5, 5));
        
        return boxes;
	}
}
