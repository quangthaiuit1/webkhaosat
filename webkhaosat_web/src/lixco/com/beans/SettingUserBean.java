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

import lixco.com.entities.Employee;
import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;

@ManagedBean
@ViewScoped
public class SettingUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long surveyId;
	private Survey surveyPlaying;
	private Department[] allDepartment;
	private List<Department> departments;
	private List<Employee> selectedDelete;
	private List<Employee> employeeBySur;
	private List<Employee> employeeAfterFilter;

	@Override
	protected void initItem() {
		employeeAfterFilter = new ArrayList<>();
		try {
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departments = castToArrayList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		selectedDelete = new ArrayList<>();
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		employeeBySur = EMPLOYEE_SERVICE_THAI.find(null, surveyId);
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
//	public List<Employee> handleListuserBySurvey(long surveyId) {
//		
//		// target Nhan vien
//		
////		String[] parts = surveyPlaying.getListUserOrDeparments().split(",");
//////		List<String> parts = splitStringMediaIdsToList(surveyPlaying.getListUserOrDeparments());
////		
////		// Them nguoi dung thuoc bo cau hoi vao danh sach
////		for (String i : parts) {
////			Member memberTemp;
////			try {
////				System.out.println("start: ");
////				long begin = System.currentTimeMillis();
////				memberTemp = EMPLOYEE_SERVICE.findByCode(i);
////				System.out.println(System.currentTimeMillis() - begin);
////				System.out.println("end: ");
////				employeeBySur.add(memberTemp);
////			} catch (RemoteException e) {
////				e.printStackTrace();
////			}
////		}
//		return employeeBySur;
//	}

// Xoa Employee 
	public void deleteEmployees() {
		if(!selectedDelete.isEmpty()) {
			for(Employee e : selectedDelete) {
				EMPLOYEE_SERVICE_THAI.delete(e);
			}
		}
		selectedDelete = new ArrayList<>();
		employeeBySur = EMPLOYEE_SERVICE_THAI.find(null, surveyId);
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

	public List<Employee> getEmployeeBySur() {
		return employeeBySur;
	}

	public void setEmployeeBySur(List<Employee> employeeBySur) {
		this.employeeBySur = employeeBySur;
	}
	
	public List<Employee> getSelectedDelete() {
		return selectedDelete;
	}

	public void setSelectedDelete(List<Employee> selectedDelete) {
		this.selectedDelete = selectedDelete;
	}

	public List<Employee> getEmployeeAfterFilter() {
		return employeeAfterFilter;
	}

	public void setEmployeeAfterFilter(List<Employee> employeeAfterFilter) {
		this.employeeAfterFilter = employeeAfterFilter;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
}
