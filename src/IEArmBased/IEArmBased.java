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

public class IEArmBased {
	
	String[] DemographicTableWords = {"weight","bodyweight","bmi","body mass index"};
	
	String[] weightWords = {"weight","bodyweight"};
	String[] BMIWords = {"bmi","body mass index"};
	
	String [] equalsNotAnArm = {"p","range","p*"," ± ","n","T","ρ","p-value","p* value","%"};
	String [] containsNotAnArm = {"all","total","p-value","95%","ratio"};
	String [] TotalArm = {"mean±sd","mean ± sd"};
	String [] TotalArmContains = {"n°"};
	String[] Male = {"male\\b","man\\b","men\\b","boy\\b","boys\\b","m\\b"};
	String[] NotGenderInfo = {"±","m-2","m 2","m−2","ratio"};
	String[] Female = {"female\\b","woman\\b","women\\b","girl\\b","girls\\b"};
	String[] Mixed = {"m(ale){0,1}[ ]{0,1}[s]{0,1}[:\\/|][ ]{0,1}f(emale){0,1}[s]{0,1}","f(emale){0,1}[s]{0,1}[ ]{0,1}[:\\/|][ ]{0,1}m(ale){0,1}[s]{0,1}"};
	String[] Patients = {"number of patients","patients"};
	String PatientPattern1 = "\\bn[ ]{0,1}=[ ]{0,1}[0-9]*\\b";
		

	
	public ClinicalArm  getArmCharacteristics(ClinicalArm arm, int k, Cell[][] cells)
	{
		
		for(int i = 0;i<cells.length;i++)
		{
			cells[i][0].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[i][0].getCell_content()));
			cells[0][k].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[0][k].getCell_content()));
			cells[i][k].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[i][k].getCell_content()));
			boolean femalePercent = false;
			boolean malePercent = false;
			for(String mm:Male){
				Pattern r = Pattern.compile("\\b" + mm);
				Matcher m = r.matcher(cells[i][0].getCell_content().toLowerCase());
				if (m.find()) {
					if(Utilities.isNumeric(cells[i][k].getCell_content()))
					{
						arm.setNoMale(Utilities.getFirstValue(cells[i][k].getCell_content()));
						if(cells[i][0].getCell_content().toLowerCase().contains("%"))
						{
							malePercent = true;
						}
					}
					else
					{
						if(Utilities.getCellType(cells[i][k].getCell_content()).equals("PartNumeric")&&!Utilities.stringContainsItemFromList(cells[i][0].getCell_content().toLowerCase(), NotGenderInfo))
						{
							arm.setNoMale(Utilities.getFirstValue(cells[i][k].getCell_content()));
						}
					}
				}
			}
			for(String mm:Female){
				Pattern r = Pattern.compile("\\b" + mm);
				Matcher m = r.matcher(cells[i][0].getCell_content().toLowerCase());
				if (m.find()) {
					if(Utilities.isNumeric(cells[i][k].getCell_content()))
					{
						arm.setNoFemale(Utilities.getFirstValue(cells[i][k].getCell_content()));
						if(cells[i][0].getCell_content().toLowerCase().contains("%"))
						{
							femalePercent = true;
						}
					}
					else
					{
						if(Utilities.getCellType(cells[i][k].getCell_content()).equals("PartNumeric")&&!Utilities.stringContainsItemFromList(cells[i][k].getCell_content().toLowerCase(), NotGenderInfo)&&!Utilities.stringContainsItemFromList(cells[i][0].getCell_content().toLowerCase(), NotGenderInfo))
						{
							arm.setNoFemale(Utilities.getFirstValue(cells[i][k].getCell_content()));
						}
					}
					
				}
			}
			
			for(String mm:Mixed){
				Pattern r = Pattern.compile("\\b" + mm);
				Matcher m = r.matcher(cells[i][0].getCell_content()
						.toLowerCase());
				if (m.find()) {
				String content = cells[i][k].getCell_content();
				r = Pattern.compile("[:/|\\\\]");
				if(cells[i][k].getCell_content().contains("("))
					r = Pattern.compile("[:/|\\\\]");
				m =r.matcher(content);
				String separator = "";
				if(m.find())
				{
					separator = m.group();
				}
				if(Utilities.NoDimensions(cells[i][k].getCell_content())<2)
					continue;
				String stub = cells[i][0].getCell_content().toLowerCase();
				String[] stubsplit = stub.split(separator);
				for(int j=0;j<stubsplit.length;j++)
				{
					r = Pattern.compile("\\bm(ale){0,1}");
					m =r.matcher(stubsplit[j]);
					if(m.find())
					{
						
						String[] split = content.split(separator);
						
						arm.setNoMale(Utilities.getFirstValue(split[j]));
					}
					r = Pattern.compile("\\bf(emale){0,1}");
					m =r.matcher(stubsplit[j]);
					if(m.find())
					{
						String[] split;
						split = content.split(separator);
						arm.setNoFemale(Utilities.getFirstValue(split[j]));
					}
				}	
			}
			}
			
			for(String mm:Patients){
				Pattern r = Pattern.compile("\\b" + mm);
				Matcher m = r.matcher(cells[i][0].getCell_content()
						.toLowerCase());
				if (m.find()) {
					if(Utilities.isNumeric(cells[i][k].getCell_content()))
					{
						cells[i][k].setCell_content(cells[i][k].getCell_content().replace(",", ""));
						arm.setNoPatients(Utilities.getFirstValue(cells[i][k].getCell_content()));
					}
				}
			}
			cells[i][0].setCell_content(cells[i][0].getCell_content().replace(",", ""));
			Pattern r = Pattern.compile(PatientPattern1);
			Matcher m = r.matcher(cells[i][0].getCell_content()
					.toLowerCase());
			if (m.find()) {
				arm.setNoPatients(Utilities.getFirstValue(m.group()));
			}
			cells[0][k].setCell_content(cells[0][k].getCell_content().replace(",", ""));
			r = Pattern.compile(PatientPattern1);
			m = r.matcher(cells[0][k].getCell_content()
					.toLowerCase());
			if (m.find()) {
				arm.setNoPatients(Utilities.getFirstValue(m.group()));
			}
			//[\(][0-9]{0,}[\)]
			r = Pattern.compile("[\\(][0-9]{0,}[\\)]");
			m = r.matcher(cells[0][k].getCell_content()
					.toLowerCase());
			if (m.find()) {
				arm.setNoPatients(Utilities.getFirstValue(m.group()));
			}
			if(malePercent)
			{
				double Meles = arm.getNoPatients()*(arm.getNoMale()/100.0);
				arm.setNoMale((int)Meles);
			}
			if(femalePercent)
			{
				double Femeles = arm.getNoPatients()*(arm.getNoFemale()/100.0);
				arm.setNoFemale((int)Femeles);
			}
		}
		
		return arm;
	}
	
	
	
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
						if(!Utilities.stringEqualsItemFromList(cells[0][k].getCell_content().toLowerCase(),TotalArm))
						{
						if(cells[0][k].getCell_content().contains("SD"))
						{
							cells[0][k].setCell_content(cells[0][k].getCell_content().replace("SD", ""));
						}
						if(cells[0][k].getCell_content().contains("Mean"))
						{
							cells[0][k].setCell_content(cells[0][k].getCell_content().replace("Mean", ""));
						}
						}
						if(!(Utilities.stringContainsItemFromList(cells[0][k].getCell_content().toLowerCase(), containsNotAnArm)||Utilities.stringEqualsItemFromList(cells[0][k].getCell_content().toLowerCase(), equalsNotAnArm)||Utilities.isSpaceOrEmpty(cells[0][k].getCell_content())))
						{
							//TODO: Change this to list of patterns
							Pattern r = Pattern.compile("\\bp[*-]{0,1}[ ]{0,1}(value){0,1}[s]{0,1}\\b");
						    Matcher m = r.matcher(cells[0][k].getCell_content().toLowerCase());
						    if(!m.find())
						    {
						    	ClinicalArm arm = new ClinicalArm(art.getPmc(),art.getTitle());
						    	arm.setArmName(cells[0][k].getCell_content());
						    	String Content = cells[j][k].getCell_content();
						    	if(cells[j][k].isIs_columnspanning() && cells[j][k].getCells_columnspanning()==cells[j].length)
						    		Content = cells[j+1][k].getCell_content();
						    	String type = Utilities.getCellTypeIsNum(Content);
						    	double d = Utilities.getFirstValue(Content);
						    	
						    	if(type.equals("Numeric") && d>5.0){
						    		arm.setBMI(Content);
						    		arms.add(arm);
						    		arm = getArmCharacteristics(arm,k,cells);
						    	}
						    }
						}
					}
				}
				
				}
				for(String word:weightWords){
					if(!cells[j][0].isIs_header() && cells[j][0].getCell_content().toLowerCase().contains(word))
					{
						for(int k = 1;k<cells[0].length;k++)
						{
							if(!Utilities.stringEqualsItemFromList(cells[0][k].getCell_content().toLowerCase(),TotalArm))
							{
							if(cells[0][k].getCell_content().contains("SD"))
							{
								cells[0][k].setCell_content(cells[0][k].getCell_content().replace("SD", ""));
							}
							if(cells[0][k].getCell_content().contains("Mean"))
							{
								cells[0][k].setCell_content(cells[0][k].getCell_content().replace("Mean", ""));
							}
							}
							if(!(Utilities.stringContainsItemFromList(cells[0][k].getCell_content().toLowerCase(), containsNotAnArm)||Utilities.stringEqualsItemFromList(cells[0][k].getCell_content().toLowerCase(), equalsNotAnArm)))
							{

							    	ClinicalArm arm = new ClinicalArm(art.getPmc(),art.getTitle());
							    	arm.setArmName(cells[0][k].getCell_content());
							    	String Content = cells[j][k].getCell_content();
							    	if(cells[j][k].isIs_columnspanning() && cells[j][k].getCells_columnspanning()==cells[j].length)
							    		Content = cells[j+1][k].getCell_content();
							    	String type = Utilities.getCellTypeIsNum(Content);
							    	double d = Utilities.getFirstValue(Content);
							    	if(type.equals("Numeric") && d>5.0){
							    		arm.setWeight(Content);
							    		arms.add(arm);
							    		arm = getArmCharacteristics(arm,k,cells);
							    	}
							    	
							    
							}
						}
					}
					
					}
			}
		}
		//Mergining arms
		for(int i =0;i<arms.size();i++)
		{
			for(int j = i+1;j<arms.size();j++)
			{
				if(arms.get(i).getArmName().equals(arms.get(j).getArmName()))
				{
					arms.get(i).setWeight(arms.get(j).getWeight());
					arms.remove(j);
				}
			}
			if(Utilities.stringEqualsItemFromList(arms.get(i).getArmName().toLowerCase(),TotalArm)||Utilities.stringContainsItemFromList(arms.get(i).getArmName().toLowerCase(),TotalArmContains))
			{
				arms.get(i).setArmName("Total");
			}
		}
		AddArmsToDB(document_id,arms);
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
		         preparedStatement.setString(4, "kg/m2");
		         preparedStatement.executeUpdate();
			 }
			 if(arms.get(i).getWeight()!=null && !arms.get(i).getWeight().equals(""))
			 {
				 con = DriverManager.getConnection(dbAddress + dbName, userName, password);
		         insertSQL = "INSERT INTO WeightDetails (ArmID, WeightCategory, WeightValue,WeightUnit ) VALUES (?, ?, ?,?)";
		         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
		         preparedStatement.setInt(1, id);
		         preparedStatement.setString(2, "Weight");
		         preparedStatement.setString(3, arms.get(i).getWeight());
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
