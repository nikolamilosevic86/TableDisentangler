package Annotation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import DataBase.DataBaseAnnotationSaver;

public class RefTextExtractor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataBaseAnnotationSaver dbas = new DataBaseAnnotationSaver();
		try {
			InputStream modelIn = new FileInputStream("en-sent.bin");
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			Statement stmt2 = dbas.conn.createStatement();
			String countCells = "SELECT * FROM originalarticle";
			ResultSet rs1 = stmt2.executeQuery(countCells);
			System.out.println("Excecuted get count query");
			while (rs1.next()) {
				String text = rs1.getString(6);
				int Articleid = rs1.getInt(7);
				String[] sentences =  sentenceDetector.sentDetect(text);
				for (int i = 0;i<sentences.length;i++)
				{
					String sentence = sentences[i];
					if(sentence.contains("ref-type=\"table\"")){
						sentence = sentence.replaceAll("\\<[^>]*>","");
						System.out.println(sentence);
						 String pattern = "Table[ ]*[\\dIV]+";
						 if(sentence.length()>2200)
						 {
							 sentence=sentence.substring(0, 2199);
						 }
					      // Create a Pattern object
					      Pattern r = Pattern.compile(pattern);
					      String TableOrder = "";
					      // Now create matcher object.
					      Matcher m = r.matcher(sentence);
					      int count = 0;
					      while (m.find())
					    	    count++;
					      m.reset();
				    	  
					      for(int j=0;j<count;j++)
					      {
					    	  m.find();
					    	  TableOrder = m.group(0);
					    	  String SelectTab = "Select * from arttable where article_idarticle="+Articleid+" and TableOrder=\'"+TableOrder+"\'";
					    	  Statement stmt3 = dbas.conn.createStatement();
					    	  ResultSet rs2 = stmt3.executeQuery(SelectTab);
					    	  int Tableid=-1;
					    	  while (rs2.next()) {
					    		  Tableid = rs2.getInt(1);
					    	  }
					    	  stmt3.close();
					    	Statement stmt4 = dbas.conn.createStatement();
			        	  	String insertTableSentence = "INSERT INTO tablesentences (Referingsentence,Table_idTable,TableOrder,Article_idArticle) VALUES (?,?,?,?)";
			        	  		PreparedStatement preparedStatement8 = dbas.conn.prepareStatement(insertTableSentence,Statement.RETURN_GENERATED_KEYS);
			        	  		preparedStatement8.setString(1,sentence);
			        	  		preparedStatement8.setInt(2,Tableid);
			        	  		preparedStatement8.setString(3,TableOrder);
			        	  		preparedStatement8.setInt(4,Articleid);
			        	  		preparedStatement8.executeUpdate();    	
			        	  		stmt4.close();
					      }
					      
					      
						Statement stmt3 = dbas.conn.createStatement();
						System.out.println("");
					}
				}
				
			}
			rs1.close();
			stmt2.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
