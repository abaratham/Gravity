package anandgames.gravity;

import java.util.ArrayList;
import java.util.Random;

import anandgames.gravity.entities.Asteroid;
import anandgames.gravity.entities.Bullet;
import anandgames.gravity.entities.Enemy;
import anandgames.gravity.entities.Entity;
import anandgames.gravity.entities.Planet;
import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.entities.Tank;
import anandgames.gravity.entities.pickups.Money;
import anandgames.gravity.entities.pickups.WeaponPickup;
import anandgames.gravity.screens.GameScreen;
import anandgames.gravity.tweens.OrientationTweenAccessor;
import anandgames.gravity.weapons.FlameThrower;
import anandgames.gravity.weapons.Rifle;
import anandgames.gravity.weapons.Shotgun;
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
	private ArrayList<WeaponPickup> weaponList;
	private ArrayList<Asteroid> asteroids;
	private TweenManager tManager;
	private Vector2 fireLoc;
	private ArrayList<Money> moneyList;

	// TESTING TOOLS, DELETE WHEN DONE
	public boolean collisions = true;

	public Board(GameScreen gs) {
		game = gs;
		ship = new PlayerShip(this);
		initEnemies();
		weaponList = new ArrayList<WeaponPickup>();
		initTweenManager();
		initPlanets();
		asteroids = new ArrayList<Asteroid>();
		moneyList = new ArrayList<Money>();
	}

	// Set up the Tween Manager
	public void initTweenManager() {
		tManager = new TweenManager();
		Tween.registerAccessor(Entity.class, new OrientationTweenAccessor());
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
		Random r = new Random();
		// Add the 7 planets at randomly generated locations
		for (int i = 0; i < 7; i++) {
			Planet p = new Planet(new Vector2(r.nextInt(width),
					r.nextInt(height)), new Vector2(1, i),
					r.nextInt(200) + 100, this);
			// Prevent planets from "hanging" over the edge of the world
			if (p.getPosition().x > getWidth() - p.getRadius())
				p.getPosition().x = getWidth() - p.getRadius();
			if (p.getPosition().x < p.getRadius())
				p.getPosition().x = p.getRadius();
			if (p.getPosition().y > getHeight() - p.getRadius())
				p.getPosition().y = getHeight() - p.getRadius();
			if (p.getPosition().y < p.getRadius())
				p.getPosition().y = p.getRadius();
			planets.add(p);
			// Start rotating the planets at a random speed
			Tween.to(p, 0, (float) r.nextInt(40) + 10).target(360)
					.repeat(-1, 0f).start(tManager);
		}
	}

	// Check if the ship is over a planet
	public boolean isOnPlanet() {
		for (int i = 0; i < planets.size(); i++) {
			Planet p = planets.get(i);
			int dist = (int) Math
					.sqrt((Math.pow(
							ship.getPosition().x + ship.getRadius()
									- p.getPosition().x, 2) + (Math.pow(
							ship.getPosition().y + ship.getRadius()
									- p.getPosition().y, 2))));
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
		if (currentWave >= 5) {
			for (int i = 0; i < currentWave; i++) {
				enemies.add(new Tank(new Vector2(r.nextInt(getWidth()), r
						.nextInt(getHeight())), this));
			}
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
		WeaponPickup wep;
		float maxX = ship.getPosition().x + (Gdx.graphics.getWidth() / 2), minX = ship
				.getPosition().x - (Gdx.graphics.getWidth() / 2), maxY = ship
				.getPosition().y + (Gdx.graphics.getHeight() / 2), minY = ship
				.getPosition().y - (Gdx.graphics.getHeight() / 2);
		// TODO: use to weight weapon spawns
		double prob = Math.random();
		if (prob < .33)
			wep = new WeaponPickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), WeaponPickup.FLAMETHROWER, this);
		else if (prob < .67)
			wep = new WeaponPickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), WeaponPickup.SHOTGUN, this);
		else
			wep = new WeaponPickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), WeaponPickup.RIFLE, this);
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
		if (f <= .001)
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
			if (e instanceof Tank) {
				Tank t = (Tank) e;
				for (int j = 0; j < t.getBullets().size(); j++)
					t.getBullets().get(j).move();
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

		int limit = ship.getWeapon().getLimiter();

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
		for (WeaponPickup w : weaponList) {
			// Ship picked up the weapon
			if (ship.collidesWith(w)) {
				weaponList.remove(w);
				String msg;
				switch (w.getId()) {
				case WeaponPickup.FLAMETHROWER:
					ship.setWeapon(new FlameThrower());
					msg = "flamethrower";
					break;
				case WeaponPickup.SHOTGUN:
					ship.setWeapon(new Shotgun());
					msg = "shotgun";
					break;
				case WeaponPickup.RIFLE:
					ship.setWeapon(new Rifle());
					msg = "rifle";
					break;
				default:
					msg = "lolbug";
				}
				game.showMessage("Picked up " + msg);
				break;
			}
		}

		// Check player-money collisions
		for (int i = 0; i < moneyList.size(); i++) {
			Money m = moneyList.get(i);
			if (ship.collidesWith(m)) {
				ship.addMoney(m.getValue());
				moneyList.remove(m);
				game.showMessage("Picked up $" + m.getValue());
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
					if (e instanceof Tank && ((Tank) e).getHp() > 0) {
						Tank t = (Tank) e;
						t.setHp(t.getHp() - 1);
						game.getSound("Explosion").play(.7f);
						bullets.remove(j);
						game.addExplosion((int) t.getPosition().x,
								(int) t.getPosition().y);
						t.setVelocity(new Vector2(0, 0));
					} else {
						e.setVisible(false);
						bullets.get(j).setVisible(false);
						game.addExplosion((int) e.getPosition().x,
								(int) e.getPosition().y);
						game.getSound("Explosion").play(.7f);
						bullets.remove(j);
						ship.setScore(ship.getScore() + 10);
						enemies.remove(i);
						int val = new Random().nextInt(100);
						moneyList.add(new Money(e.getPosition(), val, this));
					}
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
			Vector2 planet = new Vector2(x.getPosition().x, x.getPosition().y);

			float deltaX = (ship.x + this.ship.getRadius())
					- (planet.x + x.getRadius());
			float deltaY = (ship.y + this.ship.getRadius())
					- (planet.y + x.getRadius());
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

	public ArrayList<WeaponPickup> getWeaponList() {
		return weaponList;
	}

	public void setWeaponList(ArrayList<WeaponPickup> weaponList) {
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

	public ArrayList<Money> getMoneyList() {
		return moneyList;
	}

	public ArrayList<Planet> getPlanets() {
		return planets;
	}
}
