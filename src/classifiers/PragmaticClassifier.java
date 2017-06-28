package classifiers;

import java.io.File;
import java.io.PrintWriter;

import Utils.Utilities;
import stats.Statistics;
import tablInEx.Table;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.WordsFromFile;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class PragmaticClassifier {

	private String ClassifierPath="";
	//private Classifier classifier;
	
	InputMappedClassifier classifier = new InputMappedClassifier();
	public PragmaticClassifier(String path)
	{
		ClassifierPath = path;
		try {
			classifier.setModelPath(ClassifierPath);
			classifier.setTrim(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String Classify(Table t) 
	{
	
		String prediction = "";
		Instances ins = null;
		// Declare attributes
		 Attribute Attribute1 = new Attribute("num_of_rows");
		 Attribute Attribute2 = new Attribute("num_of_columns");
		 Attribute Attribute3 = new Attribute("num_of_header_rows");
		 Attribute Attribute4 = new Attribute("percentage_of_numeric_cells");
		 Attribute Attribute5 = new Attribute("percentage_of_seminumeric_cells");
		 Attribute Attribute6 = new Attribute("percentage_of_string_cells");
		 Attribute Attribute7 = new Attribute("percentage_of_empty_cells");
		 Attribute Attribute8 = new Attribute("header_strings",(FastVector)null);
		 Attribute Attribute9 = new Attribute("stub_strings",(FastVector)null);
		 Attribute Attribute10 = new Attribute("caption",(FastVector)null);
		 Attribute Attribute11 = new Attribute("footer",(FastVector)null);
		 FastVector fvClassVal = new FastVector(3);
		 fvClassVal.addElement("findings");
		 fvClassVal.addElement("settings");
		 fvClassVal.addElement("support-knowledge");
		 Attribute ClassAttribute  = new Attribute("table_class", fvClassVal);
		 // Declare the feature vector
		 FastVector fvWekaAttributes = new FastVector(11);

		 	String header = "";
			String stub = "";
			int empty = 0;
			int string = 0;
			int seminum = 0;
			int num = 0;
			int cells_num = 0;
			
			if (t.cells != null)
				for (int i = 0; i < t.cells.length; i++) {
					for (int k = 0; k < t.cells[i].length; k++) {
						cells_num++;
						if(t.cells[i][k]==null||t.cells[i][k].getCell_content()==null)
							continue;
						if(Utilities.isSpaceOrEmpty(t.cells[i][k].getCell_content()))
						{
							empty++;
						}
						else if (t.cells[i][k].getCellType().equals("Partially Numeric"))
						{
							seminum++; 
						}
						else if (t.cells[i][k].getCellType().equals("Numeric"))
						{
							num++; 
						}
						else if (t.cells[i][k].getCellType().equals("Text"))
						{
							string++; 
						}
						if (t.cells[i][k].isIs_header())
							header += t.cells[i][k].getCell_content()
									+ " ";
						if (t.cells[i][k].isIs_stub())
							stub += t.cells[i][k].getCell_content()
									+ " ";
					}

				}
		 
			float perc_num = (float)num/(float)cells_num;
			float perc_seminum = (float)seminum/(float)cells_num;
			float perc_string = (float)string/(float)cells_num;
			float perc_empty = (float)empty/(float)cells_num;

		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(Attribute2);
		 fvWekaAttributes.addElement(Attribute3);
		 fvWekaAttributes.addElement(Attribute4);
		 fvWekaAttributes.addElement(Attribute5);
		 fvWekaAttributes.addElement(Attribute6);
		 fvWekaAttributes.addElement(Attribute7);
		 fvWekaAttributes.addElement(Attribute8);
		 fvWekaAttributes.addElement(Attribute9);
		 fvWekaAttributes.addElement(Attribute10);
		 fvWekaAttributes.addElement(Attribute11);
		 fvWekaAttributes.addElement(ClassAttribute);
		 Instances Instances = new Instances("Rel", fvWekaAttributes, 0);
			 
		 Instance iExample = new DenseInstance(12);
		 if(t.getTable_caption()==null)
		 {
			 t.setTable_caption("");
		 }
		 Attribute attribute = (Attribute)fvWekaAttributes.elementAt(0);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), t.getNum_of_rows());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), t.getNum_of_columns());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), t.stat.getNum_of_header_rows());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), perc_num);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), perc_seminum);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(5), perc_string);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(6), perc_empty);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(7), header);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(8), stub);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(9), t.getTable_caption());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(10), t.getTable_footer());
		 Instances.add(iExample);
		 Instances.setClassIndex(11);
		 
		  StringToWordVector filter = new StringToWordVector(); 
		  filter.setAttributeIndices("first-last");
		  filter.setMinTermFreq(1);
		  filter.setIDFTransform(true);
		  filter.setTFTransform(true);
		  filter.setLowerCaseTokens(true);
		  
		  filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));
		  filter.setOutputWordCounts(true);
		  SnowballStemmer stemmer = new SnowballStemmer();
		  //stemmer.setStemmer("English");
		  filter.setStemmer(stemmer);
		  WordsFromFile sw = new WordsFromFile();
		  sw.setStopwords(new File("Models/stop-words-english1.txt"));
		  filter.setStopwordsHandler(sw);
		  Instances newData;
		  try {
			filter.setInputFormat(Instances);
			filter.input(Instances.instance(0));
			filter.batchFinished();
			ins= Filter.useFilter(Instances,filter);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		 try {
			 
			 double result = classifier.classifyInstance(ins.firstInstance());
			 ins.firstInstance().setClassValue(result);
			 prediction=ins.firstInstance().classAttribute().value((int)result); 
			 t.PragmaticClass = prediction;
			 System.out.println(t.PragmaticClass);
			 new File(t.PragmaticClass).mkdirs();
			 PrintWriter writer = new PrintWriter(t.PragmaticClass+File.separator+t.getDocumentFileName()+t.getTable_title()+".html", "UTF-8");
			 writer.println(t.getXml());
			 writer.close();
			
			 Statistics.addPragmaticTableType(prediction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prediction;
	}
	
	
	public String Classify2(Table t,String class1, String class2) 
	{
	
		String prediction = "";
		Instances ins = null;
		// Declare attributes
		 Attribute Attribute1 = new Attribute("num_of_rows");
		 Attribute Attribute2 = new Attribute("num_of_columns");
		 Attribute Attribute3 = new Attribute("num_of_header_rows");
		 Attribute Attribute4 = new Attribute("percentage_of_numeric_cells");
		 Attribute Attribute5 = new Attribute("percentage_of_seminumeric_cells");
		 Attribute Attribute6 = new Attribute("percentage_of_string_cells");
		 Attribute Attribute7 = new Attribute("percentage_of_empty_cells");
		 Attribute Attribute8 = new Attribute("header_strings",(FastVector)null);
		 Attribute Attribute9 = new Attribute("stub_strings",(FastVector)null);
		 Attribute Attribute10 = new Attribute("caption",(FastVector)null);
		 Attribute Attribute11 = new Attribute("footer",(FastVector)null);
		 FastVector fvClassVal = new FastVector(2);
		 fvClassVal.addElement(class1);
		 fvClassVal.addElement(class2);
		 Attribute ClassAttribute  = new Attribute("table_class", fvClassVal);
		 // Declare the feature vector
		 FastVector fvWekaAttributes = new FastVector(11);

		 	String header = "";
			String stub = "";
			int empty = 0;
			int string = 0;
			int seminum = 0;
			int num = 0;
			int cells_num = 0;
			
			if (t.cells != null)
				for (int i = 0; i < t.cells.length; i++) {
					for (int k = 0; k < t.cells[i].length; k++) {
						cells_num++;
						if(t.cells[i][k]==null||t.cells[i][k].getCell_content()==null)
							continue;
						if(Utilities.isSpaceOrEmpty(t.cells[i][k].getCell_content()))
						{
							empty++;
						}
						else if (t.cells[i][k].getCellType().equals("Partially Numeric"))
						{
							seminum++; 
						}
						else if (t.cells[i][k].getCellType().equals("Numeric"))
						{
							num++; 
						}
						else if (t.cells[i][k].getCellType().equals("Text"))
						{
							string++; 
						}
						if (t.cells[i][k].isIs_header())
							header += t.cells[i][k].getCell_content()
									+ " ";
						if (t.cells[i][k].isIs_stub())
							stub += t.cells[i][k].getCell_content()
									+ " ";
					}

				}
		 
			float perc_num = (float)num/(float)cells_num;
			float perc_seminum = (float)seminum/(float)cells_num;
			float perc_string = (float)string/(float)cells_num;
			float perc_empty = (float)empty/(float)cells_num;

		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(Attribute2);
		 fvWekaAttributes.addElement(Attribute3);
		 fvWekaAttributes.addElement(Attribute4);
		 fvWekaAttributes.addElement(Attribute5);
		 fvWekaAttributes.addElement(Attribute6);
		 fvWekaAttributes.addElement(Attribute7);
		 fvWekaAttributes.addElement(Attribute8);
		 fvWekaAttributes.addElement(Attribute9);
		 fvWekaAttributes.addElement(Attribute10);
		 fvWekaAttributes.addElement(Attribute11);
		 fvWekaAttributes.addElement(ClassAttribute);
		 Instances Instances = new Instances("Rel", fvWekaAttributes, 0);
			 
		 Instance iExample = new DenseInstance(12);
		 Attribute attribute = (Attribute)fvWekaAttributes.elementAt(0);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), t.getNum_of_rows());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), t.getNum_of_columns());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), t.stat.getNum_of_header_rows());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), perc_num);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), perc_seminum);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(5), perc_string);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(6), perc_empty);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(7), header);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(8), stub);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(9), t.getTable_caption());
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(10), t.getTable_footer());
		 Instances.add(iExample);
		 Instances.setClassIndex(11);
		 
		  StringToWordVector filter = new StringToWordVector(); 
		  filter.setAttributeIndices("first-last");
		  filter.setMinTermFreq(1);
		  filter.setIDFTransform(true);
		  filter.setTFTransform(true);
		  filter.setLowerCaseTokens(true);
		  
		  filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));
		  filter.setOutputWordCounts(true);
		  SnowballStemmer stemmer = new SnowballStemmer();
		  //stemmer.setStemmer("English");
		  filter.setStemmer(stemmer);
		  WordsFromFile sw = new WordsFromFile();
		  sw.setStopwords(new File("Models/stop-words-english1.txt"));
		  filter.setStopwordsHandler(sw);
		  Instances newData;
		  try {
			filter.setInputFormat(Instances);
			filter.input(Instances.instance(0));
			filter.batchFinished();
			ins= Filter.useFilter(Instances,filter);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		 try {
			 
			 double result = classifier.classifyInstance(ins.firstInstance());
			 ins.firstInstance().setClassValue(result);
			 prediction=ins.firstInstance().classAttribute().value((int)result); 
			 t.PragmaticClass = prediction;
			 System.out.println(t.PragmaticClass);
			 new File(t.PragmaticClass).mkdirs();
			 PrintWriter writer = new PrintWriter(t.PragmaticClass+File.separator+t.getDocumentFileName()+t.getTable_title()+".html", "UTF-8");
			 writer.println(t.getXml());
			 writer.close();
			
			 Statistics.addPragmaticTableType(prediction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prediction;
	}
	
}
