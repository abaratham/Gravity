package anandgames.gravity.weapons;

import com.badlogic.gdx.math.Vector2;

public class Pistol extends Weapon {

	public Pistol() {
		super(new Vector2(0, 2), 0, 1, 7, 12,
				"GravityData/Sounds/Laser_Shoot.wav");
	}
}
