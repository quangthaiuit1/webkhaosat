package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;

import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class DetailSurveyByUser extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String employeeCode;
	private Member employeePlaying;
	private long surveyId;
	private List<User_Result> userResultByUser;

	@Override
	protected void initItem() {
		try {
			surveyId = getParamSetOfId();
			employeeCode = getParamUserCode();
			userResultByUser = USER_RESULT_SERVICE.find(surveyId, 0L, employeeCode,null);
			employeePlaying = EMPLOYEE_SERVICE.findByCode(employeeCode);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

// Get param from URL
	protected String getParamUserCode() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request.getParameter("employeeCode");
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public List<User_Result> getUserResultByUser() {
		return userResultByUser;
	}

	public void setUserResultByUser(List<User_Result> userResultByUser) {
		this.userResultByUser = userResultByUser;
	}

	public Member getEmployeePlaying() {
		return employeePlaying;
	}

	public void setEmployeePlaying(Member employeePlaying) {
		this.employeePlaying = employeePlaying;
	}
}
