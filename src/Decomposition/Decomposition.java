/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package Decomposition;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Utils.Utilities;
import stats.Statistics;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.DataExtractionOutputObj;
import tablInEx.TablInExMain;
import tablInEx.Table;
import tablInEx.TableSimplifier;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleIE. Does simple Information extraction. 
 */
public class Decomposition {
	
	/** The folder. */
	private static String folder;
	
	/**
	 * Instantiates a new simple ie.
	 *
	 * @param inpath the inpath
	 */
	public Decomposition(String inpath) {
		folder = inpath+"_ie";
		Utilities.DeleteFolderWithContent(folder);
		Utilities.MakeDirectory(folder);
	}
	
	public Decomposition(String inpath,boolean newrun) {
		folder = inpath+"_ie";
		if(newrun){
		Utilities.DeleteFolderWithContent(folder);
		Utilities.MakeDirectory(folder);
		}
	}
	
	//TODO: Think about reading tables like PMC2361090 Table 2
	
	
	/**
	 * Checks recursively if is sequentially breaking line rows.
	 *
	 * @param cells the cells
	 * @param currentRowIndex the current row index
	 * @param currentColumnIndex the current column index
	 * @return true, if is sequentially breaking line
	 */
	public boolean isSequentiallyBreakingLine(Cell[][] cells,int currentRowIndex,int currentColumnIndex)
	{
		if(cells[currentRowIndex][currentColumnIndex].isBreakingLineOverRow() && currentRowIndex<2)
			return true;
		else if(cells[currentRowIndex][currentColumnIndex].isIs_header())
			return true;
		else if(cells[currentRowIndex][currentColumnIndex].isBreakingLineOverRow())
			return isSequentiallyBreakingLine(cells,currentRowIndex-2,currentColumnIndex);
		
		return false;
	}
	
	
	public boolean isMultiTable(Cell[][] cells,Table table)
	{
		if(cells==null)
			return false;
		for(int i = 0; i<cells.length;i++)
		{
			if(cells[i][0].isBreakingLineOverRow())
			{
				if(i-2>=0 && !cells[i-2][0].isIs_header() && !isSequentiallyBreakingLine(cells,i-2,0) && cells[i-2][0].isBreakingLineOverRow())
					return true;
			}
		}
		return false;
	}
	
	private Cell[][] markMultiTableHeaders(Cell[][] cells)
	{
		if(cells==null)
			return cells;
		for(int i = 0; i<cells.length;i++)
		{
			if(cells[i][0].isBreakingLineOverRow())
			{
				if(i-2>=0 && !cells[i-2][0].isIs_header() && !isSequentiallyBreakingLine(cells,i-2,0) && cells[i-2][0].isBreakingLineOverRow())
				{
					for(int j = 0; j<cells[i].length;j++)
					{
						cells[i][j].setIs_header(true);
					}
					for(int j = 0; j<cells[i].length;j++)
					{
						cells[i-1][j].setIs_header(true);
					}
					for(int j = 0; j<cells[i].length;j++)
					{
						cells[i-2][j].setIs_header(true);
					}
				}
			}
		}
		
		return cells;
	}
	
	/**
	 * Checks if is list table.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @return true, if is list table
	 */
	public boolean isListTable(Cell[][] cells,Table table)
	{
		if(cells==null || cells.length==0 || cells[0]==null||cells[0].length==0)
			return false;
		if((cells[0][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0][0].getCells_columnspanning()>=table.getNum_of_columns())||(table.getNum_of_columns()==1))
		{
			boolean isSame =true;
			for(int i = 0;i<cells[0].length-1;i++)
			{
				if(!cells[0][i].getCell_content().equals(cells[0][i+1]))
					isSame = false;
			}
			//TODO: Check whether columns are same type (mainly text) - maybe not necessary... seems to filter fine now!
			
			if(isSame)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if is row subheader.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @return true, if is row subheader
	 */
	public boolean isRowSubheader(Cell [] cells, Table table)
	{
		boolean emptyLine = true;
		for(int h = 0;h<cells.length;h++)
		{
			if(!Utilities.isSpaceOrEmpty(cells[h].getCell_content()))
			{
				emptyLine = false;
				break;
			}
		}
		if(emptyLine)
			return false;
		boolean isSubheader = false;
 		if(cells[0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0].getCells_columnspanning()>=table.getNum_of_columns() && !cells[0].getCell_content().trim().equalsIgnoreCase("") && !cells[0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[0].getCell_content().trim().charAt(0))== 160))
		{
			isSubheader = true;
		}
		boolean emptyCells = true;
		for(int j=1;j<cells.length;j++)
		{
			if(cells[j].getCell_content()==null)
			{
				cells[j].setCell_content("");
			}
			if((!Utilities.isSpaceOrEmpty(cells[0].getCell_content())  && !Utilities.isSpaceOrEmpty(cells[j].getCell_content())) || (Utilities.isSpaceOrEmpty(cells[0].getCell_content())  && !Utilities.isSpaceOrEmpty(cells[j].getCell_content())))
			{
				emptyCells = false;
			}
		}
		if(emptyCells == true)
		{
			isSubheader = true;
		}
		return isSubheader;
	}
	
	
	/**
	 * Checks for table subheader.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @return true, if successful
	 */
	public boolean hasTableSubheader(Cell [][] cells, Table table)
	{
		boolean hasSubheader = false;
		if(table.getNum_of_columns()<2)
			return false;
		for(int i = 1; i < cells.length;i++)
		{
			if(cells[i][0].isIs_header())
				continue;
			
			if(cells[i][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[i][0].getCells_columnspanning()>=table.getNum_of_columns() && !Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()))
			{
				hasSubheader = true;
				break;
			}
			//Tree structures
			if(Utilities.numOfBegeningSpaces(cells[i][0].getCell_content())>0)
			{
				hasSubheader = true;
				break;
			}
			boolean emptyCells = true;
			for(int j=1;j<cells[i].length;j++)
			{
				if(cells[i][j].getCell_content()==null)
				{
					cells[i][j].setCell_content("");
				}
				//TODO: Test, hopefully this does work
				if((!Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && !Utilities.isSpaceOrEmpty(cells[i][j].getCell_content()))||(Utilities.isSpaceOrEmpty(cells[i][0].getCell_content()) && Utilities.isSpaceOrEmpty(cells[i][j].getCell_content())))
				{
					emptyCells = false;
				}
				
			}
			if(emptyCells == true && table.getNum_of_columns()>2)
			{
				hasSubheader = true;
				break;
			}
		}
		return hasSubheader;
	}
	
	
	/**
	 * Gets the stack.
	 *
	 * @param stack the stack
	 * @param subheaderLevel the subheader level
	 * @return the stack
	 */
	public String getStack(String[] stack, int subheaderLevel)
	{
		String s = "";
		for(int i = 0;i<subheaderLevel;i++)
		{
			if(s.equals(""))
				s+=stack[i];
			else
				s+=", "+stack[i];
		}
		return s;
	}
	
	/**
	 * Gets the stack index. This is needed for annotation
	 *
	 * @param headerStackIndexes the header stack indexes
	 * @param prevSubheaderIndex the prev subheader index
	 * @param currentSubHeaderLevel the current sub header level
	 * @param cell the cell
	 * @param table the table
	 * @return the stack index
	 */
	public void getStackIndex(String[] headerStackIndexes,String prevSubheaderIndex,int currentSubHeaderLevel, Cell cell,Table table)
	{
		Cell[][] original_cells = table.original_cells;
		Cell OriginalCell = original_cells[cell.getRow_number()][cell.getColumn_number()];
		int l = 0;
		if(prevSubheaderIndex.length()>0)
		{
			
			OriginalCell.setSuperRowIndex(prevSubheaderIndex);
			l++;
		}
		for(int i = 0;i<currentSubHeaderLevel;i++)
		{
			if(currentSubHeaderLevel>1)
			{
				for(int s = 0;s<original_cells.length-1;s++)
				{
					if((""+s+"0").equals(headerStackIndexes[i]))
					{
						for(int d= 0;d<original_cells[s].length;d++)
						{
							if(i-1>=0 && headerStackIndexes[i-1]!=null)
								original_cells[s][d].setSuperRowIndex(headerStackIndexes[i-1]);
						}
					}
				}
			}
			if(headerStackIndexes[i]!=null)
				OriginalCell.setSuperRowIndex(headerStackIndexes[i]);
		}
		
		for(int i = 0;i<original_cells[cell.getRow_number()].length;i++)
		{
			original_cells[cell.getRow_number()][i].setSuperRowIndex(OriginalCell.getSuperRowIndex());
		}
	}
	
	/**
	 * Gets the stack as elements.
	 *
	 * @param stack the stack
	 * @param prevSubheader the prev subheader
	 * @param subheaderLevel the subheader level
	 * @param doc the doc
	 * @param stub the stub
	 * @return the stack as elements
	 */
	public Element getStackAsElements(String[] stack,String prevSubheader, int subheaderLevel,Document doc,Element stub)
	{
		//String s = "";
		int l = 0;
		if(prevSubheader.length()>0)
		{
			Element st = doc.createElement("SubHeader"+l);
			st.setTextContent(prevSubheader);
			stub.appendChild(st);
			l++;
		}
		for(int i = 0;i<subheaderLevel;i++)
		{

				Element st = doc.createElement("SubHeader"+(l+i));
				st.setTextContent(stack[i]);
				stub.appendChild(st);
			//	s+=", "+stack[i];
			
		}
		
		return stub;
	}
	
	
	/**
	 * Check headers. This function adds is header flag to the cells that are incorrectly marked (not marked as headers) and simplifies the table
	 *
	 * @param cells the cells
	 * @return the cell[][]
	 */
	private Table checkHeaders(Table table)
	{
		Cell[][] cells = table.cells;
		Cell[][] original_cells = table.original_cells;
		boolean prevWasHeader = false;
		//Check if has spanning in first columns
		for(int i = 0;i<cells.length;i++)
		{
			boolean isColSpanHeader = false;
			for(int j = 0;j<cells[i].length;j++)
			{
				if(cells[i][j].isIs_columnspanning())
					isColSpanHeader = true;
			}
			if(isColSpanHeader)
			{
				for(int j = 0;j<cells[i].length;j++)
				{
					cells[i][j].setIs_header(true);
					table.setHasHeader(true);
				}
			}
			if(prevWasHeader)
			{
				for(int j = 0;j<cells[i].length;j++)
				{
					cells[i][j].setIs_header(true);
					original_cells[cells[i][j].getRow_number()][cells[i][j].getColumn_number()].setIs_header(true);
				}
			}
			if(isColSpanHeader)
			{
				prevWasHeader = true;
			}
			else
			{
				prevWasHeader = false;
			}
			if(!isColSpanHeader && !prevWasHeader)
				break;
		}
		//Check for empty row
		int index = -1;
		for(int i = 0;i<cells.length;i++)
		{
			boolean isRowEmpty = true;
			for(int j=0;j<cells[i].length;j++)
			{
				if(!Utilities.isSpaceOrEmpty(cells[i][j].getCell_content()))
				{
					isRowEmpty = false;
					break;
				}

			}
			if(isRowEmpty && i>0)
			{
				index=i;
				break;
			}
		}
		for(int i = 0;i<index;i++)
		{
			for(int j=0;j<cells[i].length;j++)
			{
				cells[i][j].setIs_header(true);
				original_cells[cells[i][j].getRow_number()][cells[i][j].getColumn_number()].setIs_header(true);
				table.setHasHeader(true);
			}
		}
		//Similarity of cells
		int len = 0;
		if(cells.length>4)
			len = 4;
		else
			len = cells.length;
		boolean areSimilar = true;
		for(int i = 1;i<len;i++)
		{
			for(int j = 0;j<cells[i].length;j++)
			{
				if(cells[0][j]!= null && cells[i][j]!=null && cells[0][j].getCell_content()!=null & cells[i][j].getCell_content()!=null && Utilities.getCellTypeIsNum(cells[0][j].getCell_content()).equals(Utilities.getCellTypeIsNum(cells[i][j].getCell_content())))
				{
					areSimilar = false;
				}
			}
		}
		if (areSimilar) {
			for (int j = 0; j < cells[0].length; j++) {
				cells[0][j].setIs_header(true);
				original_cells[cells[0][j].getRow_number()][cells[0][j].getColumn_number()].setIs_header(true);
			}
		}
		for(int i = 0;i<cells.length;i++)
		{
			if(cells[i].length==0)
				continue;
			if(cells[i][0].isIs_header())
				table.stat.setNum_of_header_rows(table.stat.getNum_of_header_rows()+1);
			if(cells[i][0].isIs_header()==false)
				break;
		}
		//table = TableSimplifier.MergeHeaders(table);
		return table;
	}
	
	/**
	 * Process table with subheaders without header.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @param art the art
	 * @param tableFileName the table file name
	 */
	private Table processTableWithSubheadersWithoutHeader(Cell[][] cells,Table table, Article art, String tableFileName)
	{
		if(!hasTableSubheader(cells,table))
		{
			return table;
		}
		if(table.getTableStructureType()!=Table.StructureType.SUBHEADER && table.getTableStructureType()!=null)
		{
			return table;
		}
		
		Statistics.addSubheaderTable();
		table.setTableStructureType(Table.StructureType.SUBHEADER);
		if(TablInExMain.ExportLinkedData){
			TablInExMain.linkedData.AddTable(table.getTable_title(), table.getTable_caption(), "SubHeader", "", table.getTable_footer(), table.getXml());
		}
		String[] headerStackA = new String[20];
		String[] headerStackIndexes = new String[20];
		int currentSubHeaderLevel = 0; //number of levels
		String prevSubheader = "";
		String prevSubheaderIndex="";
		boolean hasSpaceSubheaders = false;
		if(table.isHasHeader()==false)
		{
			table = checkHeaders(table);
		}
		cells = table.cells;
		Cell[][] original_cells = table.original_cells;
		for(int j=0;j<cells.length;j++)
		{
			if(cells[j][0].isIs_header())
				continue;
			boolean emptyLine = true;
			for(int h = 0;h<cells[j].length;h++)
			{
				if(!Utilities.isSpaceOrEmpty(cells[j][h].getCell_content()))
				{
					emptyLine = false;
					break;
				}
			}
			if(emptyLine)
				continue;
			//Record headers in spanning structure
			if(cells[j][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[j][0].getCells_columnspanning()>=table.getNum_of_columns())
			{
				if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel){
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
				}else
				{
					currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
				}
				SetUnderSubheaderRow(cells[j]);
				SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				continue;
			}
			boolean emptyCells = true;
			//check if row has all empty cells except first
			for(int h=1;h<cells[j].length;h++)
			{
				if(cells[j][h].getCell_content()==null)
				{
					cells[j][h].setCell_content("");
				}
				if(Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) || !Utilities.isSpaceOrEmpty(cells[j][h].getCell_content()))
				{
					emptyCells = false;
				}

			}
			//If it has all empty cells, except firts it is header
			if(emptyCells){
				
				if(currentSubHeaderLevel!=0 && currentSubHeaderLevel == j-1)
				{
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
					{
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						currentSubHeaderLevel++;
						SetUnderSubheaderRow(cells[j]);
						SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
						//sequalHeaders++;
					}
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						currentSubHeaderLevel++;
						SetUnderSubheaderRow(cells[j]);
						SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
						//sequalHeaders++;
					}
				}
				else
				{					
				if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
				{
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				}
				else
				{
					currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content(); 
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				}
				if(j>0 && isRowSubheader(cells[j-1], table))
				{
					prevSubheader = cells[j-1][0].getCell_content();
					prevSubheaderIndex = cells[j-1][0].getRow_number()+cells[j-1][0].getColumn_number()+"";
				}
				continue;					
				}

			}
			//If row is a subheader, don't recrod values
			if(isRowSubheader(cells[j],table))
			{
				if(j>0 && isRowSubheader(cells[j-1], table))
				{
					prevSubheader = cells[j-1][0].getCell_content();
					prevSubheaderIndex = cells[j-1][0].getRow_number()+cells[j-1][0].getColumn_number()+"";
				}
				continue;
			}

			if(cells[j][0].getCell_content()==null)
			{
				cells[j][0].setCell_content("");
			}
			//Other levels of subheaders with possibly filled cells.
				if((cells!=null&&cells[j]!=null&&cells[j][0]!=null&&cells[j][0].getCell_content()!=null)&&(cells[j][0].getCell_content().length()>0 && Utilities.isSpace(cells[j][0].getCell_content().trim().charAt(0)))||Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) )
				{
					hasSpaceSubheaders = true;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel||Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()))
						{
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					}else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						
					}
				}
				else
				{
					if(hasSpaceSubheaders)
						currentSubHeaderLevel = 0;
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					
				}

			for(int k=1;k<cells[j].length;k++)
			{
				
				try{ 
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

					//root elements
					Document doc = docBuilder.newDocument();

					Element rootElement = doc.createElement("information");
					doc.appendChild(rootElement);
					Element cell = doc.createElement("Cell");
					rootElement.appendChild(cell);
					
					Element NavigationPath = doc.createElement("NavigationPath");
					if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()) && cells[0][0].isIs_header())
					{
						Element Head00 = doc.createElement("Head00");
						Head00.setTextContent(cells[0][0].getCell_content());
						cells[j][k].setHead00(cells[0][0].getCell_content());
						NavigationPath.appendChild(Head00);
					}
					Element Stub = doc.createElement("Stub");								
					
					if(currentSubHeaderLevel>0)
					{
						getStackAsElements(headerStackA,prevSubheader, currentSubHeaderLevel, doc, Stub);
						getStackIndex(headerStackIndexes,prevSubheaderIndex,currentSubHeaderLevel,cells[j][k],table);
						
					}
					String subheaderValues = "";
					String[] SubHeaders = new String[Stub.getChildNodes().getLength()] ;
					for(int l=0;l<Stub.getChildNodes().getLength();l++)
					{
						subheaderValues+=" "+Stub.getChildNodes().item(l).getTextContent();
						SubHeaders[l]= Stub.getChildNodes().item(l).getTextContent();
					}
					cells[j][k].setSubheader_values(subheaderValues);
					Element ss = doc.createElement("StubValue");
					ss.setTextContent(cells[j][0].getCell_content());
					cells[j][k].setStub_values(cells[j][0].getCell_content());
					Stub.appendChild(ss);
					
					NavigationPath.appendChild(Stub);
					if(cells[0][k].isIs_header()){
						for(int s = 0;s<cells[0][k].headers.size();s++){
							Element Header = doc.createElement("HeaderValue"+s);
							Header.setTextContent(cells[0][k].headers.get(s));
							cells[j][k].setHeader_values(cells[0][k].getCell_content());
							NavigationPath.appendChild(Header);
						}
					}
					cell.appendChild(NavigationPath);
					
					//info elements
					Element info = doc.createElement("value");
					info.setTextContent(cells[j][k].getCell_content());
					cell.appendChild(info);
					
					Element CellType = doc.createElement("CellType");
					CellType.setTextContent(cells[j][k].getCellType());
					cell.appendChild(CellType);
					
					Element tableEl = doc.createElement("Table");
					rootElement.appendChild(tableEl);
					
					Element tname = doc.createElement("tableName");
					tname.setTextContent(table.getTable_caption());
					tableEl.appendChild(tname);
					
					Element TableType = doc.createElement("TableType");
					TableType.setTextContent("Subheader");
					tableEl.appendChild(TableType);
					

					
					Element torder = doc.createElement("tableOrder");
					torder.setTextContent(table.getTable_title());
					tableEl.appendChild(torder);
					
					Element tfooter = doc.createElement("tableFooter");
					tfooter.setTextContent(table.getTable_footer());
					tableEl.appendChild(tfooter);
					
					Element document = doc.createElement("Document");
					rootElement.appendChild(document);
					
					Element docTitle = doc.createElement("DocumentTitle");
					docTitle.setTextContent(art.getTitle());
					document.appendChild(docTitle);
					
					Element pmc = doc.createElement("PMC");
					pmc.setTextContent(art.getPmc());
					document.appendChild(pmc);
					if(TablInExMain.ExportLinkedData){
						TablInExMain.linkedData.AddCell(cells[j][0].getCell_content(), SubHeaders, cells[j][k].getCell_content(), cells[j][k].getCellType(),  cells[0][k].headers.toArray(new String[0]), cells[0][0].getCell_content(), j, k);					
					}
					DataExtractionOutputObj dataExtObj = new DataExtractionOutputObj(folder+tableFileName+"e"+j+","+k+".xml", doc);
					//TablInExMain.outputs.add(dataExtObj);
					table.output.add(dataExtObj);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		table.cells = cells;
		return table;
		
	}

	public boolean hasSuperRowsListTable(Cell[][] cells, Table table)
	{
		boolean hasTopLevel = false;
		boolean hasLowLevel = false;
		for(int i = 0; i < cells.length;i++)
		{
			if(Utilities.numOfBegeningSpaces(cells[i][0].getCell_content())==0)
				hasTopLevel = true;
			if(Utilities.numOfSpaceOrBullets(cells[i][0].getCell_content())>0)
				hasLowLevel = true;
		}
		
		if(hasLowLevel && hasTopLevel)
			return true;
		
		return false;
	}
	
	/**
	 * Process list table. List table is a table that forms data in a list.
	 * It can have multiple columns for space saving, but generally it is one dimensional list
	 *
	 * @param cells the table object. 2 dimensional array of Cell object
	 * @param table Table object
	 * @param art the Article object
	 * @param tableFileName the table file name
	 */
	public Table processListTable(Cell[][] cells,Table table, Article art, String tableFileName){
		if(cells==null || cells.length==0 || cells[0]==null||cells[0].length==0)
			return table;
		if(table.getTableStructureType()!=Table.StructureType.LIST && table.getTableStructureType()!=null)
		{
			return table;
		}
		
		if(isListTable(cells, table))
		{
			Statistics.addListTable();
			table.setTableStructureType(Table.StructureType.LIST);
			if(TablInExMain.ExportLinkedData){
				TablInExMain.linkedData.AddTable(table.getTable_title(), table.getTable_caption(), "List", "", table.getTable_footer(), table.getXml());
			}
			boolean hasSubheaders = hasSuperRowsListTable(cells, table);
			boolean TopLevel = false;
			String currentSubHeader = "";
			String SubHeaderIndex = "";
			for(int j=0;j<cells.length;j++)
			{
				for(int k=0;k<cells[j].length;k++)
				{
					if(cells[j][k].isIs_header())
						continue;
					if(cells[j][k].isIs_stub())
						cells[j][k].setIs_stub(false);
					try{
						
						if(hasSubheaders)
						{
							if(TopLevel==false && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && cells[j+1]!= null && Utilities.numOfSpaceOrBullets(cells[j+1][k].getCell_content())!=0 && currentSubHeader.equals(""))
							{
								TopLevel = true;
								currentSubHeader=cells[j][k].getCell_content();
								cells[j][k].setIs_subheader(true);
								SubHeaderIndex = j+"."+k;
								continue;
							}
							if(TopLevel==false && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && (cells[j+2]!= null && Utilities.numOfSpaceOrBullets(cells[j+2][k].getCell_content())!=0) && currentSubHeader.equals(""))
							{
								TopLevel = true;
								currentSubHeader=cells[j][k].getCell_content();
								cells[j][k].setIs_subheader(true);
								SubHeaderIndex = j+"."+k;
								continue;
							}
							if(TopLevel == true && !cells[j][k].isBreakingLineOverRow() && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && !currentSubHeader.equals(""))
							{
								currentSubHeader=cells[j][k].getCell_content();
								cells[j][k].setIs_subheader(true);
								SubHeaderIndex = j+"."+k;
								continue;
							}
						}
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

						//root elements
						Document doc = docBuilder.newDocument();

						Element rootElement = doc.createElement("information");
						doc.appendChild(rootElement);
						
						Element cell = doc.createElement("Cell");
						rootElement.appendChild(cell);
						
						Element NavigationPath = doc.createElement("NavigationPath");
						for(int s = 0;s<cells[0][k].headers.size();s++){
							Element Header = doc.createElement("HeaderValue"+s);
							Header.setTextContent(cells[0][k].headers.get(s));
						
							NavigationPath.appendChild(Header);
						}
						if(currentSubHeader!=""){
						Element SubHeader = doc.createElement("SubHeader");
						SubHeader.setTextContent(currentSubHeader);
						cells[j][k].setSuperRowIndex(SubHeaderIndex);
						NavigationPath.appendChild(SubHeader);
						}
						cell.appendChild(NavigationPath);
						cells[j][k].setHeader_values(cells[0][k].getCell_content());
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						cell.appendChild(info);
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						cell.appendChild(CellType);
						
						Element tableA = doc.createElement("Table");
						rootElement.appendChild(tableA);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						tableA.appendChild(tname);
						
						Element TableType = doc.createElement("TableType");
						TableType.setTextContent("List");
						tableA.appendChild(TableType);			
						
						Element torder = doc.createElement("tableOrder");
						torder.setTextContent(table.getTable_title());
						tableA.appendChild(torder);
						
						Element tfooter = doc.createElement("tableFooter");
						tfooter.setTextContent(table.getTable_footer());
						tableA.appendChild(tfooter);
						
						Element document = doc.createElement("Document");
						rootElement.appendChild(document);
						
						Element docTitle = doc.createElement("DocumentTitle");
						docTitle.setTextContent(art.getTitle());
						document.appendChild(docTitle);
						
						Element pmc = doc.createElement("PMC");
						pmc.setTextContent(art.getPmc());
						document.appendChild(pmc);
						if(TablInExMain.ExportLinkedData){
							TablInExMain.linkedData.AddCell("", null, cells[j][k].getCell_content(), cells[j][k].getCellType(), cells[0][k].headers.toArray(new String[0]), "", j, k);
						}
						DataExtractionOutputObj dataExtObj = new DataExtractionOutputObj(folder+tableFileName+"e"+j+","+k+".xml", doc);
						//TablInExMain.outputs.add(dataExtObj);
						table.output.add(dataExtObj);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		table.cells = cells;
		table.original_cells = cells;
		return table;
	}
	
	/**
	 * Process regular table.
	 *
	 * @param cells the cells
	 * @param tables the tables
	 * @param art the art
	 * @param tableFileName the table file name
	 * @param tableindex the tableindex
	 */
	public Table processRegularTable(Cell[][] cells, Table[] tables, Article art, String tableFileName, int tableindex)
	{
		if(tables[tableindex].getTableStructureType()!=Table.StructureType.MATRIX && tables[tableindex].getTableStructureType()!=null)
		{
			return tables[tableindex];
		}
		Statistics.addMatrixTable();
		tables[tableindex].setTableStructureType(Table.StructureType.MATRIX);
		if(TablInExMain.ExportLinkedData){
			TablInExMain.linkedData.AddTable(tables[tableindex].getTable_title(), tables[tableindex].getTable_caption(), "Matrix", "", tables[tableindex].getTable_footer(), tables[tableindex].getXml());
		}
		if(!tables[tableindex].isHasHeader())
		{
			tables[tableindex] = checkHeaders(tables[tableindex]);
			cells = tables[tableindex].cells;
		}

		for(int j=0;j<cells.length;j++)
		{
			if(cells[j].length==0 || cells[j][0].isIs_header())
				continue;
			for(int k=1;k<cells[j].length;k++)
			{
				try{
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

					//root elements
					Document doc = docBuilder.newDocument();

					Element rootElement = doc.createElement("information");
					doc.appendChild(rootElement);
					
					Element cell = doc.createElement("Cell");
					rootElement.appendChild(cell);
					
					Element NavigationPath = doc.createElement("NavigationPath");
					if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()) && cells[0][0].isIs_header()){
					Element TopLeftHeader = doc.createElement("Head00");
					TopLeftHeader.setTextContent(cells[0][0].getCell_content());
					cells[j][k].setHead00(cells[0][0].getCell_content());
					NavigationPath.appendChild(TopLeftHeader);
					}
					if(!Utilities.isSpaceOrEmpty(cells[j][0].getCell_content())){
						Element Stub = doc.createElement("Stub");
						Element StubValue = doc.createElement("StubValue");
						StubValue.setTextContent(cells[j][0].getCell_content());
						cells[j][k].setStub_values(cells[j][0].getCell_content());
						Stub.appendChild(StubValue);
						NavigationPath.appendChild(Stub);
						}
					if(!Utilities.isSpaceOrEmpty(cells[0][k].getCell_content()) && cells[0][k].isIs_header()){
						//Element HeaderValue = doc.createElement("HeaderValue");
						//HeaderValue.setTextContent(cells[0][k].getCell_content());
						//cells[j][k].setHeader_values(cells[0][k].getCell_content());
						//NavigationPath.appendChild(HeaderValue);
						for(int s = 0;s<cells[0][k].headers.size();s++){
							Element Header = doc.createElement("HeaderValue"+s);
							Header.setTextContent(cells[0][k].headers.get(s));
							cells[j][k].setHeader_values(cells[0][k].getCell_content());
							NavigationPath.appendChild(Header);
						}
						}
					
					cell.appendChild(NavigationPath);
					
					//info elements
					Element info = doc.createElement("value");
					info.setTextContent(cells[j][k].getCell_content());
					cell.appendChild(info);
					
					Element CellType = doc.createElement("CellType");
					CellType.setTextContent(cells[j][k].getCellType());
					cell.appendChild(CellType);
					
					Element tableA = doc.createElement("Table");
					rootElement.appendChild(tableA);
					
					Element TableType = doc.createElement("TableType");
					TableType.setTextContent("Matrix");
					tableA.appendChild(TableType);
										
					Element tname = doc.createElement("tableName");
					tname.setTextContent(tables[tableindex].getTable_caption());
					tableA.appendChild(tname);
					
					Element torder = doc.createElement("tableOrder");
					torder.setTextContent(tables[tableindex].getTable_title());
					tableA.appendChild(torder);
					
					Element tfooter = doc.createElement("tableFooter");
					tfooter.setTextContent(tables[tableindex].getTable_footer());
					tableA.appendChild(tfooter);
					
					Element document = doc.createElement("Document");
					rootElement.appendChild(document);
					
					Element docTitle = doc.createElement("DocumentTitle");
					docTitle.setTextContent(art.getTitle());
					document.appendChild(docTitle);
					
					Element pmc = doc.createElement("PMC");
					pmc.setTextContent(art.getPmc());
					document.appendChild(pmc);
					if(TablInExMain.ExportLinkedData){
						TablInExMain.linkedData.AddCell(cells[j][0].getCell_content(), null, cells[j][k].getCell_content(), cells[j][k].getCellType(), cells[0][k].headers.toArray(new String[0]), cells[0][0].getCell_content(), j, k);					
					}				
					DataExtractionOutputObj dataExtObj = new DataExtractionOutputObj(folder+tableFileName+"e"+j+","+k+".xml", doc);
					//TablInExMain.outputs.add(dataExtObj);
					tables[tableindex].output.add(dataExtObj);
					

										

				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		return tables[tableindex];
	}
	
	public void SetUnderSubheaderRow(Cell[] row)
	{
		for(int i = 0; i < row.length;i++)
		{
			row[i].setUnderSubheader(true);
		}
	}
	
	
	public Table processMultiTable(Cell[][] cells,Table table, Article art, String tableFileName){
		if(table.getTableStructureType()!=Table.StructureType.MULTI && table.getTableStructureType()!=null)
		{
			return table;
		}
		Statistics.addMultiTable();
		table.setTableStructureType(Table.StructureType.MULTI);
		if(TablInExMain.ExportLinkedData){
			TablInExMain.linkedData.AddTable(table.getTable_title(), table.getTable_caption(), "MultiTable", "", table.getTable_footer(), table.getXml());
		}
		if(!table.isHasHeader())
		{
			table = checkHeaders(table);
			cells = table.cells;
		}
		
		table.original_cells = markMultiTableHeaders(table.original_cells);
		table.cells = markMultiTableHeaders(table.cells);
		//table.original_cells = table.cells;
		cells = table.cells;
		Cell[][] original_cells = table.original_cells;
		if(cells[0].length>1){

		String[] headerStackA = new String[20];
		String[] headerStackIndexes = new String[20];
		int currentSubHeaderLevel = 0; //number of levels
		String prevSubheader = "";
		String prevSubheaderIndex="";
		boolean hasSpaceSubheaders = false;
		if(table.isHasHeader()==false)
		{
			table = checkHeaders(table);
		}

		cells = table.cells;
		boolean firstHeader = true;
		for(int j=0;j<cells.length;j++)
		{
			if(cells[j][0].isIs_header()){
					continue;
			}
			boolean emptyLine = true;
			for(int h = 0;h<cells[j].length;h++)
			{
				if(!Utilities.isSpaceOrEmpty(cells[j][h].getCell_content()))
				{
					emptyLine = false;
					break;
				}
			}
			if(emptyLine)
				continue;
			//Record headers in spanning structure
			if(cells[j][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[j][0].getCells_columnspanning()>=table.getNum_of_columns())
			{
				if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel){
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
				}else
				{
					currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
				}
				SetUnderSubheaderRow(cells[j]);
				SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				continue;
			}
			boolean emptyCells = true;
			//check if row has all empty cells except first
			for(int h=1;h<cells[j].length;h++)
			{
				if(cells[j][h].getCell_content()==null)
				{
					cells[j][h].setCell_content("");
				}
				if(Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) || !Utilities.isSpaceOrEmpty(cells[j][h].getCell_content()))
				{
					emptyCells = false;
				}

			}
			//If it has all empty cells, except firts it is header
			if(emptyCells){
				
				if(currentSubHeaderLevel!=0 && currentSubHeaderLevel == j-1)
				{
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
					{
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						currentSubHeaderLevel++;
						SetUnderSubheaderRow(cells[j]);
						SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
						//sequalHeaders++;
					}
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						currentSubHeaderLevel++;
						SetUnderSubheaderRow(cells[j]);
						SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
						//sequalHeaders++;
					}
				}
				else
				{					
				if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
				{
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				}
				else
				{
					currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content(); 
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					currentSubHeaderLevel++;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
				}
				if(j>0 && isRowSubheader(cells[j-1], table))
				{
					prevSubheader = cells[j-1][0].getCell_content();
					prevSubheaderIndex = cells[j-1][0].getRow_number()+cells[j-1][0].getColumn_number()+"";
				}
				continue;					
				}

			}
			//If row is a subheader, don't recrod values
			if(isRowSubheader(cells[j],table))
			{
				if(j>0 && isRowSubheader(cells[j-1], table))
				{
					prevSubheader = cells[j-1][0].getCell_content();
					prevSubheaderIndex = cells[j-1][0].getRow_number()+cells[j-1][0].getColumn_number()+"";
				}
				continue;
			}

			if(cells[j][0].getCell_content()==null)
			{
				cells[j][0].setCell_content("");
			}
			//Other levels of subheaders with possibly filled cells.
				if((cells!=null&&cells[j]!=null&&cells[j][0]!=null&&cells[j][0].getCell_content()!=null)&&(cells[j][0].getCell_content().length()>0 && Utilities.isSpace(cells[j][0].getCell_content().trim().charAt(0)))||Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) )
				{
					hasSpaceSubheaders = true;
					SetUnderSubheaderRow(cells[j]);
					SetUnderSubheaderRow(original_cells[cells[j][0].getRow_number()]);
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel||Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()))
						{headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						}else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
						headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
						
					}
				}
				else
				{
					if(hasSpaceSubheaders)
						currentSubHeaderLevel = 0;
					headerStackA[currentSubHeaderLevel] = cells[j][0].getCell_content();
					headerStackIndexes[currentSubHeaderLevel] = ""+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getRow_number()+"."+original_cells[cells[j][0].getRow_number()][cells[j][0].getColumn_number()].getColumn_number();
					
					
				}

			for(int k=1;k<cells[j].length;k++)
			{
				
				try{ 
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

					//root elements
					Document doc = docBuilder.newDocument();

					Element rootElement = doc.createElement("information");
					doc.appendChild(rootElement);
					Element cell = doc.createElement("Cell");
					rootElement.appendChild(cell);
					
					Element NavigationPath = doc.createElement("NavigationPath");
					String Head00Str = "";
					if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()) && cells[0][0].isIs_header())
					{
						
						boolean foundHeader = false;
						for(int l = j;l>=0;l--)
						{
							if(cells[l][0].isIs_header())
							{
								if(!cells[l][0].getCell_content().equals(""))
									Head00Str = cells[l][0].getCell_content();
								foundHeader = true;
							}
							if(foundHeader && !cells[l][0].isIs_header())
							{
								break;
							}
						}
						
						Element Head00 = doc.createElement("Head00");
						Head00.setTextContent(Head00Str);
						cells[j][k].setHead00(Head00Str);
						NavigationPath.appendChild(Head00);
					}
					Element Stub = doc.createElement("Stub");								
					
					if(currentSubHeaderLevel>0)
					{
						getStackAsElements(headerStackA,prevSubheader, currentSubHeaderLevel, doc, Stub);
						getStackIndex(headerStackIndexes,prevSubheaderIndex,currentSubHeaderLevel,cells[j][k],table);
						
					}
					String subheaderValues = "";
					String[] SubHeaders = new String[Stub.getChildNodes().getLength()] ;
					for(int l=0;l<Stub.getChildNodes().getLength();l++)
					{
						subheaderValues+=" "+Stub.getChildNodes().item(l).getTextContent();
						SubHeaders[l]= Stub.getChildNodes().item(l).getTextContent();
					}
					cells[j][k].setSubheader_values(subheaderValues);
					Element ss = doc.createElement("StubValue");
					ss.setTextContent(cells[j][0].getCell_content());
					cells[j][k].setStub_values(cells[j][0].getCell_content());
					Stub.appendChild(ss);
					
					NavigationPath.appendChild(Stub);
					LinkedList<String> headers = new LinkedList<String>();
					boolean foundHeader = false;
					for(int l = j;l>=0;l--)
					{
						if(cells[l][k].isIs_header())
						{
							if(!cells[l][k].getCell_content().equals(""))
							headers.addFirst(cells[l][k].getCell_content());
							foundHeader = true;
						}
						if(foundHeader && !cells[l][k].isIs_header())
						{
							break;
						}
					}
					
						for(int s = 0;s<headers.size();s++){
							Element Header = doc.createElement("HeaderValue"+s);
							Header.setTextContent(headers.get(s));
							cells[j][k].setHeader_values(headers.toString());
							NavigationPath.appendChild(Header);
						}			
					
					cell.appendChild(NavigationPath);
					
					//info elements
					Element info = doc.createElement("value");
					info.setTextContent(cells[j][k].getCell_content());
					cell.appendChild(info);
					
					Element CellType = doc.createElement("CellType");
					CellType.setTextContent(cells[j][k].getCellType());
					cell.appendChild(CellType);
					
					Element tableEl = doc.createElement("Table");
					rootElement.appendChild(tableEl);
					
					Element tname = doc.createElement("tableName");
					tname.setTextContent(table.getTable_caption());
					tableEl.appendChild(tname);
					
					Element TableType = doc.createElement("TableType");
					TableType.setTextContent("Multi-Table");
					tableEl.appendChild(TableType);
					

					
					Element torder = doc.createElement("tableOrder");
					torder.setTextContent(table.getTable_title());
					tableEl.appendChild(torder);
					
					Element tfooter = doc.createElement("tableFooter");
					tfooter.setTextContent(table.getTable_footer());
					tableEl.appendChild(tfooter);
					
					Element document = doc.createElement("Document");
					rootElement.appendChild(document);
					
					Element docTitle = doc.createElement("DocumentTitle");
					docTitle.setTextContent(art.getTitle());
					document.appendChild(docTitle);
					
					Element pmc = doc.createElement("PMC");
					pmc.setTextContent(art.getPmc());
					document.appendChild(pmc);
					if(TablInExMain.ExportLinkedData){
						TablInExMain.linkedData.AddCell(cells[j][0].getCell_content(), SubHeaders, cells[j][k].getCell_content(), cells[j][k].getCellType(),  cells[0][k].headers.toArray(new String[0]), cells[0][0].getCell_content(), j, k);					
					}
					DataExtractionOutputObj dataExtObj = new DataExtractionOutputObj(folder+tableFileName+"e"+j+","+k+".xml", doc);
					//TablInExMain.outputs.add(dataExtObj);
					table.output.add(dataExtObj);
					
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		}
		else
		{
			//List table
			boolean hasSubheaders = hasSuperRowsListTable(cells, table);
			boolean TopLevel = false;
			String currentSubHeader = "";
			for(int j=0;j<cells.length;j++)
			{
				for(int k=0;k<cells[j].length;k++)
				{
					if(cells[j][k].isIs_header())
						continue;
					try{
						
						if(hasSubheaders)
						{
							if(TopLevel==false && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && cells[j+1]!= null && Utilities.numOfSpaceOrBullets(cells[j+1][k].getCell_content())!=0 && currentSubHeader.equals(""))
							{
								TopLevel = true;
								currentSubHeader=cells[j][k].getCell_content();
								continue;
							}
							if(TopLevel==false && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && (cells[j+2]!= null && Utilities.numOfSpaceOrBullets(cells[j+2][k].getCell_content())!=0) && currentSubHeader.equals(""))
							{
								TopLevel = true;
								currentSubHeader=cells[j][k].getCell_content();
								continue;
							}
							if(TopLevel == true && !cells[j][k].isBreakingLineOverRow() && Utilities.numOfSpaceOrBullets(cells[j][k].getCell_content())==0 && !currentSubHeader.equals(""))
							{
								currentSubHeader=cells[j][k].getCell_content();
								continue;
							}
						}
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

						//root elements
						Document doc = docBuilder.newDocument();

						Element rootElement = doc.createElement("information");
						doc.appendChild(rootElement);
						
						Element cell = doc.createElement("Cell");
						rootElement.appendChild(cell);
						
						Element NavigationPath = doc.createElement("NavigationPath");
						
						
						LinkedList<String> headers = new LinkedList<String>();
						boolean foundHeader = false;
						for(int l = j;l>=0;l--)
						{
							if(cells[l][k].isIs_header())
							{
								if(!cells[l][k].getCell_content().equals(""))
								headers.addFirst(cells[l][k].getCell_content());
								foundHeader = true;
							}
							if(foundHeader && !cells[l][k].isIs_header())
							{
								break;
							}
						}
						
							for(int s = 0;s<headers.size();s++){
								Element Header = doc.createElement("HeaderValue"+s);
								Header.setTextContent(headers.get(s));
								cells[j][k].setHeader_values(headers.toString());
								NavigationPath.appendChild(Header);
							}			
						
						
						if(currentSubHeader!=""){
						Element SubHeader = doc.createElement("SubHeader");
						SubHeader.setTextContent(currentSubHeader);
						NavigationPath.appendChild(SubHeader);
						}
						cell.appendChild(NavigationPath);
						cells[j][k].setHeader_values(cells[0][k].getCell_content());
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						cell.appendChild(info);
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						cell.appendChild(CellType);
						
						Element tableA = doc.createElement("Table");
						rootElement.appendChild(tableA);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						tableA.appendChild(tname);
						
						Element TableType = doc.createElement("TableType");
						TableType.setTextContent("Multi-Table");
						tableA.appendChild(TableType);			
						
						Element torder = doc.createElement("tableOrder");
						torder.setTextContent(table.getTable_title());
						tableA.appendChild(torder);
						
						Element tfooter = doc.createElement("tableFooter");
						tfooter.setTextContent(table.getTable_footer());
						tableA.appendChild(tfooter);
						
						Element document = doc.createElement("Document");
						rootElement.appendChild(document);
						
						Element docTitle = doc.createElement("DocumentTitle");
						docTitle.setTextContent(art.getTitle());
						document.appendChild(docTitle);
						
						Element pmc = doc.createElement("PMC");
						pmc.setTextContent(art.getPmc());
						document.appendChild(pmc);
						if(TablInExMain.ExportLinkedData){
							TablInExMain.linkedData.AddCell("", null, cells[j][k].getCell_content(), cells[j][k].getCellType(), cells[0][k].headers.toArray(new String[0]), "", j, k);
						}
						DataExtractionOutputObj dataExtObj = new DataExtractionOutputObj(folder+tableFileName+"e"+j+","+k+".xml", doc);
						//TablInExMain.outputs.add(dataExtObj);
						table.output.add(dataExtObj);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		
			
		}
		table.cells = cells;
		return table;
	}

	/**
	 * Extract information from simple tables.
	 *
	 * @param art the art
	 */
	public void ExtractData(Article art)
	{
		if(art==null)
			return;
		TablInExMain.linkedData.AddArticle(art);
		Table[] tables = art.getTables();
		for(int i = 0; i< tables.length;i++)
		{
			if(tables[i]==null || tables[i].cells==null || tables[i].cells.length==0)
				continue;
			//only simple tables
			//if( tables[i].getStructureClass()!=2 && tables[i].getStructureClass()!=1 && tables[i].getStructureClass()!=3 && tables[i].getStructureClass()!=4)
				//continue;
			
			String tableFileName = "/"+tables[i].getDocumentFileName()+tables[i].getTable_title()+"-"+tables[i].tableInTable;
			Cell[][] cells = tables[i].cells;
			
			//Temporaty, don't process Multi tables! TODO: Add processing for multitables;
			if(isMultiTable(cells, tables[i]))
			{
				tables[i] = processMultiTable(cells,tables[i], art, tableFileName);
				
			}
			if(tables[i].getTableStructureType()!=null && tables[i].getTableStructureType().equals(Table.StructureType.MULTI))
				continue;
			tables[i] = processListTable(cells,tables[i], art, tableFileName);
			tables[i] = processTableWithSubheadersWithoutHeader(cells,tables[i],art,tableFileName);//processTableWithSubheaders(cells,tables[i],art,tableFileName);
			if(!isListTable(cells, tables[i]) && !hasTableSubheader(cells, tables[i]))
			{
				tables[i] = processRegularTable(cells,  tables, art, tableFileName, i);
			}
		}
		art.setTables(tables);
		
	}


}
