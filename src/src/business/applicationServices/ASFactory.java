package business.applicationServices;

public class ASFactory {
	
	private ASFactory() {}
	
	public static CarLoanAS getApplicationService() {
		return new CarLoanAS();
	}
}
