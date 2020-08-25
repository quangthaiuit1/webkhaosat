package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lixco.com.entities.ManagerSurveyUser;
import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Account;

@ManagedBean
@ViewScoped
public class QuestionUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Account accountLogin;

	private List<Survey> surveysByEmployeeCode;
	private List<ManagerSurveyUser> managerSurveyUser;
	private List<ManagerSurveyUser> surveysIncomplete;
	private List<ManagerSurveyUser> surveysExpired;
	private List<ManagerSurveyUser> surveysInExpired;

	@Override
	protected void initItem() {
		
		try {
			trong.lixco.com.account.servicepublics.Member member = getAccount().getMember();
			surveysExpired = new ArrayList<>();
			surveysInExpired = new ArrayList<>();
			
			accountLogin = getSession();
			managerSurveyUser = getListSurvey(accountLogin);
			surveysIncomplete = getListSurveyIncomplete(managerSurveyUser);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

//Get session
	public Account getSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Account accountTemp = (Account) session.getAttribute("account");
		return accountTemp;
	}

//Get all survey
	public List<ManagerSurveyUser> getListSurvey(Account ac) throws Throwable {
		List<ManagerSurveyUser> managerSurveyUser = new ArrayList<>();
		List<Survey> allSurveyByEmployeeCode = SURVEY_SERVICE.find(accountLogin.getMember().getCode());
		// Xu ly hoan thanh, chua hoan thanh, het han
		
		for (Survey s : allSurveyByEmployeeCode) {
			ManagerSurveyUser temp = new ManagerSurveyUser();
			temp.setSurvey(s);
			//chua het han
			if (checkSurveyExpired(s)) {
				// da hoan thanh
				if(checkSurveyComplete(s.getId(),accountLogin.getMember().getCode())) {
					temp.setCompleted(true);
				}
				else {
					temp.setInCompleted(true);
				}
				//list toan bo survey chua het han cua user do
				surveysInExpired.add(temp);
			} else {
				temp.setExpired(true);
				if(checkSurveyComplete(s.getId(),accountLogin.getMember().getCode())) {
					temp.setCompleted(true);
				}
				else {
					temp.setInCompleted(true);;
				}
			}
			managerSurveyUser.add(temp);
		}
		return managerSurveyUser;
	}
//Get survey inComplete
	public List<ManagerSurveyUser> getListSurveyIncomplete(List<ManagerSurveyUser> all){
		List<ManagerSurveyUser> managerSurveyUser = new ArrayList<>();
		for(ManagerSurveyUser m : all) {
			if(m.isExpired() == false && m.isInCompleted() == true) {
				managerSurveyUser.add(m);
			}
		}
		return managerSurveyUser;
	}
//Chu y
// Kiem tra het han chua
	public boolean checkSurveyExpired(Survey setOf) throws Throwable {
		Date currentDate = getDate();
		if (currentDate.after(setOf.getStartDate()) && currentDate.before(setOf.getEndDate())) {
			return true;
		} else
			return false;
	}
//Kiem tra hoan thanh
	public boolean checkSurveyComplete(long surveyid, String employeeCode) {
		List<User_Result> check = USER_RESULT_SERVICE.find(surveyid, 0L, employeeCode,null);
		if(check.isEmpty()) {
			return false;
		}
		return true;
	}
// GET AND SET

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<ManagerSurveyUser> getManagerSurveyUser() {
		return managerSurveyUser;
	}

	public void setManagerSurveyUser(List<ManagerSurveyUser> managerSurveyUser) {
		this.managerSurveyUser = managerSurveyUser;
	}

	public static Account getAccountLogin() {
		return accountLogin;
	}

	public static void setAccountLogin(Account accountLogin) {
		QuestionUserBean.accountLogin = accountLogin;
	}

	public List<Survey> getSurveysByEmployeeCode() {
		return surveysByEmployeeCode;
	}

	public void setSurveysByEmployeeCode(List<Survey> surveysByEmployeeCode) {
		this.surveysByEmployeeCode = surveysByEmployeeCode;
	}

	public List<ManagerSurveyUser> getSurveysIncomplete() {
		return surveysIncomplete;
	}

	public void setSurveysIncomplete(List<ManagerSurveyUser> surveysIncomplete) {
		this.surveysIncomplete = surveysIncomplete;
	}

	public List<ManagerSurveyUser> getSurveysExpired() {
		return surveysExpired;
	}

	public void setSurveysExpired(List<ManagerSurveyUser> surveysExpired) {
		this.surveysExpired = surveysExpired;
	}

	public List<ManagerSurveyUser> getSurveysInExpired() {
		return surveysInExpired;
	}

	public void setSurveysInExpired(List<ManagerSurveyUser> surveysInExpired) {
		this.surveysInExpired = surveysInExpired;
	}
}
