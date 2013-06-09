package elements;

import processing.core.PApplet;
import controlP5.*;

public class GUI {
	
	ControlP5 gui;
	PApplet p;
	
	
	public GUI(PApplet parent, ControlP5 controlP5){
		p = parent;
		gui = controlP5;
	}
	
	public void initGUI(){
		gui.tab("default").activateEvent(true);
		gui.tab("default").setId(1);
		gui.tab("default").setLabel("GRAPH BUILDER");
		gui.tab("GRAPH DISPLAY").activateEvent(true);
		gui.tab("GRAPH DISPLAY").setId(2);
	}
	
	//TAB 1 BUILD GRAPH
	//TAB 2 DISPLAY GRAPH
	//LOAD BUTTON
	//PRINT PDF
	
	//DUMP BUTTON
	
}
