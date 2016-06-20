package classifiers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import weka.classifiers.misc.InputMappedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import Main.KeyValue;

public class SpecPragmatic {
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

	public void processTables(String pathOfModel) {
		DataBase();
		ClassifierPath = pathOfModel;
		try {
			classifier.setModelPath(ClassifierPath);
			classifier.setTrim(true);
			String SQL = "SELECT * from arttable where HasXML='yes'";
			Statement st = conn.createStatement();

			rs = st.executeQuery(SQL);
			while (rs.next()) {
				String classification = classifyTable(rs.getInt(1));
				System.out.println(rs.getInt(1) + "  " + classification);
				SQL = "Update arttable set SpecPragmatic='" + classification
						+ "' where idTable=" + rs.getInt(1);
				Statement st2 = conn.createStatement();

				int res = st2.executeUpdate(SQL);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String classifyTable(int tableid) {
		String classification = "NotInteresting";
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

			String SQL = "select * from arttable where idTable=" + tableid;
			Statement st = conn.createStatement();

			ResultSet rs = st.executeQuery(SQL);

			// iterate through the java resultset
			while (rs.next()) {
				Caption = rs.getString(3);
			}

			SQL = "select * from cell where Table_idTable=" + tableid;
			Statement st2 = conn.createStatement();

			ResultSet rs2 = st2.executeQuery(SQL);

			// iterate through the java resultset
			while (rs2.next()) {
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

			SQL = "select * from annotation inner join cell on cell.idCell=annotation.Cell_idCell where  annotation.AgentName='MetaMap' and AnnotationDescription LIKE '%Symptom%' and Table_idTable="
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
			FastVector fvClassVal = new FastVector(4);
			// AdverseEvent,InclusionExclusion,DontCare,BaselineCharacteristic
			fvClassVal.addElement("AdverseEvent");
			fvClassVal.addElement("InclusionExclusion");
			fvClassVal.addElement("Other");
			fvClassVal.addElement("BaselineCharacteristic");
			Attribute ClassAttribute = new Attribute("clas", fvClassVal);
			// Declare the feature vector
			FastVector fvWekaAttributes = new FastVector(26);

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
			fvWekaAttributes.addElement(ClassAttribute);
			Instances Instances = new Instances("Rel", fvWekaAttributes, 0);

			Instance iExample = new DenseInstance(26);
			Attribute attribute = (Attribute) fvWekaAttributes.elementAt(0);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), NoRows);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(1),
					NoColumn);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(2),
					TotalCellNo);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(3),
					CellEmpty);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(4),
					CellNumeric);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(5),
					CellSemiNumeric);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(6),
					CellText);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(7),
					ContainsAdverse);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(8),
					containsAge);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(9),
					containsBaseline);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(10),
					containsCharacteristicCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(11),
					containsDemographic);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(12),
					ContainsExclusion);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(13),
					ContainsInclusion);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(14),
					containsN);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(15),
					containsPatientsCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(16),
					containsPatientsCell);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(17),
					ContainsSideEffect);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(18),
					ContainsSignOrSymptomAnnotation);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(19),
					containsEglibility);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(20),
					containsToxicity);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(21),
					containsInclusionExclusionCell);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(22),
					containsHaematologic);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(23),
					containsTrialCap);
			iExample.setValue((Attribute) fvWekaAttributes.elementAt(24),
					containsCriteria);
			Instances.add(iExample);
			Instances.setClassIndex(25);
			Instance inst = Instances.firstInstance();

			double result = classifier.classifyInstance(Instances
					.firstInstance());
			Instances.firstInstance().setClassValue(result);
			prediction = Instances.firstInstance().classAttribute()
					.value((int) result);
			if (prediction.equalsIgnoreCase("other")) {
				prediction = "NotInteresting";
			}
			classification = prediction;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return classification;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpecPragmatic sp = new SpecPragmatic();
		sp.processTables("Models/SMOSpecPrag2.model");

	}

}
