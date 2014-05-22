package anandgames.gravity.screens;

import anandgames.gravity.Gravity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu implements Screen {

	private Stage stage;
	private Table table;
	private TextButton play, exit;
	private Skin skin;
	private BitmapFont white, black;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private Texture tex;

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Write the version number
		batch.begin();
		batch.draw(tex, 0, 0);
		white.draw(batch, Gravity.VERSION_NUMBER, 0, 20);
		batch.end();

		// Update and draw the stage
		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	// Initialize all elements of the stage, fonts, and button styles.
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();
		atlas = new TextureAtlas(
				Gdx.files.internal("GravityData/ui/Button.pack"));
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(atlas);

		table = new Table(skin);
		table.setFillParent(true);

		white = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/White.fnt"), false);
		LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);

		black = new BitmapFont(
				Gdx.files.internal("GravityData/Fonts/Black.fnt"), false);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button.up");
		textButtonStyle.down = skin.getDrawable("button.down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = black;

		play = new TextButton("Play", textButtonStyle);
		exit = new TextButton("Exit", textButtonStyle);

		play.addListener(new ClickListener() {

			@Override
			// Clicking this button begins the loading process
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
						.setScreen(new LoadingScreen());
			}
		});

		play.pad(15, 100, 15, 100);
		exit.pad(15, 100, 15, 100);
		exit.addListener(new ClickListener() {

			@Override
			// Clicking this button exits the game
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		table.add(play).spaceBottom(15).row();
		table.add(exit).spaceBottom(15).row();
		table.debug();
		stage.addActor(table);
		white.setScale(.5f);
		Pixmap pix = new Pixmap(
				Gdx.files.internal("GravityData/Images/splash.png"));
		tex = new Texture(pix);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
