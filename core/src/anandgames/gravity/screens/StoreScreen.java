package anandgames.gravity.screens;

import java.util.LinkedList;

import anandgames.gravity.entities.PlayerShip;
import anandgames.gravity.weapons.FlameThrower;
import anandgames.gravity.weapons.Rifle;
import anandgames.gravity.weapons.Shotgun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StoreScreen implements Screen {

	private Stage stage;
	private Table table;
	private TextButton exit, resumeButton;
	private Label label;
	private Skin skin;
	private BitmapFont white, black;
	private TextureAtlas atlas;
	private Screen resumeScreen;
	private String message;
	private SpriteBatch batch;
	private LinkedList<String> messageQueue;
	private int mCounter;
	private PlayerShip ship;
	private Label heading;

	public StoreScreen(Screen resume) {
		super();
		setResumeScreen(resume);
		batch = new SpriteBatch();
		messageQueue = new LinkedList<String>();
		ship = ((GameScreen) resumeScreen).getBoard().getShip();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		heading.setText("STORE\nMoney: $" + ship.getMoney());

		stage.act(delta);
		stage.draw();

		batch.begin();
		if (message != null) {
			drawMessage(delta);
			System.out.println(message);
		}
		batch.end();
	}

	public void drawMessage(float delta) {
		mCounter++;

		white.draw(batch, message, (Gdx.graphics.getWidth() / 2)
				- (10 * message.length()), Gdx.graphics.getHeight() - 100);
		// Current message is done showing
		if (mCounter >= (int) (3 / delta)) {
			if (!messageQueue.isEmpty())
				message = messageQueue.poll();
			else
				message = null;
			mCounter = 0;
		}
	}

	public void showMessage(String message) {
		if (this.message != null)
			messageQueue.add(message);
		else
			this.message = message;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		stage = new Stage();
		atlas = new TextureAtlas("GravityData/ui/Button.pack");
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(atlas);
		table = new Table(skin);
		table.setFillParent(true);
		white = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		black = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/Black.fnt"), false);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = black;
		
		LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);
		heading = new Label("STORE\nMoney: $" + ship.getMoney(), labelStyle);
		heading.setAlignment(Align.center);
		heading.setFontScale(2f);

		

		resumeButton = new TextButton("Resume", textButtonStyle);

		resumeButton.pad(15, 100, 15, 100);
		resumeButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
						.setScreen(resumeScreen);
			}
		});

		TextButton flameThrower = new TextButton("FlameThrower: $5000",
				textButtonStyle);
		flameThrower.pad(15, 15, 15, 15);
		flameThrower.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ship.getMoney() >= 5000) {
					ship.setDefaultWeapon(new FlameThrower());
					ship.setWeapon(new FlameThrower());
					ship.setMoney(ship.getMoney() - 5000);
					showMessage("Flamethrower purchased!");
				} else
					showMessage("Not enough money!");
			}
		});

		TextButton rifle = new TextButton("Rifle: $3000", textButtonStyle);
		rifle.pad(15, 15, 15, 15);
		rifle.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ship.getMoney() >= 3000) {
					ship.setDefaultWeapon(new Rifle());
					ship.setWeapon(new Rifle());
					ship.setMoney(ship.getMoney() - 3000);
					showMessage("Rifle purchased!");
				} else
					showMessage("Not enough Money!");
			}
		});

		TextButton shotgun = new TextButton("Shotgun: $1000", textButtonStyle);
		shotgun.pad(15, 15, 15, 15);
		shotgun.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ship.getMoney() >= 1000) {
					ship.setDefaultWeapon(new Shotgun());
					ship.setWeapon(new Shotgun());
					ship.setMoney(ship.getMoney() - 1000);
					showMessage("Shotgun purchased!");
				} else
					showMessage("Not enough money!");
			}
		});
		
		TextButton speedUp = new TextButton("Speed Up: $10000", textButtonStyle);
		speedUp.pad(15, 15, 15, 15);
		speedUp.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ship.getMoney() >= 10000) {
					ship.setMaxSpeed(ship.getMaxSpeed() + 1);
					ship.setMoney(ship.getMoney() - 10000);
					showMessage("Speed increased!");
				} else
					showMessage("Not enough money!");
			}
		});

		table.add(heading).row();
		table.add(flameThrower).spaceRight(15).spaceBottom(15);
		table.add(rifle).spaceRight(15).row().spaceBottom(15);
		table.add(shotgun).spaceRight(15);
		table.add(speedUp).row();
		table.add(resumeButton).spaceBottom(15).spaceTop(15);
		table.center();
		table.debug();
		stage.addActor(table);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Screen getResumeScreen() {
		return resumeScreen;
	}

	public void setResumeScreen(Screen resumeScreen) {
		this.resumeScreen = resumeScreen;
	}

}
