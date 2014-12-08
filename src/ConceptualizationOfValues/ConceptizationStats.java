/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package ConceptualizationOfValues;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class ConceptizationStats.
 */
public class ConceptizationStats {
	
	/** The patterns. */
	public LinkedList<ConceptualizationOfValues.PatternTree> patterns = new LinkedList<ConceptualizationOfValues.PatternTree>();

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
	
	
	public void PrintConceptizationStats()
	{
		for(int i = 0;i<patterns.size();i++)
		{
			patterns.get(i).PrintPatternTree();	
		}
	}
	
	
	/**
	 * Process cell. Finds if data cells matches pattern
	 *
	 * @param c the c
	 */
	public void ProcessCell(Cell c,Table t, Article art)
	{
		if(c.isIs_header()||c.isIs_stub())
			return;
		for(int i = 0;i<patterns.size();i++)
		{
			if(patterns.get(i).getPatternName().equals("Root"))
			{
				PatternTree p = patterns.get(i);
				for(int j = 0;j<p.patternSubTree.size();j++)
		    	{
					boolean found = ProcessPattern(c,t,art,p.patternSubTree.get(j));
		    		if(found)
		    			break;
		    	}
			}
		}
	}
	
	private boolean ProcessPattern(Cell c,Table t,Article art,PatternTree pat)
	{
		String value = Utils.Utilities.ReplaceNonBrakingSpaceToSpace(c.getCell_content());
		String pattern = pat.getPattern();
		if(pattern.equals("NL"))
		{
			Dictionary dict = Dictionary.getInstance();
			InputStream is;
			try {
				is = new FileInputStream("en-token.bin");

				TokenizerModel model = new TokenizerModel(is);
		 
				Tokenizer tokenizer = new TokenizerME(model);
				String[] tokens = value.split(" ");
				int isNLP = 0;
				for(int i = 0; i < tokens.length;i++)
				{
					if(tokens[i].length()>2 && wordExistInEnglish(tokens[i]))
					{
						isNLP++;
					}
				}
				float percentNLP = ((float)isNLP/(float)tokens.length);
				if(percentNLP>0.5)
				{
					pat.FoundWords.add(value+"\t"+art.getPmc()+t.getTable_title());
			    	pat.setNoItems(pat.getNoItems()+1);
			    	return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		Pattern r = Pattern.compile(pattern);
	    Matcher m = r.matcher(value);
	    if(m.find())
	    {
	    	pat.FoundWords.add(value+"\t"+art.getPmc()+t.getTable_title());
	    	pat.setNoItems(pat.getNoItems()+1);
	    	for(int i = 0;i<pat.patternSubTree.size();i++)
	    	{
	    		boolean found = ProcessPattern(c,t,art,pat.patternSubTree.get(i));
	    		if(found)
	    			break;
	    	}
	    	return true;
	    }
	    return false;
		
	}
	
	public static boolean wordExistInEnglish(String word)
	{
		try{
		IndexWord fword = null; 
		Dictionary dict = Dictionary.getInstance();
		fword = dict.lookupIndexWord(POS.NOUN, word);
		if(fword!=null)
			return true;
		fword = dict.lookupIndexWord(POS.ADJECTIVE, word);
		if(fword!=null)
			return true;
		fword = dict.lookupIndexWord(POS.ADVERB, word);
		if(fword!=null)
			return true;
		fword = dict.lookupIndexWord(POS.VERB, word);
		if(fword!=null)
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
		return false;
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
	//TODO: Redo method tomorrow with tree appending
	public void ReadPatterns(String filename) {
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			PatternTree p = null;
			String PatternName = "";
			int treeLevel = 1;  
			PatternTree currentPattern = null;
			PatternTree prevPattert = null;
			LinkedList<PatternTree> parentStack = new LinkedList<PatternTree>();
			//First element is called root
			p = new PatternTree();
			p.setPatternName("Root");
			patterns.add(p);
			currentPattern = p;
			prevPattert = p;
			parentStack.add(currentPattern);
			int level = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("+")) {
					Pattern r = Pattern.compile("^[+]*");
					Matcher m = r.matcher(line);
					if (m.find()) {
						level = m.group().length();
					}
					PatternName = line.substring(level);
					line = br.readLine();
					p = new PatternTree();
					p.setPatternName(PatternName);
					p.setPattern(line);
					if(treeLevel==level)
					{
						currentPattern.patternSubTree.add(p);
						prevPattert = p;
					}
					if(level>treeLevel)
					{
						currentPattern = prevPattert;
						parentStack.add(prevPattert);
						prevPattert.patternSubTree.add(p);
						prevPattert = p;
						treeLevel = level;
					}
					if(level<treeLevel)
					{
						treeLevel = level;
						if(level==1)
						{
							for(int i = 1;i<parentStack.size();i++)
								parentStack.remove(parentStack.size()-1);
							currentPattern = parentStack.get(0);
							currentPattern.patternSubTree.add(p);
							prevPattert = p;
						}
						if(level>1)
						{
							currentPattern = parentStack.get(level-1);
							currentPattern.patternSubTree.add(p);
							prevPattert = p;
							for(int i = level-1;i<parentStack.size();i++)
							{
								parentStack.remove(parentStack.size()-1);
							}
						}
					}
				}
			}
			br.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
