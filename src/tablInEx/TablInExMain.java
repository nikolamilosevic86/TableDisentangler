/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import IE.SimpleIE;
import classifiers.SimpleTableClassifier;
import readers.PMCXMLReader;
import readers.Reader;
import stats.Statistics;


public class TablInExMain {
	
	public static boolean doStats = false; 
	public static boolean TypeClassify = false;
	public static boolean ComplexClassify = false;
	public static boolean learnheaders = false;
	public static boolean doIE = false;
	public static String outputDest = "";
	public static String Inpath;
	public static HashMap<String,Integer> headermap = new HashMap<String, Integer>();
	
	public static Article[] runReadingloop(Article[] a,File[] files,int len, Class s)
	{
		try 
		{
			for(int i=0;i<len;i++)
			{
				File f = new File(files[i].getPath());
				//Not working with hidden or temp files
				if (f.isHidden()|| files[i].getPath().endsWith("~"))
					continue;
				
				Reader r;
				r = (Reader)s.newInstance();
				r.init(files[i].getPath());
				a[i]=r.Read();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return a;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("=============================================");
		System.out.println("Hello TablInEx");
		System.out.println("=============================================");
		System.out.println("For a moment, nothing happened. Then, after a second or so, nothing continued to happen. - Duglas Adams");
		System.out.println("____________________________________________________________________________________________________________________________");
		String path = args[0];
		Inpath = path;
		
		String runas = args[1];
		if(Arrays.asList(args).contains("-o"))
		{
			int i = Arrays.asList(args).indexOf("-o");
			outputDest = args[i+1];
		}
		if(Arrays.asList(args).contains("-learnheaders"))
		{
			int i = Arrays.asList(args).indexOf("-learnheaders");
			learnheaders = true;
		}
		if(Arrays.asList(args).contains("-makestats"))
		{
			doStats = true;
		}
		if(Arrays.asList(args).contains("-typeclassify"))
		{
			TypeClassify = true;
		}
		if(Arrays.asList(args).contains("-complexclassify"))
		{
			ComplexClassify = true;
		}
		if(Arrays.asList(args).contains("-doie"))
		{
			doIE = true;
		}
		
		if(Arrays.asList(args).contains("-help"))
		{
			printHelp();
		}
		if(TypeClassify)
			SimpleTableClassifier.init(TablInExMain.Inpath);
		if(ComplexClassify)
			SimpleTableClassifier.initComplexity(Inpath);
		
		System.out.println("Reading "+path);
		File Dir = new File(path);
		File[] files = Dir.listFiles();
		Article[] articles = new Article[files.length]; 
		if(runas.toLowerCase().equals("pmc"))
		{
			articles = runReadingloop(articles,files, files.length, PMCXMLReader.class);
		}
		for(int i = 0; i < articles.length;i++)
		{
			if(articles[i]!=null && articles[i].getTables()!=null)
			for(int j = 0; j<articles[i].getTables().length;j++)
			{
				Table t = articles[i].getTables()[j];
				t = TableSimplifier.MergeHeaders(t);
				t = TableSimplifier.MergeStubs(t);
			}
		}
		if (doIE) 
		{
			SimpleIE ie = new SimpleIE(Inpath);
			for (int i = 0; i < articles.length; i++) 
			{
					ie.ExtractInformation(articles[i]);
				
			}
		}
		if(learnheaders)
		{
			LinkedHashMap lm = Utilities.sortHashMapByValuesD(headermap);
			Object [] ss = lm.keySet().toArray();
			String[] sa = new String[ss.length];
			int k = 0;
			for(Object o:ss)
			{
				sa[k] = (String)ss[k];
				k++;
			}
			PrintWriter writer;
			try {
				writer = new PrintWriter("headers.txt", "UTF-8");


			for (String name: sa){

	            String key =name.toString();
	            String value = lm.get(name).toString();  
	            writer.println(key + " " + value);  

	}
			writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Statistics.CalculateStatistics();
		String stats = Statistics.makeOutputStatisticString();
		System.out.print(stats);
	}
	
	/**
	 * Prints the help.
	 */
	public static void printHelp()
	{
		System.out.println("HELP pages for TablInEX\r\n");
		System.out.println("NAME");
		System.out.println("     TablInEx -  Table Information Extraction engine");
		System.out.println("DESCRIPTION");
		System.out.println("     TablInEx is information extraction engine build with primary purpose to extract data from tables in biomedical and clinical literature.");
		System.out.println("ARGUMENTS");
		System.out.println("    First argument should be input. For bulk processing use input folder of the corpus folder");
		System.out.println("    Second argument should be type of corpus. Supported corpuses: pmc");
		System.out.println("    -o - this argument should be followed by output folder path");
		System.out.println("    -makestats - tells the program to capture statistics about processed tables");
		System.out.println("    -typeclassify - Classify tables by tipes (useful tables, tables without head, without body, with rowspans, with colspans...)");
		System.out.println("    -complexclassify - Classify tables by complexity (simple,medium,complex)");
		System.out.println("    -doie - Tells system do do Information extraction and save it to inputfolder_ie");
		System.out.println("    -learnheaders - Tells system do calculate frequency of phrases in headers. These phrases are stored in a file headers.txt, and can be later used.");
		
	}
}
