package integration.exceptions;

public class IntegrationException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public IntegrationException(String message) 
	{
		super(message);
	}
}
