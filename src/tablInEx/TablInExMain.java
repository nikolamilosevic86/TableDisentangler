/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package tablInEx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import ConceptualizationOfValues.ConceptizationStats;
import ExternalResourceHandlers.InformationClass;
import ExternalResourceHandlers.ResourceReader;
import FreqIE.FreqIE;
import IE.MetaMapStats;
import IE.SimpleDataExtraction;
import IE.TrialIE2;
import IEArmBased.ArmExtractor;
import IEArmBased.IEArmBased;
import IEArmBased.IEArmBased2;
import IEArmBased.IEArmBased21;
import Utils.SemanticType;
import Utils.Utilities;
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
	public static boolean doXMLInput = false;
	public static boolean shouldTag = false;
	public static boolean IEinSQLTial = false;
	public static boolean IEFreqSQLTial = false;
	public static boolean IEFine = false;
	public static boolean Conceptization = false;
	public static String Inpath;
	public static HashMap<String,Integer> headermap = new HashMap<String, Integer>();
	public static HashMap<String,Integer> stubmap = new HashMap<String, Integer>();
	public static LinkedList<DataExtractionOutputObj> outputs = new LinkedList<DataExtractionOutputObj>();
	public static LinkedList<String> PMCBMI = new LinkedList<String>();
	public static LinkedList<InformationClass> informationClasses = new LinkedList<InformationClass>();
	public static HashMap<String, SemanticType> semanticTypes = new HashMap<String, SemanticType>();
	public static ConceptualizationOfValues.ConceptizationStats concept;
	//public static ConceptualizationOfValues.ConceptizationStats concept2;
	
	public static void ReadSemanticTypes(){
		try{
		BufferedReader br = new BufferedReader(new FileReader("SemTypesOfInterestShort"));
		String line;
		while ((line = br.readLine()) != null) {
		   String[] semtypes = line.split("\\|");
		   SemanticType s = new SemanticType();
		   s.setShortLabel(semtypes[0]);
		   s.setLongName(semtypes[2]);
		   semanticTypes.put(semtypes[0], s);
		}
		br.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
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
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	return a;
	}

	public static void main(String[] args) {
		concept = new ConceptizationStats();
	//	concept2 = new ConceptizationStats();
		System.out.println("=============================================");
		System.out.println("Hello TablInEx");
		System.out.println("=============================================");
		System.out.println("For a moment, nothing happened. Then, after a second or so, nothing continued to happen. - Duglas Adams");
		System.out.println("____________________________________________________________________________________________________________________________");
		String path = args[0];
		ReadSemanticTypes();
		
		Inpath = path;
		String runas = "";
		if(args.length>1)
			runas = args[1];
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
		//shouldTag
		if(Arrays.asList(args).contains("-tag"))
		{
			shouldTag = true;
		}
		if(Arrays.asList(args).contains("-conceptisation"))
		{
			Conceptization = true;
		}
		if(Arrays.asList(args).contains("-freq"))
		{
			IEFreqSQLTial = true;
		}
		if(Arrays.asList(args).contains("-makestats"))
		{
			doStats = true;
		}
		if(Arrays.asList(args).contains("-extractTrialToSQL"))
		{
			IEinSQLTial = true;
		}
		
		if(Arrays.asList(args).contains("-iefine"))
		{
			IEFine = true;
		}
		if(Arrays.asList(args).contains("-typeclassify"))
		{
			TypeClassify = true;
		}
		
		if(Arrays.asList(args).contains("-doHTMLInput2Output"))
		{
			doXMLInput = true;
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
			return;
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
		//		t = TableSimplifier.LabelHeaderCells(t);// did not help
				if(t.isHasHeader()){
				t = TableSimplifier.MergeHeaders(t);
				t = TableSimplifier.MergeStubs(t);
				}
			}
		}
		if (doIE) 
		{
			SimpleDataExtraction ie = new SimpleDataExtraction(Inpath);
			for (int i = 0; i < articles.length; i++) 
			{
					ie.ExtractData(articles[i]);
				
			}
			
			//Create output
			for(int i = 0;i<outputs.size();i++)
			{
				if(shouldTag){
				outputs.get(i).MetamapTagDocument();
				}
				outputs.get(i).CreateOutput();
			}
			
			if(IEinSQLTial)
			{
				//TrialInformationExtraction tie = new TrialInformationExtraction("");
				TrialIE2 tie = new TrialIE2();
				for (int i = 0; i < articles.length; i++) 
				{
					tie.ExtractTrialData(articles[i]);				
				}
			}
			if(IEFreqSQLTial)
			{
				//TrialInformationExtraction tie = new TrialInformationExtraction("");
				FreqIE tie = new FreqIE();
				for (int i = 0; i < articles.length; i++) 
				{
					tie.ExtractTrialData(articles[i]);				
				}
			}
			if(IEFine)
			{
				//IEArmBased2  or   ArmExtractor
				IEArmBased21 tie = new IEArmBased21();
				informationClasses = ResourceReader.read("IEResources");
				for (int i = 0; i < articles.length; i++) 
				{
					tie.ExtractTrialData(articles[i]);				
				}
			}
			if(Conceptization)
			{
				
				concept.ReadPatterns("patterns");
			//	concept2.ReadPatterns("Level2Patterns");
				for (int i = 0; i < articles.length; i++) 
				{
					concept.processArticle(articles[i]);	
				//	concept2.processArticle(articles[i]);
				}
			}
		}
		int weight = 0;
		int BMI = 0;
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
	            writer.println(key + " ; " + value);  
	            if((key.toLowerCase().contains("bmi")||key.toLowerCase().contains("weight")||key.toLowerCase().contains("body mass index")||key.toLowerCase().contains("bodyweight"))&&(!key.toLowerCase().contains("loss")||!key.toLowerCase().contains("decrease")||!key.toLowerCase().contains("increase")||!key.toLowerCase().contains("gain")||!key.toLowerCase().contains("gain")))
	            {
	            	weight+=Integer.parseInt(lm.get(name).toString());
	            }
	            if((key.toLowerCase().contains("bmi")||key.toLowerCase().contains("body mass index")))
	            {
	            	BMI+=Integer.parseInt(lm.get(name).toString());
	            }

	}
			writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			
			
			lm = Utilities.sortHashMapByValuesD(stubmap);
			ss = lm.keySet().toArray();
			sa = new String[ss.length];
		    k = 0;
			for(Object o:ss)
			{
				sa[k] = (String)ss[k];
				k++;
			}
			try {
				writer = new PrintWriter("stubs.txt", "UTF-8");


			for (String name: sa){

	            String key =name.toString();
	            String value = lm.get(name).toString();  
	            writer.println(key + " ; " + value);  
	            if((key.toLowerCase().contains("bmi")||key.toLowerCase().contains("weight")||key.toLowerCase().contains("body mass index")||key.toLowerCase().contains("bodyweight"))&&(!key.toLowerCase().contains("loss")||!key.toLowerCase().contains("decrease")||!key.toLowerCase().contains("increase")||!key.toLowerCase().contains("gain")||!key.toLowerCase().contains("gain")))
	            {
	            	weight+=Integer.parseInt(lm.get(name).toString());
	            }
	            if((key.toLowerCase().contains("bmi")||key.toLowerCase().contains("body mass index")))
	            {
	            	BMI+=Integer.parseInt(lm.get(name).toString());
	            }

			}
			writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		try{
		PrintWriter writer = new PrintWriter("PMCBMI3.txt", "UTF-8");
		for(int i=0;i<PMCBMI.size();i++)
		{
			writer.println(PMCBMI.get(i));
		}
		writer.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		MetaMapStats.PrintMMStats();
		
		Statistics.CalculateStatistics();
		String stats = Statistics.makeOutputStatisticString();
		System.out.print(stats);
		System.out.println("BMI alone:"+BMI);
		System.out.println("Number of weight/BMI:"+weight);
		System.out.println("PMC documents with weight/BMI:"+PMCBMI.size());
		concept.PrintConceptizationStats();
		//concept2.PrintConceptizationStats();
		
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
		System.out.println("    -doHTMLInput2Output - Tells system to take from cells values as they are in XML or HTML format with all included tags. If this is not present, everything will be transformed to text and tags will be ignored");
		System.out.println("    -tag - Tag output (using metamap)");
		System.out.println("    -extractTrialToSQL - Extract information about trial (no of patients, males, females, age range...) and stores it in mySQL database. Has to be executed together with -doIE command");
		System.out.println("    -freq - Extract information about trial using frequency algorithm(no of patients, males, females, age range...) and stores it in mySQL database. Has to be executed together with -doIE command");
		System.out.println("    -iefine - Extract information about trial using fine graned approach (data about each arm) and stores it in mySQL database. Has to be executed together with -doIE command");
	}
}
