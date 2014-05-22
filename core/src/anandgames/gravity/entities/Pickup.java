package anandgames.gravity.entities;

import anandgames.gravity.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class Pickup extends Entity {

	public static final int FLAMETHROWER = 0;
	public static final int RIFLE = 1;
	public static final int SHOTGUN = 2;
	public static final int BOMB = 3;
	public static final int MONEY = 4;

	private int id;

	public Pickup(Vector2 pos, int id, Board board) {
		super(pos, 0, 0, 0, new Vector2(), board);
		setId(id);
		setRadius(16);
		// Set SpriteKey based on id
		switch (id) {
		case FLAMETHROWER:
			setSpriteKey(new Vector2(0, 5));
			break;
		case SHOTGUN:
			setSpriteKey(new Vector2(0, 4));
			break;
		case RIFLE:
			setSpriteKey(new Vector2(0, 6));
			break;
		case BOMB:
			setSpriteKey(new Vector2(0, 9));
			break;
		case MONEY:
			setSpriteKey(new Vector2(0, 7));
			break;
		}
		// "Move" the pickup so that if it is off the board, it will be placed
		// back on the board
		move();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
