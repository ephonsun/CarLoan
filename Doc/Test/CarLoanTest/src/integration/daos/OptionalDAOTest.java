package integration.daos;

import integration.exceptions.IntegrationException;
import java.util.List;

import business.businessObjects.Optional;

import org.junit.*;

import static org.junit.Assert.*;

public class OptionalDAOTest {
	DAO<Optional> testDAO;

	@Before
	public void setUp()
		throws Exception {
		testDAO = OptionalDAO.getInstance();
	}

	@After
	public void tearDown()
		throws Exception {
		TestUtil.restoreTable("Optional");
	}
	
	@Test
	public void testCreate() throws Exception
	{		
		String[] names = {null, 
						  "Seggiolino per bimbi"};
		
		String[] descriptions = {null, 
								 "Seggiolino per poter viaggiare col "
								 + "tuo bimbo in sicurezza"};
		
		Float[] prices = {null,
						  5.0f};
		
		boolean[] expectedResult = {false, 
									false, 
									true}; 
		
		Optional[] testCase = {null,
							   new Optional(),
							   new Optional(names[1], descriptions[1], prices[1]),
							  };
		
		// I controlli sui campi vengono gestiti in test separati,
		// in particolare nel test della classe FieldChecker
		// In questo test quindi prendiamo in considerazione solo
		// valori corretti e nulli
		for(int i = 0; i < testCase.length; i++)
		{
			// Risultato ottenuto in questo test
			boolean iResult = false;
			try 
			{
				Optional optional = testCase[i];
				
				testDAO.create(optional);
				
				// L'inserimento non ha sollevato eccezioni, 
				// quindi è andato a buon fine
				iResult = true;
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		// Inserimento di un Optional per testare
		testDAO.create(new Optional("Seggiolino per bimbi",
									"Seggiolino per poter viaggiare col tuo bimbo in sicurezza",
									5.0f));
		
		String[] id = {null, 
					   "3", 
					   "1"};
		
		String[] names = {null, 
		  				  "Seggiolino per bimbi"};

		String[] descriptions = {null, 
				 				 "Seggiolino per poter viaggiare col "
				 				 + "tuo bimbo in sicurezza"};

		Float[] prices = {null,
						  5.0f};
				
		Optional[] testCase = new Optional[4];
		testCase[0] = null;
		testCase[1] = new Optional();
		testCase[2] = new Optional(names[1], descriptions[1], prices[1]);
		testCase[2].setID(id[1]);
		testCase[3] = new Optional(names[1], descriptions[1], prices[1]);
		testCase[3].setID(id[2]);

		boolean[] expectedResult = {false, 
									false, 
									false,
									true}; 

		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			try 
			{
				Optional optional = testCase[i];
				
				testDAO.update(optional);
				
				// La modifica non ha sollevato eccezioni, 
				// quindi è andato a buon fine
				iResult = true;
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testRead() throws Exception
	{
		Optional testOptional = new Optional("Seggiolino per bimbi",
											 "Seggiolino per poter viaggiare col tuo bimbo in sicurezza",
											 5.0f);
		testDAO.create(testOptional);
		testOptional.setID("1");
		
		String[] testCase = {null, 
					       	"3", 
					       	"ABC",
							"1"};
		
		boolean[] expectedResult = {false, 
									false, 
									false,
									true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;

			try 
			{
				Optional opt = testDAO.read(testCase[i]);
				iResult = opt != null && opt.equals(testOptional);
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	// Test per ReadAll in presenza di dati
	@Test
	public void testReadAll_1() throws Exception
	{
		Optional testOptional = new Optional("Seggiolino per bimbi",
											 "Seggiolino per poter viaggiare col tuo bimbo in sicurezza",
											 5.0f);
		testOptional.setID("1");
		
		testDAO.create(testOptional);
		
		List<Optional> optionals = testDAO.readAll(null);
		
		assertTrue(optionals.contains(testOptional));
	}

	// Test per ReadAll in assenza di dati
	@Test
	public void testReadAll_2() throws Exception
	{
		List<Optional> optionals = testDAO.readAll(null);
		
		assertTrue(optionals.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception
	{
		Optional testOptional = new Optional("Seggiolino per bimbi",
				 "Seggiolino per poter viaggiare col tuo bimbo in sicurezza",
				 5.0f);
		testDAO.create(testOptional);
		testOptional.setID("1");

		String[] testCase = {null, 
							 "3", 
							 "ABC",
							 "1"};

		boolean[] expectedResult = {false, 
									false, 
									false,
									true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;

			try 
			{
				testDAO.delete(testCase[i]);
				
				iResult = testDAO.read(testCase[i]) == null;;
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(OptionalDAOTest.class);
	}
}