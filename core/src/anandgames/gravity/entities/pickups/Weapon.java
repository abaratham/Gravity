package anandgames.gravity.entities.pickups;

import anandgames.gravity.Board;
import anandgames.gravity.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class Weapon extends Entity {

	private int radius, ammo, limiter, ammoPerShot, ammoRadius;
	private String name;
	private Vector2 bulletKey;
	private Board board;
	private double orientation;
	private Sound fire;

	public Weapon(Vector2 pos, String name, Vector2 spriteKey, Vector2 bulletKey,
			int ammo, int ammoPerShot, int ammoRadius, int limiter,
			String soundPath, Board board) {
		super(pos, 0,0,0,spriteKey, board);
		setName(name);
		setRadius(16);
		setAmmo(ammo);
		setLimiter(limiter);
		setBulletKey(bulletKey);
		setAmmoPerShot(ammoPerShot);
		setAmmoRadius(ammoRadius);
		fire = Gdx.audio.newSound(Gdx.files.internal(soundPath));
	}

	public boolean equals(Weapon w) {
		return name == w.getName();
	}

	public float getY() {
		return getPosition().y;
	}

	public float getX() {
		return getPosition().x;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public double getOrientation() {
		return orientation;
	}

	public void setOrientation(double orientation) {
		this.orientation = orientation;
	}

	public void setRadius(int r) {
		radius = r;
	}

	public int getRadius() {
		return radius;
	}

	public void setAmmo(int a) {
		ammo = a;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setLimiter(int lim) {
		limiter = lim;
	}

	public int getLimiter() {
		return limiter;
	}

	public Sound getSound() {
		return fire;
	}

	public void setSound(Sound s) {
		fire = s;
	}

	public int getAmmoPerShot() {
		return ammoPerShot;
	}

	public void setAmmoPerShot(int ammoPerShot) {
		this.ammoPerShot = ammoPerShot;
	}

	public Vector2 getBulletKey() {
		return bulletKey;
	}

	public void setBulletKey(Vector2 bulletKey) {
		this.bulletKey = bulletKey;
	}

	public int getAmmoRadius() {
		return ammoRadius;
	}

	public void setAmmoRadius(int ammoRadius) {
		this.ammoRadius = ammoRadius;
	}

}
