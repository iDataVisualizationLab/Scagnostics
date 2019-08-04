package main;
import java.awt.Color;

import processing.core.PApplet;
import processing.core.PFont;

public class PopupMenu extends PApplet{
	public int b = -1;
	public PApplet parent;
	public float x = 500;
	public int y = 0;
	public int w = 90;
	public int h = 28;
	public int itemH = 20;
	public int itemNum = 9;
	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	public Color cGray  = new Color(240,240,240);
	public PopupMenu(PApplet parent_){
		parent = parent_;
	}
	public int count =0;
	
	
	public void draw2(){
		x = main.Main.iPlotX.value+500;
		checkBrushing();
		if (count>6 && parent.mousePressed)
			checkPressed();
		if (b>=0){
			parent.textFont(metaBold, 13);
			parent.fill(new Color(50,50,50,240).getRGB());
			parent.noStroke();
			parent.rect(x, y-2, w, 192);
			for (int i=0;i<9;i++){
				if (i==Main.sS){
					parent.fill(Color.GRAY.getRGB());
					parent.rect(x,y+itemH*(i)+5,w,itemH);
				}
					
				if (i==b){
					parent.fill(Color.MAGENTA.getRGB());
				}
				else{
					parent.fill(Color.WHITE.getRGB());
				}
				parent.textAlign(PApplet.CENTER);
				parent.text(Main.scagNames[i],x+w/2,y+itemH*(i+1));
			}	
		}
		else{
			parent.textFont(metaBold,14);
			parent.fill(new Color(200,200,200,80).getRGB());
			parent.noStroke();
			parent.rect(x, y, w, h);
			
			parent.fill(new Color(255,255,255,255).getRGB());
			parent.textAlign(PApplet.CENTER);
			parent.text(Main.scagNames[Main.sS],x+w/2,y+20);
			parent.textFont(metaBold,13);
			parent.textAlign(PApplet.LEFT);
			parent.text("Color SPLOM by: ",x-115,y+20);
			
		}	
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	 public void checkPressed() {
		  if (b>=0){
				count=0;
				Main.sS =b;
		}	
   	}
	 
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (b==-1){
			if (x<mX && mX<x+w && y<=mY && mY<=itemH+5){
				b =100;
				return;
			}	
		}
		else{
			for (int i=0; i<itemNum; i++){
				if (x<mX && mX<x+w && y+itemH*i<=mY && mY<=itemH*(i+1)+5){
					b =i;
					return;
				}	
			}
		}
		b =-1;
	}
	
}