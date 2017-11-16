package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.List;

import transferObject.CarLoanTO;

public interface DAO<Entity>
{
	public void create(Entity entity) throws IntegrationException;
	public void update(Entity entity) throws IntegrationException;
	public Entity read(String ID) throws IntegrationException;
	public List<Entity> readAll(CarLoanTO parameters) throws IntegrationException;
	public void delete(String ID) throws IntegrationException;
}
