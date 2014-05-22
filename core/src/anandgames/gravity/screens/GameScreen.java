package anandgames.gravity.screens;

import java.util.ArrayList;
import java.util.LinkedList;

import anandgames.gravity.Board;
import anandgames.gravity.Gravity;
import anandgames.gravity.animations.ExplosionAnimation;
import anandgames.gravity.entities.Asteroid;
import anandgames.gravity.entities.Bullet;
import anandgames.gravity.entities.Enemy;
import anandgames.gravity.entities.Pickup;
import anandgames.gravity.entities.Planet;
import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.entities.Tank;
import anandgames.gravity.weapons.FlameThrower;
import anandgames.gravity.weapons.Rifle;
import anandgames.gravity.weapons.Shotgun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
	private SpriteBatch spriteBatch;
	private Texture spriteSheet, shipSheet;
	private OrthographicCamera cam;
	private TextureRegion[][] sprites;
	private TextureRegion[] shipFrames;
	private Board board;
	private Animation shipAnimation;
	private TextureRegion currentShipFrame;
	private BitmapFont font, messageFont, smallFont;
	private PlayerShip ship;
	private boolean firstHeld, secondHeld;
	private int frameCount, mx, my, mCounter;
	private float stateTime;
	private Sound fire, explosion, background;
	private ArrayList<ExplosionAnimation> explosionAnimations;
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private long backgroundID;
	private String message;
	private AssetManager manager;
	private LinkedList<String> messageQueue;

	public GameScreen(AssetManager manager) {
		super();
		firstHeld = false;
		secondHeld = false;
		this.manager = manager;
		// Initialize the ship's flame animation
		initAnimation();

		// Initialize fonts
		font = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		font.setScale(.8f);

		smallFont = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		smallFont.setScale(.5f);

		messageFont = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		Color c = messageFont.getColor();
		messageFont.setColor(c.r, c.g, c.b, 1);

		// Set up the sprite sheet
		Pixmap pix = manager.get("GravityData/Images/Sprites.png");
		spriteSheet = new Texture(pix);
		spriteBatch = new SpriteBatch();
		sprites = new TextureRegion(spriteSheet).split(64, 64);

		// Initialize game components
		board = new Board(this);
		ship = board.getShip();

		// Frame count used for update delay
		frameCount = 0;

		// Set up Animation and Sound for Enemy explosions
		explosionAnimations = new ArrayList<ExplosionAnimation>();
		explosion = manager.get("GravityData/Sounds/Explosion.wav");

		// Set up the tiled map
		tiledMap = new TmxMapLoader().load("GravityData/Other/StarMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);

		// Set up the camera
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background = manager.get("GravityData/Sounds/loop1.mp3");
		// backgroundID = background.loop();
		messageQueue = new LinkedList<String>();

	}

	// Initialize the ship's flame animation
	public void initAnimation() {
		shipSheet = manager.get("GravityData/Images/ShipFrames.png");
		TextureRegion[][] tmp = new TextureRegion(shipSheet).split(16, 16);
		shipFrames = new TextureRegion[tmp.length * tmp[0].length];
		int count = 0;
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[0].length; j++) {
				shipFrames[count++] = tmp[i][j];
			}
		}
		shipAnimation = new Animation(.1f, shipFrames);
		stateTime = 0;
	}

	@Override
	public void render(float delta) {
		// Only update the game every other render (never do this)
		if (frameCount != 1)
			frameCount++;
		else if (frameCount == 1) {
			board.update();
			frameCount = 0;
		}
		if (board.isInGame()) {
			// Clear the screen
			GL20 gl = Gdx.graphics.getGL20();
			gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gl.glActiveTexture(GL20.GL_TEXTURE0);
			gl.glEnable(GL20.GL_TEXTURE_2D);

			// Reposition the camera
			cam.position.set(ship.getPosition().x, ship.getPosition().y, 0);
			cam.update();
			mapRenderer.setView(cam);
			mapRenderer.render();
			spriteBatch.setProjectionMatrix(cam.combined);

			// Draw all the components of the game
			spriteBatch.begin();

			drawPlanets();
			drawBullets();
			drawEnemies();
			drawShip();
			drawInfo();
			drawExplosions();
			drawWeaponPickups();
			drawAsteroids();
			drawInfo();

			if (message != null) {
				drawMessage(delta);
				System.out.println(message);
			}

			spriteBatch.end();
		} else {
			// The game is over, display the appropriate screen
			background.stop(backgroundID);
			((Game) (Gdx.app.getApplicationListener()))
					.setScreen(new GameOverScreen(ship.getScore()));

		}
	}

	public void drawPlanets() {
		for (Planet p : board.getPlanets()) {
			spriteBatch
					.draw(sprites[(int) p.getSpriteKey().x][(int) p
							.getSpriteKey().y], (float) p.getPosition().x,
							(float) p.getPosition().y, p.getRadius(), p
									.getRadius(), 2 * p.getRadius(), 2 * p
									.getRadius(), 1f, 1f, (float) p
									.getOrientation());
		}
	}

	public void drawAsteroids() {
		for (Asteroid x : board.getAsteroids()) {
			spriteBatch
					.draw(sprites[(int) x.getSpriteKey().x][(int) x
							.getSpriteKey().y], (float) x.getPosition().x,
							(float) x.getPosition().y, x.getRadius(), x
									.getRadius(), 2 * x.getRadius(), 2 * x
									.getRadius(), 1, 1, (float) Math
									.toDegrees(x.getOrientation()));
		}
	}

	public void drawMessage(float delta) {
		mCounter++;
		my = (int) board.getShip().getPosition().y - 300;
		mx = (int) board.getShip().getPosition().x
				- (10 * (message.length() / 2));
		messageFont.draw(spriteBatch, message, mx, my);
		System.out.println(mCounter);
		// Current message is done showing, poll next message if there is one
		if (mCounter >= (int) (2 / delta)) {
			if (!messageQueue.isEmpty())
				message = messageQueue.poll();
			else
				message = null;
			mCounter = 0;
		}
	}

	// Add a new message to the message queue
	public void showMessage(String message) {
		if (this.message != null)
			messageQueue.add(message);
		else
			this.message = message;
	}

	public void drawWeaponPickups() {
		for (Pickup x : board.getPickups()) {
			spriteBatch
					.draw(sprites[(int) x.getSpriteKey().x][(int) x
							.getSpriteKey().y], (float) x.getPosition().x,
							(float) x.getPosition().y, x.getRadius(), x
									.getRadius(), 2 * x.getRadius(), 2 * x
									.getRadius(), 1f, 1f, (float) x
									.getOrientation());
		}
	}

	// Draw all explosion animations
	public void drawExplosions() {
		for (int i = 0; i < explosionAnimations.size(); i++) {
			explosionAnimations.get(i).drawCurrentFrame(spriteBatch, stateTime);
			if (explosionAnimations.get(i).getAnimation()
					.isAnimationFinished(stateTime))
				explosionAnimations.remove(i);
		}
	}

	// Write the player's score, special ammo, and amount of money
	public void drawInfo() {
		font.draw(spriteBatch, "Score: " + board.getShip().getScore(),
				ship.getPosition().x + 400, ship.getPosition().y + 360);
		font.draw(spriteBatch, "Special Ammo:"
				+ board.getShip().getCurrentAmmo(), ship.getPosition().x + 400,
				ship.getPosition().y + 335);
		font.draw(spriteBatch, "Money:" + board.getShip().getMoney(),
				ship.getPosition().x + 400, ship.getPosition().y + 310);
		font.draw(spriteBatch, "Bombs:" + ship.getBombs(),
				ship.getPosition().x + 400, ship.getPosition().y + 285);
		font.draw(spriteBatch, "Wave:" + board.getCurrentWave(),
				ship.getPosition().x + 400, ship.getPosition().y + 260);
	}

	public void drawShip() {
		stateTime += Gdx.graphics.getDeltaTime();
		currentShipFrame = shipAnimation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentShipFrame, (float) board.getShip()
				.getPosition().x, (float) board.getShip().getPosition().y, ship
				.getRadius(), ship.getRadius(), 2 * ship.getRadius(), 2 * ship
				.getRadius(), 1, 1, (float) Math.toDegrees(ship
				.getOrientation()));

		// TODO: draw a shield sprite at 1,12 in Sprites.png
		if (board.getShip().isShielded())
			spriteBatch.draw(sprites[1][12], (float) board.getShip()
					.getPosition().x, (float) board.getShip().getPosition().y,
					12f, 12f, 24f, 24f, 2, 2, (float) Math.toDegrees(ship
							.getOrientation()));
	}

	public void drawEnemies() {
		for (int i = 0; i < board.getEnemies().size(); i++) {
			Enemy e = board.getEnemies().get(i);
			if (e instanceof Tank) {
				Tank t = (Tank) e;
				smallFont.draw(spriteBatch, "HP: " + t.getHp(),
						t.getPosition().x, t.getPosition().y - 20);
				for (int j = 0; j < t.getBullets().size(); j++) {
					spriteBatch.draw(sprites[0][2],
							(float) t.getBullets().get(j).getPosition().x,
							(float) t.getBullets().get(j).getPosition().y, 7,
							7, 14, 14, 1f, 1f, 0f);
				}
			}
			spriteBatch
					.draw(sprites[(int) e.getSpriteKey().x][(int) e
							.getSpriteKey().y], (float) e.getPosition().x,
							(float) e.getPosition().y, e.getRadius(), e
									.getRadius(), 2 * e.getRadius(), 2 * e
									.getRadius(), 1, 1, (float) Math
									.toDegrees(e.getOrientation()));

		}
	}

	// Draw the correct bullet based on which weapon is being wielded by the
	// player
	public void drawBullets() {
		for (int i = 0; i < ship.getBullets().size(); i++) {
			Bullet b = ship.getBullets().get(i);
			int sz = b.getRadius();
			Vector2 p = b.getSpriteKey();
			spriteBatch.draw(sprites[(int) p.x][(int) p.y],
					(float) b.getPosition().x, (float) b.getPosition().y, sz,
					sz, 2 * sz, 2 * sz, 1f, 1f, 0f);
		}
	}

	// Add a new explosion at x, y
	public void addExplosion(int x, int y) {
		explosionAnimations.add(new ExplosionAnimation(x, y));
	}

	public SpriteBatch getBatch() {
		return spriteBatch;
	}

	public Sound getSound(String sound) {
		if (sound == "Fire")
			return fire;
		else
			return explosion;

	}

	public Board getBoard() {
		return board;
	}

	// Handle input on mobile versions of the game (in progress/doesn't work)
	private class MobileInputProcessor implements InputProcessor {

		private GameScreen screen;
		private Vector2 leftStick = new Vector2(Gdx.graphics.getWidth() / 4,
				Gdx.graphics.getHeight() / 2);

		public MobileInputProcessor(GameScreen res) {
			super();
			screen = res;
		}

		@Override
		public boolean keyDown(int keycode) {
			ship.keyPressed(keycode);
			if (keycode == Keys.SPACE)
				return true;
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			ship.keyReleased(keycode);
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		public double getAngleToStick(int x, int y) {
			float dx = x - leftStick.x, dy = y - leftStick.y;
			return Math.atan2(dy, dx);
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {

			// Within the "ship control" region of the screen
			if (x < Gdx.graphics.getWidth() / 2) {
				ship.setOrientation(getAngleToStick(x, y));
				ship.getAcceleration().x = (float) (ship.getMaxAcceleration() * Math
						.cos(ship.getOrientation()));
				ship.getAcceleration().y = (float) (ship.getMaxAcceleration() * Math
						.sin(ship.getOrientation()));
			} else
				ship.fire();

			return false;
			// if (button == Input.Buttons.LEFT) {
			// if (!firstHeld && !secondHeld) {
			// ship.getAcceleration().x = (float) (ship
			// .getMaxAcceleration() * Math.cos(ship
			// .getOrientation()));
			// ship.getAcceleration().y = (float) (ship
			// .getMaxAcceleration() * Math.sin(ship
			// .getOrientation()));
			// ship.reOrient(new Vector2(x, y));
			// firstHeld = true;
			// } else if (firstHeld && !secondHeld) {
			// ship.mousePressed(x, y);
			// secondHeld = true;
			// }
			// }
			// return false;

		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			board.getShip().mouseReleased();
			if (firstHeld && secondHeld)
				secondHeld = false;
			else if (firstHeld && !secondHeld) {
				ship.setAcceleration(new Vector2(0, 0));
				firstHeld = false;
			}
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			if (!secondHeld) {
				ship.getAcceleration().x = (float) (ship.getMaxAcceleration() * Math
						.cos(ship.getOrientation()));
				ship.getAcceleration().y = (float) (ship.getMaxAcceleration() * Math
						.sin(ship.getOrientation()));
				ship.reOrient(new Vector2(screenX, screenY));
			} else {
				board.setFireLoc(new Vector2(screenX, screenY));
			}
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	// Handle input on the desktop version of the game
	private class DesktopInputProcessor implements InputProcessor {

		private GameScreen screen;

		public DesktopInputProcessor(GameScreen res) {
			super();
			screen = res;
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			if (button == Input.Buttons.LEFT) {
				ship.mousePressed();
			} else if (button == Input.Buttons.MIDDLE) {
				ship.dropBomb();
			}
			return false;
		}

		@Override
		public boolean keyDown(int keycode) {
			ship.keyPressed(keycode);
			// Space sends player to the store
			if (keycode == Keys.SPACE)
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new StoreScreen(screen));
			// Escape pauses the game
			if (keycode == Keys.ESCAPE)
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new PauseScreen(screen));

			// Dev cheats!

			// $$$$$$
			if (keycode == Keys.UP)
				ship.setMoney(ship.getMoney() + 100000);
			// Apocalypse!
			if (keycode == Keys.E)
				board.getEnemies().clear();
			// BURN
			if (keycode == Keys.F) {
				// ship.setDefaultWeapon(new FlameThrower());
				ship.setWeapon(new FlameThrower());
				ship.setCurrentAmmo((int) Float.MAX_VALUE);
			}
			// Shotgun...
			if (keycode == Keys.G) {
				// ship.setDefaultWeapon(new Shotgun());
				ship.setWeapon(new Shotgun());
				ship.setCurrentAmmo((int) Float.MAX_VALUE);
			}
			// Rifle...
			if (keycode == Keys.R) {
				// ship.setDefaultWeapon(new Rifle());
				ship.setWeapon(new Rifle());
				ship.setCurrentAmmo((int) Float.MAX_VALUE);
			}
			// INVINCIBLE
			if (keycode == Keys.C) {
				board.collisions = !board.collisions;
				showMessage("Collisions: " + board.collisions);
			}
			// Faster!
			if (keycode == Keys.RIGHT) {
				ship.setMaxSpeed(ship.getMaxSpeed() + 1);
				ship.setMaxAcceleration(ship.getMaxAcceleration() + 1);
			}
			// Slow down!
			if (keycode == Keys.LEFT) {
				ship.setMaxSpeed(ship.getMaxSpeed() - 1);
				ship.setMaxAcceleration(ship.getMaxAcceleration() + 1);
			}
			// BOMB!
			if (keycode == Keys.B) {
				ship.addBomb();
			}
			if (keycode == Keys.Z) {
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen(manager));
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			ship.keyReleased(keycode);
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			ship.mouseReleased();
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			ship.reOrient(new Vector2(screenX, screenY));
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			ship.reOrient(new Vector2(screenX, screenY));
			return true;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	// Re-initialize background music
	public void show() {
		// Set input processor based on which platform is being used
		if (((Gravity) Gdx.app.getApplicationListener()).getPlatform() == Gravity.DESKTOP)
			Gdx.input.setInputProcessor(new DesktopInputProcessor(this));
		else if (((Gravity) Gdx.app.getApplicationListener()).getPlatform() == Gravity.ANDROID)
			Gdx.input.setInputProcessor(new MobileInputProcessor(this));
		backgroundID = background.loop();
	}

	@Override
	// Stop playing background music
	public void hide() {
		background.stop(backgroundID);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
