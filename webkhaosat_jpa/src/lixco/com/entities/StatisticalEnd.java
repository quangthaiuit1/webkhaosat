package lixco.com.entities;

import java.util.List;

public class StatisticalEnd {
	private Question question;
	private List<RatingQuantity> ratingQuantities;
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public List<RatingQuantity> getRatingQuantities() {
		return ratingQuantities;
	}
	public void setRatingQuantities(List<RatingQuantity> ratingQuantities) {
		this.ratingQuantities = ratingQuantities;
	}
}
