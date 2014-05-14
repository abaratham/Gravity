package anandgames.gravity.entities; 

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity {


	public Bullet(Vector2 startPos, double orientation,int radius, Board board) {
		super(startPos, orientation, 25, 25, new Vector2(0, 2), board);
		setRadius(radius);
		// Bullet is moving as soon as it is fired
		getVelocity().x = (float) (Math.cos(orientation) * getMaxSpeed());
		getVelocity().y = (float) (Math.sin(orientation) * getMaxSpeed());

	}

	public void move() {
		getPosition().add(getVelocity());
	}

	
}
