package anandgames.gravity.entities.pickups;

import anandgames.gravity.Board;
import anandgames.gravity.entities.Entity;

import com.badlogic.gdx.math.Vector2;

public class Money extends Entity {

	private int value;

	public Money(Vector2 pos, int val, Board b) {
		super(pos, 0, 0, 0, new Vector2(), b);
		setValue(val);
		setRadius(16);
		setSpriteKey(new Vector2(0, 7));
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
