package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lixco.com.beans.entity.DepartmentByLocate;
import lixco.com.beans.entity.EmployeeByDepartment;
import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class AddUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Member[] employees; // Danh sach toan bo user
	private List<Member> listEmployeeNew;
	private List<Member> employeesAfterFilter;// after - disble
	private Department[] allDepartment; // Toan bo phong ban
	private List<Department> departmentArrayList;// cast tu array
	private long surveyId; // Bien hung ket qua get param setof
	private Survey surveyPlaying;
	private List<Department> departmentsSelected;

	private static ObjectMapper mapper = new ObjectMapper();
	private List members = new ArrayList<>();
	private List<DepartmentByLocate> departmentsByLocate;

	// Test
	private List<EmployeeByDepartment> allEmployee;

	@Override
	protected void initItem() {
		listEmployeeNew = new ArrayList<>();
		employeesAfterFilter = new ArrayList<>();
		try {
			surveyPlaying = SURVEY_SERVICE.findById(surveyId);
//			employees = EMPLOYEE_SERVICE.findAll();
//			for (Member m : employees) {
//				if (m.isDisable() == false && m.getCode() != null) {
//					employeesAfterFilter.add(m);
//				}
//			}
			allDepartment = DEPARTMENT_SERVICE.findAll();
			departmentsByLocate = filterDepartmentByLocate(allDepartment);
//			departmentArrayList = Arrays.asList(allDepartment);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		surveyId = getParamSetOfId();
//		//Test
//		try {
//			Map<Department, List<Member>> result = employeesArrayList.stream()
//					.collect(Collectors.groupingBy(Member::getDepartment));
//			List<String> keySet = new ArrayList<>();
//			allEmployee = new ArrayList<>();
//			Iterator myIter = result.keySet().iterator();
//			int i = 0;
//			while (myIter.hasNext()) {
//				Department phongBan = (Department) myIter.next();
//				List<Member> list = result.get(phongBan);
//				Member em = new Member();
////				em.setName(list.get(i).getName());
////				em.setCode(list.get(i).getCode());
////				allEmployee.add(new EmployeeByDepartment(phongBan, em));
//				i += 1;
//			}
//			System.out.println(allEmployee);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
	}

//Loc phong ban theo khu vuc
	public List<DepartmentByLocate> filterDepartmentByLocate(Department[] allDepartment) {
		List<DepartmentByLocate> departmentsTemp = new ArrayList<>();
//		DepartmentByLocate departmentByLocateTempBD = new DepartmentByLocate();
//		DepartmentByLocate departmentByLocateTempBN = new DepartmentByLocate();
//		DepartmentByLocate departmentByLocateTempHCM = new DepartmentByLocate();
//		departmentByLocateTempBD.setLocateName("Chi nhánh Bình Dương");
//		List<Department> departmentsBD = new ArrayList<>();
//		departmentByLocateTempBN.setLocateName("Chi nhánh Bắc Ninh");
//		List<Department> departmentsBN = new ArrayList<>();
//		departmentByLocateTempHCM.setLocateName("Trụ sở Hồ Chí Minh");
//		List<Department> departmentsHCM = new ArrayList<>();
//		for (Department d : allDepartment) {
//			if (d.getDepartment() != null) {
//				if (d.getDepartment().getId() == 193) {
//					departmentsBD.add(d);
//				}
//				if (d.getDepartment().getId() == 194) {
//					departmentsBN.add(d);
//				}
//				if (d.getDepartment().getId() == 191) {
//					departmentsHCM.add(d);
//				}
//			}
//		}
//		departmentByLocateTempBD.setDepartments(departmentsBD);
//		departmentByLocateTempBN.setDepartments(departmentsBN);
//		departmentByLocateTempHCM.setDepartments(departmentsHCM);
//		departmentsTemp.add(departmentByLocateTempBD);
//		departmentsTemp.add(departmentByLocateTempBN);
//		departmentsTemp.add(departmentByLocateTempHCM);
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
				//departmentsTemp.add(dT); khong de duoi nay duoc vi se bi trung danh sach
			}
		}
		return departmentsTemp;
	}

// Them nhan vien
	public void addEmployee() {
		System.out.println(departmentsSelected);
		Survey surveyTemp = SURVEY_SERVICE.findById(surveyId);
		long tempId = surveyTemp.getId();
		String jsonText = "";
//		for (Member m : listEmployeeNew) {
//			// Check trung nhan vien
//			if (EMPLOYEE_SERVICE_THAI.find(m.getCode(), tempId).isEmpty()) {
//				// kiem tra row rong
//				if (m.getCode() != null) {
//					Employee employee = new Employee();
//					employee.setName(m.getName());
//					if (m.getDepartment() != null) {
//						employee.setDepartment(m.getDepartment().getName());
//					}
//					employee.setEmployeeCode(m.getCode());
//					employee.setSurveyId(tempId);
//
//					EMPLOYEE_SERVICE_THAI.create(employee);
//				}
//			}

		// start json object
//			JSONObject jObject = new JSONObject();
//
//			jObject.put("departmentName",m.getDepartment().getName());
//			jObject.put("employeeCode", m.getCode());
//			jObject.put("employeeName", m.getName());
//			jObject.put("surveyId", surveyId);
//			
//			//convert from JSONObject to JSON string
//			 jsonText = jsonText + jObject.toJSONString() + ".";
//
//			JSONParser parser = new JSONParser();
//			
//			//convert from JSON string to JSONObject
//			JSONObject newJObject = null;
//			try {
//				newJObject = (JSONObject) parser.parse(jsonText);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//
//			System.out.println(newJObject.get("departmentName"));
//			System.out.println(newJObject.get("employeeCode"));
//			System.out.println(newJObject.get("employeeName"));
//			System.out.println(newJObject.get("surveyId"));
		// End json object

		// 1.
//			EmployeeByDepartment em = new EmployeeByDepartment();
//			em.setDepartmentName(m.getDepartment().getName());
//			em.setEmployeeCode(m.getCode());
//			em.setSurveyId(surveyId);
//			em.setEmployeeName(m.getName());

		// 2. Java object to JSON string
//			jsonInString = jsonInString + "{" + "'departmentName'" + ":" + "'" + m.getDepartment().getName() + "," + "'" +  "employeeName'"  + ":" +  "'" + m.getName() + "'" + "," + "'employeeCode'"  + ":" +  "'" + m.getCode() + "'" + "," + "'surveyId'"  + ":" +  surveyId + "}" + ",";
//		}
		try {
			String a = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listEmployeeNew);
			surveyPlaying = SURVEY_SERVICE.findById(surveyId);
			surveyPlaying.setUsersJson(a);
			SURVEY_SERVICE.update(surveyPlaying);
			Survey surveyTemp2 = SURVEY_SERVICE.findById(surveyId);
			
			// 1. convert JSON array to Array objects
			Member[] pp1 = mapper.readValue(surveyTemp2.getUsersJson(), Member[].class);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public List<Member> getEmployeesAfterFilter() {
		return employeesAfterFilter;
	}

	public void setEmployeesAfterFilter(List<Member> employeesAfterFilter) {
		this.employeesAfterFilter = employeesAfterFilter;
	}

	public List getMembers() {
		return members;
	}

	public void setMembers(List members) {
		this.members = members;
	}

	public List<DepartmentByLocate> getDepartmentsByLocate() {
		return departmentsByLocate;
	}

	public void setDepartmentsByLocate(List<DepartmentByLocate> departmentsByLocate) {
		this.departmentsByLocate = departmentsByLocate;
	}

	public List<EmployeeByDepartment> getAllEmployee() {
		return allEmployee;
	}

	public void setAllEmployee(List<EmployeeByDepartment> allEmployee) {
		this.allEmployee = allEmployee;
	}

	public List<Department> getDepartmentsSelected() {
		return departmentsSelected;
	}

	public void setDepartmentsSelected(List<Department> departmentsSelected) {
		this.departmentsSelected = departmentsSelected;
	}
}