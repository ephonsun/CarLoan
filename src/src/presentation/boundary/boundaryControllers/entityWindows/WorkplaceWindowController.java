package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import business.businessObjects.Impiegato;
import business.businessObjects.Sede;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import presentation.boundary.IBoundary;
import presentation.boundary.tableModels.TMFactory;
import presentation.boundary.tableModels.TableModel;
import presentation.boundary.tableModels.TableModelType;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.WorkplaceParameters;

public class WorkplaceWindowController implements Initializable, IBoundary 
{
	@FXML
	private TableView<TableModel> tbImpiegati;
	@FXML
	private HBox pButton;
	@FXML
	private TextField txTelefono;
	@FXML
	private Label lb_EntityName;
	@FXML
	private TextField txIndirizzo;
	@FXML
	private TextField txCitta;
    @FXML
    private Button bConferma;    
    @FXML
    private Button bAnnulla;    
    @FXML
    private AnchorPane mainPane;
    
	private TableModel mModel;
	String entityID;
    private String entityType;
    private boolean mustShowDialog;
    private EntityOperation operation;

    private IBoundary tabParentBoundary;
	
	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		int intID = entityID == null ? 0 : Integer.parseInt(entityID);
		formData.put(WorkplaceParameters.WORKPLACE_ID, intID);
		formData.put(WorkplaceParameters.CITY, txCitta.getText());
		formData.put(WorkplaceParameters.TELEPHONE_NUMBER, txTelefono.getText());
		formData.put(WorkplaceParameters.ADDRESS, txIndirizzo.getText());
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) 
	{
		if(data == null) return;
		
		Sede workplace = (Sede)data.get(ServiceParameters.SERVICE_RESULT);
		entityID = workplace.getID();
		
		txCitta.setText(workplace.getCitta());
		txTelefono.setText(workplace.getNumeroTelefono());
		txIndirizzo.setText(workplace.getIndirizzo());
		
		// Fills the workers view
		ObservableList<TableModel> value = FXCollections.observableArrayList();
		// Takes all the entities 
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, "Impiegato");
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);

		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		List<?> entities = (List<?>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
		System.out.println(entities);
		
		for(Object obj : entities)
		{
			Impiegato w = (Impiegato)obj;
			if(w.getSede() != null)
				if(w.getSede().getID().equals(entityID))
					value.add(mModel.instantiate(obj));
		}
		
		tbImpiegati.setItems(value);		
	}

	@Override
	public void setEditable(boolean value) {
		txCitta.setEditable(value);
		txIndirizzo.setEditable(value);
		txTelefono.setEditable(value);
	}

	@Override
	public void show() {}

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
		tbImpiegati.getColumns().clear();		
		mModel = TMFactory.getTableModel(TableModelType.IMPIEGATO_SEDE);
		Map<String, String> tmFields = mModel.getTableFields();
		
		for(String fieldName : tmFields.keySet())
		{
			TableColumn<TableModel, String> column = new TableColumn<TableModel, String>(tmFields.get(fieldName));
			column.setCellValueFactory(new PropertyValueFactory<TableModel, String>(fieldName));
			tbImpiegati.getColumns().add(column);		
		}
		
		// Attiva l'opzione di ridimensionamento delle colonne basata sul contenuto
		tbImpiegati.setColumnResizePolicy((param) -> true );
		// Ridimensiona l'ultima colonna in modo da riempire la TableView
		Platform.runLater(() -> customResize(tbImpiegati));
	}
	
	public static void customResize(TableView<?> view) {
		float width = 0f;
		
		for(TableColumn<?,?> col : view.getColumns())
		{
	    	width += col.getWidth();
		}

	    double tableWidth = view.getWidth();

	    if (tableWidth > width) {
	        TableColumn<?, ?> col = view.getColumns().get(view.getColumns().size() - 1);
	        col.setPrefWidth(col.getWidth() + (tableWidth - width - 2));
	    }
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
