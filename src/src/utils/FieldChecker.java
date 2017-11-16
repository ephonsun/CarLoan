package utils;

import java.util.regex.Pattern;

public class FieldChecker {
	private static final String nameExpression = ".{2,15}?";
	private static final String taxCodeExpression = "^([A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1})$";
	private static final String numberExpression = "\\d{10}";
	private static final String integerExpression = "\\d+";
	private static final String licencePlateExpression = "\\w{2}\\d{3}\\w{2}";
	private static final String licenceNumberExpression = "\\w{2}\\d{6}\\w{1}";
	private static final String priceExpression = "[0-9]+([,.][0-9]{1,2})?";
	private static final String optionalNameExpression = ".{2,30}?";
	private static final String usernameExpression = "^[a-zA-Z0-9._-]{8,15}$";
	private static final String passwordExpression = "^[a-zA-Z0-9._-]{8,15}$";
	
	public static boolean isValidName(String s)	{
	    return s != null && Pattern.matches(nameExpression, s);
	}
	
	public static boolean isValidTaxCode(String s) {
		return s != null && Pattern.matches(taxCodeExpression, s);
	}
	
	public static boolean isValidTelephoneNumber(String s) {
	    return s != null && Pattern.matches(numberExpression, s);
	}
	
	public static boolean isValidLicencePlate(String s)	{
		return s != null && Pattern.matches(licencePlateExpression, s);
	}
	
	public static boolean isValidLicenceNumber(String s) {
		return s != null && Pattern.matches(licenceNumberExpression, s);
	}
	
	public static boolean isValidDescription(String s) {
		return s != null && s.length() > 0 && s.length() <= 200;
	}
	
	public static boolean isValidAddress(String s) {
		return s != null && s.length() > 0 && s.length() <= 30;
	}
	
	public static boolean isValidPrice(String s) {
		return s != null && Pattern.matches(priceExpression, s);
	}
	
	public static boolean isInteger(String s) {
		return s != null && Pattern.matches(integerExpression, s);
	}

	public static boolean isValidUsername(String s) {
		return s != null && Pattern.matches(usernameExpression, s);
	}
	
	public static boolean isOptionalNameValid(String s) {
		return s != null && Pattern.matches(optionalNameExpression, s);
	}

	public static boolean isValidPassword(String s) {
		return s != null && Pattern.matches(passwordExpression, s);
	}
	
	public static boolean isValidField(String s) {
		return s != null && !s.isEmpty();
	}
}

