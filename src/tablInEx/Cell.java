/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.util.LinkedList;

import Utils.Utilities;

/**
 * The Class Cell. One cell of the table. Contains all necessary information about cell
 * @author Nikola Milosevic
 */
public class Cell {
	
	private String subheader_values;
	
	private String stub_values;
	
	private String header_values;
	
	public LinkedList<String>headers = new LinkedList<String>();
	public LinkedList<String>stubs = new LinkedList<String>();
	
	private String head00;
	
	private boolean isSubheader = false;
	
	/** The row_number. */
	private int row_number;
	
	/** The column_number. */
	private int column_number;
	
	/** The cell_content. */
	private String cell_content;
	
	/** The is_header. */
	private boolean is_header;
	
	/** The is_stub. */
	private boolean is_stub;
	
	private boolean is_filled = false;
	
	/** The stub_probability. */
	private float stub_probability;
	
	/** The header_probability. */
	private float header_probability;
	
	/** The is_rowspanning. */
	private boolean is_rowspanning;
	
	/** The is_columnspanning. */
	private boolean is_columnspanning;
	
	/** The cells_rowspanning. */
	private int cells_rowspanning;
	
	/** The rowspanning_index. */
	private int rowspanning_index;
	
	/** The columnspanning_index. */
	private int columnspanning_index;
	
	/** The cells_columnspanning. */
	private int cells_columnspanning;
	
	//Constructors
	/**
	 * Instantiates a new cell.
	 *
	 * @param i the i
	 * @param j the j
	 */
	public Cell(int i, int j)
	{
		column_number = i;
		row_number = j;
	}
	
	// Getters and setters
	/**
	 * Gets the row_number.
	 *
	 * @return the row_number
	 */
	public int getRow_number() {
		return row_number;
	}
	
	/**
	 * Sets the row_number.
	 *
	 * @param row_number the new row_number
	 */
	public void setRow_number(int row_number) {
		this.row_number = row_number;
	}
	
	/**
	 * Gets the column_number.
	 *
	 * @return the column_number
	 */
	public int getColumn_number() {
		return column_number;
	}
	
	/**
	 * Sets the column_number.
	 *
	 * @param column_number the new column_number
	 */
	public void setColumn_number(int column_number) {
		this.column_number = column_number;
	}
	
	/**
	 * Gets the cell_content.
	 *
	 * @return the cell_content
	 */
	public String getCell_content() {
		return cell_content;
	}
	
	/**
	 * Sets the cell_content.
	 *
	 * @param cell_content the new cell_content
	 */
	public void setCell_content(String cell_content) {
		this.cell_content = cell_content;
	}
	
	/**
	 * Checks if is is_header.
	 *
	 * @return true, if is is_header
	 */
	public boolean isIs_header() {
		return is_header;
	}
	
	/**
	 * Sets the is_header.
	 *
	 * @param is_header the new is_header
	 */
	public void setIs_header(boolean is_header) {
		this.is_header = is_header;
	}
	
	/**
	 * Checks if is is_stub.
	 *
	 * @return true, if is is_stub
	 */
	public boolean isIs_stub() {
		return is_stub;
	}
	
	/**
	 * Sets the is_stub.
	 *
	 * @param is_stub the new is_stub
	 */
	public void setIs_stub(boolean is_stub) {
		this.is_stub = is_stub;
	}
	
	/**
	 * Gets the stub_probability.
	 *
	 * @return the stub_probability
	 */
	public float getStub_probability() {
		return stub_probability;
	}
	
	/**
	 * Sets the stub_probability.
	 *
	 * @param stub_probability the new stub_probability
	 */
	public void setStub_probability(float stub_probability) {
		this.stub_probability = stub_probability;
	}
	
	/**
	 * Gets the header_probability.
	 *
	 * @return the header_probability
	 */
	public float getHeader_probability() {
		return header_probability;
	}
	
	/**
	 * Sets the header_probability.
	 *
	 * @param header_probability the new header_probability
	 */
	public void setHeader_probability(float header_probability) {
		this.header_probability = header_probability;
	}
	
	/**
	 * Checks if is is_rowspanning.
	 *
	 * @return true, if is is_rowspanning
	 */
	public boolean isIs_rowspanning() {
		return is_rowspanning;
	}
	
	/**
	 * Sets the is_rowspanning.
	 *
	 * @param is_rowspanning the new is_rowspanning
	 */
	public void setIs_rowspanning(boolean is_rowspanning) {
		this.is_rowspanning = is_rowspanning;
	}
	
	/**
	 * Checks if is is_columnspanning.
	 *
	 * @return true, if is is_columnspanning
	 */
	public boolean isIs_columnspanning() {
		return is_columnspanning;
	}
	
	/**
	 * Sets the is_columnspanning.
	 *
	 * @param is_columnspanning the new is_columnspanning
	 */
	public void setIs_columnspanning(boolean is_columnspanning) {
		this.is_columnspanning = is_columnspanning;
	}
	
	/**
	 * Gets the cells_rowspanning.
	 *
	 * @return the cells_rowspanning
	 */
	public int getCells_rowspanning() {
		return cells_rowspanning;
	}
	
	/**
	 * Sets the cells_rowspanning.
	 *
	 * @param cells_rowspanning the new cells_rowspanning
	 */
	public void setCells_rowspanning(int cells_rowspanning) {
		this.cells_rowspanning = cells_rowspanning;
	}
	
	/**
	 * Gets the cells_columnspanning.
	 *
	 * @return the cells_columnspanning
	 */
	public int getCells_columnspanning() {
		return cells_columnspanning;
	}
	
	/**
	 * Sets the cells_columnspanning.
	 *
	 * @param cells_columnspanning the new cells_columnspanning
	 */
	public void setCells_columnspanning(int cells_columnspanning) {
		this.cells_columnspanning = cells_columnspanning;
	}
	
	
	public String getCellType()
	{
		if(Utilities.isNumeric(this.getCell_content()))
		{
			return "Numeric";
		}
		
		int numbers = 0;
		int chars = 0;
		String tempCellVal = this.getCell_content().replaceAll("[\\s\\xA0]","");
		for(int i=0;i<tempCellVal.length();i++)
		{
			if(Utilities.isNumeric(tempCellVal.substring(i, i+1)) )
			{
				numbers++;
			}
			else
			{
				chars++;
			}
		}
		float proportion = (float)numbers / (chars+numbers);
		//part numeric cell
		if(proportion>0.49 && !Utilities.isNumeric(this.getCell_content()))
		{
			return "Partially Numeric";
		}
		if(proportion<=0.49 && !Utilities.isNumeric(this.getCell_content()))
		{
			return "Text";
		}
		return "Other";
	}
	
	/**
	 * Sets the cell values. For spanning cells
	 *
	 * @param cell the cell
	 * @param cell_content the cell_content
	 * @param is_columnspanning the is_columnspanning
	 * @param colspanVal the colspan val
	 * @param is_rowSpanning the is_row spanning
	 * @param rowspanning the rowspanning
	 * @param isHeader the is header
	 * @param headerProbability the header probability
	 * @param isStub the is stub
	 * @param stubProbability the stub probability
	 * @param ColumnIndex the column index
	 * @param RowIndex the row index
	 * @param columnspanning_index the columnspanning_index
	 * @param rowspanning_index the rowspanning_index
	 * @return the cell
	 */
	public static Cell setCellValues(Article a, Cell cell, String cell_content, boolean is_columnspanning, int colspanVal, boolean is_rowSpanning, int rowspanning, boolean isHeader,float headerProbability, boolean isStub, float stubProbability, int ColumnIndex,int RowIndex, int columnspanning_index,int rowspanning_index)
	{
		cell.setCell_content(cell_content);
		cell.setCells_columnspanning(colspanVal);
		cell.setHeader_probability(headerProbability);
		cell.setIs_header(isHeader);
		cell.setIs_columnspanning(is_columnspanning);
		cell.setIs_rowspanning(is_rowSpanning);
		cell.setColumn_number(ColumnIndex);
		cell.setRow_number(RowIndex);
		cell.setIs_stub(isStub);
		cell.setStub_probability(stubProbability);
		cell.rowspanning_index = rowspanning_index;
		cell.columnspanning_index = columnspanning_index;
		cell.is_filled = true;
		if(TablInExMain.learnheaders && cell.isIs_header()){
		if(!TablInExMain.headermap.containsKey(cell.getCell_content()))
		{
			TablInExMain.headermap.put(cell.getCell_content(), 1);
		}
		else
		{
			int freq = TablInExMain.headermap.get(cell.getCell_content());
			freq++;
			TablInExMain.headermap.put(cell.getCell_content(), freq);
		}
		}
		
		if(TablInExMain.learnheaders && cell.isIs_stub()){
			if(!TablInExMain.stubmap.containsKey(cell.getCell_content()))
			{
				TablInExMain.stubmap.put(cell.getCell_content(), 1);
			}
			else
			{
				int freq = TablInExMain.stubmap.get(cell.getCell_content());
				freq++;
				TablInExMain.stubmap.put(cell.getCell_content(), freq);
			}
			}
		if((cell.getCell_content().toLowerCase().contains("bmi")||cell.getCell_content().toLowerCase().contains("b.m.i.")||cell.getCell_content().toLowerCase().contains("weight")||cell.getCell_content().toLowerCase().contains("body mass index")||cell.getCell_content().toLowerCase().contains("bodyweight")||cell.getCell_content().toLowerCase().contains("quetelet index")))
        {
			if(!TablInExMain.PMCBMI.contains(a.getPmc()))
			{
				TablInExMain.PMCBMI.add(a.getPmc());
			}
        }
		
		return cell;

	}

	/**
	 * Gets the rowspanning_index.
	 *
	 * @return the rowspanning_index
	 */
	public int getRowspanning_index() {
		return rowspanning_index;
	}

	/**
	 * Sets the rowspanning_index.
	 *
	 * @param rowspanning_index the new rowspanning_index
	 */
	public void setRowspanning_index(int rowspanning_index) {
		this.rowspanning_index = rowspanning_index;
	}

	/**
	 * Gets the columnspanning_index.
	 *
	 * @return the columnspanning_index
	 */
	public int getColumnspanning_index() {
		return columnspanning_index;
	}

	/**
	 * Sets the columnspanning_index.
	 *
	 * @param columnspanning_index the new columnspanning_index
	 */
	public void setColumnspanning_index(int columnspanning_index) {
		this.columnspanning_index = columnspanning_index;
	}

	public boolean isIs_filled() {
		return is_filled;
	}

	public void setIs_filled(boolean is_filled) {
		this.is_filled = is_filled;
	}

	public String getStub_values() {
		return stub_values;
	}

	public void setStub_values(String stub_values) {
		this.stub_values = stub_values;
	}

	public String getHeader_values() {
		return header_values;
	}

	public void setHeader_values(String header_values) {
		this.header_values = header_values;
	}

	public String getHead00() {
		return head00;
	}

	public void setHead00(String head00) {
		this.head00 = head00;
	}

	public String getSubheader_values() {
		return subheader_values;
	}

	public void setSubheader_values(String subheader_values) {
		this.subheader_values = subheader_values;
	}

	public boolean isSubheader() {
		return isSubheader;
	}

	public void setSubheader(boolean isSubheader) {
		this.isSubheader = isSubheader;
	}
	
}
