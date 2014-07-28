package IEArmBased;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.didion.jwnl.dictionary.DatabaseBackedDictionary;
import Utils.Utilities;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;

public class IEArmBased2 {
	//Database access data
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbAddress = "jdbc:mysql://localhost:3306/";
	private String dbName = "TiralExtraction";
	private String userName = "root";
	private String password = "";
	private Statement statement;
	private Connection con;
	
	
	String[] DemographicTableWords = {"weight","bodyweight","bmi","body mass index"};
	
	String[] weightWords = {"weight","bodyweight"};
	String[] BMIWords = {"bmi","body mass index"};
	
	String [] equalsNotAnArm = {"p","range","p*"," ± ","T","ρ","p-value","p* value","%","(%)","[%]"};
	String [] containsNotAnArm = {"all","total","p-value","95%","ratio","p*", "p value"};
	String [] TotalArm = {"mean±sd","mean ± sd"};
	String [] TotalArmContains = {"n°"};
	String[] Male = {"male\\b","man\\b","men\\b","boy\\b","boys\\b","m\\b"};
	String[] NotGenderInfo = {"±","m-2","m 2","m−2","ratio"};
	String[] Female = {"female\\b","woman\\b","women\\b","girl\\b","girls\\b"};
	String[] Mixed = {"m(ale){0,1}[ ]{0,1}[s]{0,1}[:\\/|][ ]{0,1}f(emale){0,1}[s]{0,1}","f(emale){0,1}[s]{0,1}[ ]{0,1}[:\\/|][ ]{0,1}m(ale){0,1}[s]{0,1}"};
	String[] Patients = {"number of patients","patients"};
	String PatientPattern1 = "\\bn[ ]{0,1}=[ ]{0,1}[0-9]*\\b";
	
	public ClinicalArm  getArmDetails(ClinicalArm arm, int k, Cell[][] cells,Table t)
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
							continue;
						}
					}
					else
					{
						if(Utilities.getCellType(cells[i][k].getCell_content()).equals("PartNumeric")&&!Utilities.stringContainsItemFromList(cells[i][0].getCell_content().toLowerCase(), NotGenderInfo))
						{
							arm.setNoMale(Utilities.getFirstValue(cells[i][k].getCell_content()));
							continue;
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
						continue;
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
			

			String subheader = cells[i][k].getSubheader_values();
			if(subheader!=null)
				subheader = subheader.toLowerCase();
			String stub = cells[i][0].getCell_content().toLowerCase();
			if(Utilities.stringContainsItemFromList(stub, BMIWords))
			{
				Weight w = new Weight();
				w.setUnit("kg/m2");
				w.setValue(cells[i][k].getCell_content());
				int val = Utilities.getFirstValue(w.getValue());
				if(val<20)
					w.setWeightType("BMI change");
				else
					w.setWeightType("BMI");
				arm.weights.add(w);
			}
			if(Utilities.stringContainsItemFromList(subheader, BMIWords))
			{
				Weight w = new Weight();
				w.setUnit("kg/m2");
				w.setValue(cells[i][k].getCell_content());
				int val = Utilities.getFirstValue(w.getValue());
				if(val<20)
					w.setWeightType("BMI change");
				else
					w.setWeightType("BMI");
				w.setAdditionalInfo(cells[i][0].getCell_content());
				arm.weights.add(w);
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
			if(isDemographicTable(tables[i]))
			{
				Cell[][] cells = tables[i].cells;
				for(int j = 1;j<cells[0].length;j++)
				{
					cells[0][j].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[0][j].getCell_content()));
					if(isColumnArm(cells[0][j],j,tables[i]))
					{
						ClinicalArm arm = new ClinicalArm(art.getPmc(), art.getTitle());
						if(cells[0][j].isIs_header())
							arm.setArmName(cells[0][j].getCell_content());
						else
							arm.setArmName("Total");
						arm = getArmDetails(arm, j,cells, tables[i]);
						arms.add(arm);
						
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
		}
		AddArmsToDB(document_id,arms);
	}
	
	public boolean isColumnArm(Cell columnHead,int columnNo,Table t)
	{
		boolean isColumnArm = true;
		if(Utilities.stringContainsItemFromList(columnHead.getCell_content().toLowerCase(), containsNotAnArm)||Utilities.stringEqualsItemFromList(columnHead.getCell_content().toLowerCase(), equalsNotAnArm))
		{
			isColumnArm = false;
		}
		
		return isColumnArm;
	}
	
	public boolean isDemographicTable(Table t)
	{
		boolean isDemoghraphic = false;
		Cell[][] cells = t.cells;
		int numDemFeatures = 0;
		int k = 0;
		if(cells[0][0].isIs_header())
			k=1;
		for(int i = k;i<cells.length;i++)
		{
			if(Utilities.stringContainsItemFromList(cells[i][0].getCell_content().toLowerCase(),DemographicTableWords))
			{
				numDemFeatures++;
			}
		}
		if(numDemFeatures>0)
			isDemoghraphic = true;
		
		return isDemoghraphic;
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
	
	public void AddArmsToDB(int document_id,LinkedList<ClinicalArm> arms)
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
	        ResultSet rs = preparedStatement.getGeneratedKeys();
	         if (rs.next()){
	        	 id=rs.getInt(1);
	         }
	         insertSQL = "INSERT INTO ArmProperty (ArmID, PropertyName, Type, Value, AdditionalInfo,ValueUnit ) VALUES (?,?,?,?,?, ?)";
	         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setInt(1, id);
	         preparedStatement.setString(2, "No. Patients");
	         preparedStatement.setString(3, "Number");
	         preparedStatement.setString(4,arms.get(i).getNoPatients()+"");
	         preparedStatement.setString(5,"");
	         preparedStatement.setString(6,"");
	         preparedStatement.executeUpdate();
	         
	         insertSQL = "INSERT INTO ArmProperty (ArmID,PropertyName,  Type,Value,AdditionalInfo,ValueUnit ) VALUES (?,?,?,?,?, ?)";
	         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setInt(1, id);
	         preparedStatement.setString(2, "No. Male");
	         preparedStatement.setString(3, "Number");
	         preparedStatement.setString(4,arms.get(i).getNoMale()+"");
	         preparedStatement.setString(5,"");
	         preparedStatement.setString(6,"");
	         preparedStatement.executeUpdate();
	         
	         insertSQL = "INSERT INTO ArmProperty (ArmID,PropertyName,  Type,Value,AdditionalInfo,ValueUnit ) VALUES (?,?,?,?,?, ?)";
	         
	         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
	         preparedStatement.setInt(1, id);
	         preparedStatement.setString(2, "No. Female");
	         preparedStatement.setString(3, "Number");
	         preparedStatement.setString(4,arms.get(i).getNoFemale()+"");
	         preparedStatement.setString(5,"");
	         preparedStatement.setString(6,"");
	         preparedStatement.executeUpdate();

	         for(Weight ww: arms.get(i).weights)
	         {
				 con = DriverManager.getConnection(dbAddress + dbName, userName, password);
				 insertSQL = "INSERT INTO ArmProperty (ArmID,PropertyName,  Type,Value,AdditionalInfo,ValueUnit ) VALUES (?,?,?,?,?, ?)";
		         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
		         preparedStatement.setInt(1, id);
		         preparedStatement.setString(2, ww.getWeightType());
		         preparedStatement.setString(3, "Mean+SD");
		         preparedStatement.setString(4,ww.getValue());
		         preparedStatement.setString(5,ww.getAdditionalInfo());
		         preparedStatement.setString(6,ww.getUnit());
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
	
	public IEArmBased2()
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
                            		+"Value VARCHAR(200),"
                            		+"AdditionalInfo VARCHAR(200),"
                            		+"ValueUnit VARCHAR(20),"  //kg, g, kg/m2,g/m2
                            		+"PRIMARY KEY (`id`));"; 
                                	statement = con.createStatement();
                                    //The next line has the issue
                                    statement.executeUpdate(myTableName);
                                    System.out.println("ArmProperty Table Created");	
	  } 
        catch (Exception e) {
        	e.printStackTrace();
        }
	
	}
	
}
