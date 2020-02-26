package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

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
	private List<Member> employeesAfterFilter;
	private Department[] allDepartment; // Toan bo phong ban
	private List<Department> departmentArrayList;// cast tu array
	private long setofId; // Bien hung ket qua get param setof
	private Survey surveyPlaying;

	@Override
	protected void initItem() {
		listEmployeeNew = new ArrayList<>();
		try {
			surveyPlaying = SURVEY_SERVICE.findById(setofId);
			employees = EMPLOYEE_SERVICE.findAll();
			employeesArrayList = Arrays.asList(employees);
//			employeesAfterFilter = new ArrayList<>();
//			employeesAfterFilter = employeesArrayList;

			allDepartment = DEPARTMENT_SERVICE.findAll();
			departmentArrayList = Arrays.asList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		setofId = getParamSetOfId();
	}

	// Get param from URL
	public long getParamSetOfId() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String setofIdTemp = request.getParameter("setofid");
			return Long.parseLong(setofIdTemp);
		} catch (Exception e) {
			return 0l;
		}
	}

	// Them nhan vien
	public void addEmployee() {
		StringBuffer idEmployees = new StringBuffer();
		Survey surveyTemp = SURVEY_SERVICE.findById(setofId);
		if (StringUtils.isNotEmpty(surveyTemp.getListUserOrDeparments()) && surveyTemp.getListUserOrDeparments() != null) {
			idEmployees.append(surveyTemp.getListUserOrDeparments());	
		}
		for (Member m : listEmployeeNew) {
			String str = idEmployees.toString();
			// kiem tra trung
			if (!str.contains(m.getCode())) {
				idEmployees.append(m.getCode());
				idEmployees.append(",");
			}
		}
		String str = idEmployees.toString();
		surveyTemp.setListUserOrDeparments(str);
		SURVEY_SERVICE.update(surveyTemp);
		notifyUpdateSuccess();
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

	public List<Member> getEmployeesAfterFilter() {
		return employeesAfterFilter;
	}

	public void setEmployeesAfterFilter(List<Member> employeesAfterFilter) {
		this.employeesAfterFilter = employeesAfterFilter;
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

	public long getSetofId() {
		return setofId;
	}

	public void setSetofId(long setofId) {
		this.setofId = setofId;
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
