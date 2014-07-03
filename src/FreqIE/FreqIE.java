package FreqIE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import tablInEx.Article;
import tablInEx.ChangableString;
import tablInEx.Table;
import IEData.PatientNumExtractor;

public class FreqIE {
	private String jdbcDriver = "com.mysql.jdbc.Driver";
    
	/** The db address. */
	private String dbAddress = "jdbc:mysql://localhost:3306/";
    
//	/** The user pass. */
//	private String userPass = "?user=root&password=";
    
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
	
	
	public FreqIE()
	{
		try{
		   Class.forName(jdbcDriver);
            con = DriverManager.getConnection(dbAddress + dbName, userName, password);
            Statement s = con.createStatement();
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            s = con.createStatement();
            s.executeUpdate("DROP TABLE IF EXISTS FreqTrialDetails");            
            String myTableName = "CREATE TABLE IF NOT EXISTS FreqTrialDetails ("
            		+"idNo INT(64) NOT NULL AUTO_INCREMENT," 
            		+"PMC VARCHAR(10),"	            		
            		+"Title VARCHAR(555)," 
            		+"Authors VARCHAR(555)," 
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
	
	
	public void ExtractTrialData(Article a)
	{
		if(a==null)
			return;
		
		Table[] tables = a.getTables();
		if(tables==null)
			return;
		if(tables.length>0)
			if(tables[0].output==null||tables[0].output.size()==0)
				return;
		String articleName = a.getTitle();
		String articlePMC = a.getPmc();
		System.out.println("PMC:"+articlePMC);
		String URL = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC"+articlePMC;
		int NoPatiens = 0;
		
		int NoMales = 0;
		int NoFemales = 0;
		int NoArms=0;
		ChangableString Range = new ChangableString("");
		String articleAuthors = "";
		for(String auth : a.getAuthors())
		{
			articleAuthors = articleAuthors+";"+auth;
		}
		if(articleAuthors.length()>1)
			articleAuthors = articleAuthors.substring(1);	
		FreqExtractor freq = new FreqExtractor();
		NoPatiens = freq.getPatientNumber(a);
		
		try{
		 Class.forName(jdbcDriver);
         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
         String insertSQL = "INSERT INTO FreqTrialDetails (PMC, Title, Authors,URL, NoPatients, NoArms, NoMale, NoFemale,AgeMean,AgeRange) VALUES (?, ?, ?,?, ?,?,?,?,?,?)";
         PreparedStatement preparedStatement = con.prepareStatement(insertSQL);
         preparedStatement.setString(1, articlePMC);
         preparedStatement.setString(2, articleName);
         preparedStatement.setString(3, articleAuthors);
         preparedStatement.setString(4, URL);
         preparedStatement.setInt(5, NoPatiens);
         preparedStatement.setInt(6, NoArms);
         preparedStatement.setInt(7, NoMales);
         preparedStatement.setInt(8, NoFemales);
         preparedStatement.setDouble(9, Double.parseDouble("0.1"));
         preparedStatement.setString(10, Range.toString());
         preparedStatement.executeUpdate();
         con.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
