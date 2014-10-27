/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class ConceptizationStats.
 */
public class ConceptizationStats {
	
	/** The patterns. */
	public static LinkedList<stats.Pattern> patterns = new LinkedList<stats.Pattern>();

	public void processArticle(Article art)
	{
		if(art==null)
			return;
		Table[] tables = art.getTables();
		for(int i = 0;i<tables.length;i++)
		{
			Cell[][] cells = tables[i].cells;
			if(cells==null)
				continue;
			for(int j =0;j<cells.length;j++)
			{
				for(int k =0;k<cells[j].length;k++)
				{
					ProcessCell(cells[j][k], tables[i], art);
				}
			}
		}
	}
	
	
	public static void PrintConceptizationStats()
	{
		for(int i = 0;i<patterns.size();i++)
		{
			patterns.get(i).PrintPattern();
			
		}
	}
	
	
	/**
	 * Process cell. Finds if data cells matches pattern
	 *
	 * @param c the c
	 */
	public static void ProcessCell(Cell c,Table t, Article art)
	{
		if(c.isIs_header()||c.isIs_stub())
			return;
		for(int i = 0;i<patterns.size();i++)
		{
			String value = Utils.Utilities.ReplaceNonBrakingSpaceToSpace(c.getCell_content());
			Pattern r = Pattern.compile(patterns.get(i).getPattern());
		    Matcher m = r.matcher(value);
		    if(m.find())
		    {
		    	patterns.get(i).FoundWords.add(value+"\t"+art.getPmc()+t.getTable_title());
		    	patterns.get(i).setNoItems(patterns.get(i).getNoItems()+1);
		    	break; // On first pattern matched break and leave
		    }
		}
	}
	
	
	/**
	 * Reads patterns from the file. Pattern starts with line containing + and pattern name. Second line is regex pattern
	 * 
	 * Example:
	 * +RealNumber
	 * \d*[.]*\d*
	 *
	 * @param filename the filename
	 */
	public static void ReadPatterns(String filename) {
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			stats.Pattern p = null;
			boolean createPattern = false;
			String PatternName = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("+")) {
					createPattern = true;
					PatternName = line.substring(1);
				} else {
					if (p != null)
						p.setPattern(line);
				}
				if (createPattern == true) {
					p = new stats.Pattern();
					p.setPatternName(PatternName);
					patterns.add(p);
					createPattern = false;
				}
			}
			br.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
