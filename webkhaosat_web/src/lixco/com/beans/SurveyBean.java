package lixco.com.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;

@ManagedBean
@ViewScoped
public class SurveyBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

//Da hoan thanh	
	private Survey surveyNew;
	private List<Survey> surveys1; // Danh sach ky khao sat
	private String answerAddNew;
	private Survey surveySelected2; //// Hung gia tri bo cau hoi de xoa va sua

	private Survey surveySelected3; // hung gia tri bo cau hoi se dien ra
	// update and delete
	private Survey surveyUpdated;
	private Survey surveyDeleted;
	
	
	
	private List<User_Result> userResult1; // ket qua khao sat theo id bo khao sat
	private List<User_Result> userResult2; // List sau filter
	private List<User_Result> userResultTest; // test select user
	// CAC BIEN HUNG GIA TRI TAM THOI
	// Trong

	@Override
	public void initItem() {
		// khoi tao doi tuong hung gia tri khi khong co gia tri van khong bi loi
		surveySelected3 = new Survey();
		surveyNew = new Survey();
		surveyUpdated = new Survey();
		surveyDeleted = new Survey();
		//All ky khao sat
		surveys1 = SURVEY_SERVICE.findAllByFilter();
		//sort time desc
		Collections.sort(surveys1, new Comparator<Survey>()
	    {
	        @Override
	        public int compare(Survey d1, Survey d2)
	        {
	            return d2.getEndDate().compareTo(d1.getEndDate());//use the name specified in the pojo class for getting the date in the place of 'getdate'
	        }
	    });
	}


// BO THEM XOA SUA 

	// Tao ky khao sat
	public void createSurvey() {
		try {
			surveyNew.setDeleted(false);
			surveyNew.setCreatedDate(getDate());
			SURVEY_SERVICE.create(surveyNew);
			surveys1 = SURVEY_SERVICE.findAllByFilter();
			surveyNew = new Survey();
			notifyAddSuccess();
		} catch (Exception e) {
		}
	}

	// sua ky khao sat
	public void updatesurvey() {
		surveyUpdated.setModifiedDate(getDate());
		Date temp = surveyUpdated.getEndDate();
		System.out.println(temp);
		SURVEY_SERVICE.update(surveyUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateSetof').hide()");
		notifyUpdateSuccess();
	}

	// Xoa ky khao sat
	public void deleteSurvey() {
		SURVEY_SERVICE.delete(surveyDeleted);
		surveys1 = SURVEY_SERVICE.findAllByFilter();
		PrimeFaces.current().executeScript("PF('dialogDeleteSetof').hide()");
		notifyDeleteSuccess();
	}




	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
	}
	
// KET THUC THEM XOA SUA

	public List<Survey> completesSurvey(String input) {
		String queryLowerCase = input.toLowerCase();
		return surveys1.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
				.collect(Collectors.toList());
	}

	
//GET AND SET
	
	public Survey getSurveyNew() {
		return surveyNew;
	}

	public void setSurveyNew(Survey surveyNew) {
		this.surveyNew = surveyNew;
	}

	public List<Survey> getSurveys1() {
		return surveys1;
	}

	public void setSurveys1(List<Survey> surveys1) {
		this.surveys1 = surveys1;
	}

	public Survey getSurveySelected2() {
		return surveySelected2;
	}

	public void setSurveySelected2(Survey surveySelected2) {
		this.surveySelected2 = surveySelected2;
	}

	public Survey getSurveySelected3() {
		return surveySelected3;
	}

	public void setSurveySelected3(Survey surveySelected3) {
		this.surveySelected3 = surveySelected3;
	}

	public Survey getSurveyUpdated() {
		return surveyUpdated;
	}

	public void setSurveyUpdated(Survey surveyUpdated) {
		this.surveyUpdated = surveyUpdated;
	}

	public Survey getSurveyDeleted() {
		return surveyDeleted;
	}

	public void setSurveyDeleted(Survey surveyDeleted) {
		this.surveyDeleted = surveyDeleted;
	}

	public List<User_Result> getUserResult1() {
		return userResult1;
	}

	public void setUserResult1(List<User_Result> userResult1) {
		this.userResult1 = userResult1;
	}

	public String getAnswerAddNew() {
		return answerAddNew;
	}

	public void setAnswerAddNew(String answerAddNew) {
		this.answerAddNew = answerAddNew;
	}

	public List<User_Result> getUserResult2() {
		return userResult2;
	}

	public void setUserResult2(List<User_Result> userResult2) {
		this.userResult2 = userResult2;
	}

	public List<User_Result> getUserResultTest() {
		return userResultTest;
	}

	public void setUserResultTest(List<User_Result> userResultTest) {
		this.userResultTest = userResultTest;
	}
	
	@Override
	protected Logger getLogger() {
		return null;
	}

}
