package readers;

import java.io.BufferedReader;
import java.io.File;
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

import stats.Statistics;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;
import Utils.Author;
import Utils.Utilities;

public class DailyMedReader implements Reader {

	private String FileName;

	public void init(String file_name) {
		setFileName(file_name);
	}

	/**
	 * This method is the main method for reading PMC XML files. It uses
	 * {@link #ParseMetaData} and {@link #ParseTables} methods. It returns
	 * {@link Article} object that contains structured data from article,
	 * including tables.
	 * 
	 * @return Article
	 */
	public Article Read() {
		Article art = new Article(FileName);

		art.setSource("DailyMed");
		try {
			File[] files = (new File(FileName)).listFiles();
			File XMLFile = null;
			for(File file : files)
			{
				if(file.getName().contains(".xml"))
				{
					XMLFile = file;
				}
			}
			if (XMLFile==null)
				return null;
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(XMLFile));
			String line = null;
			String xml = "";
			while ((line = reader.readLine()) != null) {
				xml += line + '\n';
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			Document parse = builder.parse(is);
			art = ParseMetaData(art, parse, xml);
			art = ParseTables(art, parse);

		} catch (Exception ex) {
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

	public LinkedList<Author> GetAuthors(Document parse) {
		LinkedList<Author> auths = new LinkedList<Author>();
		NodeList authors = parse.getElementsByTagName("representedOrganization");
		for (int j = 0; j < authors.getLength(); j++) {
			Author auth = new Author();
			String surname = "";
			for (int k = 0; k < authors.item(j).getChildNodes().getLength(); k++) {
				if (authors.item(j).getChildNodes().item(k).getNodeName() == "name") {
					NodeList name = authors.item(j).getChildNodes().item(k)
							.getChildNodes();
					if (name.item(0) != null)
						surname = Utilities.getString(name.item(0));
					auth.name = surname;
				}

			}
			auths.add(auth);
		}
		return auths;
	}

	/**
	 * Gets the affiliations of authors.
	 *
	 * @param parse
	 *            the parse
	 * @return the string[]
	 */
	public String[] GetAffiliations(Document parse) {
		NodeList affis = parse.getElementsByTagName("aff");
		String[] affilis = new String[affis.getLength()];
		for (int j = 0; j < affis.getLength(); j++) {
			String affiliation = Utilities.getString(affis.item(j));
			affilis[j] = affiliation;
			System.out.println("Affiliation:" + affiliation);
		}
		return affilis;
	}

	/**
	 * Gets the article keywords.
	 *
	 * @param parse
	 *            the parse
	 * @return the keywords
	 */
	public String[] getKeywords(Document parse) {
		NodeList keywords = parse.getElementsByTagName("kwd");
		String[] keywords_str = new String[keywords.getLength()];
		for (int j = 0; j < keywords.getLength(); j++) {
			if (keywords.item(j).getTextContent().length() > 1) {
				String Keyword = keywords.item(j).getTextContent().substring(1);
				keywords_str[j] = Keyword;
				System.out.println("Keyword:" + Keyword);
			}
		}
		return keywords_str;
	}

	/**
	 * Reads metadata from article such as title, authors, publication type etc
	 * 
	 * @param art
	 *            - Article where to put data
	 * @param parse
	 *            - Document of XML
	 * @param xml
	 *            - XML code
	 * @return Article - populated art
	 */
	public Article ParseMetaData(Article art, Document parse, String xml) {
		String title = "";
		String journal = "";
		if (parse.getElementsByTagName("title") != null
				&& parse.getElementsByTagName("title").item(0) != null) {
			title = parse.getElementsByTagName("title").item(0)
					.getTextContent();
			title = title.replaceAll("\n", "");
			title = title.replaceAll("\t", "");
			System.out.println(title);
		}
		LinkedList<Author> auths = GetAuthors(parse);
		for (int j = 0; j < auths.size(); j++) {
			System.out.println(auths.get(j));
		}

		NodeList setId = parse.getElementsByTagName("setId");
		for (int j = 0; j < setId.getLength(); j++) {
			if (setId == null
					|| setId.item(j) == null
					|| setId.item(j).getAttributes() == null
					|| setId.item(j).getAttributes().getNamedItem("root") == null
					|| setId.item(j).getAttributes().getNamedItem("root")
							.getNodeValue() == null)
				continue;
			
			String setIdA = setId.item(j).getAttributes().getNamedItem("root")
					.getNodeValue();
			art.setSpec_id(setIdA);
		}
		System.out.println(art.getSpec_id());

		
		try {
			if (parse.getElementsByTagName("structuredBody").item(0) != null) {
				String plain_text = parse.getElementsByTagName("structuredBody").item(0)
						.getTextContent();
				plain_text = plain_text.replace("\n", "");
				plain_text = plain_text.replace("\t", " ");
				plain_text = plain_text.replaceAll("\\s+", " ");
				art.setPlain_text(plain_text);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		art.setTitle(title);
		art.setXML(xml);
		art.setAuthors(auths);
		return art;
	}

	/**
	 * Read table label.
	 *
	 * @param tablexmlNode
	 *            the tablexml node
	 * @return the string
	 */
	public String readTableLabel(Node tablexmlNode) {
		String label = "Table without label";
		List<Node> nl = getChildrenByTagName(tablexmlNode, "caption");
		if (nl.size() > 0) {
			label = Utilities.getString(nl.get(0));
		}

		return label;
	}

	/**
	 * Read table caption.
	 *
	 * @param tablexmlNode
	 *            the tablexml node
	 * @return the string
	 */
	public String readTableCaption(Node tablexmlNode) {
		String caption = "";
		List<Node> nl = getChildrenByTagName(tablexmlNode, "caption");
		if (nl.size() > 0) {
			caption = Utilities.getString(nl.get(0));
		}
		nl = getChildrenByTagName(tablexmlNode, "p");
		if (nl.size() > 0) {
			caption = Utilities.getString(nl.get(0));
		}
		nl = getChildrenByTagName(tablexmlNode, "title");
		if (nl.size() > 0) {
			caption = Utilities.getString(nl.get(0));
		}
		return caption;
	}

	/**
	 * Read table footer.
	 *
	 * @param tablesxmlNode
	 *            the tablesxml node
	 * @return the string
	 */
	public String ReadTableFooter(Node tablesxmlNode) {
		String foot = "";
		List<Node> nl = getChildrenByTagName(tablesxmlNode, "tfoot");
		if (nl.size() >= 1) {
			foot = Utilities.getString(nl.get(0));
			foot  = foot.replaceAll("\\s+", " ");
			foot = foot.replaceAll("\n+","\n");
		}
		return foot;
	}

	/**
	 * Count columns.
	 *
	 * @param rowsbody
	 *            the rowsbody
	 * @param rowshead
	 *            the rowshead
	 * @return the int
	 */
	public int CountColumns(List<Node> rowsbody, List<Node> rowshead) {
		int cols = 0;
		int headrowscount = 0;
		if (rowshead != null)
			headrowscount = rowshead.size();
		for (int row = 0; row < rowsbody.size(); row++) {
			int cnt = 0;
			List<Node> tds = getChildrenByTagName(rowsbody.get(row), "td");
			for (int k = 0; k < tds.size(); k++) {
				if (tds.get(k).getAttributes().getNamedItem("colspan") != null
						&& Utilities.getFirstValue(tds.get(k).getAttributes()
								.getNamedItem("colspan").getNodeValue()) > 1) {
					cnt += Utilities.getFirstValue(tds.get(k).getAttributes()
							.getNamedItem("colspan").getNodeValue());
				} else {
					cnt++;
				}
			}
			cols = Math.max(cols, cnt);
		}
		if (headrowscount != 0) {
			List<Node> tdsh = getChildrenByTagName(rowshead.get(0), "td");
			if (tdsh.size() == 0) {
				tdsh = getChildrenByTagName(rowshead.get(0), "th");
			}
			cols = Math.max(cols, tdsh.size());
		}

		return cols;
	}

	/**
	 * Process table header.
	 *
	 * @param table
	 *            the table
	 * @param cells
	 *            the cells
	 * @param rowshead
	 *            the rowshead
	 * @param headrowscount
	 *            the headrowscount
	 * @param num_of_columns
	 *            the num_of_columns
	 * @return the table
	 */
	public Table ProcessTableHeader(Article a, Table table, Cell[][] cells,
			List<Node> rowshead, int headrowscount, int num_of_columns) {
		for (int j = 0; j < headrowscount; j++) {

			Statistics.addHeaderRow();
			table.stat.AddHeaderRow();
			List<Node> tds = getChildrenByTagName(rowshead.get(j), "td");
			if (tds.size() == 0)
				tds = getChildrenByTagName(rowshead.get(j), "th");
			int index = 0;
			// read cells
			for (int k = 0; k < tds.size(); k++) {
				table.stat.AddUnprCell();
				table.stat.AddHeaderCell();
				boolean is_colspanning = false;
				boolean is_rowspanning = false;
				int colspanVal = 1;
				int rowspanVal = 1;
				if (tds.get(k).getAttributes().getNamedItem("rowspan") != null
						&& Utilities.isNumeric(tds.get(k).getAttributes()
								.getNamedItem("rowspan").getNodeValue())
						&& Utilities.getFirstValue(tds.get(k).getAttributes()
								.getNamedItem("rowspan").getNodeValue()) > 1) {
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal = Utilities.getFirstValue(tds.get(k)
							.getAttributes().getNamedItem("rowspan")
							.getNodeValue());
				}
				// colspan
				if (tds.get(k).getAttributes().getNamedItem("colspan") != null
						&& Utilities.isNumeric(tds.get(k).getAttributes()
								.getNamedItem("colspan").getNodeValue())
						&& Utilities.getFirstValue(tds.get(k).getAttributes()
								.getNamedItem("colspan").getNodeValue()) > 1) {
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal = Utilities.getFirstValue(tds.get(k)
							.getAttributes().getNamedItem("colspan")
							.getNodeValue());
				}

				for (int l = 0; l < colspanVal; l++) {
					int rowindex = j;
					for (int s = 0; s < rowspanVal; s++) {
						try {
							while (cells[rowindex][index].isIs_filled()
									&& index != num_of_columns)
								index++;
							cells[rowindex][index] = Cell.setCellValues(a,
									cells[rowindex][index],
									Utilities.getString(tds.get(k)),
									is_colspanning, colspanVal, is_rowspanning,
									rowspanVal, true, 1, false, 0, index,
									rowindex, l, s);
							// System.out.println(j+","+index+": "+cells[j][index].getCell_content());
							table = Statistics.statisticsForCell(table,
									cells[rowindex][index]);
						} catch (Exception ex) {
							System.out
									.println("Error: Table is spanning more then it is possible");
						}
						rowindex++;
					}
					index++;
				}
			}// end for tds.size()
		}// end for rowheads
		return table;
	}

	/**
	 * Process table body.
	 *
	 * @param table
	 *            the table
	 * @param cells
	 *            the cells
	 * @param rowsbody
	 *            the rowsbody
	 * @param headrowscount
	 *            the headrowscount
	 * @param num_of_columns
	 *            the num_of_columns
	 * @return the table
	 */
	public Table ProcessTableBody(Article a, Table table, Cell[][] cells,
			List<Node> rowsbody, int headrowscount, int num_of_columns) {
		int startj = headrowscount;
		boolean tablecounted = false;
		for (int j = 0; j < rowsbody.size(); j++) {
			table.stat.AddBodyRow();
			List<Node> tds = getChildrenByTagName(rowsbody.get(j), "td");
			int index = 0;
			int rowindex = startj;
			for (int k = 0; k < tds.size(); k++) {
				table.stat.AddUnprCell();
				List<Node> hr = getChildrenByTagName(tds.get(k), "hr");
				boolean isStub = false;
				float stubProbability = 0;

				if (index == 0) {
					isStub = true;
					stubProbability = (float) 0.9;
				}

				boolean is_colspanning = false;
				boolean is_rowspanning = false;
				int colspanVal = 1;
				int rowspanVal = 1;
				if (tds.get(k).getAttributes().getNamedItem("rowspan") != null
						&& Utilities.isNumeric(tds.get(k).getAttributes()
								.getNamedItem("rowspan").getNodeValue())
						&& Utilities.getFirstValue(tds.get(k).getAttributes()
								.getNamedItem("rowspan").getNodeValue()) > 1) {
					table.setRowSpanning(true);
					Statistics.addRowSpanningCell();
					table.stat.AddRowSpanningCell();
					is_rowspanning = true;
					rowspanVal = Utilities.getFirstValue(tds.get(k)
							.getAttributes().getNamedItem("rowspan")
							.getNodeValue());
				}
				// colspan
				if (tds.get(k).getAttributes().getNamedItem("colspan") != null
						&& Utilities.isNumeric(tds.get(k).getAttributes()
								.getNamedItem("colspan").getNodeValue())
						&& Utilities.getFirstValue(tds.get(k).getAttributes()
								.getNamedItem("colspan").getNodeValue()) > 1) {
					table.setColSpanning(true);
					Statistics.addColumnSpanningCell();
					table.stat.AddColSpanningCell();
					is_colspanning = true;
					colspanVal = Utilities.getFirstValue(tds.get(k)
							.getAttributes().getNamedItem("colspan")
							.getNodeValue());
				}
				for (int l = 0; l < colspanVal; l++) {
					rowindex = startj + j;
					for (int s = 0; s < rowspanVal; s++) {
						try {
							while (cells[rowindex][index].isIs_filled()
									&& index != num_of_columns)
								index++;
							cells[rowindex][index] = Cell.setCellValues(a,
									cells[rowindex][index],
									Utilities.getString(tds.get(k)),
									is_colspanning, colspanVal, is_rowspanning,
									rowspanVal, false, 0, isStub,
									stubProbability, index, rowindex, l, s);
							if (hr != null && hr.size() != 0
									&& hr.get(0) != null) {
								cells[rowindex][index]
										.setBreakingLineOverRow(true);
								isStub = false;
							}
							table = Statistics.statisticsForCell(table,
									cells[rowindex][index]);
						} catch (Exception ex) {
							System.out
									.println("Error: Table is spanning more then it is possible");
						}
						rowindex++;
					}
					index++;
				}
			}// end for tds.size()
		}// end for rowheads
		table.stat.setNum_columns(num_of_columns);
		return table;
	}

	public int getNumOfTablesInArticle(NodeList tablesxml) {
		int numOfTables = 0;
		for (int i = 0; i < tablesxml.getLength(); i++) {
			List<Node> tb = getChildrenByTagName(tablesxml.item(i), "table");
			numOfTables += tb.size();
		}
		if (numOfTables < tablesxml.getLength())
			numOfTables = tablesxml.getLength();

		return numOfTables;
	}

	/**
	 * Parses table, makes matrix of cells and put it into Article object
	 * 
	 * @param article
	 *            - Article to populate
	 * @param parse
	 *            - Document which is being parsed
	 * @return populated Article
	 */
	public Article ParseTables(Article article, Document parse) {
		NodeList tablesxml = parse.getElementsByTagName("table");
		int numOfTables = getNumOfTablesInArticle(tablesxml);

		Table[] tables = new Table[numOfTables];
		article.setTables(tables);
		int tableindex = 0;
		// Iterate document tables
		for (int i = 0; i < tablesxml.getLength(); i++) {
			List<Node> inline_formula = null;
			
			// check if there is one cell table with reference to the image of
			// the actual table

			if ((inline_formula != null && inline_formula.size() == 0)
					|| inline_formula == null) {
					Statistics.addTable();
					String label = "Table "+tableindex;

					tables[tableindex] = new Table(label);
					tables[tableindex].setDocumentFileName("DailyMed"
							+ article.getSpec_id());
					tables[tableindex].setXml(Utilities
							.CreateXMLStringFromSubNode(tablesxml.item(i)));
					System.out.println("Table title:"
							+ tables[tableindex].getTable_title());
					String caption =readTableLabel(tablesxml.item(i)) ;
					tables[tableindex].setTable_caption(caption);
					try{
					Node sibling = tablesxml.item(i).getParentNode().getPreviousSibling();
					 System.out.println(sibling.getTextContent());
					while (sibling != null) {
					  sibling = sibling.getPreviousSibling();
					  if(sibling.getNodeName()=="title"){
						  tables[tableindex].setSectionOfTable(sibling.getTextContent().replaceAll("\n","").replaceAll("\\s{2,}", ""));
						  break;
					  }
					}
					}catch(Exception ex)
					{
						ex.printStackTrace();
						System.out.println("ERROR: Could not read section");
					}
					System.out.println(tables[tableindex].getSectionOfTable());
					String foot = ReadTableFooter(tablesxml.item(i));
					tables[tableindex].setTable_footer(foot);
					System.out.println("Foot: " + foot);

					// count rows
					int headsize = 0;
					List<Node> thead = null;
					if (tablesxml.getLength() > 0) {
						thead = getChildrenByTagName(tablesxml.item(i), "thead");
						headsize = thead.size();
					}
					List<Node> rowshead = null;
					if (headsize > 0) {
						rowshead = getChildrenByTagName(thead.get(0), "tr");
					} else {
						tables[tableindex].setHasHeader(false);
						Statistics.TableWithoutHead();
					}
					List<Node> tbody = getChildrenByTagName(tablesxml.item(i), "tbody");
					if (tbody.size() == 0) {
						Statistics.TableWithoutBody();
						tables[tableindex].setHasBody(false);
						tableindex++;
						continue;
					}
					List<Node> rowsbody = getChildrenByTagName(tbody.get(0),
							"tr");
					// int num_of_rows = headrowscount+rowsbody.size();
					int headrowscount = 0;
					if (rowshead != null)
						headrowscount = rowshead.size();
					int num_of_rows = rowsbody.size() + headrowscount;
					int cols = CountColumns(rowsbody, rowshead);

					int num_of_columns = cols;
					tables[tableindex].setNum_of_columns(num_of_columns);
					tables[tableindex].setNum_of_rows(num_of_rows);
					tables[tableindex].CreateCells(num_of_columns, num_of_rows);
					Cell[][] cells = tables[tableindex].getTable_cells();
					tables[tableindex] = ProcessTableHeader(article,
							tables[tableindex], cells, rowshead, headrowscount,
							num_of_columns);
					Statistics.addColumn(num_of_columns);
					Statistics.addRow(num_of_rows);
					tables[tableindex] = ProcessTableBody(article,
							tables[tableindex], cells, rowsbody, headrowscount,
							num_of_columns);
					tables[tableindex].setTable_cells(cells);

					// Print cells
					for (int j = 0; j < cells.length; j++) {
						for (int k = 0; k < cells[j].length; k++) {
							System.out.println(j + "," + k + ": "
									+ cells[j][k].getCell_content());
						}
					}
					System.out.println("Number of rows: " + num_of_rows);
					System.out.println("Number of columns: " + num_of_columns);
					tableindex++;
				}
			}
			// List<Node> inlinegraphic = getChildrenByTagName(tb.get(0),
			// "inline-graphic");

	
		// end for tables
			// if(TablInExMain.TypeClassify){
		// for(int i = 0;i<numOfTables;i++)
		// {
		// SimpleTableClassifier.ClassifyTableByType(tables[i]);
		// tables[i].printTableStatsToFile("TableStats.txt");
		// }
		// }

		return article;
	}

	/**
	 * Gets the children by tag name.
	 *
	 * @param parent
	 *            the parent
	 * @param name
	 *            the name
	 * @return the children by tag name
	 */
	public static List<Node> getChildrenByTagName(Node parent, String name) {
		List<Node> nodeList = new ArrayList<Node>();
		for (Node child = parent.getFirstChild(); child != null; child = child
				.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE
					&& name.equals(child.getNodeName())) {
				nodeList.add((Node) child);
			}
		}

		return nodeList;
	}

}
