/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.util.Arrays;
import java.util.LinkedList; 

import Utils.Utilities;

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
		table.original_cells = table.cells;
		if(table.cells==null)
			return table;
		if(table.stat.getNum_of_header_rows()<2 && table.cells!=null && table.cells[0]!=null){
			for(int j = 0; j<table.cells[0].length;j++ )
				table.cells[0][j].headers.add(table.cells[0][j].getCell_content());
			return table;
		}
		Cell[][] cells = table.getTable_cells();
		
		for(int i = table.stat.getNum_of_header_rows() - 1; i >= 0;i--)
		{
			for(int j = 0; j<cells[i].length;j++)
			{
				if(cells[i][j].getCell_content()==null)
				{
					cells[i][j].setCell_content("");
				}
				if(!Utilities.isSpaceOrEmpty(cells[i][j].getCell_content()) ){
					//Appending values
					if(cells.length>i+1&& cells[i+1]!=null && cells[i+1][j]!=null && !cells[i][j].getCell_content().equals(cells[i+1][j].getCell_content())){
						//Appending line - not now used
						//cells[table.stat.getNum_of_header_rows() - 1][j].setCell_content(cells[i][j].getCell_content()+ " "+cells[table.stat.getNum_of_header_rows() - 1][j].getCell_content());
						cells[table.stat.getNum_of_header_rows() - 1][j].headers.addFirst(cells[i][j].getCell_content());
					}
					cells[table.stat.getNum_of_header_rows() - 1][j].setIs_header(true);
				}
			}
		}
		
		System.out.println("Simplifying "+table.getDocumentFileName());
		Cell[][] newcells = new Cell[table.cells.length - table.stat.getNum_of_header_rows()+1][table.getNum_of_columns()];
		for(int i = 0;i<newcells.length;i++)
		{
			newcells[i] = cells[i+table.stat.getNum_of_header_rows()-1].clone();
		}
		for(int j = 0;j<newcells[0].length;j++)
		{
			for(int s = newcells[0][j].headers.size()-1;s>=0;s--)
				if(!newcells[0][j].headers.get(s).equals(newcells[0][j].getCell_content()))
					newcells[0][j].setCell_content(newcells[0][j].headers.get(s)+ " "+newcells[0][j].getCell_content());
		}
		table.stat.setNum_of_header_rows(1);
		table.setNum_of_rows(1+table.stat.getNum_of_body_rows());
		table.cells = newcells;
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
	/**
	 * Function that labels header cells in case header 
	 * is wrongly wrapped in thead tags. This can be case in headers with more 
	 * than one row. All the rows that contains spanning are labeled as headers 
	 * until first one that does not span
	 * @param table -  Table
	 * @return table - Table with labeled header cells
	 */
	public static Table LabelHeaderCells(Table table)
	{
		Cell[][] cells = table.getTable_cells();
		boolean isHeader = true;
		for(int i = 0;i<cells.length;i++)
		{
			boolean isSpanning = false;
			for(int j=0;j<cells[i].length;j++)
			{
				if(cells[i][j].isIs_columnspanning())
					isSpanning = true;
			}
			if(isSpanning == false)
				isHeader = false;
			
			if(isHeader)
			{
				for(int j=0;j<cells[i].length;j++)
				{
					cells[i][j].setIs_header(true);
				}
			}
		}
		return table;
	}
	
	/**
	 * Function that simplifies complex stubs, that have more then one columns.
	 * Function merges complex stubs into one column stub, by appending 
	 * leftmost cells before content of right-most cells. At the end copied cells are deleted.
	 * This works only for 2 cell header, needs a fix
	 * @param cells - Cell[][] - Table
	 * @return cells - Cell[][] - Table with simplified header
	 */
	public static Table MergeStubs(Table table)
	{
		if(table.getNum_of_columns()<3)
			return table;
		Cell[][] cells = table.getTable_cells();
		Cell[][] originalcells = table.original_cells;
		String prevVal = "";
		boolean hasComplexStub = false;
		int cnt =0 ; 
		for(int i = 1;i<cells.length;i++) // starting from the header
		{
			if(Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && !Utilities.isSpaceOrEmpty(cells[i][1].getCell_content()))
			{
				cnt++;
				if(cnt>1){
				hasComplexStub = true;
				break;
				}
			}
			else cnt = 0;
			
			if(cells[i][0].isIs_rowspanning())
				hasComplexStub = true;
		}
		if(!hasComplexStub)
			return table;
		for(int i = 0;i<cells.length;i++)
		{
			if(Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && Utilities.isSpaceOrEmpty(cells[i][1].getCell_content()))
				continue;
			if(Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && i>1)
			{
				if(cells[i-1][0]!=null && !Utilities.isSpaceOrEmpty(cells[i-1][0].getCell_content()))
				prevVal = cells[i-1][0].getCell_content();
			}
			if(hasComplexStub)
			{
				if(prevVal==null || Utilities.isSpaceOrEmpty(prevVal) || !Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()))
				{
					if(Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && !Utilities.isSpaceOrEmpty(cells[i][1].getCell_content()))
					{
						cells[i][1].setCell_content(cells[i][1].getCell_content());
						originalcells[cells[i][1].getRow_number()][cells[i][1].getColumn_number()].setIs_stub(true);
					}
					if(!Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && Utilities.isSpaceOrEmpty(cells[i][1].getCell_content()))
					{
						cells[i][1].setCell_content(cells[i][0].getCell_content());
						originalcells[cells[i][1].getRow_number()][cells[i][1].getColumn_number()].setIs_stub(true);
					}
					if(!Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && !Utilities.isSpaceOrEmpty(cells[i][1].getCell_content()))
					{
						cells[i][1].setCell_content(cells[i][0].getCell_content()+", " +cells[i][1].getCell_content());
						originalcells[cells[i][1].getRow_number()][cells[i][1].getColumn_number()].setIs_stub(true);
					}
//					if(cells[i][0].isIs_rowspanning()){
//						cells[i][1].setCell_content(cells[i][0].getCell_content()+", " +cells[i][1].getCell_content());
//					}
				}
				else
				{
					if(Utilities.isSpaceOrEmpty(cells[i][1].getCell_content())){
						cells[i][1].setCell_content(prevVal);
						originalcells[cells[i][1].getRow_number()][cells[i][1].getColumn_number()].setIs_stub(true);
					}
					else{
						cells[i][1].setCell_content(prevVal + ", " +cells[i][1].getCell_content());
						originalcells[cells[i][1].getRow_number()][cells[i][1].getColumn_number()].setIs_stub(true);
					}
				}
			}
		}
		if(hasComplexStub)
		{			
			Cell[][] newcells = new Cell[table.getNum_of_rows()][table.getNum_of_columns()-1];
			for(int i = 0;i<newcells.length;i++)
			{
				newcells[i] =  Arrays.copyOfRange(cells[i], 1, cells[i].length);
			}
			table.stat.setNum_of_header_rows(1);
			table.setNum_of_rows(1+table.stat.getNum_of_body_rows());
			table.cells = newcells;
			table.original_cells = originalcells;
		}
		
		return table;
	}

}
