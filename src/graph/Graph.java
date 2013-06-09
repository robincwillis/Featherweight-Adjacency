package graph;

import processing.core.PApplet;
import processing.core.PFont;
import traer.physics.*;
import traer.animation.*;
import typology.Space;

import java.util.*;

import com.google.common.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import elements.Node;
import elements.VTextRenderer;


public class Graph {

	PApplet p;
	
	
	final float NODE_SIZE = 5;  //node diameter
	final float EDGE_LENGTH = 20;  //length of branching lines initially
	final float EDGE_STRENGTH = (float)0.1;  //strength of spring (controls the amount of stretching possible)
	final float SPACER_STRENGTH = 300;  // controls the amount of reorganization
	
	int SOFT_CLR;
	int HARD_CLR;
	int CORE_CLR;
	int OPEN_CLR;
	int CIRC_CLR;
	
	BiMap<String,Particle> nodeMap;
	public ArrayList <String>typeList;
	public ArrayList <Float>sizeList;
	ParticleSystem physics;
	Smoother3D centroid;
	Particle z;
	Particle anchor;
	
	public ArrayList<Node> nodes = new ArrayList<Node>();
	
	VTextRenderer textRender;
	
	public Graph(PApplet parent, ParticleSystem _physics){
	p = parent;
	physics = _physics;
	centroid = new Smoother3D( (float) 0.8 );
	textRender = new VTextRenderer(p, "Arial", 75, true, true );
	textRender.setColor(0);
	
	 SOFT_CLR = p.color(10,55,255);
	 HARD_CLR = p.color(255,10,10);
	 CORE_CLR = p.color(240,20,20);
	 OPEN_CLR = p.color(10,255,10);
	 CIRC_CLR = p.color(230,230,40);
	
	}
	
	public void draw() {
		  if ( physics.numberOfParticles() > 1 )
		  updateCentroid();
		  centroid.tick();
		  
		  p.translate( p.width/2 , p.height/2 );
		  p.scale( centroid.z() );
		  p.translate( -centroid.x(), -centroid.y() );
		 
		 // drawLines(); 
		  drawNetwork(); 
	}
	
	public void drawNetwork()
	{     
	  // finds a node within the system and draws a new node branching from that point
	 int fill = 150;
	 String type;
	  p.noStroke();
	  for ( int i = 0; i < physics.numberOfParticles(); ++i )
	  {
	    Particle v = physics.getParticle( i );
	    type = typeList.get(i);
	    
	    if(type.equals("Soft")){
	    	fill = SOFT_CLR;
	    }if(type.equals("Hard")){
	    	fill = HARD_CLR;
	    }if(type.equals("Core")){
	    	fill = CORE_CLR;
	    }if(type.equals("Open")){
	    	fill = OPEN_CLR;
	    }if(type.equals("Circ")){
	    	fill = CIRC_CLR;
	    }
	    
	    p.noStroke ();
	    p.fill(fill,75);
	    p.ellipse( v.position().x(), v.position().y(), NODE_SIZE+10, NODE_SIZE +10);
	    p.fill(fill,30);
	    p.ellipse( v.position().x(), v.position().y(), NODE_SIZE+10+(sizeList.get(i)/100), NODE_SIZE+10+(sizeList.get(i)/100));
	    //p.stroke (255,0,0);
	    p.fill(0,150);
	    p.ellipse( v.position().x(), v.position().y(), NODE_SIZE, NODE_SIZE );
	    
	      p.pushMatrix();
		  p.translate((int)v.position().x(),(int)v.position().y(),(int)v.position().z());

		  testText(i+" "+nodeMap.inverse().get(v)); 
		  p.popMatrix();
	    
	  }
	  
	  // draw branching lines that connect each new node
	  p.stroke(0);
	  p.strokeWeight((float) .75);
	  p.beginShape( p.LINES );
	  for ( int i = 0; i < physics.numberOfSprings(); ++i )
	  {
	    Spring e = physics.getSpring( i );
	    Particle a = e.getOneEnd();
	    Particle b = e.getTheOtherEnd();
	    p.vertex( a.position().x(), a.position().y() );
	    p.vertex( b.position().x(), b.position().y() );
	  }
	 p.endShape();
	  
	}
	
	public void drawLines(){
		  for ( int i = 0; i < physics.numberOfParticles(); ++i )
		  {
		    for ( int j = 0; j < physics.numberOfParticles(); ++j )
		    {
		      Particle d= physics.getParticle(i);
		      Particle e= physics.getParticle(j);
		      float distance= p.dist (d.position().x(), d.position().y(),e.position().x(), e.position().y());
		 
		      if((distance>=50)&&(distance<60)){
		        p.strokeWeight((float).5);
		        p.stroke(200,0,0,70);
		        p.line(d.position().x(), d.position().y(),e.position().x(), e.position().y());
		      }
		      if((distance>=40)&&(distance<50)){
		        p.strokeWeight((float).5);
		        p.stroke(0,200,0,70);
		        p.line(d.position().x(), d.position().y(),e.position().x(), e.position().y());
		      }
		      if((distance>=60)&&(distance<70)){
		        p.strokeWeight((float).5);
		        p.stroke(0,0,200,70);
		        p.line(d.position().x(), d.position().y(),e.position().x(), e.position().y());
		      }
		    }
		  }
		}
	
	public void updateCentroid() // whenever system is reorganized or scaled, node positions have to be updated
	{
	  float
	  xMax = Float.NEGATIVE_INFINITY,
	  xMin = Float.POSITIVE_INFINITY,
	  yMin = Float.POSITIVE_INFINITY,
	  yMax = Float.NEGATIVE_INFINITY;
	 
	  for ( int i = 0; i < physics.numberOfParticles(); ++i )
	  {
	    Particle p = physics.getParticle( i );
	    xMax = Math.max( xMax, p.position().x() );
	    xMin = Math.min( xMin, p.position().x() );
	    yMin = Math.min( yMin, p.position().y() );
	    yMax = Math.max( yMax, p.position().y() );
	  }
	  float deltaX = xMax-xMin;
	  float deltaY = yMax-yMin;
	  if ( deltaY > deltaX )
	    centroid.setTarget( xMin + (float)0.5*deltaX, yMin +(float)0.5*deltaY, p.height/(deltaY+100) );
	  else
	    centroid.setTarget( xMin + (float)0.5*deltaX, yMin +(float)0.5*deltaY, p.width/(deltaX+100) );
	}
	
	public void addSpacersToNode( Particle p, Particle r ){
		  //reorganizes nodes so that they are as spaced out as possible within its connectionns
		  for ( int i = 0; i < physics.numberOfParticles(); ++i )
		  {
		    Particle q = physics.getParticle( i );
		    if ( p != q && p != r )
		      physics.makeAttraction( p, q, -SPACER_STRENGTH, 20 );  //the negative sign makes particles repel rather than attract
		  }
		}
		 
	public void makeEdgeBetween( Particle a, Particle b ) //creates a spring between node and new node
		{
		  physics.makeSpring( a, b, EDGE_STRENGTH, EDGE_STRENGTH, 40);
		}
		
	public void buildGraph(ArrayList<String>nodeList, ArrayList<String> types, ArrayList<Float> sizes, ArrayList<ArrayList<String>> adjacencyList){
		
		centroid.setValue( 0, 0, (float)1.0 );
		nodeMap = HashBiMap.create();
		
		typeList = types;
		sizeList = sizes;
		
		for(int i=0;i<nodeList.size();i++){
			Particle n = physics.makeParticle();
			nodeMap.put(nodeList.get(i), n);	
		}
		
		for(int i=0;i<adjacencyList.size();i++){
			
			Particle n = nodeMap.get(nodeList.get(i));
			ArrayList<String> adjacencySet = adjacencyList.get(i); 
			
			for(int j=0;j<adjacencySet.size();j++){
				Particle q = nodeMap.get(adjacencySet.get(j));
				addSpacersToNode( n, q );
				makeEdgeBetween( n, q );
				n.position().set(q.position().x() + (float)p.random(-1,1),q.position().y() + (float)p.random(-1,1),0);
			}
		}
	}
	
	public void addNode() //function that adds a new node everytime spacebar is pressed
		{
		  Particle a = physics.makeParticle();
		  Particle b = physics.getParticle( (int)p.random(0,(physics.numberOfParticles()-1)) );
		  while ( b == a )
		    b = physics.getParticle( (int)p.random(0,(physics.numberOfParticles()-1)) );
		  
		  addSpacersToNode( a, b );
		  makeEdgeBetween( a, b );
		
		  b.position().set(b.position().x() + (float)p.random(-1,1),b.position().y() + (float)p.random(-1,1),0);
		  p.line(a.position().x(),a.position().y(),b.position().x(),b.position().y());
		}
		
	public void testText(String s){
		textRender.print( s, 10,-2,0, (float)(.1));
	}
	
}
