package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import lixco.com.entities.ManagerSurveyUser;
import lixco.com.entities.Survey;
import trong.lixco.com.account.servicepublics.Account;

@ManagedBean
@ViewScoped
public class QuestionUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Account accountLogin;

	private List<Survey> surveysByEmployeeCode;
	private List<ManagerSurveyUser> managerSurveyUser;

	@Override
	protected void initItem() {

		try {
			accountLogin = getSession();
			managerSurveyUser = getListSurvey(accountLogin);
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

//Get list survey
	public List<ManagerSurveyUser> getListSurvey(Account ac) throws Throwable {
		List<ManagerSurveyUser> managerSurveyUser = new ArrayList<>();
		List<Survey> surveyByEmployeeCodeTemp1 = SURVEY_SERVICE.find(ac.getMember().getCode(), null);
		// Xu ly hoan thanh, chua hoan thanh, het han
		for (Survey s : surveyByEmployeeCodeTemp1) {
			ManagerSurveyUser temp = new ManagerSurveyUser();
			temp.setSurvey(s);
			//chua het han
			if (checkSurveyExpired(s)) {
				// check neu list user hoan thanh: rong
				if (StringUtils.isEmpty(s.getListUserCompleted()) || s.getListUserCompleted() == null) {
					temp.setInCompleted(true);
				} else {
					if (s.getListUserCompleted().contains(accountLogin.getMember().getCode())) {
						temp.setCompleted(true);
					} else {
						temp.setInCompleted(true);
					}
				}
			} else {
				// ds hoan thanh rong
				if (StringUtils.isEmpty(s.getListUserCompleted()) || s.getListUserCompleted() == null) {
					temp.setExpired(true);
				} else {
					if (s.getListUserCompleted().contains(accountLogin.getMember().getCode())) {
						temp.setCompleted(true);
						temp.setExpired(true);
					} else {
						temp.setCompleted(false);
						temp.setExpired(true);
					}
				}
			}
			managerSurveyUser.add(temp);
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
}
