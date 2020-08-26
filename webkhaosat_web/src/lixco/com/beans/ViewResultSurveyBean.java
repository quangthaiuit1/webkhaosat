package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import lixco.com.beans.entity.DepartmentByLocate;
import lixco.com.entities.Result;
import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.servicepublic.EmployeeDTO;

@ManagedBean
@ViewScoped
public class ViewResultSurveyBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Result> results;
	private List<Result> resultsAfterFilter;
	private List<User_Result> allUserResult;
	private Long surveyId;
	private Survey surveyPlaying;
	private List<Department> departments;
	private List<Department> departmentsAfterFilter;
	private DepartmentByLocate departmentSelected;
	private List<Member> employeesByDepartment;
	private List<DepartmentByLocate> departmentsByLocate1;

	@Override
	protected void initItem() {
		results = new ArrayList<>();
		employeesByDepartment = new ArrayList<>();
		departmentSelected = new DepartmentByLocate();
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		try {
			departments = Arrays.asList(DEPARTMENT_SERVICE.findAll());
			departmentsByLocate1 = filterDepartmentByLocate(departments);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

//Cast from User_Result to Result
	public List<DepartmentByLocate> filterDepartmentByLocate(List<Department> allDepartment) {
		List<DepartmentByLocate> departmentsTemp = new ArrayList<>();
		for (Department d : allDepartment) {
			if (d.getDepartment() != null) {
				DepartmentByLocate dT = new DepartmentByLocate();
				dT.setId(d.getId());
				dT.setDepartmentId(d.getDepartment().getId());
				dT.setDepartmentCode(d.getCode());
				dT.setDepartmentName(d.getName());
				if (d.getDepartment().getId() == 193) {
					dT.setLocateName("CHI NHÁNH BÌNH DƯƠNG");
					departmentsTemp.add(dT);
				}
				if (d.getDepartment().getId() == 194) {
					dT.setLocateName("CHI NHÁNH BẮC NINH");
					departmentsTemp.add(dT);
				}
				if (d.getDepartment().getId() == 191) {
					dT.setLocateName("TRỤ SỞ HỒ CHÍ MINH");
					departmentsTemp.add(dT);
				}
				// departmentsTemp.add(dT); khong de duoi nay duoc vi se bi trung danh sach
			}
		}
		return departmentsTemp;
	}

	public void departmentSelect() throws RemoteException {
		employeesByDepartment = new ArrayList<>();
		List<String> userResultTemp = USER_RESULT_SERVICE.findByDepartmentName(surveyId,
				departmentSelected.getDepartmentName());
		// cast userResultTemp to list string
		List<String> codeEmployees = new ArrayList<>();
		for (int i = 0; i < userResultTemp.size(); i++) {
			codeEmployees.add(userResultTemp.get(i));
			//create employee list
			Member employeeTemp = EMPLOYEE_SERVICE.findByCode(userResultTemp.get(i));
			employeesByDepartment.add(employeeTemp);
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

//GET AND SET
	public List<User_Result> getAllUserResult() {
		return allUserResult;
	}

	public void setAllUserResult(List<User_Result> allUserResult) {
		this.allUserResult = allUserResult;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public List<Result> getResultsAfterFilter() {
		return resultsAfterFilter;
	}

	public void setResultsAfterFilter(List<Result> resultsAfterFilter) {
		this.resultsAfterFilter = resultsAfterFilter;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Department> getDepartmentsAfterFilter() {
		return departmentsAfterFilter;
	}

	public void setDepartmentsAfterFilter(List<Department> departmentsAfterFilter) {
		this.departmentsAfterFilter = departmentsAfterFilter;
	}

	public DepartmentByLocate getDepartmentSelected() {
		return departmentSelected;
	}

	public void setDepartmentSelected(DepartmentByLocate departmentSelected) {
		this.departmentSelected = departmentSelected;
	}

	public List<Member> getEmployeesByDepartment() {
		return employeesByDepartment;
	}

	public void setEmployeesByDepartment(List<Member> employeesByDepartment) {
		this.employeesByDepartment = employeesByDepartment;
	}

	public List<DepartmentByLocate> getDepartmentsByLocate1() {
		return departmentsByLocate1;
	}

	public void setDepartmentsByLocate1(List<DepartmentByLocate> departmentsByLocate1) {
		this.departmentsByLocate1 = departmentsByLocate1;
	}
}
