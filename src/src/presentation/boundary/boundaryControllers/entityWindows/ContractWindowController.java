package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.Cliente;
import business.businessObjects.Contratto;
import business.businessObjects.Optional;
import business.businessObjects.Sede;
import business.businessObjects.Tariffa;
import presentation.SessionManager;
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
import transferObject.parameters.entityParameters.ContractParameters;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class ContractWindowController implements IBoundary, Initializable
{
	@FXML
    private ChoiceBox<Categoria> cbCategoria;
    @FXML
    private Label lb_EntityName;
    @FXML
    private Button bAnnulla;
    @FXML
    private ChoiceBox<Tariffa> cbTariffa;
    @FXML
    private ChoiceBox<Sede> cbSedePartenza;
    @FXML
    private TableView<TableModel> tbOptional;
    @FXML
    private Button bConferma;
    @FXML
    private ChoiceBox<Sede> cbSedeArrivo;
    @FXML
    private TableView<TableModel> tbCliente;
    @FXML
    private Button bChiudiContratto;
    @FXML
    private ChoiceBox<String> cbStato;
    @FXML
    private Label lbPrezzo;
    @FXML
    private TableView<TableModel> tbAutoveicolo;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private DatePicker dtDataInizio;
    @FXML
    private DatePicker dtDataFine;
    @FXML
    private TextField txChilometri;
    @FXML
    private Button bAvviaContratto;
    @FXML
    private Button bAnnullaContratto;
    
    String entityID;
    private String entityType;
    private boolean mustShowDialog;
    private EntityOperation operation;
    private IBoundary tabParentBoundary;  
    private Contratto contract;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTableFields(tbAutoveicolo, TableModelType.AUTOVEICOLO_CONTRATTO, "Autoveicolo");
		initTableFields(tbCliente, TableModelType.CLIENTE_CONTRATTO, "Cliente");
		initTableFields(tbOptional, TableModelType.OPTIONAL, "Optional");
		tbOptional.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		tbOptional.setRowFactory(new Callback<TableView<TableModel>, TableRow<TableModel>>() {  
	        @Override  
	        public TableRow<TableModel> call(TableView<TableModel> tableView2) {  
	            final TableRow<TableModel> row = new TableRow<>();  
	            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {  
	                @Override  
	                public void handle(MouseEvent event) {  
	                    final int index = row.getIndex();
	                    if (index >= 0 && index < tbOptional.getItems().size() && tbOptional.getSelectionModel().isSelected(index)  ) {
	                    	tbOptional.getSelectionModel().clearSelection();
	                        event.consume();  
	                    }  
	                }  
	            });  
	            return row;  
	        }  
	    }); 
		
		// Inizializzare le choice box con le sedi
		List<?> entities = listEntities("Sede");
		List<Sede> sedi = new ArrayList<Sede>();
		for(Object c : entities)
		{
			Sede sede = (Sede)c;
			sedi.add(sede);
		}
		cbSedePartenza.getItems().addAll(sedi);
		cbSedeArrivo.getItems().addAll(sedi);
		
		// Inizializzare le choice box con le tariffe
		entities = listEntities("Tariffa");
		List<Tariffa> tariffe = new ArrayList<Tariffa>();
		for(Object c : entities)
			tariffe.add((Tariffa)c);
		
		cbTariffa.getItems().addAll(tariffe);
		
		// Inizializzare le choice box con le categorie
		entities = listEntities("Categoria");
		List<Categoria> categorie = new ArrayList<Categoria>();
		for(Object c : entities)
			categorie.add(((Categoria)c));
		
		cbCategoria.getItems().addAll(categorie);
		cbStato.getItems().addAll(Contratto.StatoContrattoPossibili);

		cbCategoria.valueProperty().addListener((obs, oldValue, newValue) -> {mostraAutoDisponibili();});
		cbSedePartenza.valueProperty().addListener((obs, oldValue, newValue) -> {mostraAutoDisponibili();});
		cbSedeArrivo.valueProperty().addListener((obs, oldValue, newValue) -> {mostraAutoDisponibili();});
		dtDataFine.valueProperty().addListener((obs, oldValue, newValue) -> {mostraAutoDisponibili();});
		dtDataInizio.valueProperty().addListener((obs, oldValue, newValue) -> {mostraAutoDisponibili();});
		 
		cbTariffa.valueProperty().addListener((obs, oldValue, newValue) -> {
			if(newValue.getTipo().equals("Illimitata"))
			{
				txChilometri.setText("0");
				txChilometri.setEditable(false);
			}
			else
			{
				txChilometri.setText("");
				txChilometri.setEditable(true);
			}
		});

		// Insert the customers
		ObservableList<TableModel> value = FXCollections.observableArrayList();
		// Takes all the entities 
		entities = listEntities("Cliente");
		TableModel mModel = TMFactory.getTableModel(TableModelType.CLIENTE_CONTRATTO);
		for(Object obj : entities)
		{
			value.add(mModel.instantiate(obj));
		}
		tbCliente.setItems(value);
		
		
		// Insert the optionals
		value = FXCollections.observableArrayList();
		// Takes all the entities 		
		entities = listEntities("Optional");
		mModel = TMFactory.getTableModel(TableModelType.OPTIONAL);
		
		for(Object obj : entities)
		{
			value.add(mModel.instantiate(obj));
		}
		
		tbOptional.setItems(value);
		
		bChiudiContratto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextInputDialog dialog = new TextInputDialog("0");
				dialog.setTitle("Chiusura contratto");
				dialog.setHeaderText("Inserisci il numero di chilometri percorsi dal cliente");
				dialog.setContentText("Chilometri:");
				
				java.util.Optional<String> result = dialog.showAndWait();
				result.ifPresent(km -> chiudiContratto(km));
			}
		});
		
		bAvviaContratto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				avviaContratto();
			}
		});
		
		bAnnullaContratto.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				annullaContratto();
			}
		});
		
		lbPrezzo.setText(String.format("%.2f", 0.0f) + " €");	
		cbStato.setValue("Aperto");
	}

	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		String ID = contract == null ? "0" : contract.getID();

		formData.put(ContractParameters.CONTRACT_ID, ID);
		formData.put(ContractParameters.START_DATE, dtDataInizio.getValue() == null ? null : dtDataInizio.getValue().toString());
		formData.put(ContractParameters.END_DATE, dtDataFine.getValue() == null ? null : dtDataFine.getValue().toString());
		if(contract == null)
			formData.put(ContractParameters.STIPULATION_DATE, LocalDate.now().toString());
		else
			formData.put(ContractParameters.STIPULATION_DATE, contract.getDataStipula().toString());
		formData.put(ContractParameters.CLOSING_DATE, null);
		formData.put(ContractParameters.STATUS, cbStato.getValue());
		formData.put(ContractParameters.WORKER, SessionManager.user);

		
		formData.put(ContractParameters.START_WORKPLACE, cbSedePartenza.getValue());
		formData.put(ContractParameters.END_WORKPLACE, cbSedeArrivo.getValue());

		TableModel customer = tbCliente.getSelectionModel().getSelectedItem();
		TableModel car = tbAutoveicolo.getSelectionModel().getSelectedItem();
		formData.put(ContractParameters.CUSTOMER, (Cliente)readEntity(customer == null ? null : customer.getId(), "Cliente"));
		formData.put(ContractParameters.CAR, (Autoveicolo)readEntity(car == null ? null : car.getId(), "Autoveicolo"));
		
		formData.put(ContractParameters.RENTAL_RATE, cbTariffa.getValue());
		formData.put(ContractParameters.N_KM, txChilometri.getText());
		
		List<Optional> optionals = new ArrayList<Optional>();	
		for(Object o : listEntities("Optional"))
		{
			Optional opt = (Optional)o;
			for(TableModel t : tbOptional.getSelectionModel().getSelectedItems())
			{
				if(opt.getID().equals(t.getId()))
					optionals.add(opt);
			}
		}
		formData.put(ContractParameters.OPTIONALS, optionals);
		String price = lbPrezzo.getText().replace(",", ".");
		formData.put(ContractParameters.PRICE, price.substring(0, price.length() - 1));
		
		return formData;
	}

	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		
		contract = (Contratto)data.get(ServiceParameters.SERVICE_RESULT);
		
		dtDataFine.setValue(contract.getDataFine());
		dtDataInizio.setValue(contract.getDataInizio());
		
		if(contract.getSedeRitiro() == null)
		{
			cbSedePartenza.setValue(null);
		}
		else
		{
			for(Sede s : cbSedePartenza.getItems())
			{
				if(s.getID().equals(contract.getSedeRitiro().getID()))
					cbSedePartenza.setValue(s);
			}
		}
		
		if(contract.getSedeConsegna() == null)
		{
			cbSedeArrivo.setValue(null);
		}
		else
		{
			for(Sede s : cbSedeArrivo.getItems())
			{
				if(s.getID().equals(contract.getSedeConsegna().getID()))
					cbSedeArrivo.setValue(s);
			}
		}
		
		if(contract.getAutoveicolo() == null || contract.getAutoveicolo().getCategoria() == null)
		{
			cbCategoria.setValue(null);
		}
		else
		{
			for(Categoria c : cbCategoria.getItems())
			{
				if(c.getID().equals(contract.getAutoveicolo().getCategoria().getID()))
					cbCategoria.setValue(c);
			}
		}
		
		if(contract.getTariffa() == null)
			cbTariffa.setValue(null);
		else
		{
			for(Tariffa t : cbTariffa.getItems())
			{
				if(t.getID().equals(contract.getTariffa().getID()))
					cbTariffa.setValue(t);
			}
		}
		
		cbStato.setValue(contract.getStatoContratto());
		lbPrezzo.setText(String.format("%.2f", contract.getCosto()) + " €");				
		txChilometri.setText("" + contract.getnChilometri());
		
		if(contract.getClienteStipulante() != null)
		{
			List<TableModel> customers = tbCliente.getItems();
			for(TableModel t : customers)
			{
				if(t.getId().equals(contract.getClienteStipulante().getID()))
					tbCliente.getSelectionModel().select(t);
			}
		}
		
		if(operation == EntityOperation.VIEW)
		{
			TableModel mAutoveicolo = TMFactory.getTableModel(TableModelType.AUTOVEICOLO_CONTRATTO);
			tbAutoveicolo.getItems().add(mAutoveicolo.instantiate(contract.getAutoveicolo()));
			tbAutoveicolo.getSelectionModel().select(0);
		}
		else
		{
			if(contract.getAutoveicolo() != null) 
			{
				List<TableModel> autoveicoli = tbAutoveicolo.getItems();
				for(TableModel t : autoveicoli)
				{
					if(contract.getAutoveicolo().getID().equals(t.getId()))
						tbAutoveicolo.getSelectionModel().select(t);
				}
			}
		}

		if(operation == EntityOperation.VIEW)
		{
			TableModel mOptional = TMFactory.getTableModel(TableModelType.OPTIONAL);
			int index = 0;
			for(Optional o : contract.getListaOptional())
			{
				tbOptional.getItems().add(mOptional.instantiate(o));
				tbOptional.getSelectionModel().select(index++);

			}
		}
		else
		{
			List<TableModel> optionals = tbOptional.getItems();
			for(TableModel t : optionals)
			{
				for(Optional o : contract.getListaOptional())
				{
					if(o != null && o.getID().equals(t.getId()))
						tbOptional.getSelectionModel().select(t);
				}
			}
		}
	}

	@Override
	public void setEditable(boolean value) {
		dtDataFine.setDisable(!value);
		dtDataFine.setStyle("-fx-opacity: 1");
		
		dtDataInizio.setDisable(!value);
		dtDataInizio.setStyle("-fx-opacity: 1");
		
		cbCategoria.setDisable(!value);
		cbCategoria.setStyle("-fx-opacity: 1");

		cbSedeArrivo.setDisable(!value);
		cbSedeArrivo.setStyle("-fx-opacity: 1");

		cbSedePartenza.setDisable(!value);
		cbSedePartenza.setStyle("-fx-opacity: 1");

		cbStato.setDisable(!value);
		cbStato.setStyle("-fx-opacity: 1");

		cbTariffa.setDisable(!value);
		cbTariffa.setStyle("-fx-opacity: 1");
		
		txChilometri.setEditable(value);
		txChilometri.setFocusTraversable(value);

		tbAutoveicolo.setMouseTransparent(!value);
		tbAutoveicolo.setFocusTraversable(value);

		tbOptional.setMouseTransparent(!value);
		tbOptional.setFocusTraversable(value);

		tbCliente.setMouseTransparent(!value);
		tbCliente.setFocusTraversable(value);
		
		bChiudiContratto.setDisable(!value);
		bAvviaContratto.setDisable(!value);
		bAnnullaContratto.setDisable(!value);
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
		
		setData((CarLoanTO)data.get(GenericEntityParameters.ENTITY));
		setEditable(operation != EntityOperation.VIEW);
		
		if(operation != EntityOperation.VIEW && operation != EntityOperation.ADD)
		{
			if(contract.getStatoContratto().equals("In corso"))
			{
				setEditable(false);
				bChiudiContratto.setDisable(false);
			} 
			else if(contract.getStatoContratto().equals("Aperto"))
			{
				setEditable(true);
				bChiudiContratto.setDisable(true);
			}
			else
			{
				setEditable(false);
			}
		}
		
		mustShowDialog = !(operation == EntityOperation.VIEW);

		if(mustShowDialog)
		{
			bConferma.pressedProperty().addListener((event) -> {conferma(mustShowDialog);});
			bAnnulla.pressedProperty().addListener((event) -> {annulla(mustShowDialog);});
		}
		else
		{
			((FlowPane)bConferma.getParent()).getChildren().remove(bConferma);
			((FlowPane)bAnnulla.getParent()).getChildren().remove(bAnnulla);
		}
		
		((Stage)mainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  hide();
	          }
	      });
	}

	private void initTableFields(TableView<TableModel> tbView, TableModelType type, String entityType)
	{
		tbView.getColumns().clear();		
		
		TableModel mModel = TMFactory.getTableModel(type);
		Map<String, String> tmFields = mModel.getTableFields();
		
		for(String fieldName : tmFields.keySet())
		{
			TableColumn<TableModel, String> column = new TableColumn<TableModel, String>(tmFields.get(fieldName));
			column.setCellValueFactory(new PropertyValueFactory<TableModel, String>(fieldName));
			tbView.getColumns().add(column);		
		}
		
	    // Attiva l'opzione di ridimensionamento delle colonne basata sul contenuto
		tbView.setColumnResizePolicy((param) -> true );
	    // Ridimensiona l'ultima colonna in modo da riempire la TableView
	    Platform.runLater(() -> customResize(tbView));
	    
	    tbView.setRowFactory( tv -> {
		    TableRow<TableModel> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	TableModel rowData = row.getItem();
		        	
		        	CarLoanTO data = new CarLoanTO();
		        	data.put(GenericEntityParameters.ENTITY_IDENTIFIER, rowData.getId());
					data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
					data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.VIEW);
					
					FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
		        }
		    });
		    return row ;
		});
	}
	
	private void conferma(boolean mustShowDialog)
	{
		if(cbStato.getValue().equals("Aperto"))
		{
			float prezzo = calcolaPrezzo();
			if(prezzo == -1)
				return;
			
			lbPrezzo.setText(String.format("%.2f", prezzo) + " €");
			Alert priceAlert = new Alert(AlertType.CONFIRMATION);

			priceAlert.setHeaderText("Il totale ammonta a " + lbPrezzo.getText());
			priceAlert.setContentText("Procede con la sottoscrizione?");
			priceAlert.showAndWait();
			
			if(priceAlert.getResult() == ButtonType.CANCEL) return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "Apportare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if(alert.getResult() == ButtonType.CANCEL) return;

		if(alert.getResult() == ButtonType.YES)
		{
			CarLoanTO data = new CarLoanTO();
			data.put(GenericEntityParameters.ENTITY_OPERATION, operation);
			data.put(GenericEntityParameters.ENTITY, getData());
			data.put(GenericEntityParameters.ENTITY_IDENTIFIER, entityID);
			data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
			FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, data);
		}						
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
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.NO)
			return;
		
		hide();
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
	
	private Object readEntity(String entityID, String entityType)
	{
		if(entityID == null) return null;
		CarLoanTO readRequestData = new CarLoanTO();
		readRequestData.put(GenericEntityParameters.ENTITY_TYPE, entityType);
		readRequestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.VIEW);
		readRequestData.put(GenericEntityParameters.ENTITY_IDENTIFIER, entityID);
		CarLoanTO resultData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, readRequestData);
		return resultData.get(ServiceParameters.SERVICE_RESULT);
	}
	
	private List<?> listEntities(String entityType)
	{
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, entityType);
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);
		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		return (List<?>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
	}
	
	private float calcolaPrezzo()
	{	
		CarLoanTO result = FCFactory.GetController().processRequest(FCRequestType.CALCULATE_CONTRACT_PRICE, getData());		
		if(result.containsParameter(ServiceParameters.HAS_ERROR))
			return -1;
		
		return (Float)result.get(ServiceParameters.SERVICE_RESULT);
	}
	
	private void chiudiContratto(String km)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, "Chiusura Contratto", ButtonType.YES, ButtonType.NO);
		alert.setHeaderText("Chiudere il contratto selezionato?");
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.NO)
			return;
		
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY, getData());
		requestData.put(ContractParameters.ACTUAL_KILOMETERS, km);
		
		CarLoanTO result = (CarLoanTO)FCFactory.GetController().processRequest(FCRequestType.CLOSE_CONTRACT, requestData).get(ServiceParameters.SERVICE_RESULT);		

		boolean hasPenaltyClause = (Boolean)result.get(ContractParameters.HAS_PENALTY_CLAUSE);
		float penaltyClause = (Float)result.get(ContractParameters.PENALTY_CLAUSE);
		if(hasPenaltyClause)
		{
			Alert clauseAlert = new Alert(AlertType.WARNING);
			clauseAlert.setTitle("Attenzione!");
			clauseAlert.setHeaderText("I termini del contratto non sono stati rispettati!");
			clauseAlert.setContentText("La penale ammonta a: " + String.format("%.2f", penaltyClause) + " €");
			clauseAlert.showAndWait();
			
			lbPrezzo.setText(String.format("%.2f", penaltyClause + calcolaPrezzo()) + " €");
		}
		cbStato.setValue("Chiuso");
		setEditable(false);
	}
	
	private void avviaContratto()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION, "Avvio Contratto", ButtonType.YES, ButtonType.NO);
		alert.setHeaderText("Avviare il contratto selezionato?");
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.NO)
			return;
		
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY, getData());
		requestData.put(ContractParameters.START_DATE, LocalDate.now());
		
		CarLoanTO result = (CarLoanTO)FCFactory.GetController().processRequest(FCRequestType.START_CONTRACT, requestData);		

		if(!result.containsParameter(ServiceParameters.HAS_ERROR)) {
			cbStato.setValue("In corso");
			setEditable(false);
		}
	}
	
	private void annullaContratto()
	{
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY, getData());
		CarLoanTO result = (CarLoanTO)FCFactory.GetController().processRequest(FCRequestType.CANCEL_CONTRACT, requestData).get(ServiceParameters.SERVICE_RESULT);	
		
		boolean hasPenaltyClause = (Boolean)result.get(ContractParameters.HAS_PENALTY_CLAUSE);
		float penaltyClause = (Float)result.get(ContractParameters.PENALTY_CLAUSE);
		if(hasPenaltyClause)
		{
			Alert alert = new Alert(AlertType.WARNING, "Attenzione!", ButtonType.YES, ButtonType.NO);
			alert.setHeaderText("Annullare il contratto comporta il pagamento di una penale! Continuare?");
			alert.setContentText("La penale ammonta a: " + String.format("%.2f", penaltyClause) + " €");
			alert.showAndWait();
			
			if(alert.getResult() == ButtonType.NO)
				return;
		}
		
		if(!result.containsParameter(ServiceParameters.HAS_ERROR)) {
			cbStato.setValue("Annullato");
			setEditable(false);
		}
	}
	
	private void mostraAutoDisponibili()
	{
		tbAutoveicolo.setColumnResizePolicy((param) -> true );
		if(cbSedePartenza.getValue() == null || 
		   cbSedeArrivo.getValue() == null || 
		   dtDataInizio.getValue() == null || 
		   dtDataFine.getValue() == null || 
		   cbCategoria.getValue() == null ||
		   operation == EntityOperation.VIEW)
			return;
		
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY, getData());
		List<?> autoveicoli = (List<?>)FCFactory.GetController().processRequest(FCRequestType.SHOW_AVAILABLE_CARS, requestData).get(ServiceParameters.SERVICE_RESULT);		
		
		TableModel mModel = TMFactory.getTableModel(TableModelType.AUTOVEICOLO_CONTRATTO);
		tbAutoveicolo.getItems().clear();
		ObservableList<TableModel> value = FXCollections.observableArrayList();
				
		for(Object obj : autoveicoli)
		{
			if(((Autoveicolo)obj).getCategoria().getID().equals(cbCategoria.getValue().getID()))
				value.add(mModel.instantiate(obj));
		}
		tbAutoveicolo.setItems(value);
	}
}
