/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package readers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import Utils.Author;
import Utils.Utilities;
import classifiers.SimpleTableClassifier;
import stats.Statistics;
import tablInEx.*;


/**
 * PMCXMLReader class is used to read and parse XML data from PubMed Central database
 * The class takes as input folder with XML documents extracted from PMC database and creates array of Articles {@link Article} as output
 * @author Nikola Milosevic
 */ 
public class PMCXMLReader implements Reader{

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
		art.setSource("PMC");
		try{
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(FileName));
		String line = null;
		String xml = "";
		while ((line = reader.readLine()) != null) {
			if(line.contains("JATS-archivearticle1.dtd")||line.contains("archivearticle.dtd"))
				continue;
		    xml +=line+'\n';
		}		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		factory.setNamespaceAware(true);
		factory.setValidating(false);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    Document parse =  builder.parse(is);
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

	public LinkedList<Author> GetAuthors(Document parse)
	{
		LinkedList<Author> auths = new LinkedList<Author>();
		NodeList authors = parse.getElementsByTagName("contrib");
	    for(int j = 0; j<authors.getLength(); j++)
	    {
	    	Author auth = new Author();
	    	String givenName = "";
	    	String surname = "";
	    	String email = "";
	    	
	    	for(int k = 0;k<authors.item(j).getChildNodes().getLength();k++){
	    	if(authors.item(j).getChildNodes().item(k).getNodeName()=="name"){
	    	NodeList name = authors.item(j).getChildNodes().item(k).getChildNodes();
	    	if(name.item(1)!=null)
	    		surname = Utilities.getString(name.item(0));
	    	if(name.item(1)!=null)
	    		givenName = Utilities.getString(name.item(1));
	    	auth.name = surname+ ", "+givenName;
	    	}
	    	if(authors.item(j).getChildNodes().item(k).getNodeName()=="email"){
	    		NodeList name = authors.item(j).getChildNodes().item(k).getChildNodes();
		    	if(name.item(0)!=null)
		    		email = Utilities.getString(name.item(0));
		    	auth.email = email;
	    	}
	    	
	    	
	    	if(authors.item(j).getChildNodes().item(k).getNodeName()=="xref"){
	    		Node name = authors.item(j).getChildNodes().item(k);
	    		NamedNodeMap attr = name.getAttributes();
	    		if (null != attr) {
	    			Node p = attr.getNamedItem("ref-type");
	    	        //if(p.getNodeValue() == "aff");
	    	        Node p2 = attr.getNamedItem("rid");
	    	        String affId = p2.getNodeValue();
	    	        NodeList affis = parse.getElementsByTagName("aff");
	    		    String[] affilis = new String[affis.getLength()];
	    		    for(int s = 0; s<affis.getLength(); s++)
	    		    {
	    		    	if(affis.item(s).getAttributes()!=null &&affis.item(s).getAttributes().getNamedItem("id")!=null&& affis.item(s).getAttributes().getNamedItem("id").getNodeValue().equals(affId)){
	    		    		String affName = Utilities.getString(affis.item(s));
	    		    		if(affName.contains("1")||affName.contains("2")||affName.contains("3")||affName.contains("4")||affName.contains("5"))
	    		    			affName = affName.substring(1);
	    		    		auth.affiliation.add(affName);	
	    		    	}
	    		    }
	    		}
	    	}
	    	
	    	}
	    auths.add(auth);
	    }
	    return auths;
	}
	
	/**
	 * Gets the affiliations of authors.
	 *
	 * @param parse the parse
	 * @return the string[]
	 */
	public String[] GetAffiliations(Document parse)
	{
	    NodeList affis = parse.getElementsByTagName("aff");
	    String[] affilis = new String[affis.getLength()];
	    for(int j = 0; j<affis.getLength(); j++)
	    {
	    	String affiliation = Utilities.getString(affis.item(j));
	    	affilis[j] = affiliation;
	    	System.out.println("Affiliation:"+affiliation);
	    }
	    return affilis;
	}
	
	/**
	 * Gets the article keywords.
	 *
	 * @param parse the parse
	 * @return the keywords
	 */
	public String[] getKeywords(Document parse)
	{
	    NodeList keywords = parse.getElementsByTagName("kwd");
	    String[] keywords_str = new String[keywords.getLength()];
	    for(int j = 0; j<keywords.getLength(); j++)
	    {
	    	if(keywords.item(j).getTextContent().length()>1){
	    		String Keyword = keywords.item(j).getTextContent().substring(1);
	    		keywords_str[j] = Keyword;
	    		System.out.println("Keyword:"+Keyword);
	    	}
	    }
	    return keywords_str;
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
		String title = "";
		String journal = "";
		if(parse.getElementsByTagName("article-title")!=null && parse.getElementsByTagName("article-title").item(0)!=null){
			title = parse.getElementsByTagName("article-title").item(0).getTextContent();
			title = title.replaceAll("\n", "");
			title = title.replaceAll("\t", "");
			System.out.println(title);
		}
	    LinkedList<Author> auths = GetAuthors(parse);
	    for(int j = 0; j<auths.size(); j++)
	    {
	    	System.out.println(auths.get(j));
	    }

	    //journal-title
	    if(parse.getElementsByTagName("journal-title")!=null && parse.getElementsByTagName("journal-title").item(0)!=null){
			journal = parse.getElementsByTagName("journal-title").item(0).getTextContent();
			journal = journal.replaceAll("\n", "");
			journal = journal.replaceAll("\t", "");
			
		}
	    
	    NodeList issn = parse.getElementsByTagName("issn");
	    for(int j=0;j<issn.getLength();j++)
	    {
	    	if(issn==null || issn.item(j)==null || issn.item(j).getAttributes()== null || issn.item(j).getAttributes().getNamedItem("pub-type")==null || issn.item(j).getAttributes().getNamedItem("pub-type").getNodeValue()==null)
	    		continue;
	    	if(issn.item(j).getAttributes().getNamedItem("pub-type").getNodeValue().equals("ppub"))
	    	{
	    	String issnp = issn.item(j).getTextContent();	
	    	art.setPissn(issnp);
	    	if(issnp!=null)
	    		System.out.println(issnp);
	    	}
	    	if(issn.item(j).getAttributes().getNamedItem("pub-type").getNodeValue().equals("epub"))
	    	{
	    		String issne = issn.item(j).getTextContent();	
		    	art.setPissn(issne);
		    	if(issne!=null)
		    		System.out.println(issne);
	    	}
	    }
	    NodeList article_id = parse.getElementsByTagName("article-id");
	    for(int j=0;j<article_id.getLength();j++)
	    {
	    	if(article_id.item(j).getAttributes()!=null && article_id.item(j).getAttributes().getNamedItem("pub-id-type")!=null && article_id.item(j).getAttributes().getNamedItem("pub-id-type").getNodeValue().equals("pmid"))
	    	{
	    	String pmid = article_id.item(j).getTextContent();	
	    	art.setPmid(pmid);
	    	if(pmid!=null)
	    		System.out.println(pmid);
	    	}
	    	if(article_id.item(j).getAttributes()!=null && article_id.item(j).getAttributes().getNamedItem("pub-id-type")!=null && article_id.item(j).getAttributes().getNamedItem("pub-id-type").getNodeValue().equals("pmc"))
	    	{
	    		String pmc = article_id.item(j).getTextContent();	
		    	art.setPmc(pmc);
		    	art.setSpec_id(pmc);
		    	if(pmc!=null)
		    		System.out.println(pmc);
	    	}
	    }
	    
	    String[] affilis = GetAffiliations(parse);	    
	    art.setAffiliation(affilis);
	    NodeList art_abstract = parse.getElementsByTagName("abstract");
	    for(int j=0;j<art_abstract.getLength();j++)
	    {
	    	if(art_abstract.item(j).getAttributes().getNamedItem("abstract-type")!=null&&art_abstract.item(j).getAttributes().getNamedItem("abstract-type").getNodeValue().equals("short"))
	    	{
	    		art.setShort_abstract(art_abstract.item(j).getTextContent());
	    	}
	    	else
	    	{
	    		art.setAbstract(art_abstract.item(j).getTextContent());
	    	}
	    }
	    
	    String[] keywords_str = getKeywords(parse);
	    art.setKeywords(keywords_str);
	    String publisher_name = "";
	    if(parse.getElementsByTagName("publisher-name").item(0)!=null)
	    	publisher_name = parse.getElementsByTagName("publisher-name").item(0).getTextContent();
	    art.setPublisher_name(publisher_name);
	    if(publisher_name!=null)
	    	System.out.println(publisher_name);
	    String publisher_loc = "";
	    if(parse.getElementsByTagName("publisher-loc").item(0)!=null)
	    publisher_loc = parse.getElementsByTagName("publisher-loc").item(0).getTextContent();
	    art.setPublisher_loc(publisher_loc);
	    if(publisher_loc!=null)
	    	System.out.println(publisher_loc);
	    try{
	    if(parse.getElementsByTagName("body").item(0)!=null)
	    {
	    String plain_text = parse.getElementsByTagName("body").item(0).getTextContent();
	    art.setPlain_text(plain_text);
	    }
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    }

	    art.setTitle(title);
	    art.setXML(xml);
	    art.setAuthors(auths);
	    art.setJournal_name(journal);
		return art;
	}
	
	/**
	 * Read table label.
	 *
	 * @param tablexmlNode the tablexml node
	 * @return the string
	 */
	public String readTableLabel(Node tablexmlNode)
	{
		String label = "Table without label";
		List<Node> nl = getChildrenByTagName(tablexmlNode,"label");
		if(nl.size()>0)
		{
			label = Utilities.getString(nl.get(0));
		}
		
		return label;
	}
	
	/**
	 * Read table caption.
	 *
	 * @param tablexmlNode the tablexml node
	 * @return the string
	 */
	public String readTableCaption(Node tablexmlNode)
	{
		String caption = "";
		List<Node>nl = getChildrenByTagName(tablexmlNode,"caption");
		if(nl.size()>0){
			caption = Utilities.getString(nl.get(0));
		}
		nl = getChildrenByTagName(tablexmlNode,"p");
		if(nl.size()>0){
			caption = Utilities.getString(nl.get(0));
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
	public String ReadTableFooter(Node tablesxmlNode)
	{
		String foot = "";
		List<Node> nl = getChildrenByTagName(tablesxmlNode,"table-wrap-foot");
		if(nl.size()>=1)
		{
			foot = Utilities.getString(nl.get(0));
		}
		return foot;
	}
	
	/**
	 * Count columns.
	 *
	 * @param rowsbody the rowsbody
	 * @param rowshead the rowshead
	 * @return the int
	 */
	public int CountColumns(List<Node> rowsbody,List<Node> rowshead)
	{
		int cols=0;
		int headrowscount= 0;
		if(rowshead!=null)
			headrowscount = rowshead.size();
		for(int row = 0;row<rowsbody.size();row++)
		{
			int cnt=0;
			List<Node> tds = getChildrenByTagName(rowsbody.get(row), "td");
			for(int k=0;k<tds.size();k++)
			{
				if(tds.get(k).getAttributes().getNamedItem("colspan")!=null && Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue())>1)
				{
					cnt+=Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue());
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
			List<Node> tdsh =  getChildrenByTagName(rowshead.get(0), "td");
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
	public Table ProcessTableHeader(Article a,Table table, Cell[][] cells,List<Node> rowshead,int headrowscount,int num_of_columns)
	{
		for(int j = 0;j<headrowscount;j++)
		{

			Statistics.addHeaderRow();
			table.stat.AddHeaderRow();
			List<Node> tds = getChildrenByTagName(rowshead.get(j), "td");
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
				if(tds.get(k).getAttributes().getNamedItem("rowspan")!=null && Utilities.isNumeric(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue()) && Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue())>1)
				{
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal =  Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue());											
				}
				//colspan
				if(tds.get(k).getAttributes().getNamedItem("colspan")!=null && Utilities.isNumeric(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue()) && Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue())>1)
				{
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal =  Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue());					
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
	public Table ProcessTableBody(Article a, Table table, Cell[][] cells,List<Node> rowsbody,int headrowscount, int num_of_columns)
	{
		int startj = headrowscount;
		boolean tablecounted = false;
		for(int j = 0;j<rowsbody.size();j++)
		{
			table.stat.AddBodyRow();
			List<Node> tds = getChildrenByTagName(rowsbody.get(j), "td");
			int index = 0;
			int rowindex = startj;
			for(int k = 0;k<tds.size();k++)
			{
				table.stat.AddUnprCell();
				List<Node> hr = getChildrenByTagName(tds.get(k), "hr");
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
				if(tds.get(k).getAttributes().getNamedItem("rowspan")!=null && Utilities.isNumeric(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue()) && Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue())>1)
				{
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal = Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("rowspan").getNodeValue());											
				}
				//colspan
				if(tds.get(k).getAttributes().getNamedItem("colspan")!=null && Utilities.isNumeric(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue()) && Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue())>1)
				{
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal = Utilities.getFirstValue(tds.get(k).getAttributes().getNamedItem("colspan").getNodeValue());					
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
	
	public int getNumOfTablesInArticle(NodeList tablesxml)
	{
		int numOfTables = 0;
		for(int i = 0;i<tablesxml.getLength();i++)
		{
			List<Node> tb = getChildrenByTagName(tablesxml.item(i),"table");
			numOfTables+=tb.size();
		}
		if(numOfTables<tablesxml.getLength())
			numOfTables = tablesxml.getLength();
		
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
		NodeList tablesxml = parse.getElementsByTagName("table-wrap");
		int numOfTables =  getNumOfTablesInArticle(tablesxml);
		
		Table[] tables = new Table[numOfTables];
		article.setTables(tables);
		int tableindex = 0;
		//Iterate document tables
		for(int i = 0;i<tablesxml.getLength();i++)
		{
			List<Node> inline_formula = null;
			List<Node> tb = getChildrenByTagName(tablesxml.item(i),"table");
			//check if there is one cell table with reference to the image of the actual table
			try{
			inline_formula =  getChildrenByTagName( getChildrenByTagName(getChildrenByTagName(getChildrenByTagName(tb.get(0), "tbody").get(0), "tr").get(0), "td").get(0), "inline-formula");
			}
			catch(Exception ex)
			{}
			if((inline_formula!=null && inline_formula.size()==0)||inline_formula==null){
			for(int s = 0;s<tb.size();s++)
			{
			Statistics.addTable();
			String label = readTableLabel(tablesxml.item(i));
			
			tables[tableindex] = new Table(label);
			tables[tableindex].setDocumentFileName("PMC"+article.getPmc());
			tables[tableindex].setXml(Utilities.CreateXMLStringFromSubNode(tablesxml.item(i)));
			System.out.println("Table title:"+tables[tableindex].getTable_title());
			String caption = readTableCaption(tablesxml.item(i));
			tables[tableindex].setTable_caption(caption);
			String foot = ReadTableFooter(tablesxml.item(i));
			tables[tableindex].setTable_footer(foot);
			System.out.println("Foot: "+foot);

			//count rows
			int headsize = 0;
			List<Node> thead = null;
			if(tb.size()>0){
				thead = getChildrenByTagName(tb.get(s), "thead");
				headsize = thead.size();
			}
			List<Node> rowshead = null;
			if(headsize>0)
			{
				rowshead = getChildrenByTagName(thead.get(0), "tr");
			}
			else
			{
				tables[tableindex].setHasHeader(false);
				Statistics.TableWithoutHead();
			}
			List<Node> tbody = getChildrenByTagName(tb.get(s), "tbody");
			if(tbody.size()==0)
			{
				Statistics.TableWithoutBody();
				tables[tableindex].setHasBody(false);
				tableindex++;
				continue;
			}
			List<Node> rowsbody = getChildrenByTagName(tbody.get(0), "tr");
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
			}
			//List<Node> inlinegraphic =  getChildrenByTagName(tb.get(0), "inline-graphic");
			
			if((tb.size()==0 && tableindex<numOfTables)||(inline_formula!=null && inline_formula.size()==1))
			{
				Statistics.addTable();
				String label = readTableLabel(tablesxml.item(i));
				tables[tableindex] = new Table(label);
				tables[tableindex].setDocumentFileName("PMC"+article.getPmc());
				tables[tableindex].setXml(Utilities.CreateXMLStringFromSubNode(tablesxml.item(i)));
				System.out.println("Table title:"+tables[tableindex].getTable_title());
				String caption = readTableCaption(tablesxml.item(i));
				tables[tableindex].setTable_caption(caption);
				String foot = ReadTableFooter(tablesxml.item(i));
				tables[tableindex].setTable_footer(foot);
				System.out.println("Foot: "+foot);
				Statistics.ImageTable(FileName);
				tables[tableindex].setNoXMLTable(true);
				tableindex++;
				continue;
			}
		}// end for tables
//		if(TablInExMain.TypeClassify){
//			for(int i = 0;i<numOfTables;i++)
//			{
//				SimpleTableClassifier.ClassifyTableByType(tables[i]);
//				tables[i].printTableStatsToFile("TableStats.txt");
//			}
//		}

		return article;
	}
	
	
	
	/**
	 * Gets the children by tag name.
	 *
	 * @param parent the parent
	 * @param name the name
	 * @return the children by tag name
	 */
	public static List<Node> getChildrenByTagName(Node parent, String name) {
	    List<Node> nodeList = new ArrayList<Node>();
	    for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
	      if (child.getNodeType() == Node.ELEMENT_NODE && 
	          name.equals(child.getNodeName())) {
	        nodeList.add((Node) child);
	      }
	    }

	    return nodeList;
	  }
	
	
	
	
}
