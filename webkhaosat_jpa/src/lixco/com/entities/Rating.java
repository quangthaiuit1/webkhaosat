package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "rating")
public class Rating extends AbstractEntities{
	private String name;
	
	@OneToOne
	private Question question;
	
	@OneToOne
	private TypeRating type_rating;
	
	public TypeRating getType_rating() {
		return type_rating;
	}

	public void setType_rating(TypeRating type_rating) {
		this.type_rating = type_rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
}
