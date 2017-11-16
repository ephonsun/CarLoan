package presentation.boundary.boundaryControllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import presentation.boundary.IBoundary;
import presentation.boundary.tableModels.TableModel;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabController implements Initializable, IBoundary {

    @FXML
    private Button bRimuovi;
    @FXML
    private Button bModifica;
    @FXML
    private Button bAggiungi;
    @FXML
    private Button bVisualizza;
    @FXML
    private TableView<TableModel> tbView;
    @FXML
    private Label lbTitle;
    @FXML
    private TitledPane pRicerca;
    
    private TableModel mModel;
    private String entityType;
    private IBoundary searchController;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{ 
		tbView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TableModel>() {
			@Override
			public void changed(ObservableValue<? extends TableModel> arg0,
					TableModel arg1, TableModel arg2) 
			{
				bVisualizza.setDisable(tbView.getSelectionModel().isEmpty());
				bModifica.setDisable(tbView.getSelectionModel().isEmpty());
				bRimuovi.setDisable(tbView.getSelectionModel().isEmpty());
			}
		});
				
		pRicerca.expandedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				
				if(arg2 == true && pRicerca.getContent() == null)
				{
					// LOAD THE SEARCH PANEL
					CarLoanTO formData = new CarLoanTO();
					FCRequestType request = FCRequestType.SHOW_SEARCH_PANEL;
					
					formData.put(GenericEntityParameters.ENTITY_TYPE, entityType);
					
					CarLoanTO panelData = FCFactory.GetController().processRequest(request, formData);
					Parent root = (Parent)panelData.get(BoundaryParameters.WINDOW_OBJECT);
					pRicerca.setContent(root);
					searchController = (IBoundary)panelData.get(BoundaryParameters.CONTROLLER);
					CarLoanTO to = new CarLoanTO();
					to.put(BoundaryParameters.CONTROLLER, TabController.this);
					searchController.Init(to);
				}
				else if(arg2 == false)
				{
					// RESET THE DATA IN THE TABLE VIEW WHEN CLOSING THE SEARCH PANEL
					filterTableView(null);
				}
			}
		});
	}

	@Override
	public CarLoanTO getData() {
		return null;
	}

	@Override
	public void setData(CarLoanTO data) 
	{
		ObservableList<TableModel> value = FXCollections.observableArrayList();
		
		// Takes all the entities 
		CarLoanTO requestData = new CarLoanTO();
		requestData.put(GenericEntityParameters.ENTITY_TYPE, entityType);
		requestData.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.LIST);
		CarLoanTO entitiesData = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, requestData);
		List<?> entities = (List<?>)entitiesData.get(ServiceParameters.SERVICE_RESULT);
		
		if(entities == null) return;
		
		for(Object obj : entities)
			value.add(mModel.instantiate(obj));
		
		tbView.setItems(value);
		
		// Attiva l'opzione di ridimensionamento delle colonne basata sul contenuto
		tbView.setColumnResizePolicy((param) -> true );
		// Ridimensiona l'ultima colonna in modo da riempire la TableView
		Platform.runLater(() -> customResize(tbView));
	}

	@Override
	public void setEditable(boolean value) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void Init(CarLoanTO data)
	{
		lbTitle.setText((String)data.get(BoundaryParameters.TAB_TITLE));

		bAggiungi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				CarLoanTO data = new CarLoanTO();

				data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
				data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.ADD);
				data.put(BoundaryParameters.CONTROLLER, TabController.this);

				FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
			}
		});		
		
		bVisualizza.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CarLoanTO data = new CarLoanTO();
				TableModel itemSelected = tbView.getSelectionModel().getSelectedItem();
				
				data.put(GenericEntityParameters.ENTITY_IDENTIFIER, itemSelected.getId());
				data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
				data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.VIEW);
				data.put(BoundaryParameters.CONTROLLER, TabController.this);
				
				FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
			}		
		});
		
		bRimuovi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) 
			{	
				Alert alert = new Alert(AlertType.CONFIRMATION, "Sicuro di voler eliminare l'entità selezionata?", 
										ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) 
				{
					TableModel itemSelected = tbView.getSelectionModel().getSelectedItem();
					CarLoanTO data = new CarLoanTO();

					data.put(GenericEntityParameters.ENTITY_IDENTIFIER, itemSelected.getId());
					data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
					data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.REMOVE);

					FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, data);
					setData(null);				
				}
			}		
		});
		
		bModifica.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CarLoanTO data = new CarLoanTO();
				TableModel itemSelected = tbView.getSelectionModel().getSelectedItem();

				data.put(GenericEntityParameters.ENTITY_IDENTIFIER, itemSelected.getId());
				data.put(GenericEntityParameters.ENTITY_TYPE, entityType);
				data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.EDIT);
				data.put(BoundaryParameters.CONTROLLER, TabController.this);

				FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
			}		
		});
		
		tbView.getColumns().clear();		
		mModel = (TableModel)data.get(GenericEntityParameters.ENTITY_TABLE_MODEL);
		entityType = mModel.getClass().getSimpleName().replace("TM", "");
		Map<String, String> tmFields = mModel.getTableFields();
		
		for(String fieldName : tmFields.keySet())
		{
			TableColumn<TableModel, String> column = new TableColumn<TableModel, String>(tmFields.get(fieldName));
			column.setCellValueFactory(new PropertyValueFactory<TableModel, String>(fieldName));
			tbView.getColumns().add(column);		
		}
		
		setData(null);
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
	
	public void filterTableView(CarLoanTO filterData)
	{
		if(filterData == null) {
			setData(null);
			return;
		}
		ObservableList<TableModel> actualList = tbView.getItems();
		FilteredList<TableModel> filteredList = new FilteredList<>(actualList);

		// to filter
		filteredList.setPredicate(
		    new Predicate<TableModel>(){
		        public boolean test(TableModel t){
		            return t.shouldBeFiltered(filterData);
		        }
		    }
		);
		
		tbView.setItems(filteredList);
	}
}
