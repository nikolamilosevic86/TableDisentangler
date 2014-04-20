/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.io.File;
import java.util.Arrays;

import IE.SimpleIE;
import classifiers.SimpleTableClassifier;
import readers.PMCXMLReader;
import readers.Reader;
import stats.Statistics;


public class TablInExMain {
	
	public static boolean doStats = false; 
	public static boolean TypeClassify = false;
	public static boolean ComplexClassify = false;
	public static boolean doIE = false;
	public static String outputDest = "";
	public static String Inpath;
	
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
		if (doIE) 
		{
			SimpleIE ie = new SimpleIE(Inpath);
			for (int i = 0; i < articles.length; i++) 
			{
					ie.ExtractInformation(articles[i]);
				
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
		
	}
}
