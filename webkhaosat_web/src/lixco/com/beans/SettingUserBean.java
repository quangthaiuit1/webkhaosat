package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class SettingUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Member> listEmployeeBysurvey; // Danh sach nguoi dung theo bo cau hoi

	private List<Department> listDepartmentBysurvey;
	private List<Department> departmentArrayList;// cast tu array

	private Member employeeSelected;
	private Member employeeDeleted;
	private List<Member> employeesByDepartment;
	private List<Member> employeesTest;
	private List<Member> employeesAfterFilter;
	private Department[] allDepartment; // Toan bo phong ban
	private long setofId; // Bien hung ket qua get param setof
	private Survey surveyPlaying;

	@Override
	protected void initItem() {
		employeeDeleted = new Member();
		employeeSelected = new Member();
		employeesAfterFilter = new ArrayList<>();
		departmentArrayList = new ArrayList<>();

		try {
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departmentArrayList = castToArrayList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		setofId = getParamSetOfId();
		handleListuserBySetof();
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

// Cast array-> arrayList
	public List<Department> castToArrayList(Department[] departArray) {
		List<Department> depatmentsTemp = new ArrayList<>();
		for (Department d : departArray) {
			depatmentsTemp.add(d);
		}
		return depatmentsTemp;
	}

	// Phong ban duoc chon
	public void selectedDepartment() {
		System.out.println(employeeSelected.getName());
	}

// List user ban dau
	public void handleListuserBySetof() {
		try {
			listEmployeeBysurvey = new ArrayList<>();
			listDepartmentBysurvey = new ArrayList<>();
			surveyPlaying = SURVEY_SERVICE.findById(setofId);
			// target Nhan vien
			String[] parts = surveyPlaying.getListUserOrDeparments().split(",");
			// Them nguoi dung thuoc bo cau hoi vao danh sach
			for (int i = 0; i < parts.length; i++) {
				Member memberTemp = EMPLOYEE_SERVICE.findByCode(parts[i]);
				listEmployeeBysurvey.add(memberTemp);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

// Xoa Employee // Chua xong
	public void deleteEmployee() {
		System.out.println(employeeDeleted.getCode());
		String listUser = surveyPlaying.getListUserOrDeparments();
		String a = employeeDeleted.getCode() + ",";
		listUser = listUser.replace(a , ",");
		PrimeFaces.current().executeScript("PF('dialogDeleteUser').hide()");
		notifyDeleteSuccess();
	}

//	GET AND SET
	public List<Member> getListEmployeeBysurvey() {
		return listEmployeeBysurvey;
	}

	public void setListEmployeeBysurvey(List<Member> listEmployeeBysurvey) {
		this.listEmployeeBysurvey = listEmployeeBysurvey;
	}

	public List<Department> getListDepartmentBysurvey() {
		return listDepartmentBysurvey;
	}

	public void setListDepartmentBysurvey(List<Department> listDepartmentBysurvey) {
		this.listDepartmentBysurvey = listDepartmentBysurvey;
	}

	public Member getEmployeeSelected() {
		return employeeSelected;
	}

	public void setEmployeeSelected(Member employeeSelected) {
		this.employeeSelected = employeeSelected;
	}

	public List<Member> getEmployeesByDepartment() {
		return employeesByDepartment;
	}

	public void setEmployeesByDepartment(List<Member> employeesByDepartment) {
		this.employeesByDepartment = employeesByDepartment;
	}

	public List<Member> getEmployeesTest() {
		return employeesTest;
	}

	public void setEmployeesTest(List<Member> employeesTest) {
		this.employeesTest = employeesTest;
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

	public long getSetofId() {
		return setofId;
	}

	public void setSetofId(long setofId) {
		this.setofId = setofId;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public Member getEmployeeDeleted() {
		return employeeDeleted;
	}

	public void setEmployeeDeleted(Member employeeDeleted) {
		this.employeeDeleted = employeeDeleted;
	}

	public List<Department> getDepartmentArrayList() {
		return departmentArrayList;
	}

	public void setDepartmentArrayList(List<Department> departmentArrayList) {
		this.departmentArrayList = departmentArrayList;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

}
