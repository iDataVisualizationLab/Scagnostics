package main;

import java.awt.Color;
import processing.core.PApplet;


class Button  {
  float x, y, w, h;
  PApplet parent; 
  private Color bg = Color.WHITE;
  public boolean s = false;
  public boolean b = true;
  public int count =0;
	
  Button(float x, float y, float w, float h, PApplet p) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    parent =p;
  } 
  public void setParent(PApplet p){
	  parent =p;
  }
  
  
  public boolean mouseOver() {
	    int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (mX > x && mX < x + w && mY > y && mY < y + h){
			b =true;
			return b;
		}	
		b =false;
		return b;
	}
  
  
  public boolean mousePressed() {
	  if (b){
			count=0;
			s = !s;
			return true;
		}	
		return false;
   }
  
  public void draw() {
	parent.stroke(80);
	mouseOver();
	if (count>6 && parent.mousePressed)
		mousePressed();
	
	if (s)
		bg = Color.MAGENTA;
	else if (b)
		bg = Color.PINK;
	else	
		bg =Color.WHITE;
	
    parent.fill(bg.getRGB());
    parent.rect(x,y,w,h); 
    
    count++;
    if (count==10000)
    	count=200;
  }
  
}