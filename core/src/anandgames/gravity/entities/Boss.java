package anandgames.gravity.entities;

import java.util.ArrayList;
import java.util.Random;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Boss extends Tank {

	// id the level at which this boss spawns
	private int id;

	public Boss(Vector2 startPos, Board board, int id) {
		super(startPos, board);
		setSpriteKey(new Vector2(2, 0));
		this.id = id;
		setRadius(40);
		setHp(id);
		setMaxSpeed(getMaxSpeed() + 2);
	}

	public void spawnTanks() {
		ArrayList<Enemy> b = getBoard().getEnemies();
		Random r = new Random();
		for (int i = 0; i < id; i++) {
			int dx = r.nextInt(300) - 150, dy = r.nextInt(300) - 150;
			b.add(new Tank(new Vector2(getPosition().x + dx, getPosition().y
					+ dy), getBoard()));
		}
		b.remove(this);
	}

}
