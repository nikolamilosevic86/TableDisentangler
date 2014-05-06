/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package IE;

import java.io.File;

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
	public void processTableWithSubheaders(Cell[][] cells,Table table, Article art, String tableFileName)
	{
		if(hasTableSubheader(cells,table))
		{
			for(int j=1;j<cells.length;j++)
			{
				if(cells[j][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[j][0].getCells_columnspanning()>=table.getNum_of_columns())
				{
					continue;
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
						
						Element attribute = doc.createElement("attribute");
						String subheaderVal = "";
						for(int l=j;l>=0;l--)
						{
							if(cells[l][0].isIs_columnspanning() && table.getNum_of_columns()>1 && cells[l][0].getCells_columnspanning()>=table.getNum_of_columns() && !cells[l][0].getCell_content().trim().equalsIgnoreCase("") && !cells[l][0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[l][0].getCell_content().trim().charAt(0))== 160))
							{
								subheaderVal = cells[l][0].getCell_content().trim();
								break;
							}
							boolean emptyCells = true;
							for(int h=1;h<cells[l].length;h++)
							{
								
								if(!cells[l][0].getCell_content().trim().equalsIgnoreCase("") && !cells[l][0].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[l][0].getCell_content().trim().charAt(0))== 160) && (!cells[l][h].getCell_content().trim().equalsIgnoreCase("") && !cells[l][h].getCell_content().trim().equalsIgnoreCase(" ") && !(((int)cells[l][h].getCell_content().trim().charAt(0))== 160)))
								{
									emptyCells = false;
								}
							}
							if(emptyCells == true)
							{
								subheaderVal = cells[l][0].getCell_content();
								break;
							}
						}
						if(subheaderVal.equals(""))
							attribute.setTextContent(cells[0][0].getCell_content()+";"+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
						else
							attribute.setTextContent(cells[0][0].getCell_content()+";"+subheaderVal+":"+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
						
						rootElement.appendChild(attribute);
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						rootElement.appendChild(info);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						rootElement.appendChild(tname);
						
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
						
						Element attribute = doc.createElement("attribute");
						attribute.setTextContent(cells[0][k].getCell_content());
						rootElement.appendChild(attribute);
						
						//info elements
						Element info = doc.createElement("value");
						info.setTextContent(cells[j][k].getCell_content());
						rootElement.appendChild(info);
						
						Element tname = doc.createElement("tableName");
						tname.setTextContent(table.getTable_caption());
						rootElement.appendChild(tname);
						
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
					
					Element attribute = doc.createElement("attribute");
					attribute.setTextContent(cells[0][0].getCell_content()+";"+cells[j][0].getCell_content()+";"+cells[0][k].getCell_content());
					rootElement.appendChild(attribute);
					
					//info elements
					Element info = doc.createElement("value");
					info.setTextContent(cells[j][k].getCell_content());
					rootElement.appendChild(info);
					
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
			if( tables[i].getStructureClass()!=2)
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
