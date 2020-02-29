package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class SettingUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long surveyId;
	private Survey surveyPlaying;
	private Department[] allDepartment;
	private List<Department> departments;
	private List<Member> employeesBySurvey;
	private Member employeeDeleted;
	private List<Member> selectedDelete;

	@Override
	protected void initItem() {
		long begin = System.currentTimeMillis();
		try {
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departments = castToArrayList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		selectedDelete = new ArrayList<>();
		employeeDeleted = new Member();
		surveyId = getParamSetOfId();
		employeesBySurvey = new ArrayList<>();
		employeesBySurvey = handleListuserBySurvey();
		System.out.println(System.currentTimeMillis() - begin);
		System.out.println("cc");
	}

// Cast array-> arrayList
	public List<Department> castToArrayList(Department[] departArray) {
		List<Department> depatmentsTemp = new ArrayList<>();
		for (Department d : departArray) {
			depatmentsTemp.add(d);
		}
		return depatmentsTemp;
	}

	public List<String> splitStringMediaIdsToList(String strMediaIds) {
		if ("".equals(strMediaIds)) {
			return new ArrayList<>();
		}
		return Stream.of(strMediaIds.split(",")).map(x -> x).collect(Collectors.toList());
	}

// List user ban dau
	public List<Member> handleListuserBySurvey() {
		List<Member> employeeBySur = new ArrayList<>();
		
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		// target Nhan vien
		
		String[] parts = surveyPlaying.getListUserOrDeparments().split(",");
//		List<String> parts = splitStringMediaIdsToList(surveyPlaying.getListUserOrDeparments());
		
		// Them nguoi dung thuoc bo cau hoi vao danh sach
		for (String i : parts) {
			Member memberTemp;
			try {
				System.out.println("start: ");
				long begin = System.currentTimeMillis();
				memberTemp = EMPLOYEE_SERVICE.findByCode(i);
				System.out.println(System.currentTimeMillis() - begin);
				System.out.println("end: ");
				employeeBySur.add(memberTemp);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return employeeBySur;
	}

// Xoa Employee 
	public void deleteEmployees() {
		Survey surveyTemp = SURVEY_SERVICE.findById(surveyId);
		String listUser = surveyTemp.getListUserOrDeparments();
		for (Member m : selectedDelete) {
			String idEmployee = "," + m.getCode() + ",";
			listUser = listUser.replaceAll(idEmployee, ",");
		}
		surveyTemp.setListUserOrDeparments(listUser);
		SURVEY_SERVICE.update(surveyTemp);
		employeesBySurvey = handleListuserBySurvey();
		notifyDeleteSuccess();
	}

//	GET AND SET

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public List<Member> getSelectedDelete() {
		return selectedDelete;
	}

	public void setSelectedDelete(List<Member> selectedDelete) {
		this.selectedDelete = selectedDelete;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public Department[] getAllDepartment() {
		return allDepartment;
	}

	public void setAllDepartment(Department[] allDepartment) {
		this.allDepartment = allDepartment;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Member> getEmployeesBySurvey() {
		return employeesBySurvey;
	}

	public void setEmployeesBySurvey(List<Member> employeesBySurvey) {
		this.employeesBySurvey = employeesBySurvey;
	}

	public Member getEmployeeDeleted() {
		return employeeDeleted;
	}

	public void setEmployeeDeleted(Member employeeDeleted) {
		this.employeeDeleted = employeeDeleted;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
}
