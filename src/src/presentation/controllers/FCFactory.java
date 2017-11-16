package presentation.controllers;

public class FCFactory 
{
	static CarLoanFC instance;
	
	private FCFactory() {}
	
	public static CarLoanFC GetController()
	{
		if(instance == null)
			instance = new CarLoanFC();
		
		return instance;
	}
}
