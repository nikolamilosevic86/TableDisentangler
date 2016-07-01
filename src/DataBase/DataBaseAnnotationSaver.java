package DataBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import Main.KeyValue;
import tablInEx.Annotation;
import tablInEx.Article;
import tablInEx.Cell;
import tablInEx.Table;

public class DataBaseAnnotationSaver {
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	public DataBaseAnnotationSaver(){
		 try {
			 BufferedReader br = new BufferedReader(new FileReader("settings.cfg"));
			        // StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				String host="";
				String database_name= "";
				String database_username = "";
				String database_password = "";
				String database_port = "";
				while (line != null && line != "") {
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
				String connectionUrl = "jdbc:mysql://"+host+":"+database_port+"/"+database_name;
				String connectionUser = database_username;
				String connectionPassword = database_password;
				conn = DriverManager.getConnection(connectionUrl, connectionUser, connectionPassword);	
		 }catch(SQLException ex)
		 {
			 ex.printStackTrace();
			 System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	public void SaveArticleAnnotationToDB(Article art)
	{
		try {
			Statement stmt = conn.createStatement();
			String insertTableSQL = "INSERT INTO Article (PMCID,PMID,pissn,eissn,Title,Abstract,JournalName,JournalPublisherName,JournalPublisherLocation,Source,SpecId,PlainText) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, art.getPmc());
			preparedStatement.setString(2, art.getPmid());
			preparedStatement.setString(3, art.getPissn());
			preparedStatement.setString(4, art.getPissn());
			preparedStatement.setString(5, art.getTitle());
			preparedStatement.setString(6, art.getAbstract());
			preparedStatement.setString(7, art.getJournal_name());
			preparedStatement.setString(8, art.getPublisher_name());
			preparedStatement.setString(9, art.getPublisher_loc());
			preparedStatement.setString(10, art.getSource());
			preparedStatement.setString(11, art.getSpec_id());
			preparedStatement.setString(12, art.getPlain_text());
			// execute insert SQL stetement
			int articleId = preparedStatement.executeUpdate();
			ResultSet rs1 = preparedStatement.getGeneratedKeys();
	        if(rs1.next())
	        {
	        	articleId = rs1.getInt(1);
	        }
	        
            //-----------------------------------------------
            Statement stmt12 = conn.createStatement();
	  		String insertTableSQL12 = "INSERT INTO OriginalArticle (PMID,PMCID,pissn,eissn,xml,Article_idArticle) VALUES (?,?,?,?,?,?)";
	  		PreparedStatement preparedStatement12 = conn.prepareStatement(insertTableSQL12,Statement.RETURN_GENERATED_KEYS);
	  		preparedStatement12.setString(1,art.getPmid());
	  		preparedStatement12.setString(2, art.getPmid());
	  		preparedStatement12.setString(3, art.getPissn());
	  		preparedStatement12.setString(4, art.getEissn());
	  		preparedStatement12.setString(5,art.getXML());
	  		preparedStatement12.setInt(6,articleId);
	  		
	  		// execute insert SQL stetement
	  		int TableXMLId = preparedStatement12.executeUpdate();
	  		ResultSet rs12 = preparedStatement12.getGeneratedKeys();
            if(rs12.next())
            {
            	TableXMLId = rs12.getInt(1);
            }
            
            
            //-------------------------------------------------
	        
	        
			int authorId = -1;
			
			for(int i = 0;i<art.getAuthors().size();i++)
			{

			    	//Add new Author to DB
			    	Statement stmt2 = conn.createStatement();
			  		String insertTableSQL2 = "INSERT INTO Author (FirstName,LastName,Article_idArticle) VALUES (?,?,?)";
			  		PreparedStatement preparedStatement2 = conn.prepareStatement(insertTableSQL2,Statement.RETURN_GENERATED_KEYS);
			  		String AuthorName = art.getAuthors().get(i).name;
			  		String AuthorFName = "";
			  		String AuthorSName = "";
			  		String[] AuthorFirstSecondName = AuthorName.split(",");
			    	if(AuthorFirstSecondName.length>1)
			    		AuthorFName = AuthorFirstSecondName[1];
			    	AuthorSName = AuthorFirstSecondName[0];
			  		preparedStatement2.setString(1, AuthorFName);
			  		preparedStatement2.setString(2, AuthorSName);
			  		preparedStatement2.setInt(3, articleId);
			  		// execute insert SQL stetement
			  		authorId = preparedStatement2.executeUpdate();
			  		ResultSet rs = preparedStatement2.getGeneratedKeys();
	                if(rs.next())
	                {
	                	authorId = rs.getInt(1);
	                }
			  		
	                Statement stmt4 = conn.createStatement();
			  		String insertTableSQL4 = "INSERT INTO Email (Email,Author_idAuthor) VALUES (?,?)";
			  		PreparedStatement preparedStatement4 = conn.prepareStatement(insertTableSQL4,Statement.RETURN_GENERATED_KEYS);
			  		preparedStatement4.setString(1, art.getAuthors().get(i).email);
			  		preparedStatement4.setInt(2, authorId);
			  		// execute insert SQL stetement
			  		int EmailId = preparedStatement4.executeUpdate();
			  		ResultSet rs3 = preparedStatement4.getGeneratedKeys();
	                if(rs3.next())
	                {
	                	EmailId = rs3.getInt(1);
	                }
			  		
			  		LinkedList<String> affs =art.getAuthors().get(i).affiliation;
			  		for(int j =0;j<affs.size();j++)
			  		{
			  			Statement stmt5 = conn.createStatement();
				  		String insertTableSQL5 = "INSERT INTO Affiliation (AffiliationName,Author_idAuthor) VALUES (?,?)";
				  		PreparedStatement preparedStatement5 = conn.prepareStatement(insertTableSQL5,Statement.RETURN_GENERATED_KEYS);
				  		preparedStatement5.setString(1, affs.get(j));
				  		preparedStatement5.setInt(2, authorId);
				  		// execute insert SQL stetement
				  		int AffId = preparedStatement5.executeUpdate();
				  		ResultSet rs4 = preparedStatement5.getGeneratedKeys();
		                if(rs4.next())
		                {
		                	AffId = rs4.getInt(1);
		                }
			  		}     
			}
			Table[] Tables = art.getTables();
			for(int i = 0;i<Tables.length;i++)
			{
				Statement stmt6 = conn.createStatement();
		  		String insertTableSQL6 = "INSERT INTO ArtTable (TableOrder,TableCaption,TableFooter,StructureType,PragmaticType,HasXML,Article_idArticle,Section) VALUES (?,?,?,?,?,?,?,?)";
		  		PreparedStatement preparedStatement6 = conn.prepareStatement(insertTableSQL6,Statement.RETURN_GENERATED_KEYS);
		  		preparedStatement6.setString(1,Tables[i].getTable_title());
		  		preparedStatement6.setString(2, Tables[i].getTable_caption());
		  		preparedStatement6.setString(3, Tables[i].getTable_footer());
		  		preparedStatement6.setString(4, Tables[i].getTableStructureType().name());
		  		preparedStatement6.setString(5, Tables[i].PragmaticClass);
		  		
		  		if(Tables[i].isNoXMLTable())
		  			preparedStatement6.setString(6, "no"); //maybe change
		  		else 
		  			preparedStatement6.setString(6, "yes");
		  		preparedStatement6.setInt(7,articleId);
		  		preparedStatement6.setString(8, Tables[i].getSectionOfTable());
		  		
		  		// execute insert SQL stetement
		  		int TableId = preparedStatement6.executeUpdate();
		  		ResultSet rs4 = preparedStatement6.getGeneratedKeys();
	            if(rs4.next())
	            {
	            	TableId = rs4.getInt(1);
	            }
	                        
	            Cell[][] cells = Tables[i].cells;
	            for(int j = 0;j<cells.length;j++)
	            {
	             for(int k = 0;k<cells[j].length;k++){
	            	Statement stmt7 = conn.createStatement();
	    	  		String insertTableSQL7 = "INSERT INTO Cell (CellID,CellType,Table_idTable,RowN,ColumnN,HeaderRef,StubRef,SuperRowRef,Content,WholeHeader,WholeStub,WholeSuperRow) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	    	  		PreparedStatement preparedStatement7 = conn.prepareStatement(insertTableSQL7,Statement.RETURN_GENERATED_KEYS);
	    	  		preparedStatement7.setString(1,""+j+"."+k);
	    	  		preparedStatement7.setString(2, cells[j][k].getCellType());
	    	  		preparedStatement7.setInt(3,TableId);
	    	  		preparedStatement7.setString(4, cells[j][k].getRow_number()+"");
	    	  		preparedStatement7.setString(5, cells[j][k].getColumn_number()+"");
	    	  		preparedStatement7.setString(6, cells[j][k].getHeader_ref()); //maybe change
	    	  		preparedStatement7.setString(7,cells[j][k].getStub_ref());
	    	  		preparedStatement7.setString(8,cells[j][k].getSuper_row_ref());
	    	  		preparedStatement7.setString(9,cells[j][k].getCell_content());
	    	  		preparedStatement7.setString(10,cells[j][k].getHeader_values());
	    	  		preparedStatement7.setString(11,cells[j][k].getStub_values());
	    	  		preparedStatement7.setString(12,cells[j][k].getSubheader_values());
	    	  		
	    	  		// execute insert SQL stetement
	    	  		int CellId = preparedStatement7.executeUpdate();
	    	  		ResultSet rs5 = preparedStatement7.getGeneratedKeys();
	                if(rs5.next())
	                {
	                	CellId = rs5.getInt(1);
	                }
	    	  		
	    	  		for(int l=0;l<cells[j][k].CellRoles.size();l++)
	    	  		{
		  				String insertTableSQL8 = "INSERT INTO CellRoles (CellRole_idCellRole,Cell_idCell) VALUES (?,?)";
		    	  		PreparedStatement preparedStatement8 = conn.prepareStatement(insertTableSQL8,Statement.RETURN_GENERATED_KEYS);
	    	  			if(cells[j][k].CellRoles.get(l).equals("Header"))
	    	  			{
	    	    	  		preparedStatement8.setInt(1,1);
	    	  			}
	    	  			if(cells[j][k].CellRoles.get(l).equals("Stub"))
	    	  			{
	    	    	  		preparedStatement8.setInt(1,2);
	    	  			}
	    	  			if(cells[j][k].CellRoles.get(l).equals("Data"))
	    	  			{
	    	    	  		preparedStatement8.setInt(1,3);
	    	  			}
	    	  			if(cells[j][k].CellRoles.get(l).equals("SuperRow"))
	    	  			{
	    	    	  		preparedStatement8.setInt(1,4);
	    	  			}
		    	  		preparedStatement8.setInt(2, CellId);
		    	  		preparedStatement8.executeUpdate();
	    	  			
	    	  		}
	    	  		
	    	  		LinkedList<Annotation> annot = cells[j][k].annotations;
	    	  		for(int l = 0; l<annot.size();l++)
	    	  		{
	    	  			Statement stmt8 = conn.createStatement();
	        	  		String insertTableSQL8 = "INSERT INTO Annotation (Content,Start,End,AnnotationID,AgentType,AgentName,AnnotationURL,EnvironmentDescription,Cell_idCell,AnnotationDescription,AnnotationSchemaVersion,DateOfAction, Location) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        	  		PreparedStatement preparedStatement8 = conn.prepareStatement(insertTableSQL8,Statement.RETURN_GENERATED_KEYS);
	        	  		preparedStatement8.setString(1,annot.get(l).getContent());
	        	  		preparedStatement8.setInt(2,annot.get(l).getStart());
	        	  		preparedStatement8.setInt(3,annot.get(l).getEnd());
	        	  		preparedStatement8.setString(4,annot.get(l).getID());
	        	  		preparedStatement8.setString(5,"Software");
	        	  		preparedStatement8.setString(6,annot.get(l).getSource());
	        	  		preparedStatement8.setString(7,annot.get(l).getURL());
	        	  		preparedStatement8.setString(8,annot.get(l).getEnvironment()); 
	        	  		preparedStatement8.setInt(9,CellId);
	        	  		preparedStatement8.setString(10,annot.get(l).getDescription());
	        	  		preparedStatement8.setString(11,annot.get(l).getAgentVersion());// Should be version
	        	  		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        	  		Date date = new Date();
	        	  		preparedStatement8.setString(12,dateFormat.format(date));
	        	  		preparedStatement8.setString(13,annot.get(l).getLocation()); // Should be location
	        	  		// execute insert SQL stetement
	        	  		preparedStatement8.executeUpdate();
	    	  		}
	               
	            }
	            }
			}
			}
			catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
		
	}
	
	public void CloseDBConnection()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
