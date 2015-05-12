package IEArmBased;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.didion.jwnl.dictionary.DatabaseBackedDictionary;
import Utils.Utilities;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.TablInExMain;
import tablInEx.Table;

public class IEArmBased21 {
	//Database access data
	private String jdbcDriver = "com.mysql.jdbc.Driver";
	private String dbAddress = "jdbc:mysql://localhost:3306/";
	private String dbName = "TiralExtraction";
	private String userName = "root";
	private String password = "";
	private Statement statement;
	private Connection con;
	String [] equalsNotAnArm = {"p","p*"," ± ","T","ρ","p-value","p* value","%","(%)","[%]","significance",""," "};
	String [] containsNotAnArm = {"all","total","p-value","95%","ratio","p*", "p value"};
	
	public ClinicalArm getArmDetails(ClinicalArm arm, int k, Cell[][] cells,Table t)
	{
		for(int i = 0;i<cells.length;i++)
		{
			cells[i][0].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[i][0].getCell_content()));
			cells[0][k].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[0][k].getCell_content()));
			cells[i][k].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[i][k].getCell_content()));

			String subheader = cells[i][k].getSubheader_values();
			if(subheader!=null)
				subheader = subheader.toLowerCase();
			String stub = subheader+" "+cells[i][0].getCell_content().toLowerCase();
			for (int j = 0; j < TablInExMain.informationClasses.size(); j++) {
				if (Utilities.stringMatchRegexItemFromList(stub,TablInExMain.informationClasses.get(j).MatchList) 
						&& !Utilities.stringMatchRegexItemFromList(stub,TablInExMain.informationClasses.get(j).BlackList)
						&& Utilities.stringMatchRegexItemFromList(stub,TablInExMain.informationClasses.get(j).WhiteList)) {
					ArmProperty w = new ArmProperty();
					w.setUnit(TablInExMain.informationClasses.get(j)
							.getDefault_unit());
					w.setValue(cells[i][k].getCell_content());
					double val = Utilities.getFirstDValue(w.getValue()); 
					//What about SD?
					if ((TablInExMain.informationClasses.get(j).getValue_type().equals("semi-value") ||TablInExMain.informationClasses.get(j).getValue_type().equals("value")) &&  val > TablInExMain.informationClasses.get(j).getMin_value() && TablInExMain.informationClasses.get(j).getMax_value() > val) {
						w.setType(TablInExMain.informationClasses.get(j).getClass_name());
						
					}
					if(w!=null&&w.getType()!=null&&!w.getType().equals(""))
						arm.weights.add(w);

				}
			}
		}
		return arm;
	}
	
	public boolean needTotalArm(LinkedList <ClinicalArm> arms)
	{
		String[] needTotal = {"sd","mean"};
		for(int i = 0;i<arms.size();i++)
		{
			for(int j = 0;j<needTotal.length;j++)
				if(arms.get(i).getArmName().toLowerCase().equals(needTotal[j]))
				{
					return true;
				}
		}
		return false;
	}
	
	public void ExtractTrialData(Article art)
	{
		if(art==null||art.getPmc()==null)//second condition is wierd
			return;
		System.out.println("IEArmBased "+art.getPmc());
		int document_id = AddDocumentDetains(art);
		Table[] tables = art.getTables();
		if(tables==null)
			return;
		LinkedList <ClinicalArm> arms = new LinkedList<ClinicalArm>();

		for(int i = 0; i <tables.length;i++)
		{
			//if(isDemographicTable(tables[i]))
			//{
				Cell[][] cells = tables[i].cells;
				if(cells==null || cells[0]==null)
					continue;
				for(int j = 1;j<cells[0].length;j++)
				{
					cells[0][j].setCell_content(Utilities.ReplaceNonBrakingSpaceToSpace(cells[0][j].getCell_content()));
					if(isColumnArm(cells[0][j],j,tables[i]))
					{
						ClinicalArm arm = new ClinicalArm(art.getPmc(), art.getTitle());
						if(cells[0][j].isIs_header() )
							arm.setArmName(cells[0][j].getCell_content());
						else
							arm.setArmName("Total");
						arm = getArmDetails(arm, j,cells, tables[i]);
						arms.add(arm);
						
					}
				}
			//}
		}
		ClinicalArm TotalArm=null;
		for(int i =0;i<arms.size();i++)
		{
			if(arms.get(i).getArmName().equals("Total"))
			{
				TotalArm =arms.get(i);
			}
		}
		boolean needTotalArm = needTotalArm(arms);
		if(TotalArm == null&needTotalArm){
			TotalArm = new ClinicalArm(art.getPmc(), art.getTitle());
			TotalArm.setArmName("Total");
		}
		if(needTotalArm)
			arms.add(TotalArm);
		//Mergining arms
		for(int i =0;i<arms.size();i++)
		{
			for(int j = i+1;j<arms.size();j++)
			{
				if(arms.get(i).getArmName().equals(arms.get(j).getArmName()))
				{
					//arms.get(i).setWeight(arms.get(j).getWeight());
					HashMap<String,ArmProperty> armproperties = new HashMap<String,ArmProperty>();
					LinkedList<ArmProperty> armproperties2 = new LinkedList<ArmProperty>();
					for(ArmProperty a:arms.get(i).weights)
					{
						armproperties.put(a.getType()+a.getValue(), a);
					}
					for(ArmProperty a:arms.get(j).weights)
					{
						armproperties.put(a.getType()+a.getValue(), a);
					}
				//	LinkedHashMap<String,ArmProperty> lhm = Utilities.sortHashMapByValuesD(armproperties);
					Object [] ss = armproperties.keySet().toArray();
					for(Object s:ss)
					{
						armproperties2.add(armproperties.get(s));
					}
					arms.get(i).weights=armproperties2;
					arms.remove(j);
				}
			}
		}
		//TODO: Kinda dump way to match string in string
		for(int i =0;i<arms.size();i++)
		{
			for(int j = i+1;j<arms.size();j++)
			{
				String[] arm1N = arms.get(i).getArmName().split(" ");
				String[] arm2N = arms.get(j).getArmName().split(" ");
				if(arm1N.length>2&&arm2N.length>2&&arm1N[0].equals(arm2N[0])&&arm1N[1].equals(arm2N[1]))
				{
					HashMap<String,ArmProperty> armproperties = new HashMap<String,ArmProperty>();
					LinkedList<ArmProperty> armproperties2 = new LinkedList<ArmProperty>();
					for(ArmProperty a:arms.get(i).weights)
					{
						armproperties.put(a.getType()+a.getValue(), a);
					}
					for(ArmProperty a:arms.get(j).weights)
					{
						armproperties.put(a.getType()+a.getValue(), a);
					}
					Object [] ss = armproperties.keySet().toArray();
					for(Object s:ss)
					{
						armproperties2.add(armproperties.get(s));
					}
					arms.get(i).weights=armproperties2;
					arms.remove(j);
				}
			}
		}
		for(int i =0;i<arms.size();i++)
		{
			if(arms.get(i).weights.size()==0){
				arms.remove(i);
				i--;
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
	
//	public boolean isDemographicTable(Table t)
//	{
//		boolean isDemoghraphic = false;
//		Cell[][] cells = t.cells;
//		int numDemFeatures = 0;
//		int k = 0;
//		if(cells[0][0].isIs_header())
//			k=1;
//		for(int i = k;i<cells.length;i++)
//		{
//			if(Utilities.stringContainsItemFromList(cells[i][0].getCell_content().toLowerCase(),DemographicTableWords))
//			{
//				numDemFeatures++;
//			}
//		}
//		if(numDemFeatures>0)
//			isDemoghraphic = true;
//		
//		return isDemoghraphic;
//	}
	
	public int AddDocumentDetains(Article art)
	{
		int id = -1;
		try{
			String articleAuthors = "";
			for(int i = 0; i<art.getAuthors().size();i++)
			{
				articleAuthors = articleAuthors+";"+art.getAuthors().get(i).name;
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

	         for(ArmProperty ww: arms.get(i).weights)
	         {
				// con = DriverManager.getConnection(dbAddress + dbName, userName, password);
				 insertSQL = "INSERT INTO ArmProperty (ArmID,PropertyName,  Type,Value,AdditionalInfo,ValueUnit ) VALUES (?,?,?,?,?, ?)";
		         preparedStatement = con.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
		         preparedStatement.setInt(1, id);
		         preparedStatement.setString(2, ww.getType());
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
	
	public IEArmBased21()
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
