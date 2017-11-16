package presentation.boundary.boundaryControllers.entityWindows;

import java.net.URL;
import java.util.ResourceBundle;

import business.businessObjects.Utente;
import presentation.SessionManager;
import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.UserParameters;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UserWindowController implements Initializable, IBoundary
{
	@FXML
    private Button bConferma;
    @FXML
    private HBox pButton;
    @FXML
    private TextField txUsername;
    @FXML
    private Label lb_EntityName;
    @FXML
    private PasswordField pfPassword;
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
    
    private IBoundary mainWindowBoundary;

    
	@Override
	public CarLoanTO getData() {
		CarLoanTO formData = new CarLoanTO();
		
		//int intID = Integer.parseInt(SessionManager.user.getID());
		formData.put(UserParameters.USER_ID, SessionManager.user.getID());
		formData.put(UserParameters.NAME, txNome.getText());
		formData.put(UserParameters.SURNAME, txCognome.getText());
		formData.put(UserParameters.BIRTHDATE, dtDataNascita.getValue().toString());
		formData.put(UserParameters.TAX_CODE, txCodiceFiscale.getText());
		formData.put(UserParameters.USERNAME, txUsername.getText());
		formData.put(UserParameters.PASSWORD, pfPassword.getText());		
		return formData;
	}
	@Override
	public void setData(CarLoanTO data) {
		if(data == null) return;
		
		Utente user = SessionManager.user;
		
		txNome.setText(user.getNome());
		txCognome.setText(user.getCognome());
		dtDataNascita.setValue(user.getDataNascita());
		txCodiceFiscale.setText(user.getCodiceFiscale());
		txUsername.setText(user.getUsername());
		pfPassword.setText(user.getPassword());
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
		((Stage)mainPane.getScene().getWindow()).close();
	}
	
	@Override
	public void Init(CarLoanTO data) {
		mainWindowBoundary = (IBoundary)data.get(BoundaryParameters.CONTROLLER);
		setData((CarLoanTO)data.get(GenericEntityParameters.ENTITY));
		setEditable(true);
		
		bConferma.pressedProperty().addListener((event) -> {conferma();});
		bAnnulla.pressedProperty().addListener((event) -> {annulla();});
		
		((Stage)mainPane.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  hide();
	          }
	      });
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
    
	private void conferma() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Apportare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		if(alert.getResult() != ButtonType.YES)
			return;

		CarLoanTO data = new CarLoanTO();
		data.put(GenericEntityParameters.ENTITY_OPERATION, EntityOperation.EDIT);
		data.put(GenericEntityParameters.ENTITY, getData());
		data.put(GenericEntityParameters.ENTITY_IDENTIFIER, SessionManager.user.getID());
		data.put(GenericEntityParameters.ENTITY_TYPE, "Utente");

		CarLoanTO result = FCFactory.GetController().processRequest(FCRequestType.EXECUTE_ENTITY_OPERATION, data);
		if(result.containsParameter(ServiceParameters.HAS_ERROR))
			return;

		SessionManager.user = new Utente(getData());
		mainWindowBoundary.setData(null);

		hide();
	}
	
	private void annulla() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Annullare le modifiche?", 
				ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		
		if(alert.getResult() == ButtonType.NO)
			return;
		
		hide();
	}
	
}

