package enums;

public enum SubjectType {
	assign (1),
	remove (2);
	private int typeId;
	SubjectType(int typeId) {
        this.typeId = typeId;
    }
	public int getTypeId() {
		return typeId;
	}
	public static SubjectType getById(int id) {
	    for(SubjectType e : values()) {
	        if(e.getTypeId() == id) return e;
	    }
	    return null;
	 }
}





	
	
	
