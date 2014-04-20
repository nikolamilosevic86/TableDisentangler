/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.util.LinkedList; 

/**
 * Table simplifier siplifies tables for later use
 * @author Nikola Milosevic
 */
public class TableSimplifier {
	
	/**
	 * Main simplification method for tables<br/>
	 * Example of usage: {@code TableSimplifier.Simplify(table);}
	 * @param cells - cell matrix of the table
	 * @return simplified table as matrix of cells. Return type is Cell[][]
	 */
	public static Cell[][] Simplify(Cell[][] cells)
	{
		
		return cells;
	}
	
	/**
	 * Removes empty rows from the table.
	 *<br/> Example of usage: {@code TableSimplifier.DeleteEmptyRows(table);} 
	 * @param cells - matrix of cells in a table
	 * @return Cell matrix with removed empty cells
	 */
	public static Cell[][] DeleteEmptyRows(Cell[][] cells)
	{
		LinkedList <Cell[]> survivingRows = new LinkedList<Cell[]>();
		int columnCount = cells[0].length;
		for(int i = 0;i<cells.length;i++)
		{
			boolean isEmpty = true;
			for(int j = 0;j<cells[i].length;j++)
			{
				// ASCII 160 char happens when we split the spanning cell. Non braking space: http://en.wikipedia.org/wiki/Non-breaking_space
				if(!cells[i][j].getCell_content().trim().equalsIgnoreCase(" ") && !cells[i][j].getCell_content().trim().equalsIgnoreCase("") && (((int)cells[i][j].getCell_content().trim().charAt(0))!= 160))
				{
					isEmpty  = false;
					break;
				}	
			}
			if(!isEmpty)
			{
				survivingRows.add(cells[i]);
			}
		}
		cells = new Cell[survivingRows.size()][columnCount];
		for(int i = 0; i<survivingRows.size();i++)
		{
			cells[i] = survivingRows.get(i);
		}
		
		
		return cells;
	}
	
	/**
	 * Function that simplifies complex headers, that have more then one row.
	 * Function merges complex header into one row header, by appending 
	 * higher cells before content of lower cells. At the end copied cells are deleted.
	 * @param cells - Cell[][] - Table
	 * @return cells - Cell[][] - Table with simplified header
	 */
	public static Cell[][] MergeHeaders(Cell[][] cells)
	{
		return cells;
	}

}
