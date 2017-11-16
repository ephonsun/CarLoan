package presentation.controllers;

public class ACFactory 
{
	private ACFactory() {}
	
	public static CarLoanAC GetController()
	{
		return new CarLoanAC();
	}
}
