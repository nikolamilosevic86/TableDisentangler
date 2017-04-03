package classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.NullStemmer;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import Main.KeyValue;

public class DrugHeaderClassifier {
	private String ClassifierPath = "";
	// private Classifier classifier;

	InputMappedClassifier classifier = new InputMappedClassifier();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DrugHeaderClassifier sp = new DrugHeaderClassifier();
		sp.processTables("Models/DDIHeaderClassifierModel.model");

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
	
	public void processTables(String pathOfModel) {
		DataBase();
		ClassifierPath = pathOfModel;
		int execCount = 0;
		try {
			classifier.setModelPath(ClassifierPath);
			classifier.setTrim(true);
			String SQL = "SELECT * from cell inner join arttable on Table_idTable=idTable where section='34073-7'";
			Statement st = conn.createStatement();

			rs = st.executeQuery(SQL);
			while (rs.next()) {
				String classification = classifyCell(rs.getString(10));
				System.out.println(rs.getString(10) + "  " + classification);
				if(classification.equals("yes"))
				{
					SQL = "INSERT into cellroles (CellRole_idCellRole,Cell_idCell) VALUES (1,"+rs.getInt(1)+")";
					Statement st2 = conn.createStatement();

					int res = st2.executeUpdate(SQL);
					execCount ++;
					if(execCount>10000){
						conn.close();
						DataBase();
						execCount = 0;	
					}
				}
				
				
				

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	
	
	
	
	
	public String classifyCell(String content) {
		String classification = "NotInteresting";
		try {
			
			String Content = content;

				
			String prediction = "";
			Instances ins = null;
			Attribute CellContent = new Attribute("Content", (FastVector) null);

			FastVector fvClassVal = new FastVector(2);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("yes");
			fvClassVal.addElement("no");
			Attribute ClassAttribute = new Attribute("classa", fvClassVal);
			// Declare the feature vector
			FastVector fvWekaAttributes = new FastVector(2);

			fvWekaAttributes.addElement(CellContent);
			fvWekaAttributes.addElement(ClassAttribute);
			Instances Instances = new Instances("Rel", fvWekaAttributes, 0);

			Instance iExample = new DenseInstance(2);
			Attribute attribute = (Attribute) fvWekaAttributes.elementAt(0);
			//iExample.attribute(0).addStringValue(Content);
			//iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), Content);
			double[] vals = new double[Instances.numAttributes()]; 
			vals[0] = Instances.attribute(0).addStringValue(Content);
			//Instances.add(iExample);
			Instances.add(new DenseInstance(1.0, vals));
			Instances.setClassIndex(1);
			
			Instance inst = Instances.firstInstance();
			
			
			  StringToWordVector filter = new StringToWordVector(); 
			  filter.setAttributeIndices("first-last");
			  AlphabeticTokenizer wt = new AlphabeticTokenizer();
			  filter.setTokenizer(wt);
			  
			  filter.setMinTermFreq(1);
			  
			  filter.setIDFTransform(false);
			  filter.setTFTransform(false);
			  filter.setLowerCaseTokens(false);
			  filter.setOutputWordCounts(false);
			  NullStemmer stemmer = new NullStemmer();
			  //stemmer.setStemmer("English");
			  filter.setStemmer(stemmer);
			  Instances newData;
			  try {
				filter.setInputFormat(Instances);
				filter.input(Instances.instance(0));
				filter.batchFinished();
				ins= filter.getOutputFormat();
				ins= Filter.useFilter(Instances,filter);
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			

			double result = classifier.classifyInstance(ins
					.firstInstance());
			ins.firstInstance().setClassValue(result);
			prediction = Instances.firstInstance().classAttribute()
					.value((int) result);
			
			classification = prediction;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return classification;
	}

	
	
	

}
