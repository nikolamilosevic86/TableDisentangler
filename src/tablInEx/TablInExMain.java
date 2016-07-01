package tablInEx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import net.didion.jwnl.JWNL;
import Annotation.Annotate;
import ConceptualizationOfValues.ConceptizationStats;
import DataBase.DataBaseAnnotationSaver;
import Decomposition.Decomposition;
import Decomposition.TrialIE2;
import ExternalResourceHandlers.InformationClass;
import ExternalResourceHandlers.ResourceReader;
import FreqIE.FreqIE;
import IEArmBased.IEArmBased21;
import LinkedData.DecompositionRDFWriter;
import Main.MarvinSemAnnotator;
import Utils.SemanticType;
import Utils.Utilities;
import ValueParser.ValueParser;
import classifiers.SimpleTableClassifier;
import readers.DailyMedReader;
import readers.PMCXMLReader;
import readers.Reader;
import stats.Statistics;

// Import log4j classes.
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class TablInExMain {

	public static boolean databaseSave = false;
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
	public static boolean ExportLinkedData = false;
	public static String Inpath;
	public static HashMap<String, Integer> headermap = new HashMap<String, Integer>();
	public static HashMap<String, Integer> stubmap = new HashMap<String, Integer>();
	public static LinkedList<String> PMCBMI = new LinkedList<String>();
	public static LinkedList<stats.TableStats> TStats = new LinkedList<stats.TableStats>();
	public static LinkedList<InformationClass> informationClasses = new LinkedList<InformationClass>();
	public static HashMap<String, SemanticType> semanticTypes = new HashMap<String, SemanticType>();
	public static ConceptualizationOfValues.ConceptizationStats concept;
	public static DecompositionRDFWriter linkedData;
	public static MarvinSemAnnotator marvin = new MarvinSemAnnotator();
	public static ValueParser vp = new ValueParser();

	public static void ReadSemanticTypes() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"SemTypesOfInterestShort"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] semtypes = line.split("\\|");
				SemanticType s = new SemanticType();
				s.setShortLabel(semtypes[0]);
				s.setLongName(semtypes[2]);
				semanticTypes.put(semtypes[0], s);
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Article[] runReadingloop(Article[] a, File[] files, int len,
			Class s) {
		try {
			for (int i = 0; i < len; i++) {
				File f = new File(files[i].getPath());
				// Not working with hidden or temp files
				if (f.isHidden() || files[i].getPath().endsWith("~"))
					continue;

				Reader r;
				r = (Reader) s.newInstance();
				r.init(files[i].getPath());
				a[i] = r.Read();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return a;
	}

	public static Article runReadingloopOneFile(Article a, File file, Class s) {
		try {
		    System.out.println("File: " + file.getPath());
		    
			File f = new File(file.getPath());
			// Not working with hidde,  temp files, or zip
			if (f.isHidden() || file.getPath().endsWith("~") || file.getPath().endsWith("zip"))
				return a;

			Reader r;
			r = (Reader) s.newInstance();
			r.init(file.getPath());
			a = r.Read();
			
			for(int i = 0;i<a.getTables().length;i++){
				a.getTables()[i] = Utilities.FixTablesHeader(a.getTables()[i]);
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return a;
	}

	public static void main(String[] args) {
    	        // Set up a simple configuration that logs on the console.
	        BasicConfigurator.configure();
	    
		String propsFile = "file_properties.xml";
		try {
			JWNL.initialize(new FileInputStream(propsFile));
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
		concept = new ConceptizationStats();
		// concept2 = new ConceptizationStats();
		System.out.println("=============================================");
		System.out.println("Hello TablInEx");
		System.out.println("=============================================");
		System.out
				.println("For a moment, nothing happened. Then, after a second or so, nothing continued to happen. - Duglas Adams");
		System.out
				.println("____________________________________________________________________________________________________________________________");
		String path = args[0];
		// ReadSemanticTypes();

		Inpath = path;
		String runas = "";
		if (args.length > 1)
			runas = args[1];
		if (Arrays.asList(args).contains("-o")) {
			int i = Arrays.asList(args).indexOf("-o");
			outputDest = args[i + 1];
		}
		if (Arrays.asList(args).contains("-learnheaders")) {
			int i = Arrays.asList(args).indexOf("-learnheaders");
			learnheaders = true;
		}
		// shouldTag
		if (Arrays.asList(args).contains("-tag")) {
			shouldTag = true;
		}
		if (Arrays.asList(args).contains("-ld")) {
			ExportLinkedData = true;
		}
		if (Arrays.asList(args).contains("-conceptisation")) {
			Conceptization = true;
		}
		if (Arrays.asList(args).contains("-databasesave")) {
			databaseSave = true;
		}
		if (Arrays.asList(args).contains("-freq")) {
			IEFreqSQLTial = true;
		}
		if (Arrays.asList(args).contains("-makestats")) {
			doStats = true;
		}
		if (Arrays.asList(args).contains("-extractTrialToSQL")) {
			IEinSQLTial = true;
		}

		if (Arrays.asList(args).contains("-iefine")) {
			IEFine = true;
		}
		if (Arrays.asList(args).contains("-typeclassify")) {
			TypeClassify = true;
		}

		if (Arrays.asList(args).contains("-doHTMLInput2Output")) {
			doXMLInput = true;
		}
		if (Arrays.asList(args).contains("-complexclassify")) {
			ComplexClassify = true;
		}
		if (Arrays.asList(args).contains("-doie")) {
			doIE = true;
		}

		if (Arrays.asList(args).contains("-help")) {
			printHelp();
			return;
		}
		classifiers.PragmaticClassifier pc = new classifiers.PragmaticClassifier(
				"Models/SMOPragmaticAll.model");
		// classifiers.PragmaticClassifier pc2 = new
		// classifiers.PragmaticClassifier("Models/SMOPragmaticSupportVsAll.model");
		// classifiers.PragmaticClassifier pc3 = new
		// classifiers.PragmaticClassifier("Models/SMOPragmaticFindingsVsAll.model");

		System.out.println("Reading " + path);
		File Dir = new File(path);
		File[] files = Dir.listFiles();
		// Article[] articles = new Article[files.length];
		// Article[] articles1 = articles;
		Article article = new Article("");
		boolean newrun = true;
		String LinkedDataFolder = "RDFs";

		int articlecount = 0;
		DataBaseAnnotationSaver dbas = null;
		if(databaseSave){
		dbas = new DataBaseAnnotationSaver();
		}
		//Thread[] threads = new Thread[files.length];
		for (int a = 0; a < files.length; a++) {			
			articlecount++;
			if (ExportLinkedData) {
				linkedData = new DecompositionRDFWriter();
			}
			if (runas.toLowerCase().equals("pmc")) {
				article = runReadingloopOneFile(article, files[a],
						PMCXMLReader.class);
				for (int s = 0; s < article.getTables().length; s++) {
					if (article.getTables()[s].cells == null)
						continue;
					Cell[][] original_cells = new Cell[article.getTables()[s].cells.length][];
					for (int i = 0; i < article.getTables()[s].cells.length; i++) {
						original_cells[i] = new Cell[article.getTables()[s].cells[i].length];
						for (int j = 0; j < article.getTables()[s].cells[i].length; j++)
							original_cells[i][j] = new Cell(
									article.getTables()[s].cells[i][j]);
					}
					article.getTables()[s].original_cells = original_cells;
				}
			}
			
			if (runas.toLowerCase().equals("dailymed")) {
				article = runReadingloopOneFile(article, files[a],
						DailyMedReader.class);
				for (int s = 0; s < article.getTables().length; s++) {
					if (article.getTables()[s].cells == null)
						continue;
					Cell[][] original_cells = new Cell[article.getTables()[s].cells.length][];
					for (int i = 0; i < article.getTables()[s].cells.length; i++) {
						original_cells[i] = new Cell[article.getTables()[s].cells[i].length];
						for (int j = 0; j < article.getTables()[s].cells[i].length; j++)
							original_cells[i][j] = new Cell(article.getTables()[s].cells[i][j]);
					}
					article.getTables()[s].original_cells = original_cells;
				}
			}
			Decomposition ie = null;
			TrialIE2 tie = null;
			FreqIE tie2 = null;
			IEArmBased21 tie3 = null;
			if (doIE) {
				ie = new Decomposition(Inpath, newrun);
				newrun = false;
			}

			if (IEFreqSQLTial) {
				tie2 = new FreqIE();
			}
			if (IEinSQLTial) {
				// TrialInformationExtraction tie = new
				// TrialInformationExtraction("");
				tie = new TrialIE2();
			}
			if (IEFine) {
				// IEArmBased2 or ArmExtractor
				tie3 = new IEArmBased21();
				informationClasses = ResourceReader.read("IEResources");
			}
			if (Conceptization) {
				concept.ReadPatterns("patterns");
			}
			
			if (TypeClassify)
				SimpleTableClassifier.init(TablInExMain.Inpath);
			if (ComplexClassify)
				SimpleTableClassifier.initComplexity(Inpath);
			if (ExportLinkedData) {
				// linkedData = new DecompositionRDFWriter();
				boolean success = (new File(LinkedDataFolder)).mkdirs();
			}

			if (article != null && article.getTables() != null)
				for (int j = 0; j < article.getTables().length; j++) {
					Table t = article.getTables()[j];

					if (t.isHasHeader()) {
					}
					t.PragmaticClass = pc.Classify(t);
				}

			if (doIE) {

				ie.ExtractData(article);
			}
			if (TablInExMain.ComplexClassify) {
				for (int i = 0; i < article.getTables().length; i++) {
					SimpleTableClassifier.ClassifyTableByComplexity(article
							.getTables()[i]);
					// tables[i].printTableStatsToFile("TableStats.txt");
				}
			}
			if (IEinSQLTial) {
				tie.ExtractTrialData(article);
			}
			if (IEFreqSQLTial) {
				tie2.ExtractTrialData(article);
			}
			if (IEFine) {
				tie3.ExtractTrialData(article);
			}
			if (Conceptization) {
				concept.processArticle(article);
			}
			Annotate annot = new Annotate();
			annot.AnnotateArticle(article);
			if(databaseSave){
			dbas.SaveArticleAnnotationToDB(article);
			if(articlecount>10)
			{
				dbas.CloseDBConnection();
				dbas = new DataBaseAnnotationSaver();
				articlecount = 0;
			}
			}

		}
		if(databaseSave){
		dbas.CloseDBConnection();
		}

		int weight = 0;
		int BMI = 0;
		if (learnheaders) {
			LinkedHashMap lm = Utilities.sortHashMapByValuesD(headermap);
			Object[] ss = lm.keySet().toArray();
			String[] sa = new String[ss.length];
			int k = 0;
			for (Object o : ss) {
				sa[k] = (String) ss[k];
				k++;
			}
			PrintWriter writer;
			try {
				writer = new PrintWriter("headers.txt", "UTF-8");

				for (String name : sa) {

					String key = name.toString();
					String value = lm.get(name).toString();
					writer.println(key + " ; " + value);
					if ((key.toLowerCase().contains("bmi")
							|| key.toLowerCase().contains("weight")
							|| key.toLowerCase().contains("body mass index") || key
							.toLowerCase().contains("bodyweight"))
							&& (!key.toLowerCase().contains("loss")
									|| !key.toLowerCase().contains("decrease")
									|| !key.toLowerCase().contains("increase")
									|| !key.toLowerCase().contains("gain") || !key
									.toLowerCase().contains("gain"))) {
						weight += Integer.parseInt(lm.get(name).toString());
					}
					if ((key.toLowerCase().contains("bmi") || key.toLowerCase()
							.contains("body mass index"))) {
						BMI += Integer.parseInt(lm.get(name).toString());
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
			for (Object o : ss) {
				sa[k] = (String) ss[k];
				k++;
			}
			try {
				writer = new PrintWriter("stubs.txt", "UTF-8");

				for (String name : sa) {

					String key = name.toString();
					String value = lm.get(name).toString();
					writer.println(key + " ; " + value);
					if ((key.toLowerCase().contains("bmi")
							|| key.toLowerCase().contains("weight")
							|| key.toLowerCase().contains("body mass index") || key
							.toLowerCase().contains("bodyweight"))
							&& (!key.toLowerCase().contains("loss")
									|| !key.toLowerCase().contains("decrease")
									|| !key.toLowerCase().contains("increase")
									|| !key.toLowerCase().contains("gain") || !key
									.toLowerCase().contains("gain"))) {
						weight += Integer.parseInt(lm.get(name).toString());
					}
					if ((key.toLowerCase().contains("bmi") || key.toLowerCase()
							.contains("body mass index"))) {
						BMI += Integer.parseInt(lm.get(name).toString());
					}

				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		// try{
		// PrintWriter writer = new PrintWriter("PMCBMI3.txt", "UTF-8");
		// for(int i=0;i<PMCBMI.size();i++)
		// {
		// writer.println(PMCBMI.get(i));
		// }
		// writer.close();
		// }catch(Exception ex)
		// {
		// ex.printStackTrace();
		// }

		// MetaMapStats.PrintMMStats();

		Statistics.CalculateStatistics();
		String stats = Statistics.makeOutputStatisticString();

		System.out.print(stats);
		// System.out.println("BMI alone:"+BMI);
		// System.out.println("Number of weight/BMI:"+weight);
		// System.out.println("PMC documents with weight/BMI:"+PMCBMI.size());
		concept.PrintConceptizationStats();
		// concept2.PrintConceptizationStats();
		/*PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setOut(out);*/
	}

	/**
	 * Prints the help.
	 */
	public static void printHelp() {
		System.out.println("HELP pages for TablInEX\r\n");
		System.out.println("NAME");
		System.out
				.println("     TablInEx -  Table Information Extraction engine");
		System.out.println("DESCRIPTION");
		System.out
				.println("     TablInEx is information extraction engine build with primary purpose to extract data from tables in biomedical and clinical literature.");
		System.out.println("ARGUMENTS");
		System.out
				.println("    First argument should be input. For bulk processing use input folder of the corpus folder");
		System.out
				.println("    Second argument should be type of corpus. Supported corpuses: pmc");
		System.out
				.println("    -o - this argument should be followed by output folder path");
		System.out
				.println("    -makestats - tells the program to capture statistics about processed tables");
		System.out
				.println("    -typeclassify - Classify tables by tipes (useful tables, tables without head, without body, with rowspans, with colspans...)");
		System.out
				.println("    -complexclassify - Classify tables by complexity (simple,medium,complex)");
		System.out
				.println("    -doie - Tells system do do Information extraction and save it to inputfolder_ie");
		System.out
				.println("    -learnheaders - Tells system do calculate frequency of phrases in headers. These phrases are stored in a file headers.txt, and can be later used.");
		System.out
				.println("    -doHTMLInput2Output - Tells system to take from cells values as they are in XML or HTML format with all included tags. If this is not present, everything will be transformed to text and tags will be ignored");
		System.out.println("    -tag - Tag output (using metamap)");
		System.out
				.println("    -extractTrialToSQL - Extract information about trial (no of patients, males, females, age range...) and stores it in mySQL database. Has to be executed together with -doIE command");
		System.out
				.println("    -freq - Extract information about trial using frequency algorithm(no of patients, males, females, age range...) and stores it in mySQL database. Has to be executed together with -doIE command");
		System.out
				.println("    -iefine - Extract information about trial using fine graned approach (data about each arm) and stores it in mySQL database. Has to be executed together with -doIE command");
		System.out
				.println("    -ld - Export table decomposition as RDF linked data file");
	}
}
