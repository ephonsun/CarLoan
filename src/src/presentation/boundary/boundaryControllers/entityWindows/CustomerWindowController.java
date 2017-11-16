package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Cliente;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.CustomerParameters;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomerWindowController implements Initializable, IBoundary
{
	@FXML
    private TextField txNumeroPatente;
    @FXML
    private DatePicker dtScadenzaPatente;
    @FXML
    private HBox pButton;
    @FXML
    private Label lb_EntityName;
    @FXML
    private TextField txCittaResidenza;
    @FXML
    private TextField txCognome;
    @FXML
    private TextField txCodiceFiscale;
    @FXML
    private TextField txNumeroTelefono;
    @FXML
    private TextField txIndirizzo;
    @FXML
    private DatePicker dtDataNascita;
    @FXML
    private ChoiceBox<String> cbTipoPatente;
    @FXML
    private TextField txNome;
    @FXML
    private Button bConferma;    
    @FXML
    private Button bAnnulla;    
    @FXML
    private AnchorPane mainPane;   
    
    protected EntityOperation operation;
    protected String entityID;
    protected String entityType;
    protected boolean mustShowDialog;
    protected IBoundary tabParentBoundary;
    
	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		String ID = entityID == null ? "0" : entityID;
		formData.put(CustomerParameters.CUSTOMER_ID, ID);
		formData.put(CustomerParameters.NAME, txNome.getText());
		formData.put(CustomerParameters.SURNAME, txCognome.getText());
		
		LocalDate dataNascita = dtDataNascita.getValue();
		formData.put(CustomerParameters.BIRTHDATE, dataNascita == null ? null : 
												   dtDataNascita.getValue().toString());
		formData.put(CustomerParameters.CITY, txCittaResidenza.getText());
		formData.put(CustomerParameters.TAX_CODE, txCodiceFiscale.getText());
		formData.put(CustomerParameters.TELEPHONE_NUMBER, txNumeroTelefono.getText());
		formData.put(CustomerParameters.ADDRESS, txIndirizzo.getText());
		
		String tipoPatente = cbTipoPatente.getValue();
		formData.put(CustomerParameters.LICENCE_TYPE, tipoPatente == null ? null : 
													  cbTipoPatente.getValue().toString());
		
		formData.put(CustomerParameters.LICENCE_NUMBER, txNumeroPatente.getText());
		
		LocalDate dataScadenza = dtScadenzaPatente.getValue();
		formData.put(CustomerParameters.LICENCE_EXPIRATION_DATE, dataScadenza == null ? null : 
																 dtScadenzaPatente.getValue().toString());
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) 
	{
		if(data == null) return;
		
		Cliente customer = (Cliente)data.get(ServiceParameters.SERVICE_RESULT);
		
		entityID = customer.getID();
		txNome.setText(customer.getNome());
		txCognome.setText(customer.getCognome());
		dtDataNascita.setValue(customer.getDataNascita());
		txCittaResidenza.setText(customer.getCittaResidenza());
		txCodiceFiscale.setText(customer.getCodiceFiscale());
		txNumeroTelefono.setText(customer.getNumeroTelefono());
		txIndirizzo.setText(customer.getIndirizzoResidenza());
		cbTipoPatente.setValue(customer.getTipoPatente());
		txNumeroPatente.setText(customer.getNumeroPatente());
		dtScadenzaPatente.setValue(customer.getScadenzaPatente());
	}

	@Override
	public void setEditable(boolean value) {
		txNome.setEditable(value);
		txCognome.setEditable(value);
		dtDataNascita.setDisable(!value);
		dtDataNascita.setStyle("-fx-opacity: 1");
		txCittaResidenza.setEditable(value);
		txCodiceFiscale.setEditable(value);
		txNumeroTelefono.setEditable(value);
		txIndirizzo.setEditable(value);
		cbTipoPatente.setDisable(!value);
		cbTipoPatente.setStyle("-fx-opacity: 1");
		txNumeroPatente.setEditable(value);
		dtScadenzaPatente.setDisable(!value);
		dtScadenzaPatente.setStyle("-fx-opacity: 1");	
	}

	@Override
	public void Init(CarLoanTO data) {
		cbTipoPatente.getItems().addAll(Autoveicolo.TipiPatentePossibili);
		operation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
		entityType = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
		tabParentBoundary = (IBoundary)data.get(BoundaryParameters.CONTROLLER);
		
		setData((CarLoanTO)data.get(GenericEntityParameters.ENTITY));
		setEditable(operation != EntityOperation.VIEW);
		
		mustShowDialog = (operation != EntityOperation.VIEW);
		
		if(mustShowDialog)
		{
			bConferma.pressedProperty().addListener((event) -> {conferma(mustShowDialog);});
			bAnnulla.pressedProperty().addListener((event) -> {annulla(mustShowDialog);});
		}
		else
		{
			((HBox)bConferma.getParent()).getChildren().remove(bConferma);
			((HBox)bAnnulla.getParent()).getChildren().remove(bAnnulla);
		}
	
		((Stage)mainPane.getScene().getWindow()).onCloseRequestProperty().addListener((event) -> {hide();});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
		tabParentBoundary.setData(null);
		((Stage)mainPane.getScene().getWindow()).close();
	}
	
	private void conferma(boolean mustShowDialog)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, "Apportare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if(alert.getResult() != ButtonType.YES)
			return;

		CarLoanTO data = new CarLoanTO();
		data.put(GenericEntityParameters.ENTITY_OPERATION, operation);
		data.put(GenericEntityParameters.ENTITY, getData());
		data.put(GenericEntityParameters.ENTITY_IDENTIFIER, entityID);
		data.put(GenericEntityParameters.ENTITY_TYPE, entityType);

		CarLoanTO result = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, data);
		if(result.containsParameter(ServiceParameters.HAS_ERROR))
			return;

		tabParentBoundary.setData(null);

		hide();	
	}
	
	private void annulla(boolean mustShowDialog)
	{
		if(!mustShowDialog)
		{
			hide();
			return;
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Annullare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.NO)
			return;
		
		hide();
	}
}
