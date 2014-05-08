package anandgames.gravity.entities.pickups;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Shotgun extends Weapon {
	public Shotgun(Board b, Vector2 pos) {
		super(pos, "shotgun", new Vector2(1, 9), new Vector2(0, 2), 75, 3, 7,
				14, "GravityData/Sounds/Laser_Shoot.wav", b);
	}
}
