/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package stats;

import Utils.Utilities;
import tablInEx.Cell;
import tablInEx.Table;

/**
 * 
 * The Class Statistics. Class is used for generating statistics about tables
 * @author Nikola Milosevic
 */
public class Statistics {
	
	/** The new table wo head. */
	private static boolean newTableWOHead = false;
	
	/** The new table wo body. */
	private static boolean newTableWOBody = false;
	
	/** The new image table. */
	private static boolean newImageTable = false;
	
	/** The new col spanning table. */
	private static boolean newColSpanningTable = false;
	
	/** The new row spanning table. */
	private static boolean newRowSpanningTable = false;
	
	/** The Image files. */
	private static String ImageFiles = "";
	
	/** The useful tables. */
	private static int usefulTables;
	
	private static int MatrixTables;
	private static int ListTables;
	private static int SubheaderTables;
	
	/** The tables_without_head. */
	private static int tables_without_head;
	
	/** The tables_without_body. */
	private static int tables_without_body;
	
	/** The tables_images. */
	private static int tables_images;
	
	/** The tables_colspanning. */
	private static int tables_colspanning;
	
	/** The tables_rowspanning. */
	private static int tables_rowspanning;
	
	
	/** The total_num_of_cells. */
	private static int total_num_of_cells;
	
	/** The total_num_of_tables. */
	private static int total_num_of_tables;
	
	/** The total_num_of_header_cells. */
	private static int total_num_of_header_cells;
	
	/** The total_num_of_header_rows. */
	private static int total_num_of_header_rows;
	
	/** The total_num_of_columns. */
	private static int total_num_of_columns;
	
	/** The total_num_of_rows. */
	private static int total_num_of_rows;
	
	/** The num_of_chars_in_cells. */
	private static int num_of_chars_in_cells;
	
	/** The num_of_chars_in_nonempty_cells. */
	private static int num_of_chars_in_nonempty_cells;
	
	/** The num_of_pure_numeric_cells. */
	private static int num_of_pure_numeric_cells;
	
	/** The num_of_numeric_cells. */
	private static int num_of_numeric_cells;
	
	/** The num_of_part_numeric_cells. */
	private static int num_of_part_numeric_cells; //has more then 60% of num chars
	
	/** The num_of_pure_text_cells. */
	private static int num_of_pure_text_cells;
	
	/** The num_of_columnspanning_cells. */
	private static int num_of_columnspanning_cells;
	
	/** The num_of_rowspanning_cells. */
	private static int num_of_rowspanning_cells;
	
	/** The num_of_empty_cells. */
	private static int num_of_empty_cells;
	
	/** The num_of_chars_in_pure_numeric_cells. */
	private static int num_of_chars_in_pure_numeric_cells;
	
	/** The num_of_chars_in_pure_text_cells. */
	private static int num_of_chars_in_pure_text_cells;
	
	/** The num_of_chars_in_numeric_cells. */
	private static int num_of_chars_in_numeric_cells;
	
	/** The num_of_chars_in_part_numeric_cells. */
	private static int num_of_chars_in_part_numeric_cells;
	
	//Calculated statistic data
	/** The mean_number_of_cells. */
	private static float mean_number_of_cells;
	
	/** The mean_number_of_header_cells. */
	private static float mean_number_of_header_cells;
	
	/** The mean_number_of_header_rows. */
	private static float mean_number_of_header_rows;
	
	/** The mean_number_of_columns. */
	private static float mean_number_of_columns;
	
	/** The mean_number_of_rows. */
	private static float mean_number_of_rows;
	
	/** The mean_number_of_chars_in_nonempty_cells. */
	private static float mean_number_of_chars_in_nonempty_cells;
	
	/** The mean_number_of_chars_in_cells. */
	private static float mean_number_of_chars_in_cells;
	
	/** The mean_number_of_chars_in_numeric_cells. */
	private static float mean_number_of_chars_in_numeric_cells;
	
	/** The mean_number_of_chars_in_part_numeric_cells. */
	private static float mean_number_of_chars_in_part_numeric_cells;
	
	/** The mean_number_of_chars_in_pure_numeric_cells. */
	private static float mean_number_of_chars_in_pure_numeric_cells;
	
	/** The mean_number_of_chars_in_pure_text_cells. */
	private static float mean_number_of_chars_in_pure_text_cells;
	
	/** The percentage_of_empty_cells. */
	private static float percentage_of_empty_cells;
	
	/** The percentage_of_pure_numeric_cells. */
	private static float percentage_of_pure_numeric_cells;
	
	/** The percentage_of_numeric_cells. */
	private static float percentage_of_numeric_cells;
	
	/** The percentage_of_pure_text_cells. */
	private static float percentage_of_pure_text_cells;
	
	/** The percentage_of_part_numeric_cells. */
	private static float percentage_of_part_numeric_cells;
	
	/**
	 * Adds the cell to statistics.
	 */
	public static void addCell()
	{
		setTotal_num_of_cells(getTotal_num_of_cells() + 1);
	}
	
	/**
	 * Adds the table to statistics.
	 */
	public static void addTable()
	{
		setTotal_num_of_tables(getTotal_num_of_tables() + 1);
		newTableWOHead = false;
		newTableWOBody = false;
		newImageTable = false;
		newColSpanningTable = false;
		newRowSpanningTable = false;
	}
	
	
	/**
	 * Adds the matrix table.
	 */
	public static void addMatrixTable()
	{
		MatrixTables++;
	}
	
	public static void addListTable()
	{
		ListTables++;
	}
	public static void addSubheaderTable()
	{
		SubheaderTables++;
	}
	
	/**
	 * Adds the header cell.
	 */
	public static void addHeaderCell()
	{
		Statistics.total_num_of_header_cells++;
	}
	
	/**
	 * Adds the header row.
	 */
	public static void addHeaderRow()
	{
		Statistics.total_num_of_header_rows++;
	}
	
	/**
	 * Adds the column.
	 */
	public static void addColumn()
	{
		Statistics.total_num_of_columns++;
	}
	
	/**
	 * Adds the column.
	 *
	 * @param columnsCount the columns count
	 */
	public static void addColumn(int columnsCount)
	{
		Statistics.total_num_of_columns+=columnsCount;	
	}
	
	/**
	 * Adds the row.
	 */
	public static void addRow()
	{
		Statistics.total_num_of_rows++;
	}
	
	/**
	 * Adds the row.
	 *
	 * @param rowCount the row count
	 */
	public static void addRow(int rowCount)
	{
		Statistics.total_num_of_rows+=rowCount;	
	}
	
	/**
	 * Adds the column spanning cell.
	 */
	public static void addColumnSpanningCell()
	{
		Statistics.num_of_columnspanning_cells++;
		if(newColSpanningTable==false)
		{
			newColSpanningTable = true;
			tables_colspanning++;
		}
	}
	
	/**
	 * Adds the row spanning cell.
	 */
	public static void addRowSpanningCell()
	{
		Statistics.setNum_of_rowspanning_cells(Statistics
				.getNum_of_rowspanning_cells() + 1);
		
		if(newRowSpanningTable==false)
		{
			newRowSpanningTable = true;
			tables_rowspanning++;
		}
	}
	
	/**
	 * Statistics for cell.
	 *
	 * @param table the table
	 * @param cell the cell
	 * @return the table
	 */
	public static Table statisticsForCell(Table table, Cell cell)
	{
		total_num_of_cells++;
		table.stat.AddCell();
		if(cell.isIs_header())
		{
			total_num_of_header_cells++;
		}
		num_of_chars_in_cells+=cell.getCell_content().length();
		if(cell.getCell_content().trim().equalsIgnoreCase(" ") || cell.getCell_content().trim().equalsIgnoreCase("") || (((int)cell.getCell_content().trim().charAt(0))== 160))
		{
			num_of_empty_cells++;
			table.stat.AddEmptyCell();
			cell.setCell_content("");
			if(cell.isIs_header())
				table.stat.AddHeaderEmptyCell();
			
		}
		else
		{
			num_of_chars_in_nonempty_cells+=cell.getCell_content().length();
		}
		if(Utilities.isNumeric(cell.getCell_content()))
		{
			num_of_pure_numeric_cells++;
			num_of_numeric_cells++;
			
			table.stat.AddPureNumericCell();
			
			num_of_chars_in_pure_numeric_cells+=cell.getCell_content().length();
			num_of_chars_in_numeric_cells+=cell.getCell_content().length();
		}
		
		int numbers = 0;
		int chars = 0;
		String tempCellVal = cell.getCell_content().replaceAll("[\\s\\xA0]","");
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
		if(proportion>0.49 && !Utilities.isNumeric(cell.getCell_content()))
		{
			num_of_numeric_cells++;
			num_of_part_numeric_cells++;
			
			table.stat.AddPartNumericCell();
			
			num_of_chars_in_numeric_cells+=cell.getCell_content().length();
			num_of_chars_in_part_numeric_cells+=cell.getCell_content().length();
		}
		if(proportion<=0.49 && !Utilities.isNumeric(cell.getCell_content()))
		{
			//text cell
			num_of_pure_text_cells++;
			
			table.stat.AddTextCell();
			
			num_of_chars_in_pure_text_cells+=cell.getCell_content().length();
		}
		
		return table;
	}
	
	/**
	 * Calculate statistics.
	 */
	public static void CalculateStatistics()
	{
		usefulTables = total_num_of_tables - tables_images - tables_without_body;
		mean_number_of_chars_in_cells = (float)num_of_chars_in_cells / total_num_of_cells;
		percentage_of_empty_cells = (float) num_of_empty_cells / total_num_of_cells;
		percentage_of_pure_numeric_cells = (float) num_of_pure_numeric_cells / total_num_of_cells;
		percentage_of_numeric_cells = (float) num_of_numeric_cells / total_num_of_cells;
		percentage_of_pure_text_cells = (float) num_of_pure_text_cells / total_num_of_cells;
		percentage_of_part_numeric_cells = (float) num_of_part_numeric_cells / total_num_of_cells;
	
		mean_number_of_cells = (float) total_num_of_cells / usefulTables;
		mean_number_of_header_cells = (float) total_num_of_header_cells / usefulTables;
		mean_number_of_header_rows = (float) total_num_of_header_rows / (usefulTables-tables_without_head);
		mean_number_of_columns = (float) total_num_of_columns / usefulTables;
		mean_number_of_rows = (float) total_num_of_rows / usefulTables;
	
		mean_number_of_chars_in_pure_numeric_cells = (float) num_of_chars_in_pure_numeric_cells / num_of_pure_numeric_cells;
		mean_number_of_chars_in_numeric_cells = (float) num_of_chars_in_numeric_cells / num_of_numeric_cells;
		mean_number_of_chars_in_part_numeric_cells = (float) num_of_chars_in_part_numeric_cells / num_of_part_numeric_cells;
		mean_number_of_chars_in_pure_text_cells = (float) num_of_chars_in_pure_text_cells / num_of_pure_text_cells;
		mean_number_of_chars_in_nonempty_cells = (float)num_of_chars_in_nonempty_cells / (total_num_of_cells - num_of_empty_cells);
	}
	
	/**
	 * Gets the total_num_of_cells.
	 *
	 * @return the total_num_of_cells
	 */
	public static int getTotal_num_of_cells() {
		return total_num_of_cells;
	}
	
	/**
	 * Sets the total_num_of_cells.
	 *
	 * @param total_num_of_cells the new total_num_of_cells
	 */
	public static void setTotal_num_of_cells(int total_num_of_cells) {
		Statistics.total_num_of_cells = total_num_of_cells;
	}
	
	/**
	 * Gets the total_num_of_tables.
	 *
	 * @return the total_num_of_tables
	 */
	public static int getTotal_num_of_tables() {
		return total_num_of_tables;
	}
	
	/**
	 * Sets the total_num_of_tables.
	 *
	 * @param total_num_of_tables the new total_num_of_tables
	 */
	public static void setTotal_num_of_tables(int total_num_of_tables) {
		Statistics.total_num_of_tables = total_num_of_tables;
	}
	
	/**
	 * Gets the total_num_of_header_cells.
	 *
	 * @return the total_num_of_header_cells
	 */
	public static int getTotal_num_of_header_cells() {
		return total_num_of_header_cells;
	}
	
	/**
	 * Sets the total_num_of_header_cells.
	 *
	 * @param total_num_of_header_cells the new total_num_of_header_cells
	 */
	public static void setTotal_num_of_header_cells(
			int total_num_of_header_cells) {
		Statistics.total_num_of_header_cells = total_num_of_header_cells;
	}
	
	/**
	 * Gets the total_num_of_header_rows.
	 *
	 * @return the total_num_of_header_rows
	 */
	public static int getTotal_num_of_header_rows() {
		return total_num_of_header_rows;
	}
	
	/**
	 * Sets the total_num_of_header_rows.
	 *
	 * @param total_num_of_header_rows the new total_num_of_header_rows
	 */
	public static void setTotal_num_of_header_rows(int total_num_of_header_rows) {
		Statistics.total_num_of_header_rows = total_num_of_header_rows;
	}
	
	/**
	 * Gets the total_num_of_columns.
	 *
	 * @return the total_num_of_columns
	 */
	public static int getTotal_num_of_columns() {
		return total_num_of_columns;
	}
	
	/**
	 * Sets the total_num_of_columns.
	 *
	 * @param total_num_of_columns the new total_num_of_columns
	 */
	public static void setTotal_num_of_columns(int total_num_of_columns) {
		Statistics.total_num_of_columns = total_num_of_columns;
	}
	
	/**
	 * Gets the num_of_chars_in_cells.
	 *
	 * @return the num_of_chars_in_cells
	 */
	public static int getNum_of_chars_in_cells() {
		return num_of_chars_in_cells;
	}
	
	/**
	 * Sets the num_of_chars_in_cells.
	 *
	 * @param num_of_chars_in_cells the new num_of_chars_in_cells
	 */
	public static void setNum_of_chars_in_cells(int num_of_chars_in_cells) {
		Statistics.num_of_chars_in_cells = num_of_chars_in_cells;
	}
	
	/**
	 * Gets the num_of_pure_numeric_cells.
	 *
	 * @return the num_of_pure_numeric_cells
	 */
	public static int getNum_of_pure_numeric_cells() {
		return num_of_pure_numeric_cells;
	}
	
	/**
	 * Sets the num_of_pure_numeric_cells.
	 *
	 * @param num_of_pure_numeric_cells the new num_of_pure_numeric_cells
	 */
	public static void setNum_of_pure_numeric_cells(
			int num_of_pure_numeric_cells) {
		Statistics.num_of_pure_numeric_cells = num_of_pure_numeric_cells;
	}
	
	/**
	 * Gets the num_of_numeric_cells.
	 *
	 * @return the num_of_numeric_cells
	 */
	public static int getNum_of_numeric_cells() {
		return num_of_numeric_cells;
	}
	
	/**
	 * Sets the num_of_numeric_cells.
	 *
	 * @param num_of_numeric_cells the new num_of_numeric_cells
	 */
	public static void setNum_of_numeric_cells(int num_of_numeric_cells) {
		Statistics.num_of_numeric_cells = num_of_numeric_cells;
	}
	
	/**
	 * Gets the num_of_part_numeric_cells.
	 *
	 * @return the num_of_part_numeric_cells
	 */
	public static int getNum_of_part_numeric_cells() {
		return num_of_part_numeric_cells;
	}
	
	/**
	 * Sets the num_of_part_numeric_cells.
	 *
	 * @param num_of_part_numeric_cells the new num_of_part_numeric_cells
	 */
	public static void setNum_of_part_numeric_cells(
			int num_of_part_numeric_cells) {
		Statistics.num_of_part_numeric_cells = num_of_part_numeric_cells;
	}
	
	/**
	 * Gets the num_of_pure_text_cells.
	 *
	 * @return the num_of_pure_text_cells
	 */
	public static int getNum_of_pure_text_cells() {
		return num_of_pure_text_cells;
	}
	
	/**
	 * Sets the num_of_pure_text_cells.
	 *
	 * @param num_of_pure_text_cells the new num_of_pure_text_cells
	 */
	public static void setNum_of_pure_text_cells(int num_of_pure_text_cells) {
		Statistics.num_of_pure_text_cells = num_of_pure_text_cells;
	}
	
	/**
	 * Gets the num_of_columnspanning_cells.
	 *
	 * @return the num_of_columnspanning_cells
	 */
	public static int getNum_of_columnspanning_cells() {
		return num_of_columnspanning_cells;
	}
	
	/**
	 * Sets the num_of_columnspanning_cells.
	 *
	 * @param num_of_columnspanning_cells the new num_of_columnspanning_cells
	 */
	public static void setNum_of_columnspanning_cells(
			int num_of_columnspanning_cells) {
		Statistics.num_of_columnspanning_cells = num_of_columnspanning_cells;
	}
	
	/**
	 * Gets the num_of_empty_cells.
	 *
	 * @return the num_of_empty_cells
	 */
	public static int getNum_of_empty_cells() {
		return num_of_empty_cells;
	}
	
	/**
	 * Sets the num_of_empty_cells.
	 *
	 * @param num_of_empty_cells the new num_of_empty_cells
	 */
	public static void setNum_of_empty_cells(int num_of_empty_cells) {
		Statistics.num_of_empty_cells = num_of_empty_cells;
	}
	
	/**
	 * Gets the num_of_chars_in_pure_numeric_cells.
	 *
	 * @return the num_of_chars_in_pure_numeric_cells
	 */
	public static int getNum_of_chars_in_pure_numeric_cells() {
		return num_of_chars_in_pure_numeric_cells;
	}
	
	/**
	 * Sets the num_of_chars_in_pure_numeric_cells.
	 *
	 * @param num_of_chars_in_pure_numeric_cells the new num_of_chars_in_pure_numeric_cells
	 */
	public static void setNum_of_chars_in_pure_numeric_cells(
			int num_of_chars_in_pure_numeric_cells) {
		Statistics.num_of_chars_in_pure_numeric_cells = num_of_chars_in_pure_numeric_cells;
	}
	
	/**
	 * Gets the num_of_chars_in_pure_text_cells.
	 *
	 * @return the num_of_chars_in_pure_text_cells
	 */
	public static int getNum_of_chars_in_pure_text_cells() {
		return num_of_chars_in_pure_text_cells;
	}
	
	/**
	 * Sets the num_of_chars_in_pure_text_cells.
	 *
	 * @param num_of_chars_in_pure_text_cells the new num_of_chars_in_pure_text_cells
	 */
	public static void setNum_of_chars_in_pure_text_cells(
			int num_of_chars_in_pure_text_cells) {
		Statistics.num_of_chars_in_pure_text_cells = num_of_chars_in_pure_text_cells;
	}
	
	/**
	 * Gets the num_of_chars_in_numeric_cells.
	 *
	 * @return the num_of_chars_in_numeric_cells
	 */
	public static int getNum_of_chars_in_numeric_cells() {
		return num_of_chars_in_numeric_cells;
	}
	
	/**
	 * Sets the num_of_chars_in_numeric_cells.
	 *
	 * @param num_of_chars_in_numeric_cells the new num_of_chars_in_numeric_cells
	 */
	public static void setNum_of_chars_in_numeric_cells(
			int num_of_chars_in_numeric_cells) {
		Statistics.num_of_chars_in_numeric_cells = num_of_chars_in_numeric_cells;
	}
	
	/**
	 * Gets the num_of_chars_in_part_numeric_cells.
	 *
	 * @return the num_of_chars_in_part_numeric_cells
	 */
	public static int getNum_of_chars_in_part_numeric_cells() {
		return num_of_chars_in_part_numeric_cells;
	}
	
	/**
	 * Sets the num_of_chars_in_part_numeric_cells.
	 *
	 * @param num_of_chars_in_part_numeric_cells the new num_of_chars_in_part_numeric_cells
	 */
	public static void setNum_of_chars_in_part_numeric_cells(
			int num_of_chars_in_part_numeric_cells) {
		Statistics.num_of_chars_in_part_numeric_cells = num_of_chars_in_part_numeric_cells;
	}

	/**
	 * Gets the total_num_of_rows.
	 *
	 * @return the total_num_of_rows
	 */
	public static int getTotal_num_of_rows() {
		return total_num_of_rows;
	}

	/**
	 * Sets the total_num_of_rows.
	 *
	 * @param total_num_of_rows the new total_num_of_rows
	 */
	public static void setTotal_num_of_rows(int total_num_of_rows) {
		Statistics.total_num_of_rows = total_num_of_rows;
	}

	/**
	 * Gets the mean_number_of_cells.
	 *
	 * @return the mean_number_of_cells
	 */
	public static float getMean_number_of_cells() {
		return mean_number_of_cells;
	}

	/**
	 * Sets the mean_number_of_cells.
	 *
	 * @param mean_number_of_cells the new mean_number_of_cells
	 */
	public static void setMean_number_of_cells(float mean_number_of_cells) {
		Statistics.mean_number_of_cells = mean_number_of_cells;
	}

	/**
	 * Gets the mean_number_of_header_cells.
	 *
	 * @return the mean_number_of_header_cells
	 */
	public static float getMean_number_of_header_cells() {
		return mean_number_of_header_cells;
	}

	/**
	 * Sets the mean_number_of_header_cells.
	 *
	 * @param mean_number_of_header_cells the new mean_number_of_header_cells
	 */
	public static void setMean_number_of_header_cells(
			float mean_number_of_header_cells) {
		Statistics.mean_number_of_header_cells = mean_number_of_header_cells;
	}

	/**
	 * Gets the mean_number_of_header_rows.
	 *
	 * @return the mean_number_of_header_rows
	 */
	public static float getMean_number_of_header_rows() {
		return mean_number_of_header_rows;
	}

	/**
	 * Sets the mean_number_of_header_rows.
	 *
	 * @param mean_number_of_header_rows the new mean_number_of_header_rows
	 */
	public static void setMean_number_of_header_rows(
			float mean_number_of_header_rows) {
		Statistics.mean_number_of_header_rows = mean_number_of_header_rows;
	}

	/**
	 * Gets the mean_number_of_columns.
	 *
	 * @return the mean_number_of_columns
	 */
	public static float getMean_number_of_columns() {
		return mean_number_of_columns;
	}

	/**
	 * Sets the mean_number_of_columns.
	 *
	 * @param mean_number_of_columns the new mean_number_of_columns
	 */
	public static void setMean_number_of_columns(float mean_number_of_columns) {
		Statistics.mean_number_of_columns = mean_number_of_columns;
	}

	/**
	 * Gets the mean_number_of_rows.
	 *
	 * @return the mean_number_of_rows
	 */
	public static float getMean_number_of_rows() {
		return mean_number_of_rows;
	}

	/**
	 * Sets the mean_number_of_rows.
	 *
	 * @param mean_number_of_rows the new mean_number_of_rows
	 */
	public static void setMean_number_of_rows(float mean_number_of_rows) {
		Statistics.mean_number_of_rows = mean_number_of_rows;
	}

	/**
	 * Gets the mean_number_of_chars_in_cells.
	 *
	 * @return the mean_number_of_chars_in_cells
	 */
	public static float getMean_number_of_chars_in_cells() {
		return mean_number_of_chars_in_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_cells.
	 *
	 * @param mean_number_of_chars_in_cells the new mean_number_of_chars_in_cells
	 */
	public static void setMean_number_of_chars_in_cells(
			float mean_number_of_chars_in_cells) {
		Statistics.mean_number_of_chars_in_cells = mean_number_of_chars_in_cells;
	}

	/**
	 * Gets the percentage_of_empty_cells.
	 *
	 * @return the percentage_of_empty_cells
	 */
	public static float getPercentage_of_empty_cells() {
		return percentage_of_empty_cells;
	}

	/**
	 * Sets the percentage_of_empty_cells.
	 *
	 * @param percentage_of_empty_cells the new percentage_of_empty_cells
	 */
	public static void setPercentage_of_empty_cells(
			float percentage_of_empty_cells) {
		Statistics.percentage_of_empty_cells = percentage_of_empty_cells;
	}

	/**
	 * Gets the percentage_of_pure_numeric_cells.
	 *
	 * @return the percentage_of_pure_numeric_cells
	 */
	public static float getPercentage_of_pure_numeric_cells() {
		return percentage_of_pure_numeric_cells;
	}

	/**
	 * Sets the percentage_of_pure_numeric_cells.
	 *
	 * @param percentage_of_pure_numeric_cells the new percentage_of_pure_numeric_cells
	 */
	public static void setPercentage_of_pure_numeric_cells(
			float percentage_of_pure_numeric_cells) {
		Statistics.percentage_of_pure_numeric_cells = percentage_of_pure_numeric_cells;
	}

	/**
	 * Gets the percentage_of_numeric_cells.
	 *
	 * @return the percentage_of_numeric_cells
	 */
	public static float getPercentage_of_numeric_cells() {
		return percentage_of_numeric_cells;
	}

	/**
	 * Sets the percentage_of_numeric_cells.
	 *
	 * @param percentage_of_numeric_cells the new percentage_of_numeric_cells
	 */
	public static void setPercentage_of_numeric_cells(
			float percentage_of_numeric_cells) {
		Statistics.percentage_of_numeric_cells = percentage_of_numeric_cells;
	}

	/**
	 * Gets the percentage_of_pure_text_cells.
	 *
	 * @return the percentage_of_pure_text_cells
	 */
	public static float getPercentage_of_pure_text_cells() {
		return percentage_of_pure_text_cells;
	}

	/**
	 * Sets the percentage_of_pure_text_cells.
	 *
	 * @param percentage_of_pure_text_cells the new percentage_of_pure_text_cells
	 */
	public static void setPercentage_of_pure_text_cells(
			float percentage_of_pure_text_cells) {
		Statistics.percentage_of_pure_text_cells = percentage_of_pure_text_cells;
	}

	/**
	 * Gets the percentage_of_part_numeric_cells.
	 *
	 * @return the percentage_of_part_numeric_cells
	 */
	public static float getPercentage_of_part_numeric_cells() {
		return percentage_of_part_numeric_cells;
	}

	/**
	 * Sets the percentage_of_part_numeric_cells.
	 *
	 * @param percentage_of_part_numeric_cells the new percentage_of_part_numeric_cells
	 */
	public static void setPercentage_of_part_numeric_cells(
			float percentage_of_part_numeric_cells) {
		Statistics.percentage_of_part_numeric_cells = percentage_of_part_numeric_cells;
	}

	/**
	 * Gets the mean_number_of_chars_in_numeric_cells.
	 *
	 * @return the mean_number_of_chars_in_numeric_cells
	 */
	public static float getMean_number_of_chars_in_numeric_cells() {
		return mean_number_of_chars_in_numeric_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_numeric_cells.
	 *
	 * @param mean_number_of_chars_in_numeric_cells the new mean_number_of_chars_in_numeric_cells
	 */
	public static void setMean_number_of_chars_in_numeric_cells(
			float mean_number_of_chars_in_numeric_cells) {
		Statistics.mean_number_of_chars_in_numeric_cells = mean_number_of_chars_in_numeric_cells;
	}

	/**
	 * Gets the mean_number_of_chars_in_part_numeric_cells.
	 *
	 * @return the mean_number_of_chars_in_part_numeric_cells
	 */
	public static float getMean_number_of_chars_in_part_numeric_cells() {
		return mean_number_of_chars_in_part_numeric_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_part_numeric_cells.
	 *
	 * @param mean_number_of_chars_in_part_numeric_cells the new mean_number_of_chars_in_part_numeric_cells
	 */
	public static void setMean_number_of_chars_in_part_numeric_cells(
			float mean_number_of_chars_in_part_numeric_cells) {
		Statistics.mean_number_of_chars_in_part_numeric_cells = mean_number_of_chars_in_part_numeric_cells;
	}

	/**
	 * Gets the mean_number_of_chars_in_pure_text_cells.
	 *
	 * @return the mean_number_of_chars_in_pure_text_cells
	 */
	public static float getMean_number_of_chars_in_pure_text_cells() {
		return mean_number_of_chars_in_pure_text_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_pure_text_cells.
	 *
	 * @param mean_number_of_chars_in_pure_text_cells the new mean_number_of_chars_in_pure_text_cells
	 */
	public static void setMean_number_of_chars_in_pure_text_cells(
			float mean_number_of_chars_in_pure_text_cells) {
		Statistics.mean_number_of_chars_in_pure_text_cells = mean_number_of_chars_in_pure_text_cells;
	}
	
	/**
	 * Make output statistic string.
	 *
	 * @return the string
	 */
	public static String makeOutputStatisticString()
	{
		String output = "Statistics: \r\n";
		output+="Total number of cells: ,"+total_num_of_cells+"\r\n";
		
		output += "Total number of tables: ," + total_num_of_tables+ "\r\n";
		output += "Total number of header cells: ," + total_num_of_header_cells + "\r\n";
		output += "Total number of header rows: ," + total_num_of_header_rows + "\r\n";
		output += "Total number of columns: ," + total_num_of_columns+ "\r\n";
		output += "Total number of rows: ," + total_num_of_rows+"\r\n";
		output += "Total number of characters in all cells: ,"+num_of_chars_in_cells+ "\r\n";
		output += "Total number of of chars in non empty cells: ,"+num_of_chars_in_nonempty_cells+ "\r\n";
		
		output += "Total number of pure numeric cells: ,"+num_of_pure_numeric_cells+ "\r\n";
		output += "Total number of partially numeric cells: ,"+num_of_part_numeric_cells+ "\r\n";
		output += "Total number of numeric cells: ,"+num_of_numeric_cells+ "\r\n";
		output += "Total number of pure text cells: ,"+num_of_pure_text_cells+ "\r\n";
		output += "Total number of column spanning cells: ,"+num_of_columnspanning_cells+ "\r\n";
		output += "Total number of empty cells: ,"+num_of_empty_cells+ "\r\n";
		output += "Total number chars in pure text cells: ,"+num_of_chars_in_pure_text_cells+ "\r\n";
		output += "Total number chars in pure numeric cells: ,"+num_of_chars_in_pure_numeric_cells+ "\r\n";	
		output += "Total number chars in part numeric cells: ,"+num_of_chars_in_part_numeric_cells+ "\r\n";
		output += "Total number chars in numeric cells: ,"+num_of_chars_in_numeric_cells+ "\r\n";
		
		
		output += "Mean number of cells: ,"+mean_number_of_cells+ "\r\n";
		output += "Mean number of header cells: ,"+mean_number_of_header_cells+ "\r\n";
		output += "Mean number of header rows: ,"+mean_number_of_header_rows+ "\r\n";
		output += "Mean number of columns: ,"+mean_number_of_columns+ "\r\n";
		output += "Mean number of rows: ,"+mean_number_of_rows+ "\r\n";
		
		output += "Mean number of chars in cells: ,"+mean_number_of_chars_in_cells+ "\r\n";
		output += "Mean number of chars in part numeric cells: ,"+mean_number_of_chars_in_part_numeric_cells+ "\r\n";
		output += "Mean number of chars in pure numeric cells: ,"+mean_number_of_chars_in_pure_numeric_cells+ "\r\n";
		output += "Mean number of chars in numeric cells: ,"+mean_number_of_chars_in_numeric_cells+ "\r\n";
		output += "Mean number of chars in pure text cells: ,"+mean_number_of_chars_in_pure_text_cells+ "\r\n";
		output += "Mean number of chars in non empty cells: ,"+mean_number_of_chars_in_nonempty_cells+"\r\n";
		
		output += "percentage of empty cells: ,"+(percentage_of_empty_cells*100)+ "\r\n";
		output += "percentage of pure numeric cells: ,"+(percentage_of_pure_numeric_cells*100)+ "\r\n";
		output += "percentage of part numeric cells: ,"+(percentage_of_part_numeric_cells*100)+ "\r\n";
		output += "percentage of numeric cells: ,"+(percentage_of_numeric_cells*100)+ "\r\n";
		output += "percentage of text cells: ,"+(percentage_of_pure_text_cells*100)+ "\r\n";
		
		output += "Tables without head: ,"+tables_without_head+ "\r\n";
		output += "Tables without body: ,"+tables_without_body+ "\r\n";
		output += "Tables with no XML presentation (images or similar): ,"+tables_images+ "\r\n";
		output += "Tables with colspans: ,"+tables_colspanning+ "\r\n";
		output += "Tables with rowspans: ,"+tables_rowspanning+ "\r\n";	
		
		output += "Useful tables (with body and cells): ,"+usefulTables+ "\r\n";	
		
		output+="Image Tables are in Files:"+ImageFiles;
		output+="\r\n";
		output += "Matrix tables: ,"+MatrixTables+ "\r\n";	
		output += "List tables: ,"+ListTables+ "\r\n";	
		output += "Subheader tables: ,"+SubheaderTables+ "\r\n";	
		
		return output;
	}
	
	/**
	 * Table without head.
	 */
	public static void TableWithoutHead()
	{
		if(newTableWOHead==false)
		{
			newTableWOHead = true;
			tables_without_head++;
		}
	}
	
	/**
	 * Table without body.
	 */
	public static void TableWithoutBody()
	{
		if(newTableWOBody==false)
		{
			newTableWOBody = true;
			tables_without_body++;
		}
	}
	
	/**
	 * Image table.
	 *
	 * @param FileName the file name
	 */
	public static void ImageTable(String FileName)
	{
		if(newImageTable==false)
		{
			newImageTable = true;
			tables_images++;
			ImageFiles +=","+FileName;
		}
	}

	/**
	 * Gets the mean_number_of_chars_in_pure_numeric_cells.
	 *
	 * @return the mean_number_of_chars_in_pure_numeric_cells
	 */
	public static float getMean_number_of_chars_in_pure_numeric_cells() {
		return mean_number_of_chars_in_pure_numeric_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_pure_numeric_cells.
	 *
	 * @param mean_number_of_chars_in_pure_numeric_cells the new mean_number_of_chars_in_pure_numeric_cells
	 */
	public static void setMean_number_of_chars_in_pure_numeric_cells(
			float mean_number_of_chars_in_pure_numeric_cells) {
		Statistics.mean_number_of_chars_in_pure_numeric_cells = mean_number_of_chars_in_pure_numeric_cells;
	}

	/**
	 * Gets the mean_number_of_chars_in_nonempty_cells.
	 *
	 * @return the mean_number_of_chars_in_nonempty_cells
	 */
	public static float getMean_number_of_chars_in_nonempty_cells() {
		return mean_number_of_chars_in_nonempty_cells;
	}

	/**
	 * Sets the mean_number_of_chars_in_nonempty_cells.
	 *
	 * @param mean_number_of_chars_in_nonempty_cells the new mean_number_of_chars_in_nonempty_cells
	 */
	public static void setMean_number_of_chars_in_nonempty_cells(
			float mean_number_of_chars_in_nonempty_cells) {
		Statistics.mean_number_of_chars_in_nonempty_cells = mean_number_of_chars_in_nonempty_cells;
	}

	/**
	 * Gets the num_of_rowspanning_cells.
	 *
	 * @return the num_of_rowspanning_cells
	 */
	public static int getNum_of_rowspanning_cells() {
		return num_of_rowspanning_cells;
	}

	/**
	 * Sets the num_of_rowspanning_cells.
	 *
	 * @param num_of_rowspanning_cells the new num_of_rowspanning_cells
	 */
	public static void setNum_of_rowspanning_cells(int num_of_rowspanning_cells) {
		Statistics.num_of_rowspanning_cells = num_of_rowspanning_cells;
	}

	/**
	 * Gets the useful tables.
	 *
	 * @return the useful tables
	 */
	public static int getUsefulTables() {
		return usefulTables;
	}

	/**
	 * Sets the useful tables.
	 *
	 * @param usefulTables the new useful tables
	 */
	public static void setUsefulTables(int usefulTables) {
		Statistics.usefulTables = usefulTables;
	}

	public static int getMatrixTables() {
		return MatrixTables;
	}

	public static void setMatrixTables(int matrixTables) {
		MatrixTables = matrixTables;
	}

	public static int getListTables() {
		return ListTables;
	}

	public static void setListTables(int listTables) {
		ListTables = listTables;
	}

	public static int getSubheaderTables() {
		return SubheaderTables;
	}

	public static void setSubheaderTables(int subheaderTables) {
		SubheaderTables = subheaderTables;
	}
	

}
