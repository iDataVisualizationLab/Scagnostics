package main;
import java.awt.Color;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PFont;

public class Main extends PApplet {
	int count = 0;
	public static String[] scagNames = {"Outlying","Skewed","Clumpy","Sparse",
			"Striated","Convex","Skinny","Stringy","Monotonic"}; 
	public static String[] varlist;
	
	public static int numS =127;
	public static int numV =33;
	public static int numP = numV*(numV-1)/2;
	public static int numBalls = numP;// numBalls in BBP
	public static String[] pointlist = new String[numS];
	
	public static float[][] scagVals = new float[9][numP] ;
	public static float[][] data = new float[numV][numS] ;
	public PFont metaBold = loadFont("Arial-BoldMT-18.vlw");
	
	public PopupOption option= new PopupOption(this);
	
	public static int[] bIndex = new int[2];
	public static float[] pX = new float[numV+1];
	public static float[] pY = new float[numV+1];
	public static float[] pW = new float[numV+1];
	public static float[] pH = new float[numV+1];
	public static boolean[] isTextBr = new boolean[numV];
	public static boolean[] isTextSe = new boolean[numV];
	
	//public static boolean stop =false; //Stop Lensing for timeseries
	public static int plotW2 =135;
	public static int plotH2 =90;
	public static int plotH3 =40;  // Small time series
	public static float plotX2 =0.1f;
	public static int plotY2 =150;
	public static int gap2 =2;
	public static int gapS =2;
	public static float stepX2 =3;
	public static int nDLense =4;
	public CheckBoxImposed c1 = new CheckBoxImposed(this, 5, 10,"Search by selected plot",-1,0);
	public CheckBoxImposed c2 = new CheckBoxImposed(this, 5, 32,"Search by scagnostics",1,1);
	public CheckBoxImposed c3 = new CheckBoxImposed(this, 5, 54,"Search by scagnostics",-1,2);
	public static int search=1;
	
	public static  int c1V;
	public static float sliderY1 =0;
	public static float sliderY2 =0;
	public Slider4 slider4 = new Slider4(this);
	
	public static int screenW =1280;
	public static int l =13;
	public static Integrator iX2 = new Integrator();
	public static int nLY =12;
	
	public static int bO =-1;
	public static int bD =-1;
	public static int bS =-1;
	public static boolean found =false;
	
	// Search
	public static float x1 =20;
	public static float y1 =plotY2-2;
	public static float gx1 =50;
	public static float gy1 =230;
	
	public static float h1 =520;
	public static float w1 = 1240;
	public static Integrator iW1 = new Integrator(0);
	public static Integrator iW4 = new Integrator(0);
	public static int bP1 =-1;
	public static boolean found1 =false;
	public static int nP1 = 6;
	public static float sP1 = 153;
	public static Integrator[] iP1 = new Integrator[nP1];
	public static Integrator[][] iC1 = new Integrator[nP1][3];
	public static Integrator[][] iPX1 = new Integrator[nP1][numS];
	public static Integrator[][] iPY1 = new Integrator[nP1][numS];
	public static Integrator[][] iPS1 = new Integrator[nP1][9];
	public static int n = 300;
	public static int maxDis =0;
	public static int maxDisCenter = 0;
	public static int[] disArray= new int[0];// For seach entire space 
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer>[] disArray1 = new ArrayList[n];
	public static int sgap1 = 5;
	
	public static int bP2 =-1;
	
	//Used for Buble plot
	public static int bO12 =-1;
	public static int bP12 =-1;
	public static int bM12 =-1;
	
	public static boolean found2 =false;
	public static int bO3 =-1;
	public static int bP3 =-1;
	public static int bM3 =-1;
	public static boolean found3 =false;
	public static boolean mClicked= false;
	
	public static Color color1= new Color(255,0,255);
	public static Color color2= new Color(0,255,255);
	public static Color[] color12= {new Color(255,0,255,50),new Color(0,255,255,50)};
	public static Integrator ySc = new Integrator(50);


	public static int MAX_PAIR= 9;
	
	public static Integrator[] iPX3 = new Integrator[MAX_PAIR];
	public static Integrator[] iPY3 = new Integrator[MAX_PAIR];
	public static Integrator iX3 = new Integrator(plotW2);
	public static Integrator iY3 = new Integrator(plotY2);
	
	public static Integrator[] iX4 = new Integrator[BBP1.MAX_BALL_SHOWING*2];
	public static Integrator[] iY4 = new Integrator[BBP1.MAX_BALL_SHOWING*2];
	
	public static BBP1 bbp1;
	public static BBP2 bbp2;
	public static BBP3 bbp3;
	
	public HashMap<String,Integer> hBBP = new HashMap<String,Integer>(); 
	public HashMap<String,Integer> hBBPDis = new HashMap<String,Integer>(); 
	public static ArrayList<String> keyBBP = new ArrayList<String>();
	public static ArrayList<String> keyBBPDis = new ArrayList<String>();
	public static Color[] scagColors = new Color[9];
	
	float[] scagLow = new float[9];
	float[] scagHigh = new float[9];
	
	public static Integrator[] iSL = new Integrator[9];
	public static Integrator[] iSH = new Integrator[9];
	
	public static int sPie = -1;
	public static int sSec = -1;
	public static int bPie = -1;
	public static int bSec = -1;
	boolean isActive = false;
	public static boolean change =true;
	public int kActive=0;
	public int kSatisfied=0;
	public int numPoints = 50;
	@SuppressWarnings("unchecked")
	public ArrayList<Integer>[][] satCount= new ArrayList[9][numPoints];
	
	
	// SPLOM data
	public static int sS = 0;
	public static int plotW =20;
	public static Integrator[] iW = new Integrator[numP];
	public static int plotH =20;
	public static Integrator[] iH = new Integrator[numP];
	public static float plotX =10;
	public static Integrator iPlotX = new Integrator(plotX);
	public static int plotY =90;
	public static Integrator iPlotY = new Integrator(plotY);
	public static PlotSPLOM[] plots = new PlotSPLOM[numP];
	//public static Plot[] plot2 = new Plot[numP];
	public PlotText[] plotText = new PlotText[numV];
	public static float dif =1.3f;
	public PopupMenu keyFunction = new PopupMenu(this);
	
	// Bar Chart
	public static float wBar = 300;
	public static float xBar = 100;
	public static float yBar = 80;
	public static float hGraph = 75;
	public static float hBar = 10;
	public static Polyline[] plines = new Polyline[numP];
	public static String[] schools = new String[numS] ;
	public static float[][] diss = new float[numP][numP];
	public static float dissMax = 0;
	
	
	public static void main(String args[]){
	  PApplet.main(new String[] { Main.class.getName() });
    }
	
	public void setup() {
		size(1280, 750);
		background(Color.WHITE.getRGB());
		stroke(255);
		frameRate(12);
		curveTightness(1.f); 
		
		readData();
		readScags();
		schools = loadStrings("../SchoolName.txt");
		for (int p1 =0; p1<numP;p1++){
			for (int p2 =0; p2<numP;p2++){
				float sum =0;
				for (int sc =0; sc<9;sc++){
					float dif = scagVals[sc][p1]-scagVals[sc][p2];
					sum += dif*dif;
				}
				diss[p1][p2] = sum;
				if (sum>dissMax)
					dissMax = sum;
			}	
		}
		
		for (int p =0; p<numP;p++){
			iW[p] = new Integrator(plotW);
			iH[p] = new Integrator(plotH);
			plines[p] = new Polyline(this,p);
		}
		for (int p =0; p<numP;p++){
			plots[p] = new PlotSPLOM(p, this);
		}
		for (int v =0; v<numV; v++){
			plotText[v] = new PlotText(v, this);
		}
		iX2 = new Integrator(stepX2);
		for (int p =0; p<nP1;p++){
			iP1[p] = new Integrator(0f);
			iC1[p][0] = new Integrator(0f,0.5f,0.1f);
			iC1[p][1] = new Integrator(0f,0.5f,0.1f);
			iC1[p][2] = new Integrator(0f,0.5f,0.1f);
			for (int sc =0; sc<9;sc++){
				iPS1[p][sc] = new Integrator(0f,0.5f,0.1f);;
			}
				
		}	
		for (int p =0; p<nP1;p++)
			for (int r =0; r<numS;r++){
				iPX1[p][r] = new Integrator(x1+gx1,0.5f,0.1f);
				iPY1[p][r] = new Integrator(y1+gy1,0.5f,0.1f);	
			}	
			
		for (int i =0; i<BBP1.MAX_BALL_SHOWING*2;i++){
			iX4[i] = new Integrator(slider4.x);
			iY4[i] = new Integrator(slider4.y);
		}
		
		for (int i =0; i<9;i++){
			iSL[i] = new Integrator(0);
			iSH[i] = new Integrator(0);
		}			
		scagColors[0] = new Color(141, 211, 199);
		scagColors[1] = new Color(255, 255, 179);
		scagColors[2] = new Color(190, 186, 218); 
		scagColors[3] = new Color(251, 128, 114); 
		scagColors[4] = new Color(128, 177, 211); 
		scagColors[5] = new Color(252, 205, 229); 
		scagColors[6] = new Color(253, 180, 98);
		scagColors[7] = new Color(179, 222, 105);
		scagColors[8] = new Color(188, 128, 189);
		
		bbp1 = new BBP1(this,keyBBP,keyBBPDis, 700, 10, 10, 200);
		bbp1.setData(hBBP);	
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent evt) {
				mouseWheel(evt);
			}
		});
		
		
		for (int i =0; i<8;i++){
			scagLow[i] = 0f;
			scagHigh[i] =1f;
		}
		scagLow[8] = 0.5f;
		scagHigh[8] =1f;
		
		bbp2 = new BBP2(this,500,80,80,0);
		bbp2.createBalls();
		
		bbp3 = new BBP3(this,400,20,50,20);
		bbp3.createBalls();
	}
	
	public void draw() {
	
	//	this.background(0,0,0);
		this.background(255,255,255);
		ySc.update();
	
		this.smooth();
		
		iPlotX.target(plotX);
		iPlotX.update();
		iPlotY.target(plotY);
		iPlotY.update();
		// Draw button and Option Poupmenu
		textFont(metaBold,12);
		
		textAlign(PApplet.LEFT);
		textFont(metaBold,22);
		option.draw(c1,c2,c3);
		
		
		drawSearchScag();
		if (c1.s>=0){
			drawSPLOM();
			textAlign(PApplet.LEFT);
			keyFunction.draw2();
			if (found1 || found2)
				bbp1.draw();
		}	
		else if (c2.s>=0){
			bbp2.draw();
		}	
		else if (c3.s>=0){
			bbp3.draw();
		}	
		
		drawSimilarPlots();
		
		// Check check
		textAlign(PApplet.LEFT);
		if (c1.bMode >= 0) {
			if (c1.s>=0) {
				c2.s = -1;
				c3.s = -1;
			}
		}
		else if (c2.bMode >= 0) {
			if (c2.s>=0) {
				c1.s = -1;
				c3.s = -1;
			}
		}
		else if (c3.bMode >= 0) {
			if (c3.s>=0) {
				c1.s = -1;
				c2.s = -1;
			}
		}
		
		mClicked= false;
		count++;
		if (count%10==5)
			change=true;
		if (count>1000)
			count=0;
	}	
	
	public void drawSPLOM() {
		for (int p =0; p<numP;p++){
			iW[p].update();
			iH[p].update();
		}
		
		this.textFont(metaBold, 12);
		// Check brushing
		bP1 = -1;
		found1 = false;
		for (int p =0; p<numP;p++){
			plots[p].checkBrushing();
		}
		if (!isBrushing()){
			for (int p =0; p<numP;p++){
				iW[p].target(plotW);
				iH[p].target(plotH);
			}
			for (int p =0; p<numP;p++){
				plots[p].draw();
			}
		}
		else{
			for (int p =0; p<numP;p++){
				plots[p].draw3();
			}
		}
		// Draw selected plots on the right
		
		
		strokeWeight(1f);
		textFont(metaBold,11);
		if (!isBrushing()){
			for (int v =0; v<varlist.length; v++){
				plotText[v].draw();
			}
		}
		else{
			for (int v =0; v<varlist.length; v++){
				plotText[v].draw3();
			}
		}
		drawSPLOMLegend();
	 }
	
	
	// Draw color legend
	public void drawSPLOMLegend() {
		float xL = iPlotX.value+340;
		float yL = 40;
		float wL = 300;
		float hL = 20;

			this.stroke(100,100,100);
			this.strokeWeight(1.2f);
			this.noFill();
			this.rect(xL-2, yL, wL+3, hL);
			this.textAlign(CENTER);
			DecimalFormat df = new DecimalFormat("#.##");
			for (float i=xL;i<=xL+wL;i++){
				float val = (i-xL)/wL;
				double scag = (val); 
				Color color = ColorScales.getColor(scag,"cyan", 1f);
				this.noStroke();
				this.fill(color.getRGB());
				this.rect(i-1, yL+1, 2, hL-1);
				
			}
			this.fill(100,100,100);
			this.stroke(100,100,100);
			for (int i=0;i<=10;i++){
				float xx1  = xL+i*wL/10;
				float yy1  = yL+hL+17;
				
				if (i==0 || i==5 || i==10){
					textFont(metaBold,14);
					this.text(df.format(i/10.f),xx1,yy1);
					if (i==5){
						this.strokeWeight(2f);
						this.line(xx1, yy1-13, xx1, yy1-21);
					}
				}
				else{
					textFont(metaBold,12);
					this.strokeWeight(1.2f);
					this.line(xx1, yy1-14, xx1, yy1-19);
					this.text(df.format(i/10.f),xx1,yy1);	
				}
			}
		
		
		for (int p =0; p<numP;p++){
			if (plots[p].b>=0){
				float xx1 = xL+scagVals[sS][p]*wL;
				
				this.stroke(0,0,0,150);
				this.strokeWeight(5f);
				this.line(xx1, yL, xx1, yL+hL);
			
				this.stroke(255,0,0);
				this.strokeWeight(3f);
				this.line(xx1, yL, xx1, yL+hL);
			
			}
		}
	}	
	
	public boolean isBrushing(){
		for (int p =0; p<numP;p++){
			if (plots[p].b>=0)
				return true;
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public void drawSearchScag() {
		if (search!=1)  return;
		for (int sc = 0; sc < 9; sc++){
			iSL[sc].target(scagLow[sc]);			
			iSH[sc].target(scagHigh[sc]);			
			iSL[sc].update();
			iSH[sc].update();
		}
		
		if (change){
			kActive =0;
			kSatisfied = 0;
			satCount= new ArrayList[9][numPoints];
			for (int sc =0; sc<9;sc++){
				for (int p =0; p<numPoints;p++){
					satCount[sc][p] = new ArrayList<Integer>();
				}
			}
			for (int p =0; p<numP;p++){
					plines[p].isSatisfied = false;
					boolean ok = true;
					for (int sc =0; sc<9;sc++){
						if (scagVals[sc][p]<=iSL[sc].value -0.001|| scagVals[sc][p]>=iSH[sc].value+0.0001)
							ok = false;
					}
					if (ok){
						plines[p].isSatisfied = ok;
						kSatisfied++;
						for (int sc =0; sc<9;sc++){
							int loc = (int) (scagVals[sc][p]*numPoints);
							if (loc==numPoints) loc =numPoints-1;
							satCount[sc][loc].add(p); 
						}	
					}	
					if (kActive<BBP2.maxBalls && ok) {
						BBP2.balls[p].active= true;
						kActive++;
					}
					else{
						if (p<BBP2.balls.length)
							BBP2.balls[p].active= false;
					}
			}
		}
		
		//drawPieChart();
		drawParallelCoordinates();
		
		// Draw summary text
		fill(0,0,0);
		textFont(metaBold,14);
		
		int x41 = 330;
		int x42 = 360;
		int y41 = 20;
		int y42 = 36;
		int y43 = 52;
		this.textAlign(RIGHT);
		text("Showing plots: ",x41,y41);
		text("Satisfied plots: ",x41,y42);
		text("Search space: ",x41,y43);
		text(formatIntegerThousand(kActive),x42,y41);
		text(formatIntegerThousand(kSatisfied),x42,y42);
		text(formatIntegerThousand(numP),x42,y43);
		this.textAlign(LEFT);
	}
	
	
	
	
	
	
	public String formatIntegerThousand(int num) {
		String nStr = ""+num;
		if (num<1000)
			return ""+num;
		int th = num/1000;
		return th+","+nStr.substring(nStr.length()-3,nStr.length());	
	}
		
	
	public void drawPieText(String message, float x, float y, float r, float a, Color color) {
		 // We must keep track of our position along the curve
		  float arclength = 0;
		  // For every box
		  for (int i = 0; i < message.length(); i ++ ) {
		    // The character and its width
		    char currentChar = message.charAt(i);
		    // Instead of a constant width, we check the width of each character.
		    float w = textWidth(currentChar); 
		    // Each box is centered so we move half the width
		    arclength += w/2;
		    
		    // Angle in radians is the arclength divided by the radius
		    // Starting on the left side of the circle by adding PI
		    float theta = a+arclength / r;
		    
		    pushMatrix();
		    
		    // Polar to Cartesian conversion allows us to find the point along the curve. See Chapter 13 for a review of this concept.
		    translate(x+r*cos(theta), y+r*sin(theta)); 
		    // Rotate the box (rotation is offset by 90 degrees)
		    rotate(theta + PI/2); 
		    
		    // Display the character
		    fill(color.getRGB());
		    text(currentChar,0,0);
		    //System.out.println(currentChar+" "+r*cos(theta));
		    popMatrix();
		    
		    // Move halfway again
		    arclength += w/2;
		  }
	}
		
	public void drawPieChart() {
		PFont f = createFont("Courier",40,true);
		textAlign(PApplet.LEFT);
		float lastAng = -PApplet.PI/2;
		strokeWeight(1);
		textFont(BBP1.font, 10+100/20);
		
		float r = 250;
		float x = 175;
		float y = 300;
		if (dist(x,y,mouseX,mouseY)<=r/2)
			isActive = true;
		else
			isActive = false;
			
		float a = PApplet.atan((mouseY-y)/(mouseX-x));
		if (mouseX<x)
			a += PI;
		
		bPie = -1;
		bSec = -1;
		for (int i = 0; i < 9; i++){
			float nextAng = lastAng+PApplet.PI*2/9;
			Color color =scagColors[i];
			boolean isOn = isActive && lastAng<=a && a<nextAng;
			if (isOn){
				//color = color.brighter();
				bPie =i;
				bSec = 2;
				if (dist(x,y,mouseX,mouseY)<=scagHigh[i]*r/2){
					bSec = 1;
				}
				if (dist(x,y,mouseX,mouseY)<=scagLow[i]*r/2){
					bSec = 0;
				}
				stroke(255,255,255);
				strokeWeight(2f);
			}
			else
				noStroke();
			
			// Outsider
			noStroke();
			fill(color.getRed(),color.getGreen(),color.getBlue(),80);
			arc(x,y, r, r, lastAng, nextAng);
						
			DecimalFormat df = new DecimalFormat("#.##");
			// High filter
			if (i==sPie && sSec>=1){
				stroke(255,0,255);
				strokeWeight(2f);
				
				textFont(f,12);
				drawPieText(df.format(iSH[i].value), x,y,iSH[i].value*r/2+3, lastAng, Color.MAGENTA);
				
			}
			else if (isOn && bSec>=1){
				stroke(255,255,255);
				strokeWeight(2f);
				textFont(f,12);
				drawPieText(df.format(iSH[i].value), x,y,iSH[i].value*r/2+3, lastAng, Color.WHITE);
			}
			else
				noStroke();
			fill(color.getRGB());
			arc(x,y, iSH[i].value*r, iSH[i].value*r, lastAng, nextAng);
			
			// Low Filter
			if (i==sPie && sSec<=1){
				stroke(255,0,255);
				strokeWeight(2f);
			}
			else if (isOn && bSec<=1){
				stroke(255,255,255);
				strokeWeight(2f);
			}
			else
				noStroke();
			fill(0,0,0,135);
			arc(x,y, iSL[i].value*r, iSL[i].value*r, lastAng, nextAng);
			
			
			if (i==sPie && sSec<=1){
				textFont(f,12);
				drawPieText(df.format(iSL[i].value), x,y,iSL[i].value*r/2-11, lastAng, Color.MAGENTA);
			}
			else if (isOn && bSec<=1){
				textFont(f,12);
				drawPieText(df.format(iSL[i].value), x,y,iSL[i].value*r/2-11, lastAng, Color.WHITE);
			}
				
			this.textFont(f,15);
			drawPieText(scagNames[i], x,y,r/2+5, lastAng, color.brighter());
			
		    lastAng = nextAng;  
		}
	}
	
	public void drawParallelCoordinates() {
		DecimalFormat df = new DecimalFormat("#.##");
		textAlign(PApplet.RIGHT);
		strokeWeight(1);
		textFont(this.metaBold, 12);
		
		float maxCount = Float.MIN_VALUE;
		float rate = 0.75f;
		for (int sc = 0; sc < 9; sc++){
			for (int l =0; l<numPoints;l++){
				if (PApplet.pow(satCount[sc][l].size(),rate)>maxCount){
					maxCount = PApplet.pow(satCount[sc][l].size(),rate);
				}
			}
		}	
		
		// Draw parallel coordinates
		for (int p =0; p<numP;p++){
			plines[p].draw();
		}
		
		bPie = -1;
		bSec = -1;
		boolean isBrushed = false;
		for (int sc = 0; sc < 9; sc++){
			Color color =scagColors[sc];
			float yy = yBar+sc*hGraph ;
			
			// Draw scagnostics names
			textFont(this.metaBold, 16);
			this.fill(color.darker().getRGB());
			this.textAlign(PApplet.RIGHT);
			text(scagNames[sc], xBar-10,yy+5);
			
			if (kSatisfied> BBP2.maxBalls){
				// Draw chart on top of bar
				for (int i=-1;i<2;i=i+2){
					beginShape();
					float xG = xBar;
					float yG = yy;
					curveVertex(xG, yG);
					curveVertex(xG, yG);
					for (int l =0; l<numPoints;l++){
						xG = xBar + l*wBar/numPoints;
						yG = yy + i*hGraph*.75f *(PApplet.pow(satCount[sc][l].size(),rate)/maxCount);
						curveVertex(xG, yG);
					}
					yG = yy;
					curveVertex(xG+6, yG);
					curveVertex(xG+6, yG);
					stroke(60, 60, 60, 250);
					strokeWeight(1);
					fill(color.getRGB());
					endShape();
				}
			
			}
			else{
				strokeWeight(1f);
				this.stroke(0,0,0);
				this.line(xBar-3, yy, xBar+wBar, yy);
				
				stroke(new Color(0, 0, 0).getRGB());
				strokeWeight(0.5f);
				for (int l =0; l<numPoints;l++){
					float step = wBar/numPoints;
					for (int i=0; i<satCount[sc][l].size();i++){
						float xG = xBar + l*step+step/2;
						float yG = yy+(satCount[sc][l].size()*step/2f) - i*step;
						
						if (PApplet.dist(xG, yG, mouseX, mouseY)<step){
							if (!isBrushed)
								Polyline.brushing =satCount[sc][l].get(i);
							isBrushed = true;
						}
						
						fill(color.getRGB());
						if (satCount[sc][l].get(i)==Polyline.brushing){
							fill(Color.MAGENTA.getRGB());
							this.ellipse(xG, yG-step/2, step+1, step+1);
						}
						else{
							this.ellipse(xG, yG-step/2, step, step);
						}
					}
				}
			}
					
			//Check Brushing
			boolean isOn = false;
			if (xBar<mouseX && mouseX<xBar+wBar && yy-hGraph/2 <mouseY && mouseY<yy+hGraph/2)
				isOn = true;
			if (isOn){
				bPie =sc;
				bSec = 2;
				if (xBar<mouseX && mouseX<xBar+scagHigh[sc]*wBar){
					bSec = 1;
				}
				if (xBar<mouseX && mouseX<xBar+scagLow[sc]*wBar){
					bSec = 0;
				}
				stroke(255,255,255);
				strokeWeight(2f);
			}
			else
				noStroke();
			
			
			// If not brushing a point/polyline
			if (Polyline.brushing<0){
				
				// Draw Text for High filter
				fill(color.getRGB());
				noStroke();
				//rect(xBar,yy, iSH[sc].value*wBar, hBar);
				float xxH = xBar+iSH[sc].value*wBar;
				if (sc==sPie && sSec>=1){
					stroke(255,0,255);
					strokeWeight(2f);
					this.fill(Color.MAGENTA.getRGB());
					textFont(this.metaBold, 12);
					this.textAlign(PApplet.LEFT);
					float v = iSH[sc].value;
					if (v<0) v=0;
					this.text(df.format(v), xxH+3,yy+17);
					stroke(0,0,0);
				}
				else if (isOn && bSec>=1){
					stroke(255,255,255);
					strokeWeight(2f);
					this.fill(0,0,0);
					textFont(this.metaBold, 12);
					this.textAlign(PApplet.LEFT);
					float v = iSH[sc].value;
					if (v<0) v=0;
					this.text(df.format(v), xxH+3,yy+17);
					stroke(0,0,0);
				}
				else
					noStroke();
				line(xxH,yy-hBar/2, xxH, yy+hBar/2);
				
				
				// Low Filter
				fill(0,0,0,135);
				noStroke();
				//rect(xBar,yy, iSL[sc].value*wBar, hBar);
				float xxL = xBar+iSL[sc].value*wBar;
				if (sc==sPie && sSec<=1){
					stroke(255,0,255);
					strokeWeight(2f);
					this.fill(Color.MAGENTA.getRGB());
					textFont(this.metaBold, 12);
					this.textAlign(PApplet.RIGHT);
					float v = iSL[sc].value;
					if (v<0) v=0;
					this.text(df.format(v), xxL-3,yy+17);
					stroke(0,0,0);
				}
				else if (isOn && bSec<=1){
					stroke(255,255,255);
					strokeWeight(2f);
					this.fill(0,0,0);
					textFont(this.metaBold, 12);
					this.textAlign(PApplet.RIGHT);
					float v = iSL[sc].value;
					if (v<0) v=0;
					this.text(df.format(v), xxL-3,yy+17);
					stroke(0,0,0);
				}
				else
					noStroke();
				line(xxL-4,yy-hBar/2, xxL-4, yy+hBar/2);
			}
		}	
		if (!isBrushed)
			Polyline.brushing = -1;
	}
	
	public void drawSimilarPlots() {
		if (search!=0)  return;
			
		textFont(metaBold,12);
		slider4.draw();
		
		int pp = bP2;
		if (found1){
			pp = bP1;
		}
			
		found3=false;
		bO3 = -1;
		bP3 = -1;
		bM3 = -1;
		
		if (found1 || found2){
			//System.out.println("SSSSSSSSSS"+found1);
			// Compute disimilarity
			for (int i =0; i<n;i++){
				disArray1[i] = new ArrayList<Integer>();
			}
			
			disArray = new int[numP];
			
			float[] sum = new float[numP];
			maxDis=0;
			for (int p =0; p<numP;p++){
				for (int sc =0; sc<9;sc++){
					float dif = scagVals[sc][p]-scagVals[sc][pp];
					sum[p] += dif*dif*dif*dif;
				}
				sum[p] = (float) Math.pow(sum[p],0.25f);
				disArray[p] =  (int) (sum[p]*100);
				disArray1[disArray[p]].add(p); 
				// compute max dissimilarity
				if (disArray[p]>maxDis)
					maxDis = disArray[p];
			}
			
			// Draw BBP
			if (search==0 && pp!=bP12){
				bP12 = pp;
				hBBP = new HashMap<String,Integer>(); 
				hBBPDis = new HashMap<String,Integer>(); 
				keyBBP = new ArrayList<String>();
				keyBBPDis = new ArrayList<String>();
				
				int count=0;
				maxDisCenter =0;
				for (int dis =0; dis<BBP1.maxDisimilarity;dis++){
					for (int p =0; p<numP && count<BBP1.MAX_BALL_SHOWING;p++){
						if (disArray[p]==dis){
							String key = p+"";
							keyBBP.add(new String(key));
							int occur = disArray[p];
							if (occur<0) occur=0;
							hBBP.put(key, new Integer(occur));
							// compute maximu dissimilarity in the center of BBP
							if (dis>maxDisCenter)
								maxDisCenter =dis;
							count++;
						}
					}
				}
				System.out.println("maxDis"+maxDis);
				System.out.println("maxDisCenter"+maxDisCenter);
				
				count=0;
				for (int p =0; p<numP && count<BBP1.MAX_BALL_SHOWING;p++){
					if (disArray[p]>maxDisCenter+30){
						String key = p+"";
						keyBBPDis.add(new String(key));
						int occur = disArray[p];
						if (occur<0) occur=0;
						hBBPDis.put(key, new Integer(occur));
						count++;
					}
				}
				
				
				System.out.println("maxDisCenter"+maxDisCenter);
				
				slider4.updateMaxBalls();
				bbp1 = new BBP1(this,keyBBP,keyBBPDis,260,10,10,200);
				bbp1.setData(hBBP);
				bbp1.setData2(hBBPDis);
			}
		}
		
		
		// Draw text
		int x41 = 1240;
		int x42 = 1270;
		int y41 = height - BBP1.down+BBP1.top+20;
		int y42 = height - BBP1.down+BBP1.top+40;
		int y43 = height - BBP1.down+BBP1.top+60;
		
		textAlign(PApplet.LEFT);
		textFont(metaBold,14);
		fill(255,255,255);
		this.textAlign(RIGHT);
		text("Showing plots: ",x41,y41);
		text("Satisfied plots: ",x41,y42);
		text("Search space: ",x41,y43);
		text(formatIntegerThousand(BBP1.balls.length),x42,y41);
		text(formatIntegerThousand(BBP1.sasBalls),x42,y42);
		text(formatIntegerThousand(numP),x42,y43);
		this.textAlign(LEFT);
		
		
		// Draw Dissimilarity above slider
		fill(255,255,255);
		textFont(metaBold,15);
		this.text("Dissimilarity",slider4.x-100,slider4.y+30);
		
		float xx = slider4.x;
		float yy = slider4.y;
		float g1 = sgap1;
		for (int i = 0; i < BBP1.maxDisimilarity; i++) {
			xx = slider4.x+ i*g1;
			yy = slider4.y;
			float val = ((float) BBP1.maxDisimilarity - i)/BBP1.maxDisimilarity;
			Color color = ColorScales.getColor(val, "temperature", 1f);
			fill(color.getRGB());
			rect(xx,yy,g1+1,g1*2);
		}
		
		
		// Draw scatterplot above slider
		if (found1 || found2){
			float g2 = sgap1;
			int[] numDotInStack =new int[200];
			for (int i = 0; i < keyBBP.size()+keyBBPDis.size(); i++) {
				String key  ;
				int dis ;
				if (i<keyBBP.size()){
					 key = keyBBP.get(i);
					 dis = hBBP.get(key);
				}
				else{
					 key = keyBBPDis.get(i-keyBBP.size());
					 dis =  hBBPDis.get(key);
				}
				
					
				
				numDotInStack[dis]++;
				
				yy = slider4.y - g2*(numDotInStack[dis]+1)+9;
				xx = slider4.x+  dis*g2 + g2/2; 
				
				float val = ((float) BBP1.maxDisimilarity - dis)/BBP1.maxDisimilarity;
				if (val<0) val=0;
				else if (val>1) val=1;
				Color color = ColorScales.getColor(val, "temperature", 1f);
				fill(color.getRGB());
					
					
				iX4[i].target(xx);
				iY4[i].target(yy);
				if (dis*(sgap1)>slider4.u && i<keyBBP.size()){
					iY4[i].target(slider4.y+50);
				}		
				iX4[i].update();
				iY4[i].update();
				float x33 = iX4[i].value;
				float y33 = iY4[i].value;
				if (i==0){
					stroke(255,255,0);
					strokeWeight(1f);
					this.ellipse(x33,y33-2,g2,g2);
					noStroke();
				}
				else
					this.ellipse(x33,y33-2,g2,g2);
			}
		}
	}
	
	
	
	public void readScags() {
			String[] lines = loadStrings("../Output.txt");
			for (int p = 1; p < lines.length; p++) {
				String[] pieces = lines[p].split(" ");
				for (int s = 0; s < pieces.length; s++) {
					scagVals[s][p-1] = Float.parseFloat(pieces[s]);
					if (scagVals[s][p-1]>1) 
						scagVals[s][p-1]=1;
					if (scagVals[s][p-1]<0) 
						scagVals[s][p-1]=0;
					
				}
			}	
		
	}
	
	public void readData() {
		String[] lines = loadStrings("../NRCStandardized.txt");
		varlist = lines[0].split("\t");
		System.out.println("Number of variables:"+varlist.length);
		
		for (int j = 1; j < lines.length; j++) {
			String[] pieces = lines[j].split("\t");
			pointlist[j-1] = pieces[0];
			for (int i = 0; i < pieces.length; i++) {
				String tmp = pieces[i];
				data[i][j-1] = Float.parseFloat(tmp);
			}
		}	
	}
	
	
	public void keyPressed() {
		if (c3.s>=0)
			bbp3.keyPressed();
	}
	
	public void mouseMoved() {
		if (c2.s>=0)
			bbp2.mouseMoved();
		else if (c3.s>=0)
			bbp3.mouseMoved();
	}
	
	public void mousePressed() {
		if (c2.s>=0)
			bbp2.mousePressed();
		else if (c3.s>=0)
			bbp3.mousePressed();
		slider4.checkSelectedSlider1();
	}
	public void mouseReleased() {
		if (c2.s>=0)
			bbp2.mouseReleased();
		else if (c3.s>=0)
			bbp3.mouseReleased();
		slider4.checkSelectedSlider2();
	}
	
	public void mouseDragged() {
		if (c2.s>=0)
			bbp2.mouseDragged();
		if (c3.s>=0)
			bbp3.mouseDragged();
	}
	public void mouseClicked() {
		mClicked= true;
		if (option.b>=0){
			c1.checkSelected();
			c2.checkSelected();
			c3.checkSelected();
		}
		// Check brushing
		if (c1.s>=0){
			bbp1.mouseClicked();
		}
		else if (c3.s>=0)
			bbp3.mouseClicked();
		
		
			
		// Pie chart is active ...... SEARCH 4
		sPie = bPie;
		sSec = bSec;
	}
	
	public void mouseWheel(MouseWheelEvent e) {
		int delta = e.getWheelRotation();
		if (c2.s>=0){
			//System.out.println(c5.s);
			if (sPie>=0 && sSec<=1){
				float delta2 = delta/100.f;
				scagLow[sPie]+=delta2;
				if (scagLow[sPie]<0)
					scagLow[sPie] =0;
				else if (scagLow[sPie]>scagHigh[sPie])
					scagLow[sPie] =scagHigh[sPie];
			}
			if (sPie>=0 && sSec>=1){
				float delta2 = delta/100.f;
				scagHigh[sPie]+=delta2;
				if (scagHigh[sPie]>1)
					scagHigh[sPie] =1;
				else if (scagHigh[sPie]<scagLow[sPie])
					scagHigh[sPie] =scagLow[sPie];
			}
				
		}
		else if (c3.s>=0){
			if (key=='3'){
				BBP3.x -=delta*BBP3.scale;
			}
			else {
				BBP3.y -=delta*BBP3.scale;
			}
			 
		}

			
		else{
			if (keyPressed){
				System.out.println("a"+key+"b");
				plotY -=delta;
				
			}
			else{
				plotX -=delta;
				
			}
		}
	}
}