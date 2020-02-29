package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import lixco.com.entities.Result;
import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

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

	@Override
	protected void initItem() {
		results = new ArrayList<>();
		surveyId = getParamSetOfId();
		allUserResult = USER_RESULT_SERVICE.find(surveyId, 0l, null);
		try {
			results = castToResult(allUserResult);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		try {
			departments = Arrays.asList(DEPARTMENT_SERVICE.findAll());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

//Cast from User_Result to Result
	public List<Result> castToResult(List<User_Result> listUserResult) throws Throwable{
		List<Result> resultsTemp = new ArrayList<>();
		for(User_Result ur : listUserResult) {
			Member employeeTemp = EMPLOYEE_SERVICE.findByCode(ur.getEmployeeCode());
			Result r = new Result();
			r.setUserResult(ur);
			r.setEmployeeName(employeeTemp.getName());
			r.setEmployeeDepartment(employeeTemp.getDepartment().getName());
			resultsTemp.add(r);
		}
		return resultsTemp;
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
	
}
