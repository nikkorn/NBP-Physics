package com.dumbpug.harness.launchers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dumbpug.harness.Basic2DBox;
import com.dumbpug.harness.PlayerBox;
import com.dumbpug.nbp.Axis;
import com.dumbpug.nbp.Box;
import com.dumbpug.nbp.BoxType;
import com.dumbpug.nbp.Dimension;
import com.dumbpug.nbp.Environment;
import com.dumbpug.nbp.Gravity;

/**
 * A 2D launcher with grd based tiles and a player.
 */
public class Grid2DLauncher extends ApplicationAdapter {
    SpriteBatch batch;
    Texture wimg;
    Texture gimg;

    Environment environment;
    PlayerBox player;

    private int gridBlockSize = 60;
    private int[][] gridLayout = {
            {1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1},
            {1,0,1,0,1,0,1},
            {1,0,0,0,0,0,1},
            {1,0,1,1,1,0,1},
            {1,0,0,1,0,0,1},
            {1,1,1,1,1,1,1}
    };

    @Override
    public void create() {
        batch = new SpriteBatch();
        wimg  = new Texture("white_box.png");
        gimg  = new Texture("green_box.png");

        environment = new Environment(Dimension.TWO_DIMENSIONS, 200f, new Gravity(Axis.Y, -0.09f));

        // Create our grid.
        float gridStartX = 0;
        float gridStartY = gridLayout.length*gridBlockSize;
        for(int gridX = 0; gridX < gridLayout.length; gridX++) {
            for(int gridY = 0; gridY < gridLayout.length; gridY++) {
                if(gridLayout[gridY][gridX] == 1) {
                    Box gridBlock = new Basic2DBox(gridStartX + (gridX*gridBlockSize),
                            gridStartY-(gridY*gridBlockSize), gridBlockSize, gridBlockSize, BoxType.STATIC);
                    gridBlock.setName("grid_piece");
                    environment.addBox(gridBlock);
                }
            }
        }

        // Make our player box.
        player = new PlayerBox(65, 200, 20, 40);
        environment.addBox(player);
    }
    

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());

        environment.update();

        // Test moving player
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }

        // Test jumping player
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.jump();
        }
        
        batch.begin();
        for(Box box : environment.getBoxes()) {
        	if (box.getType() == BoxType.DYNAMIC) {
        		// Draw dynamic boxes.
        		batch.draw(gimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	} else {
        		// Draw static boxes (grid).
        		batch.draw(wimg, box.getX(), box.getY(), box.getWidth(), box.getHeight());
        	}
        }
        batch.end();
    }
}
