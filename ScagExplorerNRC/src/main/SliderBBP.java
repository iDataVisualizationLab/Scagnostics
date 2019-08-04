package main;

import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;

public class SliderBBP{
	int count =0;
	public int pair =-1;
	public PApplet parent;
	public float x,y;
	public int w; 
	public int u =-1;
	public float val =-1;
	
	public Color c1  = new Color(125,125,125);
	public Color c2  = new Color(125,125,125);
	
	public int bSlider = -1;
	public int sSlider = -1;
	public int ggg =5;
	public String text ="";
	public float scale =100;
	
	
	public SliderBBP(PApplet parent_, float xx, float yy, String text_, float scale_, int initial){
		parent = parent_;
		u = initial;
		x= xx;
		y= yy;
		w= 100*ggg;
		text = text_;
		scale = scale_;
	}
		
	public void update(){
		val = u/(scale*ggg);
	}
		
	public void draw(){
		checkBrushingSlider();
		if (count>4 && parent.mousePressed)
			checkSelectedSlider3();

		update();
		float xx2 = x+u;
		DecimalFormat df = new DecimalFormat("#.##");
		parent.stroke(Color.GRAY.getRGB());
		parent.strokeWeight(1.0f);
		for (int j=0; j<=10; j++ ){
			parent.line(x+j*10*ggg, y+9, x+j*10*ggg, y+16);
			if (j==10) break;
			for (int k=1; k<10; k++ ){
				parent.line(x+j*10*ggg+k*ggg, y+9, x+j*10*ggg+k*ggg, y+11);
			}
		}
		
		//Upper range
		if (sSlider==1){
			c2= Color.WHITE;
		}	
		else if (bSlider==1){
			c2= Color.PINK;
		}	
		else{
			c2 = new Color(170,170,170);
		}
		parent.textSize(13);
		parent.noStroke();
		parent.fill(c2.getRGB());
		parent.triangle(xx2-5, y+20, xx2+5, y+20, xx2, y+10);
		parent.textAlign(PApplet.RIGHT);
		parent.text(text, x-10,y+15);
		
		parent.textAlign(PApplet.CENTER);
		parent.textSize(12);
		parent.text(df.format(val), xx2,y+8);
		parent.textAlign(PApplet.LEFT);
		
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	
	
	public void checkBrushingSlider() {
		float xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx2-20<mX && mX < xx2+20 && y<mY && mY<y+25){
			bSlider =1; 
			return;
		}
		bSlider =-1;
	}
	
	public void checkSelectedSlider1() {
		sSlider = bSlider;
	}
	public void checkSelectedSlider2() {
		sSlider = -1;
	}	
	public int checkSelectedSlider3() {
		if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<0) u=0;
			if (u>w)  u=w;
			
		}
		return sSlider;
	}
		
}