package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import lixco.com.entities.Employee;
import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class AddUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Member[] employees; // Danh sach toan bo user
	private List<Member> employeesArrayList;
	private List<Member> listEmployeeNew;
	private Department[] allDepartment; // Toan bo phong ban
	private List<Department> departmentArrayList;// cast tu array
	private long surveyId; // Bien hung ket qua get param setof
	private Survey surveyPlaying;

	@Override
	protected void initItem() {
		listEmployeeNew = new ArrayList<>();
		try {
			surveyPlaying = SURVEY_SERVICE.findById(surveyId);
			employees = EMPLOYEE_SERVICE.findAll();
			employeesArrayList = Arrays.asList(employees);
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departmentArrayList = Arrays.asList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		surveyId = getParamSetOfId();
	}

	// Them nhan vien
	public void addEmployee() {
		Survey surveyTemp = SURVEY_SERVICE.findById(surveyId);
		long tempId = surveyTemp.getId();
		for (Member m : listEmployeeNew) {
			if (EMPLOYEE_SERVICE_THAI.find(m.getCode(), 0L).isEmpty()) {
				// kiem tra row rong
				if (m.getCode() != null) {
					Employee employee = new Employee();
					employee.setName(m.getName());
					if (m.getDepartment() != null) {
						employee.setDepartment(m.getDepartment().getName());
					}
					employee.setEmployeeCode(m.getCode());
					employee.setSurveyId(tempId);

					EMPLOYEE_SERVICE_THAI.create(employee);
				}
			}
		}
		notifyUpdateSuccess();
		listEmployeeNew = null;
		listEmployeeNew = new ArrayList<>();
	}

	// Phong ban duoc chon
	public void selectedDepartment() {

	}
//GET AND SET

	@Override
	protected Logger getLogger() {
		return null;
	}

	public Member[] getEmployees() {
		return employees;
	}

	public void setEmployees(Member[] employees) {
		this.employees = employees;
	}

	public List<Member> getListEmployeeNew() {
		return listEmployeeNew;
	}

	public void setListEmployeeNew(List<Member> listEmployeeNew) {
		this.listEmployeeNew = listEmployeeNew;
	}

	public Department[] getAllDepartment() {
		return allDepartment;
	}

	public void setAllDepartment(Department[] allDepartment) {
		this.allDepartment = allDepartment;
	}

	public List<Department> getDepartmentArrayList() {
		return departmentArrayList;
	}

	public void setDepartmentArrayList(List<Department> departmentArrayList) {
		this.departmentArrayList = departmentArrayList;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public List<Member> getEmployeesArrayList() {
		return employeesArrayList;
	}

	public void setEmployeesArrayList(List<Member> employeesArrayList) {
		this.employeesArrayList = employeesArrayList;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

}
