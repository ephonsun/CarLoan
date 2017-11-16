/**
 * 
 */
package presentation.boundary;

import transferObject.CarLoanTO;

/**
 * @author Amhal
 *
 */
public interface IBoundary 
{
	public CarLoanTO getData();
	public void setData(CarLoanTO data);
	public void setEditable(boolean value);
	public void show();
	public void hide();
	void Init(CarLoanTO data);
}
