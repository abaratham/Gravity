package anandgames.gravity.weapons;

import com.badlogic.gdx.math.Vector2;

public class FlameThrower extends Weapon {

	public FlameThrower() {
		super(new Vector2(1, 11), 150,
				1, 14, 0, "GravityData/Sounds/Flame.wav");
	}
}
