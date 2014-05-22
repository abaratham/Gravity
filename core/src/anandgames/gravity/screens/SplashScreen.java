package anandgames.gravity.screens;

import anandgames.gravity.tweens.SplashAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	private SpriteBatch batch;
	private Sprite splash;
	private TweenManager tweenManager;

	@Override
	public void render(float delta) {
		//Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Draw the splash image
		batch.begin();
		splash.draw(batch);
		batch.end();

		//Update the tween manager
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SplashAccessor());

		splash = new Sprite(new Texture("GravityData/Images/splash.png"));

		//Fade in and out
		Tween.set(splash, 0).target(0).start(tweenManager);
		Tween.to(splash, 0, 1.5f).target(1).repeatYoyo(1, .5f)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						((Game) Gdx.app.getApplicationListener())
								.setScreen(new MainMenu());
					}
				}).start(tweenManager);

		//Update quickly to avoid flashing the splash image before animation begins
		tweenManager.update(Float.MIN_VALUE); 
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		splash.getTexture().dispose();
	}

}