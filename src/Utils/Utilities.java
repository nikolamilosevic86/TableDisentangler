/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tablInEx.Cell;
import tablInEx.TablInExMain;
import tablInEx.Table;

/**
 * The Class Utilities.
 */
public class Utilities {

	public static LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);

		LinkedHashMap sortedMap = new LinkedHashMap();

		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = passedMap.get(key).toString();
				String comp2 = val.toString();

				if (comp1.equals(comp2)) {
					passedMap.remove(key);
					mapKeys.remove(key);
					sortedMap.put((String) key, (Integer) val);
					break;
				}

			}

		}
		return sortedMap;
	}

	public static boolean isEmptyRow(Cell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			if (!Utilities.isSpaceOrEmpty(cells[i].getCell_content()))
				return false;
		}
		return true;
	}

	public static boolean isOneCellFilledRow(Cell[] cells) {
		int filledCells = 0;
		if (cells.length == 1)
			return false;

		for (int i = 0; i < cells.length; i++) {
			if (cells[i].isIs_columnspanning()
					&& cells[i].getCells_columnspanning() == cells.length)
				return true;
			if (!isSpaceOrEmpty(cells[i].getCell_content())) {
				filledCells++;
			}
		}
		if (filledCells == 1)
			return true;
		else
			return false;

	}

	public static Table FixTablesHeader(Table table) {
		if (table.cells==null)
			return table;
		try{
		Cell[][] cells = table.cells;
		int oldNumofHeaderRows = table.stat.getNum_of_header_rows();
		int oldNumOfBodyRows = table.stat.getNum_of_body_rows();
		if (cells == null)
			return table;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null && cells[i].length <= 0)
				continue;
			try{
				cells[i][0].isIs_header();
			}catch(ArrayIndexOutOfBoundsException ex)
			{
				continue;
			}
			if (cells[i][0].isIs_header())
				continue;
			if (isEmptyRow(cells[i]) && cells[i][0].isBreakingLineOverRow()
					&& i - 2 >= 0 && cells[i - 2][0].isIs_header()
					&& isOneCellFilledRow(cells[i - 1])) {
				for (int k = 0; k < cells[i].length; k++) {
					for (int j = i - 2; j <= i; j++) {
						if (cells[j][k].isIs_header())
							continue;
						cells[j][k].setIs_header(false);
						cells[j][k].setIs_subheader(true);
					}
				}
				table.stat.setNum_of_header_rows(table.stat
						.getNum_of_header_rows() + 2);
				table.stat
						.setNum_of_body_rows(table.stat.getNum_of_body_rows() - 2);
			}

			else if (isEmptyRow(cells[i])
					&& cells[i][0].isBreakingLineOverRow() && i - 2 >= 0
					&& cells[i - 2][0].isIs_header()) {
				for (int k = 0; k < cells[i].length; k++) {
					for (int j = i - 2; j <= i; j++) {
						cells[j][k].setIs_header(true);
					}
				}
				table.stat.setNum_of_header_rows(table.stat
						.getNum_of_header_rows() + 2);
				table.stat
						.setNum_of_body_rows(table.stat.getNum_of_body_rows() - 2);
			}

			else if (isEmptyRow(cells[i])
					&& cells[i][0].isBreakingLineOverRow() && i - 3 >= 0
					&& cells[i - 3][0].isIs_header()) {
				for (int k = 0; k < cells[i].length; k++) {
					for (int j = i - 3; j <= i; j++) {
						cells[j][k].setIs_header(true);
					}
				}
				table.stat.setNum_of_header_rows(table.stat
						.getNum_of_header_rows() + 3);
				table.stat
						.setNum_of_body_rows(table.stat.getNum_of_body_rows() - 3);
			}
		}

		if (table.stat.getNum_of_header_rows() > cells.length) {
			for (int i = oldNumofHeaderRows; i < cells.length; i++) {
				for (int j = 0; j < cells[i].length; j++) {
					cells[i][j].setIs_header(false);
				}
			}
			table.stat.setNum_of_header_rows(oldNumofHeaderRows);
			table.stat.setNum_of_body_rows(oldNumOfBodyRows);
		}
		table.setTable_cells(cells);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		

		return table;
	}

	/**
	 * Checks if is string numeric.
	 *
	 * @param str
	 *            the str
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * Creates the xml string from sub node.
	 *
	 * @param xml
	 *            the xml
	 * @return the string
	 */
	public static String CreateXMLStringFromSubNode(Node xml) {
		String result = "";
		try {
			StringWriter sw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance()
					.newTransformer();
			serializer.transform(new DOMSource(xml), new StreamResult(sw));
			result = sw.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Creates the xml string from sub node.
	 *
	 * @param xml
	 *            the xml
	 * @return the string
	 */
	public static String CreateXMLStringFromSubNodeWithoutDeclaration(Node xml) {
		xml = xml.getFirstChild();
		String result = "";
		try {
			StringWriter sw = new StringWriter();
			Transformer serializer = TransformerFactory.newInstance()
					.newTransformer();
			serializer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serializer.transform(new DOMSource(xml), new StreamResult(sw));
			result = sw.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String getString(Node xml) {
		String result = "";
		if (TablInExMain.doXMLInput) {
			result = CreateXMLStringFromSubNodeWithoutDeclaration(xml);
		} else {
			result = xml.getTextContent();
		}
		return result;
	}

	/**
	 * Delete folder with content.
	 *
	 * @param folder
	 *            the folder
	 */
	public static void DeleteFolderWithContent(String folder) {
		File folderToDelete = new File(folder);
		// File Dir = new File(ImagesFolder);
		// File[] files = folderToDelete.listFiles();
		String[] entries = folderToDelete.list();
		if (entries != null) {
			for (String s : entries) {
				File currentFile = new File(folderToDelete.getPath(), s);
				currentFile.delete();
			}
		}
		folderToDelete.delete();
	}

	/**
	 * Make directory.
	 *
	 * @param path
	 *            the path
	 * @return true, if successful
	 */
	public static boolean MakeDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return f.mkdir();
		}
		return false;
	}

	/**
	 * Write file.
	 *
	 * @param filePath
	 *            the file path
	 * @param content
	 *            the content
	 */
	public static void WriteFile(String filePath, String content) {
		try {
			PrintWriter writer = new PrintWriter(filePath, "UTF-16");
			writer.print(content);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Append to file.
	 *
	 * @param filePath
	 *            the file path
	 * @param content
	 *            the content
	 */
	public static void AppendToFile(String filePath, String content) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(filePath, true)));
			out.println(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			// exception handling left as an exercise for the reader
		}
	}

	public static boolean isSpace(char ch) {
		if (((int) ch) == 8195 || (int) ch == 160 || (int) ch == ' '
				|| (int) ch == 8194 || (int) ch == 8201)
			return true;
		else
			return false;
	}

	public static int numOfBegeningSpaces(String s) {
		int num = 0;
		if (s == null)
			return 0;
		if (s.length() == 0)
			return 0;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (((int) ch) == 8195 || (int) ch == 160 || (int) ch == ' '
					|| (int) ch == 8194 || (int) ch == 8201) {
				num++;
			} else
				break;
		}
		return num;
	}

	public static int numOfSpaceOrBullets(String s) {
		int num = 0;
		if (s == null)
			return 0;
		if (s.length() == 0)
			return 0;
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (((int) ch) == 8195 || (int) ch == 160 || (int) ch == ' '
					|| (int) ch == 8194 || (int) ch == 8201 || (int) ch == '-'
					|| (int) ch == 8226) {
				num++;
			} else
				break;
		}
		return num;
	}

	public static boolean isSpaceOrEmpty(String s) {
		if (s == null)
			return false;
		if (s.length() > 1)
			return false;
		if (s.length() == 0)
			return true;
		char ch = s.charAt(0);
		if (((int) ch) == 8195 || (int) ch == 160 || (int) ch == ' '
				|| (int) ch == 8194 || (int) ch == 8201)
			return true;
		else
			return false;
	}

	public static String ReplaceNonBrakingSpaceToSpace(String s) {
		char ch = 160;
		if (s == null)
			return "";
		s = s.replace(ch, ' ');
		ch = 8195;
		s = s.replace(ch, ' ');
		ch = 8194;
		s = s.replace(ch, ' ');
		ch = 8201;
		s = s.replace(ch, ' ');
		return s;
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static double getFirstDValue(String s) {
		int numericStart = 0;
		int numericCount = 0;
		boolean isFirst = false;
		// boolean gotVlaue = false;
		for (int i = 0; i < s.length(); i++) {
			if (Utilities.isNumeric(s.charAt(i) + "") || s.charAt(i) == '.') {
				if (!isFirst) {
					numericStart = i;
				}
				isFirst = true;
				numericCount = i - numericStart + 1;
			}
			if (i >= 1
					&& Utilities.isNumeric(s.charAt(i - 1) + "")
					&& (!Utilities.isNumeric(s.charAt(i) + "") && s.charAt(i) != '.')) {
				break;
			}
		}
		double num = 0;
		try {
			if (numericCount > 0)
				num = Double.parseDouble(s.substring(numericStart, numericStart
						+ numericCount));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return num;

	}

	public static int getFirstValue(String s) {
		int numericStart = 0;
		int numericCount = 0;
		boolean isFirst = false;
		boolean gotVlaue = false;
		for (int i = 0; i < s.length(); i++) {
			if (Utilities.isNumeric(s.charAt(i) + "")) {
				if (!isFirst) {
					numericStart = i;
				}
				isFirst = true;
				numericCount = i - numericStart + 1;
			}
			if (i >= 1 && Utilities.isNumeric(s.charAt(i - 1) + "")
					&& !Utilities.isNumeric(s.charAt(i) + "")) {
				break;
			}
		}
		int num = 0;
		if (numericCount > 0)
			num = Integer.parseInt(s.substring(numericStart, numericStart
					+ numericCount));
		return num;

	}

	public static int GetNumOfOccurrences(String string, String substring) {
		string = string.toLowerCase();
		substring = substring.toLowerCase();
		int index = string.indexOf(substring);
		int occurrences = 0;
		while (index != -1) {
			occurrences++;
			string = string.substring(index + 1);
			index = string.indexOf(substring);
		}
		return occurrences;
	}

	public static String getCellType(String cellContent) {
		int numbers = 0;
		int chars = 0;
		String tempCellVal = cellContent.replaceAll("[\\s\\xA0]", "");
		for (int i = 0; i < tempCellVal.length(); i++) {
			if (Utilities.isNumeric(tempCellVal.substring(i, i + 1))) {
				numbers++;
			} else {
				chars++;
			}
		}
		float proportion = (float) numbers / (chars + numbers);
		if (Utilities.isNumeric(tempCellVal))
			return "PureNumeric";
		// part numeric cell
		if (proportion > 0.49 && !Utilities.isNumeric(tempCellVal)) {
			return "PartNumeric";
		}
		if (proportion <= 0.49 && !Utilities.isNumeric(tempCellVal)) {
			return "PureText";
		}
		return "Other (Empty)";
	}

	public static String getCellTypeIsNum(String cellContent) {
		int numbers = 0;
		int chars = 0;
		String tempCellVal = cellContent.replaceAll("[\\s\\xA0]", "");
		for (int i = 0; i < tempCellVal.length(); i++) {
			if (Utilities.isNumeric(tempCellVal.substring(i, i + 1))) {
				numbers++;
			} else {
				chars++;
			}
		}
		float proportion = (float) numbers / (chars + numbers);
		if (Utilities.isNumeric(tempCellVal))
			return "Numeric";
		// part numeric cell
		if (proportion > 0.49 && !Utilities.isNumeric(tempCellVal)) {
			return "Numeric";
		}
		if (proportion <= 0.49 && !Utilities.isNumeric(tempCellVal)) {
			return "PureText";
		}
		return "Other (Empty)";
	}

	public static LinkedList<NumberFormat> getNumsAndFormats(String num) {
		num.replace("·", ".");
		LinkedList<NumberFormat> nums = new LinkedList<NumberFormat>();
		int startVal = 0;
		int endVal = 0;
		boolean numberStart = false;
		boolean numberFinish = false;
		for (int i = 0; i < num.length(); i++) {
			if (!(Utilities.isNumeric(num.substring(i, i + 1))
					|| num.substring(i, i + 1).equals(".")
					|| num.substring(i, i + 1).equals("-")
					|| num.substring(i, i + 1).equals(",")
					|| num.substring(i, i + 1).equals("%") || Utilities
						.isSpaceOrEmpty(num.substring(i, i + 1)))) {
				if (numberStart) {
					numberFinish = true;
				}
				endVal = i;

				if (numberStart == true && numberFinish == true) {
					String form = "SingleVal";
					String newVal = num.substring(startVal, endVal);
					if (newVal.contains(".")) {
						if (newVal.contains("-")) {
							String[] ss = newVal.split("-");
							for (int j = 0; j < ss.length; j++) {
								ss[j] = "0" + ss[j];
							}
						} else {
							newVal = 0 + newVal;
						}
					}
					if (newVal.contains(",") || newVal.contains("-"))
						form = "Range";
					if (newVal.contains("%"))
						form = "Percent";
					NumberFormat f = new NumberFormat(newVal, form);
					nums.add(f);
					startVal = i;
					numberStart = false;
					numberFinish = false;
				}
			}
			if (Utilities.isNumeric(num.substring(i, i + 1))
					|| num.substring(i, i + 1).equals(".")
					|| num.substring(i, i + 1).equals("-")
					|| num.substring(i, i + 1).equals(",")
					|| num.substring(i, i + 1).equals("%")
					|| Utilities.isSpaceOrEmpty(num.substring(i, i + 1))) {
				if (numberStart == false) {
					startVal = i;
					numberStart = true;
				}
			}
		}
		if (startVal < num.length()) {
			String form = "SingleVal";
			String newVal = num.substring(startVal, num.length());
			boolean hasValue = false;
			for (int k = 0; k < newVal.length(); k++) {
				if (Utilities.isNumeric(newVal.substring(k, k + 1)))
					hasValue = true;
			}
			if (hasValue) {
				if (newVal.contains(",") || newVal.contains("-"))
					form = "Range";
				if (newVal.contains("%"))
					form = "Percent";
				NumberFormat f = new NumberFormat(num.substring(startVal,
						num.length()), form);
				nums.add(f);
			}
		}
		return nums;
	}

	public static int NoDimensions(String s) {
		int no = 0;
		Matcher m = Pattern.compile("-?\\d+(\\.\\d+)?").matcher(s);

		while (m.find()) {
			// double value = Double.parseDouble(m.group());
			// System.out.println("value=" + value);
			no++;
		}
		return no;
	}

	public static boolean stringContainsItemFromList(String inputString,
			String[] items) {
		if (inputString == null || inputString.equals(""))
			return false;
		for (int i = 0; i < items.length; i++) {
			if (inputString.contains(items[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean stringEqualsItemFromList(String inputString,
			String[] items) {
		if (inputString == null || inputString.equals(""))
			return false;
		for (int i = 0; i < items.length; i++) {
			if (inputString.equals(items[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean stringMatchRegexItemFromList(String inputString,
			LinkedList<String> items) {
		if (inputString == null || inputString.equals(""))
			return false;
		if (items.size() == 0)
			return true;
		for (int i = 0; i < items.size(); i++) {
			Pattern r = Pattern.compile(items.get(i));
			Matcher m = r.matcher(inputString);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	public static boolean stringMatchRegexItemFromList(String inputString,
			String[] items) {
		if (inputString == null || inputString.equals(""))
			return false;
		for (int i = 0; i < items.length; i++) {
			Pattern r = Pattern.compile(items[i]);
			Matcher m = r.matcher(inputString);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

	public static String stringMatchRegexItemFromListPattern(
			String inputString, String[] items) {
		if (inputString == null || inputString.equals(""))
			return "";
		for (int i = 0; i < items.length; i++) {
			Pattern r = Pattern.compile(items[i]);
			Matcher m = r.matcher(inputString);
			if (m.find()) {
				return items[i];
			}
		}
		return "";
	}

}
