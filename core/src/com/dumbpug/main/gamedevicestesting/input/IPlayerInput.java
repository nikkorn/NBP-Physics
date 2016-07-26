package com.dumbpug.main.gamedevicestesting.input;

/**
 * Defines input from the player.
 * @author nikolas.howard
 *
 */
public interface IPlayerInput {
	public boolean isUpPressed();
	public boolean isDownPressed();
	public boolean isLeftPressed();
	public boolean isRightPressed();
	public boolean isNum1Pressed();
	public boolean isNum2Pressed();
	public boolean isNum3Pressed();
	public boolean isNum4Pressed();
	public boolean isNum5Pressed();
	public boolean isNum6Pressed();
	public boolean isEnterPressed();
	public boolean isRightMouseButtonDown();
	public boolean isLeftMouseButtonDown();
	public float getAngleOfFocus();
}
