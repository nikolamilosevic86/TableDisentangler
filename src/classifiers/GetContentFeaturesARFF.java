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
		//Adverse event tables
//		int[] table_array = {3211,6974,11226,11675,2986,9547,7430,3937,3922,4045,8049,2474,7199,1600,2517,9821,257,1875,9012,5464,5433,
//				1763,8107,8269,1112,1425,12137,4672,12283,2386,10442,10527,12256,7550,7722,4615,2001,3172,7392,1749,6417,7137,5724,12227,
//				11043,2667,9504,3699,2156,2891,2706,1869,11621,7334,2223,4951,6263,3333,2440,12534,7213,2727,1947,6612,6464,3182,4664,8857,
//				5204,3269,10282,7509,771,4994,2267,3419,8407,3979,6135,5617,4859,4019,6648,7431,11072,2894,11641,8893,12311,7894,3317,7105,
//				1285,1127,3888,12086,3105,4202,2013,2808,7122,10222,6312,7824,1217,742,4205,7014,8839,7714,6439,1111,9034,12090,943,1638,
//				1396,3162,4350,3309,2042,2123,2467,1318,9607,11684,2309,8783,7275,11212,8840,4111,7295,2987,2654,9270,4055,4485,9276,12185,
//				11671,473,548,4864,464,7377,11120,1635,7018,1466,3340,5471,4947,11182,8445,1867,10780,7233,999,11141,1510,7172,1641,5774,
//				1772,1624,11591,6400,4973,3021,2782,3661,765,1794,6633,5459,1836,5620,8983,8263,10181,7706,6148,3750,2347,3214,3180,1495,
//				4385,2218,1832,3251};
		//Baseline characteristics 
//		int[] table_array = {8196,1269,9351,5823,10407,3682,5180,2266,4965,9816,12648,12716,2780,11569,9875,1560,2972,11876,6292,5300,11325,
//				12165,4115,6248,8074,10556,6914,1526,8586,4604,4609,4108,740,8910,8734,9040,5140,12323,4570,5351,8798,7261,9580,12240,7217,
//				97,9665,1212,12421,7721,938,1415,8242,4867,10927,2561,3620,9997,9193,4613,10153,9225,3177,632,11451,5067,9685,9945,1308,
//				6662,12652,8603,11158,5499,5555,91,7359,11427,355,5621,6973,9600,8133,6272,6262,5105,12678,2047,5291,1755,9053,1830,4958,
//				12131,9076,12088,3945,4839,6622,10136,8398,10812,5567,8285,1739,989,3609,4702,10326,11715,1326,7388,290,3511,6719,8686,9513,
//				2514,5687,2554,7499,9491,308,11485,10650,1161,11122,2278,10234,593,4135,9335,10009,10485,7366,9406,6017,12307,6856,2346,
//				7905,1238,807,5648,9161,4752,1995,10922,11440,447,4900,2378,6962,6094,4128,2185,8212,2702,6424,12464,9675,10770,6157,5195,
//				10299,12377,9327,4712,12726,8075,1110,1770,11056,3473,4196,12596,2954,6230,12146,11859,11667,1298,11044,10195,12585,2409,
//				6618,7315,2415,8956,3977,10973};
		//Inclusion Exclusion
//		int[] table_array = {5201,4944,8191,848,9210,12337,10603,11343,1095,622,7572,3291,1381,12604,7084,866,33,10199,11781,260,3758,8327,
//				12159,10962,12233,8324,508,8176,9507,1332,10519,8372,3882,9701,1121,4915,12214,7781,6559,4191,4027,11298,6313,1291,8989,
//				7386,3061,10901,12343,12365,4419,3850,7355,4876,7932,6122,6562,674,11809,11246,11269,15,875,5541,7546,9393,105,4408,8478,
//				5725,6332,9252,497,1717,11984,7238,8080,9708,6177,3883,12138,8073,8408,8895};
		//Other
		int[] table_array = {8929,5409,3286,3623,3020,3357,8266,1887,2914,5230,9895,9271,11081,5396,12031,8454,3472,2588,2186,6461,12513,
				4319,11237,169,262,2544,6936,7798,4505,4787,585,10154,4090,7574,1282,10317,11573,4838,9745,795,5905,8141,6466,1496,8121,
				10681,12193,5049,4429,7906,12621,7455,1463,5753,8516,604,1645,6212,7154,4271,5652,3777,4949,11019,6066,4120,821,5287,6403,
				1940,3421,845,1074,3236,11668,12249,7641,3219,4176,6310,7979,10274,1939,151,8912,10566,12663,2904,2747,5175,10258,9669,11743,
				3469,9870,7296,6597,1941,6677,538,9700,3505,7815,2473,6435,3717,11334,4220,11252,11300,1331,4151,5556,4875,7155,3041,4234,
				7712,12040,6712,8259,5729,481,8377,4092,8947,9528,2538,1328,1773,3619,1034,11430,2922,8663,10376,2647,4449,3704,6338,7500,
				5417,879,3493,11560,8416,8513,4243,9400,4037,4919,11843,7535,5707,2015,1257,4075,5919,647,6539,6886,7635,6732,8662,2207,3573,
				7060,8799,8198,1018,498,3962,411,4018,8485,801,7224,3168,5354,8369,4493,10290,3614,6877,7091,132,2090,10429,7348,1859,732,
				6385,424,220,11978,1136,546,5765,2883,8543,8666,7947,16,4519,8076,11375,12771,3101,5080,1808,1493,2050,11525,5318,3821};
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
			
			data = new Instances("MyRelation", atts, 3);
			String SQL = "SELECT * from ArtTable where HasXML='yes' and idTable in "+Arrays.toString(table_array);
			SQL = SQL.replace("[", "(").replace("]", ")");
			Statement st = conn.createStatement();
			rs = st.executeQuery(SQL);
			while (rs.next()) {
				Caption = "";
				WholeHeader = "";
				WholeStub = "";
				WholeSuperRow="";
				WholeData = "";
				RefereingSentences = "";
				
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
			vals[8] = 2;
			iExample = new DenseInstance(1.0, vals);
			data.add(iExample);
			}
			ArffSaver saver = new ArffSaver();
			 saver.setInstances(data);
			 saver.setFile(new File("z_arffs/old_data_content.arff"));
			 //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
			 saver.writeBatch();
				
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
