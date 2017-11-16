package integration.daos;

import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import business.businessObjects.Cliente;

import org.junit.*;

import static org.junit.Assert.*;

public class ClienteDAOTest {
	DAO<Cliente> testDAO;

	@Before
	public void setUp() throws Exception 
	{
		testDAO = ClienteDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception 
	{
		TestUtil.restoreTable("Cliente");
	}

	@Test
	public void testCreate() throws Exception
	{
		Cliente[] testCase = {null,
							  new Cliente(),
							  new Cliente("Antonio", "Pasqualini", "0883445672", 
									      "NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
									      "Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")))};
		
		boolean[] expectedResult = {false,
									false,
									true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				testDAO.create(testCase[i]);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Cliente) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testCreate_1() throws Exception
	{
		Cliente testCliente = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "NTNPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		testDAO.create(testCliente); 
		
		Cliente testCase = new Cliente("Antonio", "Pasqualini", "0411578492", 
				  "NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sforza, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		
		try
		{
			testDAO.create(testCase);
		}
		catch(IntegrationException e)
		{
			e.printStackTrace();
		}
		finally
		{
			assertNull("create(Cliente) TC4: ", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		Cliente testCliente = new Cliente("Antonio", "Pasqualini", "0411578492", 
		      							  "NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
		      							  "Venezia", "Via Sforza, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		testDAO.create(testCliente);
		
		Cliente[] testCase = new Cliente[4];
		testCase[0] = null;
		testCase[1] = new Cliente();
		testCase[2] = new Cliente("Antonio", "Pasqualini", "0883445672", 
						      	"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
						      	"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		testCase[2].setID("3");
		testCase[3] = new Cliente("Antonio", "Pasqualini", "0883445672", 
			      				"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
			      				"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		testCase[3].setID("1");
		
		boolean[] expectedResult = {false, false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				testDAO.update(testCase[i]);
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Categoria) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
		
	}
	
	@Test
	public void testUpdate_1() throws Exception
	{
		Cliente testCliente1 = new Cliente("Alberto", "Pasquale", "0414758216", 
				  "LBRPSQ55U58A285P", LocalDate.parse("11-01-1963", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
				  "Venezia", "Via Sberla, 121", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		testDAO.create(testCliente1); 
		
		Cliente testCliente2 = new Cliente("Antonio", "Pasqualini", "0883445672", 
  				"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
  				"Termoli", "Via Francia, 87", "B1", "AB8452PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));		
		testDAO.create(testCliente2);
		
		Cliente testCase =  new Cliente("Antonio", "Pasqualini", "0883445672", 
  				"LBRPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
  				"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));

		
		try
		{
			testDAO.update(testCase);
		}
		catch(IntegrationException e)
		{
			e.printStackTrace();
		}
		finally
		{
			assertNotEquals("update(Cliente) TC5: ", testDAO.read("2"), testCase);
		}
	}
	
	@Test
	public void testRead() throws Exception
	{
		Cliente testCliente = new Cliente("Antonio", "Pasqualini", "0883445672", 
  				"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
  				"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));		
		testDAO.create(testCliente);
  				
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
				Cliente result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testCliente);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testReadAll_1() throws Exception
	{
		Cliente testCliente = new Cliente("Antonio", "Pasqualini", "0883445672", 
  				"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
  				"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));		
		testDAO.create(testCliente);
	
		List<Cliente> clienti = testDAO.readAll(null);

		assertTrue(clienti.contains(testCliente));
	}
	
	@Test
	public void testReadAll_2() throws Exception
	{
		List<Cliente> clienti = testDAO.readAll(null);
		
		assertTrue(clienti.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception
	{
		Cliente testCliente = new Cliente("Antonio", "Pasqualini", "0883445672", 
  				"NTNPSQ55U58A285P", LocalDate.parse("09-11-1973", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
  				"Termoli", "Via Francia, 87", "B1", "AB75145PO", LocalDate.parse("18-11-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")));		
		testDAO.create(testCliente);

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
				e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(ClienteDAOTest.class);
	}
}