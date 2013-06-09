package graph;

import processing.core.PApplet;
import processing.opengl.PGraphicsOpenGL;
import traer.animation.Smoother3D;
import traer.physics.*;
import java.util.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import elements.*;
import typology.*;

public class graphBuilder {

	PApplet p;
	ParticleSystem physics;
	BiMap<Node,Particle> nodeMap; 
	public BiMap<Particle,Space> spaceMap;
	Smoother3D centroid;

	VTextRenderer textRender;
	
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public ArrayList<Particle> springAParticles = new ArrayList<Particle>();
	public ArrayList<Particle> springBParticles = new ArrayList<Particle>();
	
	final float RADIUS;
	final float EDGE_STRENGTH = (float)0.1;  //strength of spring (controls the amount of stretching possible)
	
	public graphBuilder(PApplet parent, ParticleSystem x){
		p = parent;
		physics = x;
		centroid = new Smoother3D( (float) 0.8 );
		textRender = new VTextRenderer(p, "Arial", 75, true, true );
		textRender.setColor(0);
		RADIUS = p.height/4; 
	}
	
	public void draw(){
	
		  if ( physics.numberOfParticles() > 1 )	  
			  updateCentroid();
			  centroid.tick();
			  p.translate( p.width/2 , p.height/2 );
			  p.scale( centroid.z() ); 
			  p.translate( -centroid.x(), -centroid.y() ); 
			  drawNetwork();
	}
	
	public void buildParticles(ArrayList<Space> spaces){
			
		p.println(spaces.size());
		
		float px, py;
		
		nodeMap = HashBiMap.create();
		spaceMap = HashBiMap.create();
		
		for(int i=0;i<spaces.size();i++){
			
			Space space = spaces.get(i);

			float angle = (360/spaces.size())*i;
			Particle n  = physics.makeParticle();
			
			px = p.width/2  + p.cos(p.radians(angle))*(RADIUS/2);
			py = p.height/2 + p.sin(p.radians(angle))*(RADIUS/2);
			n.position().set(px,py,0);
			
			String title = space.activity +"-"+ space.key;
			
			Node node = new Node(p,n, space.color, space.area, title);
			nodeList.add(node);
			
			nodeMap.put(node, n);
			spaceMap.put(n, spaces.get(i));
		}
		
	}
	
	public void addConnection(Particle a, Particle b){
		
		float aX = a.position().x();
		float aY = a.position().y();
		float aZ = a.position().z();
		
		float bX = b.position().x();
		float bY = b.position().y();
		float bZ = b.position().z();
		
		
		float LENGTH = p.dist(aX,aY,aZ,bX,bY,bZ);
		Spring s =  physics.makeSpring( a, b, EDGE_STRENGTH, EDGE_STRENGTH, LENGTH);
		
		updateConnectionList(s);
	}

	public void drawNetwork(){
		
		  for ( int i = 0; i < nodeList.size(); ++i )
		  {
		    Node node = nodeList.get(i);  
			Particle v = node.n;
		    float angle = (360/nodeList.size())*i*(p.PI/180);
		    
		    node.draw();
		    
		      p.pushMatrix();
			  p.translate((int)v.position().x(),(int)v.position().y(),(int)v.position().z());
			  p.rotate(angle);
			  testText(i+" "+node.t); 
			  p.popMatrix();
		  }

		 drawConnections();
	}
	
	public void drawConnections(){
		
		 p.pushStyle();
		  // draw branching lines that connect each new node
		  p.stroke(100);
		  p.strokeWeight((float) 3);
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
		 p.popStyle();
	}
	
	void updateCentroid() // whenever system is reorganized or scaled, node positions have to be updated
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
	    centroid.setTarget( xMin + (float)0.5*deltaX, yMin +(float)0.5*deltaY, p.height/(deltaY+270) );
	  else
	    centroid.setTarget( xMin + (float)0.5*deltaX, yMin +(float)0.5*deltaY, p.width/(deltaX+270) );
	}
	
	public void updateConnectionList(Spring s){

		Particle a = s.getOneEnd();
		Particle b = s.getTheOtherEnd();
		springAParticles.add(a);
		springBParticles.add(b);

	}
	
	public void drawMatrix(){
		float cS = 5;

		
		for(int i=0;i<physics.numberOfParticles();i++){
			for (int j=0;j<physics.numberOfParticles();j++){
				Particle pI = physics.getParticle(i);
				Particle pJ = physics.getParticle(j);

				p.pushStyle();
				p.stroke(100);
				p.strokeWeight(1);
				p.fill(255,0);
				for(int k=0;k<springAParticles.size();k++){
					if(springAParticles.get(k) == pI && springBParticles.get(k) == pJ){
						p.fill(255,0,0,50);
						p.rect(10+(i*cS),20+(j*cS),cS,cS);
						
					}
					if(springAParticles.get(k) == pJ && springBParticles.get(k) == pI){
						p.fill(0,255,0,50);
						p.rect(10+(i*cS),20+(j*cS),cS,cS);
						
					}
				}
				
				p.stroke(200);
				p.rect(10+(i*cS),20+(j*cS),cS,cS);
				p.popStyle();
			}
		}
		
		
		
	}
		
	public void initialize() //this function clears the screen and resets function
	{
	  nodeList.clear();
	  springAParticles.clear();
	  springBParticles.clear();
	  physics.clear();
	  centroid.setValue( 0, 0, (float)1.0 );
	}
	
	public void testText(String s){
		textRender.print( s, 10,-2,0, (float)(.1));
	}
	
	
}
