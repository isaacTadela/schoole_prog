package enums;

public enum UserType {
	 system_manager   (1),
	 secretary   (2),
	 school_manager   (3),
	 teacher  (4),
	 student   (5),
	 parent (6),
	 employee (7);
	
	private int typeId;
	UserType(int typeId) {
        this.typeId = typeId;
    }
	public int getTypeId() {
		return typeId;
	}
	public static UserType getById(int id) {
	    for(UserType e : values()) {
	        if(e.getTypeId() == id) return e;
	    }
	    return null;
	 }
}
