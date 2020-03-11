package lixco.com.beans.entity;

public class DepartmentByLocate {
	private long id;
	private String locateName;
	private String departmentCode;
	private String departmentName;
	private long departmentId;
	
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLocateName() {
		return locateName;
	}
	public void setLocateName(String locateName) {
		this.locateName = locateName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
}
