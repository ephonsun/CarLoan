package integration.connector;

public class ConnectorFactory 
{
	private ConnectorFactory() {}
	
	public static Connector getInstance(ConnectorSupported connectorRequested)
	{
		Connector connector = null;
		
		switch(connectorRequested)
		{
		case MYSQL:
			connector = MySQLConnector.getInstance();
			break;
		default:
			break;
		
		}
		return connector;
	}
}
