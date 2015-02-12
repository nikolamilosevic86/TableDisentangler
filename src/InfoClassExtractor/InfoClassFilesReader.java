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
		    	  //TODO:FREETEXTPATTERNS,NAV AND DATA PATTERNS READING!!!
		    	  LinkedList<String> triggerList = new LinkedList<String>();
		    	  LinkedList<String> stopwordList = new LinkedList<String>();
		    	  ValueType Type1 = null;
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
		    	    	 String canbeincaption_str = line.split(":")[1].trim();
		    	    	 if(canbeincaption_str.toLowerCase().equals("true"))
		    	    		 canbeincaption = true;
		    	     }
		    	     if(line.toLowerCase().startsWith("canbeinfooter")){
		    	    	 String canbeinfooter_str = line.split(":")[1].trim();
		    	    	 if(canbeinfooter_str.toLowerCase().equals("true"))
		    	    		 canbeinfooter = true;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("canbeinnav")){
		    	    	 String canbeinnav_str = line.split(":")[1].trim();
		    	    	 if(canbeinnav_str.toLowerCase().equals("true"))
		    	    		 canbeinnav = true;
		    	     }
		    	     
		    	     if(line.toLowerCase().startsWith("canbeindata")){
		    	    	 String canbeindata_str = line.split(":")[1].trim();
		    	    	 if(canbeindata_str.toLowerCase().equals("true"))
		    	    		 canbeindata = true;
		    	     }
		    	     
		    	     
		    	     if(line.toLowerCase().startsWith("triggers")){
		    	    	 triggers = true;
		    	    	 continue;
		    	     }
		    	     if(line.toLowerCase().startsWith("stopwords"))
		    	     {
		    	    	 triggers = false;
		    	    	 stopwords = true;
		    	    	 continue;
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
		    	  
		    	  InfoClass ic = new InfoClass(ClassName);
		    	  ic.setType(Type1);
		    	  ic.setFValueMin(fvaluemin);
		    	  ic.setFValueMax(fvaluemax);
		    	  ic.triggerWords = triggerList;
		    	  ic.StopWords = stopwordList;
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
