package classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import Main.KeyValue;
import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class SpecPragmaticCreateDataset_posteriori_10 {

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;

	public static void main(String[] args) {
		SpecPragmaticCreateDataset_posteriori_10 sp = new SpecPragmaticCreateDataset_posteriori_10();
		int[] table_array = {8929,5409,3286,3623,3020,3357,8266,1887,2914,5230,9895,9271,11081,5396,12031,8454,3472,2588,2186,6461,12513,4319,11237,169,262,2544,6936,7798,4505,4787,585,10154,4090,7574,1282,10317,11573,4838,9745,795,5905,8141,6466,1496,8121,10681,12193,5049,4429,7906,12621,7455,1463,5753,8516,604,1645,6212,7154,4271,5652,3777,4949,11019,6066,4120,821,5287,6403,1940,3421,845,1074,3236,11668,12249,7641,3219,4176,6310,7979,10274,1939,151,8912,10566,12663,2904,2747,5175,10258,9669,11743,3469,9870,7296,6597,1941,6677,538,9700,3505,7815,2473,6435,3717,11334,4220,11252,11300,1331,4151,5556,4875,7155,3041,4234,7712,12040,6712,8259,5729,481,8377,4092,8947,9528,2538,1328,1773,3619,1034,11430,2922,8663,10376,2647,4449,3704,6338,7500,5417,879,3493,11560,8416,8513,4243,9400,4037,4919,11843,7535,5707,2015,1257,4075,5919,647,6539,6886,7635,6732,8662,2207,3573,7060,8799,8198,1018,498,3962,411,4018,8485,801,7224,3168,5354,8369,4493,10290,3614,6877,7091,132,2090,10429,7348,1859,732,6385,424,220,11978,1136,546,5765,2883,8543,8666,7947,16,4519,8076,11375,12771,3101,5080,1808,1493,2050,11525,5318,3821};
		sp.ProcessTables(table_array);
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

		FastVector fvClassVal = new FastVector(4);
		// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
		fvClassVal.addElement("AdverseEvent");
		fvClassVal.addElement("InclusionExclusion");
		fvClassVal.addElement("Other");
		fvClassVal.addElement("BaselineCharacteristic");
		Attribute ClassAttribute = new Attribute("clas", fvClassVal);
		// Declare the feature vector
		FastVector fvWekaAttributes = new FastVector(48);
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

		fvWekaAttributes.addElement(ClassAttribute);
		Instances Instances = new Instances("Rel", fvWekaAttributes, 0);
		Instances.setClassIndex(47);
		return Instances;
	}
	
	public void ProcessTables(int[] table_array)
	{
		
		DataBase();
		int execCount = 0;
		try {
			String SQL = "SELECT * from ArtTable where HasXML='yes' and idTable in "+Arrays.toString(table_array);
			SQL = SQL.replace("[", "(").replace("]", ")");
			Statement st = conn.createStatement();
			Instances instances = CreateInstances();
			FastVector fvWekaAttributes = new FastVector(48);
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
			 saver.setFile(new File("spptest10.arff"));
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
			int[] caption_topics = new  int[10];
			int[] ref_sentence_topic = new int[10];

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
				String SQL4 = "select * from caption_topic10 where SentenceID=" + tableid;
				Statement st4 = conn.createStatement();

				ResultSet rs4 = st4.executeQuery(SQL4);
				while (rs4.next()) {
					String num = rs4.getString(3);
					caption_topics[Integer.parseInt(num)]=1;
				}
				
				String SQL5 = "SELECT * FROM table_db.tablesentences inner join sentence_topic10 on idTableSentences = SentenceID where Table_idTable=" + tableid;
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
			
			FastVector fvClassVal = new FastVector(4);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("AdverseEvent");
			fvClassVal.addElement("InclusionExclusion");
			fvClassVal.addElement("Other");
			fvClassVal.addElement("BaselineCharacteristic");
			Attribute ClassAttribute = new Attribute("clas", fvClassVal);
			// Declare the feature vector
			FastVector fvWekaAttributes = new FastVector(48);
			
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
			
			
			fvWekaAttributes.addElement(ClassAttribute);
			Instances Instances = new Instances("Rel", fvWekaAttributes, 0);

			iExample = new DenseInstance(48);
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
			for(int i = 27;i<37;i++)
			{
				iExample.setValue((Attribute) fvWekaAttributes.elementAt(i),
						caption_topics[i-27]);
			}
			for(int i = 37;i<47;i++)
			{
				iExample.setValue((Attribute) fvWekaAttributes.elementAt(i),
						ref_sentence_topic[i-37]);
			}
			Instances.add(iExample);
			Instances.setClassIndex(47);


		
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return iExample;
	}

}
