package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;

import org.junit.*;

import static org.junit.Assert.*;

public class AutoveicoloDAOTest 
{
	DAO<Autoveicolo> testDAO;
	
	@Before
	public void setUp() throws Exception {
		testDAO = AutoveicoloDAO.getInstance();	
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.restoreTable("Stato_Autoveicolo");
		TestUtil.restoreTable("Categoria");
		TestUtil.restoreTable("Sede");
		TestUtil.restoreTable("Autoveicolo");
	}
	
	@Test
	public void testCreate() throws Exception
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		
		Autoveicolo[] testCase = {null,
								  new Autoveicolo(),
								  new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
										  		  "AB123CX", "2007", "Manuale", 
										  		  "Climatizzatore, Airbag", 
										          testStato, testCategoria)};
		
		boolean[] expectedResult = {false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;
		
			try
			{
				testDAO.create(testCase[i]);
				
				Autoveicolo result = testDAO.read("1");

				iResult = testCase[i].equals(result);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("create(Autoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}	
	}
	
	@Test
	public void testCreate_1() throws Exception
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
									  		  "AB123CX", "2000", "Manuale", 
									  		  "Airbag", 
									          testStato, testCategoria);
		testDAO.create(testAutoveicolo);
		
		Autoveicolo testCase = new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
									  		  "AB123CX", "2007", "Manuale", 
									  		  "Climatizzatore, Airbag", 
									          testStato, testCategoria);
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
			assertNull("create(Autoveicolo) TC5:", testDAO.read("2"));
		}
	}
	
	@Test
	public void testUpdate() throws Exception
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
									  		  "AB123CX", "2000", "Manuale", 
									  		  "Airbag", 
									          testStato, testCategoria);
		testDAO.create(testAutoveicolo);
		
		Autoveicolo[] testCase = {null, 
								  new Autoveicolo(), 
								  new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
								  		  "AB123CX", "2007", "Manuale", 
								  		  "Climatizzatore, Airbag", 
								          testStato, testCategoria),
								  new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
										  "AB123CX", "2007", "Manuale", 
										  "Climatizzatore, Airbag", 
										  testStato, testCategoria)};

		testCase[2].setID("3");
		testCase[3].setID("1");

		boolean[] expectedResult = {false, false, false, true};

		for(int i = 0; i < testCase.length; i++)
		{
			boolean iResult = false;

			try
			{
				Autoveicolo sede = testCase[i];
				testDAO.update(sede);

				iResult = testDAO.read("1").equals(testCase[i]);
			}
			catch(IntegrationException e)
			{
				e.printStackTrace();
			}
			finally
			{
				assertEquals("update(Autoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	@Test
	public void testUpdate_1() throws Exception 
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);
		
		Autoveicolo testAutoveicolo1 = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
									  		  "AB123CX", "2000", "Manuale", 
									  		  "Airbag", 
									          testStato, testCategoria);
		testDAO.create(testAutoveicolo1);
		
		Autoveicolo testAutoveicolo2 = new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
										"DE723OP", "2007", "Manuale", 
										"Climatizzatore, Airbag", 
										testStato, testCategoria);
		testDAO.create(testAutoveicolo2);
		
		Autoveicolo testCase = new Autoveicolo("Fiat", "Punto", 1500, "5", "5", 
				"AB123CX", "2007", "Manuale", 
				"Climatizzatore, Navigatore satellitare", 
				testStato, testCategoria);
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
			assertNotEquals("update(Autoveicolo) TC5: ", testDAO.read("2"), testCase);
		}
	}	
	
	@Test
	public void testRead() throws Exception
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);

		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
												"AB123CX", "2000", "Manuale", 
												"Airbag", 
												testStato, testCategoria);
		testDAO.create(testAutoveicolo);

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
				Autoveicolo result = testDAO.read(testCase[i]);
				iResult = result != null && result.equals(testAutoveicolo);
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
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);

		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
												"AB123CX", "2000", "Manuale", 
												"Airbag", 
												testStato, testCategoria);
		testDAO.create(testAutoveicolo);
		
		List<Autoveicolo> autoveicoli = testDAO.readAll(null);

		assertTrue(autoveicoli.contains(testAutoveicolo));
	}
	
	@Test
	public void testReadAll_2() throws Exception
	{
		List<Autoveicolo> autoveicoli = testDAO.readAll(null);
		
		assertTrue(autoveicoli.isEmpty());
	}

	@Test
	public void testDelete() throws Exception
	{
		Sede testSede = new Sede("Andria", "Via Colbacco, 23", "0883447852");
		testSede.setID("1");
		SedeDAO.getInstance().create(testSede);
		
		StatoAutoveicolo testStato = new StatoAutoveicolo(25000, "Disponibile", 
										"Graffi sulla carrozzeria", testSede);
		testStato.setID("1");
		StatoAutoveicoloDAO.getInstance().create(testStato);

		Categoria testCategoria = new Categoria("Catorci", "Macchine messe davvero male", 
												0.05f, 1500, "Manuale");
		testCategoria.setID("1");
		CategoriaDAO.getInstance().create(testCategoria);

		Autoveicolo testAutoveicolo = new Autoveicolo("BMW", "Vecchia", 1200, "5", "5", 
												"AB123CX", "2000", "Manuale", 
												"Airbag", 
												testStato, testCategoria);
		testDAO.create(testAutoveicolo);

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
				e.printStackTrace();
			}
			finally
			{
				assertEquals("delete(Autoveicolo) TC" + (i + 1) + ": ", expectedResult[i], iResult);
			}
		}
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(AutoveicoloDAOTest.class);
	}
}