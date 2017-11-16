package transferObject.parameters;

public enum EntityOperation 
{
	VIEW("read"),
	LIST("list"),
	ADD("create"),
	REMOVE("delete"),
	EDIT("update"); 
	
	private String serviceName;

	EntityOperation(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
