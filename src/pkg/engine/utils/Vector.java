package pkg.engine.utils;

public class Vector {

	public float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector() {
		new Vector(0, 0);
	}
	
	public Vector(Vector v) {
		new Vector(v.x, v.y);
	}
	
	public float getLength() {
		return Math.round(Math.sqrt(x * x + y * y));
	}
	
	public Vector normalise() {
		float length = getLength();
		x /= length;
		y /= length;
		return this;
	}
	
	public Vector add(Vector v) {
		this.x += v.x;
		this.y += v.y;
		return this;
	}
	
	public Vector add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}	
	public Vector subtract(Vector v) {
		this.x -= v.x;
		this.y -= v.y;
		return this;
	}
	
	public Vector subtract(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}	
	public Vector multiply(Vector v) {
		this.x *= v.x;
		this.y *= v.y;
		return this;
	}
	
	public Vector multiply(float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}	
	public Vector divide(Vector v) {
		this.x /= v.x;
		this.y /= v.y;
		return this;
	}
	
	public Vector divide(float x, float y) {
		this.x /= x;
		this.y /= y;
		return this;
	}
	
	public int getIntX() {
		return Math.round(x);
	}
	
	public int getIntY() {
		return Math.round(y);
	}
	
	public Vector set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector set(Vector v) {
		return set(v.x, v.y);
	}
	
	public void clearX() {
		x = 0;
	}
	
	public void clearY() {
		y = 0;
	}
	
	public void clear() {
		clearX();
		clearY();
	}
	
	public Vector debug() {
		System.out.println(x + " x, " + y + " y");
		return this;
	}
}
