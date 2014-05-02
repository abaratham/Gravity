package anandgames.gravity.entities;

import java.awt.Point;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Rifle extends Weapon {

	public Rifle(Board board, Vector2 pos) {
		super(pos, "rifle", new Vector2(1, 12), new Vector2(0, 2), 150, 1,
				7, 3, "GravityData/Sounds/Laser_Shoot.wav", board);
	}
}
