package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	@SuppressWarnings("unchecked")
	public void addEmployee() {
		long begin = System.currentTimeMillis();
//		StringBuffer idEmployees = new StringBuffer();
		Survey surveyTemp = SURVEY_SERVICE.findById(surveyId);
//		if (StringUtils.isNotEmpty(surveyTemp.getListUserOrDeparments())
//				&& surveyTemp.getListUserOrDeparments() != null) {
//			idEmployees.append(surveyTemp.getListUserOrDeparments());
//		}
//		String str = idEmployees.toString();
		JSONArray employeeList = new JSONArray();
		for (Member m : listEmployeeNew) {
			
			JSONObject employeeDetails = new JSONObject();
			employeeDetails.put("code", m.getCode());
			if(m.getDepartment() != null) {
				employeeDetails.put("department", m.getDepartment().getName());
			}
			if(m.getDepartment() == null) {
				employeeDetails.put("department", "");
			}
			employeeDetails.put("name", m.getName());

			JSONObject employeeObject = new JSONObject();
			employeeObject.put("employee", employeeDetails);
			employeeList.add(employeeObject);
			
			
			// kiem tra trung
//			if (m.getCode() != null) {
//				if (!str.contains(m.getCode())) {
//					idEmployees.append(m.getCode());
//					idEmployees.append(",");
//				}
//			}
		}
		String test ='"' +  employeeList.toString() + '"';
		surveyTemp.setListEmployeesJson(test);
		System.out.println(employeeList.toString());
//		str = idEmployees.toString();
//		surveyTemp.setListUserOrDeparments(str);
		SURVEY_SERVICE.update(surveyTemp);
		notifyUpdateSuccess();
		listEmployeeNew = null;
		listEmployeeNew = new ArrayList<>();
		System.out.println(System.currentTimeMillis() - begin);
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
