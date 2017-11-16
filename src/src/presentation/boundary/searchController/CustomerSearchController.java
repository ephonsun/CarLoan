package presentation.boundary.searchController;

import java.net.URL;
import java.util.ResourceBundle;

import presentation.boundary.IBoundary;
import presentation.boundary.boundaryControllers.TabController;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.entityParameters.CustomerParameters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CustomerSearchController implements Initializable, IBoundary 
{

    @FXML
    private TextField txNomeCliente;

    @FXML
    private TextField txCognomeCliente;

    @FXML
    private TextField txCodiceFiscale;
    
    @FXML
    private Button btCerca;

    @FXML
    private Button btReset;
    
    private TabController parentController;

	@Override
	public CarLoanTO getData() {
		CarLoanTO searchData = new CarLoanTO();
		
		searchData.put(CustomerParameters.NAME, txNomeCliente.getText());
		searchData.put(CustomerParameters.SURNAME, txCognomeCliente.getText());
		searchData.put(CustomerParameters.TAX_CODE, txCodiceFiscale.getText());

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

	@Override
	public void Init(CarLoanTO data) {
		// TODO Auto-generated method stub
		parentController = (TabController)data.get(BoundaryParameters.CONTROLLER);
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
				parentController.filterTableView(null);
			}
		});
	}
}
