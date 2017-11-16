package presentation.controllers;
import transferObject.CarLoanTO;

public interface FrontController 
{
	public CarLoanTO processRequest(FCRequestType request, CarLoanTO data);
	public CarLoanTO processRequest(FCRequestType request);
}
