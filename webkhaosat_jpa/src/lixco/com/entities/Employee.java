package lixco.com.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "employee")
public class Employee extends AbstractEntities{
	private String name;
	@Column(name = "department")
	private String department;
	@Column(name = "employee_code")
	private String employeeCode;
	@Column(name = "survey_id")
	private long surveyId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String deparment) {
		this.department = deparment;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public long getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}
}
