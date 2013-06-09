package elements;

import processing.core.PApplet;
import traer.physics.*;

public class Node {
	
	PApplet p;
	public Particle n;
	public int c;
	public float r;
	public String t;
	
	final float SCALE = 100;
	final float NODE_SIZE = 5;
	
	public Node(PApplet parent, Particle particle, int color, float area, String title){
		p = parent;
		n = particle;
		c = p.color(color);
		r = area/SCALE;
		t = title;
		p.println(color);
	}
	
	public void draw(){
		p.pushStyle();
		p.noStroke();
	    p.fill(c);
	    p.ellipse( n.position().x(), n.position().y(), NODE_SIZE+r, NODE_SIZE +r);
	   

	    p.fill(0,150);
	    p.ellipse( n.position().x(), n.position().y(), NODE_SIZE, NODE_SIZE );
	    p.popStyle();
	}
	
}
