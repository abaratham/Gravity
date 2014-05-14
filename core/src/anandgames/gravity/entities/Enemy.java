package anandgames.gravity.entities;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {


	//Standard "Chaser" enemy
	public Enemy(Vector2 startPos, Board board) {
		super(startPos, 0, 12, .75f, new Vector2(0,1), board);
		setRadius(20);
	}

	//Face the player and move
	public void move() {
		setOrientation(getDirectionTowardShip());
		getAcceleration().x = ((float)Math.cos(getOrientation()) * getMaxAcceleration());
		getAcceleration().y = ((float)Math.sin(getOrientation()) * getMaxAcceleration());
		super.move();
	}

	//Find direction toward the player
	public double getDirectionTowardShip() {
		PlayerShip ship = getBoard().getShip();
		float dx = getPosition().x - ship.getPosition().x, dy = getPosition().y - ship.getPosition().y;
		double angle = Math.atan2(dy, dx);
			return angle + (Math.PI);

	}

}
