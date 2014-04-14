package jagodzinski.steve.hw5.avoidtheblocks.model;

import java.util.Collection;
import java.util.LinkedList;

import android.graphics.Point;

public class GameState {

	private short easyLocation;
	private short currentLocation;
	private Difficulty difficulty;

	private Collection<Point> blocks = new LinkedList<Point>();

	private Point playerPosition;
	private float playerVelocity;
	private float playerAcceleration;

	public short getEasyLocation() {
		return easyLocation;
	}

	public void setEasyLocation(short easyLocation) {
		this.easyLocation = easyLocation;
	}

	public short getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(short currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Collection<Point> getBlocks() {
		return blocks;
	}

	public void setBlocks(Collection<Point> blocks) {
		this.blocks = blocks;
	}

	public Point getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(Point playerPosition) {
		this.playerPosition = playerPosition;
	}

	public float getPlayerVelocity() {
		return playerVelocity;
	}

	public void setPlayerVelocity(float playerVelocity) {
		this.playerVelocity = playerVelocity;
	}

	public float getPlayerAcceleration() {
		return playerAcceleration;
	}

	public void setPlayerAcceleration(float playerAcceleration) {
		this.playerAcceleration = playerAcceleration;
	}
}
