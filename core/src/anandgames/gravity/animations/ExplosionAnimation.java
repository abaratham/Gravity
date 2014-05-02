package anandgames.gravity.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ExplosionAnimation {
	
	private float x, y;
	private Animation animation;
	private TextureRegion currentFrame;
	Texture frameSheet;
	TextureRegion[] frames;
	
	//Create a new Explosion at given coordinates
	public ExplosionAnimation( float x, float y) {
		frameSheet = new Texture(
				Gdx.files.internal("GravityData/Images/ExplosionFrames.png"));
		TextureRegion[][] tmp = new TextureRegion(frameSheet).split(16, 16);
		frames = new TextureRegion[tmp.length * tmp[0].length];
		int count = 0;
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[0].length; j++) {
				frames[count++] = tmp[i][j];
			}
		}
		animation = new Animation(.1f, frames);
		this.x = x;
		this.y = y;
	}
	
	//Draw keyFrame
	public void drawCurrentFrame(SpriteBatch batch, float stateTime) {
		currentFrame = animation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, x, y,
				4f, 4f, 8f, 8f, 1, 1, 0);			
	}
	
	public Animation getAnimation() {
		return animation;
	}

}
