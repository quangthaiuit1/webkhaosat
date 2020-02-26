package lixco.com.entities;

public class ManagerSurveyUser {
	private Survey survey;
	private boolean expired = false;
	private boolean completed = false;
	private boolean inCompleted = false;
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public boolean isInCompleted() {
		return inCompleted;
	}
	public void setInCompleted(boolean inCompleted) {
		this.inCompleted = inCompleted;
	}
}
