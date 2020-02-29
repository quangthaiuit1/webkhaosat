package lixco.com.entities;

public class Result {
	private User_Result userResult;
	private String employeeName;
	private String employeeDepartment;
	
	public User_Result getUserResult() {
		return userResult;
	}
	public void setUserResult(User_Result userResult) {
		this.userResult = userResult;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeDepartment() {
		return employeeDepartment;
	}
	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}
}
