package lixco.com.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Answer;
import lixco.com.entities.Department;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionAndAnswer;
import lixco.com.entities.QuestionAndRating;
import lixco.com.entities.QuestionType;
import lixco.com.entities.Rating;
import lixco.com.entities.Setofquestions;
import lixco.com.entities.User;
import lixco.com.entities.User_Result;

@ManagedBean
@ViewScoped
public class QuestionsBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

//Da hoan thanh	
	private List<Question> questionsType1; // Cau hoi lay y kien
	private List<Question> questionsType2; // Cau hoi danh gia
	private List<Question> questionsType3; // Cau hoi co dap an
	private List<Question> questionsType4; // Cau hoi thang diem
	private List<QuestionAndAnswer> questionAndAnswer2;// Danh sach bo cau hoi phan 2
	private List<QuestionAndRating> questionAndAnswer1; // Danh sach bo cau hoi phan 1
	private String[] ketquaPhan1; // Danh sach dap an phan 1
	private String[] ketquaPhan2; // Danh sach dap an phan 2
	private String[] ketquaPhan3; // Danh sach dap an phan 3
	private List<Answer> answers2; // Toan bo cau tra loi cua phan 3
	private List<Rating> answers1; // Toan bo cau tra loi cua phan 1
	private List<QuestionType> questionTypeList1; // toan bo loai cau hoi
	private QuestionType questionTypeSelected;
	private QuestionType questionTypeTemp;//giu loai cau hoi khi them cau hoi
	private List<Question> listQuestionBySet; // Danh sach cau hoi thuoc 1 bo
	private String questionNameNew; // Danh sach bien hung gia tri
	private Setofquestions setofquestionNew;
	private String[] answersNew; // Bien hung dap an khi tao moi
	private List<Setofquestions> setofquestions1; // Danh sach bo cau hoi
	private Setofquestions setofquestionsSelected1; // Hung gia tri bo cau hoi khi user chon them
	private String answerAddNew;
	private Setofquestions setofquestionSelected2; //// Hung gia tri bo cau hoi de xoa va sua
	private Question questionSelected; // Hung gia tri khi cau hoi duoc chon
	private Setofquestions setOfquestionsSelected3; // hung gia tri bo cau hoi se dien ra
	// update and delete
	private Setofquestions setofquestionsUpdated;
	private Setofquestions setofquestionsDeleted;
	private Question questionUpdated;
	private Question questionDeleted;
	private Answer answerUpdated;
	private Answer answerDeleted;
	private Rating ratingUpdated;
	private Rating ratingDeleted;
	// Danh sach cau tra loi theo tung cau hoi
	private List<Answer> listAnswersByQuestion;
	private List<Rating> listRatingByQuestion;
	private Setofquestions setofquestionsSelected4; // Hung gia tri bo cau hoi de set user
	private List<User> listusersBySetofquestion; // Danh sach nguoi dung theo bo cau hoi
	private List<User> users; // Danh sach toan bo user
	private List<User_Result> userResult1; // ket qua khao sat theo id bo khao sat
	private List<User_Result> userResult2; // List sau filter
	// CAC BIEN HUNG GIA TRI TAM THOI
	private Date startDate;
	private Date endDate;
	
	private String newAnswer1;
	private String newAnswer2;
	private long setofId; //Bien hung ket qua get param setof
	private List<Department> departments1; // Toan bo phong ban
	private Setofquestions setofquestionsPlaying; // Bo ca hoi dang dien ra

	@PostConstruct
	public void init() {
		// khoi tao doi tuong hung gia tri khi khong co gia tri van khong bi loi
		setofquestionsSelected1 = new Setofquestions();
		setOfquestionsSelected3 = new Setofquestions();
		setofquestionsSelected4 = new Setofquestions();
		questionTypeTemp = new QuestionType();
		listQuestionBySet = new ArrayList<>();
		try {
			getParam();
			listQuestionBySet = QUESTION_SERVICE.find(null, setofId);
			userResult1 = USER_RESULT_SERVICE.find(setofId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		listAnswersByQuestion = new ArrayList<>();
		listRatingByQuestion = new ArrayList<>();
		listusersBySetofquestion = new ArrayList<>();
		// ?? Tai sao lai phai khoi tao
		questionUpdated = new Question();
		questionDeleted = new Question();
		answerUpdated = new Answer();
		answerDeleted = new Answer();
		ratingUpdated = new Rating();
		ratingDeleted = new Rating();
		setofquestionsUpdated = new Setofquestions();
		setofquestionsDeleted = new Setofquestions();
		// Toan bo user
		users = USER_SERVICE.findAllByFilter();
		departments1 = DEPARTMENT_SERVICE.findAllByFilter();

		// END
		// Danh sach toan bo cau hoi tu DB
		setofquestions1 = SETOFQUESTION_SERVICE.findAllByFilter();
		// Duyet danh sach bo cau hoi thu bo nao dang dien ra
		for (Setofquestions set : setofquestions1) {
			if (set.getStatus().equals("ON")) {
				setofquestionsPlaying = set;
			}
		}
		// Danh sach loai cau hoi truy van tu DB
		questionTypeList1 = QUESTIONTYPE_SERVICE.findAllByFilter();
		setofquestionNew = new Setofquestions();
		try {
			// Lay y kien
			questionsType1 = QUESTION_SERVICE.find(1L, setofquestionsPlaying.getId());
			// Danh gia
			questionsType2 = QUESTION_SERVICE.find(2L, setofquestionsPlaying.getId());
			// Thang diem 100-> Gop thang diem 100 vao cau hoi lay y kien
			questionsType4 = QUESTION_SERVICE.find(4L, setofquestionsPlaying.getId());
			for (Question q : questionsType4) {
				questionsType2.add(q);
			}
			// co dap an
			questionsType3 = QUESTION_SERVICE.find(3L, setofquestionsPlaying.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		// tao bien hung ket qua phan 1
		ketquaPhan1 = new String[questionsType2.size() + 1];
		ketquaPhan2 = new String[questionsType3.size() + 1];
		ketquaPhan3 = new String[questionsType1.size() + 1];
		// Danh sach dap an tao moi
		answersNew = new String[5];

		answers1 = RATING_SERVICE.findAllByFilter();
		answers2 = ANSWER_SERVICE.findAllByFilter();
		// Bo cau hoi phan danh gia
		questionAndAnswer1 = new ArrayList<>();
		for (Question q : questionsType2) {
			List<Rating> tempRatings = new ArrayList<>();
			for (Rating r : answers1) {
				if (q.getId() == r.getQuestion().getId()) {
					tempRatings.add(r);
				}
			}
			QuestionAndRating tempQA = new QuestionAndRating();
			tempQA.setQuestion(q);
			tempQA.setRatings(tempRatings);
			questionAndAnswer1.add(tempQA);
		}

		// Bo cau hoi phan tra loi
		questionAndAnswer2 = new ArrayList<>();
		for (Question q : questionsType3) {
			List<Answer> tempAnswers = new ArrayList<>();
			for (Answer a : answers2) {
				if (q.getId() == a.getQuestion().getId()) {
					tempAnswers.add(a);
				}
			}
			QuestionAndAnswer tempQA = new QuestionAndAnswer();
			tempQA.setQuestion(q);
			tempQA.setAnswers(tempAnswers);
			questionAndAnswer2.add(tempQA);
		}
	}
	

// Get param from URL
		public void getParam() {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String setofIdTemp = request.getParameter("setofid");
			setofId = Long.parseLong(setofIdTemp);
		}

// Ham luu ket qua 
	public void completeSurvey() {
		User_Result userResultTemp;

		List<String> resultList = new ArrayList<>();
		List<Question> questionList = new ArrayList<>();
		try {
			for (Question questTemp : questionsType2) {
				questionList.add(questTemp);
			}
			for (Question questTemp1 : questionsType3) {
				questionList.add(questTemp1);
			}
			for (Question questTemp3 : questionsType1) {
				questionList.add(questTemp3);
			}

			for (String a : ketquaPhan1) {
				if (StringUtils.isNotEmpty(a)) {
					resultList.add(a);
				}
			}
			for (String b : ketquaPhan2) {
				if (StringUtils.isNotEmpty(b)) {
					resultList.add(b);
				}
			}
			for (String c : ketquaPhan3) {
				if (StringUtils.isNotEmpty(c)) {
					resultList.add(c);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (resultList.size() != questionList.size()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
					"Vui lòng hoàn thành tất cả khảo sát!");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			return;
		}
		User userTemp = new User();
		userTemp = USER_SERVICE.findById(3L);
		for (int i = 0; i < questionList.size(); i++) {
			userResultTemp = new User_Result();
			userResultTemp.setCreatedDate(getDate());
			userResultTemp.setResult(resultList.get(i));
			userResultTemp.setQuestion(questionList.get(i));
			userResultTemp.setUser(userTemp);
			USER_RESULT_SERVICE.create(userResultTemp);
		}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Bạn đã hoàn thành khảo sát!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

// 	Ky khao sat duoc chon
	public void selectedSetofquestion() {
		try {
			listQuestionBySet = QUESTION_SERVICE.find(null, this.setofquestionSelected2.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

// list user -> bo cau hoi duoc chon
	public void handleListuserBySetof() {
		try {
			listusersBySetofquestion = new ArrayList<>();
			String[] parts = setofquestionsSelected4.getListusers().split(",");
			long[] longs = new long[parts.length];
			for (int i = 0; i < parts.length; i++) {
				longs[i] = Long.parseLong(parts[i]);
			}
			// Them nguoi dung thuoc bo cau hoi vao danh sach
			for (int i = 0; i < longs.length; i++) {
				User userTemp = USER_SERVICE.findById(longs[i]);
				listusersBySetofquestion.add(userTemp);
			}
			setofquestionsSelected4 = new Setofquestions();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

//Cau hoi duoc chon
	public void selectedQuestion() {
		// Danh sach cau tra loi theo id cau hoi
		// Danh gia
		if (questionSelected.getQuestiontype().getId() == 2) {
			listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId());
			// Reset ds dap ap cua loai cau hoi
			listAnswersByQuestion = new ArrayList<>();
		}
		// Co dap an
		if (questionSelected.getQuestiontype().getId() == 3) {
			listAnswersByQuestion = ANSWER_SERVICE.find(questionSelected.getId());
			// Reset ds dap ap cua loai cau hoi thang diem
			listRatingByQuestion = new ArrayList<>();
		}
		// Lay y kien
		if (questionSelected.getQuestiontype().getId() == 1 || questionSelected.getQuestiontype().getId() == 4) {
			listAnswersByQuestion = new ArrayList<>();
			listRatingByQuestion = new ArrayList<>();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
					"Loại câu hỏi không có đáp án!.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

//Chon ky khao sat
	public void SetofquestionSelected() {
		// try catch de xu ly-> nhap sai format
		try {
			if (setOfquestionsSelected3.getId() == 0) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng chọn kỳ khảo sát!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			} else {
				setofquestions1 = SETOFQUESTION_SERVICE.findAllByFilter();
				for (Setofquestions setTemp : setofquestions1) {
					// OFF toan bo list bo cau hoi
					if (setTemp.getStatus().equals("ON")) {
						setTemp.setStatus("OFF");
						SETOFQUESTION_SERVICE.update(setTemp);
					}
					// Lay bo cau hoi vua duoc chon gan ON
					if (setTemp.getId() == setOfquestionsSelected3.getId()) {
						setOfquestionsSelected3.setStatus("ON");
						SETOFQUESTION_SERVICE.update(setOfquestionsSelected3);
					}
				}
				setofquestionsPlaying = setOfquestionsSelected3;
				setOfquestionsSelected3 = new Setofquestions();
				notifySuccess();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
// BO THEM XOA SUA 

	// Them ky khao sat
	public void createSetofquestion() {
		try {
			setofquestionNew.setDeleted(false);
			setofquestionNew.setCreatedDate(getDate());
			java.sql.Timestamp temp = new java.sql.Timestamp(startDate.getTime());
			setofquestionNew.setStarttime(temp);
			temp = new Timestamp(endDate.getTime());
			setofquestionNew.setEndtime(temp);
			setofquestionNew.setStatus("OFF");
			SETOFQUESTION_SERVICE.create(setofquestionNew);
			setofquestions1 = SETOFQUESTION_SERVICE.findAllByFilter();
			setofquestionNew = new Setofquestions();
			notifyAddSuccess();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// sua ky khao sat
	public void updateSetofquestions() {
		setofquestionsUpdated.setModifiedDate(getDate());
		SETOFQUESTION_SERVICE.update(setofquestionsUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateSetof').hide()");
		notifyUpdateSuccess();
	}
	//Xoa ky khao sat
	public void deleteSetofquestion() {
		SETOFQUESTION_SERVICE.delete(setofquestionsDeleted);
		setofquestions1 = SETOFQUESTION_SERVICE.findAllByFilter();
		PrimeFaces.current().executeScript("PF('dialogDeleteSetof').hide()");
		notifyDeleteSuccess();
	}

	// Them cau hoi
	public void createQuestion() {
		FacesMessage messageError;
		// khong nhap loai cau hoi
		if (questionTypeTemp.getName() == null) {
			messageError = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng chọn loại câu hỏi!.");
			PrimeFaces.current().dialog().showMessageDynamic(messageError);
			return;
		}
		Question tempQ = new Question();
		Setofquestions tempSet = new Setofquestions();
		tempSet = SETOFQUESTION_SERVICE.findById(setofId);
		tempQ.setSetofquestions(tempSet);
		tempQ.setQuestiontype(questionTypeTemp);
		tempQ.setCreatedDate(getDate());
		tempQ.setName(questionNameNew);
		tempQ.setDeleted(false);
		// Danh gia
		if (questionTypeTemp.getId() == 2) {
			if (StringUtils.isEmpty(answersNew[0])) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đán án!.");
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
			setofquestionsSelected1 = new Setofquestions();
			questionTypeTemp = new QuestionType();
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
		// Thang diem 100
		if (questionTypeTemp.getId() == 4) {
			tempQ = QUESTION_SERVICE.create(tempQ);

			Rating tempRating = new Rating();
			tempRating.setCreatedDate(getDate());
			tempRating.setDeleted(false);
			tempRating.setName("100");
			tempRating.setQuestion(tempQ);
			RATING_SERVICE.create(tempRating);

			setofquestionsSelected1 = new Setofquestions();
			questionTypeTemp = new QuestionType();
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
		// Lay y kien
		if (questionTypeTemp.getId() == 1) {
			QUESTION_SERVICE.create(tempQ);
			setofquestionsSelected1 = new Setofquestions();
			questionTypeTemp = new QuestionType();
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
		// Co dap an
		if (questionTypeTemp.getId() == 3) {
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
			setofquestionsSelected1 = new Setofquestions();
			questionTypeTemp = new QuestionType();
			questionNameNew = null;
			notifyAddSuccess();
			return;
		}
	}
	public void selectedQuestionType() {
		System.out.println(questionTypeSelected.getId());
	}
	// Bo sung them dap an
	public void addNewAnswer() {
		try {
			if (questionSelected.getName() != null && StringUtils.isNotEmpty(newAnswer1)
					&& questionSelected.getQuestiontype().getId() == 3) {
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
					&& questionSelected.getQuestiontype().getId() == 2) {
				Rating newRating = new Rating();
				newRating.setCreatedDate(getDate());
				newRating.setDeleted(false);
				newRating.setName(newAnswer2);
				newRating.setQuestion(questionSelected);
				RATING_SERVICE.create(newRating);
				newAnswer2 = "";
				listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId());
				PrimeFaces.current().executeScript("PF('dialogAddNewRating').hide()");
				notifyAddSuccess();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

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
		listRatingByQuestion = RATING_SERVICE.find(questionSelected.getId());
		PrimeFaces.current().executeScript("PF('dialogDeleteRati').hide()");
		notifyDeleteSuccess();
	}

	// Xoa Cau hoi
	public void deleteQuestion() {
		QUESTION_SERVICE.delete(questionDeleted);
		listQuestionBySet = QUESTION_SERVICE.findAllByFilter();
		PrimeFaces.current().executeScript("PF('dialogDeleteQuest').hide()");
		notifyDeleteSuccess();
	}

	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
	}
// KET THUC THEM XOA SUA
	public void test() {
		System.out.println("vao roi");
	}
//Autocomplete
	public List<QuestionType> completeQuestionType(String input) {
		String queryLowerCase = input.toLowerCase();
		return questionTypeList1.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
				.collect(Collectors.toList());
	}

	public List<Setofquestions> completesSetofquestion(String input) {
		String queryLowerCase = input.toLowerCase();
		return setofquestions1.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase))
				.collect(Collectors.toList());
	}

	public List<Question> getQuestionsType1() {
		return questionsType1;
	}

	public void setQuestionsType1(List<Question> questionsType1) {
		this.questionsType1 = questionsType1;
	}

	public List<Question> getQuestionsType2() {
		return questionsType2;
	}

	public void setQuestionsType2(List<Question> questionsType2) {
		this.questionsType2 = questionsType2;
	}

	public List<Question> getQuestionsType3() {
		return questionsType3;
	}

	public void setQuestionsType3(List<Question> questionsType3) {
		this.questionsType3 = questionsType3;
	}

	public List<Answer> getAnswers2() {
		return answers2;
	}

	public void setAnswers2(List<Answer> answers2) {
		this.answers2 = answers2;
	}

	public List<Rating> getAnswers1() {
		return answers1;
	}

	public void setAnswers1(List<Rating> answers1) {
		this.answers1 = answers1;
	}

	public List<QuestionAndAnswer> getQuestionAndAnswer2() {
		return questionAndAnswer2;
	}

	public void setQuestionAndAnswer2(List<QuestionAndAnswer> questionAndAnswer2) {
		this.questionAndAnswer2 = questionAndAnswer2;
	}

	public List<QuestionAndRating> getQuestionAndAnswer1() {
		return questionAndAnswer1;
	}

	public void setQuestionAndAnswer1(List<QuestionAndRating> questionAndAnswer1) {
		this.questionAndAnswer1 = questionAndAnswer1;
	}

	public String[] getKetquaPhan1() {
		return ketquaPhan1;
	}

	public void setKetquaPhan1(String[] ketquaPhan1) {
		this.ketquaPhan1 = ketquaPhan1;
	}

	public String[] getKetquaPhan2() {
		return ketquaPhan2;
	}

	public void setKetquaPhan2(String[] ketquaPhan2) {
		this.ketquaPhan2 = ketquaPhan2;
	}

	public String[] getKetquaPhan3() {
		return ketquaPhan3;
	}

	public void setKetquaPhan3(String[] ketquaPhan3) {
		this.ketquaPhan3 = ketquaPhan3;
	}

	public String getQuestionNameNew() {
		return questionNameNew;
	}

	public void setQuestionNameNew(String questionNameNew) {
		this.questionNameNew = questionNameNew;
	}

	public Setofquestions getSetofquestionNew() {
		return setofquestionNew;
	}

	public void setSetofquestionNew(Setofquestions setofquestionNew) {
		this.setofquestionNew = setofquestionNew;
	}

	public List<QuestionType> getQuestionTypeList1() {
		return questionTypeList1;
	}

	public void setQuestionTypeList1(List<QuestionType> questionTypeList1) {
		this.questionTypeList1 = questionTypeList1;
	}
	
	public QuestionType getQuestionTypeTemp() {
		return questionTypeTemp;
	}

	public void setQuestionTypeTemp(QuestionType questionTypeTemp) {
		this.questionTypeTemp = questionTypeTemp;
	}


	public String[] getAnswersNew() {
		return answersNew;
	}

	public void setAnswersNew(String[] answersNew) {
		this.answersNew = answersNew;
	}

	public List<Setofquestions> getSetofquestions1() {
		return setofquestions1;
	}

	public void setSetofquestions1(List<Setofquestions> setofquestions1) {
		this.setofquestions1 = setofquestions1;
	}

	public Setofquestions getSetofquestionsSelected1() {
		return setofquestionsSelected1;
	}

	public void setSetofquestionsSelected1(Setofquestions setofquestionsSelected1) {
		this.setofquestionsSelected1 = setofquestionsSelected1;
	}

	public Setofquestions getSetofquestionSelected2() {
		return setofquestionSelected2;
	}

	public void setSetofquestionSelected2(Setofquestions setofquestionSelected2) {
		this.setofquestionSelected2 = setofquestionSelected2;
	}

	public List<Question> getListQuestionBySet() {
		return listQuestionBySet;
	}

	public void setListQuestionBySet(List<Question> listQuestionBySet) {
		this.listQuestionBySet = listQuestionBySet;
	}

	public Question getQuestionSelected() {
		return questionSelected;
	}

	public void setQuestionSelected(Question questionSelected) {
		this.questionSelected = questionSelected;
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

	public Question getQuestionUpdated() {
		return questionUpdated;
	}

	public void setQuestionUpdated(Question questionUpdated) {
		this.questionUpdated = questionUpdated;
	}

	public Answer getAnswerUpdated() {
		return answerUpdated;
	}

	public void setAnswerUpdated(Answer answerUpdated) {
		this.answerUpdated = answerUpdated;
	}

	public Rating getRatingUpdated() {
		return ratingUpdated;
	}

	public void setRatingUpdated(Rating ratingUpdated) {
		this.ratingUpdated = ratingUpdated;
	}

	public Setofquestions getSetofquestionsUpdated() {
		return setofquestionsUpdated;
	}

	public void setSetofquestionsUpdated(Setofquestions setofquestionsUpdated) {
		this.setofquestionsUpdated = setofquestionsUpdated;
	}

	public Setofquestions getSetOfquestionsSelected3() {
		return setOfquestionsSelected3;
	}

	public void setSetOfquestionsSelected3(Setofquestions setOfquestionsSelected3) {
		this.setOfquestionsSelected3 = setOfquestionsSelected3;
	}

	public Setofquestions getSetofquestionsSelected4() {
		return setofquestionsSelected4;
	}

	public void setSetofquestionsSelected4(Setofquestions setofquestionsSelected4) {
		this.setofquestionsSelected4 = setofquestionsSelected4;
	}

	public List<User> getListusersBySetofquestion() {
		return listusersBySetofquestion;
	}

	public void setListusersBySetofquestion(List<User> listusersBySetofquestion) {
		this.listusersBySetofquestion = listusersBySetofquestion;
	}
	
	public Setofquestions getSetofquestionsPlaying() {
		return setofquestionsPlaying;
	}

	public void setSetofquestionsPlaying(Setofquestions setofquestionsPlaying) {
		this.setofquestionsPlaying = setofquestionsPlaying;
	}


	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<User_Result> getUserResult1() {
		return userResult1;
	}

	public void setUserResult1(List<User_Result> userResult1) {
		this.userResult1 = userResult1;
	}

	public String getAnswerAddNew() {
		return answerAddNew;
	}

	public void setAnswerAddNew(String answerAddNew) {
		this.answerAddNew = answerAddNew;
	}

	public List<User_Result> getUserResult2() {
		return userResult2;
	}

	public void setUserResult2(List<User_Result> userResult2) {
		this.userResult2 = userResult2;
	}

	public List<Department> getDepartments1() {
		return departments1;
	}

	public void setDepartments1(List<Department> departments1) {
		this.departments1 = departments1;
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

	public Setofquestions getSetofquestionsDeleted() {
		return setofquestionsDeleted;
	}

	public void setSetofquestionsDeleted(Setofquestions setofquestionsDeleted) {
		this.setofquestionsDeleted = setofquestionsDeleted;
	}

	public Question getQuestionDeleted() {
		return questionDeleted;
	}

	public void setQuestionDeleted(Question questionDeleted) {
		this.questionDeleted = questionDeleted;
	}

	public Answer getAnswerDeleted() {
		return answerDeleted;
	}

	public void setAnswerDeleted(Answer answerDeleted) {
		this.answerDeleted = answerDeleted;
	}

	public Rating getRatingDeleted() {
		return ratingDeleted;
	}

	public void setRatingDeleted(Rating ratingDeleted) {
		this.ratingDeleted = ratingDeleted;
	}


	public long getSetofId() {
		return setofId;
	}

	public void setSetofId(long setofId) {
		this.setofId = setofId;
	}


	public QuestionType getQuestionTypeSelected() {
		return questionTypeSelected;
	}

	public void setQuestionTypeSelected(QuestionType questionTypeSelected) {
		this.questionTypeSelected = questionTypeSelected;
	}
	
}
