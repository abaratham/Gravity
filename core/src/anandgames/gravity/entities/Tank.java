package anandgames.gravity.entities;

import java.util.ArrayList;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Tank extends Enemy {

	private int hp;
	private ArrayList<Bullet> bullets;
	private int counter;

	public Tank(Vector2 startPos, Board board) {
		super(startPos, board);
		setHp(5);
		bullets = new ArrayList<Bullet>();
		setSpriteKey(new Vector2(0, 0));
	}

	public void fire() {
		// for (int i = 0; i < 7; i++) {
		bullets.add(new Bullet(new Vector2(getPosition().x + getRadius(),
				getPosition().y + getRadius()), getOrientation(), 7, getBoard()));
		// }
		System.out.println("FIRE");
	}

	public void move() {
		super.move();
		float x = getPosition().x, y = getPosition().y, px = getBoard()
				.getShip().getPosition().x, py = getBoard().getShip()
				.getPosition().y;

		if (x < px + (getBoard().getWidth() / 2)
				&& x > px - (getBoard().getWidth() / 2)
				&& y < py + (getBoard().getHeight() / 2)
				&& y > py - (getBoard().getHeight() / 2))
			if (counter >= 50) {
//				fire();
				counter = 0;
			} else
				counter++;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}
}
