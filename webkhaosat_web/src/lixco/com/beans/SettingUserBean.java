package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lixco.com.beans.entity.DepartmentByLocate;
import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;

@ManagedBean
@ViewScoped
public class SettingUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long surveyId;
	private Survey surveyPlaying;
	private Department[] allDepartment;
	private List<Department> departments;
	private List<Member> selectedDelete;
	private List<DepartmentByLocate> departmentsByLocate;
	private List<DepartmentByLocate> departmentsSelected;
	private static ObjectMapper mapper = new ObjectMapper();
	private List<Member> employeesBySurList;
	private List<Member> membersSelected;
	private Member[] employeesBySur;
	private Member autocompleteEmployee;
	private String txt1;

	private List<EmployeeDTO> employeesDTO;
	private EmployeeDTO[] employeesDTOArray;
	private List<EmployeeDTO> selectedsDelete;
	private EmployeeDTO employeeDTOSelected;
	private EmployeeDTO[] employeeBySurDTO;
	private List<EmployeeDTO> employeeBySurListDTO;
	private Set<EmployeeDTO> employeeBySurSetDTO;
//	private Set<Department> departmentList;
	private List<Department> departmentList;
	EmployeeServicePublic EMPLOYEE_SERVICEPUBLIC_DTO;

	@Override
	protected void initItem() {
		EMPLOYEE_SERVICEPUBLIC_DTO = new EmployeeServicePublicProxy();
		employeesDTO = new ArrayList<EmployeeDTO>();
		try {
			employeesDTOArray = EMPLOYEE_SERVICEPUBLIC_DTO.findAll();
			employeesDTO = new ArrayList<EmployeeDTO>(Arrays.asList(employeesDTOArray));
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		employeeDTOSelected = new EmployeeDTO();
		autocompleteEmployee = new Member();
//		departmentList = new HashSet<Department>();
		employeeBySurSetDTO = new HashSet<EmployeeDTO>();
		departments = new ArrayList<>();

		try {
//			allDepartment = DEPARTMENT_SERVICE.findAll();
//			departmentsByLocate = filterDepartmentByLocate(allDepartment);

			// chay chinh ne
//			Department[] deps = DEPARTMENT_SERVICE.getAllDepartSubByParent("10001");
//			for (int i = 0; i < deps.length; i++) {
//				if (deps[i].getLevelDep().getLevel() <= 2) {
//					departments.add(deps[i]);
//					Department[] departments = DEPARTMENT_SERVICE.getAllDepartSubByParent(deps[i].getCode());
//					List<String> codeDeps = new ArrayList<String>();
//					if (departments != null)
//						for (int j = 0; j < departments.length; j++) {
//							codeDeps.add(departments[j].getCode());
//						}
//					if (deps[i].getLevelDep().getLevel() == 2) {
//						codeDeps.add(deps[i].getCode());
//						departmentList.add(deps[i]);
//						String[] codearr = codeDeps.stream().toArray(String[]::new);
//						EmployeeDTO[] employeeDTOs = EMPLOYEE_SERVICEPUBLIC_DTO.findByDep(codearr);
//						if (employeeDTOs != null)
//							for (int j = 0; j < employeeDTOs.length; j++) {
//								employeeDTOs[j].setCodeDepart(deps[i].getCode());
//								employeeDTOs[j].setNameDepart(deps[i].getName());
//								this.employeesDTO.add(employeeDTOs[j]);
//							}
//					}
//				}
//			}
			// end

			// test department new
			// Test department New
			departmentList = new ArrayList<Department>();
			Department[] deps = DEPARTMENT_SERVICE.findAll();
			for (int i = 0; i < deps.length; i++) {
				if (deps[i].getLevelDep() != null)
					if (deps[i].getLevelDep().getLevel() > 1)
						departmentList.add(deps[i]);
			}

			// end test
//			if (departments.size() != 0) {
//				departments = DepartmentUtil.sort(departments);
			if (departmentList.size() != 0) {
				departmentList = DepartmentUtil.sort(departmentList);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		selectedDelete = new ArrayList<>();
		selectedsDelete = new ArrayList<>();
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		try {
//			if (StringUtils.isEmpty(surveyPlaying.getUsersJson())) {
//				employeesBySurList = new ArrayList<>();
//			} else {
//				employeesBySur = mapper.readValue(surveyPlaying.getUsersJson(), Member[].class);
//				employeesBySurList = new ArrayList<>();
//				employeesBySurList = Arrays.asList(employeesBySur);
//			}
			if (StringUtils.isEmpty(surveyPlaying.getUsersJson())) {
				employeeBySurListDTO = new ArrayList<EmployeeDTO>();
			} else {
				employeeBySurDTO = mapper.readValue(surveyPlaying.getUsersJson(), EmployeeDTO[].class);
				employeeBySurListDTO = new ArrayList<>();
				employeeBySurListDTO = Arrays.asList(employeeBySurDTO);
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
			}
			List<Member> listTemp = employeesBySurList;
			employeesNewArrayL = Stream.concat(employeesNewArrayL.stream(), listTemp.stream()).distinct()
					.collect(Collectors.toList());
			this.employeesBySurList = employeesNewArrayL;
			departmentsSelected = new ArrayList<>();
			PrimeFaces.current().executeScript("PF('dialogAddEmployee').hide();");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdate() throws JsonParseException, JsonMappingException, IOException {
		// convert JSON to Array objects
		// Member[] employeesOld = mapper.readValue(surveyPlaying.getUsersJson(),
		// Member[].class);

		// convert Array object to JSON
//		String a = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.employeesBySurList);

		String a = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.employeeBySurListDTO);
		surveyPlaying.setUsersJson(a);
		SURVEY_SERVICE.update(surveyPlaying);
		notifyUpdateSuccess();
	}

	public void autocompleteSelected() {
		if (!autocompleteEmployee.isDisable()) {
			List<Member> listTemp = new ArrayList<>();
			listTemp.add(autocompleteEmployee);
			listTemp = Stream.concat(listTemp.stream(), this.employeesBySurList.stream()).distinct()
					.collect(Collectors.toList());
			this.employeesBySurList = listTemp;
		}
		autocompleteEmployee = new Member();
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
//		Map<Long, Member> maps = new HashMap<Long, Member>();
//		for (Member i : employeesBySurList) {
//			maps.put(i.getId(), i);
//		}
//		for (Member m : selectedDelete) {
//			if (maps.containsKey(m.getId())) {
//				maps.remove(m.getId());
//			}
//		}

		Map<Long, EmployeeDTO> maps = new HashMap<Long, EmployeeDTO>();
		for (EmployeeDTO i : employeeBySurListDTO) {
			maps.put(i.getId(), i);
		}
		for (EmployeeDTO m : selectedsDelete) {
			if (maps.containsKey(m.getId())) {
				maps.remove(m.getId());
			}
		}

		this.employeeBySurListDTO = maps.values().stream().collect(Collectors.toList());
	}

	public List<Member> completeTest(String query) throws RemoteException {
		Member[] result = EMPLOYEE_SERVICE.findSearch(query, new String[] { "name" });
		// test
		// end test
		if (result == null) {
			return new ArrayList<>();
		} else {
			List<Member> ahihi = Arrays.asList(result);
			return ahihi;
		}
	}

	// new
	public List<Department> getDepartmentList() {
		List<Department> results = new ArrayList<>(departmentList);
		Collections.sort(results, new Comparator<Department>() {
			public int compare(Department o1, Department o2) {
				try {
					int rs = o1.getDepartment().getCode().compareTo(o2.getDepartment().getCode());
					if (rs == 0) {
						return o1.getCode().compareTo(o2.getCode());
					} else {
						return rs;
					}
				} catch (Exception e) {
					return -1;
				}
			}
		});
		return results;
	}

	public void selectDep() throws RemoteException {
		List<String> departList = new ArrayList<>();
		for (int i = 0; i < departmentList.size(); i++) {
			if (departmentList.get(i).isSelect()) {
				departmentList.get(i).setSelect(false);
				departList.add(departmentList.get(i).getCode());
			}
		}
		String[] departmentCodeArray = departList.toArray(new String[departList.size()]);
		EmployeeDTO[] employeesByDep = EMPLOYEE_SERVICEPUBLIC_DTO.findByDep(departmentCodeArray);

		List<EmployeeDTO> tempList = new ArrayList<EmployeeDTO>(Arrays.asList(employeesByDep));
		List<EmployeeDTO> usrAll1 = new ArrayList<>();
		// them list hien tai dang co vao usrAll1
		usrAll1.addAll(employeeBySurListDTO);
		for (int j = 0; j < tempList.size(); j++) {
//			// kiem tra trong list da co employee do chua
			boolean check = false;
			for (int l = 0; l < employeeBySurListDTO.size(); l++) {
				if (employeeBySurListDTO.get(l).getCode().equals(tempList.get(j).getCode())) {
					check = true;
					break;
				}
			}
			if (check == false) {
				usrAll1.add(tempList.get(j));
			}
		}
		employeeBySurListDTO = new ArrayList<>();
		employeeBySurListDTO.addAll(usrAll1);

//		for (Department dep : departmentList) {
//			if (dep.isSelect()) {
//				for (int i = 0; i < employeesDTO.size(); i++) {
//					try {
//						if (dep.getCode().equals(employeesDTO.get(i).getCodeDepart())) {
//							employeeBySurSetDTO.add(employeesDTO.get(i));
//
//							// convert hash set to arraylist
//							List<EmployeeDTO> usrAll = new ArrayList<EmployeeDTO>(employeeBySurSetDTO);
//							List<EmployeeDTO> usrAll1 = new ArrayList<>();
//							usrAll1.addAll(employeeBySurListDTO);
//							// add to list employee by survey
//							for (int j = 0; j < usrAll.size(); j++) {
//								// kiem tra trong list da co employee do chua
//								boolean check = false;
//								for (int l = 0; l < employeeBySurListDTO.size(); l++) {
//									if (employeeBySurListDTO.get(l).getCode().equals(usrAll.get(j).getCode())) {
//										check = true;
//										break;
//									}
//								}
//								if (check == false) {
//									usrAll1.add(usrAll.get(j));
//								}
//							}
//							employeeBySurListDTO = new ArrayList<>();
//							employeeBySurListDTO.addAll(usrAll1);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
	}

	public void ajaxEmp() {
		try {
			List<EmployeeDTO> listTemp = new ArrayList<>();
			listTemp.add(employeeDTOSelected);
			listTemp = Stream.concat(listTemp.stream(), this.employeeBySurListDTO.stream()).distinct()
					.collect(Collectors.toList());
			this.employeeBySurListDTO = listTemp;
			employeeDTOSelected = new EmployeeDTO();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String converViToEn(String s) {
		try {
			String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			String result = pattern.matcher(temp).replaceAll("");
			return pattern.matcher(result).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
		} catch (Exception e) {
			return "";
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<EmployeeDTO> completeEmployee(final String containedStr)
			throws NamingException, ClassNotFoundException {
		List<EmployeeDTO> linkedList = new LinkedList<EmployeeDTO>();
		String searchText = converViToEn(containedStr.toLowerCase());
		for (int i = 0; i < employeesDTO.size(); i++) {
			if (converViToEn(employeesDTO.get(i).getCode()).toLowerCase().contains(searchText)
					|| converViToEn(employeesDTO.get(i).getName()).toLowerCase().contains(searchText))
				linkedList.add(employeesDTO.get(i));
		}
		return linkedList;
	}

	// end new
	// GET AND SET

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

	public List<Member> getSelectedDelete() {
		return selectedDelete;
	}

	public void setSelectedDelete(List<Member> selectedDelete) {
		this.selectedDelete = selectedDelete;
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

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public List<EmployeeDTO> getEmployeesDTO() {
		return employeesDTO;
	}

	public void setEmployeesDTO(List<EmployeeDTO> employeesDTO) {
		this.employeesDTO = employeesDTO;
	}

	public EmployeeDTO getEmployeeDTOSelected() {
		return employeeDTOSelected;
	}

	public void setEmployeeDTOSelected(EmployeeDTO employeeDTOSelected) {
		this.employeeDTOSelected = employeeDTOSelected;
	}

	public EmployeeDTO[] getEmployeeBySurDTO() {
		return employeeBySurDTO;
	}

	public void setEmployeeBySurDTO(EmployeeDTO[] employeeBySurDTO) {
		this.employeeBySurDTO = employeeBySurDTO;
	}

	public List<EmployeeDTO> getEmployeeBySurListDTO() {
		return employeeBySurListDTO;
	}

	public void setEmployeeBySurListDTO(List<EmployeeDTO> employeeBySurListDTO) {
		this.employeeBySurListDTO = employeeBySurListDTO;
	}

	public List<EmployeeDTO> getSelectedsDelete() {
		return selectedsDelete;
	}

	public void setSelectedsDelete(List<EmployeeDTO> selectedsDelete) {
		this.selectedsDelete = selectedsDelete;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
}
