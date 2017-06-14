package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import Main.KeyValue;
import weka.core.Instances;

public class TableDatabaseToHTML {
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;
	
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
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] table_list = {7,36,115,394,428,440,464,765,766,771,799,944,1062,1091,1098,1102,1111,1112,1127,1170,1181,1209,1217,1243,1274,1277,
                1279,1280,1285,1296,1297,1309,1390,
                1423, 1424,1425,1465,1466,1486,1515,1517,1544,1551,1585,1618,1624,1635,1664,1676,1764,1792,1800,1838,1871,1875,1916,1930,1947,1951,1958,
1959,1962,1963,1978,2013,2027,2045,2066,2161,2171,2249,2279,2286,2295,2316,2338,2374,2406,2532,2662,2699,2750,2778,2782,2885,2888,2923,
3155,3156,3162,3176,3251,3333,3338,3369,3371,3378,3379,3391,3399,3400,3419,3420,3478,3479,3484,3514,3543,3558,3592,3661,3736,3762,3766,
3811,3829,3848,3849,3888,3931,3979,4004,4019,4025,4028,4055,4068,4093,4111,4136,4163,4202,4205,4206,4209,4223,4227,4312,4350,4351,4359,
4379,4380,4385,4406,4442,4485,4496,4532,4552,4559,4588,4589,4594,4595,4596,4610,4615,4617,4625,4629,4650,4664,4672,4673,4674,4675,4682,
4726,4781,4798,4850,4859,4882,4883,4923,4947,4963,4973,4975,4987,4988,4999,5000,5063,5092,5093,5106,5186,5226,5240,5243,5284,5305,5322,8049,
8071,8107,8117,8139,8148, 293, 653, 1048,1658,1862,2466,2467,3272,3985,3986,4127,4868,4901,4951,5292,5602,6186,6857,7052,7103,7104,7105,7506,7507,7508,7509,
7961,8163,8446,9505,11072,12375,12465,12466,12519,12559};
		String ClassName = "AdverseEvents";
		try{
			TableDatabaseToHTML h = new TableDatabaseToHTML();
		h.DataBase();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("html");
		doc.appendChild(rootElement);

		// staff elements
		Element body = doc.createElement("body");
		rootElement.appendChild(body);
		String SQL = "SELECT * from arttable inner join originalarticle on originalarticle.Article_idArticle=arttable.Article_idArticle where idTable in "+Arrays.toString(table_list);
		SQL = SQL.replace("[", "(").replace("]", ")");
		Statement st = h.conn.createStatement();
		h.rs = st.executeQuery(SQL);
		while(h.rs.next())
		{
			String idTable = h.rs.getString(1);
			String TableOrder = h.rs.getString(2);
			String classA = h.rs.getString(10);
			String PMC = h.rs.getString(12);
			String xml =  h.rs.getString(16);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(xml));
		    Document parse =  builder.parse(is);
		    NodeList tablesxml = parse.getElementsByTagName("table-wrap");
		    for(int i = 0;i<tablesxml.getLength();i++)
		    {
		    	String table = Utilities.CreateXMLStringFromSubNode(tablesxml.item(i));
		    	if(table.contains("xlink"))
		    		continue;
		    	
		    	String label = "Table without label";
				List<Node> nl = getChildrenByTagName(tablesxml.item(i),"label");
				if(nl.size()>0)
				{
					label = Utilities.getString(nl.get(0));
				}
				if(label.equals(TableOrder))
				{
					Element MetaElement = doc.createElement("div");
					MetaElement.setTextContent("PMC:"+PMC+"   TableOrder:"+TableOrder+"   Tableid:"+idTable+"   TableClass: "+ClassName);
					Node TableNode = doc.importNode(tablesxml.item(i), true);
					MetaElement.appendChild(TableNode);
					body.appendChild(MetaElement);	
				}
		    }
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		//StreamResult result = new StreamResult(new File("table_data_file.html"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
		 String body_out = Utilities.CreateXMLStringFromSubNode(body);
		 BufferedWriter writer = new BufferedWriter( new FileWriter( "table_data_file.html"));
		 writer.write( body_out);
		//transformer.transform(source, result);

		System.out.println("File saved!");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
	
	public static List<Node> getChildrenByTagName(Node parent, String name) {
	    List<Node> nodeList = new ArrayList<Node>();
	    for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
	      if (child.getNodeType() == Node.ELEMENT_NODE && 
	          name.equals(child.getNodeName())) {
	        nodeList.add((Node) child);
	      }
	    }

	    return nodeList;
	  }

}
