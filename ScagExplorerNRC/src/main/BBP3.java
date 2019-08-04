package main;

import java.util.ArrayList;

import processing.core.*;
import static main.Main.*;

public class BBP3{
	public static PFont font;

	public static Ball3[] balls = new Ball3[0];
	public static float f = 0.01f; // Friccion
	
	public static boolean show_name = false;
	public static boolean halt = false;
	public static boolean show_rect = false;
	public static boolean show_background = true;
	public static boolean show_pull = false;
	public static boolean show_push = false;
	public static boolean show_scag = false;
	public static boolean no_gravity = false;
	public PApplet parent;
	public PopupVar popup;
	public static int sVar =33;
	public static SliderBBP slider1;
	public static float scale = 1f;
	public static float x = 0f;
	public static float y = 0f;
	public static ArrayList<Integer> sBalls = new ArrayList<Integer>();
	
	
	
	public BBP3(PApplet p, int l, int r, int t, int d) {
		parent =p;
		font = parent.loadFont("Arial-BoldMT-18.vlw");
		
		popup = new PopupVar(parent);
		slider1 = new SliderBBP(parent, 950, 10, "Dissimilarity Cut", 100f,300);
	}
	
	
	public void createBalls() {
		int k=0;
		for (int v1 =0; v1<numV;v1++){
			for (int v2 =0; v2<v1;v2++){
				newKP(k,v1,v2,0);
				k++;
			}	
		}
	}

	public void draw() {
		//for (int i = 0; i < balls.length; i++) {
		//	balls[i].x += parent.random(-1, 1);
		//	balls[i].y += parent.random(-1, 1);
		//}
		parent.scale(scale);
		
		parent.noStroke();
		for (int i = 0; i < balls.length; i++) {
			balls[i].pullX =0;
			balls[i].pullY =0;
			balls[i].pushX =0;
			balls[i].pushY =0;
			balls[i].dX =0;
			balls[i].dY =0;
			if (!halt){
				for (int i2 = 0; i2 < balls.length; i2++) {
					if (i!=i2 ){
						balls[i].spring(balls[i2],i,i2);
					}
				}
				balls[i].collide();
			}
			balls[i].updatePosition();
			balls[i].bounce();
			balls[i].display();
			
			if (show_pull){
				parent.stroke(0,255,0);
				parent.line(balls[i].x, balls[i].y, balls[i].x+balls[i].pullX*10, balls[i].y+balls[i].pullY*10);
			}
			if (show_push){
				parent.stroke(255,0,0);
				parent.line(balls[i].x, balls[i].y, balls[i].x+balls[i].pushX*10, balls[i].y+balls[i].pushY*10);
			}
		}
		
		if (Ball3.b>=0){
			//balls[Ball3.b].drawBallBig();
			//balls[Ball3.b].drawPieBig();
			balls[Ball3.b].display2();
		}
		
		for (int i=0;i<sBalls.size();i++){
			int index = sBalls.get(i);
			balls[index].display2();
		}
		
		slider1.draw();
		//slider2.draw();
		//slider3.draw();
		popup.draw2();
	}
	
	


	void newKP(int id, int v1,int v2, int m) {
		Ball3[] tempBall = Ball3.append(balls, id, v1,v2,m, parent);
		balls = tempBall;
	}
	
	
	
	public void mouseClicked() {
		System.out.println(x);
		for (int i = 0; i < balls.length; i++) {
			balls[i].mouseClicked();
		}
	}
	public void mouseMoved() {
		Ball3.b=-1;
		for (int i = 0; i < balls.length; i++) {
			balls[i].checkBrushing();
		}
			
	}
	public void mouseDragged() {
	
	}
	public void mousePressed() {
		slider1.checkSelectedSlider1();
	}	 
	public void mouseReleased() {
		slider1.checkSelectedSlider2();
	}
	
	public void keyPressed() {
		if (parent.key == '+') { // mostrar info en burbujas
			scale /= 0.9f;
		}
		if (parent.key == '-') { // mostrar info en burbujas
			scale *= 0.9f;
		}
	if (parent.key == '0') { // mostrar info en burbujas
			scale = 1;
			x=0;
			y=0;
	}
	if (parent.key == '1') { // mostrar info en burbujas
				show_pull = !show_pull;
		}
		if (parent.key == '2') { // mostrar info en burbujas
			show_push = !show_push;
	}
		if (parent.key == 'n' || parent.key == 'N') { // mostrar info en burbujas
				show_name = !show_name;
		}
		if (parent.key == 's') { // shaking
			for (int i = 0; i < balls.length; i++) {
				balls[i].x += parent.random(-10, 10);
				balls[i].y += parent.random(-10, 10);
			}
		}
		if (parent.key == 'r') { // shaking
			show_rect = !show_rect;
		}
		if (parent.key == 'h') { // HALT
			halt = !halt;
		}
		
		if (parent.key == 'o') { // shaking
			show_scag = !show_scag;
		}
		if (parent.key == 'u') { // shaking
			show_background = !show_background;
		}
		if (parent.key == 'd' || parent.key == 'D') { // redistribuyendo
			for (int i = 0; i < balls.length; i++) {
				balls[i].x = parent.random(Ball3.xx1 - Ball3.rr1, Ball3.xx1 + Ball3.rr1);
				balls[i].y = parent.random(Ball3.yy1 - Ball3.rr1, Ball3.yy1 + Ball3.rr1);
			}
		}
	}

}
