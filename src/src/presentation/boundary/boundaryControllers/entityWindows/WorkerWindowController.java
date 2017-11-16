package presentation.boundary.boundaryControllers.entityWindows;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import business.businessObjects.Impiegato;
import business.businessObjects.Sede;
import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.UserParameters;
import transferObject.parameters.entityParameters.WorkerParameters;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

public class WorkerWindowController implements Initializable, IBoundary
{
	@FXML
	private Button bConferma;
	@FXML
	private HBox pButton;
	@FXML
	private Label lb_EntityName;
	@FXML
	private TextField txCognome;
	@FXML
	private Button bAnnulla;
	@FXML
	private TextField txCodiceFiscale;
	@FXML
	private AnchorPane mainPane;
	@FXML
	private DatePicker dtDataNascita;
	@FXML
	private TextField txNome;
	@FXML
	private ChoiceBox<Sede> cbSede;

	String entityID;
    private String entityType;
    private boolean mustShowDialog;
    private EntityOperation operation;

    private IBoundary tabParentBoundary;

	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		String ID = entityID == null ? "0" : entityID;
		formData.put(WorkerParameters.WORKER_ID, ID);
		formData.put(WorkerParameters.NAME, txNome.getText());
		formData.put(WorkerParameters.SURNAME, txCognome.getText());
		formData.put(WorkerParameters.BIRTHDATE, dtDataNascita.getValue().toString());
		formData.put(WorkerParameters.TAX_CODE, txCodiceFiscale.getText());
		formData.put(WorkerParameters.WORKPLACE, cbSede.getValue());

		if(operation == EntityOperation.ADD)
		{
			formData.put(UserParameters.USERNAME, "");
			formData.put(UserParameters.PASSWORD, "");

		}

		return formData;
	}

	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		
		Impiegato worker = (Impiegato)data.get(ServiceParameters.SERVICE_RESULT);
		
		entityID = worker.getID();
		txNome.setText(worker.getNome());
		txCognome.setText(worker.getCognome());
		dtDataNascita.setValue(worker.getDataNascita());
		txCodiceFiscale.setText(worker.getCodiceFiscale());
		
		if(worker.getSede() == null)
			cbSede.setValue(null);
		else
		{
			for(Sede s : cbSede.getItems())
			{
				if(s.getID().equals(worker.getSede().getID()))
					cbSede.setValue(s);
			}
		}
	}

	@Override
	public void setEditable(boolean value) {
		txNome.setEditable(value);
		txCognome.setEditable(value);
		dtDataNascita.setEditable(value);
		txCodiceFiscale.setEditable(value);
		cbSede.setDisable(!value);
		cbSede.setStyle("-fx-opacity: 1");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		tabParentBoundary.setData(null);
		((Stage)mainPane.getScene().getWindow()).close();
		
	}

	@Override
	public void Init(CarLoanTO data) {
		operation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
		entityType = (String)data.get(GenericEntityParameters.ENTITY_TYPE);
		tabParentBoundary = (IBoundary)data.get(BoundaryParameters.CONTROLLER);
		
		setData((CarLoanTO)data.get(GenericEntityParameters.ENTITY));
		setEditable(operation != EntityOperation.VIEW);
		mustShowDialog = operation != EntityOperation.VIEW;
		
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

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, "Sede");
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);

		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		List<Sede> sedi = (List<Sede>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
		for(Sede s : sedi)
			cbSede.getItems().add(s);
	} 
	
	private void conferma(boolean mustShowDialog) {
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
	
	private void annulla(boolean mustShowDialog) {
		if(!mustShowDialog){
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
