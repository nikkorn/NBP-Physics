package com.dumbpug.main.gamedevicestesting.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.dumbpug.main.gamedevicestesting.player.Player;
import com.dumbpug.nbp.NBPMath;
import com.dumbpug.nbp.NBPPoint;

/**
 * Input for local player.
 * @author nikolas.howard
 *
 */
public class LocalPlayerInput implements IPlayerInput {
	private Player player;
	
	public LocalPlayerInput(Player player) {
		this.player = player;
	}

	@Override
	public boolean isUpPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.W);
	}

	@Override
	public boolean isDownPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.S);
	}

	@Override
	public boolean isLeftPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.A);
	}

	@Override
	public boolean isRightPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.D);
	}

	@Override
	public boolean isNum1Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_1);
	}

	@Override
	public boolean isNum2Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_2);
	}

	@Override
	public boolean isNum3Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_3);
	}

	@Override
	public boolean isNum4Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_4);
	}

	@Override
	public boolean isNum5Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_5);
	}

	@Override
	public boolean isNum6Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_6);
	}
	
	@Override
	public boolean isNum7Pressed() {
		return Gdx.input.isKeyPressed(Input.Keys.NUM_7);
	}

	@Override
	public boolean isEnterPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.ENTER);
	}

	@Override
	public boolean isRightMouseButtonDown() {
		return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}

	@Override
	public boolean isLeftMouseButtonDown() {
		return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}

	@Override
	public float getAngleOfFocus() {
		return NBPMath.getAngleBetweenPoints(player.getCurrentOriginPoint(), new NBPPoint(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
	}

	@Override
	public boolean isCycleWeaponForwardButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.E);
	}

	@Override
	public boolean isCycleWeaponBackwardButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.Q);
	}
}
