package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "question")
public class Question extends AbstractEntities{
	private String name;
	
	@OneToOne
	private Setofquestions setofquestions;
	
	@OneToOne
	private QuestionType questiontype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Setofquestions getSetofquestions() {
		return setofquestions;
	}

	public void setSetofquestions(Setofquestions setofquestions) {
		this.setofquestions = setofquestions;
	}

	public QuestionType getQuestiontype() {
		return questiontype;
	}

	public void setQuestiontype(QuestionType questiontype) {
		this.questiontype = questiontype;
	}

}
