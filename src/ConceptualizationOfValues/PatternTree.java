package ConceptualizationOfValues;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class PatternTree {

	private String PatternName;
	public LinkedList<String> FoundWords = new LinkedList<String>();
	public LinkedList<PatternTree> patternSubTree = new LinkedList<PatternTree>();
	private int NoItems = 0;
	private String pattern;
	
	public void PrintPatternTree()
	{
		
		String filename ="Conceptisation\\" +PatternName+".txt";
		try {
			new File("Conceptisation").mkdir();
			File file = new File(filename);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("No: "+NoItems+"\r\n");
			for(int i = 0;i<FoundWords.size();i++){
				bw.write(FoundWords.get(i)+"\r\n");
			}
			bw.close();
			for(int i = 0;i<patternSubTree.size();i++)
			{
				patternSubTree.get(i).PrintPatternTree();
			}
 
			//System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @return the noItems
	 */
	public int getNoItems() {
		return NoItems;
	}
	/**
	 * @param noItems the noItems to set
	 */
	public void setNoItems(int noItems) {
		NoItems = noItems;
	}
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return the patternName
	 */
	public String getPatternName() {
		return PatternName;
	}
	/**
	 * @param patternName the patternName to set
	 */
	public void setPatternName(String patternName) {
		PatternName = patternName;
	}
	
	
}
