package lixco.com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_result")
public class User_Result extends AbstractEntities {

	private String result;

	@Column(name = "employee_code")
	private String employeeCode;
	
	@Column(name = "employee_Name")
	private String employeeName;
	
	@Column(name = "departmentName")
	private String departmentName;

	@OneToOne
	private Question question;

	public String getResult() {
		return result;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}


	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}


	public String getDepartmentName() {
		return departmentName;
	}


	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}


	public void setResult(String result) {
		this.result = result;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

}
