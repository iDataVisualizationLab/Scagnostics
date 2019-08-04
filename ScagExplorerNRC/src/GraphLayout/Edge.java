package GraphLayout;

import java.awt.Color;
import java.text.DecimalFormat;

import processing.core.PApplet;

//Copyright 2005 Sean McCullough
//banksean at yahoo

public class Edge {
	public float k=0.1f; //stiffness
	public float length=1; //natural length.  ehmm uh, huh huh stiffness. natural length ;-)
	public float thickness=1; 
	public float mono=0; 
	public Color color = Color.WHITE;
	DecimalFormat df = new DecimalFormat("#.##");
	
	Node to;
	Node from;
	Graph g;
	PApplet parent;

	public Edge(Node t, Node f, PApplet papa) {
		parent = papa;
		to = t;
		from = f;
	}
	  
	public void setGraph(Graph h) {
		g = h;
	}

	public Node getTo() {
		return to;
	}

	public Node getFrom() {
		return from;
	}

	public void setTo(Node n) {
		to = n;
	}

	public void setFrom(Node n) {
		from = n;
	}

	public float dX() {
		return to.getX() - from.getX();
	}

	public float dY() {
		return to.getY() - from.getY();
	}

	public Vector3D getForceTo() {
	    float dx = dX();
	    float dy = dY();
	    float l = PApplet.sqrt(dx*dx + dy*dy);
	    float f = k*(l-length)*1f;
	    
	    return new Vector3D(-f*dx/l, -f*dy/l, 0);
	  }
	    
	  public Vector3D getForceFrom() {
	    float dx = dX();
	    float dy = dY();
	    float l = PApplet.sqrt(dx*dx + dy*dy);
	    float f = k*(l-length)*1f;
	    
	    return new Vector3D(f*dx/l, f*dy/l, 0);
	  }

	  public void draw() {
		if (parent!=null){
			parent.strokeWeight(thickness);
	    	if (g.getHoverNode() ==null){
		    	parent.stroke(color.getRed(),color.getGreen(),color.getBlue(),200);
	    		parent.line(from.getX(), from.getY(), to.getX(), to.getY());
	    	}
	    	else if (g.getHoverNode().equals(from) ||
	    			g.getHoverNode().equals(to)){ 
	    		parent.stroke(color.getRGB());
		        parent.line(from.getX(), from.getY(), to.getX(), to.getY());
		        
		        parent.fill(0,0,0,125);
		        parent.noStroke();
		        float xx =(from.getX()+to.getX())/2;
		        float yy =(from.getY()+to.getY())/2;
		        String tt = df.format(mono);
		        float ww = parent.textWidth(tt);
				        
		        parent.ellipse(xx, yy-4, ww+2, 16);
		        parent.fill(color.brighter().getRGB());
		        parent.textSize(12);
		        parent.textAlign(PApplet.CENTER);
		        parent.text(tt,xx,yy);
		        
			    from.isConnected =true;
			    to.isConnected = true;
	    	}
	    	else{
	    	 	parent.stroke(color.getRed(),color.getGreen(),color.getBlue(),40);
		    	parent.line(from.getX(), from.getY(), to.getX(), to.getY());
	     	}
	 	}
	  }
}
