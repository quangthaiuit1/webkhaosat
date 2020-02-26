package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "question_type")
public class QuestionType extends AbstractEntities{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
