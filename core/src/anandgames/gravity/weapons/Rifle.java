package anandgames.gravity.weapons;

import com.badlogic.gdx.math.Vector2;

public class Rifle extends Weapon {

	public Rifle() {
		super(new Vector2(0, 2), 150, 1, 7, 3,
				"GravityData/Sounds/Laser_Shoot.wav");
	}
}
