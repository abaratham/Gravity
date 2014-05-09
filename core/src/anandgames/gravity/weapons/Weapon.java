package anandgames.gravity.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class Weapon {

	private int ammo, limiter, ammoPerShot, ammoRadius;
	private Vector2 bulletKey;
	private Sound fire;

	public Weapon(Vector2 bulletKey, int ammo, int ammoPerShot, int ammoRadius,
			int limiter, String soundPath) {
		setAmmo(ammo);
		setLimiter(limiter);
		setBulletKey(bulletKey);
		setAmmoPerShot(ammoPerShot);
		setAmmoRadius(ammoRadius);
		setFire(Gdx.audio.newSound(Gdx.files.internal(soundPath)));
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public int getLimiter() {
		return limiter;
	}

	public void setLimiter(int limiter) {
		this.limiter = limiter;
	}

	public int getAmmoPerShot() {
		return ammoPerShot;
	}

	public void setAmmoPerShot(int ammoPerShot) {
		this.ammoPerShot = ammoPerShot;
	}

	public int getAmmoRadius() {
		return ammoRadius;
	}

	public void setAmmoRadius(int ammoRadius) {
		this.ammoRadius = ammoRadius;
	}

	public Vector2 getBulletKey() {
		return bulletKey;
	}

	public void setBulletKey(Vector2 bulletKey) {
		this.bulletKey = bulletKey;
	}

	public Sound getFire() {
		return fire;
	}

	public void setFire(Sound fire) {
		this.fire = fire;
	}

}
