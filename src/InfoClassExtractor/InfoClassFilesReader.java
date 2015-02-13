package InfoClassExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import InfoClassExtractor.InfoClass.ValueType;

public class InfoClassFilesReader {
	
	public LinkedList<InfoClass> InfoClasses = new LinkedList<InfoClass>();
	
	public boolean ReadInfoClasses(String DirectoryPath)
	{
		try {
		File folder = new File(DirectoryPath);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        //System.out.println("File " + listOfFiles[i].getName());
		    	  BufferedReader br;

					br = new BufferedReader(new FileReader(DirectoryPath+"\\"+listOfFiles[i].getName()));

		    	  String line;
		    	  String ClassName= "";
		    	  String Type = "";
		    	  float fvaluemin = 0;
		    	  float fvaluemax = 0;
		    	  boolean triggers = false;
		    	  boolean stopwords = false;
		    	  boolean canbeincaption = false;
		    	  boolean canbeinfooter = false;
		    	  boolean canbeinnav = false;
		    	  boolean canbeindata = false;
		    	  boolean freetextpatterns_b = false;
		    	  boolean navpatterns_b = false;
		    	  boolean datapatterns_b = false;
		    	  LinkedList<String> freeTextPatterns = new LinkedList<String>();
		    	  LinkedList<String> NavPatterns = new LinkedList<String>();
		    	  LinkedList<String> DataPatterns = new LinkedList<String>();
		    	  LinkedList<String> triggerList = new LinkedList<String>();
		    	  LinkedList<String> stopwordList = new LinkedList<String>();
		    	  ValueType Type1 = null;
		    	  //Reading file and putting into appropriate structures
		    	  while ((line = br.readLine()) != null) {
		    	     if(line.startsWith("InformationClass"))
		    	     {
		    	    	 ClassName = line.split(":")[1].trim();
		    	     }
		    	     if(line.startsWith("Type")){
		    	    	 Type = line.split(":")[1].trim();
		    	    	 if(Type.toLowerCase().equals("numeric"))
		    	    	 {
		    	    		 Type1 = ValueType.NUMERIC;
		    	    	 }
		    	    	 if(Type.toLowerCase().equals("seminumeric"))
		    	    	 {
		    	    		 Type1 = ValueType.SEMINUMERIC;
		    	    	 }
		    	    	 if(Type.toLowerCase().equals("string"))
		    	    	 {
		    	    		 Type1 = ValueType.STRING;
		    	    	 }
		    	    	 if(Type.toLowerCase().equals("other"))
		    	    	 {
		    	    		 Type1 = ValueType.OTHER;
		    	    	 }
		    	     }
		    	     if(line.toLowerCase().startsWith("fvaluemin")){
		    	    	 fvaluemin = Float.parseFloat(line.split(":")[1].trim());
		    	     }
		    	     if(line.toLowerCase().startsWith("fvaluemax")){
		    	    	 fvaluemax = Float.parseFloat(line.split(":")[1].trim());
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("canbeincaption")){
		    	    	  freetextpatterns_b = false;
				    	  navpatterns_b = false;
				    	  datapatterns_b = false;
		    	    	 String canbeincaption_str = line.split(":")[1].trim();
		    	    	 if(canbeincaption_str.toLowerCase().equals("true"))
		    	    		 canbeincaption = true;
		    	     }
		    	     if(line.toLowerCase().startsWith("canbeinfooter")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
		    	    	 String canbeinfooter_str = line.split(":")[1].trim();
		    	    	 if(canbeinfooter_str.toLowerCase().equals("true"))
		    	    		 canbeinfooter = true;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("canbeinnav")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
		    	    	 String canbeinnav_str = line.split(":")[1].trim();
		    	    	 if(canbeinnav_str.toLowerCase().equals("true"))
		    	    		 canbeinnav = true;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("canbeindata")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
		    	    	 String canbeindata_str = line.split(":")[1].trim();
		    	    	 if(canbeindata_str.toLowerCase().equals("true"))
		    	    		 canbeindata = true;
		    	     }
		    	     if(line.toLowerCase().startsWith("freetextpatterns")){
		    	    	 freetextpatterns_b = true;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
				    	 triggers = false;
		    	    	 stopwords = false;
		    	    	 continue;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("navpathpatterns")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = true;
				    	 datapatterns_b = false;
				    	 triggers = false;
		    	    	 stopwords = false;
		    	    	 continue;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("datapatterns")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = true;
				    	 triggers = false;
		    	    	 stopwords = false;
		    	    	 continue;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("triggers")){
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
				    	 triggers = true;
		    	    	 stopwords = false;
		    	    	 continue;
		    	     }
		    	     if(line.toLowerCase().startsWith("stopwords"))
		    	     {
		    	    	 freetextpatterns_b = false;
				    	 navpatterns_b = false;
				    	 datapatterns_b = false;
				    	 triggers = false;
		    	    	 stopwords = true;
		    	    	 continue;
		    	     }
		    	     if(freetextpatterns_b && line.startsWith("+"))
		    	     {
		    	    	 freeTextPatterns.add(line.substring(1));
		    	     }
		    	     else{
		    	    	 freetextpatterns_b = false;
		    	     }
		    	     
		    	     if(navpatterns_b && line.startsWith("+"))
		    	     {
		    	    	 NavPatterns.add(line.substring(1));
		    	     }
		    	     else{
		    	    	 navpatterns_b = false;
		    	     }
		    	     
		    	     if(datapatterns_b && line.startsWith("+"))
		    	     {
		    	    	 DataPatterns.add(line.substring(1));
		    	     }
		    	     else{
		    	    	 datapatterns_b = false;
		    	     }
		    	     
		    	     if(triggers && line.startsWith("+"))
		    	     {
		    	    	 triggerList.add(line.substring(1));
		    	     }
		    	     else{
		    	    	 triggers = false;
		    	     }
		    	     if(stopwords && line.startsWith("+"))
		    	     {
		    	    	 stopwordList.add(line.substring(1));
		    	     } 
		    	     else{
		    	    	 stopwords = false;
		    	     }
		    	  }
		    	  br.close();
		    	  //Setting InfoClass
		    	  InfoClass ic = new InfoClass(ClassName);
		    	  ic.setType(Type1);
		    	  ic.setFValueMin(fvaluemin);
		    	  ic.setFValueMax(fvaluemax);
		    	  ic.setCanBeInCaption(canbeincaption);
		    	  ic.setCanBeInDataCells(canbeindata);
		    	  ic.setCanBeInFooter(canbeinfooter);
		    	  ic.setCanBeInNavigationalCells(canbeinnav);
		    	  ic.DataCellPatterns = DataPatterns;
		    	  //making rules from string presentation
		    	  for(String d:ic.DataCellPatterns)
		    	  {
		    		  ic.DataCellRules.add(new InfoClassExtractionRule(d));
		    	  }
		    	  ic.FreeTextPatterns = freeTextPatterns;
		    	  for(String d:ic.FreeTextPatterns)
		    	  {
		    		  ic.FreeTextRules.add(new InfoClassExtractionRule(d));
		    	  }
		    	  ic.NavigationalPatterns = NavPatterns;
		    	  for(String d:ic.NavigationalPatterns)
		    	  {
		    		  ic.NavigationalRules.add(new InfoClassExtractionRule(d));
		    	  }
		    	  ic.triggerWords = triggerList;
		    	  ic.StopWords = stopwordList;
		    	  //Adding info class to list
		    	  InfoClasses.add(ic);
		      }
		    }
		    
		
		return true;
	}catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
	}

}
