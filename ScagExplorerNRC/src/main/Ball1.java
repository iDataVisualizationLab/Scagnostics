package main;

import java.awt.Color;
import java.text.DecimalFormat;

import static main.Main.bP2;
import static main.Main.found2;
import static main.Main.numS;
import static main.Main.data;
import static main.Main.scagVals;
import static main.Main.bP12;
import static main.Main.varlist;
import static main.Main.scagColors;


import processing.core.*;

public class Ball1 {
	float r;
	float m;
	float x;
	float y;
	float vx;
	float vy;
	int id;
	float ka;
	String name;
	int ocurrences;
	float ocurrenceStandardized;
	public PApplet parent;
	float mass; // Masa
	float kspring; // Constante de resorte
	float damp; // Damping
	float rest_posx;
	float rest_posy;
	float accel = 0; // Aceleracion
	float force = 0; // Fuerza
	float dissimilarity = 0; 
	public boolean b;
	public boolean s=false;
	
	public int pair = -1; 
	public int vX = -1; 
	public int vY = -1; 
	public Integrator[] iP = new Integrator[9];
	
	
	Ball1(int ID, float KA, String key, int count, PApplet pa) {
		pair  = Integer.parseInt(key);
		
		int[] indexes = PlotSPLOM.pairToIndex(pair);
		vX = indexes[1];
		vY = indexes[0];
		
		for (int j = 0; j < 9; j++) {
			iP[j] = new Integrator(0,0.2f,0.5f);
		}
		
		parent = pa;
		rest_posx = ((parent.width - BBP1.right) / 2)
				+ BBP1.left / 2;
		rest_posy = ((parent.height - BBP1.down) / 2)
				+ BBP1.right / 2;
		ka = KA;
		
		
		r = ka / PApplet.PI;
		m = r;
		x = BBP1.centerX;
		y = parent.random(BBP1.top, parent.height - BBP1.down);
		vx = parent.random(-3, 3);
		vy = parent.random(-3, 3);
		id = ID;
		name = key;
		b = false;
		mass = PApplet
				.sqrt((((PApplet.PI
						* PApplet.pow(
								(parent.height - BBP1.down - BBP1.top) / 2,
								2) * 0.8f) / 2000) / PApplet.PI));
		damp = 0.9f;
		kspring = 0.01f;
		
		ocurrences = 100-count;
		ocurrenceStandardized = PApplet.map(count, 1, 0, Main.maxDis, 0);;
		dissimilarity = (count)/100f;
		
	}

	void fall() {
	}

	void spring() {
		rest_posx = BBP1.centerX;
		rest_posy = BBP1.centerY;
		
		float mid  =0f;
		if (id<BBP1.key.size())
			mid= PApplet.map(Main.maxDisCenter*2, 1, 0, Main.maxDis, 0);
		else{
			mid= PApplet.map(Main.maxDisCenter+20, 1, 0, Main.maxDis, 0);
		}
		
		if ( BBP1.balls.length > 0) {
	      float A = BBP1.balls[0].ocurrenceStandardized;                        // maximo original
	      float C = ocurrenceStandardized;                                 // valor original
	      float B = mid;    // minimo original
	      float D = 5;                                           // nuevo maximo
	      float E;                                               // nuevo minimo
	      if ( BBP1.balls.length > 20 ) E = -1;
	      else E = 0;
	      kspring = -1 * ( ( ( A - C ) / ( A - B ) ) * ( D - E ) - D );
	    }
	    if ( BBP1.balls.length == 1 ) kspring = 4;
	    
	    //mass = r;
	    
	    if (id<BBP1.key.size())
	    	 kspring /= 50f;
		else
			kspring /= 40f;
		 
	    
	    force = -kspring * (y - rest_posy);    // f=-ky 
	    accel = force / mass;                  // Asignar aceleracion
	    vy = damp * (vy + accel);              // Definir velocidad 
	    //y += vy;

	    force = -kspring * (x - rest_posx);    // f=-ky 
	    accel = force / mass;                  // Asignar aceleracion
	    vx = damp * (vx + accel);              // Definir velocidad 
	    //x += vx;
	  }

	public void spring2() {
		rest_posx = BBP1.centerX;
		rest_posy = BBP1.centerY;
		float mul = (1-dissimilarity); 
		
		force = -kspring * (y - rest_posy); // f=-ky
		accel = force / mass; // Asignar aceleracion
		vy = damp * (vy + accel); // Definir velocidad
		vy *= mul; 
		
		force = -kspring * (x - rest_posx); // f=-ky
		accel = force / mass; // Asignar aceleracion
		vx = damp * (vx + accel); // Definir velocidad
		vx *= mul; 
		
	}
	
	
	void bounce() {
		if (y + vy + r > parent.height - BBP1.down) {
			y = parent.height - BBP1.down - r;
			vx *= BBP1.f;
			vy *= -BBP1.b;
		}
		if (y + vy - r < BBP1.top) {
			y = r + BBP1.top;
			vx *= BBP1.f;
			vy *= -BBP1.b;
		}
		if (x + vx + r > parent.width - BBP1.right) {
			x = parent.width - BBP1.right - r;
			vx *= -BBP1.b;
			vy *= BBP1.f;
		}
		if (x + vx - r < BBP1.left) {
			x = r + BBP1.left;
			vx *= -BBP1.b;
			vy *= BBP1.f;
		}
	}

	void collide() {
		for (int i = BBP1.balls.length-1; i >= 0; i--) {
			float X = BBP1.balls[i].x;
			float Y = BBP1.balls[i].y;
			float R = BBP1.balls[i].r;
			float M = BBP1.balls[i].m;
			float deltax = X - x;
			float deltay = Y - y;
			float d = PApplet.sqrt(PApplet.pow(deltax, 2) + PApplet.pow(deltay, 2));
			if (d <= (r + R) && d > 0) {
				float dD = r + R - d;
				float theta = PApplet.atan2(deltay, deltax);
				vx += -dD * PApplet.cos(theta) * M / (m + M);
				vy += -dD * PApplet.sin(theta) * M / (m + M);
				vx *= BBP1.b;
				vy *= BBP1.b;
			}
		}
		
	}

	void move() {
		if (b && parent.mousePressed && BBP1.count>1) {
			x = parent.mouseX;
			y = parent.mouseY;
			vx = 0;
			vy = 0;
			BBP1.draggingBall = id;
			BBP1.count=0;
		} else {
			x += vx*0.7f;
			y += vy*0.7f;
		}
	}

	public void mouseClicked() {
		if (b) {
			s=true;
		}	
	}
		
	public boolean checkBrushing() {
		if (PApplet.dist(x, y, parent.mouseX, parent.mouseY) < r)
			b = true;
		else
			b = false;
		return b;

	}

	void display() {
		float A = BBP1.balls[0].ocurrences; // maximo original
		float C = ocurrences; // valor original
		float B = BBP1.balls[BBP1.bubble_plots - 1].ocurrences; // minimo
		if (BBP1.llenar_burbujas)
			parent.fill(255, 0, 255);
		else
			parent.noFill();
		
		if (b)
			parent.fill(255, 0, 0, 115);
			// stroke(ColorLineasGrales);
			float lc = -1 * (((A - C) / (A - B)) * (60 - 200) - 60);
			float lcalpha = -1 * (((A - C) / (A - B)) * (255 - 90) - 255);
			if (A == B)
				lcalpha = 255;
			if (lc > 255)
				lc = 255;
			else if (lc < 1)
				lc = 1;

			Color local = new Color(0, 0, 0, 120);
			parent.stroke(local.getRGB());
			parent.strokeWeight(r / 20);
			// noFill();
			
			float tamanio = r * 0.8f;
			parent.textFont(BBP1.font, tamanio);
			//parent.fill(255, 0,255, lcalpha);
			int count = 100 - ocurrences;
			
			float val = ((float) BBP1.maxDisimilarity -count)/BBP1.maxDisimilarity;
			if (val<0) val =0;
			else if (val>1) val=1;
			Color color = ColorScales.getColor(val, "temperature", 1f);
			parent.fill(color.getRGB());
			
			float rr = r*0.99f;
			//parent.noStroke();
			
			// Selected Plot is centered
			if (pair==bP12){
				x= BBP1.centerX;
				y= BBP1.centerY;
			}
			
			if (BBP1.show_background){
				if (pair==bP12 ){
					parent.strokeWeight(2);
					parent.stroke(255,255,0);
					
					if (BBP1.show_rect){
						parent.rect(x-r*0.78f,y-r*0.78f,r*1.56f,r*1.56f);
					}
					else
						parent.ellipse(x, y, rr*2, rr*2);
					parent.strokeWeight(1);
					
				}
				else{
					
					if (s && BBP1.bBall==id){
						parent.stroke(255,0,255);
						bP2 = pair;
						found2 = true;
					}
					else if (b && BBP1.bBall==id){
						parent.stroke(255,255,255);
					}
					
					if (BBP1.show_rect)
						parent.rect(x-r*0.78f,y-r*0.78f,r*1.56f,r*1.56f);
					else{
					
						//System.out.println("BALL:"+month+" "+x+" "+r);
						parent.ellipse(x, y, rr*2, rr*2);
					}	
				}
			
				parent.noStroke();
				parent.fill(0,0,0);
				for (int s = 0; s < numS; s++) {
					float x3 = x - rr*0.65f + rr*1.3f*data[vX][s];
					float y3 = y - rr*0.65f + rr*1.3f*(1-data[vY][s]);
					parent.ellipse(x3, y3, 2+rr/13, 2+rr/13);
				}
			}
			
				
			drawPie();
			parent.textAlign(PApplet.CENTER);
			if (BBP1.show_info) {
				float size = 3+r* 0.20f;
				if (pair!=bP12 ){
					DecimalFormat df = new DecimalFormat("#.##");
					parent.textFont(BBP1.font, size);
					parent.fill(150, 0, 150);
					parent.text(df.format(dissimilarity) , x, y-r*0.50f);
					
				}
			}
			if (BBP1.show_name){
				float size = 5+r * 0.2f;
				parent.textFont(BBP1.font, size);
				parent.fill(255, 255, 255);
				parent.text(varlist[vX], x, y +r*0.9f);
				
				parent.fill(255, 255, 255);
				parent.textAlign(PApplet.LEFT);
				parent.translate(x-r*0.77f,y+r*0.7f);
				parent.rotate((float) (-PApplet.PI/2.));
				parent.text(varlist[vY],0,0);
				parent.rotate((float) (PApplet.PI/2.));
				parent.translate(-(x-r*0.77f),-(y+r*0.7f));
			}	
			
	}
	public static Ball1[] append(Ball1 t[], float ka, String key, int count,
			PApplet p) {
		Ball1 temp[] = new Ball1[t.length + 1];
		System.arraycopy(t, 0, temp, 0, t.length);
		temp[t.length] = new Ball1(t.length, ka, key, count, p);
		return temp;
	}
	
	public void drawPie() {
		parent.textAlign(PApplet.LEFT);
		float lastAng = -PApplet.PI/2;
		parent.strokeWeight(1);
		parent.textFont(BBP1.font, 10+r/16);
		for (int i = 0; i < 9; i++){
			float v = 0;
			if (BBP1.show_scag)
				v = scagVals[i][pair]*r*2;
			iP[i].target(v);
			iP[i].update();
			
			Color color =scagColors[i];
			
			parent.fill(color.getRGB());
			parent.arc(x,y, iP[i].value, iP[i].value, lastAng, lastAng+PApplet.PI*2/9);
			if (BBP1.show_scag && BBP1.show_info && pair==bP12){
				parent.fill(255,255,255);
				drawPieLabel(x+1,y+1,lastAng, lastAng+PApplet.PI*2/9, Main.scagNames[i],color);
				
				parent.fill(color.getRGB());
				drawPieLabel(x,y,lastAng, lastAng+PApplet.PI*2/9, Main.scagNames[i],color.darker());
				
			}	
		    lastAng += PApplet.PI*2/9;  
		}
	}
	public void drawPieLabel(float cX, float cY,float al1 , float al2, String name,Color color) {
		float al = al1 + PApplet.PI/9;
		float eX = (r*0.35f);
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
