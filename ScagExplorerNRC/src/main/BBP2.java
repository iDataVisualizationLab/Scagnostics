package main;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

import GraphLayout.Edge;
import GraphLayout.Graph;
import GraphLayout.Node;
import GraphLayout.Vector3D;

import processing.core.*;
import static main.Main.*;

public class BBP2{
	public static int nro_linea = 0;
	public static int nro_espacio = 0;

	public static PFont font;
	public static String par_actual = ""; // valor actual del escaneo del texto
	public static int arrastrando = -1;

	public static int total_pairs = 0;
	public float k_total;

	public static int down;
	public static int top;
	public static int right;
	public static int left;
	public static int maxBalls = 50;
	public static int bubble_plots = 0;

	public static Ball2[] balls = new Ball2[0];
	public static float b = 0.85f; // Rebote
	public static float f = 0.10f; // Friccion
	
	public static boolean show_name = false;
	public static boolean show_rect = true;
	public static boolean show_background = true;
	public static boolean show_info = false;
	public static boolean show_scag = false;
	public static boolean no_gravity = false;
	public static boolean show_graph = true;
	public PApplet parent;
	public static int count=0;
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[] correlation = new ArrayList[numV];
	@SuppressWarnings("unchecked")
	public static ArrayList<Float>[] correlationVal = new ArrayList[numV];
	public static Graph g;
	public PopupVar popup;
	public static int sVar =33;
	
	
	public BBP2(PApplet p, int l, int r, int t, int d) {
		parent =p;
		calcularKtotal();
		font = parent.loadFont("Arial-BoldMT-18.vlw");
		down = d;
		top = t;
		right = r;
		left = l;
		balls = new Ball2[0];
		popup = new PopupVar(parent);
	}
	public  Graph buildGraph() {
		  Graph  g = new Graph();
		  int nNodes=0;
		  for (int v = 0; v < numV; v++) {
				if (correlation[v].size()>0){
					Node n = new Node(new Vector3D(left + parent.random((parent.width-left)), 
							parent.random(parent.height), 0), parent,v, varlist[v]);
					
					float total =0;
					 for (int i = 0; i < correlation[v].size(); i++) {
						 total += correlationVal[v].get(i);
					 }
					n.setMass(PApplet.pow(total,0.7f));
					 
				    g.addNode(n);
				    nNodes++;
				}	
		  }
		 	
		  int nEdges=0;
			 
		  for (int i = 0; i < nNodes; i++) {
			  Node n1 = g.getNodes().get(i);
			  int v1 = g.getNodes().get(i).var;
		  
			  for (int j=0; j<correlation[v1].size(); j++) {
					int v2 = correlation[v1].get(j);
					Node n2 = g.getNodeByVar(v2);
					
					float mono = correlationVal[v1].get(j);
					if (n1 != n2 && !(g.isConnected(n1,n2))) {
					      Edge e = new Edge(n1, n2, parent);
					      e.length = 250+(0.5f-mono)*100;
					      e.thickness = (mono-0.25f)*5;
					      e.mono = mono;
					      e.color = ColorScales.getColor(mono, "temperature", 1f);
						  g.addEdge(e);
					      nEdges++;
					}
			  }
		  
		  }
		  System.out.println("nNodes:"+nNodes+" nEdges:"+nEdges);
		  
		  return g;
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
	@SuppressWarnings("unchecked")
	public void draw() {
		if (parent.keyPressed)
			keyPressed() ;
		for (int i = 0; i < balls.length; i++) {
			balls[i].x += parent.random(-1, 1);
			balls[i].y += parent.random(-1, 1);
		}
		
		if (Main.change){
			total_pairs = 0;
			bubble_plots = 0;
			for (int i = 0; i < balls.length; i++) {
				if (balls[i].active) {
					total_pairs += balls[i].ocurrences;
					bubble_plots++;
				}
			}
			calcularKtotal();
			for (int i = 0; i < balls.length; i++) {
				if (balls[i].active) {
					float kprima = (k_total * balls[i].ocurrences) / total_pairs;
					balls[i].ka = kprima;
					balls[i].r = PApplet.sqrt(((kprima) / PApplet.PI));
					balls[i].iR.target(balls[i].r);					
				}
			}
			Main.change =false;
		}
		correlation = new ArrayList[numV];
		correlationVal = new ArrayList[numV];
		for (int v = 0; v < numV; v++) {
			correlation[v] = new ArrayList<Integer>();
			correlationVal[v] = new ArrayList<Float>();
		}
		for (int i = 0; i < balls.length; i++) {
			if (balls[i].active) {
				balls[i].bounce();
				balls[i].collide();
				balls[i].move();
				balls[i].checkBrushing();
				balls[i].display();
				
				// graph monotonic
				if (varlist[balls[i].vX].contains(":5th") ||
						varlist[balls[i].vY].contains(":5th"))
					continue;
				correlation[balls[i].vX].add(balls[i].vY);
				correlation[balls[i].vY].add(balls[i].vX);
				correlationVal[balls[i].vX].add(scagVals[8][i]);
				correlationVal[balls[i].vY].add(scagVals[8][i]);
				
			}	
		}
		
		 if (g != null) {
		    //parent.fill(0,0,0,255);
			 parent.fill(255,255,255);
			parent.noStroke();
			parent.rect(0, 0, parent.width, parent.height);
			
			if (parent.key!='h')
				doLayout();
		    g.draw();  
		    
		    // Color legend
		    float xx = parent.width-50;
			float yy = 40;
			float ww = 20;
			float g = 3;
			
			parent.fill(0);
			parent.textSize(14);
			parent.textAlign(PApplet.LEFT);
			parent.text("Monotonic",xx-40, yy-5);
			parent.fill(200,200,200);
			parent.textAlign(PApplet.RIGHT);
			DecimalFormat df = new DecimalFormat("#.##");
			for (int i = 50; i <= 100; i++) {
				yy = yy+1.5f*g;
				float val = i/100.f;
				Color color = ColorScales.getColor(val, "temperature", 1f);
				parent.fill(color.getRGB());
				parent.rect(xx,yy,ww,g+2);
				if (i%10==0){
					parent.text(df.format(val),xx-5, yy+8);
				}
			}
			// Sum monotonic
			yy = yy+50;
			parent.fill(0,0,0);
			
			parent.textAlign(PApplet.CENTER);
			parent.text("Sum monotonic",xx-14, yy+6);
			yy = yy+14;
			parent.text("by variable",xx-14, yy+6);
			parent.textAlign(PApplet.RIGHT);
			
			yy = yy+8;
			for (float i = 0.5f; i < 5; i++) {
				float w = 10+PApplet.pow(i,0.7f)*10;
				yy = yy+w;
				parent.fill(140,140,140);
				parent.ellipse(xx,yy,w,w);
				parent.text(""+i,xx-w/2-5, yy+6);
				if (i<=0.51f)
					i-=0.5f;
			}
		 }
		 else{
			 // draw popup variables
			// popup.draw2();
		 }
		
		count++;
		if (count>1000)
			count=1000;
	}
	
	public void doLayout() {
		  if (g == null) 
				return;
	  //calculate forces on each node
	  //calculate spring forces on each node
	  for (int i=0; i<g.getNodes().size(); i++) {
	    Node n = (Node) g.getNodes().get(i);
	    ArrayList edges = (ArrayList)g.getEdgesFrom(n);
	    n.setForce(new Vector3D(0,0,0));
	    for (int j=0; edges != null && j<edges.size(); j++) {
	      Edge e = (Edge)edges.get(j);
	      Vector3D f = e.getForceFrom();
	    	  n.applyForce(f);
		     
	    }
	    
	    edges = (ArrayList)g.getEdgesTo(n);
	    for (int j=0; edges != null && j<edges.size(); j++) {
	      Edge e = (Edge)edges.get(j);
	      Vector3D f = e.getForceTo();
	       n.applyForce(f);
	    }
	    
	    
	    
	    if ( n.position.getY()<30 ||  n.position.getY()>parent.height -30||
	    		 n.position.getX()<0 ||  n.position.getX()>parent.width-30){
	    	Vector3D f = new Vector3D(800-n.position.getX(),375-n.position.getY(),0);
	    	n.applyForce(f.divide(4));
	    }	
	  }
	  
	 
	  
	  //move nodes according to forces
	  for (int i=0; i<g.getNodes().size(); i++) {
		  Node n = (Node)g.getNodes().get(i);
	    if (n != g.getDragNode()) {
	      n.setPosition(n.getPosition().add(n.getForce()));
	    }
	  }
	}


	void newKP(int id, int v1,int v2, int m) {
		calcularKtotal();
		float ka;
		if (balls.length > 0)
			ka = k_total / balls.length;
		else
			ka = k_total;
		Ball2[] tempBall = Ball2.append(balls, ka, id, v1,v2,m, parent);
		balls = tempBall;
		balls[0].s =true;
	}

	void ordenarArrays() {
		Ball2[] temp_ocurrencias = new Ball2[balls.length];
		System.arraycopy(balls, 0, temp_ocurrencias, 0, balls.length);
		Ball2 temp;
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

	void calcularKtotal() {
		float high = parent.height - top - down;
		float width = parent.width - left - right;
		if (bubble_plots <= 1) {
			if (high < width)
				k_total = PApplet.PI * PApplet.pow(high / 2, 2f) * 0.8f;
			else
				k_total = PApplet.PI * PApplet.pow(width / 2f, 2f) * 0.8f;
		} else if (bubble_plots > 1 && bubble_plots <= 6)
			k_total = width * high * 0.65f;
		else if (bubble_plots > 6 && bubble_plots <= 20)
			k_total = width * high * 0.7f;
		else if (bubble_plots > 20 && bubble_plots <= 50)
			k_total = width * high * 0.72f;
		else if (bubble_plots > 50 && bubble_plots <= 200)
			k_total = width * high * 0.82f;
		else if (bubble_plots > 200)
			k_total = width * high * 0.90f;

	}

	public void mouseMoved() {
		  if (g!=null && g.getDragNode() == null) {
		    g.setHoverNode(null);
		    for(int i=0; i<g.getNodes().size(); i++) {
		    	Node n = (Node)g.getNodes().get(i);
		      if (n.containsPoint(parent.mouseX, parent.mouseY)) {
		        g.setHoverNode(n);
		      }
		    }
		  }
	}
	public void mouseDragged() {
		  if (g!=null && g.getDragNode() != null) {
		    g.getDragNode().setPosition(new Vector3D(parent.mouseX, parent.mouseY, 0));
		  }
		}
	public void mousePressed() {
		  Ball2.brushing =-1;
			
		if (g!=null){
		  g.setSelectedNode(null);
		  g.setDragNode(null);
		  for(int i=0; i<g.getNodes().size(); i++) {
			  Node n = (Node)g.getNodes().get(i);
		    if (n.containsPoint(parent.mouseX, parent.mouseY)) {
		      g.setSelectedNode(n);
		      g.setDragNode(n);
		    }
		  }
		}
	}	 
	public void mouseReleased() {
		 if (g!=null)
		  g.setDragNode(null);
		}
	
	public void keyPressed() {
		if (parent.key == '+') { // viendo mas burbujas
			maxBalls++;
		}
		if (parent.key == '-') { // viendo menos burbujas
			if (maxBalls > 2)
				maxBalls--;
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
		if (parent.key == 'o') { // shaking
			show_scag = !show_scag;
		}
		if (parent.key == 'u') { // shaking
			show_background = !show_background;
		}
		if (parent.key == 'd' || parent.key == 'D') { // redistribuyendo
			for (int i = 0; i < balls.length; i++) {
				balls[i].x = parent.random(balls[i].r + left, parent.width - right
						- balls[i].r);
				balls[i].y = parent.random(balls[i].r + top, parent.height - down
						- balls[i].r);
			}
		}
		if (parent.key == 'g') { // shaking
			show_graph = !show_graph;
			if (show_graph){
				g = buildGraph();
			}
			else
				g=null;
		}
	}

}
