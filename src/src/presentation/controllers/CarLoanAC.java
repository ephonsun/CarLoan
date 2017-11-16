package presentation.controllers;

import java.io.IOException;

import business.applicationServices.ASFactory;
import business.exceptions.BusinessException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import presentation.boundary.BoundaryFactory;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.ServiceParameters;

public class CarLoanAC implements ApplicationController 
{
	@Override
	public CarLoanTO handleRequest(ACRequestType request, CarLoanTO data)
	{
		CarLoanTO returnValue = null;

		switch(request)
		{
			case DISPATCH:
				returnValue = dispatch(data);
				break;
			case EXECUTE:
				returnValue = execute(data);
				break;
			default:
				break;
		}
		return returnValue;
	}
	
	private CarLoanTO dispatch(CarLoanTO data)
	{
		if(data == null) return null;
		
		boolean isErrorMessage = data.get(BoundaryParameters.IS_ERROR_WINDOW) == null ? false : true;
		
		CarLoanTO resultData = null;
		if(isErrorMessage)
		{
			resultData = dispatchError(data);
		}
		else
		{
			resultData = dispatchGUI(data);
		}
		
		return resultData;
	}
	
	private CarLoanTO dispatchError(CarLoanTO errorData)
	{
		String errorMessage = (String)errorData.get(BoundaryParameters.ERROR_MESSAGE);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Errore!");
		alert.setHeaderText(errorMessage);

		alert.showAndWait();
		
		CarLoanTO result = new CarLoanTO();
		result.put(ServiceParameters.HAS_ERROR, true);
		return result;
	}
	
	private CarLoanTO dispatchGUI(CarLoanTO guiData)
	{
		Stage stage = new Stage();
		CarLoanTO boundaryData = new CarLoanTO();
		try 
		{
			boundaryData = BoundaryFactory.BuildBoundary((String)guiData.get(BoundaryParameters.WINDOW_NAME));
			Parent root = (Parent)boundaryData.get(BoundaryParameters.WINDOW_OBJECT);
			if(!(boolean)guiData.get(BoundaryParameters.IS_NESTED))
	        {
				Scene scene;
				if(guiData.containsParameter(BoundaryParameters.WINDOW_HEIGHT) && 
				   guiData.containsParameter(BoundaryParameters.WINDOW_WIDTH))
				{
					scene = new Scene(root, 
							(Integer)guiData.get(BoundaryParameters.WINDOW_WIDTH), 
							(Integer)guiData.get(BoundaryParameters.WINDOW_HEIGHT));
				}
				else
				{
					scene = new Scene(root);
				}
				 
		        stage.setTitle((String)guiData.get(BoundaryParameters.WINDOW_TITLE));
		        stage.setScene(scene);
		        stage.setResizable(false);
		        if(guiData.containsParameter(BoundaryParameters.IS_MODAL))
			        if((Boolean)guiData.get(BoundaryParameters.IS_MODAL))
			        	stage.initModality(Modality.APPLICATION_MODAL);
		        stage.show();
	        }
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return boundaryData;
	}
	
	private CarLoanTO execute(CarLoanTO data)
	{
		CarLoanTO serviceResult = null;
		try 
		{
			serviceResult = ASFactory.getApplicationService().callService(data);
		} 
		catch (BusinessException e) {
			CarLoanTO errorData = new CarLoanTO();
			errorData.put(BoundaryParameters.ERROR_MESSAGE, e.getMessage());
			errorData.put(BoundaryParameters.IS_ERROR_WINDOW, true);
			serviceResult = dispatch(errorData);
		}
		
		return serviceResult;
	}
}
