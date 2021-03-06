package enums;

// second enumerator which indicates the in more precise where the request came from and which task/method asked for it.
public enum PacketSub {

	GENERIC_GET_USERS_TYPE(1,0),
	TEACHER_CONTROLLER_LIST_ENTITIES   (1,1),
	TEACHER_CONTROLLER_GET_ENTITY   (1,2),
	TEACHER_CONTROLLER_UPDATE_ENTITY   (1,3),
	TEACHER_CONTROLLER_UPDATE_LIST_ENTITY  (1,4),
	TEACHER_CONTROLLER_MY_COURSES  (1,5),
	GET_STUDENT_IN_TEACHER_COURSE (1,6),
	EVALUATE_STUDENT_IN_COURSE (1,7),
	EVALUATE_STUDENT_IN_TASK (1,8),
	
	LOGIN_CONTROLLER_GET_DATA (2,1),
	
	DEFINE_COURSE_GET_ACADEMIC_UNITS (3,1),
	DEFINE_COURSE_GET_COURSES (3,2),
	DEFINE_COURSE_SUBMIT (3,3),
	
	OPEN_NEW_SEMESTER(4,1),
	GET_LATEST_SEMESTER(4,2),
	GET_CHOSEN_SEMESTER(5,1),
	GET_STUDENT_IN_COURSE(6,1),
	SUBMIT_TASK_SOLUTION(7,1),
	GET_TASK_SOLUTION(7,2),
	GET_STUDENTS_FOR_PARENT(8,1),
	GET_STUDENT_IN_COURSE_TASKS(9,1),

	GET_CLASSES_NAMES(10,0),
	UPDATE_STUDENT_CLASS(10,2),	
	BLOCK_PARENT_ACCESS(10,1),
	UNBLOCK_PARENT_ACCESS(10,2),
	DEFINE_NEW_CLASS(11,0),
	DEFINE_TASK_GET_CLASSES(12,0),
	DEFINE_TASK_SUBMIT(11,2),

	ADD_NEW_STUDENT(11,1),
	CREATE_NEW_USER(11,2),
	
	GET_SCHOOL_CLASSES(10,1),
	GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER(11,1),
	GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT(12,1),
	ASSIGN_COURSE_TO_CLASS(13,1),
	ASSIGN_STUDENTS_IN_CLASS_TO_COURSE_ONLY_THOSE_WHO_HAVE_ALL_PRECOURSES_AND_GET_THEM(14,1),
	ASSIGN_TEACHER_TO_COURSE_IN_CLASS(15,1),
	ASSIGN_STUDENT_TO_COURSE_IN_CLASS(16,1),
	GET_STUDENTS_IN_COURSE_IN_CLASS(17,1),
	REMOVE_STUDENT_FROM_COURSE_IN_CLASS(18,1),
	DELETE_USER (1,2),
	
	GET_SCHOOL_TEACHERS(19,1),
	GET_ALL_SEMESTERS(20,1),
	GET_SPECIFIC_TEACHER_STATISTICS(21,1),
	GET_CLASS_STATISTICS_WITH_DIFFERENT_TEACHERS(22,1),
	GET_CLASS_STATISTICS_IN_DIFFERENT_COURSES(23,1),

	GENERIC_GET_SEMESTERS(10,1),
	GENERIC_GET_PARENTS(10,2),
	GENERIC_GET_STUDENTS(10,3),
	GENERIC_GET_TEACHERS(10,4),
	GENERIC_GET_COURSES(10,5),
	GENERIC_GET_CLASSES(10,6),
	GENERIC_GET_USERS(10,7),

	GENERIC_GET_COURSE_IN_CLASS(10,8),
	
	GENERIC_GET__COURSE_IN_CLASS__WITH_NAMES(15,1),
	DEFINE_TASK_GET_COURSE_IN_CLASS(15,0),


	GENERIC_GET_REQUESTS (10,9),
	CREATE_NEW_REQUEST(10,10),
	UPDATE_REQUESTS (10,11),
	REQUEST_LOGOUT (11,0);



	public int getControllerId() {
		return controllerId;
	}

	public void setControllerId(int controllerId) {
		this.controllerId = controllerId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	private int controllerId;
	private int taskId;

	PacketSub(int controllerId,int taskId) {
		this.controllerId = controllerId;
		this.taskId = taskId;
	}


}
