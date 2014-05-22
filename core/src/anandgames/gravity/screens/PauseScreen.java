package anandgames.gravity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseScreen implements Screen {

	private Stage stage;
	private Table table;
	private TextButton exit, resumeButton;
	private Label label;
	private Skin skin;
	private BitmapFont white, black;
	private TextureAtlas atlas;
	private Screen resumeScreen;
	
	public PauseScreen(Screen resume) {
		super();
		setResumeScreen(resume);
	}

	@Override
	public void render(float delta) {
		//Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Update and draw the stage
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	//Initialize all elements of the stage, fonts, and button styles. \
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

		exit = new TextButton("Exit", textButtonStyle);

		exit.pad(15, 100, 15, 100);
		exit.addListener(new ClickListener() {

			@Override
			//Clicking this button exits the game
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		resumeButton = new TextButton("Resume", textButtonStyle);

		resumeButton.pad(15, 100, 15, 100);
		resumeButton.addListener(new ClickListener() {

			@Override
			//Clicking this button resumes the game
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener())
				.setScreen(resumeScreen);
			}
		});
		table.add(resumeButton).spaceBottom(15).row();
		table.add(exit).spaceBottom(15).row();
		table.debug();
		stage.addActor(table);
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
	}

	public Screen getResumeScreen() {
		return resumeScreen;
	}

	public void setResumeScreen(Screen resumeScreen) {
		this.resumeScreen = resumeScreen;
	}

}
