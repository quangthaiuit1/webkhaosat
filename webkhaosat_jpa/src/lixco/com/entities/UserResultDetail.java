package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_result_detail")
public class UserResultDetail extends AbstractEntities{
	
	@OneToOne
	private User_Result user_result;
	@OneToOne
	private Question question;
	@OneToOne
	private Rating rating;
	
	@OneToOne
	private Survey survey;
	
	//thang diem
	private int thangdiem;
	//lay y kien
	private String lay_y_kien;
	private String note;
	
	public User_Result getUser_result() {
		return user_result;
	}
	public void setUser_result(User_Result user_result) {
		this.user_result = user_result;
	}
	public Rating getRating() {
		return rating;
	}
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	public int getThangdiem() {
		return thangdiem;
	}
	public void setThangdiem(int thangdiem) {
		this.thangdiem = thangdiem;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getLay_y_kien() {
		return lay_y_kien;
	}
	public void setLay_y_kien(String lay_y_kien) {
		this.lay_y_kien = lay_y_kien;
	}
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
}
