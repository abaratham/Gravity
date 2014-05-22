package anandgames.gravity.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class LoadingScreen implements Screen {

	private Stage stage;
	private Table table;
	private Label loading;
	private Skin skin;
	private BitmapFont white, black;
	private TextureAtlas atlas;
	private AssetManager manager;

	public LoadingScreen() {
		super();
		manager = new AssetManager();
		queueAssets();
	}
	
	//Queue all assets to be loaded
	public void queueAssets() {
		manager.load("GravityData/Images/Sprites.png", Pixmap.class);
		manager.load("GravityData/Sounds/Explosion.wav", Sound.class);
		manager.load("GravityData/Sounds/loop1.mp3", Sound.class);
		manager.load("GravityData/Images/ShipFrames.png", Texture.class);
	}

	@Override
	public void render(float delta) {
		//Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Continue loading assets, if done, start the game!
		if (manager.update())
			((Game) Gdx.app.getApplicationListener())
			.setScreen(new GameScreen(manager));
		
		//Update and draw the stage
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	//Initialize all elements of the stage, fonts, and button styles. 
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

		loading = new Label("LOADING...", labelStyle);
		loading.setAlignment(Align.center);
		loading.setFontScale(2f);		
		table.add(loading).center().row();
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

}
