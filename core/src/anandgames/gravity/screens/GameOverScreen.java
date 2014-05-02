package anandgames.gravity.screens;

import anandgames.gravity.Gravity;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen {

	private Stage stage;
	private Table table;
	private TextButton exit, playAgainButton;
	private Label heading;
	private Skin skin;
	private BitmapFont white, black;
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private int score;
	
	public GameOverScreen(int s) {
		score = s;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
		
		batch.begin();
		white.draw(batch, Gravity.VERSION_NUMBER, 0, 20);
		batch.end();
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
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

		exit = new TextButton("Exit", textButtonStyle);

		exit.pad(15, 100, 15, 100);
		exit.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		LabelStyle labelStyle = new LabelStyle(white, Color.WHITE);
		heading = new Label("You Died!\nGAME OVER!\nScore: " + score , labelStyle);
		heading.setAlignment(Align.center);
		heading.setFontScale(2f);
		
		playAgainButton = new TextButton("Play Again", textButtonStyle);

		playAgainButton.pad(15, 100, 15, 100);
		playAgainButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
				.setScreen(new LoadingScreen());
			}
		});
		table.add(heading).spaceBottom(150).row();
		table.add(playAgainButton).spaceBottom(15).row();
		table.add(exit).spaceBottom(15).row();
		table.debug();
		stage.addActor(table);
		white.setScale(.5f);
	}

}
