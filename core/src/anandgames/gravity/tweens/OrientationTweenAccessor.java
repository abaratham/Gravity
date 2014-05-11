package anandgames.gravity.tweens;

import anandgames.gravity.entities.Entity;
import aurelienribon.tweenengine.TweenAccessor;

public class OrientationTweenAccessor implements TweenAccessor<Entity> {

	@Override
	public int getValues(Entity target, int type, float[] returnValues) {
		returnValues[0] = (float) target.getOrientation();
		return 1;
	}

	@Override
	public void setValues(Entity target, int type, float[] newValues) {
		target.setOrientation(newValues[0]);
	}

}
