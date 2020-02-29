package lixco.com.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "survey")
public class Survey extends AbstractEntities{
	private String name;
	@Column(name = "list_user_or_department")
	private String listUserOrDeparments;
	@Column(name = "employees_json")
	private String listEmployeesJson;
	@Column(name = "target_type")
	private String targetType;
	private String description; 
	@Column(name = "start_date")
	private Date startDate;
	@Column(name = "end_date")
	private Date endDate;
	@Column(name="title_rating")
	private String titleRating;
	@Column(name="title_slider")
	private String titleSlider;
	@Column(name="title_feedback")
	private String titleFeedback;
	
	
	public String getListEmployeesJson() {
		return listEmployeesJson;
	}

	public void setListEmployeesJson(String listEmployeesJson) {
		this.listEmployeesJson = listEmployeesJson;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getListUserOrDeparments() {
		return listUserOrDeparments;
	}

	public void setListUserOrDeparments(String listUserOrDeparments) {
		this.listUserOrDeparments = listUserOrDeparments;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTitleRating() {
		return titleRating;
	}

	public void setTitleRating(String titleRating) {
		this.titleRating = titleRating;
	}

	public String getTitleSlider() {
		return titleSlider;
	}

	public void setTitleSlider(String titleSlider) {
		this.titleSlider = titleSlider;
	}

	public String getTitleFeedback() {
		return titleFeedback;
	}

	public void setTitleFeedback(String titleFeedback) {
		this.titleFeedback = titleFeedback;
	}
	
}
