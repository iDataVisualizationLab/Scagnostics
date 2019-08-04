package main;

import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;
import static main.Main.sgap1;
import static main.Main.disArray;
import static main.Main.iPlotX;
import static main.Main.iPlotY;

public class Slider4{
	int count =0;
	public int pair =-1;
	public PApplet parent;
	public float x,y;
	public int w; 
	public int l =-1;
	public int u =-1;
	public float lM =-1;
	public float uM =-1;
	public float hM =-1;
	
	public static Color c1  = new Color(125,125,125);
	public static Color c2  = new Color(125,125,125);
	
	public int bSlider = -1;
	public int sSlider = -1;
	
	
	public Slider4(PApplet parent_){
		parent = parent_;
		l = 0;
		u = 400;
		x= 1240;
		y= 200;
		w= (BBP1.maxDisimilarity)*sgap1;
	}
		
	public void update(){
		lM = l/(100f*sgap1);
		uM = u/(100f*sgap1);
		hM = w/(100f*sgap1);
	}
		
	public void draw(){
		x = iPlotX.value+850;
		y = iPlotY.value+610;
		checkBrushingSlider();
		if (count>4 && parent.mousePressed)
			checkSelectedSlider3();

		update();
		float xx1 = x+l;
		float xx2 = x+u;
		DecimalFormat df = new DecimalFormat("#.##");

		parent.stroke(Color.GRAY.getRGB());
		parent.strokeWeight(1.0f);
		for (int j=0; j<=BBP1.maxDisimilarity/10; j++ ){
			parent.line(x+j*10*sgap1, y+9, x+j*10*sgap1, y+14);
			if (j==BBP1.maxDisimilarity/10) break;
			for (int k=1; k<10; k++ ){
				parent.line(x+j*10*sgap1+k*sgap1, y+9, x+j*10*sgap1+k*sgap1, y+11);
			}
		}
		
		//Lower range
		if (sSlider==0){
			c1= Color.WHITE;
		}	
		else if (bSlider==0){
			c1= Color.PINK;
		}	
		else{
			c1 = new Color(170,170,170);
		}
		parent.noStroke();
		parent.fill(c1.getRGB());
		parent.triangle(xx1-5, y+25, xx1+5, y+25, xx1, y+15);
		parent.textAlign(PApplet.CENTER);
		String lT =df.format(lM);
		parent.text(lT,xx1,y+36);
		
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
		parent.noStroke();
		parent.fill(c2.getRGB());
		parent.triangle(xx2-5, y+25, xx2+5, y+25, xx2, y+15);
		parent.text(df.format(uM), xx2,y+36);
		parent.textAlign(PApplet.LEFT);
		
		if (sSlider==2){
			c2= Color.WHITE;
		}	
		else if (bSlider==2){
			c2= Color.PINK;
		}	
		else{
			c2 = new Color(170,170,170);
		}
		parent.stroke(c2.getRGB());
		parent.line(x+w+1,y,x+w+1,y+40);
		parent.fill(c2.getRGB());
		parent.text(df.format(hM), x+w+2,y+34);
		parent.noStroke();
		
		count++;
	    if (count==10000)
	    	count=200;
	}
	
	
	
	public void checkBrushingSlider() {
		float xx1 = x+l;
		float xx2 = x+u;
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		
		if (xx1-20<mX && mX < xx1+20 && y<mY && mY<y+45){
			bSlider =0;
			return;
		}	
		else if (xx2-20<mX && mX < xx2+20 && y<mY && mY<y+45){
			bSlider =1; 
			return;
		}
		else if (x+w-20<mX && mX < x+w+20 && y<mY && mY<y+50){
			bSlider =2; 
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
		
		if (sSlider==0){
			l += (parent.mouseX - parent.pmouseX);
			if (l>=u) l=u-1;
			if (l<0)  l=0;
		}	
		else if (sSlider==1){
			u += (parent.mouseX - parent.pmouseX);
			if (u<=l) u=l+1;
			if (u>w)  u=w;
			
		}
		else if (sSlider==2){
			w += (parent.mouseX - parent.pmouseX);
			if (w<=u+10) w=u+11;
			BBP1.maxDisimilarity = w/sgap1;
			w = BBP1.maxDisimilarity*sgap1; 
		}
		
			
		updateMaxBalls();
		return sSlider;
	}
	
		public void updateMaxBalls() {
			int count =0;
			for (int p =0; p<disArray.length;p++){
					if (Main.disArray[p]<=u/sgap1){
						count++;
					}
				
			}	
			BBP1.sasBalls =count;
			if (count<BBP1.MAX_BALL_SHOWING)
				BBP1.maxBalls =count;
			else
				BBP1.maxBalls =BBP1.MAX_BALL_SHOWING;
		}	
}