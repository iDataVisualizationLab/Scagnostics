package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.*;
import static main.Main.*;

public class BBP1{
	public static int nro_linea = 0;
	public static int nro_espacio = 0;

	public static PFont font;
	public static String par_actual = ""; // valor actual del escaneo del texto
	public static int draggingBall = -1;

	public static int pares_totales = 0;
	public static float k_total;

	public static int down;
	public static int top;
	public static float right;
	public static float left;
	public static float centerX;
	public static float centerY;
	public static float X1;
	public static float Y1;
	public static int MAX_BALL_SHOWING = 50;
	public static int maxDisimilarity = 100;
	public static int maxBalls = MAX_BALL_SHOWING;
	public static int sasBalls = 0;
	public static int bubble_plots = 0;

	public static Ball1[] balls = new Ball1[0];
	public static float grav = 1.40f; // Gravedad
	public static float b = 0.90f; // Rebote
	public static float f = 0.20f; // Friccion

	public static Color ColorLineasGrales = new Color(200, 200, 0);

	public static int lapso_refresh = 1; // cada cuantos frames se renueva la
											// info del listado
	public static int timer_interno = 0;
	public static boolean resorte_activado = false;
	public static boolean show_name = false;
	public static boolean show_rect = true;
	public static boolean show_background = true;
	public static boolean show_info = false;
	public static boolean show_scag = false;
	public static boolean isHalt = false;
	public static boolean llenar_burbujas = false;
	public static boolean hay_gravedad = false;
	public static int bBall = -1;
	public PApplet parent;
	public HashMap<String,Integer> hm = new HashMap<String,Integer>();
	public HashMap<String,Integer> hDis = new HashMap<String,Integer>();
	public static ArrayList<String> key = new ArrayList<String>();
	public static ArrayList<String> keyDis = new ArrayList<String>();
	public static int count= 0;
	
	
	public BBP1(PApplet p, ArrayList<String> key_, ArrayList<String> keyDis_, int l, int r, int t, int d) {
		parent =p;
		calcularKtotal();
		font = parent.loadFont("Arial-BoldMT-18.vlw");
		down = d;
		top = t;
		right = r;
		left = l;
		
		key = key_;
		keyDis = keyDis_;
		count= 0;
		balls = new Ball1[0];
	}
	public void setData(HashMap<String,Integer> hm_) {
		hm =hm_;
		for (int k = 0; k < key.size(); k++) {
			int count = hm.get(key.get(k));
			newKP(key.get(k), count);
		}
	}
	public void setData2(HashMap<String,Integer> hm2_) {
		hDis =hm2_;
		for (int k = 0; k < keyDis.size(); k++) {
			int count = hDis.get(keyDis.get(k));
			newKP(keyDis.get(k), count);
		}
	}
	
	public void draw() {
		left = main.Main.iPlotX.value+750;
		centerX = left + (parent.width-right-left)/2;
		centerY = top + (parent.height-down -top)/2; 
		X1 = left;
		Y1 = top;
		
		if (parent.keyPressed)
			keyPressed() ;
		for (int i = 0; i < balls.length; i++) {
			if (!isHalt){
				balls[i].x += parent.random(-1, 1);
				balls[i].y += parent.random(-1, 1);
			}
		}
		
		// refrescar orden de la info
		timer_interno++;
		if (timer_interno == lapso_refresh) {
			timer_interno = 0;
			ordenarArrays();
		}
		pares_totales = 0;
		bubble_plots = 0;
		for (int i = balls.length-1; i >= 0; i--) {
			pares_totales += balls[i].ocurrences;
			bubble_plots++;
		}

		calcularKtotal();
		for (int i = 0; i < balls.length; i++) {
			float kprima = (k_total) / (balls.length/2);
			balls[i].ka = kprima;
			balls[i].r = PApplet.sqrt(((kprima) / PApplet.PI));
		}
		
		bBall =- 1;
		
		for (int i = balls.length-1; i >= 0; i--) {
			if (balls[i].checkBrushing() ){
				bBall =i;
			}
		}	
		
		for (int i = balls.length-1; i >= 0; i--) {
				if (!isHalt){
					if (hay_gravedad)
						balls[i].fall();
					//if (resorte_activado)
					balls[i].spring();
					balls[i].bounce();
					balls[i].collide();
					balls[i].move();
				}
				balls[i].display();
			
		}
		
		count++;
		if (count>100) count=100;
	}

	public void mouseClicked() {
		for (int i = BBP1.balls.length-1; i >= 0; i--) {
			if (i < balls.length) {
				balls[i].mouseClicked();
			}
		}	
	}
	
	void newKP(String newx, int count1) {
		calcularKtotal();
		float ka;
		if (balls.length > 0)
			ka = k_total / balls.length;
		else
			ka = k_total;
		Ball1[] tempBall = Ball1.append(balls, ka, newx, count1, parent);
		balls = tempBall;
		balls[0].s =true;
	}

	void ordenarArrays() {
		Ball1[] temp_ocurrencias = new Ball1[balls.length];
		System.arraycopy(balls, 0, temp_ocurrencias, 0, balls.length);
		Ball1 temp;
		int i, j;
		for (i = temp_ocurrencias.length - 1; i >= 0; i--)
			for (j = 0; j < i; j++)
				if (temp_ocurrencias[j].ocurrences < temp_ocurrencias[j + 1].ocurrences) {
					temp = temp_ocurrencias[j];
					temp_ocurrencias[j] = temp_ocurrencias[j + 1];
					temp_ocurrencias[j + 1] = temp;
				}
		balls = temp_ocurrencias;
	}

	//B
	void calcularKtotal() {
		float alto = parent.height - top - down;
		float ancho = parent.width - left - right;
		if (bubble_plots <= 1) {
			if (alto < ancho)
				k_total = PApplet.PI * PApplet.pow(alto / 2, 2f) * 0.5f;
			else
				k_total = PApplet.PI * PApplet.pow(ancho / 2f, 2f) * 0.5f;
		} else if (bubble_plots > 1 && bubble_plots <= 6)
			k_total = ancho * alto * 0.5f;
		else if (bubble_plots > 6 && bubble_plots <= 20)
			k_total = ancho * alto * 0.75f;
		else if (bubble_plots > 20 && bubble_plots <= 50)
			k_total = ancho * alto * 0.85f;
		else if (bubble_plots > 50 && bubble_plots <= 200)
			k_total = ancho * alto * 0.9f;
		else if (bubble_plots > 200)
			k_total = ancho * alto * 0.95f;

		float reduce =0.35f;
		k_total *= reduce;
	}
	//E
	
	public void keyPressed() {
		if (parent.key == '+') { // viendo mas burbujas
			MAX_BALL_SHOWING++;
			maxBalls++;
		}
		if (parent.key == '-') { // viendo menos burbujas
			if (maxBalls > 2){
				MAX_BALL_SHOWING--;
				maxBalls--;
			}	
		}
		if (parent.key == 'i' || parent.key == 'I') { // mostrar info en burbujas
				show_info = !show_info;
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
		if (parent.key == 'o') { // show scagnostic
			show_scag = !show_scag;
		}
		if (parent.key == 'u') { 
			show_background = !show_background;
		}
		if (parent.key == 'h') { 
			isHalt = !isHalt;
		}
		if (parent.key == 'd' || parent.key == 'D') { // redistribuyendo
			for (int i = 0; i < balls.length; i++) {
				balls[i].x = parent.random(balls[i].r + left, parent.width - right
						- balls[i].r);
				balls[i].y = parent.random(balls[i].r + top, parent.height - down
						- balls[i].r);
			}
		}
	}

}
