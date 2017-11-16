package presentation.boundary.searchController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import business.businessObjects.Categoria;
import presentation.boundary.IBoundary;
import presentation.boundary.boundaryControllers.TabController;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.CarParameters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class CarSearchController implements Initializable, IBoundary 
{
    @FXML
    private ChoiceBox<Categoria> cbCategoria;

    @FXML
    private TextField txModello;

    @FXML
    private Button btReset;

    @FXML
    private Button btCerca;

    @FXML
    private TextField txMarca;
    
    private TabController parentController;

	@Override
	public CarLoanTO getData() {
		CarLoanTO searchData = new CarLoanTO();
		
		searchData.put(CarParameters.CATEGORY, cbCategoria.getValue());
		searchData.put(CarParameters.BRAND, txMarca.getText());
		searchData.put(CarParameters.MODEL, txModello.getText());
		
		return searchData;
	}

	@Override
	public void setData(CarLoanTO data) {}

	@Override
	public void setEditable(boolean value) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@SuppressWarnings("unchecked")
	@Override
	public void Init(CarLoanTO data) {
		// TODO Auto-generated method stub
		parentController = (TabController)data.get(BoundaryParameters.CONTROLLER);
		
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, "Categoria");
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);
		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		cbCategoria.getItems().addAll((List<Categoria>)entitiesData.get(ServiceParameters.SERVICE_RESULT));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		btCerca.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
				parentController.filterTableView(getData());
			}
		});
		
		btReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
				cbCategoria.setValue(null);
				parentController.filterTableView(null);
			}
		});
	}
}
