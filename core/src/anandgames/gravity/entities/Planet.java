package anandgames.gravity.entities;

import anandgames.gravity.Board;

import com.badlogic.gdx.math.Vector2;

public class Planet extends Entity {

	private int range;
	private float maxEffect;

	public Planet(Vector2 startPos, Vector2 key, int radius, Board b) {
		super(startPos, 0,0,0,key,b);
		setRadius(radius);
		setRange(radius * 3);
		setMaxEffect(radius / 100);
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public float getMaxEffect() {
		return maxEffect;
	}

	public void setMaxEffect(float maxEffect) {
		this.maxEffect = maxEffect;
	}

}
