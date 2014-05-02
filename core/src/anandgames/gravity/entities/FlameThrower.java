package anandgames.gravity.entities;

import java.awt.Point;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class FlameThrower extends Weapon {
	public FlameThrower(Board b, Vector2 pos) {
		super(pos, "flamethrower", new Vector2(1, 10), new Vector2(1, 11), 150,
				1, 14, 0, "GravityData/Sounds/Flame.wav", b);
	}

}
