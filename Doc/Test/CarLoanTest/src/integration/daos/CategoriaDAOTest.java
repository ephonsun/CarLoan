package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.Categoria;

import org.junit.*;

import static org.junit.Assert.*;

public class CategoriaDAOTest {
	DAO<Categoria> testDAO;
	
	@Before
	public void setUp() throws Exception {
		testDAO = CategoriaDAO.getInstance();
	}

	@After
	public void tearDown()
		throws Exception {
		TestUtil.restoreTable("Categoria");
	}
	
	@Test
	public void testCreate() throws Exception
	{
		Categoria[] testCase = {null,
								new Categoria(),
								new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico")};
		
		boolean[] expectedResult = {false,
									false,
									true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			try
			{
				testDAO.create(testCase[i]);
				
				Categoria result = testDAO.read("1");
				
				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Categoria) TC" + (i + 1) + ": ", iResult, expectedResult[i]);
			}
		}
	}
	
	@Test
	public void testCreate_1() throws Exception
	{
		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 0.05f, 700, "Manuale");
		testDAO.create(testCategoria);

		Categoria testCase = new Categoria("Catorci", "Auto di lusso", 4.0f, 2500, "Automatico");
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
			assertNull("create(Categoria) TC4: ", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		Categoria testCategoria = new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico");
		testDAO.create(testCategoria);
		
		Categoria[] testCase = new Categoria[4];

		testCase[0] = null;
		testCase[1] = new Categoria();
		testCase[2] = new Categoria("Catorci", "Macchine messe davvero male", 0.05f, 700, "Manuale");
		testCase[2].setID("3");
		testCase[3] = new Categoria("Catorci", "Macchine messe davvero male", 0.05f, 700, "Manuale");
		testCase[3].setID("1");
		
		boolean[] expectedResult = {false, false, false, true};
		
		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
			
			try
			{
				Categoria categoria = testCase[i];
				testDAO.update(categoria);
				
				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Categoria) TC" + (i + 1) + ": ", iResult, expectedResult[i]);
			}
		}
	}
	
	@Test
	public void testUpdate_1() throws Exception
	{
		Categoria testCategoria1 = new Categoria("Catorci", "Macchine messe davvero male", 0.05f, 700, "Manuale");
		testDAO.create(testCategoria1);

		Categoria testCategoria2 = new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico");
		testDAO.create(testCategoria2);

		
		Categoria testCase = new Categoria("Catorci", "Auto di lusso", 4.0f, 2500, "Automatico");
		testCase.setID("2");
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
			assertNotEquals("update(Categoria) TC5: ", testDAO.read("2"), testCase);
		}
	}
	
	@Test
	public void testRead() throws Exception
	{
		Categoria testCategoria = new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico");
		testDAO.create(testCategoria);
		
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
				Categoria result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testCategoria);
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
	public void testReadAll_1() throws Exception
	{
		Categoria testCategoria = new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico");	
		testDAO.create(testCategoria);

		List<Categoria> categorie = testDAO.readAll(null);

		assertTrue(categorie.contains(testCategoria));
	}

	@Test
	public void testReadAll_2() throws Exception
	{
		List<Categoria> categorie = testDAO.readAll(null);
		
		assertTrue(categorie.isEmpty());
	}
	
	@Test
	public void testDelete() throws Exception
	{
		Categoria testCategoria = new Categoria("Lusso", "Auto di lusso", 4.0f, 2500, "Automatico");
		testDAO.create(testCategoria);

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
				
				iResult = testDAO.read(testCase[i]) == null;
			}
			catch(IntegrationException e)
			{
				//System.out.println(e.getMessage());
				//e.printStackTrace();
			}
			finally
			{
				assertEquals("Caso di test n° " + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(CategoriaDAOTest.class);
	}
}