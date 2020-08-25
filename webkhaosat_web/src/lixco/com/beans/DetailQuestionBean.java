package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import lixco.com.config.ConfigQuestionType;
import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.Rating;
import lixco.com.entities.TypeRating;
import lixco.com.services.TypeRatingService;

@Named
@ViewScoped
public class DetailQuestionBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long setofId;
	private List<Question> listQuestionBySet; // Danh sach cau hoi thuoc 1 bo
	private Question questionUpdated;
	private Question questionDeleted;
	private Answer answerUpdated;
	private Answer answerDeleted;
	private Question questionSelected; // Hung gia tri khi cau hoi duoc chon
	private String newAnswer1;
	private String newAnswer2;
	// Danh sach cau tra loi theo tung cau hoi
	private List<Answer> listAnswersByQuestion;
	private List<Rating> listRatingByQuestion;
	private List<Rating> listRatingRemove;

	@Inject
	private TypeRatingService TYPE_RATING_SERVICE;
	private List<TypeRating> typeRatings;
	private TypeRating typeRatingSelected;
	private Rating ratingSelected;

	@Override
	protected void initItem() {
		ratingSelected = new Rating();
		listRatingRemove = new ArrayList<>();
		typeRatings = TYPE_RATING_SERVICE.findAll();
		typeRatingSelected = new TypeRating();
		listQuestionBySet = new ArrayList<>();
		// ?? Tai sao lai phai khoi tao
		questionUpdated = new Question();
		questionDeleted = new Question();
		answerUpdated = new Answer();
		answerDeleted = new Answer();
		listAnswersByQuestion = new ArrayList<>();
		listRatingByQuestion = new ArrayList<>();
		try {
			setofId = getParamSetOfId();
			listQuestionBySet = QUESTION_SERVICE.find(null, setofId, null);
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
		listQuestionBySet = QUESTION_SERVICE.find(null, setofId, null);
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


	public void deleteRating(Rating item) {
		List<Rating> temp = new ArrayList<>();
		for(Rating r : listRatingByQuestion) {
			if(r.getId() != item.getId()) {
				temp.add(r);
			}else {
				listRatingRemove.add(r);
			}
		}
		listRatingByQuestion = new ArrayList<>();
		listRatingByQuestion.addAll(temp);
	}

	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
	}

	// Cau hoi duoc chon
	public void selectedQuestion() {
		// Danh sach cau tra loi theo id cau hoi
		// Danh gia
		if (questionSelected.getQuestionType().getId() == ConfigQuestionType.DANH_GIA) {
			listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(), 0L);
			// Reset ds dap ap cua loai cau hoi
			listAnswersByQuestion = new ArrayList<>();
		}
		if (questionSelected.getQuestionType().getId() == ConfigQuestionType.THANG_DIEM) {
			listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId(), 0L);
			listAnswersByQuestion = new ArrayList<>();
		}
		// Lay y kien
		if (questionSelected.getQuestionType().getId() == ConfigQuestionType.LAY_Y_KIEN) {
			listAnswersByQuestion = new ArrayList<>();
			listRatingByQuestion = new ArrayList<>();
		}
	}

	public void updateRatingNew() {
		boolean check = false;
		try {
			if(!listRatingRemove.isEmpty()) {
				for(Rating ra: listRatingRemove) {
					RATING_SERVICE.delete(ra);
				}
				listRatingRemove = new ArrayList<>();
			}
			for (Rating r : listRatingByQuestion) {
				if (r.getId() != 0) {
					Rating newR = RATING_SERVICE.update(r);
					if(newR == null) {
						check = true;
					}
				} else {
					if (StringUtils.isNotEmpty(r.getName()) && r.getType_rating() != null) {
						r.setQuestion(listRatingByQuestion.get(0).getQuestion());
						Rating newR = RATING_SERVICE.create(r);
						if(newR == null) {
							check = true;
						}
					}
				}
			}
			if(check == false) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addNewRatingNew() {
		Rating newR = new Rating();
		newR.setType_rating(typeRatings.get(1));
		listRatingByQuestion.add(newR);
	}

	public void handleRatingSelected(Rating item) {
		ratingSelected = item;
		PrimeFaces.current().executeScript("PF('dialogChooseCTKKS').show()");
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

	public void handleChoose(TypeRating item) {
		for (int i = 0; i < listRatingByQuestion.size(); i++) {
			if (listRatingByQuestion.get(i).getName().equals(ratingSelected.getName())) {
				listRatingByQuestion.get(i).setType_rating(item);
			}
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

	public List<TypeRating> getTypeRatings() {
		return typeRatings;
	}

	public void setTypeRatings(List<TypeRating> typeRatings) {
		this.typeRatings = typeRatings;
	}

	public TypeRating getTypeRatingSelected() {
		return typeRatingSelected;
	}

	public void setTypeRatingSelected(TypeRating typeRatingSelected) {
		this.typeRatingSelected = typeRatingSelected;
	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}
}
