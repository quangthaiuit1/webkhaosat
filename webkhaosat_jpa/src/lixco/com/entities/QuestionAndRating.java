package lixco.com.entities;

import java.util.List;

public class QuestionAndRating {
	private Question question;
	private List<Rating> ratings;
	
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}
