package anandgames.gravity;

import anandgames.gravity.screens.SplashScreen;

import com.badlogic.gdx.Game;

public class Gravity extends Game {
	public static final int DESKTOP = 0;
	public static final int ANDROID = 1;
	public static final int HTML = 2;
	public static final String VERSION_NUMBER = "v1.0";
	
	private int platform;

	public Gravity (int platform) {
		super();
		setPlatform(platform);
	}
	
	public void create() {
		setScreen(new SplashScreen());
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void render() {
		super.render();
	}

	public void pause() {
		super.pause();
	}

	public void resume() {
		super.resume();
	}

	public void dispose() {
		super.dispose();
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

}
