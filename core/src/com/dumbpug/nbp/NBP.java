package com.dumbpug.nbp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBP extends ApplicationAdapter {
	SpriteBatch batch;
	Texture wimg;
    Texture rimg;

	NBPWorld world;
	NBPBox box1;
	NBPBox box2;
	NBPBox boxStatic;
	NBPBox boxStatic2;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");

		world = new NBPWorld(0.09f, 4f);
		box1 = new NBPBox(200,200,20,40,NBPBoxType.KINETIC);
        box1.setName("box1");
        box1.setFriction(0.98f);
        box1.setRestitution(0.8f);
        
		box2 = new NBPBox(265,300,30,10,NBPBoxType.STATIC);
        box2.setName("box2");
		boxStatic = new NBPBox(120,100,200,20,NBPBoxType.STATIC);
        boxStatic.setName("boxStatic");
		boxStatic2 = new NBPBox(300,180,20,200,NBPBoxType.STATIC);
        boxStatic2.setName("boxStatic2");

		world.addBox(box1);
		world.addBox(box2);
		world.addBox(boxStatic);
		world.addBox(boxStatic2);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update();

        // Test moving box1
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            box1.addVelImpulse(-0.1f,0f);
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)){
            box1.addVelImpulse(0.1f,0f);
        }

        // Test jumping box1
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            box1.addVelImpulse(0f,3f);
        }
        
		batch.begin();
		batch.draw(isBoxColliding(box1) ? rimg : wimg, box1.getX(), box1.getY(), box1.getWidth(), box1.getHeight());
        batch.draw(isBoxColliding(box2) ? rimg : wimg, box2.getX(), box2.getY(), box2.getWidth(), box2.getHeight());
        batch.draw(isBoxColliding(boxStatic) ? rimg : wimg, boxStatic.getX(), boxStatic.getY(), boxStatic.getWidth(), boxStatic.getHeight());
		batch.draw(isBoxColliding(boxStatic2) ? rimg : wimg, boxStatic2.getX(), boxStatic2.getY(), boxStatic2.getWidth(), boxStatic2.getHeight());
		batch.end();
	}

    public boolean isBoxColliding(NBPBox tBox) {
        for(NBPBox box : world.getWorldBoxes()) {
            if(!(tBox == box)) {
                if(NBPMath.doBoxesCollide(tBox, box)) {
                    return true;
                }
            }
        }
        return false;
    }
}
