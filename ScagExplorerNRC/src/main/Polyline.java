package main;

import java.awt.Color;
import processing.core.PApplet;
import static main.Main.*;

class Polyline  {
  float x, y, w, h;
  PApplet parent; 
  private Color bg = Color.WHITE;
  public boolean s = false;
  public boolean b = true;
  public int count =0;
  public int pair =0;
  public boolean isSatisfied = true;
  public static int brushing = -1;
  
  Polyline(PApplet p, int id_) {
	  pair = id_;
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
	 if (!isSatisfied)
		 return;
	  
	parent.stroke(80);
	mouseOver();
	if (count>6 && parent.mousePressed)
		mousePressed();
	
	if (pair==brushing){
		bg = Color.MAGENTA;
		parent.strokeWeight(2f);
	}	
	else{	
		bg =new Color(0,0,0,50);
		parent.strokeWeight(1f);
	}	
	
    parent.stroke(bg.getRGB());
    
	for (int sc = 0; sc < 9-1; sc++){
    	float x1 = xBar+scagVals[sc][pair]*wBar ;
    	float x2 = xBar+scagVals[sc+1][pair]*wBar ;
		float y1 = yBar+sc*hGraph ;
		float y2 = yBar+(sc+1)*hGraph ;
		parent.line(x1, y1, x2, y2);
    }
    	
    count++;
    if (count==10000)
    	count=200;
  }
  
}