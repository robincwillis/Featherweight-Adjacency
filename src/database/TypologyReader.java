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

import processing.core.PApplet;
import java.util.*;
import typology.*;
import jxl.*;

public class TypologyReader {

	PApplet p;
	Parse parser;

	// List of Activity Workbooks
	ArrayList<Workbook> workbookList;
	// List of Titles
	ArrayList<String> titleList;

	int SPACE_SHEET = 1;
	int KEY_COL = 0; // title of space
	int W_COL = 3;
	int L_COL = 4;
	int H_COL = 5;
	int TYPE_COL = 6; // type of space
	int SOFT_CLR;
	int HARD_CLR;
	int CORE_CLR;
	int OPEN_CLR;
	int CIRC_CLR;

	int rowCount, columnCount;

	public TypologyReader(PApplet parent, ArrayList<String> titleList,
			ArrayList<Workbook> workbookList) {

		p = parent;
		parser = new Parse();
		this.workbookList = workbookList;

		this.titleList = titleList;

		SOFT_CLR = p.color(10, 55, 255, 50);
		HARD_CLR = p.color(255, 10, 10, 50);
		CORE_CLR = p.color(240, 20, 20, 50);
		OPEN_CLR = p.color(10, 255, 10, 50);
		CIRC_CLR = p.color(230, 230, 40, 50);
	}

	public ArrayList<Space> buildSpaces() {

		ArrayList<Space> spaceList = new ArrayList<Space>();

		for (int i = 0; i < workbookList.size(); i++) {
			Workbook activityBook = workbookList.get(i);
			String title = titleList.get(i);
			Sheet spaceSheet = activityBook.getSheet(SPACE_SHEET);

			columnCount = activityBook.getSheet(SPACE_SHEET).getColumns();
			rowCount = activityBook.getSheet(SPACE_SHEET).getRows();

			for (int j = 2; j < rowCount; j++) {
				Space space = new Space(p);
				String key = spaceSheet.getCell(KEY_COL, j).getContents();
				space.setKey(key);
				space.setActivity(title);
				
				float w = parser.toFloat(spaceSheet.getCell(W_COL, j));
				float l = parser.toFloat(spaceSheet.getCell(L_COL, j));
				float h = parser.toFloat(spaceSheet.getCell(H_COL, j));
				
				space.setDim(w, l, h);
				String type = spaceSheet.getCell(TYPE_COL, j).getContents();
				space.setType(type);
				space.setArea();
				setColor(type, space);
				spaceList.add(space);
			}
		}
		return spaceList;
	}

	public void setColor(String type, Space space) {

		if (type.equals("Soft")) {
			space.setColor(SOFT_CLR);
		}
		if (type.equals("Hard")) {
			space.setColor(HARD_CLR);
		}
		if (type.equals("Open")) {
			space.setColor(OPEN_CLR);
		}
		if (type.equals("Core")) {
			space.setColor(CORE_CLR);
		}
		if (type.equals("Circ")) {
			space.setColor(CIRC_CLR);
		}

	}

}
