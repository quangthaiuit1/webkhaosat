package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.Rating;

@ManagedBean
@ViewScoped
public class DetailQuestionBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long setofId;
	private List<Question> listQuestionBySet; // Danh sach cau hoi thuoc 1 bo
	private Question questionUpdated;
	private Question questionDeleted;
	private Answer answerUpdated;
	private Answer answerDeleted;
	private Rating ratingUpdated;
	private Rating ratingDeleted;
	private Question questionSelected; // Hung gia tri khi cau hoi duoc chon
	private String newAnswer1;
	private String newAnswer2;
	// Danh sach cau tra loi theo tung cau hoi
	private List<Answer> listAnswersByQuestion;
	private List<Rating> listRatingByQuestion;

	@Override
	protected void initItem() {
		listQuestionBySet = new ArrayList<>();
		// ?? Tai sao lai phai khoi tao
		questionUpdated = new Question();
		questionDeleted = new Question();
		answerUpdated = new Answer();
		answerDeleted = new Answer();
		ratingUpdated = new Rating();
		ratingDeleted = new Rating();
		listAnswersByQuestion = new ArrayList<>();
		listRatingByQuestion = new ArrayList<>();

		try {
			getParam();
			listQuestionBySet = QUESTION_SERVICE.find(null, setofId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// Get param from URL
	public void getParam() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("setofid");
		setofId = Long.parseLong(setofIdTemp);
	}

	// Xoa Cau hoi
	public void deleteQuestion() {
		QUESTION_SERVICE.delete(questionDeleted);
		listQuestionBySet = QUESTION_SERVICE.find(null, setofId);
		PrimeFaces.current().executeScript("PF('dialogDeleteQuest').hide()");
		notifyDeleteSuccess();
	}

	// Sua cau hoi
	public void updateQuesion() {
		questionUpdated.setModifiedDate(getDate());
		QUESTION_SERVICE.update(questionUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateQuest').hide()");
		notifyUpdateSuccess();
	}

	// sua cau tra loi
	public void updateAnswer() {
		answerUpdated.setModifiedDate(getDate());
		ANSWER_SERVICE.update(answerUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateAns').hide()");
		notifyUpdateSuccess();
	}

	public void deleteAnswer() {
		ANSWER_SERVICE.delete(answerDeleted);
		listAnswersByQuestion = ANSWER_SERVICE.find(questionSelected.getId());
		PrimeFaces.current().executeScript("PF('dialogDeleteAns').hide()");
		notifyDeleteSuccess();
	}

	public void updateRating() {
		ratingUpdated.setModifiedDate(getDate());
		RATING_SERVICE.update(ratingUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateRati').hide()");
		notifyUpdateSuccess();
	}

	public void deleteRating() {
		RATING_SERVICE.delete(ratingDeleted);
		listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(),0L);
		PrimeFaces.current().executeScript("PF('dialogDeleteRati').hide()");
		notifyDeleteSuccess();
	}

	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
	}

	// Cau hoi duoc chon
	public void selectedQuestion() {
		// Danh sach cau tra loi theo id cau hoi
		// Danh gia
		if (questionSelected.getQuestionType().getId() == 2) {
			listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(),0L);
			// Reset ds dap ap cua loai cau hoi
			listAnswersByQuestion = new ArrayList<>();
		}
		if(questionSelected.getQuestionType().getId() == 4) {
			listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(),0L);
			listAnswersByQuestion = new ArrayList<>();
		}
		// Lay y kien
		if (questionSelected.getQuestionType().getId() == 1) {
			listAnswersByQuestion = new ArrayList<>();
			listRatingByQuestion = new ArrayList<>();
		}
	}

	// Bo sung them dap an
	public void addNewAnswer() {
		try {
			if (questionSelected.getName() != null && StringUtils.isNotEmpty(newAnswer1)
					&& questionSelected.getQuestionType().getId() == 3) {
				Answer newAnswer = new Answer();
				newAnswer.setCreatedDate(getDate());
				newAnswer.setDeleted(false);
				newAnswer.setName(newAnswer1);
				newAnswer.setQuestion(questionSelected);
				ANSWER_SERVICE.create(newAnswer);
				newAnswer1 = "";
				listAnswersByQuestion = ANSWER_SERVICE.find(questionSelected.getId());
				PrimeFaces.current().executeScript("PF('dialogAddNewAnswer').hide()");
				notifyAddSuccess();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void addNewRating() {
		try {
			if (questionSelected.getName() != null && StringUtils.isNotEmpty(newAnswer2)
					&& questionSelected.getQuestionType().getId() == 2) {
				Rating newRating = new Rating();
				newRating.setCreatedDate(getDate());
				newRating.setDeleted(false);
				newRating.setName(newAnswer2);
				newRating.setQuestion(questionSelected);
				RATING_SERVICE.create(newRating);
				newAnswer2 = "";
				listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(),0L);
				PrimeFaces.current().executeScript("PF('dialogAddNewRating').hide()");
				notifyAddSuccess();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	public Long getSetofId() {
		return setofId;
	}

	public void setSetofId(Long setofId) {
		this.setofId = setofId;
	}

	public List<Question> getListQuestionBySet() {
		return listQuestionBySet;
	}

	public void setListQuestionBySet(List<Question> listQuestionBySet) {
		this.listQuestionBySet = listQuestionBySet;
	}

	public Question getQuestionUpdated() {
		return questionUpdated;
	}

	public void setQuestionUpdated(Question questionUpdated) {
		this.questionUpdated = questionUpdated;
	}

	public Question getQuestionDeleted() {
		return questionDeleted;
	}

	public void setQuestionDeleted(Question questionDeleted) {
		this.questionDeleted = questionDeleted;
	}

	public Answer getAnswerUpdated() {
		return answerUpdated;
	}

	public void setAnswerUpdated(Answer answerUpdated) {
		this.answerUpdated = answerUpdated;
	}

	public Answer getAnswerDeleted() {
		return answerDeleted;
	}

	public void setAnswerDeleted(Answer answerDeleted) {
		this.answerDeleted = answerDeleted;
	}

	public Rating getRatingUpdated() {
		return ratingUpdated;
	}

	public void setRatingUpdated(Rating ratingUpdated) {
		this.ratingUpdated = ratingUpdated;
	}

	public Rating getRatingDeleted() {
		return ratingDeleted;
	}

	public void setRatingDeleted(Rating ratingDeleted) {
		this.ratingDeleted = ratingDeleted;
	}

	public Question getQuestionSelected() {
		return questionSelected;
	}

	public void setQuestionSelected(Question questionSelected) {
		this.questionSelected = questionSelected;
	}

	public String getNewAnswer1() {
		return newAnswer1;
	}

	public void setNewAnswer1(String newAnswer1) {
		this.newAnswer1 = newAnswer1;
	}

	public String getNewAnswer2() {
		return newAnswer2;
	}

	public void setNewAnswer2(String newAnswer2) {
		this.newAnswer2 = newAnswer2;
	}

	public List<Answer> getListAnswersByQuestion() {
		return listAnswersByQuestion;
	}

	public void setListAnswersByQuestion(List<Answer> listAnswersByQuestion) {
		this.listAnswersByQuestion = listAnswersByQuestion;
	}

	public List<Rating> getListRatingByQuestion() {
		return listRatingByQuestion;
	}

	public void setListRatingByQuestion(List<Rating> listRatingByQuestion) {
		this.listRatingByQuestion = listRatingByQuestion;
	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
}
