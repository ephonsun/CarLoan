package business.businessObjects;

public enum UserPermission 
{
	WORKER(0),
	MANAGER(1),
	NONE(2);
	
    private int numVal;

	UserPermission(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
