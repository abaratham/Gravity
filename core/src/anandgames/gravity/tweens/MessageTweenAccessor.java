package anandgames.gravity.tweens;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class MessageTweenAccessor implements TweenAccessor<BitmapFont> {

	@Override
	public int getValues(BitmapFont target, int type, float[] returnValues) {
		returnValues[0] = (float) target.getColor().a;
		return 1;
	}

	@Override
	public void setValues(BitmapFont target, int type, float[] newValues) {
		Color c = target.getColor();
		target.setColor(c.r, c.g, c.b, newValues[0]);
	}

	
	
}
