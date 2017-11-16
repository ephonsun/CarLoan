package business.exceptions;

public class LoginException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public LoginException(String message) {
		super(message);
	}
}
