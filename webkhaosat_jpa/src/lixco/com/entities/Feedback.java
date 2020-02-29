package lixco.com.entities;

public class Feedback {
	private String employeeName;
	private String department;
	private User_Result userResult;
	
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public User_Result getUserResult() {
		return userResult;
	}
	public void setUserResult(User_Result userResult) {
		this.userResult = userResult;
	}
}
