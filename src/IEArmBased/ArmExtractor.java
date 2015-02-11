package IEArmBased;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Decomposition.MetaMapOutput;
import Decomposition.MetaMapping;
import Utils.Token;
import Utils.Utilities;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.TablInExMain;
import tablInEx.Table;

public class ArmExtractor {
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbAddress = "jdbc:mysql://localhost:3306/";
	private String dbName = "TiralExtraction";
	private String userName = "root";
	private String password = "";
	private Statement statement;
	private Connection con;
	private String Clear = "\\bn\\b|mean|sd|threatmant|diagnosis|\\bp\\b";
	
	public String clearContent(String CellContent){
		boolean loop = true;
		while(loop){
		
			Pattern r = Pattern.compile(Clear);
			Matcher m =r.matcher(CellContent);
			if(m.find() && !m.group().equals(""))
			{
				CellContent = CellContent.substring(0,m.start())+CellContent.substring(m.end(),CellContent.length());
			}
			else
				loop = false;
		}
		CellContent = CellContent.replace("⋄", "");
		CellContent = CellContent.replace("ρ", "");//⋄
		CellContent = CellContent.replace("±", "");
		CellContent = CellContent.trim().replaceAll(" +", " ");
		
		return CellContent;
	}
	
	
	public void ExtractTrialData(Article art)
	{
		try{
		System.out.println("IEArmBased "+art.getPmc());
		int document_id = AddDocumentDetails(art);
		Table[] tables = art.getTables();
		if(tables==null)
			return;
		LinkedList <ClinicalArm> arms = new LinkedList<ClinicalArm>();
		MetaMapping m = new MetaMapping();
		
		for(int i = 0; i <tables.length;i++)
		{
			Cell[][] cells = tables[i].cells;
			for(int j = 0;j<cells[0].length;j++)
			{
				//LinkedList<Token> tokens = new LinkedList<Token>();
				String CellContent = cells[0][j].getCell_content();
				CellContent = clearContent(CellContent.toLowerCase());
				if(CellContent==null || CellContent.equals("")|| Utilities.isSpaceOrEmpty(CellContent)||CellContent.equals("  "))
					continue;

				Map<Object, Object> aMap = m.getClassification(CellContent);
				int k = 0;
				LinkedList<MetaMapOutput> mp = new LinkedList<MetaMapOutput>();
				while(k<aMap.size())
				{
					MetaMapOutput mo = new MetaMapOutput();
					mo.setConceptID((String)aMap.get(k++));
					mo.setMatchedWords(aMap.get(k++));
					mo.setPositionalInfo(aMap.get(k++));
					mo.setSemanticType((String)aMap.get(k++));
					mo.setConceptName((String)aMap.get(k++));
					mo.setPreferedName((String)aMap.get(k++));
					mp.add(mo);
				}
				int numOfConcepts = 0;
				for(int s = 0;s<mp.size();s++)
				{
					if(TablInExMain.semanticTypes.get((mp.get(s).getSemanticType()))!=null)
						numOfConcepts++;
				}
				if(numOfConcepts>0)
				{
					ClinicalArm ca = new ClinicalArm(art.getPmc(),art.getTitle());
					ca.setArmName(cells[0][j].getCell_content());
					arms.add(ca);	
				}
			}
		}
		saveArms(arms,document_id);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void saveArms(LinkedList<ClinicalArm> arms,int document_id)
	{
			try{
				 Class.forName(jdbcDriver);
				 for(int i = 0;i<arms.size();i++){
		         con = DriverManager.getConnection(dbAddress + dbName, userName, password);
		         String insertSQL = "INSERT INTO ArmDetails (ArmName,  DocumentID ) VALUES (?, ?)";
		         PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
		         preparedStatement.setString(1, arms.get(i).getArmName());
		         preparedStatement.setInt(2, document_id);		         
		         int id=0;
		         preparedStatement.executeUpdate();
		       
		         con.close();
				 }
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
		
	}
	
	
	public int AddDocumentDetails(Article art)
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
	         String insertSQL = "INSERT INTO DocumentDetails (PMC,PISSN,EISSN, Title, Authors,Abstract, LongAbstract ,URL, XML) VALUES (?,?,?, ?, ?,?, ?,?,?)";
	         PreparedStatement preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setString(1, art.getPmc());
	         preparedStatement.setString(2, art.getPissn());
	         preparedStatement.setString(3, art.getEissn());
	         preparedStatement.setString(4, art.getTitle());
	         preparedStatement.setString(5, articleAuthors);
	         preparedStatement.setString(6, art.getShort_abstract());
	         preparedStatement.setString(7, art.getAbstract());
	         preparedStatement.setString(8, URL);
	         preparedStatement.setString(9, art.getXML());

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
	public ArmExtractor()
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
            		+"PISSN VARCHAR(15),"
            		+ "EISSN VARCHAR(15),"
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
                    	//	+"NoPatients INT(64)," 
                    	//	+"NoMale INT(64)," 
                    	//	+"NoFemale INT(64),"
                    		+"DocumentID INT(64),"
                    		+"PRIMARY KEY (`id`));"; 
                        	statement = con.createStatement();
                            //The next line has the issue
                            statement.executeUpdate(myTableName);
                            System.out.println("ArmDetails Table Created");	
                            
                      s.executeUpdate("DROP TABLE IF EXISTS ArmProperty");            
                      myTableName = "CREATE TABLE IF NOT EXISTS ArmProperty ("
                            		+"id INT(64) NOT NULL AUTO_INCREMENT," 
                            		+"ArmID INT(64),"	            		
                            		+"PropertyName VARCHAR(200)," 
                            		+"Type VARCHAR(100)," // May be Mean weight, Weight range, BMI, Weight change 
                            		+"Value VARCHAR(300),"
                            		+"AdditionalInfo VARCHAR(200),"
                            		+"ValueUnit VARCHAR(20),"  //kg, g, kg/m2,g/m2
                            		+"PRIMARY KEY (`id`));"; 
                                	statement = con.createStatement();
                                    //The next line has the issue
                                    statement.executeUpdate(myTableName);
                                    System.out.println("ArmProperty Table Created");	
                                    con.close();
	  } 
        catch (Exception e) {
        	e.printStackTrace();
        }
	
	}

}
