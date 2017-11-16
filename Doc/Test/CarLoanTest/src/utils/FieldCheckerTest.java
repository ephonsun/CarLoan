package utils;

import java.util.Arrays;

import org.junit.*;

import static org.junit.Assert.*;

public class FieldCheckerTest {

	@Test
	public void testIsValidName() throws Exception {
		String[] testCase = {"AA", "AAAAAAAAAAAAAAA", 
						 	 null, "A", "AAAAAAAAAAAAAAAA"};
		boolean[] expectedResult = {true, true, false, false, false};
		
		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidName(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidName(testCase[i]));		
	}
	
	@Test
	public void testIsValidTaxCode() throws Exception {
		String[] testCase = {"RSSMRA80A01H501U", null, 
			 	 			 "AAAAAAAAAAAAAAAA"};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidTaxCode(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidTaxCode(testCase[i]));
	}
	
	@Test
	public void testIsValidTelephoneNumber() throws Exception {
		String[] testCase = {"0803795148", null, 
							 "AFE345LOC0"};
		boolean[] expectedResult = {true, false, false};
		
		for(int i = 0; i < testCase.length; i++)
		assertEquals("isValidTelephoneNumber(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidTelephoneNumber((testCase[i])));
	}
	
	@Test
	public void testIsValidLicencePlate() throws Exception {
		String[] testCase = {"BA782PO", null, 
							 "ABCD123"};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidLicencePlate(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidLicencePlate(testCase[i]));
	}
	
	@Test
	public void testIsValidLicenceNumber() throws Exception {
		String[] testCase = {"BA782845O", null, 
							 "ABCD123"};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidLicenceNumber(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidLicenceNumber(testCase[i]));
	}
	
	@Test
	public void testIsValidDescription() throws Exception {
		char[] testCase2 = new char[200];
		Arrays.fill(testCase2, 'A');
		char[] testCase4 = new char[201];
		Arrays.fill(testCase4, 'A');
		
		String[] testCase = {"A", new String(testCase2), 
				null, "", new String(testCase4)};
		boolean[] expectedResult = {true, true, false, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidDescription(String) TC" + (i + 1) + ": ", 
						expectedResult[i], FieldChecker.isValidDescription(testCase[i]));		
	}
	
	@Test
	public void testIsValidAddress() throws Exception {
		char[] testCase2 = new char[30];
		Arrays.fill(testCase2, 'A');
		char[] testCase4 = new char[31];
		Arrays.fill(testCase4, 'A');
		
		String[] testCase = {"A", new String(testCase2), 
				null, "", new String(testCase4)};
		boolean[] expectedResult = {true, true, false, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidAddress(String) TC" + (i + 1) + ": ", 
						expectedResult[i], FieldChecker.isValidAddress(testCase[i]));
	}
	
	@Test
	public void testIsValidPrice() throws Exception {
		String[] testCase = {"20.00", "-2.0A", 
							 null};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidPrice(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidPrice(testCase[i]));
	}
	
	@Test
	public void testIsInteger() throws Exception {
		String[] testCase = {"5", "-2A", null};
		boolean[] expectedResult = {true, false, false};
		
		for(int i = 0; i < testCase.length; i++)
			assertEquals("isInteger(String) TC" + (i + 1) + ": ", expectedResult[i], FieldChecker.isInteger(testCase[i]));
	}
	
	@Test
	public void testIsOptionalNameValid() throws Exception {
		String[] testCase = {null, 
							 "A", 
							 "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
							 "AA", 
							 "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"};
		
		boolean[] expectedResult = {false, false, false, true, true};
		
		for(int i = 0; i < testCase.length; i++)
			assertEquals("isInteger(String) TC" + (i + 1) + ": ", expectedResult[i], FieldChecker.isOptionalNameValid(testCase[i]));
	}

	@Test
	public void testIsValidUsername() throws Exception {
		String[] testCase = {"root.admin", "rootadmin?", 
								null};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidUsername(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidUsername(testCase[i]));
	}
	
	@Test
	public void testIsValidPassword() throws Exception {
		String[] testCase = {"root.admin", "rootadmin?", 
								null};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidPassword(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidPassword(testCase[i]));
	}
	
	@Test
	public void testIsValidField() throws Exception {
		String[] testCase = {"03-08-1994", "", 
				null};
		boolean[] expectedResult = {true, false, false};

		for(int i = 0; i < testCase.length; i++)
			assertEquals("isValidField(String) TC" + (i + 1) + ": ", 
					expectedResult[i], FieldChecker.isValidField(testCase[i]));
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(FieldCheckerTest.class);
	}
}