package main;

import static main.Main.*;

import java.awt.Color;

import processing.core.PApplet;

public class PlotText{
	public int v =-1;
	public PApplet parent;
	public float x, y;
	public float w, h;
	public double al= PI/6;
	public int wText =160;
	public int count =0;
	
	
	
	public static int[][] varr = new int[numP][2];
	
	
	public PlotText(int var, PApplet parent_){
		parent = parent_;
		v = var;
		
		for (int i =0 ; i<numP;i++){
			varr[i] = PlotText.pairToIndex(i);
		}
		
	}
		
	public void draw(){
		w = iW[0].value;
		h = iH[0].value;
		x = iPlotX.value + w*v;
		y = iPlotY.value + h*v;
		parent.fill(90,90,90);
		parent.translate(x+8,y-2);
		parent.rotate((float) (-al));
		parent.text(varlist[v],0,0);
		parent.rotate((float) (al));
		parent.translate(-(x+8),-(y-2));
	}
	
	
	public void draw3(){
		w= pW[v];
		h= pH[v];
		x = pX[v];
		y = pY[v+1];
		if (v==0)
			h = w/2;
		else if (v==numV-1)
			w = h/2; 
		parent.fill(90,90,90);
		for (int p =0; p<numP;p++){
			if (plots[p].b>=0 && (plots[p].index[0]==v || plots[p].index[1]==v)){
				parent.fill(255,0,0);
			}
		}
			
		parent.translate(x+8,y-2);
		parent.rotate((float) (-al));
		parent.text(varlist[v],0,0);
		parent.rotate((float) (al));
		parent.translate(-(x+8),-(y-2));
	}
	


	
	public int transform(int a){
		return (a-10)*6;
	}
		
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
	
	
	
	
	
	
}