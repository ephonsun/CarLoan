package presentation.controllers;

import transferObject.CarLoanTO;


public interface ApplicationController 
{
	public Object handleRequest(ACRequestType request, CarLoanTO data);
}
