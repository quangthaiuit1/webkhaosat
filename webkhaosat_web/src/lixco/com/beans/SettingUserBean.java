package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lixco.com.beans.entity.DepartmentByLocate;
import lixco.com.entities.Employee;
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
	private List<Member> selectedDelete;
	private List<Employee> employeeBySur;
	private List<Employee> employeeAfterFilter;
	private List<DepartmentByLocate> departmentsByLocate;
	private List<DepartmentByLocate> departmentsSelected;
	private static ObjectMapper mapper = new ObjectMapper();
	private List<Member> employeesBySurList;
	private List<Member> membersSelected;
	private List<Employee> test;
	private Member[] employeesBySur;
	private Member autocompleteEmployee;
	private List<Member> allEmployee;
	private String txt1;

	@Override
	protected void initItem() {
		autocompleteEmployee = new Member();
		try {
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departmentsByLocate = filterDepartmentByLocate(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		selectedDelete = new ArrayList<>();
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
//		employeeBySur = EMPLOYEE_SERVICE_THAI.find(null, surveyId);
		try {
			if (StringUtils.isEmpty(surveyPlaying.getUsersJson())) {
				employeesBySurList = new ArrayList<>();
			} else {
				employeesBySur = mapper.readValue(surveyPlaying.getUsersJson(), Member[].class);
				employeesBySurList = new ArrayList<>();
				employeesBySurList = Arrays.asList(employeesBySur);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
//Tao 

//Loc phong ban theo khu vuc
	public List<DepartmentByLocate> filterDepartmentByLocate(Department[] allDepartment) {
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

// Them nhan vien
	public void addEmployee() throws JsonParseException, JsonMappingException, IOException {

		try {
			List<Member> employeesNewArrayL = new ArrayList<>();
			for (DepartmentByLocate d : departmentsSelected) {
				Member[] employeesNew = EMPLOYEE_SERVICE.findByCodeDepart(d.getDepartmentCode());
				List<Member> temp = new ArrayList<>();
				for (Member m : employeesNew) {
					if (!m.isDisable()) {
						temp.add(m);
					}
				}
				employeesNewArrayL = Stream.concat(employeesNewArrayL.stream(), temp.stream()).distinct()
						.collect(Collectors.toList());
				System.out.println(employeesNewArrayL.size());
			}
			List<Member> listTemp = employeesBySurList;
			employeesNewArrayL = Stream.concat(employeesNewArrayL.stream(), listTemp.stream()).distinct()
					.collect(Collectors.toList());
			this.employeesBySurList = employeesNewArrayL;
			PrimeFaces.current().executeScript("PF('dialogAddEmployee').hide();");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdate() throws JsonParseException, JsonMappingException, IOException {
		// convert JSON to Array objects
		//Member[] employeesOld = mapper.readValue(surveyPlaying.getUsersJson(), Member[].class);
		
		// convert Array object to JSON
		String a = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.employeesBySurList);
		surveyPlaying.setUsersJson(a);
		SURVEY_SERVICE.update(surveyPlaying);
		notifyUpdateSuccess();
		System.out.println(employeesBySurList.size());
	}

//	public List<String> splitStringMediaIdsToList(String strMediaIds) {
//		if ("".equals(strMediaIds)) {
//			return new ArrayList<>();
//		}
//		return Stream.of(strMediaIds.split(",")).map(x -> x).collect(Collectors.toList());
//	}

// List user ban dau - > tach dau
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
		Map<Long, Member> maps = new HashMap<Long, Member>();
		for (Member i : employeesBySurList) {
			maps.put(i.getId(), i);
		}
		for (Member m : selectedDelete) {
			if (maps.containsKey(m.getId())) {
				maps.remove(m.getId());
			}
		}

		this.employeesBySurList = maps.values().stream().collect(Collectors.toList());
	}

	public List<Member> completeThemeContains(String query) throws RemoteException {
		String queryLowerCase = query.toLowerCase();
		Member[] temp = EMPLOYEE_SERVICE.findAll();
		List<Member> allThemes = new ArrayList<>();
		for (Member m : temp) {
			if (!m.isDisable()) {
				allThemes.add(m);
			}
		}
		return allThemes.stream().filter(t -> t.getName().toLowerCase().contains(queryLowerCase))
				.collect(Collectors.toList());
	}

	public List<Member> completeTest(String query) throws RemoteException {
		Member[] temp = EMPLOYEE_SERVICE.findSearch(query, null);
		System.out.println("Not Ok");
		return Arrays.asList(temp);
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

	public List<Member> getSelectedDelete() {
		return selectedDelete;
	}

	public void setSelectedDelete(List<Member> selectedDelete) {
		this.selectedDelete = selectedDelete;
	}

	public List<Employee> getEmployeeAfterFilter() {
		return employeeAfterFilter;
	}

	public void setEmployeeAfterFilter(List<Employee> employeeAfterFilter) {
		this.employeeAfterFilter = employeeAfterFilter;
	}

	public List<Employee> getTest() {
		return test;
	}

	public void setTest(List<Employee> test) {
		this.test = test;
	}

	public List<DepartmentByLocate> getDepartmentsByLocate() {
		return departmentsByLocate;
	}

	public void setDepartmentsByLocate(List<DepartmentByLocate> departmentsByLocate) {
		this.departmentsByLocate = departmentsByLocate;
	}

	public List<DepartmentByLocate> getDepartmentsSelected() {
		return departmentsSelected;
	}

	public void setDepartmentsSelected(List<DepartmentByLocate> departmentsSelected) {
		this.departmentsSelected = departmentsSelected;
	}

	public List<Member> getEmployeesBySurList() {
		return employeesBySurList;
	}

	public void setEmployeesBySurList(List<Member> employeesBySurList) {
		this.employeesBySurList = employeesBySurList;
	}

	public Member[] getEmployeesBySur() {
		return employeesBySur;
	}

	public void setEmployeesBySur(Member[] employeesBySur) {
		this.employeesBySur = employeesBySur;
	}

	public List<Member> getMembersSelected() {
		return membersSelected;
	}

	public void setMembersSelected(List<Member> membersSelected) {
		this.membersSelected = membersSelected;
	}

	public Member getAutocompleteEmployee() {
		return autocompleteEmployee;
	}

	public void setAutocompleteEmployee(Member autocompleteEmployee) {
		this.autocompleteEmployee = autocompleteEmployee;
	}

	public List<Member> getAllEmployee() {
		return allEmployee;
	}

	public void setAllEmployee(List<Member> allEmployee) {
		this.allEmployee = allEmployee;
	}

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
}
