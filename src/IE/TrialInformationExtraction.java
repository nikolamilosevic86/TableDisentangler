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

import tablInEx.Article;
import tablInEx.Cell;
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
	            		+"AgeMean INT,"
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
	
	public boolean checkNumOfPatients(String s)
	{
		if(s.toLowerCase().contains("number of patients") || s.toLowerCase().contains("no of patients")|| s.toLowerCase().contains("no. of patients")|| s.toLowerCase().contains("n of patients")||s.toLowerCase().contains("n=")||s.toLowerCase().contains("n ="))
		{
			return true;
		}
		else return false;
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
			if(i>1 && Utilities.isNumeric(s.charAt(i-1)+"") && !Utilities.isNumeric(s.charAt(i)+""))
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
			
			
			
			if(numOfPatients.size()>=1)
				break;
		}
		
		
		
		try{
		 Class.forName(jdbcDriver);
         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
         String insertSQL = "INSERT INTO TrialDetails (PMC, Title, Authors,URL, NoPatients, NoArms) VALUES (?, ?, ?,?, ?,?)";
         PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
         preparedStatement.setString(1, articlePMC);
         preparedStatement.setString(2, articleName);
         preparedStatement.setString(3, articleAuthors);
         preparedStatement.setString(4, URL);
         preparedStatement.setInt(5, NoPatiens);
         preparedStatement.setInt(6, NoArms);
         preparedStatement.executeUpdate();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
