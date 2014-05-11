package anandgames.gravity.entities.pickups;

import anandgames.gravity.Board;
import anandgames.gravity.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class WeaponPickup extends Entity {
	
	public static final int FLAMETHROWER = 0;
	public static final int RIFLE = 1;
	public static final int SHOTGUN = 2;

	private int id;

	public WeaponPickup(Vector2 pos, int id, Board board) {
		super(pos, 0, 0, 0, new Vector2(), board);
		setId(id);
		setRadius(16);
		switch(id) {
		case FLAMETHROWER:
			setSpriteKey(new Vector2(0,5));
			break;
		case SHOTGUN:
			setSpriteKey(new Vector2(0, 4));
			break;
		case RIFLE:
			setSpriteKey(new Vector2(0, 5));
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
