package readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXParseException;

import stats.Statistics;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;
//import Utils.Author;
import Utils.Utilities;

public class HTMLEasyPDFConverterReader  implements Reader{
	private String FileName;
	
	public void init(String file_name)
	{
		setFileName(file_name);
	}
	
	/**
	 * This method is the main method for reading PMC XML files. It uses {@link #ParseMetaData} and {@link #ParseTables} methods.
	 * It returns {@link Article} object that contains structured data from article, including tables.
	 * @return Article
	 */
	public Article Read()
	{
		Article art =  new Article(FileName);
		art.setSource("easyPDF2HTML");
		try{
		if(FileName == null || FileName.equals(""))
			return art;
		
		if(!FileName.contains(".html"))
			return art;
		@SuppressWarnings("resource")
		FileReader fr = new FileReader(FileName);
		if(fr==null)
			return art;
		BufferedReader reader = new BufferedReader(fr);
		String line = null;
		String xml = "";
		while ((line = reader.readLine()) != null) {
			if(line.contains("JATS-archivearticle1.dtd")||line.contains("archivearticle.dtd")||line.contains("strict.dtd"))
				continue;
			if(line.toLowerCase().contains("meta"))
			{
				String[] splitted = line.split("><");
				line = "";
				for(String s : splitted)
				{
					line=s+"</META>";
				}
			}
			if(line.toLowerCase().contains("img"))
			{
				String[] splitted = line.split("><");
				line = "";
				for(String s : splitted)
				{
					line=s+"</IMG>";
				}
			}
		    xml +=line+'\n';
		}		
		
	    Document parse =  Jsoup.parse(xml);//builder.parse(is);
	    art = ParseMetaData(art, parse, xml);
		art = ParseTables(art,parse);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return art;
	}
	
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}

	
	
	
	
	
	/**
	 * Reads metadata from article such as title, authors, publication type etc
	 * @param art - Article where to put data
	 * @param parse - Document of XML 
	 * @param xml - XML code
	 * @return Article - populated art
	 */
	public Article ParseMetaData(Article art, Document parse, String xml)
	{
	    
	    try{
	    if(parse.getElementsByTag("body")!=null)
	    {
	    String plain_text = parse.getElementsByTag("body").text();
	    art.setPlain_text(plain_text);
	    }
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }

	    art.setTitle(art.getFile_name());
	    art.setXML(xml);
		return art;
	}
	
	/**
	 * Read table label.
	 *
	 * @param tablexmlNode the tablexml node
	 * @return the string
	 */
	public String readTableLabel(Element tablexmlNode)
	{
		String label = "Table without label";
		List<Element> nl = getChildrenByTagName(tablexmlNode,"label");
		if(nl.size()>0)
		{
			label = Utilities.getString(nl.get(0));
		}
		//TODO: Obtain label as the first p tag before table tag
		
		return label;
	}
	
	/**
	 * Read table caption.
	 *
	 * @param tablexmlNode the tablexml node
	 * @return the string
	 */
	public String readTableCaption(Element tablexmlNode)
	{
		//TODO: Obtain label as the first p tag before table tag
		String caption = "";
		List<Element>nl = getChildrenByTagName(tablexmlNode,"caption");
		if(nl.size()>0){
			caption = Utilities.getString(nl.get(0));
		}
		Elements inDivChild = tablexmlNode.parent().getAllElements().first().children();
		for(int i =0;i<inDivChild.size();i++){
			Element el = inDivChild.get(i);
			if(i+1<inDivChild.size()){
				Element tableel = inDivChild.get(i+1);
				if(tableel.tagName().equals("table")){
					caption = el.text();
				}
			}
		
		}
		nl = getChildrenByTagName(tablexmlNode,"title");
		if(nl.size()>0){
			caption = Utilities.getString(nl.get(0));
		}
		return caption;
	}
	
	/**
	 * Read table footer.
	 *
	 * @param tablesxmlNode the tablesxml node
	 * @return the string
	 */
	public String ReadTableFooter(Element tablesxmlNode)
	{
		//TODO: Obtain label as the first p tag before table tag
				String caption = "";
				List<Element>nl = getChildrenByTagName(tablesxmlNode,"caption");
				if(nl.size()>0){
					caption = Utilities.getString(nl.get(0));
				}
				nl = getChildrenByTagName(tablesxmlNode,"p");
				if(nl.size()>0){
					for(int i=0;i<nl.size();i++){
					caption += Utilities.getString(nl.get(i))+'\n';
					}
				}
				nl = getChildrenByTagName(tablesxmlNode,"title");
				if(nl.size()>0){
					caption = Utilities.getString(nl.get(0));
				}
				return caption;
	}
	
	/**
	 * Count columns.
	 *
	 * @param rowsbody the rowsbody
	 * @param rowshead the rowshead
	 * @return the int
	 */
	public int CountColumns(List<Element> rowsbody,List<Element> rowshead)
	{
		int cols=0;
		int headrowscount= 0;
		if(rowshead!=null)
			headrowscount = rowshead.size();
		for(int row = 0;row<rowsbody.size();row++)
		{
			int cnt=0;
			List<Element> tds = getChildrenByTagName(rowsbody.get(row), "td");
			for(int k=0;k<tds.size();k++)
			{
				if(tds.get(k).attr("colspan")!=null && Utilities.getFirstValue(tds.get(k).attr("colspan"))>1)
				{
					cnt+=Utilities.getFirstValue(tds.get(k).attr("colspan"));
				}
				else
				{
					cnt++;
				}
			}
			cols = Math.max(cols,cnt);
		}
		if(headrowscount!=0)
		{
			List<Element> tdsh =  getChildrenByTagName(rowshead.get(0), "td");
			if(tdsh.size()==0){
				tdsh =  getChildrenByTagName(rowshead.get(0), "th");
			}
		cols = Math.max(cols, tdsh.size());
		}
		
		return cols;
	}
	
	/**
	 * Process table header.
	 *
	 * @param table the table
	 * @param cells the cells
	 * @param rowshead the rowshead
	 * @param headrowscount the headrowscount
	 * @param num_of_columns the num_of_columns
	 * @return the table
	 */
	public Table ProcessTableHeader(Article a,Table table, Cell[][] cells,List<Element> rowshead,int headrowscount,int num_of_columns)
	{
		for(int j = 0;j<headrowscount;j++)
		{

			Statistics.addHeaderRow();
			table.stat.AddHeaderRow();
			List<Element> tds = getChildrenByTagName(rowshead.get(j), "td");
			if(tds.size()==0)
				tds = getChildrenByTagName(rowshead.get(j), "th");
			int index = 0;
			//read cells
			for(int k = 0;k<tds.size();k++)
			{		
				table.stat.AddUnprCell();
				table.stat.AddHeaderCell();
				boolean is_colspanning = false;
				boolean is_rowspanning = false;
				int colspanVal = 1;
				int rowspanVal = 1;
				if(tds.get(k).attr("rowspan")!=null && Utilities.isNumeric(tds.get(k).attr("rowspan")) && Utilities.getFirstValue(tds.get(k).attr("rowspan"))>1)
				{
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal =  Utilities.getFirstValue(tds.get(k).attr("rowspan"));											
				}
				//colspan
				if(tds.get(k).attr("colspan")!=null && Utilities.isNumeric(tds.get(k).attr("colspan")) && Utilities.getFirstValue(tds.get(k).attr("colspan"))>1)
				{
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal =  Utilities.getFirstValue(tds.get(k).attr("colspan"));					
				}

				for(int l=0;l<colspanVal;l++)
				{
					int rowindex = j;
					for(int s =0;s<rowspanVal;s++)
					{
						try
						{
							while(cells[rowindex][index].isIs_filled() && index!=num_of_columns)
								index++;
							cells[rowindex][index] = Cell.setCellValues(a,cells[rowindex][index], Utilities.getString(tds.get(k)), is_colspanning, colspanVal, is_rowspanning, rowspanVal, true, 1, false, 0, index,rowindex, l, s);
							//System.out.println(j+","+index+": "+cells[j][index].getCell_content());
							table = Statistics.statisticsForCell(table, cells[rowindex][index]);
						}
						catch(Exception ex)
						{
							System.out.println("Error: Table is spanning more then it is possible");
						}
						rowindex++;
					}
					index++;
				}					
			}//end for tds.size()
		}// end for rowheads
		return table;
	}
	
	/**
	 * Process table body.
	 *
	 * @param table the table
	 * @param cells the cells
	 * @param rowsbody the rowsbody
	 * @param headrowscount the headrowscount
	 * @param num_of_columns the num_of_columns
	 * @return the table
	 */ 
	public Table ProcessTableBody(Article a, Table table, Cell[][] cells,List<Element> rowsbody,int headrowscount, int num_of_columns)
	{
		int startj = headrowscount;
		//boolean tablecounted = false;
		for(int j = 0;j<rowsbody.size();j++)
		{
			table.stat.AddBodyRow();
			List<Element> tds = getChildrenByTagName(rowsbody.get(j), "td");
			int index = 0;
			int rowindex = startj;
			for(int k = 0;k<tds.size();k++)
			{
				table.stat.AddUnprCell();
				List<Element> hr = getChildrenByTagName(tds.get(k), "hr");
				boolean isStub = false;
				float stubProbability =0;
				
				if(index ==0)
				{
					isStub = true;
					stubProbability = (float) 0.9;
				}
				
				
				boolean is_colspanning = false;
				boolean is_rowspanning = false;
				int colspanVal = 1;
				int rowspanVal = 1;
				if(tds.get(k).attr("rowspan")!=null && Utilities.isNumeric(tds.get(k).attr("rowspan")) && Utilities.getFirstValue(tds.get(k).attr("rowspan"))>1)
				{
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal = Utilities.getFirstValue(tds.get(k).attr("rowspan"));											
				}
				//colspan
				if(tds.get(k).attr("colspan")!=null && Utilities.isNumeric(tds.get(k).attr("colspan")) && Utilities.getFirstValue(tds.get(k).attr("colspan"))>1)
				{
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal = Utilities.getFirstValue(tds.get(k).attr("colspan"));					
				}
				for(int l=0;l<colspanVal;l++)
				{
					rowindex = startj+j;
					for(int s =0;s<rowspanVal;s++)
					{
						try
						{
							while(cells[rowindex][index].isIs_filled() && index!=num_of_columns)
								index++;
							cells[rowindex][index] = Cell.setCellValues(a,cells[rowindex][index], Utilities.getString(tds.get(k)), is_colspanning, colspanVal, is_rowspanning, rowspanVal, false, 0, isStub, stubProbability, index,rowindex, l, s);
							if(hr!=null && hr.size()!=0 && hr.get(0)!=null){
								cells[rowindex][index].setBreakingLineOverRow(true);
								isStub=false;
							}
							table = Statistics.statisticsForCell(table, cells[rowindex][index]);
						}
						catch(Exception ex)
						{
							System.out.println("Error: Table is spanning more then it is possible");
						}
						rowindex++;
					}
					index++;
				}
			}//end for tds.size()
		}// end for rowheads
		table.stat.setNum_columns(num_of_columns);
		return table;
	}
	
	public int getNumOfTablesInArticle(Elements tablesxml)
	{
		int numOfTables = 0;
		for(int i = 0;i<tablesxml.size();i++)
		{
			List<Element> tb = getChildrenByTagName(tablesxml.get(i),"table");
			numOfTables+=tb.size();
		}
		if(numOfTables<tablesxml.size())
			numOfTables = tablesxml.size();
		
		return numOfTables;
	}
	
	
	/**
	 * Parses table, makes matrix of cells and put it into Article object
	 * @param article - Article to populate
	 * @param parse - Document which is being parsed
	 * @return populated Article
	 */
	public Article ParseTables(Article article, Document parse)
	{
		Elements tablesxml = parse.getElementsByTag("table");
		int numOfTables =  getNumOfTablesInArticle(tablesxml);
		
		Table[] tables = new Table[numOfTables];
		article.setTables(tables);
		int tableindex = 0;

			List<Element> tb = tablesxml;
			for(int s = 0;s<tb.size();s++)
			{
			Statistics.addTable();
			String label = readTableLabel(tablesxml.get(s));
			
			tables[tableindex] = new Table(label);
			tables[tableindex].setDocumentFileName("PMC"+article.getPmc());
			tables[tableindex].setXml(Utilities.CreateXMLStringFromSubNode(tablesxml.get(s)));
			System.out.println("Table title:"+tables[tableindex].getTable_title());
			String caption = readTableCaption(tablesxml.get(s));
			tables[tableindex].setTable_footer(caption);
			String foot = ReadTableFooter(tablesxml.get(s));
			//tables[tableindex].setTable_footer(foot);
			System.out.println("Foot: "+foot);

			//count rows
			int headsize = 0;
			List<Element> thead = null;
			if(tb.size()>0){
				thead = getChildrenByTagName(tb.get(s), "thead");
				headsize = thead.size();
			}
			List<Element> rowshead = null;
			if(headsize>0)
			{
				rowshead = getChildrenByTagName(thead.get(0), "tr");
			}
			else
			{
				tables[tableindex].setHasHeader(false);
				Statistics.TableWithoutHead();
			}
			List<Element> tbody = getChildrenByTagName(tb.get(s), "tbody");
			if(tbody.size()==0)
			{
				Statistics.TableWithoutBody();
				tables[tableindex].setHasBody(false);
				tableindex++;
				continue;
			}
			List<Element> rowsbody = getChildrenByTagName(tbody.get(0), "tr");
			//int num_of_rows = headrowscount+rowsbody.size();
			int headrowscount = 0;
			if (rowshead != null) 
				headrowscount = rowshead.size();
			int num_of_rows = rowsbody.size()+headrowscount;
			int cols = CountColumns(rowsbody,rowshead);
			tables[tableindex].tableInTable = s;

			int num_of_columns = cols;
			tables[tableindex].setNum_of_columns(num_of_columns);
			tables[tableindex].setNum_of_rows(num_of_rows);
			tables[tableindex].CreateCells(num_of_columns, num_of_rows);
			Cell[][] cells = tables[tableindex].getTable_cells();
			tables[tableindex] = ProcessTableHeader(article,tables[tableindex],cells, rowshead, headrowscount, num_of_columns);
			Statistics.addColumn(num_of_columns);
			Statistics.addRow(num_of_rows);
			tables[tableindex] = ProcessTableBody(article,tables[tableindex],cells, rowsbody, headrowscount, num_of_columns);
			tables[tableindex].setTable_cells(cells);
			
			//Print cells
			for(int j = 0; j<cells.length;j++)
			{
				for(int k = 0; k<cells[j].length;k++)
				{
					System.out.println(j+","+k+": "+cells[j][k].getCell_content());
				}
			}
			System.out.println("Number of rows: "+num_of_rows);
			System.out.println("Number of columns: "+num_of_columns);
			tableindex++;
			}
//			
//				Statistics.addTable();
//				String label = readTableLabel(tablesxml.get(i));
//				tables[tableindex] = new Table(label);
//				tables[tableindex].setDocumentFileName("PMC"+article.getPmc());
//				tables[tableindex].setXml(Utilities.CreateXMLStringFromSubNode(tablesxml.get(s)));
//				System.out.println("Table title:"+tables[tableindex].getTable_title());
//				String caption = readTableCaption(tablesxml.get(s));
//				tables[tableindex].setTable_caption(caption);
//				String foot = ReadTableFooter(tablesxml.get(i));
//				tables[tableindex].setTable_footer(foot);
//				System.out.println("Foot: "+foot);
//				Statistics.ImageTable(FileName);
//				tables[tableindex].setNoXMLTable(true);
//				tableindex++;
//				continue;



		return article;
	}
	
	
	
	/**
	 * Gets the children by tag name.
	 *
	 * @param parent the parent
	 * @param name the name
	 * @return the children by tag name
	 */
	public static List<Element> getChildrenByTagName(Element parent, String name) {
	    List<Element> nodeList = new ArrayList<Element>();
	    for (Element child = parent.getAllElements().first().children().first(); child != null; child = child.nextElementSibling()) {
	      if ( name.equals(child.nodeName())) {
	        nodeList.add(child);
	      }
	    }

	    return nodeList;
	  }
	

}
