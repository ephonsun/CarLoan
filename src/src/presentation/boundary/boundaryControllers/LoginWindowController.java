package presentation.boundary.boundaryControllers;

import java.net.URL;
import java.util.ResourceBundle;

import presentation.boundary.IBoundary;
import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import transferObject.CarLoanTO;
import transferObject.parameters.BoundaryParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.LoginParameters;
import javafx.stage.Stage;

public class LoginWindowController implements Initializable, IBoundary
{
    @FXML
    private PasswordField txPassword;

    @FXML
    private TextField txUsername;

    @FXML
    private Button bLogin;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
				CarLoanTO data = new CarLoanTO();
				data.put(LoginParameters.USERNAME, txUsername.getText());
				data.put(LoginParameters.PASSWORD, txPassword.getText());

				CarLoanTO result = FCFactory.GetController().processRequest(FCRequestType.LOGIN, data);
				if(result.containsParameter(ServiceParameters.HAS_ERROR))
					return;
				
				CarLoanTO boundaryData = FCFactory.GetController().processRequest(FCRequestType.SHOW_MAIN_WINDOW);
				((IBoundary)boundaryData.get(BoundaryParameters.CONTROLLER)).Init(null);
				hide();
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		((Stage)bLogin.getScene().getWindow()).close();
	}

	@Override
	public void Init(CarLoanTO data) {
		// TODO Auto-generated method stub
		
	}
}
