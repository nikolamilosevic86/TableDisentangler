/*
 * @author: Nikola Milosevic
 * @affiliation: University of Manchester, School of Computer science
 * 
 */
package IE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.ChangableString;
import tablInEx.DataExtractionOutputObj;
import tablInEx.Table;
import tablInEx.Utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class TrialInformationExtraction.
 */
public class TrialInformationExtraction {
	 
 	/** The jdbc driver. */
 	private String jdbcDriver = "com.mysql.jdbc.Driver";
	    
    	/** The db address. */
    	private String dbAddress = "jdbc:mysql://localhost:3306/";
	    
//    	/** The user pass. */
//    	private String userPass = "?user=root&password=";
	    
    	/** The db name. */
    	private String dbName = "TiralExtraction";
	    
    	/** The user name. */
    	private String userName = "root";
	    
    	/** The password. */
    	private String password = "";

	    
    	/** The statement. */
    	private Statement statement;
	    

	    
    	/** The con. */
    	private Connection con;
	
	/**
	 * Instantiates a new trial information extraction. Creates database and table for trial information if they don't exist
	 *
	 * @param DBfileoptions - File path with ptions for mySQL connection
	 */
	public TrialInformationExtraction(String DBfileoptions)
	{
		  try {
	            Class.forName(jdbcDriver);
	            con = DriverManager.getConnection(dbAddress + dbName, userName, password);
	            Statement s = con.createStatement();
	            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
	            s = con.createStatement();
	            s.executeUpdate("DROP TABLE IF EXISTS TrialDetails");            
	            String myTableName = "CREATE TABLE IF NOT EXISTS TrialDetails ("
	            		+"idNo INT(64) NOT NULL AUTO_INCREMENT," 
	            		+"PMC VARCHAR(10),"	            		
	            		+"Title VARCHAR(255)," 
	            		+"Authors VARCHAR(255)," 
	            		+"NoPatients INT," 
	            		+"NoMale INT," 
	            		+"NoFemale INT," 
	            		+"NoArms INT," 
	            		+"AgeMean DOUBLE,"
	            		+"AgeRange VARCHAR(100),"
	            		+"Arms VARCHAR(255)," 
	            		+"URL VARCHAR(255),"
	            		+"PRIMARY KEY (`idNo`));"; 
	            		
	                try {
	                	statement = con.createStatement();
	                    //The next line has the issue
	                    statement.executeUpdate(myTableName);
	                    System.out.println("Table Created");
	                }
	                catch (SQLException e ) {
	                    System.out.println("An error has occurred on Table Creation");
	                }
		  
		  } 
	        catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } 
	        catch (SQLException e) {
	        	e.printStackTrace();
	        }
	}
	
	public void getMeanAndRange(Table t, ChangableString Mean, ChangableString Range)
	{
		LinkedList<DataExtractionOutputObj> list = t.output;
		LinkedList<Double> means = new LinkedList<Double>();
		LinkedList<Double> mins = new LinkedList<Double>();
		LinkedList<Double> maxs = new LinkedList<Double>();
		boolean meanFound = false;
		boolean rangeFound = false;
		for(DataExtractionOutputObj obj:list)
		{
			NodeList Head00 = obj.getXMLDocument().getElementsByTagName("Head00");
			NodeList HeaderValue = obj.getXMLDocument().getElementsByTagName("HeaderValue");
			NodeList Stub = obj.getXMLDocument().getElementsByTagName("Stub");
			NodeList value = obj.getXMLDocument().getElementsByTagName("value");
			String Head00Value = "";
			String Header = "";
			String stubVal = "";
			String Vvalue = "";
			if(Head00!=null && Head00.item(0)!=null)
				Head00Value = Head00.item(0).getTextContent();
			if(HeaderValue!=null && HeaderValue.item(0)!=null)
				Header = HeaderValue.item(0).getTextContent();
			for(int i=0;i<Stub.getLength();i++)
			{
				if(Stub!=null && Stub.item(i)!=null)
					stubVal+=";"+ Stub.item(i).getTextContent();
			}
			if(value!=null && value.item(0)!=null)
			{
				Vvalue = value.item(0).getTextContent();
			}
			if(Head00Value.toLowerCase().contains("age")||Header.toLowerCase().contains("age")||stubVal.toLowerCase().contains("age"))
			{
				Pattern regex3 = Pattern.compile("\\b[A-Za-z/]\\b");
			    Matcher regexMatcher3 = regex3.matcher(Vvalue);
			    if(regexMatcher3.find())
			    {
			    	continue;
			    }
			    String[] values = Vvalue.split("\\(");
			    if(Utilities.isDouble(values[0]))
			    {
			    	means.add(Double.parseDouble(values[0]));
			    }
			    for(String v:values)
			    {
			    	Pattern regex1 = Pattern.compile("\\b[0-9]{1,}[\\.]{0,1}[0-9]*[–-][0-9]{1,}[\\.]{0,1}[0-9]*\\b");
				    Matcher regexMatcher1 = regex1.matcher(v);
				    if(regexMatcher1.find())
				    {
				    	String[] range = v.split("–");
				    	if(range.length<2)
				    	{
				    		range = v.split("-");
				    	}
				    	mins.add(getFirstDoubleValue(range[0]));
				    	maxs.add(getFirstDoubleValue(range[1]));
				    }
			    }
			    if(Vvalue.contains("±"))
			    {
			    	String[] sd = Vvalue.split("±");
			    	means.add(Double.parseDouble(sd[0]));
			    	Double min = Double.parseDouble(sd[0]) - Double.parseDouble(sd[1]);
			    	Double max = Double.parseDouble(sd[0]) + Double.parseDouble(sd[1]);
			    	mins.add(min);
			    	maxs.add(max);
			    }
				System.out.println(Vvalue);
				
			}	
		}
		Double FinalMean = 0.0;
		for(Double mean:means)
		{
			FinalMean+=mean;
		}
		if(means.size()>0)
			FinalMean = FinalMean/means.size();
		Double Min = 999.0;
		for(Double m:mins)
		{
			if(m<Min)
				Min = m;
		}
		Double Max = 0.0;
		for(Double m:maxs)
		{
			if(m>Max)
				Max = m;
		}
		Mean.changeTo(FinalMean+"");
		Range.changeTo(Min+"-"+Max);
	}
	
	public boolean checkGender(String s)
	{
		if(s.toLowerCase().contains("male") || s.toLowerCase().contains("female")||s.toLowerCase().contains("m/f"))
		{
			return true;
		}
		return false;
	}
	
	
	
	public int[] getMalesFemales(Cell[] cells)
	{
			int[] results = new int[2];
		    String matched = "";
			LinkedList<Integer> malesList = new LinkedList<Integer>();
			LinkedList<Integer> femalesList = new LinkedList<Integer>();
			String cellValue = Utilities.ReplaceNonBrakingSpaceToSpace(cells[0].getCell_content().toLowerCase());
		  	Pattern regex = Pattern.compile("\\b[ ]{0,1}[:/\\|a-z]{0,}male[s]{0,1}[ ]{0,1}[:/\\|]{0,}[ ]{0,1}([a-z]{0,}(male)[s]*)*\\b");
		    Matcher regexMatcher = regex.matcher(cellValue);
		    while (regexMatcher.find()) {
		    	matched = regexMatcher.group();
		    	System.out.println(regexMatcher.group());
		    }
		    boolean MaleSecond = false;
		    boolean hasBothValues = false;
		    boolean hasOnlyFemale = false;
		    Pattern regex1 = Pattern.compile("\\bmale[s]{0,1}\\b");
		    Matcher regexMatcher1 = regex1.matcher(matched);
		    Pattern regex2 = Pattern.compile("\\bfemale[s]{0,1}\\b");
		    Matcher regexMatcher2 = regex2.matcher(matched);
		    boolean males = regexMatcher1.find();
		    boolean females =  regexMatcher2.find();
		    if(!males && females)
		    {
		    	hasOnlyFemale = true;
		    }
		    if(males && females)
		    {
		    	hasBothValues = true;
		    	if(regexMatcher1.start()>regexMatcher2.start())
		    		MaleSecond = true;		    	
		    }
		    
		    
		    for(int i=1;i<cells.length;i++)
		    {
		    	Pattern regex3 = Pattern.compile("\\b[a-z]\\b");
			    Matcher regexMatcher3 = regex3.matcher(cells[i].getCell_content().toLowerCase());
			    if(regexMatcher3.find())
			    {
			    	continue;
			    }
		    	if(!hasBothValues && !hasOnlyFemale)
		    	{
		    		malesList.add(getFirstValue(cells[i].getCell_content()));
		    	}
		    	if(!hasBothValues && hasOnlyFemale)
		    	{
		    		femalesList.add(getFirstValue(cells[i].getCell_content()));
		    	}
		    	if(hasBothValues && !MaleSecond)
		    	{
		    		String s = cells[i].getCell_content();
		    		int numericStart=0;
		    		int numericCount = 0;
		    		boolean isFirst = false;
		    		for(int j = 0;j<s.length();j++)
		    		{
		    			if(Utilities.isNumeric(s.charAt(j)+""))
		    			{
		    				if(!isFirst)
		    				{
		    					numericStart = j;
		    				}
		    				isFirst = true;
		    				numericCount = j-numericStart+1;
		    			}
		    			if(j>=1 && Utilities.isNumeric(s.charAt(j-1)+"") && !Utilities.isNumeric(s.charAt(j)+""))
		    			{
		    				break;
		    			}	
		    		}
		    		int num = 0;
		    		if(numericCount>0)
		    		 num = Integer.parseInt(s.substring(numericStart,numericStart+numericCount));
		    		
		    		String val = s.substring(numericStart+numericCount);
		    		int secondnum = getFirstValue(val);
		    		malesList.add(num);
		    		femalesList.add(secondnum);
		    	}
		    	if(hasBothValues && MaleSecond)
		    	{
		    		String s = cells[i].getCell_content();
		    		int numericStart=0;
		    		int numericCount = 0;
		    		boolean isFirst = false;
		    		for(int j = 0;j<s.length();j++)
		    		{
		    			if(Utilities.isNumeric(s.charAt(j)+""))
		    			{
		    				if(!isFirst)
		    				{
		    					numericStart = j;
		    				}
		    				isFirst = true;
		    				numericCount = j-numericStart+1;
		    			}
		    			if(j>=1 && Utilities.isNumeric(s.charAt(j-1)+"") && !Utilities.isNumeric(s.charAt(j)+""))
		    			{
		    				break;
		    			}	
		    		}
		    		int num = 0;
		    		if(numericCount>0)
		    		 num = Integer.parseInt(s.substring(numericStart,numericStart+numericCount));
		    		
		    		String val = s.substring(numericStart+numericCount);
		    		int secondnum = getFirstValue(val);
		    		femalesList.add(num);
		    		malesList.add(secondnum);
		    	}
		    }
		    for(int maleItem:malesList)
		    {
		    	results[0]+=maleItem;
		    }
		    for(int femaleItem:femalesList)
		    {
		    	results[1]+=femaleItem;
		    }
		return results;
	}
	
	
	public boolean checkNumOfPatients(String s)
	{
		if(s.toLowerCase().contains("number of patients") || s.toLowerCase().contains("no of patients")|| s.toLowerCase().contains("no. of patients")|| s.toLowerCase().contains("n of patients")||s.toLowerCase().contains("n=")||s.toLowerCase().contains("n ="))
		{
			return true;
		}
		else return false;
	}
	
	public double getFirstDoubleValue(String s)
	{
		int numericStart=0;
		int numericCount = 0;
		boolean isFirst = false;
		for(int i = 0;i<s.length();i++)
		{
			if(Utilities.isNumeric(s.charAt(i)+"")||s.charAt(i)=='.')
			{
				if(!isFirst)
				{
					numericStart = i;
				}
				isFirst = true;
				numericCount = i-numericStart+1;
			}
			if(i>=1 && Utilities.isNumeric(s.charAt(i-1)+"") && !Utilities.isNumeric(s.charAt(i)+""))
			{
				break;
			}	
		}
		float num = 0;
		if(numericCount>0)
		 num = Float.parseFloat(s.substring(numericStart,numericStart+numericCount));
		return num;
		
	}
	
	public int getFirstValue(String s)
	{
		int numericStart=0;
		int numericCount = 0;
		boolean isFirst = false;
		boolean gotVlaue = false;
		for(int i = 0;i<s.length();i++)
		{
			if(Utilities.isNumeric(s.charAt(i)+""))
			{
				if(!isFirst)
				{
					numericStart = i;
				}
				isFirst = true;
				numericCount = i-numericStart+1;
			}
			if(i>=1 && Utilities.isNumeric(s.charAt(i-1)+"") && !Utilities.isNumeric(s.charAt(i)+""))
			{
				break;
			}	
		}
		int num = 0;
		if(numericCount>0)
		 num = Integer.parseInt(s.substring(numericStart,numericStart+numericCount));
		return num;
		
	}
	
	public void ExtractTrialData(Article a)
	{
		String articleName = a.getTitle();
		String articlePMC = a.getPmc();
		String URL = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC"+articlePMC;
		int NoPatiens = 0;
		int NoMales = 0;
		int NoFemales = 0;
		int NoArms=0;
		ChangableString Mean = new ChangableString("");
		ChangableString Range = new ChangableString("");
		String Arms;
		float AgeMean;
		String AgeRange;
		String articleAuthors = "";
		for(String auth : a.getAuthors())
		{
			articleAuthors = articleAuthors+";"+auth;
		}
		articleAuthors = articleAuthors.substring(1);
		Table[] tables = a.getTables();
		LinkedList<Integer> numOfPatients = new LinkedList<Integer>(); 
		
		for(Table t:tables)
		{
			Cell[][] cells = t.cells;
			for(int i = 0;i<cells.length;i++)
			{
				if(checkNumOfPatients(cells[i][0].getCell_content()))
				{
					if(cells[i][0].getCell_content().toLowerCase().contains("no of patients")||cells[i][0].getCell_content().toLowerCase().contains("number of patients")||cells[i][0].getCell_content().toLowerCase().contains("no. of patients")||cells[i][0].getCell_content().toLowerCase().contains("n of patients"))
					{
						for(int j=1;j<cells[i].length;j++)
						{
							if(Utilities.isNumeric(cells[i][j].getCell_content()))
							{
								int n = getFirstValue(cells[i][j].getCell_content());
								if(n>0){
									numOfPatients.add(n);
									NoArms++;
								}
							}
						}
					}
					if(cells[i][0].getCell_content().toLowerCase().contains("n=")||cells[i][0].getCell_content().toLowerCase().contains("n ="))
					{
						int n = getFirstValue(cells[i][0].getCell_content());
						if(n>0)
							numOfPatients.add(n);
					}
				}
				if(checkGender(cells[i][0].getCell_content()))
				{
					int[] res = getMalesFemales(cells[i]);
					NoMales += res[0];
					NoFemales += res[1];
				}
			}
			for(int i = 0; i<cells[0].length;i++)
			{
				if(checkNumOfPatients(cells[0][i].getCell_content()))
				{
					if(cells[0][i].getCell_content().toLowerCase().contains("n=")||cells[0][i].getCell_content().toLowerCase().contains("n ="))
					{
						int n = getFirstValue(cells[0][i].getCell_content());
						if(n>0)
						{
							numOfPatients.add(n);
							NoArms++;
						}
					}
				}
			}
			for(int j = 0;j<numOfPatients.size();j++)
			{
				NoPatiens += numOfPatients.get(j);
			}
			

			getMeanAndRange(t,Mean,Range);
			
//			System.out.println("Mean: "+ Mean);
//			System.out.println("Range: "+ Range);
		
			
			if(numOfPatients.size()>=1)
				break;
		}
		
		if(NoMales==0 && NoFemales!=0)
		{
			NoMales = NoPatiens-NoFemales;
		}
		if(NoMales!=0 && NoFemales==0)
		{
			NoFemales = NoPatiens - NoMales;
		}
		if(Mean.toString().equals(""))
		{
			Mean.changeTo("0");
		}
		
		try{
		 Class.forName(jdbcDriver);
         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
         String insertSQL = "INSERT INTO TrialDetails (PMC, Title, Authors,URL, NoPatients, NoArms, NoMale, NoFemale,AgeMean,AgeRange) VALUES (?, ?, ?,?, ?,?,?,?,?,?)";
         PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
         preparedStatement.setString(1, articlePMC);
         preparedStatement.setString(2, articleName);
         preparedStatement.setString(3, articleAuthors);
         preparedStatement.setString(4, URL);
         preparedStatement.setInt(5, NoPatiens);
         preparedStatement.setInt(6, NoArms);
         preparedStatement.setInt(7, NoMales);
         preparedStatement.setInt(8, NoFemales);
         preparedStatement.setDouble(9, Double.parseDouble(Mean.toString()));
         preparedStatement.setString(10, Range.toString());
         preparedStatement.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
