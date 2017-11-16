package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.util.ResourceBundle;

import business.businessObjects.Tariffa;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.fxml.Initializable;
import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.RentalRateParameters;

public class LoanRateWindowController implements Initializable, IBoundary
{
	@FXML
	private TextArea txDescrizione;

	@FXML
	private Button bConferma;

	@FXML
	private HBox pButton;

	@FXML
	private Label lb_EntityName;

	@FXML
	private Button bAnnulla;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private TextField txNome;
	
	@FXML
	private TextField txPrezzo;
	
	@FXML
	private TextField txPrezzoKM;
	
	@FXML
	private TextField txPrezzoBonus;
	
	@FXML
	private TextField txPrezzoKMBonus;

	@FXML
	private ChoiceBox<String> cbTipoTariffa;
	
    String entityID;
    private String entityType;
    private boolean mustShowDialog;
    private EntityOperation operation;
    private IBoundary tabParentBoundary;   
	
	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		int intID = entityID == null ? 0 : Integer.parseInt(entityID);
		formData.put(RentalRateParameters.RATE_ID, intID);
		formData.put(RentalRateParameters.NAME, txNome.getText());
		formData.put(RentalRateParameters.BASE_PRICE, txPrezzo.getText());
		formData.put(RentalRateParameters.KM_PRICE, txPrezzoKM.getText());
		formData.put(RentalRateParameters.TYPE, cbTipoTariffa.getValue());
		formData.put(RentalRateParameters.DESCRIPTION, txDescrizione.getText());
		formData.put(RentalRateParameters.BASE_PRICE_BONUS, txPrezzoBonus.getText());
		formData.put(RentalRateParameters.KM_PRICE_BONUS, txPrezzoKMBonus.getText());
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		
		Tariffa tariffa = (Tariffa)data.get(ServiceParameters.SERVICE_RESULT);
		entityID = tariffa.getID();
		txNome.setText(tariffa.getNome());
		txPrezzo.setText("" + tariffa.getPrezzoGiornaliero());
		txPrezzoKM.setText("" + tariffa.getPrezzoChilometro());
		txPrezzoBonus.setText("" + tariffa.getPrezzoGiornalieroBonus());
		txPrezzoKMBonus.setText("" + tariffa.getPrezzoChilometroBonus());
		txDescrizione.setText(tariffa.getDescrizione());
		cbTipoTariffa.setValue(tariffa.getTipo());
	}

	@Override
	public void setEditable(boolean value) {
		txNome.setEditable(value);
		txPrezzo.setEditable(value);
		txDescrizione.setEditable(value);
		cbTipoTariffa.setDisable(!value);
		cbTipoTariffa.setStyle("-fx-opacity: 1");		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		if(tabParentBoundary != null)
			tabParentBoundary.setData(null);
		((Stage)mainPane.getScene().getWindow()).close();		
	}

	@Override
	public void Init(CarLoanTO data) {
		operation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
		entityType = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
		tabParentBoundary = (IBoundary)data.get(BoundaryParameters.CONTROLLER);
		mustShowDialog = !(operation == EntityOperation.VIEW);
		
		setData((CarLoanTO)data.get(GenericEntityParameters.ENTITY));
		setEditable(operation != EntityOperation.VIEW);

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
		
		((Stage)mainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  hide();
	          }
	      });
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbTipoTariffa.getItems().addAll(Tariffa.tipiTariffa);
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
