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

import jxl.Cell;
import jxl.CellType;
import jxl.FormulaCell;
import jxl.NumberCell;

public class Parse {
	
	public String checkReturn(Cell cell){
		String type = "la";
        if (cell.getType() == CellType.NUMBER_FORMULA || 
                cell.getType() == CellType.STRING_FORMULA || 
                cell.getType() == CellType.BOOLEAN_FORMULA ||
                cell.getType() == CellType.DATE_FORMULA ||
                cell.getType() == CellType.FORMULA_ERROR)
        {
        	
        }
		if(cell.getType() == CellType.LABEL){
			
		}
		if(cell.getType() == CellType.NUMBER){
			
		}
		return type;
		
	}
	
	public String toString(Cell cell){
		String contents;
			contents = cell.getContents();
		return contents;
	}
	
	public int toInt(Cell cell){
		float value =  this.toFloat(cell);
		return (int)value;
	}
	
	public float toFloat(Cell cell){
		float value = 0;
		
		if(cell.getType() == CellType.LABEL){
			
            try
            {
            	String contents = cell.getContents();
            	value = Float.valueOf(contents.trim()).floatValue();

            }
            catch (NumberFormatException nfe)
            {
              System.out.println("NumberFormatException: " + nfe.getMessage());
              
            }
          //return null;
	
		}
		
		  if (cell.getType() == CellType.NUMBER_FORMULA || 
	                cell.getType() == CellType.STRING_FORMULA || 
	                cell.getType() == CellType.BOOLEAN_FORMULA ||
	                cell.getType() == CellType.DATE_FORMULA ||
	                cell.getType() == CellType.FORMULA_ERROR)
          {
           FormulaCell nfc = (FormulaCell) cell;
           String contents = nfc.getContents();
           	
               try
               {
                  value = Float.valueOf(contents.trim()).floatValue();
 
               }
               catch (NumberFormatException nfe)
               {
                 System.out.println("NumberFormatException: " + nfe.getMessage());
               }

           }
		if(cell.getType() == CellType.NUMBER){
			NumberCell nc = (NumberCell) cell;
			value = (float) nc.getValue();
			
		}
		return value;
		
		
	}
}
