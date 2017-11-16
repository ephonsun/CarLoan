package presentation.boundary.boundaryControllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import business.businessObjects.UserPermission;
import presentation.SessionManager;
import presentation.boundary.IBoundary;
import presentation.boundary.tableModels.TMFactory;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainWindowController implements Initializable, IBoundary 
{
    @FXML
    private Button bUtenteModifica;
    @FXML
    private Label lbUtenteCollegato;
    @FXML
    private Button bUtenteLogOut;

    @FXML
    private TabPane mainTabPane;
	@FXML
    private Tab TabSedi;
    @FXML
    private Tab TabBusiness;
    @FXML
    private Tab TabClienti;
    @FXML
    private Tab TabImpiegati;
    @FXML
    private Tab TabContratti;
    @FXML
    private Tab TabAutoveicoli;
    
    private Map<Tab, IBoundary> tabs;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		// TODO: Close tabs according to the user permissions
		tabs = new HashMap<Tab, IBoundary>();
		
		mainTabPane.getSelectionModel().clearSelection();
		mainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> arg0, Tab arg1,
					Tab arg2) {
				if(arg2.getContent() == null)
				{
					CarLoanTO data = new CarLoanTO();
					data.put(BoundaryParameters.TAB_NAME, arg2.getText());
					FCRequestType request = FCRequestType.SHOW_GENERIC_TAB;
					
					if(arg2.getText().contains("Business"))
						request = FCRequestType.SHOW_BUSINESS_TAB;
					
					CarLoanTO boundaryData = new CarLoanTO();
					boundaryData = FCFactory.GetController().processRequest(request, data);
					Parent root = (Parent)boundaryData.get(BoundaryParameters.WINDOW_OBJECT);
					IBoundary tabController = (IBoundary)boundaryData.get(BoundaryParameters.CONTROLLER);
					
					arg2.setContent(root);
					
					if(request != FCRequestType.SHOW_BUSINESS_TAB)
					{
						CarLoanTO tabParameters = new CarLoanTO();
						String tabTitle = arg2.getText();
						tabParameters.put(BoundaryParameters.TAB_TITLE, tabTitle);
						tabParameters.put(GenericEntityParameters.ENTITY_TABLE_MODEL, TMFactory.getTableModel(TMFactory.stringToTMType(tabTitle)));
						tabController.Init(tabParameters);
					}
					
					tabs.put(arg2, tabController);
				}
				else
				{
					tabs.get(arg2).setData(null);
				}
			}
		});
		
		// LOG OUT
		bUtenteLogOut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FCFactory.GetController().processRequest(FCRequestType.LOGOUT);
				hide();
			}
		});
		
		bUtenteModifica.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				CarLoanTO data = new CarLoanTO();
				data.put(GenericEntityParameters.ENTITY_IDENTIFIER, SessionManager.user.getID());
				data.put(GenericEntityParameters.ENTITY_TYPE, "Utente");
				data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.EDIT);
				data.put(BoundaryParameters.CONTROLLER, MainWindowController.this);

				FCFactory.GetController().processRequest(FCRequestType.SHOW_ENTITY_WINDOW, data);
			}
		});
	}

	@Override
	public CarLoanTO getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(CarLoanTO data) {
		String name = SessionManager.user.getNome() + " " + SessionManager.user.getCognome();
		lbUtenteCollegato.setText(name);
	}

	@Override
	public void setEditable(boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		((Stage)mainTabPane.getScene().getWindow()).close();
	}

	@Override
	public void Init(CarLoanTO data) {
		
		setData(null);
		UserPermission userPermission = SessionManager.user.getUserPermission();

		if(userPermission == UserPermission.MANAGER)
		{

		} 
		else if(userPermission == UserPermission.WORKER)
		{
			mainTabPane.getTabs().remove(TabSedi);
			mainTabPane.getTabs().remove(TabImpiegati);
		}
	}
}
