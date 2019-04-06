package enums;

public enum Status {
	pending (1),
	accepted (2),
	rejected (3);

	private int typeId;
	Status(int typeId) {
        this.typeId = typeId;
    }
	public int getTypeId() {
		return typeId;
	}
	public static Status getById(int id) {
	    for(Status e : values()) {
	        if(e.getTypeId() == id) return e;
	    }
	    return null;
	 }
}



	






	
	
	
