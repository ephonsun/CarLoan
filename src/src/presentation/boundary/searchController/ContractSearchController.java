package presentation.boundary.searchController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import presentation.boundary.IBoundary;
import presentation.boundary.boundaryControllers.TabController;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.entityParameters.ContractParameters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ContractSearchController implements Initializable, IBoundary 
{

	@FXML
    private CheckBox ckInCorso;

    @FXML
    private Button btReset;

    @FXML
    private Button btCerca;

    @FXML
    private CheckBox ckAperto;

    @FXML
    private CheckBox ckChiuso;

    @FXML
    private CheckBox ckAnnullato;

    @FXML
    private DatePicker dtDataInizio;

    @FXML
    private TextField txCliente;
    
    private TabController parentController;

	@Override
	public CarLoanTO getData() {
		CarLoanTO searchData = new CarLoanTO();
		
		List<String> stati = new ArrayList<String>();
		if(ckAperto.selectedProperty().get())
			stati.add("Aperto");
		if(ckChiuso.selectedProperty().get())
			stati.add("Chiuso");
		if(ckInCorso.selectedProperty().get())
			stati.add("In corso");
		if(ckAnnullato.selectedProperty().get())
			stati.add("Annullato");
				
		searchData.put(ContractParameters.STATUS, stati);
		searchData.put(ContractParameters.START_DATE, dtDataInizio.getValue());
		searchData.put(ContractParameters.CUSTOMER, txCliente.getText());

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
		parentController = (TabController)data.get(BoundaryParameters.CONTROLLER);	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
				dtDataInizio.setValue(null);
				parentController.filterTableView(null);
			}
		});
	}
}
