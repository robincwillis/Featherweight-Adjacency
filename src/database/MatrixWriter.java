/**
 * Featherweight : AdjacencyGraph
 * Copyright (C) 2010 Robin Willis
 * by Robin Willis (http://www.designingcrime.com) Jan 20, 2011
 * --------------------------------------------------------------------
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * --------------------------------------------------------------------
 * @author Robin Willis
 * @version 1.0
 */

package database;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.io.*;
import java.util.Locale;
import traer.physics.*;
import traer.physics.Particle;
import java.util.ArrayList;
import typology.*;

public class MatrixWriter {
	
	  ParticleSystem physics;
	  ArrayList springAParticles;
	  ArrayList springBParticles;
	  BiMap<Particle,Space> spaceMap;
	  private String filename;

	  /**
	   * The workbook
	   */
	  private WritableWorkbook workbook;

	  /**
	   * Constructor
	   * 
	   * @param fn 
	   */
	  public MatrixWriter(String fn, ParticleSystem x, ArrayList sA, ArrayList sB, BiMap<Particle,Space> sM)
	  {
		springAParticles = sA;
		springBParticles = sB;
		spaceMap = sM;
		physics = x;
	    filename = fn;
	  }

	  public void write() throws IOException, WriteException
	  {
	    WorkbookSettings ws = new WorkbookSettings();
	    ws.setLocale(new Locale("en", "EN"));
	    workbook = Workbook.createWorkbook(new File(filename), ws);


	    WritableSheet s1 = workbook.createSheet("Global", 0);
	    WritableSheet s2 = workbook.createSheet("Adjacency Matrix", 1);
	    WritableSheet s3 = workbook.createSheet("Base Spaces", 2);
	    
	    writeGlobalSheet(s1);
	    writeAdjacencySheet(s2);
	    writeBaseSpacesSheet(s3);
	    
	    workbook.write();
	    workbook.close();
	  }
	  
	  public void writeGlobalSheet(WritableSheet s) throws WriteException
	  {
		  Label label = new Label(0, 2, "A label record");
		  s.addCell(label); 
	  }
	  
	  public void writeAdjacencySheet(WritableSheet s) throws WriteException
	  {
		  
		  
		  //MATRIX LOOP
			for(int i=0;i<physics.numberOfParticles();i++){
				for (int j=0;j<physics.numberOfParticles();j++){
					
					boolean writeOne = false;
					Number number;
					Particle pI = physics.getParticle(i);
					Particle pJ = physics.getParticle(j);
					Space space = spaceMap.get(pJ);
					
					for(int k=0;k<springAParticles.size();k++){
						if(springAParticles.get(k) == pI && springBParticles.get(k) == pJ){
							
							//WRITE ONE
							writeOne = true;
							//System.out.println("hit");
						}
						if(springAParticles.get(k) == pJ && springBParticles.get(k) == pI){
						
							//WRITE ONE
							writeOne = true;
							//System.out.println("hit");
					}
					
						//WRITE CELL
				}
					
					if(writeOne){ 
						number = new Number(i, j, 1);
					}else{
						number = new Number(i, j, 0);
					}
					 s.addCell(number); 
					 writeSpaceInfo(s, physics.numberOfParticles(), space, j);
				}
			}

		  //write the matrix
		  //write the space names
		  //write the space widths
		  //write the space lengths
		  //write the space heights
	  }
	  
	  public void writeSpaceInfo(WritableSheet sheet, int matrixWidth, Space space, int index) throws WriteException
	  {
		  String spaceName = space.key;
		  Label keyLabel = new Label(matrixWidth, index, spaceName);
		  sheet.addCell(keyLabel); 
		  
		  String activityName = space.activity;
		  Label acitivityLabel = new Label(matrixWidth+1, index, activityName);
		  sheet.addCell(acitivityLabel); 
		  
		  
		  String spaceType = space.type;
		  Label typeLabel = new Label(matrixWidth+2, index, spaceType);
		  sheet.addCell(typeLabel); 
		  
		  float width = space.dim.w;
		  Number wNumber = new Number(matrixWidth+3, index, width);
		  sheet.addCell(wNumber); 
		  
		  float length = space.dim.l;
		  Number lNumber = new Number(matrixWidth+4, index, length);
		  sheet.addCell(lNumber); 
		  
		  float height = space.dim.h;
		  Number hNumber = new Number(matrixWidth+5, index, height);
		  sheet.addCell(hNumber); 
	  }
	  
	  public void writeBaseSpacesSheet(WritableSheet s) throws WriteException
	  {
		  Label label = new Label(0, 2, "A label record");
		  s.addCell(label); 
	  }
}
