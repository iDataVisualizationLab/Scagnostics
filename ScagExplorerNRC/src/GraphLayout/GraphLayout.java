package GraphLayout;

import java.util.ArrayList;

import processing.core.PApplet;

public class GraphLayout extends PApplet {
	int H = 800;
	int W = 800;

	Graph g;
	float scaleFactor = 1;

	public void setup() {
	  size(800,800);
	  frameRate(20);
	  smooth();
	  g = buildRandomGraph();
	}

	public void draw() {
	  background(0);
	  if (g != null) {
	    doLayout();
	    g.draw();  
	  }
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
	    
	  }
	  
	  //calculate the anti-gravitational forces on each node
	  //this is the N^2 shittiness that needs to be optimized
	  //TODO: at least make it N^2/2 since forces are symmetrical
	  for (int i=0; i<g.getNodes().size(); i++) {
		  Node a = (Node)g.getNodes().get(i);
	    for (int j=0; j<g.getNodes().size(); j++) {
	    	Node b = (Node)g.getNodes().get(j);
	      if (b != a) {
	        float dx = b.getX() - a.getX();
	        float dy = b.getY() - a.getY();
	        float r = sqrt(dx*dx + dy*dy);
	        //F = G*m1*m2/r^2  
	        
	        if (r != 0) { //don't divide by zero.
	          float f = 100*(a.getMass()*b.getMass()/(r*r));
	          Vector3D vf = new Vector3D(-dx*f, -dy*f, 0);
	          a.applyForce(vf);
	        }              
	      }
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

	public void keyPressed() {
	  if (key == ' ') {
	    g = buildRandomGraph();
	  } else if (key == '[') {
	    scaleFactor += 0.1;
	  } else if (key == ']') {
	    scaleFactor -= 0.1;
	  }
	}

	public void mousePressed() {
	  g.setSelectedNode(null);
	  g.setDragNode(null);
	  for(int i=0; i<g.getNodes().size(); i++) {
		  Node n = (Node)g.getNodes().get(i);
	    if (n.containsPoint(mouseX/scaleFactor, mouseY/scaleFactor)) {
	      g.setSelectedNode(n);
	      g.setDragNode(n);
	    }
	  }
	}

	public void mouseMoved() {
	  if (g.getDragNode() == null) {
	    g.setHoverNode(null);
	    for(int i=0; i<g.getNodes().size(); i++) {
	    	Node n = (Node)g.getNodes().get(i);
	      if (n.containsPoint(mouseX/scaleFactor, mouseY/scaleFactor)) {
	        g.setHoverNode(n);
	      }
	    }
	  }
	}

	public void mouseReleased() {
	  g.setDragNode(null);
	}

	public void mouseDragged() {
	  if (g.getDragNode() != null) {
	    g.getDragNode().setPosition(new Vector3D(mouseX/scaleFactor, mouseY/scaleFactor, 0));
	  }
	}

	public Graph buildRandomGraph() {
				
	  Graph g;
	  int nNodes = 35;
	  int nEdges = 200;
	  g = new Graph();
	  
	  for (int i=0; i<nNodes; i++) {
		  Node n = new Node(new Vector3D(W/4 + random(W/2), H/4 + random(H/2), 20000), this, i,"S1");
	    n.setMass((float) (1.0 + random(3)));
	    g.addNode(n);
	  }
	  
	  for (int i=0; i<nEdges; i++) {
	    Node a = (Node)g.getNodes().get((int)random(g.getNodes().size()));
	    Node b = (Node)g.getNodes().get((int)random(g.getNodes().size()));
	    if (a != b && !(g.isConnected(a,b))) {
	      Edge e = new Edge(a, b, this);
	      e.length = 100+random(100);
	     g.addEdge(e);
	    }
	  }
	  
	  return g;
	}
	
	
}
