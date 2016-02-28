package com.dumbpug.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.nbp.*;

/**
 * Testing for the physics engine.
 * @author Nikolas Howard
 *
 */
public class NBPGrid extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wimg;
    Texture rimg;
    Texture simg;

    NBPWorld world;
    PlayerBox player;
    NBPBox box2;
    NBPBox boxStatic;
    NBPBox boxStatic2;
    NBPBox box4;

    @Override
    public void create() {
        batch = new SpriteBatch();
        wimg = new Texture("wbox.png");
        rimg = new Texture("rbox.png");
        simg = new Texture("sbox.png");

        world = new NBPWorld(0.09f, 4f);

        // Make our player box.
        player = new PlayerBox(200, 200, 20, 40);

        box2 = new NBPBox(30, 120, 100, 10, NBPBoxType.STATIC);
        box2.setName("box2");

        // Make bouncy box
        boxStatic = new NBPBox(190, 120, 100, 10, NBPBoxType.STATIC);
        boxStatic.setName("boxStatic");
        boxStatic.setRestitution(0.5f);

        boxStatic2 = new NBPBox(350, 120, 100, 10, NBPBoxType.STATIC);
        boxStatic2.setName("boxStatic2");

        // Make super slippery box.
        box4 = new NBPBox(500, 120, 100, 10, NBPBoxType.STATIC);
        box4.setFriction(0.2f);
        box4.setName("box4");

        world.addBox(player);
        world.addBox(box2);
        world.addBox(boxStatic);
        world.addBox(boxStatic2);
        world.addBox(box4);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        world.update();

        // Test moving box1
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }

        // Test jumping box1
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.jump();
        }

        batch.begin();
        // Draw boxes
        batch.draw(isBoxColliding(player) ? rimg : wimg, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        batch.draw(isBoxColliding(box2) ? rimg : wimg, box2.getX(), box2.getY(), box2.getWidth(), box2.getHeight());
        batch.draw(isBoxColliding(boxStatic) ? rimg : wimg, boxStatic.getX(), boxStatic.getY(), boxStatic.getWidth(), boxStatic.getHeight());
        batch.draw(isBoxColliding(boxStatic2) ? rimg : wimg, boxStatic2.getX(), boxStatic2.getY(), boxStatic2.getWidth(), boxStatic2.getHeight());
        batch.draw(isBoxColliding(box4) ? rimg : wimg, box4.getX(), box4.getY(), box4.getWidth(), box4.getHeight());
        // Draw the sensors for player
        for (NBPSensor sensor : player.getAttachedSensors()) {
            batch.draw(simg, sensor.getX(), sensor.getY(), sensor.getWidth(), sensor.getHeight());
        }
        batch.end();
    }

    public boolean isBoxColliding(NBPBox tBox) {
        for (NBPBox box : world.getWorldBoxes()) {
            if (!(tBox == box)) {
                if (NBPMath.doBoxesCollide(tBox, box)) {
                    return true;
                }
            }
        }
        return false;
    }
}