package anandgames.gravity.entities;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Asteroid extends Entity {

	public Asteroid(Board b) {
		super(null, 0, 0, 0, new Vector2(0, 3), b);
		// Set random values for position, orientation and speed
		int x = (int) (Math.random() * (b.getWidth() * 2) - b.getWidth());
		int y = (int) (Math.random() * (b.getHeight() * 2) - b.getHeight());
		setPosition(new Vector2(x, y));
		setOrientation((int) (Math.random() * (360)));
		setMaxSpeed((int) (Math.random() * (10 - 5) + 5));
		// Asteroid is moving at full speed immediately
		float vx = (float) Math.cos(Math.toRadians(getOrientation()))
				* getMaxSpeed();
		float vy = (float) Math.sin(Math.toRadians(getOrientation()))
				* getMaxSpeed();
		setVelocity(new Vector2(vx, vy));
		System.out.println(getVelocity());
		setAcceleration(new Vector2(0, 0));
		setRadius(60);
	}

	public void move() {
		super.move();
		Vector2 pos = getPosition();
		//Remove the asteroid once it is outside the bounds of the board
		if (pos.x > getBoard().getWidth() || pos.x < 0
				|| pos.y > getBoard().getHeight() || pos.y < 0)
			getBoard().getAsteroids().remove(this);
	}

}
