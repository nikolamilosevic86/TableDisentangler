/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package IE;

import java.io.File;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import stats.Statistics;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;
import tablInEx.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleIE. Does simple Information extraction. 
 */
public class SimpleIE {
	
	/** The folder. */
	private static String folder;
	
	/**
	 * Instantiates a new simple ie.
	 *
	 * @param inpath the inpath
	 */
	public SimpleIE(String inpath) {
		// TODO Auto-generated constructor stub
		folder = inpath+"_ie";
		Utilities.DeleteFolderWithContent(folder);
		Utilities.MakeDirectory(folder);
	}
	
	//TODO: Think about reading tables like PMC2361090 Table 2
	
	
	/**
	 * Checks if is list table.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @return true, if is list table
	 */
	public boolean isListTable(Cell[][] cells,Table table)
	{
		if(cells[0][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0][0].getCells_columnspanning()>=table.getNum_of_columns())
		{
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
			if(!Utilities.isSpaceOrEmpty(cells[0].getCell_content())  && !Utilities.isSpaceOrEmpty(cells[j].getCell_content()))
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
			if(cells[i][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[i][0].getCells_columnspanning()>=table.getNum_of_columns() && !cells[i][0].getCell_content().trim().equalsIgnoreCase("") && !cells[i][0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[i][0].getCell_content().trim().charAt(0))== 160))
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
				if(!cells[i][0].getCell_content().trim().equalsIgnoreCase("") && !cells[i][0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[i][0].getCell_content().trim().charAt(0))== 160) && (!cells[i][j].getCell_content().trim().equalsIgnoreCase("") && !cells[i][j].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[i][j].getCell_content().trim().charAt(0))== 160)))
				{
					emptyCells = false;
				}
			}
			if(emptyCells == true)
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
	 * Gets the stack as elements.
	 *
	 * @param stack the stack
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
	 * Process table with subheaders.
	 *
	 * @param cells the cells
	 * @param table the table
	 * @param art the art
	 * @param tableFileName the table file name
	 */
	public void processTableWithSubheaders(Cell[][] cells,Table table, Article art, String tableFileName)
	{
		if(hasTableSubheader(cells,table))
		{
			Statistics.addSubheaderTable();
			String[] headerStackA = new String[20];
			//int sequalHeaders = 0;
			int currentSubHeaderLevel = 0; //number of levels
			String prevSubheader = "";
			for(int j=1;j<cells.length;j++)
			{
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
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
						headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
					}
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
							headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
							//sequalHeaders++;
						}
						else
						{
							currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
							headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
							//sequalHeaders++;
						}
					}
					else
					{					
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
					{
						headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
						//sequalHeaders ++;
					}
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
						headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content(); 
				//		sequalHeaders++;
					}
					if(isRowSubheader(cells[j-1], table))
					{

						prevSubheader = cells[j-1][0].getCell_content();
					}
					continue;					
					}

				}
//				else
//				{
//					//sequalHeaders = 0;
//				}
				//If row is a subheader, don't recrod values
				if(isRowSubheader(cells[j],table))
				{
					if(isRowSubheader(cells[j-1], table))
					{
						prevSubheader = cells[j-1][0].getCell_content();
					}
					continue;
				}
//				if(cells[j-1][0].getCell_content()!= null && cells[j-1][0].getCell_content().length()>0 && Utilities.isSpace(cells[j-1][0].getCell_content().trim().charAt(0)) )
//				{
//					currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content());
//					headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
//				}
				//Other levels of subheaders with possibly filled cells.
					if(cells[j][0].getCell_content().length()>0 && Utilities.isSpace(cells[j][0].getCell_content().trim().charAt(0)) )
					{
						if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel+1)
							headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
						else
						{
							currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())-1;
							headerStackA[currentSubHeaderLevel++] = cells[j][0].getCell_content();
						}
					}
					else
					{

						currentSubHeaderLevel = 0;
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
						if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
						{
							Element Head00 = doc.createElement("Head00");
							Head00.setTextContent(cells[0][0].getCell_content());
							NavigationPath.appendChild(Head00);
						}
						Element Stub = doc.createElement("Stub");								
						
					//	Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
						Element s = doc.createElement("HeaderValue");
						s.setTextContent(cells[0][k].getCell_content());
						NavigationPath.appendChild(s);
						

						
						if(currentSubHeaderLevel>0)
						{
							getStackAsElements(headerStackA,prevSubheader, currentSubHeaderLevel, doc, Stub);
							
						}
						Element ss = doc.createElement("StubValue");
						ss.setTextContent(cells[j][0].getCell_content());
						Stub.appendChild(ss);
						
						NavigationPath.appendChild(Stub);
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
						
											
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);

						StreamResult result =  new StreamResult(new File(folder+tableFileName+"e"+j+","+k+".xml"));
						transformer.transform(source, result);
						
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
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
	public void processListTable(Cell[][] cells,Table table, Article art, String tableFileName){
		if((cells[0][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0][0].getCells_columnspanning()>=table.getNum_of_columns())||(table.getNum_of_columns()==1))
		{
			Statistics.addListTable();
			for(int j=1;j<cells.length;j++)
			{
				for(int k=0;k<cells[j].length;k++)
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
						//attribute.setTextContent(cells[0][k].getCell_content());
						Element Header = doc.createElement("HeaderValue");
						Header.setTextContent(cells[0][k].getCell_content());
						NavigationPath.appendChild(Header);
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
						
											
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource(doc);

						StreamResult result =  new StreamResult(new File(folder+tableFileName+"e"+j+","+k+".xml"));
						transformer.transform(source, result);
					}catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
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
	public void processRegularTable(Cell[][] cells, Table[] tables, Article art, String tableFileName, int tableindex)
	{
		Statistics.addMatrixTable();
		for(int j=1;j<cells.length;j++)
		{
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
					
					// TODO: Make attribute NavigationPath and make it structured
					Element NavigationPath = doc.createElement("NavigationPath");
					if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content())){
					Element TopLeftHeader = doc.createElement("Head00");
					TopLeftHeader.setTextContent(cells[0][0].getCell_content());
					NavigationPath.appendChild(TopLeftHeader);
					}
					if(!Utilities.isSpaceOrEmpty(cells[j][0].getCell_content())){
						Element Stub = doc.createElement("Stub");
						Element StubValue = doc.createElement("StubValue");
						StubValue.setTextContent(cells[j][0].getCell_content());
						Stub.appendChild(StubValue);
						NavigationPath.appendChild(Stub);
						}
					if(!Utilities.isSpaceOrEmpty(cells[0][k].getCell_content())){
						Element HeaderValue = doc.createElement("HeaderValue");
						HeaderValue.setTextContent(cells[0][k].getCell_content());
						NavigationPath.appendChild(HeaderValue);
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
					
										
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);

					StreamResult result =  new StreamResult(new File(folder+tableFileName+"e"+j+","+k+".xml"));
					transformer.transform(source, result);
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Extract information from simple tables.
	 *
	 * @param art the art
	 */
	public void ExtractInformation(Article art)
	{
		if(art==null)
			return;
		Table[] tables = art.getTables();
		for(int i = 0; i< tables.length;i++)
		{
			//only simple tables
			if( tables[i].getStructureClass()!=2 && tables[i].getStructureClass()!=1)
				continue;
			
			String tableFileName = "/"+tables[i].getDocumentFileName()+tables[i].getTable_title()+"-"+tables[i].tableInTable;
			Cell[][] cells = tables[i].cells;
			
			processListTable(cells,tables[i], art, tableFileName);
			//processTableWithGroupedSubheaders(cells,tables[i],art,tableFileName);
			processTableWithSubheaders(cells,tables[i],art,tableFileName);
			if(!isListTable(cells, tables[i]) && !hasTableSubheader(cells, tables[i]))
			{
				processRegularTable(cells,  tables, art, tableFileName, i);
			}
		}
		
	}


}
