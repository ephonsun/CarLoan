package integration.daos;
import integration.connector.ConnectorFactory;
import integration.connector.ConnectorSupported;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUtil 
{
	public static void restoreTable(String tableName)
	{
		try {
			String deleteQuery = "DELETE FROM " + tableName + ";";
			String updateIndex = "ALTER TABLE " + tableName + " auto_increment = ?;";
			Connection conn = ConnectorFactory.getInstance(ConnectorSupported.MYSQL).getConnection();
			conn.setAutoCommit(false);
			PreparedStatement delete = conn.prepareStatement(deleteQuery);
			PreparedStatement update = conn.prepareStatement(updateIndex);
			delete.executeUpdate();
			update.setInt(1, 1);
			update.executeUpdate();
			conn.commit();
			if (delete != null) {
				delete.close();
			}
			if (update != null) {
				update.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
