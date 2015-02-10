/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package stats;

/**
 * The Class TableStats. Used for statistics about one table
 */
public class TableStats {
	
	/** The num_of_cells. Processed - split */
	private int num_of_cells_proc;
	
	/** The num_of_cells. Unprocessed - nonsplit */
	private int num_of_cells_unproc;
	
	/** The num_of_chars. */
	private int num_of_chars;
	
	/** The num_columns. */
	private int num_columns;
	
	/** The num_of_header_cells. */
	private int num_of_header_cells;
		
	/** The num_of_empty_cells. */
	private int num_of_empty_cells;
	
	/** The num_of_pure_numeric_cells. */
	private int num_of_pure_numeric_cells;
	
	/** The num_of_part_numeric_cells. */
	private int num_of_part_numeric_cells;
	
	/** The num_of_text_cells. */
	private int num_of_text_cells;
	
	/** The num_of_colspanning_cells. */
	private int num_of_colspanning_cells;
	
	/** The num_of_rowspanning_cells. */
	private int num_of_rowspanning_cells;
	
	/** The num_of_header_rows. */
	private int num_of_header_rows;
	
	private int header_empty_cells;
	
	/** The num_of_body_rows. */
	private int num_of_body_rows;
	
	public void AddHeaderCell()
	{
		num_of_header_cells++;
	}
	
	/**
	 * Adds the header row.
	 */
	public void AddHeaderRow()
	{
		num_of_header_rows++;
	}
	
	public void AddHeaderEmptyCell()
	{
		header_empty_cells++;
	}
	
	/**
	 * Adds the body row.
	 */
	public void AddBodyRow()
	{
		num_of_body_rows++;
	}
	
	/**
	 * Adds the cell.
	 */
	public void AddUnprCell()
	{
		num_of_cells_unproc++;
	}
	
	
	/**
	 * Adds the cell.
	 */
	public void AddCell()
	{
		num_of_cells_proc++;
	}
	
	/**
	 * Adds the empty cell.
	 */
	public void AddEmptyCell()
	{
		num_of_empty_cells++;
	}
	
	/**
	 * Adds the pure numeric cell.
	 */
	public void AddPureNumericCell()
	{
		num_of_pure_numeric_cells++;
	}
	
	/**
	 * Adds the part numeric cell.
	 */
	public void AddPartNumericCell()
	{
		num_of_part_numeric_cells++;
	}
	
	/**
	 * Adds the text cell.
	 */
	public void AddTextCell()
	{
		num_of_text_cells++;
	}
	
	/**
	 * Adds the col spanning cell.
	 */
	public void AddColSpanningCell()
	{
		num_of_colspanning_cells++;
	}
	
	/**
	 * Adds the row spanning cell.
	 */
	public void AddRowSpanningCell()
	{
		num_of_rowspanning_cells++;
	}
	
	
	/**
	 * Gets the num_of_cells.
	 *
	 * @return the num_of_cells
	 */
	public int getNum_of_cells() {
		return num_of_cells_proc;
	}
	
	/**
	 * Sets the num_of_cells.
	 *
	 * @param num_of_cells the new num_of_cells
	 */
	public void setNum_of_cells(int num_of_cells) {
		this.num_of_cells_proc = num_of_cells;
	}
	
	/**
	 * Gets the num_of_empty_cells.
	 *
	 * @return the num_of_empty_cells
	 */
	public int getNum_of_empty_cells() {
		return num_of_empty_cells;
	}
	
	/**
	 * Sets the num_of_empty_cells.
	 *
	 * @param num_of_empty_cells the new num_of_empty_cells
	 */
	public void setNum_of_empty_cells(int num_of_empty_cells) {
		this.num_of_empty_cells = num_of_empty_cells;
	}
	
	/**
	 * Gets the num_of_pure_numeric_cells.
	 *
	 * @return the num_of_pure_numeric_cells
	 */
	public int getNum_of_pure_numeric_cells() {
		return num_of_pure_numeric_cells;
	}
	
	/**
	 * Sets the num_of_pure_numeric_cells.
	 *
	 * @param num_of_pure_numeric_cells the new num_of_pure_numeric_cells
	 */
	public void setNum_of_pure_numeric_cells(
			int num_of_pure_numeric_cells) {
		this.num_of_pure_numeric_cells = num_of_pure_numeric_cells;
	}
	
	/**
	 * Gets the num_of_part_numeric_cells.
	 *
	 * @return the num_of_part_numeric_cells
	 */
	public int getNum_of_part_numeric_cells() {
		return num_of_part_numeric_cells;
	}
	
	/**
	 * Sets the num_of_part_numeric_cells.
	 *
	 * @param num_of_part_numeric_cells the new num_of_part_numeric_cells
	 */
	public void setNum_of_part_numeric_cells(
			int num_of_part_numeric_cells) {
		this.num_of_part_numeric_cells = num_of_part_numeric_cells;
	}
	
	/**
	 * Gets the num_of_text_cells.
	 *
	 * @return the num_of_text_cells
	 */
	public int getNum_of_text_cells() {
		return num_of_text_cells;
	}
	
	/**
	 * Sets the num_of_text_cells.
	 *
	 * @param num_of_text_cells the new num_of_text_cells
	 */
	public void setNum_of_text_cells(int num_of_text_cells) {
		this.num_of_text_cells = num_of_text_cells;
	}
	
	/**
	 * Gets the num_of_colspanning_cells.
	 *
	 * @return the num_of_colspanning_cells
	 */
	public int getNum_of_colspanning_cells() {
		return num_of_colspanning_cells;
	}
	
	/**
	 * Sets the num_of_colspanning_cells.
	 *
	 * @param num_of_colspanning_cells the new num_of_colspanning_cells
	 */
	public void setNum_of_colspanning_cells(int num_of_colspanning_cells) {
		this.num_of_colspanning_cells = num_of_colspanning_cells;
	}
	
	/**
	 * Gets the num_of_rowspanning_cells.
	 *
	 * @return the num_of_rowspanning_cells
	 */
	public  int getNum_of_rowspanning_cells() {
		return num_of_rowspanning_cells;
	}
	
	/**
	 * Sets the num_of_rowspanning_cells.
	 *
	 * @param num_of_rowspanning_cells the new num_of_rowspanning_cells
	 */
	public void setNum_of_rowspanning_cells(int num_of_rowspanning_cells) {
		this.num_of_rowspanning_cells = num_of_rowspanning_cells;
	}
	
	/**
	 * Gets the num_of_header_rows.
	 *
	 * @return the num_of_header_rows
	 */
	public int getNum_of_header_rows() {
		return num_of_header_rows;
	}
	
	/**
	 * Sets the num_of_header_rows.
	 *
	 * @param num_of_header_rows the new num_of_header_rows
	 */
	public void setNum_of_header_rows(int num_of_header_rows) {
		this.num_of_header_rows = num_of_header_rows;
	}
	
	/**
	 * Gets the num_of_body_rows.
	 *
	 * @return the num_of_body_rows
	 */
	public int getNum_of_body_rows() {
		return num_of_body_rows;
	}
	
	/**
	 * Sets the num_of_body_rows.
	 *
	 * @param num_of_body_rows the new num_of_body_rows
	 */
	public void setNum_of_body_rows(int num_of_body_rows) {
		this.num_of_body_rows = num_of_body_rows;
	}

	public int getHeader_empty_cells() {
		return header_empty_cells;
	}

	public void setHeader_empty_cells(int header_empty_cells) {
		this.header_empty_cells = header_empty_cells;
	}

	/**
	 * @return the num_of_cells_unproc
	 */
	public int getNum_of_cells_unproc() {
		return num_of_cells_unproc;
	}

	/**
	 * @param num_of_cells_unproc the num_of_cells_unproc to set
	 */
	public void setNum_of_cells_unproc(int num_of_cells_unproc) {
		this.num_of_cells_unproc = num_of_cells_unproc;
	}

	/**
	 * @return the num_of_chars
	 */
	public int getNum_of_chars() {
		return num_of_chars;
	}

	/**
	 * @param num_of_chars the num_of_chars to set
	 */
	public void setNum_of_chars(int num_of_chars) {
		this.num_of_chars = num_of_chars;
	}

	/**
	 * @return the num_columns
	 */
	public int getNum_columns() {
		return num_columns;
	}

	/**
	 * @param num_columns the num_columns to set
	 */
	public void setNum_columns(int num_columns) {
		this.num_columns = num_columns;
	}

	/**
	 * @return the num_of_header_cells
	 */
	public int getNum_of_header_cells() {
		return num_of_header_cells;
	}

	/**
	 * @param num_of_header_cells the num_of_header_cells to set
	 */
	public void setNum_of_header_cells(int num_of_header_cells) {
		this.num_of_header_cells = num_of_header_cells;
	}

}
