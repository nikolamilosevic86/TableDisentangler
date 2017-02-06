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
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class GetContentFeaturesARFF {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;
	public static void main(String[] args) {
		GetContentFeaturesARFF sp = new GetContentFeaturesARFF();
		int[] table_array = {7,36,115,394,428,440,464,765,766,771,799,944,1062,1091,1098,1102,1111,1112,1127,1170,1181,1209,1217,1243,1274,1277,
                1279,1280,1285,1296,1297,1309,1390,
                1423, 1424,1425,1465,1466,1486,1515,1517,1544,1551,1585,1618,1624,1635,1664,1676,1764,1792,1800,1838,1871,1875,1916,1930,1947,1951,1958,
1959,1962,1963,1978,2013,2027,2045,2066,2161,2171,2249,2279,2286,2295,2316,2338,2374,2406,2532,2662,2699,2750,2778,2782,2885,2888,2923,
3155,3156,3162,3176,3251,3333,3338,3369,3371,3378,3379,3391,3399,3400,3419,3420,3478,3479,3484,3514,3543,3558,3592,3661,3736,3762,3766,
3811,3829,3848,3849,3888,3931,3979,4004,4019,4025,4028,4055,4068,4093,4111,4136,4163,4202,4205,4206,4209,4223,4227,4312,4350,4351,4359,
4379,4380,4385,4406,4442,4485,4496,4532,4552,4559,4588,4589,4594,4595,4596,4610,4615,4617,4625,4629,4650,4664,4672,4673,4674,4675,4682,
4726,4781,4798,4850,4859,4882,4883,4923,4947,4963,4973,4975,4987,4988,4999,5000,5063,5092,5093,5106,5186,5226,5240,5243,5284,5305,5322,8049,
8071,8107,8117,8139,8148, 293, 653, 1048,1658,1862,2466,2467,3272,3985,3986,4127,4868,4901,4951,5292,5602,6186,6857,7052,7103,7104,7105,7506,7507,7508,7509,
7961,8163,8446,9505,11072,12375,12465,12466,12519,12559};
		sp.ProcessTables(table_array);
		// TODO Auto-generated method stub
	}
	
	public void ProcessTables(int[] table_array)
	{
		
		DataBase();
		int execCount = 0;
		try {
			processTable(table_array);
			
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
	
	public void processTable(int[] table_array) {
		Instance iExample = null;
		try {
			int tableid;
			Instances data;
			FastVector atts;
		    FastVector      attVals;
		    double[]        vals;
		    int             i;
			String Caption = "";
			String WholeHeader = "";
			String WholeStub = "";
			String WholeSuperRow="";
			String WholeData = "";
			String RefereingSentences = "";
			int PMC = 0;
			atts = new FastVector();
			atts.addElement(new Attribute("PMC"));
			atts.addElement(new Attribute("TableID"));
			atts.addElement(new Attribute("Caption", (FastVector) null));
			atts.addElement(new Attribute("WholeHeader", (FastVector) null));
			atts.addElement(new Attribute("WholeStub", (FastVector) null));
			atts.addElement(new Attribute("SuperRow", (FastVector) null));
			atts.addElement(new Attribute("Data", (FastVector) null));
			atts.addElement(new Attribute("RefSentence", (FastVector) null));
			
			FastVector fvClassVal = new FastVector(4);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("AdverseEvent");
			fvClassVal.addElement("InclusionExclusion");
			fvClassVal.addElement("Other");
			fvClassVal.addElement("BaselineCharacteristic");
			Attribute ClassAttribute = new Attribute("clas", fvClassVal);
			atts.addElement(ClassAttribute);
			
			data = new Instances("MyRelation", atts, 0);
			String SQL = "SELECT * from ArtTable where HasXML='yes' and idTable in "+Arrays.toString(table_array);
			SQL = SQL.replace("[", "(").replace("]", ")");
			Statement st = conn.createStatement();
			rs = st.executeQuery(SQL);
			while (rs.next()) {
				tableid = rs.getInt(1);

			String SQL2 = "select * from ArtTable inner join article on article.idArticle=ArtTable.Article_idArticle where idTable=" + tableid;
			Statement st2 = conn.createStatement();

			ResultSet rs1 = st2.executeQuery(SQL2);

			// iterate through the java resultset
			while (rs1.next()) {
				Caption = rs1.getString(3);
				PMC = rs1.getInt(12);
			}

			SQL = "select * from Cell where Table_idTable=" + tableid;
			Statement st3 = conn.createStatement();

			ResultSet rs2 = st3.executeQuery(SQL);

			// iterate through the java resultset
			while (rs2.next()) {
				int cell_id = rs2.getInt(1);
				String SQL3 = "select * from cellroles inner join cell on cell.idCell=cellroles.Cell_idCell where Cell_idCell=" + cell_id;
				Statement st4 = conn.createStatement();

				ResultSet rs4 = st4.executeQuery(SQL3);
				while (rs4.next()) {
					int CellRole = rs4.getInt(2);
					if(CellRole==1)
					{
						WholeHeader+="; "+rs4.getString(13).toLowerCase();
					}
					if(CellRole==2)
					{
						WholeStub+="; "+rs4.getString(13).toLowerCase();
					}
					if(CellRole==3)
					{
						WholeData+="; "+rs4.getString(13).toLowerCase();
					}
					if(CellRole==4)
					{
						WholeSuperRow+="; "+rs4.getString(13).toLowerCase();
					}
				}
			}
			
			SQL = "select * from tablesentences where Table_idTable=" + tableid;
			Statement st4 = conn.createStatement();

			ResultSet rs5 = st4.executeQuery(SQL);
			while(rs5.next())
			{
				RefereingSentences+="; "+rs5.getString(2);
			}
			
			vals = new double[data.numAttributes()];
			vals[0] = PMC;
			vals[1] = tableid;
			vals[2] = data.attribute(2).addStringValue(Caption);
			vals[3] = data.attribute(3).addStringValue(WholeHeader);
			vals[4] = data.attribute(4).addStringValue(WholeStub);
			vals[5] = data.attribute(5).addStringValue(WholeSuperRow);
			vals[6] = data.attribute(6).addStringValue(WholeData);
			vals[7] = data.attribute(7).addStringValue(RefereingSentences);
			iExample = new DenseInstance(1.0, vals);
			data.add(iExample);
			}
			ArffSaver saver = new ArffSaver();
			 saver.setInstances(data);
			 saver.setFile(new File("old_data_content.arff"));
			 //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
			 saver.writeBatch();
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
