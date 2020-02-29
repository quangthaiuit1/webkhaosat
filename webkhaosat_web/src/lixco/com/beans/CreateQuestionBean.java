package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionType;
import lixco.com.entities.Rating;
import lixco.com.entities.Survey;

@ManagedBean
@ViewScoped
public class CreateQuestionBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long setofId;
	private List<QuestionType> questionTypeList1; // toan bo loai cau hoi
	private QuestionType questionTypeSelected;
	private long questionTypeTempTest;// giu loai cau hoi khi them cau hoi
	private String questionNameNew; // Danh sach bien hung gia tri
	private String[] answersNew; // Bien hung dap an khi tao moi

	@Override
	protected void initItem() {
		try {
			setofId = getParamSetOfId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Danh sach dap an tao moi
		answersNew = new String[5];
		// Danh sach loai cau hoi truy van tu DB
		questionTypeList1 = QUESTIONTYPE_SERVICE.findAllByFilter();
	}

	// Them cau hoi
	public void createQuestion() {
		Question tempQ = new Question();
		Survey tempSet = new Survey();
		tempSet = SURVEY_SERVICE.findById(setofId);
		tempQ.setSurvey(tempSet);
		tempQ.setQuestionType(questionTypeSelected);
		tempQ.setCreatedDate(getDate());
		tempQ.setName(questionNameNew);
		tempQ.setDeleted(false);
		// Danh gia || Thang diem
		if (questionTypeTempTest == 2 || questionTypeTempTest == 4) {
			if (StringUtils.isEmpty(answersNew[0])) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập thông tin!.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			tempQ = QUESTION_SERVICE.create(tempQ);
			for (int i = 0; i < answersNew.length; i++) {
				if (StringUtils.isNotEmpty(answersNew[i])) {
					Rating tempRating = new Rating();
					tempRating.setCreatedDate(getDate());
					tempRating.setDeleted(false);
					tempRating.setName(answersNew[i]);
					tempRating.setQuestion(tempQ);
					RATING_SERVICE.create(tempRating);
				}
			}
			answersNew = new String[5];
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}

		// Lay y kien
		if (questionTypeTempTest == 1) {
			QUESTION_SERVICE.create(tempQ);
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
		// Dang test Co dap an
		if (questionTypeTempTest == 3) {
			if (StringUtils.isEmpty(answersNew[0])) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đán án.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			tempQ = QUESTION_SERVICE.create(tempQ);
			for (int i = 0; i < answersNew.length; i++) {
				if (StringUtils.isNotEmpty(answersNew[i])) {
					Answer tempAnswer = new Answer();
					tempAnswer.setCreatedDate(getDate());
					tempAnswer.setDeleted(false);
					tempAnswer.setName(answersNew[i]);
					tempAnswer.setQuestion(tempQ);
					ANSWER_SERVICE.create(tempAnswer);
				}
			}
			answersNew = new String[5];
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
	}

	// Autocomplete
	public List<QuestionType> completeQuestionType(String input) {
		String queryLowerCase = input.toLowerCase();
		return questionTypeList1.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
				.collect(Collectors.toList());
	}

	public void selectedQuestionType() throws IOException {
		questionTypeTempTest = questionTypeSelected.getId();
	}

//GET AND SET

	@Override
	protected Logger getLogger() {
		return null;
	}

	public Long getSetofId() {
		return setofId;
	}

	public void setSetofId(Long setofId) {
		this.setofId = setofId;
	}

	public List<QuestionType> getQuestionTypeList1() {
		return questionTypeList1;
	}

	public void setQuestionTypeList1(List<QuestionType> questionTypeList1) {
		this.questionTypeList1 = questionTypeList1;
	}

	public QuestionType getQuestionTypeSelected() {
		return questionTypeSelected;
	}

	public void setQuestionTypeSelected(QuestionType questionTypeSelected) {
		this.questionTypeSelected = questionTypeSelected;
	}

	public long getQuestionTypeTempTest() {
		return questionTypeTempTest;
	}

	public void setQuestionTypeTempTest(long questionTypeTempTest) {
		this.questionTypeTempTest = questionTypeTempTest;
	}

	public String getQuestionNameNew() {
		return questionNameNew;
	}

	public void setQuestionNameNew(String questionNameNew) {
		this.questionNameNew = questionNameNew;
	}

	public String[] getAnswersNew() {
		return answersNew;
	}

	public void setAnswersNew(String[] answersNew) {
		this.answersNew = answersNew;
	}

}
