package presentation.boundary.tableModels;

public class TMFactory 
{
	private TMFactory() {}
	
	public static TableModel getTableModel(TableModelType tbType)
	{
		TableModel returnValue = null;
		
		switch(tbType)
		{
			case CLIENTE:
				returnValue = new TMCliente();
				break;
			case CONTRATTO:
				returnValue = new TMContratto();
				break;
			case AUTOVEICOLO:
				returnValue = new TMAutoveicolo();
				break;
			case IMPIEGATO:
				returnValue = new TMImpiegato();
				break;
			case SEDE:
				returnValue = new TMSede();
				break;
			case OPTIONAL:
				returnValue = new TMOptional();
				break;
			case TARIFFA:
				returnValue = new TMTariffa();
				break;
			case CATEGORIA:
				returnValue = new TMCategoria();
				break;
			case IMPIEGATO_SEDE:
				returnValue = new TMImpiegatoSede();
				break;
			case AUTOVEICOLO_CONTRATTO:
				returnValue = new TMAutoveicoloContratto();
				break;
			case CLIENTE_CONTRATTO:
				returnValue = new TMClienteContratto();
				break;
		default:
			break;
		}
		return returnValue;
	}
	
	public static TableModelType stringToTMType(String string)
	{
		TableModelType result = TableModelType.NONE;
		if(string.contains("Client"))
			result = TableModelType.CLIENTE;
		else if(string.contains("Auto"))
			result = TableModelType.AUTOVEICOLO;
		else if(string.contains("Contratt"))
			result = TableModelType.CONTRATTO;
		else if(string.contains("Impiegat"))
			result = TableModelType.IMPIEGATO;
		else if(string.contains("Sed"))
			result = TableModelType.SEDE;
		else if(string.contains("Optional"))
			result = TableModelType.OPTIONAL;
		else if(string.contains("Tariff"))
			result = TableModelType.TARIFFA;
		else if(string.contains("Fasc"))
			result = TableModelType.CATEGORIA;
		
		return result;
	}
}
