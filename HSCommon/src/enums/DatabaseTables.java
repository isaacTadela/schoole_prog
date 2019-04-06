package enums;

public enum DatabaseTables {
	USER("user"),
	COURSE("course"),
	ACADEMIC_UNIT("academic_unit"),
	TEACHER_ACADEMIC_UNIT("teacher_academic_unit"),
	TEACHER("teacher"),
	PARENT("parent"),
	STUDENT("student"),
	SEMESTER("semester"),
	STUDENT_COURSES("student_in_course_in_class"),
	COURSE_IN_CLASS("course_in_class"),
	TASK("task"),
	STUDENT_IN_COURSE_IN_CLASS("student_in_course_in_class"),
	SUBMISSION("submission"),
	COURSE_PREDESESSORS("course_pre"),
	CLASS("class"),
	REQUEST("request");

	private String table;
	DatabaseTables(String table) {
       this.table = table;
   }
	public String getTable() {
		return table;
	}
	
}
