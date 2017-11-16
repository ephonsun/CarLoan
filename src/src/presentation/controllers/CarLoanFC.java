package presentation.controllers;

import business.businessObjects.Utente;
import presentation.SessionManager;
import presentation.boundary.BoundaryFactory;
import presentation.boundary.IBoundary;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;


public class CarLoanFC implements FrontController 
{
	@Override
	public CarLoanTO processRequest(FCRequestType request) {
		CarLoanTO returnValue = null;
		switch(request)
		{
			case SHOW_LOGIN_WINDOW:
				returnValue = showLoginWindow();
				break;
			case SHOW_MAIN_WINDOW:
				returnValue = showMainWindow();
				break;
			case LOGOUT:
				returnValue = logout();
				break;
			default:
				System.err.println("Invalid FCRequestType");
				break;
		}
		return returnValue;
	}
	
	@Override
	public CarLoanTO processRequest(FCRequestType request, CarLoanTO data)
	{
		CarLoanTO returnValue = null;

		if(data == null)
		{
			returnValue = processRequest(request);
		}
		else
		{
			switch(request)
			{
				case SHOW_AVAILABLE_CARS:
					returnValue = showCars(data);
				break;
				case SHOW_BUSINESS_TAB:
					returnValue = showBusinessTab(data);
					break;
				case SHOW_GENERIC_TAB:
					returnValue = showGenericTab(data);
					break;
				case LOGIN:
					returnValue = login(data);
					break;
				case SHOW_ENTITY_WINDOW:
					returnValue = showEntityWindow(data);
					break;
				case EXECUTE_ENTITY_OPERATION:
					String entityType = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
					EntityOperation operation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
					String entityID = (String)data.get(GenericEntityParameters.ENTITY_IDENTIFIER);
					if(entityID == null) entityID = "0";
					returnValue = executeEntityOperation(entityType, operation, entityID, (CarLoanTO)data.get(GenericEntityParameters.ENTITY));
					break;
				case SHOW_PANEL:
					returnValue = showPanel(data);
					break;
				case SHOW_SEARCH_PANEL:
					returnValue = showSearchPanel(data);
					break;
				case CALCULATE_CONTRACT_PRICE:
					returnValue = calculateContractPrice(data);
					break;
				case CLOSE_CONTRACT:
					returnValue = closeContract(data);
					break;
				case START_CONTRACT:
					returnValue = startContract(data);
					break;
				case CANCEL_CONTRACT:
					returnValue = cancelContract(data);
					break;
				default:
					System.err.println("Invalid or unhandled FCRequestType");
					break;
			}
		}
		return returnValue; 
	}

	private CarLoanTO cancelContract(CarLoanTO data) {
		data.put(ServiceParameters.SERVICE_ENTITY, "GestioneContratto");
		data.put(ServiceParameters.SERVICE_NAME, "annullaContratto");
		
		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, data);
	}

	private CarLoanTO startContract(CarLoanTO data) {
		data.put(ServiceParameters.SERVICE_ENTITY, "GestioneContratto");
		data.put(ServiceParameters.SERVICE_NAME, "avviaContratto");
		
		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, data);
	}

	private CarLoanTO showCars(CarLoanTO data) {
		data.put(ServiceParameters.SERVICE_ENTITY, "GestioneContratto");
		data.put(ServiceParameters.SERVICE_NAME, "mostraAutoDisponibili");
		data.put(ServiceParameters.ACTIVE_USER, SessionManager.user);

		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, data);
	}

	private CarLoanTO closeContract(CarLoanTO data) {
		data.put(ServiceParameters.SERVICE_ENTITY, "GestioneContratto");
		data.put(ServiceParameters.SERVICE_NAME, "chiudiContratto");
		
		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, data);
	}

	private CarLoanTO calculateContractPrice(CarLoanTO data) 
	{
		CarLoanTO operationData = new CarLoanTO();
		operationData.put(ServiceParameters.SERVICE_ENTITY, "GestioneContratto");
		operationData.put(ServiceParameters.SERVICE_NAME, "calcolaPrezzo");
		operationData.put(GenericEntityParameters.ENTITY, data);
		
		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, operationData);
	}

	private CarLoanTO executeEntityOperation(String entityType, EntityOperation operation, String entityIdentifier, CarLoanTO entityData)
	{
		CarLoanTO operationData = new CarLoanTO();
		operationData.put(ServiceParameters.SERVICE_ENTITY, "Gestione" + entityType);
		operationData.put(ServiceParameters.SERVICE_NAME, operation.getServiceName());
		operationData.put(GenericEntityParameters.ENTITY_IDENTIFIER, entityIdentifier);
		operationData.put(GenericEntityParameters.ENTITY, entityData);
		operationData.put(ServiceParameters.ACTIVE_USER, SessionManager.user);
		return ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, operationData);
	}

	private CarLoanTO showMainWindow()
	{
		CarLoanTO data = new CarLoanTO();
		data.put(BoundaryParameters.WINDOW_NAME, "mainPanels/EmptyMainWindow");
		data.put(BoundaryParameters.WINDOW_HEIGHT, new Integer(600));
		data.put(BoundaryParameters.WINDOW_WIDTH, new Integer(800));
		data.put(BoundaryParameters.WINDOW_TITLE, "CarLoan");
		data.put(BoundaryParameters.IS_NESTED, false);
		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO showSearchPanel(CarLoanTO data)
	{
		data.put(BoundaryParameters.IS_NESTED, true);
		String entityName = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
		data.put(BoundaryParameters.WINDOW_NAME, BoundaryFactory.GetEntitySearchPanelName(entityName));

		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO showLoginWindow()
	{
		CarLoanTO data = new CarLoanTO();
		
		data.put(BoundaryParameters.WINDOW_NAME, "mainPanels/LoginWindow");
		data.put(BoundaryParameters.WINDOW_TITLE, "Login");
		data.put(BoundaryParameters.WINDOW_HEIGHT, new Integer(250));
		data.put(BoundaryParameters.WINDOW_WIDTH, new Integer(400));
		data.put(BoundaryParameters.IS_NESTED, false);
		
		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO showGenericTab(CarLoanTO data)
	{
		data.put(BoundaryParameters.IS_NESTED, true);
		data.put(BoundaryParameters.WINDOW_NAME, "mainPanels/GenericTab." + data.get(BoundaryParameters.TAB_NAME));

		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO showPanel(CarLoanTO data)
	{
		data.put(BoundaryParameters.IS_NESTED, true);
		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO showBusinessTab(CarLoanTO data)
	{
		data.put(BoundaryParameters.IS_NESTED, true);
		data.put(BoundaryParameters.WINDOW_NAME, "mainPanels/BusinessTab");
		
		return ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, data);
	}
	
	private CarLoanTO login(CarLoanTO data)
	{
		data.put(ServiceParameters.SERVICE_ENTITY, "GestioneLogin");
		data.put(ServiceParameters.SERVICE_NAME, "Login");
		
		CarLoanTO result = (CarLoanTO)ACFactory.GetController().handleRequest(ACRequestType.EXECUTE, data);
		Utente userConnected = (Utente)result.get(ServiceParameters.SERVICE_RESULT);
		SessionManager.user = userConnected;

		return result;
	}
	
	private CarLoanTO logout()
	{
		SessionManager.Reset();
		return FCFactory.GetController().processRequest(FCRequestType.SHOW_LOGIN_WINDOW);
	}
	
	
	private CarLoanTO showEntityWindow(CarLoanTO data) 
	{
		CarLoanTO acData = new CarLoanTO();
		acData.put(BoundaryParameters.WINDOW_NAME, BoundaryFactory.GetEntityBoundaryName((String)data.get(GenericEntityParameters.ENTITY_TYPE)));
		acData.put(BoundaryParameters.WINDOW_TITLE, data.get(GenericEntityParameters.ENTITY_TYPE));
		acData.put(BoundaryParameters.IS_NESTED, false);
		acData.put(BoundaryParameters.IS_MODAL, true);
		CarLoanTO formData = ACFactory.GetController().handleRequest(ACRequestType.DISPATCH, acData);
		IBoundary controller = (IBoundary)formData.get(BoundaryParameters.CONTROLLER);
		
		CarLoanTO result = null;
		
		EntityOperation entityOperation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
		String entityType = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
		String entityID = (String)data.get(GenericEntityParameters.ENTITY_IDENTIFIER);
		
		
		switch(entityOperation)
		{
			case ADD:
				controller.setEditable(true);
				break;
			case EDIT:
				result = executeEntityOperation(entityType, EntityOperation.VIEW, entityID, null);				
				break;
			case VIEW:
				result = executeEntityOperation(entityType, EntityOperation.VIEW, entityID, null);
				break;
			default:
				break;
		}
		
		data.put(GenericEntityParameters.ENTITY, result);
		controller.Init(data);
		
		return formData;
	}
}
