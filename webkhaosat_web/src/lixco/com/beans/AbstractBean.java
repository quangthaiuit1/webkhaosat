package lixco.com.beans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import lixco.com.entities.Department;
import lixco.com.services.AnswerService;
import lixco.com.services.DepartmentService;
import lixco.com.services.QuestionService;
import lixco.com.services.QuestiontypeService;
import lixco.com.services.RatingService;
import lixco.com.services.SetofquestionService;
import lixco.com.services.UserResultService;
import lixco.com.services.UserService;

public class AbstractBean {
	@Inject
	protected QuestionService QUESTION_SERVICE;

	@Inject
	protected AnswerService ANSWER_SERVICE;

	@Inject
	protected RatingService RATING_SERVICE; // RATING_SERVICE

	@Inject
	protected SetofquestionService SETOFQUESTION_SERVICE;

	@Inject
	protected QuestiontypeService QUESTIONTYPE_SERVICE;
	
	@Inject 
	protected UserService USER_SERVICE;
	
	@Inject
	protected UserResultService USER_RESULT_SERVICE;
	
	@Inject
	protected DepartmentService DEPARTMENT_SERVICE;
	
	@PostConstruct
	public void init() {
		
	}
	protected Date getDate() {
		return new Date();
	}
// Bo thong bao
	protected void notifySuccess(){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	protected void notifyAddSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thêm mới thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	protected void notifyUpdateSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	protected void notifyDeleteSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Đã xóa!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

}
