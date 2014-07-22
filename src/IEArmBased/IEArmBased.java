package IEArmBased;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.Utilities;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;
import weka.associations.tertius.Body;

public class IEArmBased {
	
	String[] weightWords = {"weight","bodyweight"};
	String[] BMIWords = {"bmi","body mass index"};
	String[] NotAnArm = {"p value","p* value", "p", "N", "p-value","total", "all"};
		
//	SELECT ArmID, WeightCategory, WeightValue, ArmName, PMC
//	FROM `WeightDetails`
//	INNER JOIN ArmDetails ON ArmDetails.id = WeightDetails.ArmID
//	INNER JOIN DocumentDetails ON DocumentDetails.id = ArmDetails.DocumentID
//	LIMIT 0 , 30
	
	public void ExtractTrialData(Article art)
	{
		System.out.println("IEArmBased "+art.getPmc());
		int document_id = AddDocumentDetains(art);
		Table[] tables = art.getTables();
		if(tables==null)
			return;
		LinkedList <ClinicalArm> arms = new LinkedList<ClinicalArm>();

		for(int i = 0; i <tables.length;i++)
		{
			Cell [][] cells = tables[i].cells;
			for(int j = 0; j<cells.length;j++)
			{
				for(String word:BMIWords){
				if(cells[j][0].getCell_content().toLowerCase().contains(word))
				{
					for(int k = 1;k<cells[0].length;k++)
					{
						if(cells[0][k].getCell_content().contains("SD"))
						{
							cells[0][k].setCell_content(cells[0][k].getCell_content().replace("SD", ""));
						}
						if(cells[0][k].getCell_content().contains("Mean"))
						{
							cells[0][k].setCell_content(cells[0][k].getCell_content().replace("Mean", ""));
						}
						if(!(cells[0][k].getCell_content().toLowerCase().equals("p")||cells[0][k].getCell_content().toLowerCase().contains("all")||cells[0][k].getCell_content().toLowerCase().contains("total")||cells[0][k].getCell_content().toLowerCase().equals("range")||cells[0][k].getCell_content().toLowerCase().equals("p*")||cells[0][k].getCell_content().toLowerCase().equals(" ± ")||Utilities.isSpaceOrEmpty(cells[0][k].getCell_content())||cells[0][k].getCell_content().toLowerCase().equals("n")||cells[0][k].getCell_content().toLowerCase().contains("n°")))
						{
							Pattern r = Pattern.compile("\\bp[-* ]*value[a-z]*\\b");
						    Matcher m = r.matcher(cells[0][k].getCell_content().toLowerCase());
						    if(!m.find())
						    {
						    	ClinicalArm arm = new ClinicalArm(art.getPmc(),art.getTitle());
						    	arm.setArmName(cells[0][k].getCell_content());
						    	String Content = cells[j][k].getCell_content();
						    	if(cells[j][k].isIs_columnspanning() && cells[j][k].getCells_columnspanning()==cells[j].length)
						    		Content = cells[j+1][k].getCell_content();
						    	String type = Utilities.getCellTypeIsNum(Content);
						    	if(type.equals("Numeric"))
						    		arm.setBMI(Content);
						    	arms.add(arm);
						    }
						}
					}
				}
				
				}
			}
		}
		LinkedList<ClinicalArm> arms2 = new LinkedList<ClinicalArm>();
		LinkedList<String> armNames = new LinkedList<String>();
		for(int i = 0;i<arms.size();i++)
		{
			if(!armNames.contains(arms.get(i).getArmName())){
				arms2.add(arms.get(i));
				armNames.add(arms.get(i).getArmName());
			}
		}
		AddArmsToDB(document_id,arms2);
	}
	
private String jdbcDriver = "com.mysql.jdbc.Driver";
    
	/** The db address. */
	private String dbAddress = "jdbc:mysql://localhost:3306/";
    
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
	public IEArmBased()
	{
		try{
		   Class.forName(jdbcDriver);
            con = DriverManager.getConnection(dbAddress + dbName, userName, password);
            Statement s = con.createStatement();
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            s = con.createStatement();
            s.executeUpdate("DROP TABLE IF EXISTS DocumentDetails");            
            String myTableName = "CREATE TABLE IF NOT EXISTS DocumentDetails ("
            		+"id INT(64) NOT NULL AUTO_INCREMENT," 
            		+"PMC VARCHAR(10),"	            		
            		+"Title VARCHAR(555)," 
            		+"Authors VARCHAR(555)," 
            		+"Abstract TEXT," 
            		+"LongAbstract TEXT," 
            		+"URL VARCHAR(255),"
            		+"XML LONGTEXT,"
            		+"PRIMARY KEY (`id`));"; 
            		

                	statement = con.createStatement();
                    //The next line has the issue
                    statement.executeUpdate(myTableName);
                    System.out.println("DocumentDetails Table Created");
                    
                    s.executeUpdate("DROP TABLE IF EXISTS Users");            
                    myTableName = "CREATE TABLE IF NOT EXISTS Users ("
                    		+"id INT(64) NOT NULL AUTO_INCREMENT," 
                    		+"email VARCHAR(255),"	            		
                    		+"password VARCHAR(555)," 
                    		+"affilliation VARCHAR(555)," 
                    		+"user_role VARCHAR(255)," 
                    		+"PRIMARY KEY (`id`));"; 
                    		

                        	statement = con.createStatement();
                            //The next line has the issue
                            statement.executeUpdate(myTableName);
                         String insertSQL = "INSERT INTO Users (email, password, affilliation,user_role ) VALUES (?, ?, ?,?)";
                         PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
           		         preparedStatement.setString(1, "nikola.milosevic86@gmail.com");
           		         preparedStatement.setString(2, "HelloWorld");
           		         preparedStatement.setString(3, "University of Manchester");
           		         preparedStatement.setString(4, "admin");
           		         preparedStatement.executeUpdate();
                    
                    s.executeUpdate("DROP TABLE IF EXISTS ArmDetails");            
                    myTableName = "CREATE TABLE IF NOT EXISTS ArmDetails ("
                    		+"id INT(64) NOT NULL AUTO_INCREMENT," 
                    		+"ArmName VarChar(255),"	            		
                    		+"NoPatients INT(64)," 
                    		+"NoMale INT(64)," 
                    		+"NoFemale INT(64),"
                    		+"DocumentID INT(64),"
                    		+"PRIMARY KEY (`id`));"; 
                        	statement = con.createStatement();
                            //The next line has the issue
                            statement.executeUpdate(myTableName);
                            System.out.println("ArmDetails Table Created");	
                            
                      s.executeUpdate("DROP TABLE IF EXISTS WeightDetails");            
                      myTableName = "CREATE TABLE IF NOT EXISTS WeightDetails ("
                            		+"id INT(64) NOT NULL AUTO_INCREMENT," 
                            		+"ArmID INT(64),"	            		
                            		+"WeightCategory VARCHAR(200)," // May be Mean weight, Weight range, BMI, Weight change 
                            		+"WeightValue VARCHAR(100)," 
                            		+"WeightUnit VARCHAR(20),"  //kg, g, kg/m2,g/m2
                            		+"PRIMARY KEY (`id`));"; 
                                	statement = con.createStatement();
                                    //The next line has the issue
                                    statement.executeUpdate(myTableName);
                                    System.out.println("ArmDetails Table Created");	
	  } 
        catch (Exception e) {
        	e.printStackTrace();
        }
	
	}
	
	public int AddDocumentDetains(Article art)
	{
		int id = -1;
		try{
			String articleAuthors = "";
			for(String auth : art.getAuthors())
			{
				articleAuthors = articleAuthors+";"+auth;
			}
			if(articleAuthors.length()>1)
				articleAuthors = articleAuthors.substring(1);	
			String URL = "http://www.ncbi.nlm.nih.gov/pmc/articles/PMC"+art.getPmc();
			 Class.forName(jdbcDriver);

	         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
	         String insertSQL = "INSERT INTO DocumentDetails (PMC, Title, Authors,Abstract, LongAbstract ,URL, XML) VALUES (?, ?, ?,?, ?,?,?)";
	         PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setString(1, art.getPmc());
	         preparedStatement.setString(2, art.getTitle());
	         preparedStatement.setString(3, articleAuthors);
	         preparedStatement.setString(4, art.getShort_abstract());
	         preparedStatement.setString(5, art.getAbstract());
	         preparedStatement.setString(6, URL);
	         preparedStatement.setString(7, art.getXML());

	         id = preparedStatement.executeUpdate();
	         ResultSet rs = preparedStatement.getGeneratedKeys();
	         if (rs.next()){
	        	 id=rs.getInt(1);
	         }
	         con.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		return id;
	}
	
	public void AddArmsToDB(int document_id,LinkedList<ClinicalArm> arms)
	{
		try{
			 Class.forName(jdbcDriver);
			 for(int i = 0;i<arms.size();i++){
	         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
	         String insertSQL = "INSERT INTO ArmDetails (ArmName, NoPatients, NoMale,NoFemale, DocumentID ) VALUES (?, ?, ?,?, ?)";
	         PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setString(1, arms.get(i).getArmName());
	         preparedStatement.setInt(2, arms.get(i).getNoPatients());
	         preparedStatement.setInt(3, arms.get(i).getNoMale());
	         preparedStatement.setInt(4, arms.get(i).getNoFemale());
	         preparedStatement.setInt(5, document_id);
	         int id=0;
	        preparedStatement.executeUpdate();
	        ResultSet rs = preparedStatement.getGeneratedKeys();
	         if (rs.next()){
	        	 id=rs.getInt(1);
	         }
			 if(arms.get(i).getBMI()!=null && !arms.get(i).getBMI().equals(""))
			 {
				 con = DriverManager.getConnection(dbAddress + dbName, userName, password);
		         insertSQL = "INSERT INTO WeightDetails (ArmID, WeightCategory, WeightValue,WeightUnit ) VALUES (?, ?, ?,?)";
		         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
		         preparedStatement.setInt(1, id);
		         preparedStatement.setString(2, "BMI");
		         preparedStatement.setString(3, arms.get(i).getBMI());
		         preparedStatement.setString(4, "kg");
		         preparedStatement.executeUpdate();
			 }

	         con.close();
			 }
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
	}
	
}
