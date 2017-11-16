package integration.connector;

import integration.exceptions.IntegrationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector implements Connector
{
	private final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final String DATABASE = "carloandb";
	private final String PORT = "3306";
	private final String USER_ID = "root";
	private final String PASSWORD = "root";
	private Connection conn;
	private static MySQLConnector instance = null;
	
	private MySQLConnector() {}
	
	public static MySQLConnector getInstance() {
		if(instance == null) instance = new MySQLConnector();

		return instance;
	}
	
	@Override
	public void openConnection() throws IntegrationException {
		if(conn != null) return;
		
		String dbUrl = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE;
		try
		{
			//Carica driver
			Class.forName(DRIVER_CLASS_NAME).newInstance();
			//Stabilisce la connessione
			conn = DriverManager.getConnection(dbUrl, USER_ID, PASSWORD);
		}
		catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			//Eccezioni di caricamento fallito dei driver o di mancata connessione
			throw new IntegrationException("Failed to connect to " + dbUrl);
		}
	}

	@Override
	public Connection getConnection() {
		return conn;
	}

	@Override
	public void closeConnection() throws IntegrationException {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new IntegrationException(e.getMessage());
		}
	}

}
