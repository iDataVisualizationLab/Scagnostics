package main;

import java.awt.Color;
import java.util.ArrayList;

import static main.Main.scagVals;
import static main.Main.scagColors;
import static main.Main.varlist;
import static main.Main.data;
import static main.Main.numS;


import processing.core.*;

public class Ball3 {
	float r;
	float x;
	float y;
	float dX,pushX, pullX;
	float dY,pushY, pullY;
	int id;
	public PApplet parent;
	public static int b=-1;
	
	public Integrator[] iP = new Integrator[9];
	public int vX = -1; 
	public int vY = -1; 
	public static float xx1 = 640;//380;
	public static float yy1 = 370;//370;
	public static float rr1 = 310;//350;
	
	Ball3(int ID, int id_, int v1, int v2, int m_, PApplet pa) {
		vX = v2;
		vY = v1;
		id = id_;
		for (int j = 0; j < 9; j++) {
			iP[j] = new Integrator(0,0.2f,0.5f);
		}
		parent = pa;
		r=14;
		x = parent.random(Ball3.xx1 - Ball3.rr1, Ball3.xx1 + Ball3.rr1);
		y = parent.random(Ball3.yy1 - Ball3.rr1, Ball3.yy1 + Ball3.rr1);
	}

	void spring(Ball3 ball2, int i1, int i2) {
		float val = Main.diss[i1][i2]-BBP3.slider1.val;
		if(Main.diss[i1][i2]<BBP3.slider1.val){
			//val *= BBP3.slider2.val; 
			pullX +=  val*Math.signum(x-ball2.x); 
			pullY +=  val*Math.signum(y-ball2.y);            
		}
		else{
			float disSquare = (x-ball2.x)*(x-ball2.x) + (y-ball2.y)*(y-ball2.y);
			val /= PApplet.pow(disSquare, 0.5f);
			pushX +=  val*Math.signum(x-ball2.x); 
			pushY +=  val*Math.signum(y-ball2.y);            
		}
		
	  }

	
	void bounce() {
		float dis = PApplet.sqrt((x-xx1)*(x-xx1) + (y-yy1)*(y-yy1));
		float rate = rr1/dis;
		if (rate < 1) {
			x = (float) (xx1+(x-xx1)*rate);
			y = (float) (yy1+ (y-yy1)*rate);
		}
	}

	void collide() {
		float rate = 0.6f;
		for (int i = BBP3.balls.length-1; i >= 0; i--) {
			float X = BBP3.balls[i].x;
			float Y = BBP3.balls[i].y;
			float R = BBP3.balls[i].r;
			float deltax = X - x;
			float deltay = Y - y;
			float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
			if (d < r + R && d > 0) {
				float dD = r + R - d;
				float theta = PApplet.atan2(deltay, deltax);
				dX += -dD * PApplet.cos(theta)*rate;
				dY += -dD * PApplet.sin(theta)*rate;
			}
		}
	}
	

	void updatePosition() {
		float rate = 1f;
		x += (dX+(pushX+pullX)*rate);
		y += (dY+(pushY+pullY)*rate);;
	}

	void checkBrushing() {
		if (PApplet.dist(x, y, parent.mouseX, parent.mouseY) < r)
			b = id;
	}

	void mouseClicked() {
		if (b==id) {
			x = parent.mouseX;
			y = parent.mouseY;
			if (isIn(id,BBP3.sBalls))
				BBP3.sBalls.remove((Object) id);
			else
				BBP3.sBalls.add(id);
		} 
	}
	
	public boolean isIn(int num, ArrayList<Integer> a) {
		for (int i=0;i<a.size();i++){
			if (a.get(i)==num)
				return true;
		}
		return false;
	}
		
	void display() {
		float rr2 = r*1.f;
		float xx2 = x+BBP3.x;
		float yy2 = y+BBP3.y;
		
		if (PopupVar.sS<9){
			Color color = ColorScales.getColor(scagVals[PopupVar.sS][id],"cyan", 0.75f);
			parent.fill(color.getRGB());
		}
		else{
			parent.fill(255,255,255,100);
		}
		
		if (BBP3.show_background){
			if (BBP3.show_rect)
				parent.rect(xx2-rr2*0.78f,yy2-rr2*0.78f,rr2*1.56f,rr2*1.56f);
			else{
				parent.ellipse(xx2, yy2, rr2*2, rr2*2);
			}		
		}
		drawPie(rr2,xx2,yy2);
	}
	
	public void display2() {
		float rr2 = r*4f;
		float xx2 = x+BBP3.x;
		float yy2 = y+BBP3.y;
		
		parent.stroke(0,0,0);
		parent.strokeWeight(0.4f);
		parent.fill(255,255,255,200);
		
		if (id==b){
			parent.fill(255,150,255,150);
		}
		
		
			if (BBP3.show_rect)
				parent.rect(xx2-rr2*0.78f,yy2-rr2*0.78f,rr2*1.56f,rr2*1.56f);
			else{
				parent.ellipse(xx2, yy2, rr2*2, rr2*2);
			}
			parent.stroke(50,50,50);
			parent.strokeWeight(0.5f);
			parent.fill(0,0,0);
			for (int s = 0; s < numS; s++) {
				float x3 = xx2 - rr2*0.65f + rr2*1.3f*data[vX][s];
				float y3 = yy2 + rr2*0.65f - rr2*1.3f*(data[vY][s]);
				parent.ellipse(x3, y3, 2+rr2/12, 2+rr2/12);
			}
		
	}
	
	public void drawPie(float rr2, float xx2, float yy2) {
		parent.textAlign(PApplet.LEFT);
		float lastAng = -PApplet.PI/2;
		parent.strokeWeight(1);
		for (int i = 0; i < 9; i++){
			float v = 0;
			if (BBP3.show_scag)
				v = scagVals[i][id]*rr2*2;
			iP[i].target(v);
			iP[i].update();
			Color color =scagColors[i];
			parent.fill(color.getRGB());
			float alpha = PApplet.PI*2/9;
			parent.arc(xx2,yy2, iP[i].value, iP[i].value, lastAng, lastAng+alpha);
			lastAng += alpha;  
		}
	}
	
	public static Ball3[] append(Ball3 t[], int id, int v1, int v2, int m,
			PApplet parent) {
		Ball3 temp[] = new Ball3[t.length + 1];
		System.arraycopy(t, 0, temp, 0, t.length);
		temp[t.length] = new Ball3(t.length, id, v1, v2,m, parent);
		return temp;
	}
	
	public void drawBallBig() {
		float w1 =  250;
		float x1 =  860;
		float y1 =  350;
		
		parent.stroke(200,200,200);
		parent.strokeWeight(3f);
		if (PopupVar.sS<9){
			Color color = ColorScales.getColor(scagVals[PopupVar.sS][Ball3.b],"cyan", 0.75f);
			parent.fill(color.getRGB());
		}
		else{
			parent.fill(255,255,255,100);
		}
		
		parent.rect(x1-20, y1-w1-20, w1+40, w1+40);
		
		parent.textAlign(PApplet.LEFT);
		parent.fill(200,200,200);
		parent.textSize(16);
		parent.text(varlist[BBP3.balls[b].vX], x1, y1 +40);
		
		parent.translate(x1-30,y1);
		parent.rotate((float) (-PApplet.PI/2.));
		parent.text(varlist[BBP3.balls[b].vY],0,0);
		parent.rotate((float) (PApplet.PI/2.));
		parent.translate(-(x1-30),-(y1));
		
		parent.textSize(14);
		parent.strokeWeight(1f);
		parent.stroke(0,0,0,250);
		for (int s = 0; s < numS; s++) {
			float x3 =  x1 + w1*data[BBP3.balls[b].vX][s];
			float y3 =  y1 - w1*data[BBP3.balls[b].vY][s];
			float v = s/(float) (numS-1);
			if (v >1) v=1; 
			Color c = ColorScales.getColor(v, "rainbow", 0.95f);
			parent.fill(c.getRGB());
			parent.ellipse(x3, y3, 13, 13);
			if (s%12==0){
				parent.fill(0,0,0,200);
				parent.text(""+(2000+s/12),x3+8, y3);
				}
		}
		parent.strokeWeight(1f);
	}
	
	public void drawPieBig() {
		parent.textAlign(PApplet.LEFT);
		float lastAng = -PApplet.PI/2;
		parent.noStroke();
		parent.textFont(BBP3.font, 10+r/20);
		
		float rrr = 300;
		float xxx = 980;
		float yyy = 570;
		PFont f = parent.createFont("Courier",40,true);
		parent.textAlign(PApplet.LEFT);
		parent.strokeWeight(1);
		
		for (int i = 0; i < 9; i++){
			float v = scagVals[i][id]*rrr;
			Color color =scagColors[i];
			parent.fill(color.getRed(), color.getGreen(),color.getBlue(),50);
			parent.arc(xxx,yyy, rrr, rrr, lastAng, lastAng+PApplet.PI*2/9);
			parent.fill(color.getRGB());
			parent.arc(xxx,yyy, v, v, lastAng, lastAng+PApplet.PI*2/9);
			parent.textFont(f, 16);
			float arclength = 0;
			for (int j = 0; j < Main.scagNames[i].length(); j ++ ) {
				    char currentChar = Main.scagNames[i].charAt(j);
				    float w = parent.textWidth(currentChar); 
				    arclength += w/2;
				    float theta = lastAng+arclength / (rrr/2);
				    parent.pushMatrix();
				    parent.translate(xxx+rrr*0.5f*PApplet.cos(theta), yyy+rrr*0.5f*PApplet.sin(theta)); 
				    parent.rotate(theta + PApplet.PI/2); 
				    parent.fill(color.getRGB());
				    parent.text(currentChar,0,0);
				    parent.popMatrix();
				    arclength += w/2;
			}
			lastAng += PApplet.PI*2/9;  
		}
		parent.textFont(BBP3.font, 15);
	}
	
	
}
