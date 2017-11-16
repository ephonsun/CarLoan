package presentation.boundary.tableModels;
import java.util.Map;
import transferObject.CarLoanTO;

public interface TableModel 
{
	public Map<String, String> getTableFields();
	public String getId();
	public TableModel instantiate(Object entity);
	public boolean shouldBeFiltered(CarLoanTO parameters);
}
