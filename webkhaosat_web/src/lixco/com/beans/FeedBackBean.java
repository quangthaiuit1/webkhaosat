package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;

import lixco.com.entities.Feedback;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class FeedBackBean extends AbstractBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<User_Result> feedbacksTemp;
	private List<User_Result> feedbacksAfterFilter;
	private List<Department> departments;
	private Department[] departmentsArray;
	private long surveyId;
	private List<Feedback> feedbacks;
	
	@Override
	protected void initItem() {
		surveyId = getParamSetOfId();
		feedbacksTemp = USER_RESULT_SERVICE.find(surveyId, 1, null,null);
		feedbacks = createListFeedback(feedbacksTemp);
		try {
			departmentsArray = DEPARTMENT_SERVICE.findAll();
			departments = Arrays.asList(departmentsArray);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
//Tao list feedback
	public List<Feedback> createListFeedback(List<User_Result> feedbacks) {
		List<Feedback> listFeedbackTemp = new ArrayList<Feedback>();
		for(User_Result u : feedbacks) {
			Feedback feed = new Feedback();
			feed.setUserResult(u);
			try {
				Member m = EMPLOYEE_SERVICE.findByCode(u.getEmployeeCode());
				feed.setEmployeeName(m.getName());
				feed.setDepartment(m.getDepartment().getName());
				listFeedbackTemp.add(feed);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		}
		return listFeedbackTemp;
	}

	public long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(long surveyId) {
		this.surveyId = surveyId;
	}

	public List<User_Result> getFeedbacksAfterFilter() {
		return feedbacksAfterFilter;
	}

	public void setFeedbacksAfterFilter(List<User_Result> feedbacksAfterFilter) {
		this.feedbacksAfterFilter = feedbacksAfterFilter;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public Department[] getDepartmentsArray() {
		return departmentsArray;
	}

	public List<User_Result> getFeedbacksTemp() {
		return feedbacksTemp;
	}

	public void setFeedbacksTemp(List<User_Result> feedbacksTemp) {
		this.feedbacksTemp = feedbacksTemp;
	}

	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public void setDepartmentsArray(Department[] departmentsArray) {
		this.departmentsArray = departmentsArray;
	}


	@Override
	protected Logger getLogger() {
		return null;
	}
}
