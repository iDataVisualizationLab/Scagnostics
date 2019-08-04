package GraphLayout;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PImage;

//Copyright 2005 Sean McCullough
//banksean at yahoo

public class Node {
	Vector3D f = new Vector3D(0, 0, 0);
	float mass = 1;
	public String name = "";
	public int var = -1;
	public PApplet parent;
	
	public Vector3D position;
	float h = 10;
	float w = 10;
	Graph g;
	public boolean isConnected = false;
	
	public Node(Vector3D vec, PApplet p, int v, String s) {
		position = vec;
		parent = p;
		var = v;
		name = s;
		h = 20;
		w = 20;
	}

	public void setGraph(Graph h) {
		g = h;
	}

	public boolean containsPoint(float x, float y) {
		float dx = position.getX() - x;
		float dy = position.getY() - y;

		return (PApplet.abs(dx) < w / 2 && PApplet.abs(dy) < h / 2);
	}

	public Node(Vector3D v) {
		position = v;
	}

	public Vector3D getPosition() {
		return position;
	}

	public void setPosition(Vector3D v) {
		position = v;
	}

	public float getX() {
		return position.getX();
	}

	public float getY() {
		return position.getY();
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float m) {
		mass = m;
		h = 10+m * 10;
		w = 10+m * 10;
	}

	public void setForce(Vector3D v) {
		f = v;
	}

	public Vector3D getForce() {
		return f;
	}

	public void applyForce(Vector3D v) {
		f = f.add(v.divide(4));
	}

	public void draw() {
		// Draw background
		
		if (g.getHoverNode()==this){
			parent.fill(255, 255, 255);
			parent.ellipse(getX(), getY(), h, w);
		}
		else if (g.getHoverNode() == null || isConnected) {
			parent.noStroke();
			parent.fill(100, 100, 100);
			parent.ellipse(getX(), getY(), h, w);
		}
		else{
			parent.noStroke();
			parent.fill(100, 100, 100, 200);
			parent.ellipse(getX(), getY(), h, w);
		}
		
	
		// Draw song names
		parent.textAlign(PApplet.CENTER);
		parent.textSize(12);
		parent.noStroke();
		parent.fill(0,0,0);

		
		if (g.getHoverNode() == this) {
			parent.text(name, getX()+1, getY() + h / 2 + 11);
			parent.fill(255, 255, 255);
		} 
		else if (g.getHoverNode() == null || isConnected) {
			parent.fill(150, 150, 150);		
		}
		else {
			parent.fill(50, 50, 50, 200);
		}
		parent.fill(0, 0, 0);		
		parent.text(name, getX(), getY() + h / 2 + 10);
	
	}
}
