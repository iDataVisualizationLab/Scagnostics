package main;

import java.awt.Color;
import processing.core.PApplet;

public class CheckBox{
	public boolean s = false;
	public int bMode = -1;
	public PApplet parent;
	public int x = 0;
	public int y = 0;
	public String text = "TUAN";
	
	public Color cGray  = new Color(240,240,240);
	public static Color[] cs  = {Color.MAGENTA,Color.PINK, Color.RED,
		Color.GREEN,Color.BLUE, Color.YELLOW, Color.CYAN, Color.ORANGE,
		Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.LIGHT_GRAY,Color.WHITE,Color.WHITE};
	
	public int id = 0;
	int count =0;
	  
	public CheckBox(PApplet parent_, int x_, int y_, String text_,boolean s_){
		parent = parent_;
		x = x_;
		y = y_;
		text = text_;
		s = s_;
	}
	
	public void set(int y_){
		y = y_;
	}
		
	public void draw(){
		checkBrushing();
		if (count>4 && parent.mousePressed)
			checkSelected();
		
		parent.stroke(Color.GRAY.getRGB());
		parent.fill(cGray.getRGB());
		parent.rect(x, y, 15, 15);
		if (s){
			parent.noStroke();
			parent.fill(cs[id].getRGB());
			parent.ellipse(x+8, y+8, 12, 12);
		}
		parent.fill(cGray.getRGB());
		if (s)
			parent.fill(cs[id].getRGB());
		if (bMode==0)
			parent.fill(Color.PINK.getRGB());
		parent.text(text,x+20,y+13);
			
		count++;
	    if (count==10000)
	    	count=200;
		
	}
	
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (x-10<mX && mX < x+110 && y<mY && mY<y+20){
			bMode =0;
			return;
		}	
		bMode =-1;
	}
	
	public boolean checkSelected() {
		if (bMode>=0){
			count=0;
			s = !s;
			return true;
		}	
		return false;
		
	}
	
}