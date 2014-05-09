package anandgames.gravity.weapons;

import com.badlogic.gdx.math.Vector2;

public class Shotgun extends Weapon {

	public Shotgun() {
		super(new Vector2(0, 2), 75, 3, 7, 14,
				"GravityData/Sounds/Laser_Shoot.wav");
	}
}
