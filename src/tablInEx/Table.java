/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import stats.TableStats;


// TODO: Auto-generated Javadoc
/**
 * The Class Table. Contain various information about table, including the cell matrix
 */
public class Table {
	
	private String xml;
	private String documentFileName;
	public TableStats stat;
	
	/** The num_of_rows. */
	private int num_of_rows;
	
	public int tableInTable;
	
	/** The num_of_columns. */
	private int num_of_columns;
	
	/** The table_title. */
	private String table_title;
	
	/** The table_caption. */
	private String table_caption;
	
	/** The table_footer. */
	private String table_footer;
	
	private boolean hasHeader = true;
	
	private boolean hasBody = true;
	
	private boolean isNoXMLTable = false;
	
	private boolean isRowSpanning = false;
	
	private boolean isColSpanning = false;
	private int StructureClass = 0; //0 - no class,1- simplest, 2 - simple, 3 - medium, 4 - complex
	
	/** The cells. Cell matrix of the table */
	public Cell[][] cells;
	
	//Constructors
	/**
	 * Instantiates a new table.
	 *
	 * @param Title the title
	 */
	public Table(String Title)
	{
		table_title = Title;
		hasHeader = true;
		hasBody = true;
		isNoXMLTable = false;
		isRowSpanning = false;
		isColSpanning = false; 
		stat = new TableStats();
	}
	
	/**
	 * Instantiates a new table.
	 *
	 * @param Title the title
	 * @param Caption the caption
	 * @param Footer the footer
	 */
	public Table(String Title, String Caption, String Footer)
	{
		table_title = Title;
		table_caption = Caption;
		table_footer = Footer;
		stat = new TableStats();
	}
	
	/**
	 * Instantiates a new table.
	 *
	 * @param Title the title
	 * @param Caption the caption
	 * @param Footer the footer
	 * @param Columns the columns
	 * @param Rows the rows
	 */
	public Table(String Title, String Caption, String Footer,  int Columns,int Rows)
	{
		table_title = Title;
		table_caption = Caption;
		table_footer = Footer;
		num_of_rows = Rows;
		num_of_columns = Columns;
		stat = new TableStats();
	}
	
	public void printTableStatsToFile(String fileName)
	{
		if(!isNoXMLTable()&&isHasBody()!=false)
		{
			String output = "";
			output+= "File Name: ;"+documentFileName+"\r\n";
			output+= "Table Name: ;"+table_title+"\r\n";
			output+= "Number of cells: ;"+stat.getNum_of_cells()+"\r\n";
			output+= "Number of empty cells: ;"+stat.getNum_of_empty_cells()+"\r\n";
			output+= "Number of pure numeric cells: ;"+stat.getNum_of_pure_numeric_cells()+"\r\n";
			output+= "Number of part numeric cells: ;"+stat.getNum_of_part_numeric_cells()+"\r\n";
			output+= "Number of text cells: ;"+stat.getNum_of_text_cells()+"\r\n";
			output+= "Number of colspanning cells: ;"+stat.getNum_of_colspanning_cells()+"\r\n";
			output+= "Number of rowspanning cells: ;"+stat.getNum_of_rowspanning_cells()+"\r\n";
			output+= "Number of header rows: ;"+stat.getNum_of_header_rows()+"\r\n";
			output+= "Number of body rows: ;"+stat.getNum_of_body_rows()+"\r\n";
			output+="-----";		
		
			Utilities.AppendToFile(fileName, output);
		}
	}
	
	/**
	 * Creates the cells.
	 *
	 * @param Columns the columns
	 * @param Rows the rows
	 */
	public void CreateCells(int Columns,int Rows )
	{
		cells = new Cell[Rows][Columns];
		for(int i=0;i<Rows;i++)
		{
			for(int j=0;j<Columns;j++)
			{
				cells[i][j] = new Cell(i,j);
			}
		}
	}
	
	//Getters and setters
	/**
	 * Gets the num_of_rows.
	 *
	 * @return the num_of_rows
	 */
	public int getNum_of_rows() {
		return num_of_rows;
	}
	
	/**
	 * Sets the num_of_rows.
	 *
	 * @param num_of_rows the new num_of_rows
	 */
	public void setNum_of_rows(int num_of_rows) {
		this.num_of_rows = num_of_rows;
	}
	
	/**
	 * Gets the num_of_columns.
	 *
	 * @return the num_of_columns
	 */
	public int getNum_of_columns() {
		return num_of_columns;
	}
	
	/**
	 * Sets the num_of_columns.
	 *
	 * @param num_of_columns the new num_of_columns
	 */
	public void setNum_of_columns(int num_of_columns) {
		this.num_of_columns = num_of_columns;
	}
	
	/**
	 * Gets the table_title.
	 *
	 * @return the table_title
	 */
	public String getTable_title() {
		return table_title;
	}
	
	/**
	 * Sets the table_title.
	 *
	 * @param table_title the new table_title
	 */
	public void setTable_title(String table_title) {
		this.table_title = table_title;
	}
	
	/**
	 * Gets the table_caption.
	 *
	 * @return the table_caption
	 */
	public String getTable_caption() {
		return table_caption;
	}
	
	/**
	 * Sets the table_caption.
	 *
	 * @param table_caption the new table_caption
	 */
	public void setTable_caption(String table_caption) {
		this.table_caption = table_caption;
	}
	
	/**
	 * Gets the table_footer.
	 *
	 * @return the table_footer
	 */
	public String getTable_footer() {
		return table_footer;
	}
	
	/**
	 * Sets the table_footer.
	 *
	 * @param table_footer the new table_footer
	 */
	public void setTable_footer(String table_footer) {
		this.table_footer = table_footer;
	}
	
	/**
	 * Gets the table_cells.
	 *
	 * @return the table_cells
	 */
	public Cell[][] getTable_cells() {
		return cells;
	}
	
	/**
	 * Sets the table_cells.
	 *
	 * @param cells the new table_cells
	 */
	public void setTable_cells(Cell[][] cells) {
		this.cells = cells;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public boolean isHasBody() {
		return hasBody;
	}

	public void setHasBody(boolean hasBody) {
		this.hasBody = hasBody;
	}

	public boolean isNoXMLTable() {
		return isNoXMLTable;
	}

	public void setNoXMLTable(boolean isNoXMLTable) {
		this.isNoXMLTable = isNoXMLTable;
	}

	public boolean isRowSpanning() {
		return isRowSpanning;
	}

	public void setRowSpanning(boolean isRowSpanning) {
		this.isRowSpanning = isRowSpanning;
	}

	public boolean isColSpanning() {
		return isColSpanning;
	}

	public void setColSpanning(boolean isColSpanning) {
		this.isColSpanning = isColSpanning;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public int getStructureClass() {
		return StructureClass;
	}

	public void setStructureClass(int structureClass) {
		StructureClass = structureClass;
	}
}
