package anandgames.gravity.screens;

import java.util.ArrayList;

import anandgames.gravity.Board;
import anandgames.gravity.Gravity;
import anandgames.gravity.animations.ExplosionAnimation;
import anandgames.gravity.entities.Asteroid;
import anandgames.gravity.entities.Enemy;
import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.entities.Weapon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	private BitmapFont font, messageFont;
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

		messageFont = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		Color c = messageFont.getColor();
		// System.out.println(c.a);
		messageFont.setColor(c.r, c.g, c.b, 1);

		// Initialize Tween Manager
		// tManager = new TweenManager();
		// Tween.registerAccessor(BitmapFont.class, new MessageTweenAccessor());

		// Set up the sprite sheet
		Pixmap pix = manager.get("GravityData/Images/Sprites.png");
		spriteSheet = new Texture(pix);
		spriteBatch = new SpriteBatch();
		sprites = new TextureRegion(spriteSheet).split(16, 16);

		// Initialize game components
		board = new Board(this);
		ship = board.getShip();

		// Frame count used for update delay
		frameCount = 0;

		// Set up Animation and Sound for Enemy explosions
		explosionAnimations = new ArrayList<ExplosionAnimation>();
		explosion = manager.get("GravityData/Sounds/Explosion.wav");

		tiledMap = new TmxMapLoader().load("GravityData/Other/StarMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background = manager.get("GravityData/Sounds/loop1.mp3");
		// backgroundID = background.loop();
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
		if (frameCount != 1)
			frameCount++;
		else if (frameCount == 1) {
			board.update();
			frameCount = 0;
		}
		if (board.isInGame()) {
			GL20 gl = Gdx.graphics.getGL20();
			gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gl.glActiveTexture(GL20.GL_TEXTURE0);
			gl.glEnable(GL20.GL_TEXTURE_2D);
			Gdx.input.setInputProcessor(new MyInputProcessor());
			PlayerShip ship = board.getShip();
			cam.position.set(ship.getPosition().x, ship.getPosition().y, 0);
			cam.update();
			mapRenderer.setView(cam);
			mapRenderer.render();
			spriteBatch.setProjectionMatrix(cam.combined);
			spriteBatch.begin();

			drawBullets();
			drawEnemies();
			drawShip();
			drawInfo();
			drawExplosions();
			drawWeapons();
			drawAsteroids();
			drawInfo();
			if (message != null)
				drawMessage(delta);

			spriteBatch.end();
		} else {
			background.stop(backgroundID);
			((Game) (Gdx.app.getApplicationListener()))
					.setScreen(new GameOverScreen(ship.getScore()));

		}
	}

	public void drawAsteroids() {
		for (Asteroid x : board.getAsteroids()) {
			spriteBatch
					.draw(sprites[(int) x.getSpriteKey().x][(int) x
							.getSpriteKey().y], (float) x.getPosition().x,
							(float) x.getPosition().y, 30f, 30f, 60f, 60f, 2,
							2, (float) Math.toDegrees(x.getOrientation()));
		}
	}

	public void drawMessage(float delta) {
		mCounter++;
		my = (int) board.getShip().getPosition().y - 300;
		mx = (int) board.getShip().getPosition().x
				- (10 * (message.length() / 2));
		messageFont.draw(spriteBatch, message, mx, my);
		System.out.println(mCounter);
		if (mCounter >= (int) (3 / delta)) {
			message = null;
			mCounter = 0;
		}
	}

	public void showMessage(String message) {
		this.message = message;
		// Tween.to(messageFont, 0, 3f).target(.99f).start(tManager);
		// message = null;
	}

	public void drawWeapons() {
		for (Weapon x : board.getWeaponList()) {
			spriteBatch
					.draw(sprites[(int) x.getSpriteKey().x][(int) x
							.getSpriteKey().y], (float) x.getX(), (float) x
							.getY(), 16f, 16f, 32f, 32f, 1f, 1f, (float) x
							.getOrientation());
		}
	}

	public void drawExplosions() {
		for (int i = 0; i < explosionAnimations.size(); i++) {
			explosionAnimations.get(i).drawCurrentFrame(spriteBatch, stateTime);
			if (explosionAnimations.get(i).getAnimation()
					.isAnimationFinished(stateTime))
				explosionAnimations.remove(i);
		}
	}

	public void drawInfo() {
		font.draw(spriteBatch, "Score: " + board.getShip().getScore(),
				ship.getPosition().x + 400, ship.getPosition().y + 360);
		font.draw(spriteBatch, "Special Ammo:"
				+ board.getShip().getCurrentAmmo(), ship.getPosition().x + 400,
				ship.getPosition().y + 310);
	}

	public void drawShip() {
		stateTime += Gdx.graphics.getDeltaTime();
		currentShipFrame = shipAnimation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentShipFrame, (float) board.getShip()
				.getPosition().x, (float) board.getShip().getPosition().y, 10f,
				10f, 20f, 20f, 2, 2, (float) Math.toDegrees(ship
						.getOrientation()));

		// TODO: draw a shield sprite at 1,12 in Sprites.png
		if (board.getShip().isShielded())
			spriteBatch.draw(sprites[1][12], (float) board.getShip()
					.getPosition().x, (float) board.getShip().getPosition().y,
					4f, 4f, 8f, 8f, 1, 1, (float) Math.toDegrees(ship
							.getOrientation()));
	}

	public void drawEnemies() {
		for (int i = 0; i < board.getEnemies().size(); i++) {
			Enemy e = board.getEnemies().get(i);
			spriteBatch
					.draw(sprites[(int) e.getSpriteKey().x][(int) e
							.getSpriteKey().y], (float) e.getPosition().x,
							(float) e.getPosition().y, 10f, 10f, 20f, 20f, 2,
							2, (float) Math.toDegrees(e.getOrientation()));

		}
	}

	public void drawBullets() {
		int sz;
		if (ship.getWeapon() != null)
			sz = ship.getWeapon().getAmmoRadius();
		else
			sz = 7;
		for (int i = 0; i < ship.getBullets().size(); i++) {
			Vector2 p;
			if (ship.getWeapon() == null)
				p = new Vector2(0, 2);
			else
				p = ship.getWeapon().getBulletKey();
			spriteBatch.draw(sprites[(int) p.x][(int) p.y], (float) ship
					.getBullets().get(i).getPosition().x, (float) ship
					.getBullets().get(i).getPosition().y, sz, sz, 2 * sz,
					2 * sz, 1f, 1f, 0f);
		}
	}

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

	private class MyInputProcessor implements InputProcessor {

		private final PlayerShip ship = board.getShip();

		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			if (button == Input.Buttons.LEFT) {
				if (((Gravity) ((Game) Gdx.app.getApplicationListener()))
						.getPlatform() == Gravity.DESKTOP) {
					ship.mousePressed();
					return false;
				}
				if (((Gravity) ((Game) Gdx.app.getApplicationListener()))
						.getPlatform() == Gravity.ANDROID) {
					if (!firstHeld && !secondHeld) {
						ship.getAcceleration().x = (float) (ship
								.getMaxAcceleration() * Math.cos(ship
								.getOrientation()));
						ship.getAcceleration().y = (float) (ship
								.getMaxAcceleration() * Math.sin(ship
								.getOrientation()));
						ship.reOrient(new Vector2(x, y));
						firstHeld = true;
					} else if (firstHeld && !secondHeld) {
						ship.mousePressed(x, y);
						secondHeld = true;
					}

				}
			}
			return false;
		}

		@Override
		public boolean keyDown(int keycode) {
			ship.keyPressed(keycode);
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
			board.getShip().mouseReleased();
			if (firstHeld && secondHeld)
				secondHeld = false;
			else if (firstHeld && !secondHeld) {
				ship.setAcceleration(new Vector2(0, 0));
				firstHeld = false;
			}
			// touchHeld = false;
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			if (((Gravity) ((Game) Gdx.app.getApplicationListener()))
					.getPlatform() == Gravity.ANDROID) {
				if (!secondHeld) {
					ship.getAcceleration().x = (float) (ship
							.getMaxAcceleration() * Math.cos(ship
							.getOrientation()));
					ship.getAcceleration().y = (float) (ship
							.getMaxAcceleration() * Math.sin(ship
							.getOrientation()));
					ship.reOrient(new Vector2(screenX, screenY));
				} else {
					board.setFireLoc(new Vector2(screenX, screenY));
				}
			}
			else {
				ship.reOrient(new Vector2(screenX, screenY));
			}
			// touchHeld = true;
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
	public void show() {
		backgroundID = background.loop();
	}

	@Override
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
