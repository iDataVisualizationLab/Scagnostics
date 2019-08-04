package main;

import static main.Main.*;

import java.awt.Color;

import processing.core.PApplet;

public class PlotSPLOM{
	public int pair =-1;
	public PApplet parent;
	public float x, y;
	public float w, h;
	public int b=-1;
	public int[] index;
	public int id;
	
	public static float xS0 =646;
	public static float xS =xS0-1280;
	public static int yS =50;
	public static int wS =44;
	public static int hS =44;
	
	public PlotSPLOM(int pair_, PApplet parent_){
		parent = parent_;
		pair = pair_;
		id= pair;
		index = pairToIndex(pair);
		bIndex[0] =-1;
		bIndex[1] =-1;
	}
		
	public void draw(){
		w = iW[id].value;
		h = iH[id].value;
		float stepX= Main.iPlotX.value;
		float stepY= Main.iPlotY.value; 
		x = stepX+index[1]*w;
		y = stepY+index[0]*h;
		
		
		
		//double scag = (meanYear[sS][pair]-meanYearMin[sS])/(meanYearMax[sS]-meanYearMin[sS]); 
		Color color = ColorScales.getColor(scagVals[sS][pair],"cyan", 1f);
		parent.fill(color.getRGB());
		parent.noStroke();
		if (b>=0 || (found2 && bP2 == pair))
			parent.fill(Color.RED.getRGB());
		parent.rect(x, y-h+1, w-1, h-1);
		
		
		
		parent.fill(0,0,0,80);
		for (int s = 0; s < numS; s=s+4) {
			float x3 = x +w*0.1f + w*0.8f*data[index[1]][s];
			float y3 = y -h*.1f - h*0.8f*data[index[0]][s];
			parent.ellipse(x3, y3, 1+w/20, 1+h/20);
		}
		
		drawSelected();
	}
	
	public void draw3(){
		// Lensing X-Axis
		w = Main.plotW-dif;
		if (index[1]==0)
			pX[0] = Main.plotX;
		if (index[1]<bIndex[1]){
			double mid = (bIndex[1]-1)/2.;
			int dd = (int) ((index[1]-mid)*dif);
			w += dd; 
		}	
		else if (index[1]==bIndex[1]){
			w = Main.plotW+(numV-2)*dif;
		}	
		else{
			double mid = bIndex[1]+(numV-bIndex[1]-1)/2.;
			int dd = (int) ((mid-index[1])*dif);
			w += dd; 
		}
		pX[index[1]+1]=pX[index[1]] +w; 
		x =pX[index[1]];
		pW[index[1]] = w; 
		
		// Lensing Y-Axis
		h = Main.plotH-dif;
		if (index[0]==1)
			pY[1] = Main.plotY;
		
		if (index[0]<bIndex[0]){
			double mid = (bIndex[0])/2.;
			int dd = (int) ((index[0]-mid)*dif);
			h += dd; 
		}	
		else if (index[0]==bIndex[0]){
			h = Main.plotH+(numV-2)*dif;
		}	
		else{
			double mid = bIndex[0]+(numV-bIndex[0])/2.;
			int dd = (int) ((mid-index[0])*dif);
			h += dd; 
		}
		pY[index[0]+1]=pY[index[0]] +h; 
		y =pY[index[0]]+h;
		pH[index[0]] = h; 
		
		
		//double scag = (meanYear[sS][pair]-meanYearMin[sS])/(meanYearMax[sS]-meanYearMin[sS]); 
		Color color = ColorScales.getColor(scagVals[sS][pair],"cyan", 1f);
		parent.fill(color.getRGB());
		if (b>=0 || (found2 && bP2 == pair))
			parent.fill(Color.RED.getRGB());
		
		parent.noStroke();
		parent.rect(x, y-h+1, w-0.5f, h-0.5f);
		
		
		//Change saturation depend on how far from the brushing plot
		int bIndex = -1;
		for (int p =0; p<numP;p++){
			if (plots[p].b>=0)
				bIndex =p;
		}
		
		if (bIndex<0){
			parent.fill(new Color(0,0,0,150).getRGB());
		}
		else{
			float r1 = index[0]-plots[bIndex].index[0];
			r1= r1*r1;
			float r2 = index[1]-plots[bIndex].index[1];
			r2 =r2*r2;
			
			float dis = PApplet.sqrt(r1+r2);
			float sat = 180-10*dis;
			if (sat<0) sat = 0;
				parent.fill(0,0,0,sat);
				
			if (dis<15){	
				if (b>=0){
					parent.fill(new Color(0,0,0,255).getRGB());
					for (int s = 0; s < numS; s++) {
						float x3 = x +w*0.1f + w*0.8f*data[index[1]][s];
						float y3 = y -h*.1f - h*0.8f*data[index[0]][s];
						parent.ellipse(x3, y3, 1+w/20, 1+h/20);
					}
				}	
				else{
					for (int s = 0; s < numS; s=s+4) {
						float x3 = x +w*0.1f + w*0.8f*data[index[1]][s];
						float y3 = y -h*.1f - h*0.8f*data[index[0]][s];
						parent.ellipse(x3, y3, 1+w/20, 1+h/20);
					}
				}
				
			}	
			
		}
		drawSelected();
	}
	
	public void drawSelected() {
		// Draw Selected
		if (found2 && bP2 == pair){
			parent.noFill();
			parent.strokeWeight(2);
			parent.stroke(Color.YELLOW.getRGB());
			parent.rect(x-1, y-h, w, h);
		}
	}

		
	public void checkBrushing() {
		int mX = parent.mouseX;
		int mY = parent.mouseY;
		if (x<mX && mX<=x+w && y-h<mY && mY<=y){
			b =0;
			bIndex[0] =index[0];
			bIndex[1] =index[1];
			
			bP1 = pair;
			found1 = true;
			if (mClicked){
				if (found2  && pair==bP2){
					bP2 = -1;
					found2 = false;
				}
				else{		
					bP2 = pair;
					found2 = true;
				}
			}
			
			return;
		}	
		b =-1;
	}
	
	
	//
	public static int[] pairToIndex(int p){
		int[] index = new int[2];
		index[0]=-1;
		index[1]=-1;
		int k=0;
		for (int r=1;r<numV;r++){
			for (int c=0;c<r;c++){
				if (k==p){
					index[0]=r;
					index[1]=c;
				}
				k++;
			}
		}
		return index;
	}
	
	public static int indexToPair(int i1,int i2){
		int k=0;
		for (int r=1;r<numV;r++){
			for (int c=0;c<r;c++){
				if (r== i1 && c==i2){
					return k;
				}
				k++;
			}
		}
		return -1;
	}
	
		
	
	
	
	// Compute order variables based on scagnostics
	public static void orderVar(){
			for (int s=0; s<9;s++){
				//orderVar[s] = order(meanYearVar[s]);
			}
		}
	public static int[] order(double[] a){
		int[] o = new int[a.length];
		double maxV = Double.MAX_VALUE;
		for (int i=0; i<a.length;i++){
			double m= Double.MIN_VALUE;
			int index= -1;
			for (int j=0; j<a.length;j++){
				if (a[j]>m && a[j]<maxV){
					m = a[j];
					index = j;
				}
			}
			o[i] =index;
			maxV = m;
		}
		return o;
	}
	
	
}