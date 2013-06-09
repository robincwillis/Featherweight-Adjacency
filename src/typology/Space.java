package typology;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;
import elements.*;
import run.*;
import database.*;

public class Space {

	PApplet p;

	ArrayList<Space> neighbors;
	public String key, type, activity;
	public int color, lLevel, uLevel;
	public float area, volume;
	public float rating;
	public Point pos;
	public Dimension dim;
	public boolean fixed;
	public boolean rotated;
	public boolean done;
	
	public Space(PApplet parent){
		p = parent;		
		pos = new Point();
		dim = new Dimension();
		key = "newSpace";
		color = p.color(255);
		rating = 0;
		fixed = false;
		rotated = false;
		done = false;
	}
	
	public Space(PApplet parent,String k,float x, float y, float z, float w, float l, float h){
		p = parent;
		pos = new Point(x,y,z);
		dim = new Dimension(w,l,h);
		this.key = k;
		this.setArea();
		this.setVolume();
	}
	
	public void setActivity(String s){
		activity = s;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setDim(float w, float l, float h){
		setWidth(w);
		setLength(l);
		setHeight(h);
	}
	
	public void setWidth(float w){	
	 	this.dim.w = w;
	}
	public void setLength(float l){	
		this.dim.l = l;
	}
	public void setHeight(float h){	
		this.dim.h = h;
	}
	
	public void setType(String s){
		type = s;
	}
	
	public void setLevels(int lower, int upper){
		lLevel = lower;
		uLevel = upper;
	}
	
	public void setRating(float newRating){
		rating = newRating;
	}
	
	public void setPos(float x, float y, float z){
		setXPos(x);
		setYPos(y);
		setZPos(z);
	}
	
//	public void setPosByCell(SiteCell cell){
//		
//		setXPos(cell.pos.a.x-cell.dim.w/2);
//		setYPos(cell.pos.a.y-cell.dim.w/2);
//		setZPos(0);
//		
//	}
	
	public void fixSpace(){
		this.fixed = true;
	}
	
	public void unfixSpace(){
		this.fixed = false;
	}
	
	public void setPosByIndex(int i, int j, float cellSize){
		setXPos((i*cellSize)-(cellSize/2));
		setYPos((j*cellSize)-(cellSize/2));
		setZPos(0);
	}
	
	public int getIndexXPos(float cellSize){
		return (int) ((int)(this.pos.a.x+cellSize/2)/cellSize);
	}
	
	public int getIndexYPos(float cellSize){
		return (int) ((int)(this.pos.a.y+cellSize/2)/cellSize);
		
	}
	
	public Point getCentroid(){
		Point centroid = new Point();
		centroid.a.x = this.pos.a.x+(this.dim.w/2); 
		centroid.a.y = this.pos.a.x+(this.dim.l/2);
		//PVector sumOfAllVerts = new PVector(0f, 0f, 0f);
		return centroid;
	}
	
	public void rotateSpace(){
		float width = this.dim.w;
		float length = this.dim.l;
		
		this.dim.w = length;
		this.dim.l = width;
		if(rotated){
			rotated = false;
		}else if(!rotated){
			rotated = true;
		}
	}
	
//	public SiteCell[][] getCellsWithin(Site site){
//		
//
//		int cellXCount = this.cellWidth(site.cellSize);
//		int cellYCount = this.cellLength(site.cellSize);
//		
//		int cellXPos = getIndexXPos(site.cellSize);
//		int cellYPos = getIndexYPos(site.cellSize);
//
//		SiteCell[][] containedCells = new SiteCell[cellXCount][cellYCount];
//
//			for(int i = cellXPos;i<cellXPos+cellXCount;i++){
//				for(int j=cellYPos;j<cellYPos+cellYCount;j++){
//					//p.println(i+","+j);
//						//p.println();
//						containedCells[i-cellXPos][j-cellYPos] = site.getCell(i, j);
//						//p.println(site.getCell(i, j).full);
//				}
//	
//		}
//
//		return containedCells; 
//	}
	
	public void setXPos(float x){
		this.pos.a.x = x;
	}
	public void setYPos(float y){
		this.pos.a.y = y;
	}
	public void setZPos(float z){
		this.pos.a.z = z;
	}
	
	public void setKey(String k){
		this.key = k;
	}
	
	public float getDistance( Space bSpace){
		Point aCentroid = this.getCentroid();
		Point bCentroid = bSpace.getCentroid();
		return aCentroid.distanceTo(bCentroid);
	}
	
	
	public int cellLength(float cellLength){
		int cellCount = (int) (this.dim.l/cellLength);
		return cellCount;
	}
	
	public int cellWidth(float cellWidth){
		int cellCount = (int) (this.dim.w/cellWidth);
		return cellCount;
	}
	
	public void setArea(){
		this.area = this.dim.w * this.dim.l;
	}
	
	public void setVolume(){
		this.volume = this.dim.h * this.dim.w * this.dim.l;
	}
	
	public void draw2dIsolated(float scale, float xPos, float yPos){
		p.pushStyle();	
		p.stroke(0);
		p.strokeWeight(1);
		p.fill(color,240);
		p.rect(xPos, yPos, this.dim.w*scale, this.dim.l*scale);
		p.fill(0);
		p.text(key+":"+rating,xPos, yPos+this.dim.l*scale+15);
		if(done){
			p.fill(0);
			p.rect(xPos+ this.dim.w*scale+10,yPos,this.dim.w*scale, this.dim.l*scale);	
		}
		p.popStyle();
	}
	
	public void draw2d(float scale){
		p.pushStyle();
		p.stroke(0);
		p.strokeWeight(1);
		//p.rectMode(p.CENTER);
		p.fill(color,100);
		p.rect(this.pos.a.x*scale, this.pos.a.y*scale, this.dim.w*scale, this.dim.l*scale);

		p.popStyle();
	}
	
	public void draw3d(float scale){
		
		p.pushMatrix();
		p.translate((this.pos.a.x+(this.dim.w/2))*scale,(this.pos.a.y+(this.dim.l/2))*scale, (this.dim.h/2)*scale);
		p.pushStyle();
		p.stroke(0);
		p.strokeWeight(1);
		//p.rectMode(p.CENTER);
		p.fill(color,200);
		
		p.box(this.dim.w*scale, this.dim.l*scale, this.dim.h*scale);
	
		p.popStyle();
		p.popMatrix();
	}
	
}
