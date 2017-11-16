package presentation.boundary.boundaryControllers;

import java.net.URL;
import java.util.ResourceBundle;

import business.businessObjects.UserPermission;
import presentation.SessionManager;
import presentation.boundary.IBoundary;
import presentation.boundary.tableModels.TMFactory;
import presentation.boundary.tableModels.TableModelType;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.GenericEntityParameters;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BusinessTabController implements Initializable, IBoundary 
{
    @FXML
    private VBox vBox;
    
    @FXML
    private HBox hBox;

    @FXML
    private AnchorPane mainPane;
    
	@Override
	public CarLoanTO getData() {
		return null;
	}

	@Override
	public void setData(CarLoanTO data) {
	}

	@Override
	public void setEditable(boolean value) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		FCRequestType request = FCRequestType.SHOW_PANEL;
		CarLoanTO initParams;
		Parent panelRoot = null;
		IBoundary boundaryController;
		
		String windowName = "mainPanels/GenericPanel";
		
		if(SessionManager.user.getUserPermission() == UserPermission.WORKER)
			windowName = "mainPanels/MinimalPanel";
		
		CarLoanTO optionalPanelParameters = new CarLoanTO();
		optionalPanelParameters.put(BoundaryParameters.WINDOW_NAME, windowName);
		optionalPanelParameters.put(BoundaryParameters.IS_NESTED, true);
		CarLoanTO optionalPanelData = (CarLoanTO)FCFactory.GetController().processRequest(request, optionalPanelParameters);
		panelRoot = (Parent) optionalPanelData.get(BoundaryParameters.WINDOW_OBJECT);
		boundaryController = (IBoundary)optionalPanelData.get(BoundaryParameters.CONTROLLER);
		initParams = new CarLoanTO();
		initParams.put(BoundaryParameters.WINDOW_TITLE, "Optional");
		initParams.put(GenericEntityParameters.ENTITY_TABLE_MODEL, TMFactory.getTableModel(TableModelType.OPTIONAL));
		boundaryController.Init(initParams);
		hBox.getChildren().add(0, panelRoot);

		CarLoanTO fascePanelParameters = new CarLoanTO();
		fascePanelParameters.put(BoundaryParameters.WINDOW_NAME, windowName);
		fascePanelParameters.put(BoundaryParameters.IS_NESTED, true);
		CarLoanTO fascePanelData = (CarLoanTO)FCFactory.GetController().processRequest(request, fascePanelParameters);
		panelRoot = (Parent) fascePanelData.get(BoundaryParameters.WINDOW_OBJECT);
		boundaryController = (IBoundary)fascePanelData.get(BoundaryParameters.CONTROLLER);
		initParams = new CarLoanTO();
		initParams.put(BoundaryParameters.WINDOW_TITLE, "Categorie");
		initParams.put(GenericEntityParameters.ENTITY_TABLE_MODEL, TMFactory.getTableModel(TableModelType.CATEGORIA));
		boundaryController.Init(initParams);
		vBox.getChildren().add(panelRoot);

		CarLoanTO tariffePanelParameters = new CarLoanTO();
		tariffePanelParameters.put(BoundaryParameters.WINDOW_NAME, windowName);
		tariffePanelParameters.put(BoundaryParameters.IS_NESTED, true);
		CarLoanTO tariffePanelData = (CarLoanTO)FCFactory.GetController().processRequest(request, tariffePanelParameters);
		panelRoot = (Parent) tariffePanelData.get(BoundaryParameters.WINDOW_OBJECT);
		boundaryController = (IBoundary)tariffePanelData.get(BoundaryParameters.CONTROLLER);
		initParams = new CarLoanTO();
		initParams.put(BoundaryParameters.WINDOW_TITLE, "Tariffe");
		initParams.put(GenericEntityParameters.ENTITY_TABLE_MODEL, TMFactory.getTableModel(TableModelType.TARIFFA));
		boundaryController.Init(initParams);
		vBox.getChildren().add(panelRoot);
	}

	@Override
	public void Init(CarLoanTO data) {
	}
}
