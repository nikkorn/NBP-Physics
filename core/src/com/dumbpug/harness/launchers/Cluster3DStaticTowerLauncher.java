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
import com.dumbpug.nbp.Environment;
import com.dumbpug.nbp.Gravity;

/**
 * A simple 2D launcher with lots of individual dynamic boxes.
 */
public class Cluster3DStaticTowerLauncher extends ApplicationAdapter {
	public com.badlogic.gdx.graphics.g3d.Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model intersectingModel;
	public Model nonIntersectingModel;

    /** The physics entities. */
    private Environment world;
    private ArrayList<Basic3DBox> boxes = new ArrayList<Basic3DBox>();
    private HashMap<Basic3DBox, ModelInstance> boxToModelInstanceMap = new HashMap<Basic3DBox, ModelInstance>();
    
    private int clusterSize = 50;
    
    private int towerWidth  = 10;
    private int towerHeight = 10;
    private int towerBoxSize = 5;

    @Override
    public void create () {
    	Random random = new Random();
        
        world = new Environment(Dimension.THREE_DIMENSIONS, 20f, new Gravity(Axis.Y, -0.09f));
        
        environment = new com.badlogic.gdx.graphics.g3d.Environment();
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
            new Material(ColorAttribute.createDiffuse(Color.BLUE)),
            Usage.Position | Usage.Normal);
        nonIntersectingModel = modelBuilder.createBox(5f, 5f, 5f, 
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                Usage.Position | Usage.Normal);
		
        
        // Create a tower of static boxes that do not overlap.
        for(int x = 0; x < towerWidth; x++) {
        	for(int y = 0; y < towerWidth; y++) {
        		for(int z = 0; z < towerHeight; z++) {
            		Basic3DBox cBox = new Basic3DBox((x * towerBoxSize) + 80f, (y * towerBoxSize) - 300f, (z * towerBoxSize) + 80f, towerBoxSize, towerBoxSize, towerBoxSize, BoxType.STATIC);
                    world.addBox(cBox);
                    boxes.add(cBox);
                    
                    ModelInstance instance = new ModelInstance(nonIntersectingModel);
         			instance.transform.setToTranslation(cBox.getX(), cBox.getY(), cBox.getZ());
         			boxToModelInstanceMap.put(cBox, instance);
                }
            }
        }
        
        // Create a cluster of dynamic boxes.
        for(int i = 0; i < clusterSize; i++) {
        	Basic3DBox cBox = new Basic3DBox((random.nextFloat()*500f) + 80f, 300f, (random.nextFloat()*500f) + 80f, 5, 5, 5, BoxType.DYNAMIC);
            cBox.applyImpulse(Axis.X, (random.nextFloat()*6f)-3f);
            cBox.applyImpulse(Axis.Z, (random.nextFloat()*6f)-3f);
            cBox.setRestitution(random.nextFloat());
            cBox.setFriction(random.nextFloat());
            world.addBox(cBox);
            boxes.add(cBox);
            
            ModelInstance instance = new ModelInstance(intersectingModel);
 			instance.transform.setToTranslation(cBox.getX(), cBox.getY(), cBox.getZ());
 			boxToModelInstanceMap.put(cBox, instance);
        }
        
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }

    @Override
    public void render () {
    	camController.update();
    	
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		
        // Update the physics environment.
        world.update();
        
        

        // Draw all boxes.
        modelBatch.begin(cam);
        for(Basic3DBox box : boxes) {
        	// Get the model instance.
        	ModelInstance instance = boxToModelInstanceMap.get(box);
        	// Draw the instance.
        	modelBatch.render(instance, environment);
        }
        modelBatch.end();
    }
}