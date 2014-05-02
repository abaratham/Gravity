package anandgames.gravity;


public class Planet {

	private int x, y, radius, range;
	private float maxEffect;
	private String name;
	
	public Planet(String name, int x, int y, int radius, float effect) {
		setX(x);
		setY(y);
		setRadius(radius);
		setRange(radius * 3);
		setMaxEffect(effect);
		setName(name);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
