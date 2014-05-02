package anandgames.gravity.entities;

import java.awt.Point;
import java.util.ArrayList;

import anandgames.gravity.Board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public class PlayerShip extends Entity {

	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private boolean upPressed, rightPressed, leftPressed, downPressed,
			mouseHeld, shielded;
	private int score, currentAmmo;
	private Sound fire;
	private Weapon weapon;
	private double oldOrientation;

	public PlayerShip(Board board) {
		// Always initialized at the center of the board
		super(new Vector2(board.getWidth() / 2, board.getHeight() / 2), 0, 15,
				2f, new Vector2(), board);
		setRadius(20);
		setScore(0);
		// Initialize default fire sound byte
		fire = Gdx.audio.newSound(Gdx.files
				.internal("GravityData/Sounds/Laser_Shoot.wav"));
		// Start with infinite ammo
		currentAmmo = 0;
		setMouseHeld(false);

	}

	// Fire a new bullet and add it to the ship's bullets list
	public void fire() {
		// Fire current weapon and subtract from ammo
		// If weapon is null, use default weapon
		if (weapon == null || weapon.getAmmoPerShot() == 1) {
			bullets.add(new Bullet(new Vector2(getPosition().x + 2,
					getPosition().y + 2), getOrientation(), 7, getBoard()));
			if (weapon != null)
				currentAmmo--;
		}
		// else fire equipped weapon
		else {
			// Make sure the ship doesn't fire more ammo than it has
			int limit;
			if (currentAmmo < weapon.getAmmoPerShot())
				limit = currentAmmo;
			else
				limit = weapon.getAmmoPerShot();
			for (int i = 0; i < limit; i++) {
				bullets.add(new Bullet(new Vector2(getPosition().x
						+ getRadius(), getPosition().y + getRadius()),
						getOrientation() + ((Math.PI / 12) * (i - 1)),
						getWeapon().getAmmoRadius(), getBoard()));
			}
			currentAmmo -= limit;


		}
		// Play correct sound
		// If out of ammo set weapon to default
		if (currentAmmo <= 0) {
			setWeapon(null);
		}
		Sound sound;
		if (weapon == null)
			sound = fire;
		else
			sound = weapon.getSound();
		sound.play(.5f);
		System.out.println(currentAmmo);

	}
	
	public void fire(int x, int y) {
		oldOrientation = getOrientation();
		reOrient(new Vector2(x, y));
		fire();
		setOrientation(oldOrientation);
	}

	// Fire a bullet when the mouse is pressed
	public void mousePressed() {
		fire();
		setMouseHeld(true);
	}

	//Firing a bullet(used for android)
	public void mousePressed(int x, int y) {
		fire(x, y);
		setMouseHeld(true);
	}

	public void mouseReleased() {
		if (oldOrientation != 0)
			setOrientation(oldOrientation);
		setMouseHeld(false);
	}

	// Handle key input
	public void keyPressed(int key) {

		if (key == Keys.A) {
			getAcceleration().x = -getMaxAcceleration();
			leftPressed = true;
		}

		if (key == Keys.D) {
			getAcceleration().x = getMaxAcceleration();
			rightPressed = true;
		}

		if (key == Keys.W) {
			getAcceleration().y = getMaxAcceleration();
			upPressed = true;
		}

		if (key == Keys.S) {
			getAcceleration().y = -getMaxAcceleration();
			downPressed = true;
		}

	}

	public void keyReleased(int key) {

		if (key == Keys.A) {
			if (!rightPressed) {
				getAcceleration().x = 0;
			}
			if (rightPressed)
				getAcceleration().x = getMaxAcceleration();
			leftPressed = false;
		}

		if (key == Keys.D) {
			if (!leftPressed) {
				getAcceleration().x = 0;
			}
			if (leftPressed) {
				getAcceleration().x = -getMaxAcceleration();
			}
			rightPressed = false;
		}

		if (key == Keys.W) {
			if (!downPressed) {
				getAcceleration().y = 0;
			}
			if (downPressed)
				getAcceleration().y = -getMaxAcceleration();
			upPressed = false;
		}

		if (key == Keys.S) {
			if (!upPressed) {
				getAcceleration().y = 0;
			}
			if (upPressed)
				getAcceleration().y = getMaxAcceleration();
			downPressed = false;
		}

		if (key == Keys.R) {
			getBoard().reset();
		}

	}

	// Orient the ship to face the mouse
	public void reOrient(Vector2 m) {
		float angle = (float) (Math.atan2(Gdx.graphics.getHeight() - m.y
				- (Gdx.graphics.getHeight() / 2),
				m.x - (Gdx.graphics.getWidth() / 2)));
		setOrientation(angle);
	}

	public boolean isMouseHeld() {
		return mouseHeld;
	}

	public void setMouseHeld(boolean mouseHeld) {
		this.mouseHeld = mouseHeld;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setWeapon(Weapon w) {
		if (w != null) {
			if (weapon != null && weapon.equals(w))
				currentAmmo += w.getAmmo();
			else
				currentAmmo = w.getAmmo();
		}
		weapon = w;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setCurrentAmmo(int ammo) {
		currentAmmo = ammo;
	}

	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public boolean isShielded() {
		return shielded;
	}

	public void setShielded(boolean s) {
		shielded = s;
	}
	

}
