package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;
import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.CarStatusParameters;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class CarStatusWindowController implements Initializable, IBoundary
{
	@FXML
	private Button bConferma;

	@FXML
	private HBox pButton;

	@FXML
	private Label lb_EntityName;

	@FXML
	private ChoiceBox<String> cbStato;
	
    @FXML
    private ChoiceBox<Sede> cbSede;

	@FXML
	private TextField txChilometri;

	@FXML
	private Button bAnnulla;

	@FXML
	private TextArea txDettagli;

	@FXML
	private AnchorPane mainPane;

	 String entityID;
	 private boolean mustShowDialog;
	 private EntityOperation operation;
	 private IBoundary parentBoundary;    

	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		int intID = entityID == null ? 0 : Integer.parseInt(entityID);
		
		formData.put(CarStatusParameters.STATUS_ID, intID);
		formData.put(CarStatusParameters.DETAILS, txDettagli.getText());
		formData.put(CarStatusParameters.N_KM, txChilometri.getText());
		formData.put(CarStatusParameters.STATUS, cbStato.getValue());
		formData.put(CarStatusParameters.WORKPLACE, cbSede.getValue());
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		
		StatoAutoveicolo status = (StatoAutoveicolo)data.get(ServiceParameters.SERVICE_RESULT);
		
		entityID = status.getID();
		txDettagli.setText(status.getDettagli());
		txChilometri.setText("" + status.getNumeroChilometri());
		cbStato.setValue(status.getStato());
		
		for(Sede s : cbSede.getItems())
		{
			if(s.equals(status.getSedeVeicolo()))
				cbSede.setValue(s);
		}
	}

	@Override
	public void setEditable(boolean value) {
		txDettagli.setEditable(value);
		txChilometri.setEditable(value);
		
		cbSede.setDisable(!value);
		cbSede.setStyle("-fx-opacity: 1");
		cbStato.setDisable(!value);
		cbStato.setStyle("-fx-opacity: 1");
	}

	@Override
	public void show() {}

	@Override
	public void hide() {
		((Stage)mainPane.getScene().getWindow()).close();				
	}

	@Override
	public void Init(CarLoanTO data) {
		operation = (EntityOperation)data.get(GenericEntityParameters.ENTITY_OPERATION);
		mustShowDialog = !(operation == EntityOperation.VIEW);
		parentBoundary = (IBoundary)data.get(BoundaryParameters.CONTROLLER);

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
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, "Sede");
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);
		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		List<?> entities = (List<?>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
		List<Sede> sedi = new ArrayList<Sede>();
		for(Object c : entities)
		{
			Sede sede = (Sede)c;
			sedi.add(sede);
		}
		
		cbSede.getItems().addAll(sedi);
		cbStato.getItems().addAll(StatoAutoveicolo.StatoVeicolo);
	}
	
	private void conferma(boolean mustShowDialog)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, "Apportare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if(alert.getResult() == ButtonType.NO)
			return;

		((CarWindowController)parentBoundary).setStatus(new StatoAutoveicolo(getData()));
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
