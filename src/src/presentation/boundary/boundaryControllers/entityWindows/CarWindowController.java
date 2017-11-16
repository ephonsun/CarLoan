package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.CarParameters;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.StatoAutoveicolo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

public class CarWindowController implements Initializable, IBoundary
{
	@FXML
    private TextField txModello;

    @FXML
    private ChoiceBox<Categoria> cbCategoria;

    @FXML
    private HBox pButton;

    @FXML
    private Label lb_EntityName;

    @FXML
    private Button bAnnulla;

    @FXML
    private ChoiceBox<String> cbImmatricolazione;

    @FXML
    private TextField txCilindrata;

    @FXML
    private ChoiceBox<String> cbCambio;

    @FXML
    private Button bConferma;

    @FXML
    private Button bStatoVeicolo;

    @FXML
    private ChoiceBox<String> cbPosti;

    @FXML
    private TextArea txOptionalSerie;

    @FXML
    private ChoiceBox<String> cbPorte;

    @FXML
    private TextField txMarca;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField txTarga;
    
    String entityID;
    private String entityType;
    private boolean mustShowDialog;
    private EntityOperation operation;
    private IBoundary tabParentBoundary;    
    private StatoAutoveicolo stato;
    private StatoAutoveicolo initStato;
    
	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		int intID = entityID == null ? 0 : Integer.parseInt(entityID);

		formData.put(CarParameters.CAR_ID, intID);
		formData.put(CarParameters.MODEL, txModello.getText());
		formData.put(CarParameters.BRAND, txMarca.getText());
		formData.put(CarParameters.ENGINE_CAPACITY, txCilindrata.getText());
		formData.put(CarParameters.BASIC_OPTIONALS, txOptionalSerie.getText());		
		formData.put(CarParameters.LICENCE_PLATE, txTarga.getText());
		formData.put(CarParameters.MATRICULATION, cbImmatricolazione.getValue());
		formData.put(CarParameters.GEAR_TYPE, cbCambio.getValue());
		formData.put(CarParameters.SEATS_NUMBER, cbPosti.getValue());
		formData.put(CarParameters.DOOR_NUMBER, cbPorte.getValue());
		formData.put(CarParameters.CAR_STATUS, stato);
		formData.put(CarParameters.CATEGORY, cbCategoria.getValue());
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		Autoveicolo auto = (Autoveicolo)data.get(ServiceParameters.SERVICE_RESULT);
		
		entityID = auto.getID();
		stato = auto.getStatoVeicolo();
		if(initStato == null)
			initStato = stato;
		
		txModello.setText(auto.getModello());
		txMarca.setText(auto.getMarca());
		txTarga.setText(auto.getTarga());
		txOptionalSerie.setText(auto.getAccessoriSerie());
		txCilindrata.setText("" + auto.getCilindrata());
		
		cbPorte.setValue("" + auto.getNumeroPorte());
		cbPosti.setValue("" + auto.getNumeroPosti());
		cbImmatricolazione.setValue(auto.getAnnoImmatricolazione());
		cbCambio.setValue(auto.getTipoCambio());
		
		cbCategoria.setValue(auto.getCategoria());
		
		for(Categoria c : cbCategoria.getItems())
		{
			if(c.getID().equals(auto.getCategoria().getID()))
				cbCategoria.setValue(c);
		}
	}

	@Override
	public void setEditable(boolean value) {
		txModello.setEditable(value);
		txCilindrata.setEditable(value);
		txTarga.setEditable(value);
		txOptionalSerie.setEditable(value);
		txMarca.setEditable(value);
		
		cbCategoria.setDisable(!value);
		cbCategoria.setStyle("-fx-opacity: 1");
		
		cbImmatricolazione.setDisable(!value);
		cbImmatricolazione.setStyle("-fx-opacity: 1");
		
		cbCambio.setDisable(!value);
		cbCambio.setStyle("-fx-opacity: 1");
		
		cbPorte.setDisable(!value);
		cbPorte.setStyle("-fx-opacity: 1");
		
		cbPosti.setDisable(!value);
		cbPosti.setStyle("-fx-opacity: 1");
	}

	@Override
	public void show() {}

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
		
		bStatoVeicolo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) 
			{
				CarLoanTO data = new CarLoanTO();
				
				CarLoanTO requestData = new CarLoanTO();
				requestData.put(GenericEntityParameters.ENTITY_TYPE, "Autoveicolo");
				requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.VIEW);
				requestData.put(GenericEntityParameters.ENTITY_IDENTIFIER, entityID);
				CarLoanTO entityData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
				Autoveicolo auto = (Autoveicolo)entityData.get(ServiceParameters.SERVICE_RESULT);
				
				String ID = auto == null ? "" : auto.getStatoVeicolo().getID();
				
				data.put(GenericEntityParameters.ENTITY_IDENTIFIER, ID);
				data.put(GenericEntityParameters.ENTITY_TYPE, "StatoAutoveicolo");
				data.put(GenericEntityParameters.ENTITY_OPERATION, operation);
				data.put(BoundaryParameters.CONTROLLER, CarWindowController.this);
				
				FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
			}
		});
		
		((Stage)mainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  hide();
	          }
	      });
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbCambio.getItems().addAll(Autoveicolo.TipologiaCambio);
		cbImmatricolazione.getItems().addAll(Autoveicolo.AnnoImmatricolazionePossibili);
		cbPorte.getItems().addAll(Autoveicolo.NumeroPortePossibili);
		cbPosti.getItems().addAll(Autoveicolo.NumeroPostiPossibili);
		
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, "Categoria");
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);
		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		List<?> entities = (List<?>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
		List<Categoria> categorie = new ArrayList<Categoria>();
		for(Object c : entities)
			categorie.add(((Categoria)c));
		
		cbCategoria.getItems().addAll(categorie);
	}
	
	public void setStatus(StatoAutoveicolo stato)
	{
		this.stato = stato;
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
		if(initStato != null && operation == EntityOperation.ADD)
		{
			CarLoanTO readData = new CarLoanTO();
			readData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.REMOVE);
			readData.put(GenericEntityParameters.ENTITY_IDENTIFIER, stato.getID());
			readData.put(GenericEntityParameters.ENTITY_TYPE, entityType);
			FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, readData);
		}
		
		if(stato != null && !stato.equals(initStato))
		{
			CarLoanTO data = new CarLoanTO();
			data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.EDIT);
			data.put(GenericEntityParameters.ENTITY, initStato);
			data.put(GenericEntityParameters.ENTITY_IDENTIFIER, stato.getID());
			data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
			FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, data);
		}
		
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
