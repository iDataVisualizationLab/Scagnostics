package main;

import java.awt.Color;
import processing.core.PApplet;

public class CheckBoxImposed{
	public int s = -1;
	public int bMode = -1;
	public PApplet parent;
	public int x = 0;
	public int y = 0;
	public String text = "";
	
	public Color cGray  = new Color(240,240,240);
	public static Color[] cs  = {Color.MAGENTA,Color.MAGENTA, Color.MAGENTA,Color.MAGENTA,Color.MAGENTA,Color.MAGENTA};
	
	public int id = 0;
	int count =0;
	  
	public CheckBoxImposed(PApplet parent_, int x_, int y_, String text_,int s_, int id_){
		parent = parent_;
		x = x_;
		y = y_;
		text = text_;
		s = s_;
		id = id_;
	}
	
	public void set(int y_){
		y = y_;
	}
		
	public void draw(){
		if (s>0)
			s++;
		else
			s--;
		if (s>10000) s =10000;
		else if (s<-10000) s =-10000;
		
		checkBrushing();
		
		parent.stroke(Color.GRAY.getRGB());
		parent.fill(cGray.getRGB());
		parent.rect(x, y, 15, 15);
		if (s>=0){
			parent.noStroke();
			parent.fill(cs[id].getRGB());
			parent.ellipse(x+8, y+8, 12, 12);
		}
		parent.fill(cGray.getRGB());
		if (s>=0)
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
		//System.out.println("bMode:"+bMode);
		if (bMode>=0){
			count=0;
			if (s>0){
				Main.search = 0;
				s=-1;
			}	
			else{
				Main.search = id;
				s =1;
			}	
			return true;
		}
		return false;
		
	}
	
}