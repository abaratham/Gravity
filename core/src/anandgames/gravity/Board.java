package anandgames.gravity;

import java.util.ArrayList;
import java.util.Random;

import anandgames.gravity.entities.Asteroid;
import anandgames.gravity.entities.Bullet;
import anandgames.gravity.entities.Enemy;
import anandgames.gravity.entities.FlameThrower;
import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.entities.Rifle;
import anandgames.gravity.entities.Shotgun;
import anandgames.gravity.entities.Weapon;
import anandgames.gravity.screens.GameScreen;
import anandgames.gravity.tweens.ShotgunTweenAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Board {

	private PlayerShip ship;
	private ArrayList<Enemy> enemies;
	private GameScreen game;
	private int width = 8192;
	private ArrayList<Planet> planets;
	private int height = 8192;
	private int counter = 0, currentPhase = 0, currentWave = 0;
	private boolean inGame = true;
	private ArrayList<Weapon> weaponList;
	private ArrayList<Asteroid> asteroids;
	private TweenManager tManager;
	private Vector2 fireLoc;

	// TESTING TOOLS, DELETE WHEN DONE
	public boolean collisions = true;

	public Board(GameScreen gs) {
		game = gs;
		ship = new PlayerShip(this);
		initEnemies();
		weaponList = new ArrayList<Weapon>();
		initTweenManager();
		initPlanets();
		asteroids = new ArrayList<Asteroid>();
		// for testing
		// spawnWeapon();
	}

	// Set up the Tween Manager
	public void initTweenManager() {
		tManager = new TweenManager();
		Tween.registerAccessor(Weapon.class, new ShotgunTweenAccessor());
	}

	// Initialize enemies
	public void initEnemies() {
		inGame = true;
		currentPhase = 0;
		currentWave = 0;
		enemies = new ArrayList<Enemy>();
		// initPlanets();
		newWave();
	}

	// Add Planets to the Board
	public void initPlanets() {
		planets = new ArrayList<Planet>();
		planets.add(new Planet("Moon", 768, 8192 - 2624, 224, 1.5f));
		planets.add(new Planet("Mars", 6976, 8192 - 896, 96, .7f));
		planets.add(new Planet("Marsh", 2560, 8192 - 6080, 160, 1f));
		planets.add(new Planet("Jungle", 3776, 8192 - 2816, 160, 1.2f));
		planets.add(new Planet("Base", 3776, 8192 - 4880, 128, .7f));
		planets.add(new Planet("Earth", 6976, 8192 - 6080, 256, 2f));
	}

	// Check if the ship is over a planet
	public boolean isOnPlanet() {
		for (int i = 0; i < planets.size(); i++) {
			Planet p = planets.get(i);
			int dist = (int) Math
					.sqrt((Math.pow(
							ship.getPosition().x + ship.getRadius() - p.getX(),
							2) + (Math.pow(
							ship.getPosition().y + ship.getRadius() - p.getY(),
							2))));
			if (dist < ship.getRadius() + p.getRadius())
				return true;
		}
		return false;
	}

	// Generate a new wave of enemies
	public void newWave() {
		Random r = new Random();
		for (int i = 0; i < 5 * currentWave; i++) {
			enemies.add(new Enemy(new Vector2(r.nextInt(getWidth()), r
					.nextInt(getHeight())), this));
		}
	}

	// Spawn a new asteroid
	public void spawnAsteroid() {
		asteroids.add(new Asteroid(this));
	}

	// Reset the game to the first wave of enemies and ship starting position
	public void reset() {
		ship = new PlayerShip(this);
		initEnemies();
	}

	// Spawn a new random Weapon at a random location
	public void spawnWeapon() {
		Weapon wep;
		float maxX = ship.getPosition().x + (Gdx.graphics.getWidth() / 2), minX = ship
				.getPosition().x - (Gdx.graphics.getWidth() / 2), maxY = ship
				.getPosition().y + (Gdx.graphics.getHeight() / 2), minY = ship
				.getPosition().y - (Gdx.graphics.getHeight() / 2);
		// TODO: use to weight weapon spawns
		double prob = Math.random();
		if (prob < .33)
			wep = new FlameThrower(this, new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)));
		else if (prob < .67)
			wep = new Rifle(this, new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)));
		else
			wep = new Shotgun(this, new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)));
		Tween.to(wep, 0, 4.0f).target(360).repeat(-1, 0f).start(tManager);
		weaponList.add(wep);

	}

	// Move and re-orient all entities, spawn a new weapon randomly, check for
	// collisions, and update enemies
	// if needed
	public void update() {
		tManager.update(.032f);
		double f = Math.random();
		// TODO: pick the probability of a weapon spawn
		if (f <= 0.05) {
			spawnAsteroid();
		}
		if (f <= .005)
			spawnWeapon();
		if (enemies.size() == 0) {
			currentWave++;
			newWave();
			ship.getBullets().clear();

		}

		// Check collisions between all entities
		if (collisions)
			checkCollisions();

		// Check if entities are affected by any planets
		checkPlanetEffects();

		// Move and re-orient all enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (e.isVisible()) {
				e.move();
			}
		}

		// Move all bullets
		for (int i = 0; i < ship.getBullets().size(); i++) {
			Bullet b = ship.getBullets().get(i);
			b.move();
			if (b.getPosition().x > ship.getPosition().x
					+ Gdx.graphics.getWidth() / 2
					|| b.getPosition().x < ship.getPosition().x
							- Gdx.graphics.getWidth() / 2
					|| b.getPosition().y > ship.getPosition().y
							+ Gdx.graphics.getHeight() / 2
					|| b.getPosition().y < ship.getPosition().y
							- Gdx.graphics.getHeight() / 2)
				ship.getBullets().remove(b);
		}

		// Move all asteroids
		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).move();
		}

		// Move and re-orient the ship
		ship.move();
		// ship.reOrient();

		int limit;
		if (ship.getWeapon() == null)
			limit = 12;
		else {
			limit = ship.getWeapon().getLimiter();
		}
		// Fire if the mouse is held
		if (ship.isMouseHeld() && counter == limit) {
			if (fireLoc == null)
				ship.fire();
			else {
				ship.fire((int) fireLoc.x, (int) fireLoc.y);
			}
		} else if (!ship.isMouseHeld())
			counter = 0;

		// Create delay for MouseHeld firing
		if (counter == limit)
			counter = 0;
		else
			counter++;
	}

	public PlayerShip getShip() {
		return ship;
	}

	// Return the current wave of enemies
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	// Check for player-enemy and enemy-bullet collisions
	public void checkCollisions() {
		ArrayList<Bullet> bullets = ship.getBullets();

		// Check player-weapon collisions
		for (Weapon w : weaponList) {
			// Ship picked up the weapon
			if (ship.collidesWith(w)) {
				weaponList.remove(w);
				ship.setWeapon(w);
				game.showMessage("Picked up " + w.getName());
				break;
			}

		}

		for (int i = 0; i < enemies.size(); i++) {
			// Check player-enemy collisions
			Enemy e = enemies.get(i);
			if (ship.collidesWith(e)) {
				// The ship has a shield around it
				if (ship.isShielded())
					ship.setShielded(false);
				else {
					ship.setVisible(false);
					inGame = false;
				}
			}
			// Check bullet-enemy collisions for each bullet
			for (int j = 0; j < bullets.size(); j++) {
				if ((bullets.get(j)).collidesWith(e)) {
					e.setVisible(false);
					bullets.get(j).setVisible(false);
					game.addExplosion((int) e.getPosition().x,
							(int) e.getPosition().y);
					game.getSound("Explosion").play(.7f);
					bullets.remove(j);
					ship.setScore(ship.getScore() + 10);
					enemies.remove(i);
					if (enemies.size() == 0)
						return;
				}

			}

			// Check Asteroid collisions
			for (int k = 0; k < asteroids.size(); k++) {
				if (e.collidesWith(asteroids.get(k))) {
					e.setVisible(false);
					game.addExplosion((int) e.getPosition().x,
							(int) e.getPosition().y);
					game.getSound("Explosion").play(.7f);
					enemies.remove(i);
					if (enemies.size() == 0)
						return;
				}

				if (ship.collidesWith(asteroids.get(k))) {
					ship.setVisible(false);
					inGame = false;
				}

			}
		}

	}

	// Check if entities are within the range of planets
	// TODO: figure out why the ship revolves in a square...
	public void checkPlanetEffects() {
		Vector2 ship = this.ship.getPosition();
		for (Planet x : planets) {
			Vector2 planet = new Vector2(x.getX(), x.getY());

			float deltaX = (ship.x + this.ship.getRadius()) - (planet.x);
			float deltaY = (ship.y + this.ship.getRadius()) - (planet.y);
			float dist = (float) Math.sqrt(Math.pow(deltaX, 2)
					+ Math.pow(deltaY, 2));

			// The ship is within range of the planet
			if (dist <= x.getRange()) {
				float temp = (dist / x.getRange());
				float effect = x.getMaxEffect() - temp;
				double angle = Math.atan2(deltaY, deltaX) + (Math.PI);
				double effectX = (Math.cos(angle) * effect);
				double effectY = (Math.sin(angle) * effect);
				this.ship.getVelocity().x += effectX;
				this.ship.getVelocity().y += effectY;
			}
		}
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public int getWave() {
		return currentPhase;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ArrayList<Weapon> getWeaponList() {
		return weaponList;
	}

	public void setWeaponList(ArrayList<Weapon> weaponList) {
		this.weaponList = weaponList;
	}

	public TweenManager getTManager() {
		return tManager;
	}

	public ArrayList<Asteroid> getAsteroids() {
		return asteroids;
	}

	public Vector2 getFireLoc() {
		return fireLoc;
	}

	public void setFireLoc(Vector2 fireLoc) {
		this.fireLoc = fireLoc;
	}
}
