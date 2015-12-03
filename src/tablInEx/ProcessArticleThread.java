package tablInEx;

import java.util.LinkedList;

import readers.PMCXMLReader;
import Annotation.Annotate;
import DataBase.DataBaseAnnotationSaver;
import Decomposition.Decomposition;
import Decomposition.TrialIE2;
import ExternalResourceHandlers.ResourceReader;
import FreqIE.FreqIE;
import IEArmBased.IEArmBased21;
import LinkedData.DecompositionRDFWriter;
import classifiers.SimpleTableClassifier;

public class ProcessArticleThread implements Runnable {
	private Article article;
	private int IndexOfArticle;
	
	public ProcessArticleThread(Article a, int articleIndex)
	{
		article = a;
		IndexOfArticle = articleIndex;
	}
	
	public void run(){
		
//		articlecount++;
//		if (ExportLinkedData) {
//			linkedData = new DecompositionRDFWriter();
//		}
//		if (runas.toLowerCase().equals("pmc")) {
//			article = runReadingloopOneFile(article, files[a],
//					PMCXMLReader.class);
//			for (int s = 0; s < article.getTables().length; s++) {
//				if (article.getTables()[s].cells == null)
//					continue;
//				Cell[][] original_cells = new Cell[article.getTables()[s].cells.length][];
//				for (int i = 0; i < article.getTables()[s].cells.length; i++) {
//					original_cells[i] = new Cell[article.getTables()[s].cells[i].length];
//					for (int j = 0; j < article.getTables()[s].cells[i].length; j++)
//						original_cells[i][j] = new Cell(
//								article.getTables()[s].cells[i][j]);
//				}
//				article.getTables()[s].original_cells = original_cells;
//			}
//		}
//		Decomposition ie = null;
//		TrialIE2 tie = null;
//		FreqIE tie2 = null;
//		IEArmBased21 tie3 = null;
//		if (doIE) {
//			ie = new Decomposition(Inpath, newrun);
//			newrun = false;
//		}
//
//		if (IEFreqSQLTial) {
//			tie2 = new FreqIE();
//		}
//		if (IEinSQLTial) {
//			// TrialInformationExtraction tie = new
//			// TrialInformationExtraction("");
//			tie = new TrialIE2();
//		}
//		if (IEFine) {
//			// IEArmBased2 or ArmExtractor
//			tie3 = new IEArmBased21();
//			informationClasses = ResourceReader.read("IEResources");
//		}
//		if (Conceptization) {
//			concept.ReadPatterns("patterns");
//		}
//
//		if (article != null && article.getTables() != null)
//			for (int j = 0; j < article.getTables().length; j++) {
//				Table t = article.getTables()[j];
//
//				if (t.isHasHeader()) {
//				}
//				t.PragmaticClass = pc.Classify(t);
//			}
//
//		if (doIE) {
//
//			ie.ExtractData(article);
//		}
//		if (TablInExMain.ComplexClassify) {
//			for (int i = 0; i < article.getTables().length; i++) {
//				SimpleTableClassifier.ClassifyTableByComplexity(article
//						.getTables()[i]);
//				// tables[i].printTableStatsToFile("TableStats.txt");
//			}
//		}
//		if (IEinSQLTial) {
//			tie.ExtractTrialData(article);
//		}
//		if (IEFreqSQLTial) {
//			tie2.ExtractTrialData(article);
//		}
//		if (IEFine) {
//			tie3.ExtractTrialData(article);
//		}
//		if (Conceptization) {
//			concept.processArticle(article);
//		}
//
//		if (ExportLinkedData) {
//			linkedData.printToFile(LinkedDataFolder + "\\"
//					+ article.getPmc() + ".rdf");
//		}
//
//		for (int l = 0; l < article.getTables().length; l++) {
//			LinkedList<DataExtractionOutputObj> outputs = article
//					.getTables()[l].output;
//			for (DataExtractionOutputObj out : outputs) {
//				out.CreateOutput();
//			}
//		}
//		Annotate annot = new Annotate();
//		annot.AnnotateArticle(article);
//		if(databaseSave){
//		dbas.SaveArticleAnnotationToDB(article);
//		if(articlecount>10)
//		{
//			dbas.CloseDBConnection();
//			dbas = new DataBaseAnnotationSaver();
//			articlecount = 0;
//		}
//		}
		
	}

}
