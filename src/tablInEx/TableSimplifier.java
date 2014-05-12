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
	public static Table MergeHeaders(Table table)
	{
		if(table.stat.getNum_of_header_rows()<2)
			return table;
		Cell[][] cells = table.getTable_cells();
		
		for(int i = table.stat.getNum_of_header_rows() - 2; i >= 0;i--)
		{
			for(int j = 0; j<cells[i].length;j++)
			{
				if(!Utilities.isSpaceOrEmpty(cells[i][j].getCell_content()))
					cells[table.stat.getNum_of_header_rows() - 1][j].setCell_content(cells[i][j].getCell_content()+ " "+cells[table.stat.getNum_of_header_rows() - 1][j].getCell_content());
			}
		}
		
		Cell[][] newcells = new Cell[table.getNum_of_rows()- table.stat.getNum_of_header_rows()+1][table.getNum_of_columns()];
		for(int i = 0;i<newcells.length;i++)
		{
			newcells[i] = cells[i+table.stat.getNum_of_header_rows()-1].clone();
		}
		table.stat.setNum_of_header_rows(1);
		table.setNum_of_rows(1+table.stat.getNum_of_body_rows());
		table.cells = newcells;
		
		//TODO: Delete this part
		for(int i = 0;i<table.cells.length;i++){
			int num_of_empty = 0;
			for(int j = 0;j<table.cells[i].length;j++)
			{
				
				if(table.cells[i][j].getCell_content()==null)
				{
					table.cells[i][j].setCell_content("");
				}
				if(table.cells[i][j]==null)
				{
					table.cells[i][j] = new Cell(i, j);
					table.cells[i][j].setCell_content("");
				}
				if(Utilities.isSpaceOrEmpty(table.cells[i][j].getCell_content()))
				{
					num_of_empty++;
				}
			}
			if(table.getNum_of_columns()-num_of_empty>1)
				table.isEmptyOnlyHeaders = false;
		}
		
		return table;
	}

}
