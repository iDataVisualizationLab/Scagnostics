package main;

import java.awt.Color;
import processing.core.PApplet;

class ButtonMain {
	float x, y, w, h;
	PApplet parent;
	private Color bg = Color.WHITE;
	public boolean s = false;
	public boolean b = true;
	public int count = 0;
	public ButtonIntegrator pos = new ButtonIntegrator(0);
	public String text = "";
	
	ButtonMain(float x, float y, float w, float h, PApplet p) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		parent = p;
		pos.target(x);
		text = ">>";
	}

	public void setParent(PApplet p) {
		parent = p;
	}

	public boolean mouseOver() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (mX > pos.value-w && mX < pos.value + w+2 && mY > y && mY < y + h) {
			b = true;
			return b;
		}
		b = false;
		return b;
	}

	public boolean mousePressed() {
		if (b) {
			count = 0;
			s = !s;
			if (s)
				pos.target(1232-w);
			else
				pos.target(0);
			return true;
		}
		return false;
	}

	public void draw() {
		parent.stroke(80);
		mouseOver();
		if (s){
			bg = Color.MAGENTA;
			x = 1280-w;
		}	
		else {
			x =0;
			bg = Color.GRAY;
		}
		if (b)
			bg = Color.PINK;
		
		parent.noStroke();
		parent.fill(bg.getRGB());
		float xx = pos.value;
		if (s && xx >= 1200-w) text ="<<";
		else if (!s && xx >= w) text ="<<";
		else  text =">>";
		parent.rect(xx, y, w, h);
		parent.fill(Color.BLACK.getRGB());
		parent.text(text, xx + 4, y + 12);
		x=xx;
		
		count++;
		if (count == 10000)
			count = 200;
	}

}