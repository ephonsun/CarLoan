package presentation.boundary;

import java.io.IOException;

import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class BoundaryFactory 
{
	private static String FXML_PATH = "fxml/";
	private static BoundaryFactory instance;
	
	private BoundaryFactory()
	{}
	
	public static CarLoanTO BuildBoundary(String boundaryName) throws IOException
	{
		if(instance == null)
			instance = new BoundaryFactory();
		
		return instance.LoadFXML(boundaryName);
	}
	
	public static String GetEntityBoundaryName(String entityName)
	{
		return "entityWindow/"+ Character.toLowerCase(entityName.charAt(0)) + entityName.substring(1) + "Window";
	}
	
	public static String GetEntitySearchPanelName(String entityName)
	{
		return "searchPanels/" + entityName + "Ricerca";
	}
	
	private CarLoanTO LoadFXML(String fileName) throws IOException
	{
		String[] tokens = fileName.split("\\.");
		String fxmlFileName = fileName;
		if(tokens.length > 1)
			fxmlFileName = tokens[0];
		
		String actualFileName = FXML_PATH + fxmlFileName + ".fxml";
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(actualFileName));
		Parent root =  loader.load();
		
		CarLoanTO returnData = new CarLoanTO();
		returnData.put(BoundaryParameters.WINDOW_OBJECT, root);
		returnData.put(BoundaryParameters.CONTROLLER, loader.getController());
		
		return returnData;
	}
}
