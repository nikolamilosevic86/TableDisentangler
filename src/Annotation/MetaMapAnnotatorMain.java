package Annotation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import DataBase.DataBaseAnnotationSaver;
import Main.MarvinSemAnnotator;
import Main.Word;
import Main.WordMeaningOutputElement;


public class MetaMapAnnotatorMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataBaseAnnotationSaver dbas = new DataBaseAnnotationSaver();
		try {
			int CellId = Integer.parseInt(args[0]);
			Statement stmt = dbas.conn.createStatement();
			String insertTableSQL = "SELECT * FROM Cell where idCell > "+CellId; // change this to be idCell that was available before running the last TableAnnotator load. Currently 489245.
			ResultSet rs = stmt.executeQuery(insertTableSQL);
			MarvinSemAnnotator marvin = new MarvinSemAnnotator();
			while (rs.next()) {
				int idCell = rs.getInt("idCell");
				String CellID = rs.getString("CellID");
				String CellType = rs.getString("CellType");
				int Table_idTable  = rs.getInt("Table_idTable");
				int RowN = rs.getInt("RowN");
				int ColumnN = rs.getInt("ColumnN");
				String Content = rs.getString("Content");
				System.out.println("Processing cell record - \n\tidCell: " + idCell + "\n\tCellID: " + CellID + "\n\tCellType: " + CellType + "\n\tTable_idTable: " + Table_idTable + "\n\tRowN: " + RowN + "\n\tColumnN: " + ColumnN);
				System.out.println("Cell content:" + Content);
				if(Content==null)
				{
					Content = "";
				}
				int mathTypeIndex = Content.indexOf("MathType@");
				if(mathTypeIndex>0)
				{
					Content = Content.substring(0, mathTypeIndex);
				}
				LinkedList<Word> words = null;
				if(Content!=null){
					Content = Content.trim();
					words = marvin.annotate(Content);
					if(words == null){
					    System.out.println("ERROR: Marvin returned NULL when annotating content (see above) - this cell will be skipped for now ");
					    System.out.println("ERROR: letting process rest and then initiating a new marvin metamap annotation connection....");
					    try {
						Thread.sleep(1000);                 //1000 milliseconds is one second.
					    } catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					    }
					    marvin = new MarvinSemAnnotator();
					    continue;
					}
					
					for(Word w:words)
					{
						
						for(WordMeaningOutputElement wm: w.wordmeanings)
						{
							System.out.println(wm.AgentName);
							Statement stmt8 = dbas.conn.createStatement();
		        	  		String insertTableSQL8 = "INSERT INTO Annotation (Content,Start,End,AnnotationID,AgentType,AgentName,AnnotationURL,EnvironmentDescription,Cell_idCell,AnnotationDescription,AnnotationSchemaVersion,DateOfAction, Location) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		        	  		PreparedStatement preparedStatement8 = dbas.conn.prepareStatement(insertTableSQL8,Statement.RETURN_GENERATED_KEYS);
		        	  		preparedStatement8.setString(1,wm.appearingWord);
		        	  		preparedStatement8.setInt(2,wm.startAt);
		        	  		preparedStatement8.setInt(3,wm.endAt);
		        	  		preparedStatement8.setString(4,wm.id);
		        	  		preparedStatement8.setString(5,"Software");
		        	  		preparedStatement8.setString(6,wm.AgentName);
		        	  		preparedStatement8.setString(7,wm.URL);
		        	  		preparedStatement8.setString(8,wm.EnvironmentDesc); 
		        	  		preparedStatement8.setInt(9,idCell);
		        	  		preparedStatement8.setString(10,wm.Description);
		        	  		preparedStatement8.setString(11,wm.AgentVersion);// Should be version
		        	  		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        	  		Date date = new Date();
		        	  		preparedStatement8.setString(12,dateFormat.format(date));
		        	  		preparedStatement8.setString(13,wm.Location); // Should be location
		        	  		// execute insert SQL stetement
		        	  		preparedStatement8.executeUpdate();
						}
					}
				} 
			  }
			
			dbas.CloseDBConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
