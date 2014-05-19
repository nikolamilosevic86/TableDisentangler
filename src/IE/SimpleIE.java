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
	
	
	public boolean isListTable(Cell[][] cells,Table table)
	{
		if(cells[0][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0][0].getCells_columnspanning()>=table.getNum_of_columns())
		{
			return true;
		}
		return false;
	}
	
	public boolean isRowSubheader(Cell [] cells, Table table)
	{
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
			if(!cells[0].getCell_content().trim().equalsIgnoreCase("") && !cells[0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[0].getCell_content().trim().charAt(0))== 160) && (!cells[j].getCell_content().trim().equalsIgnoreCase("") && !cells[j].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[j].getCell_content().trim().charAt(0))== 160)))
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
	
	
	public boolean hasTableSubheader(Cell [][] cells, Table table)
	{
		boolean hasSubheader = false;
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
	
	public Element getStackAsElements(String[] stack, int subheaderLevel,Document doc,Element stub)
	{
		//String s = "";
		for(int i = 0;i<subheaderLevel;i++)
		{

				Element st = doc.createElement("SubHeader"+i);
				st.setTextContent(stack[i]);
				stub.appendChild(st);
			//	s+=", "+stack[i];
			
		}
		return stub;
	}
	
	//TODO: Redo, comment, do something with this!!!
	public void processTableWithSubheaders(Cell[][] cells,Table table, Article art, String tableFileName)
	{
		if(hasTableSubheader(cells,table))
		{
			String[] headerStack = new String[10];
			int currentSubHeaderLevel = -1;
			int subHeaderValIndex = 0;
			boolean hadsubheader = false;
			boolean valueSeparator = false;
			boolean subheaderTableWIthValSeparators = false;
			String subheaderVal = "";
			String subHeaderValLevelUp = "";
			String prevRowHeader = "";
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
				if(cells[j][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[j][0].getCells_columnspanning()>=table.getNum_of_columns())
				{
					subheaderVal = cells[j][0].getCell_content()+": ";
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel)
						headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())-1;
						headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
					}
						
					continue;
				}
				boolean emptyCells = true;

				for(int h=1;h<cells[j].length;h++)
				{
					if(cells[j][h].getCell_content()==null)
					{
						cells[j][h].setCell_content("");
					}
					if(Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) || !Utilities.isSpaceOrEmpty(cells[j][h].getCell_content()))
					{
						emptyCells = false;
						prevRowHeader = cells[j-1][0].getCell_content();
					}

				}
				if(emptyCells){
					if(subHeaderValIndex!=0 && subHeaderValIndex == j-1)
					{
						subHeaderValLevelUp = subheaderVal;
						subheaderVal = cells[j][0].getCell_content()+": ";
						if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel+1)
							headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
						else
						{
							currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())-1;
							headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
						}
					}
					else
					{
					subheaderVal = cells[j][0].getCell_content()+": ";
					
					if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel+1)
						headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
					else
					{
						currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())-1;
						headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
					}
					subHeaderValIndex = j;
					continue;					
					}

				}
				if(isRowSubheader(cells[j],table))
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
						
						Element ReadingPath = doc.createElement("ReadingPath");
						
						if(cells[j][0].getCell_content().length()>0 && Utilities.isSpace(cells[j][0].getCell_content().trim().charAt(0)) )
						{
							if(Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())==currentSubHeaderLevel+1)
								headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
							else
							{
								currentSubHeaderLevel = Utilities.numOfBegeningSpaces(cells[j][0].getCell_content())-1;
								headerStack[++currentSubHeaderLevel] = cells[j][0].getCell_content();
							}
							valueSeparator = true;
							hadsubheader = true;
							subheaderTableWIthValSeparators = true;
						}
						else
						{
							valueSeparator = false;
							hadsubheader = false;
							if(subheaderTableWIthValSeparators && !Utilities.isSpaceOrEmpty(cells[j][0].getCell_content()) && Utilities.isSpaceOrEmpty(subHeaderValLevelUp))
								subheaderVal = "";
						}
						
						if((valueSeparator == true && (Utilities.isSpace(cells[j][0].getCell_content().trim().charAt(0)))) || (hadsubheader == true && valueSeparator == false)){
							if(cells[j][0].getCell_content()=="")
							{
								//attribute.setTextContent(cells[0][0].getCell_content()+";"+subHeaderValLevelUp+ " "+subheaderVal+prevRowHeader+";"+cells[0][k].getCell_content());
								if( Arrays.asList(Arrays.copyOfRange(headerStack, 0, currentSubHeaderLevel)).contains(subHeaderValLevelUp.substring(0,subHeaderValLevelUp.length()-2)))
								{
									if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
									{
										Element Head00 = doc.createElement("Head00");
										Head00.setTextContent(cells[0][0].getCell_content());
										ReadingPath.appendChild(Head00);
									}
									Element Stub = doc.createElement("Stub");
									Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
									Element prev = doc.createElement("Subheader"+currentSubHeaderLevel);
									prev.setTextContent(prevRowHeader);
									Stub.appendChild(prev);
									Element s = doc.createElement("HeaderValue");
									s.setTextContent(cells[0][k].getCell_content());
									Stub.appendChild(s);
									
									ReadingPath.appendChild(Stub);
									//attribute.setTextContent(cells[0][0].getCell_content()+";"+getStack(headerStack, currentSubHeaderLevel)+prevRowHeader+";"+cells[0][k].getCell_content());
								}
								else	
								{
									if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
									{
										Element Head00 = doc.createElement("Head00");
										Head00.setTextContent(cells[0][0].getCell_content());
										ReadingPath.appendChild(Head00);
									}
									Element Stub = doc.createElement("Stub");
									Element subHeadValLevelUp = doc.createElement("SubLevelUp");
									subHeadValLevelUp.setTextContent(subHeaderValLevelUp);
									Stub.appendChild(subHeadValLevelUp);
									Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
									Element prev = doc.createElement("Subheader"+currentSubHeaderLevel);
									prev.setTextContent(prevRowHeader);
									Stub.appendChild(prev);
									Element s = doc.createElement("HeaderValue");
									s.setTextContent(cells[0][k].getCell_content());
									Stub.appendChild(s);
									
									ReadingPath.appendChild(Stub);
									//attribute.setTextContent(cells[0][0].getCell_content()+";"+subHeaderValLevelUp+getStack(headerStack, currentSubHeaderLevel)+prevRowHeader+";"+cells[0][k].getCell_content());
								}
							}else
							{
								if(subHeaderValLevelUp.length()>2 && Arrays.asList(Arrays.copyOfRange(headerStack, 0, currentSubHeaderLevel)).contains(subHeaderValLevelUp.substring(0,subHeaderValLevelUp.length()-2)))
								{
									if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
									{
										Element Head00 = doc.createElement("Head00");
										Head00.setTextContent(cells[0][0].getCell_content());
										ReadingPath.appendChild(Head00);
									}
									Element Stub = doc.createElement("Stub");
									Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
									Element s = doc.createElement("HeaderValue");
									s.setTextContent(cells[0][k].getCell_content());
									ReadingPath.appendChild(s);
									
									Element ss = doc.createElement("StubValue");
									ss.setTextContent(cells[j][0].getCell_content());
									Stub.appendChild(ss);
									
									ReadingPath.appendChild(Stub);
									//attribute.setTextContent(cells[0][0].getCell_content()+";"+getStack(headerStack, currentSubHeaderLevel)+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
								}
								else
								{
									if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
									{
										Element Head00 = doc.createElement("Head00");
										Head00.setTextContent(cells[0][0].getCell_content());
										ReadingPath.appendChild(Head00);
									}
									Element Stub = doc.createElement("Stub");
									if(!Utilities.isSpaceOrEmpty(subHeaderValLevelUp)){
									Element subHeaderLevelUp = doc.createElement("SubHeaderLevelUp");
									subHeaderLevelUp.setTextContent(subHeaderValLevelUp);
									Stub.appendChild(subHeaderLevelUp);
									}
									
									
									Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
									Element s = doc.createElement("HeaderValue");
									s.setTextContent(cells[0][k].getCell_content());
									ReadingPath.appendChild(s);
									
									Element ss = doc.createElement("StubValue");
									ss.setTextContent(cells[j][0].getCell_content());
									Stub.appendChild(ss);
									
									ReadingPath.appendChild(Stub);
									//attribute.setTextContent(cells[0][0].getCell_content()+";"+subHeaderValLevelUp+getStack(headerStack, currentSubHeaderLevel)+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());

								}
							}
						}
						else
						{

						if(subheaderVal.equals("")){
							if(cells[j][0].getCell_content()!="")
							{
								if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
								{
									Element Head00 = doc.createElement("Head00");
									Head00.setTextContent(cells[0][0].getCell_content());
									ReadingPath.appendChild(Head00);
								}
								Element Stub = doc.createElement("Stub");								
								
							//	Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
								Element s = doc.createElement("HeaderValue");
								s.setTextContent(cells[0][k].getCell_content());
								ReadingPath.appendChild(s);
								
								Element ss = doc.createElement("StubValue");
								ss.setTextContent(cells[j][0].getCell_content());
								Stub.appendChild(ss);
								
								ReadingPath.appendChild(Stub);
								//attribute.setTextContent(cells[0][0].getCell_content()+";"+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
							}
							else
							{
								if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
								{
									Element Head00 = doc.createElement("Head00");
									Head00.setTextContent(cells[0][0].getCell_content());
									ReadingPath.appendChild(Head00);
								}
								Element Stub = doc.createElement("Stub");								
								
								//Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
								Element s = doc.createElement("HeaderValue");
								s.setTextContent(cells[0][k].getCell_content());
								ReadingPath.appendChild(s);
								
								Element ss = doc.createElement("StubValue");
								ss.setTextContent(prevRowHeader);
								Stub.appendChild(ss);
								
								ReadingPath.appendChild(Stub);
							//	attribute.setTextContent(cells[0][0].getCell_content()+";"+prevRowHeader+";"+cells[0][k].getCell_content());
								
							}
							}else{
								if(cells[j][0].getCell_content()!="")
								{
									if( subHeaderValLevelUp.length()>2 && Arrays.asList(Arrays.copyOfRange(headerStack, 0, currentSubHeaderLevel)).contains(subHeaderValLevelUp.substring(0,subHeaderValLevelUp.length()-2)))
									{
										if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
										{
											Element Head00 = doc.createElement("Head00");
											Head00.setTextContent(cells[0][0].getCell_content());
											ReadingPath.appendChild(Head00);
										}
										Element Stub = doc.createElement("Stub");		
										
										Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
										Element s = doc.createElement("HeaderValue");
										s.setTextContent(cells[0][k].getCell_content());
										ReadingPath.appendChild(s);
										
										Element ss = doc.createElement("StubValue");
										ss.setTextContent(cells[j][0].getCell_content());
										Stub.appendChild(ss);
										
										ReadingPath.appendChild(Stub);
									//	attribute.setTextContent(cells[0][0].getCell_content()+";"+getStack(headerStack, currentSubHeaderLevel)+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
									}
									else
									{
										if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
										{
											Element Head00 = doc.createElement("Head00");
											Head00.setTextContent(cells[0][0].getCell_content());
											ReadingPath.appendChild(Head00);
										}
										Element Stub = doc.createElement("Stub");
										if(!Utilities.isSpaceOrEmpty(subHeaderValLevelUp)){
										Element subHeaderLevelUp = doc.createElement("SubHeaderLevelUp");
										subHeaderLevelUp.setTextContent(subHeaderValLevelUp);
										Stub.appendChild(subHeaderLevelUp);
										}
										
										
										Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
										Element s = doc.createElement("HeaderValue");
										s.setTextContent(cells[0][k].getCell_content());
										ReadingPath.appendChild(s);
										
										Element ss = doc.createElement("StubValue");
										ss.setTextContent(cells[j][0].getCell_content());
										Stub.appendChild(ss);
										
										ReadingPath.appendChild(Stub);
										//attribute.setTextContent(cells[0][0].getCell_content()+";"+subHeaderValLevelUp+getStack(headerStack, currentSubHeaderLevel)+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
									}
								}
								else
								{
									if( subHeaderValLevelUp.length()>2 && Arrays.asList(Arrays.copyOfRange(headerStack, 0, currentSubHeaderLevel)).contains(subHeaderValLevelUp.substring(0,subHeaderValLevelUp.length()-2)))
									{
										if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
										{
											Element Head00 = doc.createElement("Head00");
											Head00.setTextContent(cells[0][0].getCell_content());
											ReadingPath.appendChild(Head00);
										}
										Element Stub = doc.createElement("Stub");
								
										Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
										Element prev = doc.createElement("StubValue");
										prev.setTextContent(prevRowHeader);
										Stub.appendChild(prev);
										Element s = doc.createElement("HeaderValue");
										s.setTextContent(cells[0][k].getCell_content());
										ReadingPath.appendChild(s);
										
//										Element ss = doc.createElement("StubValue");
//										ss.setTextContent(cells[j][0].getCell_content());
//										Stub.appendChild(ss);
										
										ReadingPath.appendChild(Stub);
										//attribute.setTextContent(cells[0][0].getCell_content()+";"+getStack(headerStack, currentSubHeaderLevel)+":"+prevRowHeader+";"+cells[0][k].getCell_content());
									}
									else
									{
										if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content()))
										{
											Element Head00 = doc.createElement("Head00");
											Head00.setTextContent(cells[0][0].getCell_content());
											ReadingPath.appendChild(Head00);
										}
										Element Stub = doc.createElement("Stub");
										Element subHeadValLevelUp = doc.createElement("SubLevelUp");
										subHeadValLevelUp.setTextContent(subHeaderValLevelUp);
										Stub.appendChild(subHeadValLevelUp);
										Stub = getStackAsElements(headerStack, currentSubHeaderLevel, doc, Stub);
										Element prev = doc.createElement("Subheader"+currentSubHeaderLevel);
										prev.setTextContent(prevRowHeader);
										Stub.appendChild(prev);
										Element s = doc.createElement("HeaderValue");
										s.setTextContent(cells[0][k].getCell_content());
										Stub.appendChild(s);
										
										ReadingPath.appendChild(Stub);
									//	attribute.setTextContent(cells[0][0].getCell_content()+";"+subHeaderValLevelUp+getStack(headerStack, currentSubHeaderLevel)+":"+prevRowHeader+";"+cells[0][k].getCell_content());
									}
								}
							}
						}
						rootElement.appendChild(ReadingPath);
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						rootElement.appendChild(info);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						rootElement.appendChild(tname);
						
						Element TableType = doc.createElement("TableType");
						TableType.setTextContent("Subheader");
						rootElement.appendChild(TableType);
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						rootElement.appendChild(CellType);
						
						Element torder = doc.createElement("tableOrder");
						torder.setTextContent(table.getTable_title());
						rootElement.appendChild(torder);
						
						Element tfooter = doc.createElement("tableFooter");
						tfooter.setTextContent(table.getTable_footer());
						rootElement.appendChild(tfooter);
						
						Element docTitle = doc.createElement("DocumentTitle");
						docTitle.setTextContent(art.getTitle());
						rootElement.appendChild(docTitle);
						
						Element pmc = doc.createElement("PMC");
						pmc.setTextContent(art.getPmc());
						rootElement.appendChild(pmc);
						
											
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
		if(cells[0][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[0][0].getCells_columnspanning()>=table.getNum_of_columns())
		{
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
						
						Element ReadingPath = doc.createElement("ReadingPath");
						//attribute.setTextContent(cells[0][k].getCell_content());
						Element Header = doc.createElement("Header");
						Header.setTextContent(cells[0][k].getCell_content());
						ReadingPath.appendChild(Header);
						rootElement.appendChild(ReadingPath);
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						rootElement.appendChild(info);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						rootElement.appendChild(tname);
						
						Element TableType = doc.createElement("TableType");
						TableType.setTextContent("List");
						rootElement.appendChild(TableType);
						
						Element CellType = doc.createElement("CellType");
						CellType.setTextContent(cells[j][k].getCellType());
						rootElement.appendChild(CellType);
						
						Element torder = doc.createElement("tableOrder");
						torder.setTextContent(table.getTable_title());
						rootElement.appendChild(torder);
						
						Element tfooter = doc.createElement("tableFooter");
						tfooter.setTextContent(table.getTable_footer());
						rootElement.appendChild(tfooter);
						
						Element docTitle = doc.createElement("DocumentTitle");
						docTitle.setTextContent(art.getTitle());
						rootElement.appendChild(docTitle);
						
						Element pmc = doc.createElement("PMC");
						pmc.setTextContent(art.getPmc());
						rootElement.appendChild(pmc);
						
											
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
	
	public void processRegularTable(Cell[][] cells, Table[] tables, Article art, String tableFileName, int tableindex)
	{
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
					// TODO: Make attribute ReadingPath and make it structured
					Element ReadingPath = doc.createElement("ReadingPath");
					if(!Utilities.isSpaceOrEmpty(cells[0][0].getCell_content())){
					Element TopLeftHeader = doc.createElement("Head00");
					TopLeftHeader.setTextContent(cells[0][0].getCell_content());
					ReadingPath.appendChild(TopLeftHeader);
					}
					if(!Utilities.isSpaceOrEmpty(cells[j][0].getCell_content())){
						Element StubValue = doc.createElement("StubValue");
						StubValue.setTextContent(cells[j][0].getCell_content());
						ReadingPath.appendChild(StubValue);
						}
					if(!Utilities.isSpaceOrEmpty(cells[0][k].getCell_content())){
						Element HeaderValue = doc.createElement("HeaderValue");
						HeaderValue.setTextContent(cells[0][k].getCell_content());
						ReadingPath.appendChild(HeaderValue);
						}
					
					rootElement.appendChild(ReadingPath);
					
					//info elements
					Element info = doc.createElement("value");
					info.setTextContent(cells[j][k].getCell_content());
					rootElement.appendChild(info);
					
					Element TableType = doc.createElement("TableType");
					TableType.setTextContent("Matrix");
					rootElement.appendChild(TableType);
					
					Element CellType = doc.createElement("CellType");
					CellType.setTextContent(cells[j][k].getCellType());
					rootElement.appendChild(CellType);
					
					Element tname = doc.createElement("tableName");
					tname.setTextContent(tables[tableindex].getTable_caption());
					rootElement.appendChild(tname);
					
					Element torder = doc.createElement("tableOrder");
					torder.setTextContent(tables[tableindex].getTable_title());
					rootElement.appendChild(torder);
					
					Element tfooter = doc.createElement("tableFooter");
					tfooter.setTextContent(tables[tableindex].getTable_footer());
					rootElement.appendChild(tfooter);
					
					Element docTitle = doc.createElement("DocumentTitle");
					docTitle.setTextContent(art.getTitle());
					rootElement.appendChild(docTitle);
					
					Element pmc = doc.createElement("PMC");
					pmc.setTextContent(art.getPmc());
					rootElement.appendChild(pmc);
					
										
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
			processTableWithSubheaders(cells,tables[i],art,tableFileName);
			if(!isListTable(cells, tables[i]) && !hasTableSubheader(cells, tables[i]))
			{
				processRegularTable(cells,  tables, art, tableFileName, i);
			}
		}
		
	}


}
