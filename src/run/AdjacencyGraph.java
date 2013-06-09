package run;

import processing.core.PApplet;
import elements.*;
import traer.physics.*;
import graph.*;
import java.io.IOException;
import java.util.*;
import database.*;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import com.google.common.collect.BiMap;
import controlP5.*;

public class AdjacencyGraph extends PApplet {
	
	ParticleSystem physics;
	Read matrixReader;
	Read typologyReader;
	TypologyReader readTypology;
	MatrixReader readMatrix;
	graphBuilder graphBuild;
	MatrixWriter writeMatrix;
	Graph adjGraph;
	ControlP5 controlP5;
	GUI gui;
	Particle focusParticle, selectParticle;
	
	float mxPos, myPos;
	String typologyPath, configPath;
	
	//int MODE = 2;
	int MODE = 1;
	
	float tX, tY; 
	float otX, otY;
	float tsX, tsY;
	float ogX, ogY;
	boolean dragging = false;
	boolean record = false;
	public void setup() {
		size(1024,748,OPENGL);
		hint( DISABLE_OPENGL_2X_SMOOTH );
		hint( ENABLE_OPENGL_4X_SMOOTH );
		frameRate( 24 );
		strokeWeight(1);
		ellipseMode( CENTER );    
	
		//typologyPath = "data/SBO/Activities/";
		//configPath = "data/SBO/";
		typologyPath = "data/DTC/Activities/";
		configPath = "data/DTC/";
		
		typologyReader = new Read(typologyPath);
		typologyReader.getFiles();
		matrixReader = new Read(configPath);
		matrixReader.getFiles();
		
		try {
			readTypology = new TypologyReader(this, typologyReader.getTitles(), typologyReader.read());
			readMatrix = new MatrixReader(this, matrixReader.getTitles(),matrixReader.read());
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		readMatrix.buildMatrix();
		
		physics = new ParticleSystem( 0, (float) 1.0 );
		
		graphBuild = new graphBuilder(this, physics);
		graphBuild.buildParticles(readTypology.buildSpaces());
	
		controlP5 = new ControlP5(this);
		gui = new GUI(this, controlP5);
		gui.initGUI();
	}

	public void draw() {
		physics.tick( (float)1.0 );
		
		if (record) {
		   beginRaw(PDF, "output.pdf");
		  }

		background(255);
		
		pushMatrix();
		
		if(MODE == 1){
			graphBuild.draw();
		}else if(MODE == 2){
			adjGraph.draw();	
		}
		
		
		focusParticle = getParticle(mouseX,mouseY);
		popMatrix();
		
		if(MODE == 1){
			graphBuild.drawMatrix();	
		}
		
		
		drawDrag();
		
		  if (record) {
			    endRaw();
				record = false;
			  }

	}
	
	Particle getParticle(float mX, float mY){

		float x,y,z;
		float bounds = 10;
	    for(int i=0;i<physics.numberOfParticles();i++){
			
	    	x = physics.getParticle(i).position().x();
			y = physics.getParticle(i).position().y();
			z = physics.getParticle(i).position().z();
			
			float screenXpos;
			float screenYpos;
			
			screenXpos=screenX(x,y,z);
			screenYpos=screenY(x,y,z);
			
			if (mX>screenXpos-bounds && mX<screenXpos+bounds && mY>screenYpos-bounds && mY<screenYpos+bounds) {
			   
				pushStyle();
				fill(255,0,0);
				Particle n = physics.getParticle(i);
				ellipse((float)n.position().x(),n.position().y(),(float)5,(float)5);
				tsX = screenX(n.position().x(),n.position().y(), n.position().z());
				tsY = screenY(n.position().x(),n.position().y(), n.position().z());
				popStyle();
				return physics.getParticle(i);
				}
	    }
	    return null;
	}
	
	public void drawDrag(){
		
		if(dragging){
			if(selectParticle != null && MODE == 1){
				pushStyle();
				stroke(0,0,0);
				line(tsX,tsY,mouseX,mouseY);
				popStyle();
			}		
			if(selectParticle != focusParticle && selectParticle != null && focusParticle != null){
				pushStyle();
				stroke(255,0,0);
				line(tsX,tsY,otX,otY);
			}
		}
	}
	
	public void writeConfig() throws WriteException, IOException{
		String fileName = "data/lala.xls";
		BiMap spaceMap = graphBuild.spaceMap;
		ArrayList springAParticles = graphBuild.springAParticles;
		ArrayList springBParticles = graphBuild.springBParticles;
		MatrixWriter configWrite = new MatrixWriter(fileName,physics,springAParticles, springBParticles, spaceMap);
		configWrite.write();
	}
	
		public void mousePressed(){

			if(focusParticle != null){
		
				selectParticle = focusParticle;   
				otX = tsX;
				otY = tsY;
				dragging = true;
				tX = mouseX;
				tY = mouseY;
				ogX = focusParticle.position().x();
				ogY = focusParticle.position().y();
			}
		}
		
		public void mouseClicked(){
			if(focusParticle != null){
				if(graphBuild.springAParticles.contains(focusParticle)){
					int index = graphBuild.springAParticles.indexOf(focusParticle);
					graphBuild.springAParticles.remove(index);
					graphBuild.springBParticles.remove(index);
					physics.removeSpring(index);
				}else if(graphBuild.springBParticles.contains(focusParticle)){
					int index = graphBuild.springBParticles.indexOf(focusParticle);
					graphBuild.springAParticles.remove(index);
					graphBuild.springBParticles.remove(index);
					physics.removeSpring(index);
				}
				}
		}
	
		public void mouseDragged(){
			
			switch(MODE){
			//CONNECT PARTICLES
			case 1:

			
			break;
				
			//DRAG PARTICLES
			case 2:
				
				if(selectParticle != null){
					
					float dX=mouseX-tX;
				    float dY=mouseY-tY;	 
				   // println(dX+" "+dY);
					selectParticle.position().set(ogX+dX, ogY+dY, selectParticle.position().z());
				}
				
			break;
			}
		}
		
		public void mouseReleased(){
			
			if(selectParticle != focusParticle && selectParticle != null && focusParticle != null){
				graphBuild.addConnection(selectParticle, focusParticle);
			}
			dragging = false;
			selectParticle = null;
		}
		
		public void keyPressed()
		{
		  if ( key == 'c' )  //clears screen and resets the script
		  {
			  graphBuild.initialize();
			  graphBuild.buildParticles(readTypology.buildSpaces());
		    return;
		  }

		  if ( key == ' ' )  //when spacebar is pressed, a new node is added
		  {
			 try {
				writeConfig();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   // aGraph.addNode();
		    return;
		  }
		  
		  if(key == 'r'){
			  record = true;
		  }
		}
		
		public void controlEvent(ControlEvent theControlEvent) {
			
			  if(theControlEvent.isController()) {
			    println("controller : "+theControlEvent.controller().id());
			  } else if (theControlEvent.isTab()) {
			    println("tab : "+theControlEvent.tab().id()+" / "+theControlEvent.tab().name());
			  
			    switch(theControlEvent.tab().id()){
			    case(1):
			    	physics.clear();
					graphBuild = new graphBuilder(this, physics);
					graphBuild.buildParticles(readTypology.buildSpaces());
			    	MODE = 1;
			    break;
			    case(2):
			    	physics.clear();
			    	adjGraph = new Graph(this, physics);
					adjGraph.buildGraph(readMatrix.nodeList,readMatrix.typeList, readMatrix.sizeList, readMatrix.adjacencyList);
				   	MODE = 2;
					break;
			    }
			  }
		}
		
	public static void main(String _args[]) {
		PApplet.main(new String[] { run.AdjacencyGraph.class.getName() });
	}
	

	
}
