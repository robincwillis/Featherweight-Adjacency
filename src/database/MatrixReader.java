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

import java.util.ArrayList;
import jxl.Cell;
import jxl.Workbook;
import processing.core.PApplet;

public class MatrixReader {

	PApplet p;
	Parse parser;
	Workbook matrixBook;
	ArrayList<Workbook> workbookList;
	ArrayList<String> titleList;

	public ArrayList<String> nodeList;
	public ArrayList<String> activityList;
	public ArrayList<String> typeList;
	public ArrayList<Float> sizeList;
	public ArrayList<ArrayList<String>> adjacencyList;

	int KEY_COL = 0; // title of space
	int ACT_COL = 1; // activity space belongs to
	int TYPE_COL = 2; // type of space
	int WIDTH_COL = 3; // width of space
	int LENGTH_COL = 4; // length of space
	int SIZE_COL = 5; // area of space
	int SHEET = 1;
	int rowCount, columnCount;
	int order = 6;

	public MatrixReader(PApplet parent, ArrayList<String> titleList,
			ArrayList<Workbook> workbookList) {

		p = parent;
		parser = new Parse();
		this.workbookList = workbookList;
		this.titleList = titleList;
		matrixBook = (Workbook) workbookList.get(0);

	}

	public void buildMatrix() {
		// get dimensions
		columnCount = matrixBook.getSheet(1).getColumns() - 6;
		rowCount = matrixBook.getSheet(SHEET).getRows();
		nodeList = nodeList(rowCount, rowCount, matrixBook);
		adjacencyList = adjacencyList(rowCount, rowCount, nodeList, matrixBook);
		typeList = getList(rowCount, rowCount + TYPE_COL, matrixBook);
		activityList = getList(rowCount, rowCount + ACT_COL, matrixBook);
		sizeList = getValueList(rowCount, rowCount + WIDTH_COL, rowCount
				+ LENGTH_COL, matrixBook);

	}

	public ArrayList<String> nodeList(int rCount, int cCount, Workbook mBook) {
		ArrayList<String> nodeList = new ArrayList<String>();

		for (int i = 0; i < rCount; i++) {
			String keyLabel = mBook.getSheet(SHEET)
					.getCell(cCount + KEY_COL, i).getContents();
			String activityLabel = mBook.getSheet(SHEET)
					.getCell(cCount + ACT_COL, i).getContents();
			String nodeLabel = activityLabel + "-" + keyLabel;

			nodeList.add(nodeLabel);

		}

		return nodeList;
	}

	public ArrayList<ArrayList<String>> adjacencyList(int rCount, int cCount,ArrayList<String> nodeList, Workbook mBook) {

		ArrayList<ArrayList<String>> adjacencyList = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < cCount; i++) {

			String nodeLabel = nodeList.get(i);

			ArrayList<String> adjacencySet = new ArrayList<String>();

			for (int j = 0; j < rCount; j++) {
				Cell cell = mBook.getSheet(SHEET).getCell(j, i);

				int adjacency = parser.toInt(cell);

				if (adjacency == 1) {
					String aLabel = nodeList.get(j);
					adjacencySet.add(aLabel);
				}

			}
			adjacencyList.add(adjacencySet);
		}

		return adjacencyList;
	}

	public ArrayList<String> getList(int rCount, int col, Workbook mBook) {
		ArrayList<String> getList = new ArrayList<String>();

		for (int i = 0; i < rCount; i++) {

			String label = mBook.getSheet(SHEET).getCell(col, i).getContents();
			getList.add(label);

		}
		return getList;
	}

	public ArrayList<Float> getValueList(int rCount, int colA, int colB,
			Workbook mBook) {
		ArrayList<Float> getList = new ArrayList<Float>();

		for (int i = 0; i < rCount; i++) {
			float valueA = parser.toFloat(mBook.getSheet(SHEET)
					.getCell(colA, i));
			float valueB = parser.toFloat(mBook.getSheet(SHEET)
					.getCell(colB, i));
			float size = valueA * valueB;
			getList.add(size);
		}
		return getList;
	}
}
