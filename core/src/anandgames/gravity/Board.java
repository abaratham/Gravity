package anandgames.gravity;

import java.util.ArrayList;
import java.util.Random;

import anandgames.gravity.entities.Asteroid;
import anandgames.gravity.entities.Boss;
import anandgames.gravity.entities.Bullet;
import anandgames.gravity.entities.Enemy;
import anandgames.gravity.entities.Entity;
import anandgames.gravity.entities.Planet;
import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.entities.Tank;
import anandgames.gravity.entities.Pickup;
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
	private int counter = 0, currentWave = 0;
	private boolean inGame = true;
	private ArrayList<Pickup> pickups;
	private ArrayList<Asteroid> asteroids;
	private TweenManager tManager;
	private Vector2 fireLoc;

	// TESTING TOOLS, DELETE WHEN DONE
	public boolean collisions = true;

	public Board(GameScreen gs) {
		game = gs;
		ship = new PlayerShip(this);
		initEnemies();
		pickups = new ArrayList<Pickup>();
		initTweenManager();
		initPlanets();
		asteroids = new ArrayList<Asteroid>();
	}

	// Set up the Tween Manager
	public void initTweenManager() {
		tManager = new TweenManager();
		Tween.registerAccessor(Entity.class, new OrientationTweenAccessor());
	}

	// Initialize enemies
	public void initEnemies() {
		inGame = true;
		currentWave = 1;
		enemies = new ArrayList<Enemy>();
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
			int dist = (int) ship.getDistanceTo(p);
			if (dist < ship.getRadius() + p.getRadius())
				return true;
		}
		return false;
	}

	// Generate a new wave of enemies
	public void newWave() {
		Random r = new Random();
		if (currentWave % 5 == 0) {
			enemies.add(new Boss(new Vector2(r.nextInt(getWidth()), r
					.nextInt(getHeight())), this, currentWave));
			return;
		}
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
		Pickup pkup;
		float maxX = ship.getPosition().x + (Gdx.graphics.getWidth() / 2), minX = ship
				.getPosition().x - (Gdx.graphics.getWidth() / 2), maxY = ship
				.getPosition().y + (Gdx.graphics.getHeight() / 2), minY = ship
				.getPosition().y - (Gdx.graphics.getHeight() / 2);
		// use to weight weapon spawns
		double prob = Math.random();
		if (prob < .1)
			pkup = new Pickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), Pickup.BOMB, this);
		else if (prob < .2)
			pkup = new Pickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), Pickup.FLAMETHROWER, this);
		else if (prob < .3)
			pkup = new Pickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), Pickup.RIFLE, this);
		else
			pkup = new Pickup(new Vector2((float) (Math.random()
					* (maxX - minX) + minX), (float) (Math.random()
					* (maxY - minY) + minY)), Pickup.SHOTGUN, this);
		Tween.to(pkup, 0, 4.0f).target(360).repeat(-1, 0f).start(tManager);
		pickups.add(pkup);

	}

	// Move and re-orient all entities, spawn a new weapon randomly, check for
	// collisions, and update enemies
	// if needed
	public void update() {
		// Update tween manager
		tManager.update(.032f);
		double f = Math.random();
		if (f <= 0.05) {
			spawnAsteroid();
		}
		if (f <= .002)
			spawnWeapon();

		// Start a new wave if all enemies are dead
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

		// Move and re-orient all enemies and enemy bullets
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.move();

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

		// Move the ship
		ship.move();

		int limit = ship.getWeapon().getLimiter();

		// Fire at a limited rate if the mouse is held
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

	// Return the current wave of enemies
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	// Check for player-enemy and enemy-bullet collisions
	public void checkCollisions() {
		ArrayList<Bullet> bullets = ship.getBullets();

		// Check player-pickup collisions
		for (Pickup w : pickups) {
			// Ship picked up the pickup
			if (ship.collidesWith(w)) {
				pickups.remove(w);
				String msg;
				// Inform player of which weapon they picked up
				switch (w.getId()) {
				case Pickup.FLAMETHROWER:
					ship.setWeapon(new FlameThrower());
					msg = "flamethrower";
					break;
				case Pickup.SHOTGUN:
					ship.setWeapon(new Shotgun());
					msg = "shotgun";
					break;
				case Pickup.RIFLE:
					ship.setWeapon(new Rifle());
					msg = "rifle";
					break;
				case Pickup.MONEY:
					int val = new Random().nextInt(99) + 1;
					ship.addMoney(val);
					msg = "$" + val;
					break;
				case Pickup.BOMB:
					ship.addBomb();
					msg = "a bomb";
					break;
				default:
					msg = "lolbug";
					break;
				}
				game.showMessage("Picked up " + msg);
				break;
			}
		}

		// Check all enemy collisions
		for (int i = 0; i < enemies.size(); i++) {
			// Check player-enemy collisions
			Enemy e = enemies.get(i);
			if (ship.collidesWith(e)) {
				// The ship has a shield around it
				if (ship.isShielded())
					ship.setShielded(false);
				else {
					inGame = false;
				}
			}
			// Check bullet-enemy collisions for each bullet
			for (int j = 0; j < bullets.size(); j++) {
				Bullet b = bullets.get(j);
				if (b.collidesWith(e)) {
					// The bullet collided with a Tank that has hp > 0
					if (e instanceof Tank && ((Tank) e).getHp() > 0) {
						Tank t = (Tank) e;
						t.setHp(t.getHp() - 1);
						game.getSound("Explosion").play(.7f);
						bullets.remove(j);
						game.addExplosion((int) t.getPosition().x,
								(int) t.getPosition().y);
						t.getPosition().x -= 10 * Math.cos(t.getOrientation());
						t.getPosition().y -= 10 * Math.sin(t.getOrientation());
					} else {
						// The enemy died
						game.addExplosion((int) e.getPosition().x,
								(int) e.getPosition().y);
						game.getSound("Explosion").play(.7f);
						bullets.remove(j);
						ship.setScore(ship.getScore() + 10);
						enemies.remove(e);
						pickups.add(new Pickup(e.getPosition(), Pickup.MONEY,
								this));
						if (e instanceof Boss) {
							((Boss) e).spawnTanks();
							ship.addBomb();
							game.showMessage("Picked up a Bomb");
						}
						break;
					}
					// if (enemies.size() == 0)
					// break;
				}

			}

			// Check Asteroid collisions
			for (int k = 0; k < asteroids.size(); k++) {
				if (e.collidesWith(asteroids.get(k))) {
					game.addExplosion((int) e.getPosition().x,
							(int) e.getPosition().y);
					game.getSound("Explosion").play(.7f);
					enemies.remove(i);
					if (enemies.size() == 0)
						return;
				}

				if (ship.collidesWith(asteroids.get(k))) {
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

	public PlayerShip getShip() {
		return ship;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
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

	public ArrayList<Pickup> getPickups() {
		return pickups;
	}

	public void setPickups(ArrayList<Pickup> weaponList) {
		this.pickups = weaponList;
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

	public ArrayList<Planet> getPlanets() {
		return planets;
	}

	public int getCurrentWave() {
		return currentWave;
	}
}
