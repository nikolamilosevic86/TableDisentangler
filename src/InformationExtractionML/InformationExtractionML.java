package InformationExtractionML;

import java.awt.BufferCapabilities.FlipContents;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hp.hpl.jena.util.Tokenizer;

import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.StopwordsHandler;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import Main.KeyValue;

public class InformationExtractionML {

	private String ClassifierPath = "";
	// private Classifier classifier;

	InputMappedClassifier classifier = new InputMappedClassifier();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

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
			String connectionUrl = "jdbc:mysql://" + host + ":" + database_port
					+ "/" + database_name;
			String connectionUser = database_username;
			String connectionPassword = database_password;
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

	public void processCells(String pathOfModel) {
		DataBase();
		ClassifierPath = pathOfModel;
		try {
			classifier.setModelPath(ClassifierPath);
			classifier.setTrim(true);
			classifier.setIgnoreCaseForNames(true);
			//classifier.setSuppressMappingReport(true);
			//RandomForest rf  = new RandomForest();
			//rf.setMaxDepth(0);
			//rf.setNumExecutionSlots(1);
			//rf.setNumFeatures(0);
			//rf.setNumTrees(100);	
			//classifier.setClassifier(rf);
			String SQL = "select * from cell inner join arttable on cell.Table_idTable=arttable.idTable where arttable.SpecPragmatic='BaselineCharacteristic'";
			Statement st = conn.createStatement();

			rs = st.executeQuery(SQL);
			while (rs.next()) {
				String classification = classifyCell(rs.getInt(1));
				String text = rs.getInt(1)+"  "+rs.getInt(4)+"  "+rs.getString(10)+"   "+ rs.getString(11)+rs.getString(12)+"   "+rs.getString(13)+"    class:" +classification+"\r\n";
				Files.write(Paths.get("gender.txt"), text.getBytes(), StandardOpenOption.APPEND);
				// SQL = "Update arttable set SpecPragmatic='" + classification
				// + "' where idTable=" + rs.getInt(1);
				// Statement st2 = conn.createStatement();

				// int res = st2.executeUpdate(SQL);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String classifyCell(int cellid) {
		String classification = "No";
		try {
			String CellContent = "";
			String StubContent = "";
			String HeaderContent = "";
			String SuperRowContent = "";
			int rowN = 0;
			int columnN = 0;
			int function = 0;

			String SQL = "select * from cell where idCell=" + cellid;
			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(SQL);

			// iterate through the java resultset
			while (rs.next()) {
				rowN = rs.getInt(5);
				columnN = rs.getInt(6);
				CellContent = rs.getString(10);
				if(CellContent!=null)
					CellContent = CellContent.replaceAll("[0-9]", "x");
				else
					CellContent = "";
				HeaderContent = rs.getString(11);
				if(HeaderContent!=null)
					HeaderContent = HeaderContent.replaceAll("[0-9]", "x");
				else
					HeaderContent = "";
				StubContent = rs.getString(12);
				if(StubContent!=null)
					StubContent = StubContent.replaceAll("[0-9]", "x");
				else
					StubContent = "";
				SuperRowContent = rs.getString(13);
				if(SuperRowContent!=null)
					SuperRowContent = SuperRowContent.replaceAll("[0-9]", "x");
				else
					SuperRowContent = "";
			}
			SQL = "select * from cellroles where Cell_idCell=" + cellid;
			Statement st1 = conn.createStatement();

			ResultSet rs2 = st1.executeQuery(SQL);
			String s = "";
			while (rs2.next()) {
				s += rs2.getInt(2);
			}
			try{
			function = Integer.parseInt(s);
			}catch(Exception ex){
				function = 0;
			}

			// iterate through the java resultset

			String prediction = "";
			Instances ins = null;
			Attribute CellContentAttribute = new  Attribute("CellContent",(FastVector) null);
			Attribute HeaderAttribute = new Attribute("Header",(FastVector) null);
			Attribute StubAttribute = new Attribute("Stub",(FastVector) null);
			Attribute SuperRowAttribute = new Attribute("SuperRow",(FastVector) null);
			Attribute rowNAttribute = new Attribute("rowN");
			Attribute columnNAttribute = new Attribute("columnN");
			Attribute FunctionAttribute = new Attribute("function");
			FastVector fvClassVal = new FastVector(2);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("yes");
			fvClassVal.addElement("no");
			Attribute ClassAttribute = new Attribute("hasGender", fvClassVal);
			// Declare the feature vector
			FastVector fvWekaAttributes = new FastVector(8);

			fvWekaAttributes.addElement(CellContentAttribute);
			fvWekaAttributes.addElement(HeaderAttribute);
			fvWekaAttributes.addElement(StubAttribute);
			fvWekaAttributes.addElement(SuperRowAttribute);
			fvWekaAttributes.addElement(rowNAttribute);
			fvWekaAttributes.addElement(columnNAttribute);
			fvWekaAttributes.addElement(FunctionAttribute);

			fvWekaAttributes.addElement(ClassAttribute);
			Instances Instances = new Instances("Rel", fvWekaAttributes, 0);

			Instance iExample = new DenseInstance(8);
			//Attribute attribute = (Attribute) fvWekaAttributes.elementAt(0);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(0),
					CellContent);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(1),
					HeaderContent);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(2),
					StubContent);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(3),
					SuperRowContent);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(4), rowN);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(5),
					columnN);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(6),
					function);
			Instances.add(iExample);
			Instances.setClassIndex(7);
			//Instance inst = Instances.firstInstance();

			StringToWordVector filter = new StringToWordVector();
			filter.setAttributeIndices("first-last");
			filter.setMinTermFreq(1);
			filter.setAttributeNamePrefix("feat_");
			filter.setWordsToKeep(2000);
			filter.setStopwordsHandler(null);
			filter.setLowerCaseTokens(true);
			SnowballStemmer stemmer = new SnowballStemmer();
			filter.setTokenizer(new WordTokenizer());
			// stemmer.setStemmer("English");
			filter.setStemmer(stemmer);
			
			try {
				filter.setInputFormat(Instances);
				filter.input(Instances.instance(0));
				filter.batchFinished();
				ins = Filter.useFilter(Instances, filter);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			double result = classifier.classifyInstance(ins
					.firstInstance());
			Instances.firstInstance().setClassValue(result);
			prediction = Instances.firstInstance().classAttribute()
					.value((int) result);
			classification = prediction;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return classification;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InformationExtractionML sp = new InformationExtractionML();
		sp.processCells("Models/NaiveBayesGenderDetection.model");

	}
}
