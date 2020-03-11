package lixco.com.entities;

import java.util.Date;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "survey")
public class Survey extends AbstractEntities{
	private String name;
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
	
	@Column(name = "users_json")
	private String usersJson;
	
	
	
	public String getUsersJson() {
		return usersJson;
	}

	public void setUsersJson(String usersJson) {
		this.usersJson = usersJson;
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
