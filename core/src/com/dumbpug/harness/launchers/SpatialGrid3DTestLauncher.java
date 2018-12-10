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
import com.dumbpug.nbp.AABB;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.spatialgrid.SpatialGrid;

/**
 * A simple launcher for 3D visualisation of the SAP implementation.
 */
public class SpatialGrid3DTestLauncher extends ApplicationAdapter {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model intersectingModel;
	public Model nonIntersectingModel;
	
	SpatialGrid<AABB> grid = new SpatialGrid<AABB>(Dimension.THREE_DIMENSIONS, 100f);
	
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
        ArrayList<AABB> boxes = getRandomBoxes(3000, 12345);
        
        // Add the boxes to the SAP.
 		for (AABB box : boxes) {
 			grid.add(box);
 		}
        
 		for (AABB box : boxes) {	
 			ModelInstance instance = new ModelInstance(this.getModelForAABB(box));
 			
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
 			boxes.add(new AABB(ran.nextInt(200) - 100, ran.nextInt(200) - 100, ran.nextInt(200) - 100, 5, 5, 5));
 		}
 		
 		return boxes;
	}
	
	/**
	 * Get the relevant model for an AABB.
	 * @param box The aabb to get the model for.
	 * @return The relevant model for an AABB.
	 */
	private Model getModelForAABB(AABB box) {
		// Try to find any boxes that intersect the specified one.
		for (AABB candidate : grid.getCollisionCandidates(box)) {
			if (box.intersects(candidate)) {
				return intersectingModel;
			}
		}
		
		// The aabb did not intersect with another aabb.
		return nonIntersectingModel;
	}
}
