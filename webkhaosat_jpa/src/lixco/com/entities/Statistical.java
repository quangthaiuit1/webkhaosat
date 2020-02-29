package lixco.com.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "statistical", query = "Select root.id,q.id as questionId,s.name , q.name questionName, root.result as result, count(root.result) as quantity from user_result as root, question as q, survey as s where q.survey_id = 1 and q.id = root.question_id and q.survey_id = s.id group by root.question_id, root.result", resultClass = User_Result.class)
public class Statistical {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long questionId;
	private String surveyName;
	private String questionName;
	private String result;
	private long quantity;
	
	
	public long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}
	public String getSurveyName() {
		return surveyName;
	}
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
	
}
