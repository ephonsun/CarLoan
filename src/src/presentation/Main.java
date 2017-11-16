package presentation;
import java.io.File;
import java.io.IOException;

import presentation.controllers.FCFactory;
import presentation.controllers.FCRequestType;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application 
{
	@Override
	public void start(Stage stage) 
	{
//
//		
//		
//		//String m = "src/res";
//		try 
//		{
//			String m = Main.class.getClassLoader().getResource("res").getPath();	
//			System.out.println(m);
//			m = m.replace("!", "");
//			Process p =  Runtime.getRuntime().exec("cmd /c start init.bat", null, 
//					new File(m));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
		
		FCFactory.GetController().processRequest(FCRequestType.SHOW_LOGIN_WINDOW);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
