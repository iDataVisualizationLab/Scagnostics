package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class PopupOption extends PApplet{
	public int b = -1;
	public PApplet parent;
	public int y = 0;
	public int w = 93;
	public int h = 25;
	public float x2 = 0;
	public int w2 = 150;
	public int h2 = 80;
	public int itemNum = 9;
	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	public Color cGray  = new Color(240,240,240);
	public PopupOption(PApplet parent_){
		parent = parent_;
	}
	public int count =0;
	
	
	public void draw(CheckBoxImposed c0, CheckBoxImposed c1, CheckBoxImposed c2){
		checkBrushing();
		if (b>=0){
			parent.textFont(metaBold, 13);
			parent.fill(60,60,60,248);
			parent.strokeWeight(1f);
			parent.stroke(200,200,200,100);
			parent.rect(x2, y-2, w2+50, h2);
			c0.draw();
			c1.draw();
			c2.draw();
		}
		else{
			parent.textFont(metaBold,13);
			parent.fill(new Color(155,155,155,100).getRGB());
			parent.noStroke();
			parent.rect(x2, y, w, h);
			
			parent.fill(new Color(255,255,255,255).getRGB());
			parent.text("Menu",x2+27,y+16);
		}	
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-1){
			if (x2<mX && mX<x2+w && y<mY && mY<h){
				b =100;
				return;
			}	
		}
		else{
			if (x2<mX && mX<x2+w2 && y<mY && mY<h2){
				return;
			}	
			
		}
		b =-1;
	}
	
}