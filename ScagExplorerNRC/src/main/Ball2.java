package main;

import java.awt.Color;
import java.text.DecimalFormat;

import static main.Main.numS;
import static main.Main.data;
import static main.Main.scagVals;
import static main.Main.bM12;
import static main.Main.bP12;
import static main.Main.varlist;
import static main.Main.scagColors;
import static main.Main.scagNames;


import processing.core.*;

public class Ball2 {
	float r;
	float m;
	float x;
	float y;
	float vx;
	float vy;
	int id;
	float ka;
	int ocurrences;
	public PApplet parent;
	float kspring; // Constante de resorte
	float damp; // Damping
	float rest_posx;
	float rest_posy;
	float accel = 0; // Aceleracion
	float force = 0; // Fuerza
	public boolean b;
	public boolean s=false;
	
	public int vX = -1; 
	public int vY = -1; 
	public int month = -1; 
	public Integrator[] iP = new Integrator[9];
	public Integrator iR = new Integrator(0,0.1f,0.5f);
	public boolean active= true;
	public static int brushing =-1;
	
	Ball2(int ID, float KA, int id_, int v1, int v2, int m_, PApplet pa) {
		month = m_;		
		vX = v2;
		vY = v1;
		
		id = id_;
		if (id>=BBP2.maxBalls)
			active= false;
		//if (active)
		//System.out.println("id:"+id+" "+active);
		
		
		for (int j = 0; j < 9; j++) {
			iP[j] = new Integrator(0,0.2f,0.5f);
		}
		
		parent = pa;
		rest_posx = ((parent.width - BBP2.right) / 2)
				+ BBP2.left / 2;
		rest_posy = ((parent.height - BBP2.down) / 2)
				+ BBP2.right / 2;
		ka = KA;
		
		
		r = PApplet.sqrt(ka / PApplet.PI);
		iR.target(r);
		
		m = r;
		x = 820;
		y = 340;
		vx = parent.random(-3, 3);
		vy = parent.random(-3, 3);
		ocurrences = 10;
		b = false;
		damp = 0.85f;
		kspring = 0.01f;
	}

	void fall() {

	}

	public void spring() {
		rest_posx = ((parent.width - BBP2.right) / 2)
				+ BBP2.left / 2;
		rest_posy = ((parent.height - BBP2.down) / 2)
				+ BBP2.right / 2;

		if (BBP2.balls.length > 0
				&& (BBP2.balls[0].ocurrences - BBP2.balls[BBP2.bubble_plots - 1].ocurrences) > 0) {
			float A = BBP2.balls[0].ocurrences; // maximo original
			float C = ocurrences; // valor original
			float B = BBP2.balls[BBP2.bubble_plots - 1].ocurrences; // minimo
																			// original
			float D = 5; // nuevo maximo
			float E; // nuevo minimo
			if (BBP2.bubble_plots > 20)
				E = -1;
			else
				E = 0;
			kspring = -1 * (((A - C) / (A - B)) * (D - E) - D);
		}
		if (BBP2.bubble_plots == 1)
			kspring = 4;

		force = -kspring * (y - rest_posy); // f=-ky
		accel = force; // Asignar aceleracion
		vy = damp * (vy + accel); // Definir velocidad
		vx = damp * (vx + accel); // Definir velocidad
	}

	void bounce() {
		if (y + vy + r > parent.height - BBP2.down) {
			y = parent.height - BBP2.down - r;
			vx *= BBP2.f;
			vy *= -BBP2.b;
		}
		if (y + vy - r < BBP2.top) {

			y = r + BBP2.top;
			vx *= BBP2.f;
			vy *= -BBP2.b;
		}
		if (x + vx + r > parent.width - BBP2.right) {

			x = parent.width - BBP2.right - r;
			vx *= -BBP2.b;
			vy *= BBP2.f;
		}
		if (x + vx - r < BBP2.left) {

			x = r + BBP2.left;
			vx *= -BBP2.b;
			vy *= BBP2.f;
		}
	}

	void collide() {
		for (int i = BBP2.balls.length-1; i >= 0; i--) {
			if (BBP2.balls[i].active){
				float X = BBP2.balls[i].x;
				float Y = BBP2.balls[i].y;
				float R = BBP2.balls[i].r;
				float M = BBP2.balls[i].m;
				float deltax = X - x;
				float deltay = Y - y;
				float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
				if (d < r + R && d > 0) {
					float dD = r + R - d;
					float theta = PApplet.atan2(deltay, deltax);
					vx += -dD * PApplet.cos(theta) * M / (m + M);
					vy += -dD * PApplet.sin(theta) * M / (m + M);
					vx *= BBP2.b;
					vy *= BBP2.b;
				}
			}
		}
	}

	void move() {
		if (BBP2.g!=null) 
			return;
		
		if (b && parent.mousePressed && BBP2.count>1) {
			x = parent.mouseX;
			y = parent.mouseY;
			vx = 0;
			vy = 0;
			BBP2.arrastrando = id;
			s=!s;
			BBP2.count=0;
		} else {
			x += vx;
			y += vy;
		}
	}

	void checkBrushing() {
		if (BBP2.g!=null) 
			return;
		if (PApplet.dist(x, y, parent.mouseX, parent.mouseY) < r)
			b = true;
		else
			b = false;

	}

	void display() {
		iR.update();
		parent.fill(155,155,155,180);
		if (id==Polyline.brushing){
			parent.fill(255,0,255,180);		
		}
		parent.stroke(0,0,0);
		parent.strokeWeight(1);
		
		if (BBP2.show_background){
			if (BBP2.show_rect)
				parent.rect(x-r*0.78f,y-r*0.78f,r*1.56f,r*1.56f);
			else{
				parent.ellipse(x, y, r*2, r*2);
			}		
			if (BBP2.show_name){
				parent.textAlign(PApplet.LEFT);
				float size = 3+r * 0.14f;
				parent.textFont(BBP2.font, size);
				parent.fill(255, 0, 255,200);
				parent.text(varlist[vX], x-r*0.5f, y +r*0.75f);
				
				parent.translate(x-r*0.65f,y+r/2);
				parent.rotate((float) (-PApplet.PI/2.));
				parent.text(varlist[vY],0,0);
				parent.rotate((float) (PApplet.PI/2.));
				parent.translate(-(x-r*0.65f),-(y+r/2));
			}	
			parent.noStroke();
			parent.fill(0,0,0,180);
			for (int s = 0; s < numS; s++) {
				float x3 = x - r*0.65f + r*1.3f*data[vX][s];
				float y3 = y + r*0.65f - r*1.3f*(data[vY][s]);
				if (BBP2.sVar<varlist.length){
					float val = data[BBP2.sVar][s];
					Color color = ColorScales.getColor(val, "rainbow", 0.8f);
					parent.fill(color.getRGB());
				}
				
				parent.ellipse(x3, y3, 2+r/12, 2+r/12);
				if (PApplet.dist(x3, y3, parent.mouseX, parent.mouseY)<=r/12)
					brushing = s;
			}
			/*if (brushing>=0){
				parent.fill(Color.RED.getRGB());
				float x3 = x - rr*0.65f + rr*1.3f*data[vX][brushing];
				float y3 = y + rr*0.65f - rr*1.3f*(data[vY][brushing]);
				parent.ellipse(x3, y3, 2+rr/12, 2+rr/12);
				parent.textAlign(PApplet.CENTER);
				if (PApplet.dist(x3, y3, parent.mouseX, parent.mouseY)<=rr/12)
					parent.text(Main.schools[brushing],x3,y3-4);
			}*/
				 
		}
		
		drawPie();
		parent.textAlign(PApplet.CENTER);
		if (BBP2.show_info) {
			float size = r * 0.3f;
			parent.textFont(BBP2.font, size);
			parent.fill(255, 255, 0,200);
		//	parent.text(df.format((25-ocurrences)/100f), x, y-r*0.74f);
		}
	}
	public static Ball2[] append(Ball2 t[], float ka, int id, int v1, int v2, int m,
			PApplet parent) {
		Ball2 temp[] = new Ball2[t.length + 1];
		System.arraycopy(t, 0, temp, 0, t.length);
		temp[t.length] = new Ball2(t.length, ka, id, v1, v2,m, parent);
		return temp;
	}
	
	public void drawPie() {
		parent.textAlign(PApplet.LEFT);
		float lastAng = -PApplet.PI/2;
		parent.strokeWeight(1);
		parent.textFont(BBP2.font, 10+r/20);
		for (int i = 0; i < 9; i++){
			float v = 0;
			if (BBP2.show_scag)
				v = scagVals[i][id]*r*2;
			iP[i].target(v);
			iP[i].update();
			
			Color color =scagColors[i];
			
			parent.fill(color.getRGB());
			parent.arc(x,y, iP[i].value, iP[i].value, lastAng, lastAng+PApplet.PI*2/9);
			if (BBP2.show_scag && BBP2.show_info && id==bP12 && month==bM12)
				drawPieLabel(x,y,lastAng, lastAng+PApplet.PI*2/9, Main.scagNames[i],color.darker());
		    lastAng += PApplet.PI*2/9;  
		}
	}
	public void drawPieLabel(float cX, float cY,float al1 , float al2, String name,Color color) {
		float al = al1 + PApplet.PI/9;
		float eX = (r*0.2f);
		if (al<=PApplet.PI/2){  
			parent.translate(cX,cY);
			parent.rotate((float) (al+0.1));
			parent.fill(color.getRGB());
			parent.text(name,eX,0);
			parent.rotate((float) (-al-0.1));
			parent.translate(-(cX),-(cY));
		}
		else{
			al = al -PApplet.PI; 
			parent.translate(cX,cY);
			parent.rotate((float) (al-0.1));
			parent.fill(color.getRGB());
			float ww = parent.textWidth(name);
			parent.text(name,-ww-eX,0);
			parent.rotate((float) (-al+0.1));
			parent.translate(-(cX),-(cY));
		}
	}
}
