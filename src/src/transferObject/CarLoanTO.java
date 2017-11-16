package transferObject;

import java.util.HashMap;
import java.util.Map;

public class CarLoanTO
{
	Map<String, Object> parameters;
	
	public CarLoanTO()
	{
		parameters = new HashMap<String, Object>();
	}
	
	public void put(String name, Object value)
	{
		parameters.put(name, value);
	}
	
	public Object get(String parameterName) 
	{
		return parameters.get(parameterName);
	}
	
	public boolean containsParameter(String parameterName)
	{
		return parameters.containsKey(parameterName);
	}
}
