package lixco.com.entities;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "question")
public class Question extends AbstractEntities {
	private String name;
	private byte[] image;

	@OneToOne
	private Survey survey;

	@OneToOne
	private QuestionType questionType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String imageString() {
		try {
			return new String(Base64.getEncoder().encodeToString(image));
		} catch (Exception e) {
			return "";
		}

	}

}
