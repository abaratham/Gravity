package anandgames.gravity.tweens;

import anandgames.gravity.entities.pickups.WeaponPickup;
import aurelienribon.tweenengine.TweenAccessor;

public class ShotgunTweenAccessor implements TweenAccessor<WeaponPickup> {

	@Override
	public int getValues(WeaponPickup target, int type, float[] returnValues) {
		returnValues[0] = (float) target.getOrientation();
		return 1;
	}

	@Override
	public void setValues(WeaponPickup target, int type, float[] newValues) {
		target.setOrientation(newValues[0]);
	}

}
