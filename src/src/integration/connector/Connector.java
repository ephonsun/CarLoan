package integration.connector;

import integration.exceptions.IntegrationException;

import java.sql.Connection;

public interface Connector 
{
	public void openConnection() throws IntegrationException;
	public Connection getConnection();
	public void closeConnection() throws IntegrationException;
}
