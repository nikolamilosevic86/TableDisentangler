package classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Main.KeyValue;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class SpecPragmaticCreateDataset_posteriori {

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;

	public static void main(String[] args) {
		SpecPragmaticCreateDataset_posteriori sp = new SpecPragmaticCreateDataset_posteriori();
		sp.ProcessTables("NotInteresting");
	}
	
	public Instances CreateInstances(){
		Attribute PMCAttribute = new Attribute("PMC");
		Attribute TableIDAttribute = new Attribute("TableID");
		Attribute NoRowsAttribute = new Attribute("NoRows");
		Attribute NoColumnsAttribute = new Attribute("NoColumn");
		Attribute TotalCellsAttribute = new Attribute("TotalCellNo");
		Attribute EmptyCellsAttribute = new Attribute("CellEmpty");
		Attribute CellNumericAttribute = new Attribute("CellNumeric");
		Attribute CellSemiNumericAttribute = new Attribute(
				"CellSemiNumeric");
		Attribute CellTextAttribute = new Attribute("CellText");
		Attribute ContainsAdverseAttribute = new Attribute(
				"ContainsAdverse");
		Attribute containsAgeAttribute = new Attribute("containsAge");
		Attribute containsBaselineAttribute = new Attribute(
				"containsBaseline");
		Attribute containsCharacteristicCapAttribute = new Attribute(
				"containsCharacteristicCap");
		Attribute containsDemographicAttribute = new Attribute(
				"containsDemographic");
		Attribute ContainsExclusionAttribute = new Attribute(
				"ContainsExclusion");
		Attribute ContainsInclusionAttribute = new Attribute(
				"ContainsInclusion");
		Attribute containsNAttribute = new Attribute("containsN");
		Attribute containsPatientsCapAttribute = new Attribute(
				"containsPatientsCap");
		Attribute containsPatientsCellAttribute = new Attribute(
				"containsPatientsCell");
		Attribute ContainsSideEffectAttribute = new Attribute(
				"ContainsSideEffect");
		Attribute ContainsSignOrSymptomAnnotationAttribute = new Attribute(
				"ContainsSignOrSymptomAnnotation");
		Attribute containsEglibilityAttribute = new Attribute(
				"containsEglibility");
		Attribute containsToxicityAttribute = new Attribute(
				"containsToxicity");
		Attribute containsInclusionExclusionCellAttribute = new Attribute(
				"containsInclusionExclusionCell");
		Attribute containsHaematologicAttribute = new Attribute(
				"containsHaematologic");

		Attribute containsTrialCapAttribute = new Attribute(
				"containsTrialCap");
		Attribute containsCriteriaAttribute = new Attribute(
				"containsCriteria");
//		Attribute attCaption = new Attribute("Caption");
//		Attribute attWholeHeader = new Attribute("WholeHeader");
//		Attribute attWholeStub = new Attribute("WholeStub");
//		Attribute attWholeSuperRow = new Attribute("SuperRow");
//		Attribute attWholeData= new Attribute("Data");
		Attribute caption_topic1= new Attribute("Caption_topic1");
		Attribute caption_topic2= new Attribute("Caption_topic2");
		Attribute caption_topic3= new Attribute("Caption_topic3");
		Attribute caption_topic4= new Attribute("Caption_topic4");
		Attribute caption_topic5= new Attribute("Caption_topic5");
		Attribute caption_topic6= new Attribute("Caption_topic6");
		Attribute caption_topic7= new Attribute("Caption_topic7");
		Attribute caption_topic8= new Attribute("Caption_topic8");
		Attribute caption_topic9= new Attribute("Caption_topic9");
		Attribute caption_topic10= new Attribute("Caption_topic10");
		Attribute caption_topic11= new Attribute("Caption_topic11");
		Attribute caption_topic12= new Attribute("Caption_topic12");
		Attribute caption_topic13= new Attribute("Caption_topic13");
		Attribute caption_topic14= new Attribute("Caption_topic14");
		Attribute caption_topic15= new Attribute("Caption_topic15");
		Attribute caption_topic16= new Attribute("Caption_topic16");
		Attribute caption_topic17= new Attribute("Caption_topic17");
		Attribute caption_topic18= new Attribute("Caption_topic18");
		Attribute caption_topic19= new Attribute("Caption_topic19");
		Attribute caption_topic20= new Attribute("Caption_topic20");
		Attribute caption_topic21= new Attribute("Caption_topic21");
		Attribute caption_topic22= new Attribute("Caption_topic22");
		Attribute caption_topic23= new Attribute("Caption_topic23");
		Attribute caption_topic24= new Attribute("Caption_topic24");
		Attribute caption_topic25= new Attribute("Caption_topic25");
		Attribute caption_topic26= new Attribute("Caption_topic26");
		Attribute caption_topic27= new Attribute("Caption_topic27");
		Attribute caption_topic28= new Attribute("Caption_topic28");
		Attribute caption_topic29= new Attribute("Caption_topic29");
		Attribute caption_topic30= new Attribute("Caption_topic30");
		Attribute caption_topic31= new Attribute("Caption_topic31");
		Attribute caption_topic32= new Attribute("Caption_topic32");
		Attribute caption_topic33= new Attribute("Caption_topic33");
		Attribute caption_topic34= new Attribute("Caption_topic34");
		Attribute caption_topic35= new Attribute("Caption_topic35");
		Attribute caption_topic36= new Attribute("Caption_topic36");
		Attribute caption_topic37= new Attribute("Caption_topic37");
		Attribute caption_topic38= new Attribute("Caption_topic38");
		Attribute caption_topic39= new Attribute("Caption_topic39");
		Attribute caption_topic40= new Attribute("Caption_topic40");
		Attribute caption_topic41= new Attribute("Caption_topic41");
		Attribute caption_topic42= new Attribute("Caption_topic42");
		Attribute caption_topic43= new Attribute("Caption_topic43");
		Attribute caption_topic44= new Attribute("Caption_topic44");
		Attribute caption_topic45= new Attribute("Caption_topic45");
		Attribute caption_topic46= new Attribute("Caption_topic46");
		Attribute caption_topic47= new Attribute("Caption_topic47");
		Attribute caption_topic48= new Attribute("Caption_topic48");
		Attribute caption_topic49= new Attribute("Caption_topic49");
		Attribute caption_topic50= new Attribute("Caption_topic50");
		Attribute sentence_topic1= new Attribute("sentence_topic1");
		Attribute sentence_topic2= new Attribute("sentence_topic2");
		Attribute sentence_topic3= new Attribute("sentence_topic3");
		Attribute sentence_topic4= new Attribute("sentence_topic4");
		Attribute sentence_topic5= new Attribute("sentence_topic5");
		Attribute sentence_topic6= new Attribute("sentence_topic6");
		Attribute sentence_topic7= new Attribute("sentence_topic7");
		Attribute sentence_topic8= new Attribute("sentence_topic8");
		Attribute sentence_topic9= new Attribute("sentence_topic9");
		Attribute sentence_topic10= new Attribute("sentence_topic10");
		Attribute sentence_topic11= new Attribute("sentence_topic11");
		Attribute sentence_topic12= new Attribute("sentence_topic12");
		Attribute sentence_topic13= new Attribute("sentence_topic13");
		Attribute sentence_topic14= new Attribute("sentence_topic14");
		Attribute sentence_topic15= new Attribute("sentence_topic15");
		Attribute sentence_topic16= new Attribute("sentence_topic16");
		Attribute sentence_topic17= new Attribute("sentence_topic17");
		Attribute sentence_topic18= new Attribute("sentence_topic18");
		Attribute sentence_topic19= new Attribute("sentence_topic19");
		Attribute sentence_topic20= new Attribute("sentence_topic20");
		Attribute sentence_topic21= new Attribute("sentence_topic21");
		Attribute sentence_topic22= new Attribute("sentence_topic22");
		Attribute sentence_topic23= new Attribute("sentence_topic23");
		Attribute sentence_topic24= new Attribute("sentence_topic24");
		Attribute sentence_topic25= new Attribute("sentence_topic25");
		Attribute sentence_topic26= new Attribute("sentence_topic26");
		Attribute sentence_topic27= new Attribute("sentence_topic27");
		Attribute sentence_topic28= new Attribute("sentence_topic28");
		Attribute sentence_topic29= new Attribute("sentence_topic29");
		Attribute sentence_topic30= new Attribute("sentence_topic30");
		Attribute sentence_topic31= new Attribute("sentence_topic31");
		Attribute sentence_topic32= new Attribute("sentence_topic32");
		Attribute sentence_topic33= new Attribute("sentence_topic33");
		Attribute sentence_topic34= new Attribute("sentence_topic34");
		Attribute sentence_topic35= new Attribute("sentence_topic35");
		Attribute sentence_topic36= new Attribute("sentence_topic36");
		Attribute sentence_topic37= new Attribute("sentence_topic37");
		Attribute sentence_topic38= new Attribute("sentence_topic38");
		Attribute sentence_topic39= new Attribute("sentence_topic39");
		Attribute sentence_topic40= new Attribute("sentence_topic40");
		Attribute sentence_topic41= new Attribute("sentence_topic41");
		Attribute sentence_topic42= new Attribute("sentence_topic42");
		Attribute sentence_topic43= new Attribute("sentence_topic43");
		Attribute sentence_topic44= new Attribute("sentence_topic44");
		Attribute sentence_topic45= new Attribute("sentence_topic45");
		Attribute sentence_topic46= new Attribute("sentence_topic46");
		Attribute sentence_topic47= new Attribute("sentence_topic47");
		Attribute sentence_topic48= new Attribute("sentence_topic48");
		Attribute sentence_topic49= new Attribute("sentence_topic49");
		Attribute sentence_topic50= new Attribute("sentence_topic50");
		FastVector fvClassVal = new FastVector(4);
		// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
		fvClassVal.addElement("AdverseEvent");
		fvClassVal.addElement("InclusionExclusion");
		fvClassVal.addElement("Other");
		fvClassVal.addElement("BaselineCharacteristic");
		Attribute ClassAttribute = new Attribute("clas", fvClassVal);
		// Declare the feature vector
		FastVector fvWekaAttributes = new FastVector(128);
		fvWekaAttributes.addElement(PMCAttribute);
		fvWekaAttributes.addElement(TableIDAttribute);
		fvWekaAttributes.addElement(NoRowsAttribute);
		fvWekaAttributes.addElement(NoColumnsAttribute);
		fvWekaAttributes.addElement(TotalCellsAttribute);
		fvWekaAttributes.addElement(EmptyCellsAttribute);
		fvWekaAttributes.addElement(CellNumericAttribute);
		fvWekaAttributes.addElement(CellSemiNumericAttribute);
		fvWekaAttributes.addElement(CellTextAttribute);
		fvWekaAttributes.addElement(ContainsAdverseAttribute);
		fvWekaAttributes.addElement(containsAgeAttribute);
		fvWekaAttributes.addElement(containsBaselineAttribute);
		fvWekaAttributes.addElement(containsCharacteristicCapAttribute);
		fvWekaAttributes.addElement(containsDemographicAttribute);
		fvWekaAttributes.addElement(ContainsExclusionAttribute);
		fvWekaAttributes.addElement(ContainsInclusionAttribute);
		fvWekaAttributes.addElement(containsNAttribute);
		fvWekaAttributes.addElement(containsPatientsCapAttribute);
		fvWekaAttributes.addElement(containsPatientsCellAttribute);
		fvWekaAttributes.addElement(ContainsSideEffectAttribute);
		fvWekaAttributes
				.addElement(ContainsSignOrSymptomAnnotationAttribute);
		fvWekaAttributes.addElement(containsEglibilityAttribute);
		fvWekaAttributes.addElement(containsToxicityAttribute);
		fvWekaAttributes
				.addElement(containsInclusionExclusionCellAttribute);
		fvWekaAttributes.addElement(containsHaematologicAttribute);

		fvWekaAttributes.addElement(containsTrialCapAttribute);
		fvWekaAttributes.addElement(containsCriteriaAttribute);
//		fvWekaAttributes.addElement(new Attribute("WholeHeader", (FastVector) null));
//		fvWekaAttributes.addElement(new Attribute("WholeStub", (FastVector) null));
//		fvWekaAttributes.addElement(new Attribute("WholeSuperRow", (FastVector) null));
//		fvWekaAttributes.addElement(new Attribute("WholeData", (FastVector) null));
//		fvWekaAttributes.addElement(new Attribute("Caption", (FastVector) null));
		
		fvWekaAttributes.addElement(caption_topic1);
		fvWekaAttributes.addElement(caption_topic2);
		fvWekaAttributes.addElement(caption_topic3);
		fvWekaAttributes.addElement(caption_topic4);
		fvWekaAttributes.addElement(caption_topic5);
		fvWekaAttributes.addElement(caption_topic6);
		fvWekaAttributes.addElement(caption_topic7);
		fvWekaAttributes.addElement(caption_topic8);
		fvWekaAttributes.addElement(caption_topic9);
		fvWekaAttributes.addElement(caption_topic10);
		fvWekaAttributes.addElement(caption_topic11);
		fvWekaAttributes.addElement(caption_topic12);
		fvWekaAttributes.addElement(caption_topic13);
		fvWekaAttributes.addElement(caption_topic14);
		fvWekaAttributes.addElement(caption_topic15);
		fvWekaAttributes.addElement(caption_topic16);
		fvWekaAttributes.addElement(caption_topic17);
		fvWekaAttributes.addElement(caption_topic18);
		fvWekaAttributes.addElement(caption_topic19);
		fvWekaAttributes.addElement(caption_topic20);
		fvWekaAttributes.addElement(caption_topic21);
		fvWekaAttributes.addElement(caption_topic22);
		fvWekaAttributes.addElement(caption_topic23);
		fvWekaAttributes.addElement(caption_topic24);
		fvWekaAttributes.addElement(caption_topic25);
		fvWekaAttributes.addElement(caption_topic26);
		fvWekaAttributes.addElement(caption_topic27);
		fvWekaAttributes.addElement(caption_topic28);
		fvWekaAttributes.addElement(caption_topic29);
		fvWekaAttributes.addElement(caption_topic30);
		fvWekaAttributes.addElement(caption_topic31);
		fvWekaAttributes.addElement(caption_topic32);
		fvWekaAttributes.addElement(caption_topic33);
		fvWekaAttributes.addElement(caption_topic34);
		fvWekaAttributes.addElement(caption_topic35);
		fvWekaAttributes.addElement(caption_topic36);
		fvWekaAttributes.addElement(caption_topic37);
		fvWekaAttributes.addElement(caption_topic38);
		fvWekaAttributes.addElement(caption_topic39);
		fvWekaAttributes.addElement(caption_topic40);
		fvWekaAttributes.addElement(caption_topic41);
		fvWekaAttributes.addElement(caption_topic42);
		fvWekaAttributes.addElement(caption_topic43);
		fvWekaAttributes.addElement(caption_topic44);
		fvWekaAttributes.addElement(caption_topic45);
		fvWekaAttributes.addElement(caption_topic46);
		fvWekaAttributes.addElement(caption_topic47);
		fvWekaAttributes.addElement(caption_topic48);
		fvWekaAttributes.addElement(caption_topic49);
		fvWekaAttributes.addElement(caption_topic50);
		
		fvWekaAttributes.addElement(sentence_topic1);
		fvWekaAttributes.addElement(sentence_topic2);
		fvWekaAttributes.addElement(sentence_topic3);
		fvWekaAttributes.addElement(sentence_topic4);
		fvWekaAttributes.addElement(sentence_topic5);
		fvWekaAttributes.addElement(sentence_topic6);
		fvWekaAttributes.addElement(sentence_topic7);
		fvWekaAttributes.addElement(sentence_topic8);
		fvWekaAttributes.addElement(sentence_topic9);
		fvWekaAttributes.addElement(sentence_topic10);
		fvWekaAttributes.addElement(sentence_topic11);
		fvWekaAttributes.addElement(sentence_topic12);
		fvWekaAttributes.addElement(sentence_topic13);
		fvWekaAttributes.addElement(sentence_topic14);
		fvWekaAttributes.addElement(sentence_topic15);
		fvWekaAttributes.addElement(sentence_topic16);
		fvWekaAttributes.addElement(sentence_topic17);
		fvWekaAttributes.addElement(sentence_topic18);
		fvWekaAttributes.addElement(sentence_topic19);
		fvWekaAttributes.addElement(sentence_topic20);
		fvWekaAttributes.addElement(sentence_topic21);
		fvWekaAttributes.addElement(sentence_topic22);
		fvWekaAttributes.addElement(sentence_topic23);
		fvWekaAttributes.addElement(sentence_topic24);
		fvWekaAttributes.addElement(sentence_topic25);
		fvWekaAttributes.addElement(sentence_topic26);
		fvWekaAttributes.addElement(sentence_topic27);
		fvWekaAttributes.addElement(sentence_topic28);
		fvWekaAttributes.addElement(sentence_topic29);
		fvWekaAttributes.addElement(sentence_topic30);
		fvWekaAttributes.addElement(sentence_topic31);
		fvWekaAttributes.addElement(sentence_topic32);
		fvWekaAttributes.addElement(sentence_topic33);
		fvWekaAttributes.addElement(sentence_topic34);
		fvWekaAttributes.addElement(sentence_topic35);
		fvWekaAttributes.addElement(sentence_topic36);
		fvWekaAttributes.addElement(sentence_topic37);
		fvWekaAttributes.addElement(sentence_topic38);
		fvWekaAttributes.addElement(sentence_topic39);
		fvWekaAttributes.addElement(sentence_topic40);
		fvWekaAttributes.addElement(sentence_topic41);
		fvWekaAttributes.addElement(sentence_topic42);
		fvWekaAttributes.addElement(sentence_topic43);
		fvWekaAttributes.addElement(sentence_topic44);
		fvWekaAttributes.addElement(sentence_topic45);
		fvWekaAttributes.addElement(sentence_topic46);
		fvWekaAttributes.addElement(sentence_topic47);
		fvWekaAttributes.addElement(sentence_topic48);
		fvWekaAttributes.addElement(sentence_topic49);
		fvWekaAttributes.addElement(sentence_topic50);
		fvWekaAttributes.addElement(ClassAttribute);
		Instances Instances = new Instances("Rel", fvWekaAttributes, 0);
		Instances.setClassIndex(127);
		return Instances;
	}
	
	public void ProcessTables(String tableType)
	{
		
		DataBase();
		int execCount = 0;
		try {
			String SQL = "SELECT * from ArtTable where HasXML='yes' and specPragmatic='"+tableType+"' order by RAND() limit 200";
			Statement st = conn.createStatement();
			Instances instances = CreateInstances();
			FastVector fvWekaAttributes = new FastVector(128);
			rs = st.executeQuery(SQL);
			while (rs.next()) {
				Instance iExample = processTable(rs.getInt(1));
				instances.add(iExample);
				

				execCount ++;
				if(execCount>10000){
					conn.close();
					DataBase();
					execCount = 0;	
				}

			}
			System.out.println(instances.toString());
			ArffSaver saver = new ArffSaver();
			 saver.setInstances(instances);
			 saver.setFile(new File("spptest.arff"));
			 //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
			 saver.writeBatch();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void DataBase() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"settings.cfg"));
			// StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String host = "";
			String database_name = "";
			String database_username = "";
			String database_password = "";
			String database_port = "";

			while (line != null) {
				KeyValue kv = new KeyValue();
				String[] parts = line.split(";");
				kv.key = parts[0];
				kv.value = parts[1];
				if (kv.key.equals("database_host")) {
					host = kv.value;
				}
				if (kv.key.equals("database_name")) {
					database_name = kv.value;
				}
				if (kv.key.equals("database_username")) {
					database_username = kv.value;
				}
				if (kv.key.equals("database_password")) {
					database_password = kv.value;
				}
				if (kv.key.equals("database_port")) {
					database_port = kv.value;
				}
				line = br.readLine();
			}

			database_password = database_password.replace("\"", "");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connectionUrl = "jdbc:mysql://" + host + ":" + database_port
					+ "/" + database_name;
			connectionUser = database_username;
			connectionPassword = database_password;
			conn = DriverManager.getConnection(connectionUrl, connectionUser,
					connectionPassword);
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Instance processTable(int tableid) {
		String classification = "NotInteresting";
		Instance iExample = null;
		try {
			int containsDemographic = 0;
			int containsPatientsCap = 0;
			int containsPatientsCell = 0;
			int containsBaseline = 0;
			int containsAge = 0;
			int containsN = 0;
			int containsTrialCap = 0;
			int containsCharacteristicCap = 0;
			int NoColumn = 0;
			int NoRows = 0;
			int ContainsInclusion = 0;
			int ContainsExclusion = 0;
			int containsEglibility = 0;
			int containsInclusionExclusionCell = 0;
			int containsToxicity = 0;
			int containsHaematologic = 0;
			float CellNumeric = 0;
			float CellText = 0;
			float CellEmpty = 0;
			float CellSemiNumeric = 0;
			int TotalCellNo = 0;
			int ContainsAdverse = 0;
			int ContainsSideEffect = 0;
			int ContainsSignOrSymptomAnnotation = 0;
			int containsCriteria = 0;
			String Caption = "";
			String WholeHeader = "";
			String WholeStub = "";
			String WholeSuperRow="";
			String WholeData = "";
			int PMC = 0;
			int[] caption_topics = new  int[50];
			int[] ref_sentence_topic = new int[50];

			String SQL = "select * from ArtTable inner join article on article.idArticle=ArtTable.Article_idArticle where idTable=" + tableid;
			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(SQL);

			// iterate through the java resultset
			while (rs.next()) {
				Caption = rs.getString(3);
				PMC = rs.getInt(12);
			}

			SQL = "select * from Cell where Table_idTable=" + tableid;
			Statement st2 = conn.createStatement();

			ResultSet rs2 = st2.executeQuery(SQL);

			// iterate through the java resultset
			while (rs2.next()) {
				int cell_id = rs2.getInt(1);
				String SQL2 = "select * from cellroles inner join cell on cell.idCell=cellroles.Cell_idCell where Cell_idCell=" + cell_id;
				Statement st3 = conn.createStatement();

				ResultSet rs3 = st3.executeQuery(SQL2);
				while (rs3.next()) {
					int CellRole = rs3.getInt(2);
					if(CellRole==1)
					{
						WholeHeader+="; "+rs3.getString(13).toLowerCase();
					}
					if(CellRole==2)
					{
						WholeStub+="; "+rs3.getString(13).toLowerCase();
					}
					if(CellRole==3)
					{
						WholeData+="; "+rs3.getString(13).toLowerCase();
					}
					if(CellRole==4)
					{
						WholeSuperRow+="; "+rs3.getString(13).toLowerCase();
					}
				}
				String SQL4 = "select * from topics_captions where Table_id=" + tableid;
				Statement st4 = conn.createStatement();

				ResultSet rs4 = st4.executeQuery(SQL4);
				while (rs4.next()) {
					String num = rs4.getString(3);
					caption_topics[Integer.parseInt(num)]=1;
				}
				
				String SQL5 = "SELECT * FROM table_db.tablesentences inner join sentence_topic on idTableSentences = SentenceID where Table_idTable=" + tableid;
				Statement st5 = conn.createStatement();

				ResultSet rs5 = st5.executeQuery(SQL5);
				while (rs5.next()) {
					ref_sentence_topic[rs5.getInt(8)]=1;
				}
				
				String CellType = rs2.getString(3);
				if (CellType.equalsIgnoreCase("Empty")) {
					CellEmpty++;
				}
				if (CellType.equalsIgnoreCase("Partially Numeric")) {
					CellSemiNumeric++;
				}
				if (CellType.equalsIgnoreCase("Numeric")) {
					CellNumeric++;
				}
				if (CellType.equalsIgnoreCase("Text")) {
					CellText++;
				}
				TotalCellNo++;
				if (NoRows < rs2.getInt(5))
					NoRows = rs2.getInt(5);
				if (NoColumn < rs2.getInt(6))
					NoColumn = rs2.getInt(6);
				if (rs2.getString(10).toLowerCase().contains("patient"))
					containsPatientsCell = 1;
				if (rs2.getString(10).toLowerCase().contains("age"))
					containsAge = 1;
				if (rs2.getString(10).toLowerCase().contains("n=")
						|| rs2.getString(10).toLowerCase().contains("n ="))
					containsN = 1;
				if (rs2.getString(10).toLowerCase().contains("inclusion")
						|| rs2.getString(10).toLowerCase()
								.contains("exclusion"))
					containsInclusionExclusionCell = 1;
			}
			NoRows++;
			NoColumn++;
			if(tableid == 260)
			{
				System.out.println("I am here!");
			}
			if (Caption.toLowerCase().contains("patient"))
				containsPatientsCap = 1;
			if (Caption.toLowerCase().contains("characteristic"))
				containsCharacteristicCap = 1;
			if (Caption.toLowerCase().contains("demograph"))
				containsDemographic = 1;
			if (Caption.toLowerCase().contains("baseline"))
				containsBaseline = 1;
			if (Caption.toLowerCase().contains("trial"))
				containsBaseline = 1;
			if (Caption.toLowerCase().contains("inclusion"))
				containsBaseline = 1;
			if (Caption.toLowerCase().contains("exclusion"))
				ContainsExclusion = 1;
			if (Caption.toLowerCase().contains("adverse"))
				ContainsAdverse = 1;
			if (Caption.toLowerCase().contains("side effect"))
				ContainsAdverse = 1;
			if (Caption.toLowerCase().contains("eligibi"))
				containsEglibility = 1;
			if (Caption.toLowerCase().contains("toxicity"))
				containsToxicity = 1;
			if (Caption.toLowerCase().contains("haematologic"))
				containsToxicity = 1;
			if (Caption.toLowerCase().contains("criteria"))
				containsCriteria = 1;

			SQL = "select * from Annotation inner join Cell on Cell.idCell=Annotation.Cell_idCell where  Annotation.AgentName='MetaMap' and AnnotationDescription LIKE '%Symptom%' and Table_idTable="
					+ tableid;
			Statement st3 = conn.createStatement();
			ResultSet rs3 = st3.executeQuery(SQL);
			rs3.last();
			int size = rs3.getRow();
			rs3.beforeFirst();
			if (size > 0) {
				ContainsSignOrSymptomAnnotation = 1;
			}

			CellEmpty = ((float) (CellEmpty)) / ((float) (TotalCellNo)) * 100;
			CellText = ((float) (CellText)) / ((float) (TotalCellNo)) * 100;
			CellNumeric = ((float) (CellNumeric)) / ((float) (TotalCellNo))
					* 100;
			CellSemiNumeric = ((float) (CellSemiNumeric))
					/ ((float) (TotalCellNo)) * 100;
			String prediction = "";
			Instances ins = null;
			Attribute PMCAttribute = new Attribute("PMC");
			Attribute TableIDAttribute = new Attribute("TableID");
			Attribute NoRowsAttribute = new Attribute("NoRows");
			Attribute NoColumnsAttribute = new Attribute("NoColumn");
			Attribute TotalCellsAttribute = new Attribute("TotalCellNo");
			Attribute EmptyCellsAttribute = new Attribute("CellEmpty");
			Attribute CellNumericAttribute = new Attribute("CellNumeric");
			Attribute CellSemiNumericAttribute = new Attribute(
					"CellSemiNumeric");
			Attribute CellTextAttribute = new Attribute("CellText");
			Attribute ContainsAdverseAttribute = new Attribute(
					"ContainsAdverse");
			Attribute containsAgeAttribute = new Attribute("containsAge");
			Attribute containsBaselineAttribute = new Attribute(
					"containsBaseline");
			Attribute containsCharacteristicCapAttribute = new Attribute(
					"containsCharacteristicCap");
			Attribute containsDemographicAttribute = new Attribute(
					"containsDemographic");
			Attribute ContainsExclusionAttribute = new Attribute(
					"ContainsExclusion");
			Attribute ContainsInclusionAttribute = new Attribute(
					"ContainsInclusion");
			Attribute containsNAttribute = new Attribute("containsN");
			Attribute containsPatientsCapAttribute = new Attribute(
					"containsPatientsCap");
			Attribute containsPatientsCellAttribute = new Attribute(
					"containsPatientsCell");
			Attribute ContainsSideEffectAttribute = new Attribute(
					"ContainsSideEffect");
			Attribute ContainsSignOrSymptomAnnotationAttribute = new Attribute(
					"ContainsSignOrSymptomAnnotation");
			Attribute containsEglibilityAttribute = new Attribute(
					"containsEglibility");
			Attribute containsToxicityAttribute = new Attribute(
					"containsToxicity");
			Attribute containsInclusionExclusionCellAttribute = new Attribute(
					"containsInclusionExclusionCell");
			Attribute containsHaematologicAttribute = new Attribute(
					"containsHaematologic");

			Attribute containsTrialCapAttribute = new Attribute(
					"containsTrialCap");
			Attribute containsCriteriaAttribute = new Attribute(
					"containsCriteria");
//			Attribute attCaption = new Attribute("Caption");
//			Attribute attWholeHeader = new Attribute("WholeHeader");
//			Attribute attWholeStub = new Attribute("WholeStub");
//			Attribute attWholeSuperRow = new Attribute("SuperRow");
//			Attribute attWholeData= new Attribute("Data");
			Attribute caption_topic1= new Attribute("Caption_topic1");
			Attribute caption_topic2= new Attribute("Caption_topic2");
			Attribute caption_topic3= new Attribute("Caption_topic3");
			Attribute caption_topic4= new Attribute("Caption_topic4");
			Attribute caption_topic5= new Attribute("Caption_topic5");
			Attribute caption_topic6= new Attribute("Caption_topic6");
			Attribute caption_topic7= new Attribute("Caption_topic7");
			Attribute caption_topic8= new Attribute("Caption_topic8");
			Attribute caption_topic9= new Attribute("Caption_topic9");
			Attribute caption_topic10= new Attribute("Caption_topic10");
			Attribute caption_topic11= new Attribute("Caption_topic11");
			Attribute caption_topic12= new Attribute("Caption_topic12");
			Attribute caption_topic13= new Attribute("Caption_topic13");
			Attribute caption_topic14= new Attribute("Caption_topic14");
			Attribute caption_topic15= new Attribute("Caption_topic15");
			Attribute caption_topic16= new Attribute("Caption_topic16");
			Attribute caption_topic17= new Attribute("Caption_topic17");
			Attribute caption_topic18= new Attribute("Caption_topic18");
			Attribute caption_topic19= new Attribute("Caption_topic19");
			Attribute caption_topic20= new Attribute("Caption_topic20");
			Attribute caption_topic21= new Attribute("Caption_topic21");
			Attribute caption_topic22= new Attribute("Caption_topic22");
			Attribute caption_topic23= new Attribute("Caption_topic23");
			Attribute caption_topic24= new Attribute("Caption_topic24");
			Attribute caption_topic25= new Attribute("Caption_topic25");
			Attribute caption_topic26= new Attribute("Caption_topic26");
			Attribute caption_topic27= new Attribute("Caption_topic27");
			Attribute caption_topic28= new Attribute("Caption_topic28");
			Attribute caption_topic29= new Attribute("Caption_topic29");
			Attribute caption_topic30= new Attribute("Caption_topic30");
			Attribute caption_topic31= new Attribute("Caption_topic31");
			Attribute caption_topic32= new Attribute("Caption_topic32");
			Attribute caption_topic33= new Attribute("Caption_topic33");
			Attribute caption_topic34= new Attribute("Caption_topic34");
			Attribute caption_topic35= new Attribute("Caption_topic35");
			Attribute caption_topic36= new Attribute("Caption_topic36");
			Attribute caption_topic37= new Attribute("Caption_topic37");
			Attribute caption_topic38= new Attribute("Caption_topic38");
			Attribute caption_topic39= new Attribute("Caption_topic39");
			Attribute caption_topic40= new Attribute("Caption_topic40");
			Attribute caption_topic41= new Attribute("Caption_topic41");
			Attribute caption_topic42= new Attribute("Caption_topic42");
			Attribute caption_topic43= new Attribute("Caption_topic43");
			Attribute caption_topic44= new Attribute("Caption_topic44");
			Attribute caption_topic45= new Attribute("Caption_topic45");
			Attribute caption_topic46= new Attribute("Caption_topic46");
			Attribute caption_topic47= new Attribute("Caption_topic47");
			Attribute caption_topic48= new Attribute("Caption_topic48");
			Attribute caption_topic49= new Attribute("Caption_topic49");
			Attribute caption_topic50= new Attribute("Caption_topic50");
			Attribute sentence_topic1= new Attribute("sentence_topic1");
			Attribute sentence_topic2= new Attribute("sentence_topic2");
			Attribute sentence_topic3= new Attribute("sentence_topic3");
			Attribute sentence_topic4= new Attribute("sentence_topic4");
			Attribute sentence_topic5= new Attribute("sentence_topic5");
			Attribute sentence_topic6= new Attribute("sentence_topic6");
			Attribute sentence_topic7= new Attribute("sentence_topic7");
			Attribute sentence_topic8= new Attribute("sentence_topic8");
			Attribute sentence_topic9= new Attribute("sentence_topic9");
			Attribute sentence_topic10= new Attribute("sentence_topic10");
			Attribute sentence_topic11= new Attribute("sentence_topic11");
			Attribute sentence_topic12= new Attribute("sentence_topic12");
			Attribute sentence_topic13= new Attribute("sentence_topic13");
			Attribute sentence_topic14= new Attribute("sentence_topic14");
			Attribute sentence_topic15= new Attribute("sentence_topic15");
			Attribute sentence_topic16= new Attribute("sentence_topic16");
			Attribute sentence_topic17= new Attribute("sentence_topic17");
			Attribute sentence_topic18= new Attribute("sentence_topic18");
			Attribute sentence_topic19= new Attribute("sentence_topic19");
			Attribute sentence_topic20= new Attribute("sentence_topic20");
			Attribute sentence_topic21= new Attribute("sentence_topic21");
			Attribute sentence_topic22= new Attribute("sentence_topic22");
			Attribute sentence_topic23= new Attribute("sentence_topic23");
			Attribute sentence_topic24= new Attribute("sentence_topic24");
			Attribute sentence_topic25= new Attribute("sentence_topic25");
			Attribute sentence_topic26= new Attribute("sentence_topic26");
			Attribute sentence_topic27= new Attribute("sentence_topic27");
			Attribute sentence_topic28= new Attribute("sentence_topic28");
			Attribute sentence_topic29= new Attribute("sentence_topic29");
			Attribute sentence_topic30= new Attribute("sentence_topic30");
			Attribute sentence_topic31= new Attribute("sentence_topic31");
			Attribute sentence_topic32= new Attribute("sentence_topic32");
			Attribute sentence_topic33= new Attribute("sentence_topic33");
			Attribute sentence_topic34= new Attribute("sentence_topic34");
			Attribute sentence_topic35= new Attribute("sentence_topic35");
			Attribute sentence_topic36= new Attribute("sentence_topic36");
			Attribute sentence_topic37= new Attribute("sentence_topic37");
			Attribute sentence_topic38= new Attribute("sentence_topic38");
			Attribute sentence_topic39= new Attribute("sentence_topic39");
			Attribute sentence_topic40= new Attribute("sentence_topic40");
			Attribute sentence_topic41= new Attribute("sentence_topic41");
			Attribute sentence_topic42= new Attribute("sentence_topic42");
			Attribute sentence_topic43= new Attribute("sentence_topic43");
			Attribute sentence_topic44= new Attribute("sentence_topic44");
			Attribute sentence_topic45= new Attribute("sentence_topic45");
			Attribute sentence_topic46= new Attribute("sentence_topic46");
			Attribute sentence_topic47= new Attribute("sentence_topic47");
			Attribute sentence_topic48= new Attribute("sentence_topic48");
			Attribute sentence_topic49= new Attribute("sentence_topic49");
			Attribute sentence_topic50= new Attribute("sentence_topic50");
			FastVector fvClassVal = new FastVector(4);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("AdverseEvent");
			fvClassVal.addElement("InclusionExclusion");
			fvClassVal.addElement("Other");
			fvClassVal.addElement("BaselineCharacteristic");
			Attribute ClassAttribute = new Attribute("clas", fvClassVal);
			// Declare the feature vector
			FastVector fvWekaAttributes = new FastVector(128);
			
			fvWekaAttributes.addElement(PMCAttribute);
			fvWekaAttributes.addElement(TableIDAttribute);
			fvWekaAttributes.addElement(NoRowsAttribute);
			fvWekaAttributes.addElement(NoColumnsAttribute);
			fvWekaAttributes.addElement(TotalCellsAttribute);
			fvWekaAttributes.addElement(EmptyCellsAttribute);
			fvWekaAttributes.addElement(CellNumericAttribute);
			fvWekaAttributes.addElement(CellSemiNumericAttribute);
			fvWekaAttributes.addElement(CellTextAttribute);
			fvWekaAttributes.addElement(ContainsAdverseAttribute);
			fvWekaAttributes.addElement(containsAgeAttribute);
			fvWekaAttributes.addElement(containsBaselineAttribute);
			fvWekaAttributes.addElement(containsCharacteristicCapAttribute);
			fvWekaAttributes.addElement(containsDemographicAttribute);
			fvWekaAttributes.addElement(ContainsExclusionAttribute);
			fvWekaAttributes.addElement(ContainsInclusionAttribute);
			fvWekaAttributes.addElement(containsNAttribute);
			fvWekaAttributes.addElement(containsPatientsCapAttribute);
			fvWekaAttributes.addElement(containsPatientsCellAttribute);
			fvWekaAttributes.addElement(ContainsSideEffectAttribute);
			fvWekaAttributes
					.addElement(ContainsSignOrSymptomAnnotationAttribute);
			fvWekaAttributes.addElement(containsEglibilityAttribute);
			fvWekaAttributes.addElement(containsToxicityAttribute);
			fvWekaAttributes
					.addElement(containsInclusionExclusionCellAttribute);
			fvWekaAttributes.addElement(containsHaematologicAttribute);

			fvWekaAttributes.addElement(containsTrialCapAttribute);
			fvWekaAttributes.addElement(containsCriteriaAttribute);
//			fvWekaAttributes.addElement(new Attribute("WholeHeader", (FastVector) null));
//			fvWekaAttributes.addElement(new Attribute("WholeStub", (FastVector) null));
//			fvWekaAttributes.addElement(new Attribute("WholeSuperRow", (FastVector) null));
//			fvWekaAttributes.addElement(new Attribute("WholeData", (FastVector) null));
//			fvWekaAttributes.addElement(new Attribute("Caption", (FastVector) null));
			
			fvWekaAttributes.addElement(caption_topic1);
			fvWekaAttributes.addElement(caption_topic2);
			fvWekaAttributes.addElement(caption_topic3);
			fvWekaAttributes.addElement(caption_topic4);
			fvWekaAttributes.addElement(caption_topic5);
			fvWekaAttributes.addElement(caption_topic6);
			fvWekaAttributes.addElement(caption_topic7);
			fvWekaAttributes.addElement(caption_topic8);
			fvWekaAttributes.addElement(caption_topic9);
			fvWekaAttributes.addElement(caption_topic10);
			fvWekaAttributes.addElement(caption_topic11);
			fvWekaAttributes.addElement(caption_topic12);
			fvWekaAttributes.addElement(caption_topic13);
			fvWekaAttributes.addElement(caption_topic14);
			fvWekaAttributes.addElement(caption_topic15);
			fvWekaAttributes.addElement(caption_topic16);
			fvWekaAttributes.addElement(caption_topic17);
			fvWekaAttributes.addElement(caption_topic18);
			fvWekaAttributes.addElement(caption_topic19);
			fvWekaAttributes.addElement(caption_topic20);
			fvWekaAttributes.addElement(caption_topic21);
			fvWekaAttributes.addElement(caption_topic22);
			fvWekaAttributes.addElement(caption_topic23);
			fvWekaAttributes.addElement(caption_topic24);
			fvWekaAttributes.addElement(caption_topic25);
			fvWekaAttributes.addElement(caption_topic26);
			fvWekaAttributes.addElement(caption_topic27);
			fvWekaAttributes.addElement(caption_topic28);
			fvWekaAttributes.addElement(caption_topic29);
			fvWekaAttributes.addElement(caption_topic30);
			fvWekaAttributes.addElement(caption_topic31);
			fvWekaAttributes.addElement(caption_topic32);
			fvWekaAttributes.addElement(caption_topic33);
			fvWekaAttributes.addElement(caption_topic34);
			fvWekaAttributes.addElement(caption_topic35);
			fvWekaAttributes.addElement(caption_topic36);
			fvWekaAttributes.addElement(caption_topic37);
			fvWekaAttributes.addElement(caption_topic38);
			fvWekaAttributes.addElement(caption_topic39);
			fvWekaAttributes.addElement(caption_topic40);
			fvWekaAttributes.addElement(caption_topic41);
			fvWekaAttributes.addElement(caption_topic42);
			fvWekaAttributes.addElement(caption_topic43);
			fvWekaAttributes.addElement(caption_topic44);
			fvWekaAttributes.addElement(caption_topic45);
			fvWekaAttributes.addElement(caption_topic46);
			fvWekaAttributes.addElement(caption_topic47);
			fvWekaAttributes.addElement(caption_topic48);
			fvWekaAttributes.addElement(caption_topic49);
			fvWekaAttributes.addElement(caption_topic50);
			
			fvWekaAttributes.addElement(sentence_topic1);
			fvWekaAttributes.addElement(sentence_topic2);
			fvWekaAttributes.addElement(sentence_topic3);
			fvWekaAttributes.addElement(sentence_topic4);
			fvWekaAttributes.addElement(sentence_topic5);
			fvWekaAttributes.addElement(sentence_topic6);
			fvWekaAttributes.addElement(sentence_topic7);
			fvWekaAttributes.addElement(sentence_topic8);
			fvWekaAttributes.addElement(sentence_topic9);
			fvWekaAttributes.addElement(sentence_topic10);
			fvWekaAttributes.addElement(sentence_topic11);
			fvWekaAttributes.addElement(sentence_topic12);
			fvWekaAttributes.addElement(sentence_topic13);
			fvWekaAttributes.addElement(sentence_topic14);
			fvWekaAttributes.addElement(sentence_topic15);
			fvWekaAttributes.addElement(sentence_topic16);
			fvWekaAttributes.addElement(sentence_topic17);
			fvWekaAttributes.addElement(sentence_topic18);
			fvWekaAttributes.addElement(sentence_topic19);
			fvWekaAttributes.addElement(sentence_topic20);
			fvWekaAttributes.addElement(sentence_topic21);
			fvWekaAttributes.addElement(sentence_topic22);
			fvWekaAttributes.addElement(sentence_topic23);
			fvWekaAttributes.addElement(sentence_topic24);
			fvWekaAttributes.addElement(sentence_topic25);
			fvWekaAttributes.addElement(sentence_topic26);
			fvWekaAttributes.addElement(sentence_topic27);
			fvWekaAttributes.addElement(sentence_topic28);
			fvWekaAttributes.addElement(sentence_topic29);
			fvWekaAttributes.addElement(sentence_topic30);
			fvWekaAttributes.addElement(sentence_topic31);
			fvWekaAttributes.addElement(sentence_topic32);
			fvWekaAttributes.addElement(sentence_topic33);
			fvWekaAttributes.addElement(sentence_topic34);
			fvWekaAttributes.addElement(sentence_topic35);
			fvWekaAttributes.addElement(sentence_topic36);
			fvWekaAttributes.addElement(sentence_topic37);
			fvWekaAttributes.addElement(sentence_topic38);
			fvWekaAttributes.addElement(sentence_topic39);
			fvWekaAttributes.addElement(sentence_topic40);
			fvWekaAttributes.addElement(sentence_topic41);
			fvWekaAttributes.addElement(sentence_topic42);
			fvWekaAttributes.addElement(sentence_topic43);
			fvWekaAttributes.addElement(sentence_topic44);
			fvWekaAttributes.addElement(sentence_topic45);
			fvWekaAttributes.addElement(sentence_topic46);
			fvWekaAttributes.addElement(sentence_topic47);
			fvWekaAttributes.addElement(sentence_topic48);
			fvWekaAttributes.addElement(sentence_topic49);
			fvWekaAttributes.addElement(sentence_topic50);
			
			fvWekaAttributes.addElement(ClassAttribute);
			Instances Instances = new Instances("Rel", fvWekaAttributes, 0);

			iExample = new DenseInstance(128);
			Attribute attribute = (Attribute) fvWekaAttributes.elementAt(0);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), PMC);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), tableid);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(2), NoRows);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(3),
					NoColumn);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(4),
					TotalCellNo);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(5),
					CellEmpty);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(6),
					CellNumeric);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(7),
					CellSemiNumeric);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(8),
					CellText);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(9),
					ContainsAdverse);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(10),
					containsAge);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(11),
					containsBaseline);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(12),
					containsCharacteristicCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(13),
					containsDemographic);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(14),
					ContainsExclusion);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(15),
					ContainsInclusion);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(16),
					containsN);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(17),
					containsPatientsCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(18),
					containsPatientsCell);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(19),
					ContainsSideEffect);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(20),
					ContainsSignOrSymptomAnnotation);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(21),
					containsEglibility);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(22),
					containsToxicity);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(23),
					containsInclusionExclusionCell);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(24),
					containsHaematologic);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(25),
					containsTrialCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(26),
					containsCriteria);
			
			WholeHeader = WholeHeader.replace("'", "");
			WholeStub = WholeStub.replace("'", "");
			WholeSuperRow = WholeSuperRow.replace("'", "");
			WholeData = WholeData.replace("'", "");
			Caption = Caption.replace("'", "");
			
			System.out.println("WholeHeader:"+WholeHeader);
			System.out.println("WholeStub:"+WholeStub);
			System.out.println("WholeSuperRow:"+WholeSuperRow);
			System.out.println("WholeData:"+WholeData);
			System.out.println("Caption:"+Caption);
			
			
//			iExample.setValue((Attribute) fvWekaAttributes.elementAt(25),
//					WholeHeader);
//			iExample.setValue((Attribute) fvWekaAttributes.elementAt(26),
//					WholeStub);
//			iExample.setValue((Attribute) fvWekaAttributes.elementAt(27),
//					WholeSuperRow);
//			iExample.setValue((Attribute) fvWekaAttributes.elementAt(28),
//					WholeData);
//			iExample.setValue((Attribute) fvWekaAttributes.elementAt(25),
//					Caption);
			for(int i = 27;i<77;i++)
			{
				iExample.setValue((Attribute) fvWekaAttributes.elementAt(i),
						caption_topics[i-27]);
			}
			for(int i = 77;i<127;i++)
			{
				iExample.setValue((Attribute) fvWekaAttributes.elementAt(i),
						ref_sentence_topic[i-77]);
			}
			Instances.add(iExample);
			Instances.setClassIndex(127);


		
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return iExample;
	}

}
