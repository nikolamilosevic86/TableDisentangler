package classifiers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Main.KeyValue;

public class DrugHeaderFixer {

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String connectionUrl;
	String connectionUser;
	String connectionPassword;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DrugHeaderFixer sp = new DrugHeaderFixer();
		sp.processData();
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
	
	public void processData(){
		DataBase();
		try {
			String SQL = "SELECT * FROM cell inner join table_db_amia.cellroles on Cell_idCell=idCell where CellRole_idCellRole=1";
			Statement st = conn.createStatement();

			rs = st.executeQuery(SQL);
			int rowN = -1;
			int TableID = -1;
			while (rs.next()) {
				if(rs.getInt(4)!=TableID || rs.getInt(5)!=rowN)
				{
					String SQL2 = "SELECT * FROM cell where Table_idTable="+rs.getInt(4)+"  and RowN="+rs.getInt(5)+" and idCell not in (Select Cell_idCell from cellroles where CellRole_idCellRole=1);";
					Statement st2 = conn.createStatement();
					ResultSet rs2 = st2.executeQuery(SQL2);
					while(rs2.next())
					{
						String SQL3 = "INSERT into cellroles (CellRole_idCellRole,Cell_idCell) VALUES (1,"+rs2.getInt(1)+")";
						Statement st3 = conn.createStatement();
						int res = st3.executeUpdate(SQL3);
					}
					rowN=rs.getInt(5);
					TableID = rs.getInt(4);
				}
				
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}
