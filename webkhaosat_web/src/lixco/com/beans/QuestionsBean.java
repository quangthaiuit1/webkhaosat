package lixco.com.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionAndAnswer;
import lixco.com.entities.QuestionAndRating;
import lixco.com.entities.QuestionType;
import lixco.com.entities.Rating;
import lixco.com.entities.Setofquestions;
import lixco.com.entities.User;

@ManagedBean
@ViewScoped
public class QuestionsBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

//Da hoan thanh	
	// Cau hoi lay y kien
	private List<Question> questionsType1;
	// Cau hoi thang diem
	private List<Question> questionsType2;
	// Cau hoi co dap an
	private List<Question> questionsType3;
	// Danh sach bo cau hoi phan 2
	private List<QuestionAndAnswer> questionAndAnswer2;
	// Danh sach bo cau hoi phan 1
	private List<QuestionAndRating> questionAndAnswer1;
	// Danh sach dap an phan 1
	private String[] ketquaPhan1;

	// Danh sach dap an phan 2
	private String[] ketquaPhan2;
	// Danh sach dap an phan 3
	private String[] ketquaPhan3;

	// Toan bo cau tra loi cua phan 3
	private List<Answer> answers2;
	// Toan bo cau tra loi cua phan 1
	private List<Rating> answers1;
	// Danh sach loai cau hoi tu DB
	private List<QuestionType> questionTypeList1;
	// Giu gia tri loai cau hoi khi tao
	private QuestionType questiontypeAdd;
	// Danh sach cau hoi thuoc 1 bo
	private List<Question> listQuestionBySet;
	// Danh sach bien hung gia tri
	private String setofquestionNameNew;
	private String questionNameNew;
	private Setofquestions setofquestionNew;
	// Bien hung dap an khi tao moi
	private String[] answersNew;
	// Danh sach bo cau hoi
	private List<Setofquestions> setofquestions1;
	// Hung gia tri bo cau hoi khi user chon them
	private Setofquestions setofquestionsSelected1;
	//// Hung gia tri bo cau hoi de xoa va sua
	private Setofquestions setofquestionSelected2;
	// Hung gia tri khi cau hoi duoc chon
	private Question questionSelected;
	// Bien hung gia tri bo cau hoi se dien ra
	private Setofquestions setOfquestionsSelected3;
	// Danh sach bien hung gia tri update
	private Setofquestions setofquestionsUpdated;
	private Question questionUpdated;
	private Answer answerUpdated;
	private Rating ratingUpdated;
	// Danh sach cau tra loi theo tung cau hoi
	private List<Answer> listAnswersByQuestion;
	private List<Rating> listRatingByQuestion;
	private Question questionDeleteSelected;
	// Hung gia tri bo cau hoi de set user
	private Setofquestions setofquestionsSelected4;
	// Danh sach nguoi dung theo bo cau hoi
	private List<User> listusersBySetofquestion;
	// Danh sach toan bo user
	private List<User> users;

	// Bien de giu gia tri bo cau hoi dang dien ra
	private long setofquestionsId;
	// Ten bo cau hoi dang dien ra
	private String setofquestionsName;
	// Bo ca hoi dang dien ra
	private Setofquestions setofquestionsplaying;

	@PostConstruct
	public void init() {
		// khoi tao doi tuong hung gia tri khi khong co gia tri van khong bi loi
		setofquestionsSelected1 = new Setofquestions();
		setOfquestionsSelected3 = new Setofquestions();
		setofquestionsSelected4 = new Setofquestions();
		questiontypeAdd = new QuestionType();
		listQuestionBySet = new ArrayList<>();
		listAnswersByQuestion = new ArrayList<>();
		listRatingByQuestion = new ArrayList<>();
		listusersBySetofquestion = new ArrayList<>();
		// ?? Tai sao lai phai khoi tao
		questionUpdated = new Question();
		answerUpdated = new Answer();
		ratingUpdated = new Rating();
		setofquestionsUpdated = new Setofquestions();
		// Toan bo user
		users = userService.findAllByFilter();

		// END
		// Danh sach toan bo cau hoi tu DB
		setofquestions1 = setofquestionService.findAllByFilter();
		// Duyet danh sach bo cau hoi thu bo nao dang dien ra
		for (Setofquestions set : setofquestions1) {
			if (set.getStatus().equals("ON")) {
				setofquestionsplaying = set;
			}
		}
		// Danh sach loai cau hoi truy van tu DB
		questionTypeList1 = questiontypeService.findAllByFilter();
		setofquestionNew = new Setofquestions();
		// Lay y kien
		questionsType1 = questionService.find(1L, setofquestionsplaying.getId());
		// thang diem
		questionsType2 = questionService.find(2L, setofquestionsplaying.getId());
		// co dap an
		questionsType3 = questionService.find(3L, setofquestionsplaying.getId());
		// tao bien hung ket qua phan 1
		ketquaPhan1 = new String[questionsType2.size() + 1];
		ketquaPhan2 = new String[questionsType3.size() + 1];
		ketquaPhan3 = new String[questionsType1.size() + 1];
		// Danh sach dap an tao moi
		answersNew = new String[5];

		answers1 = ratingService.findAllByFilter();
		answers2 = answerService.findAllByFilter();
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

	public void checkcau1() {
		System.out.println("Đã vào check 1");
//		System.out.println(ketquaPhan1[1].toString());

//		System.out.println(Arrays.toString(ketquaPhan1));

	}

	public void checkcau2() {
		System.out.println("Đã vào check 2");

//		System.out.println(Arrays.toString(ketquaPhan1));
//		System.out.println(Arrays.toString(ketquaPhan2));

	}

// Khi bo cau hoi duoc chon -> danh sach cac cau hoi theo bo
	public void selectedSetofquestion() {
		listQuestionBySet = questionService.find(null, this.setofquestionSelected2.getId());
	}

// Danh sach user theo bo cau hoi khi bo cau hoi duoc chon
	public void handleListuserBySetof() {
		listusersBySetofquestion = new ArrayList<>();
		String[] parts = setofquestionsSelected4.getListusers().split(",");
		long[] longs = new long[parts.length];
		for (int i = 0; i < parts.length; i++) {
			longs[i] = Long.parseLong(parts[i]);
		}
		// Them nguoi dung thuoc bo cau hoi vao danh sach
		for (int i = 0; i < longs.length; i++) {
			User userTemp = userService.findById(longs[i]);
			listusersBySetofquestion.add(userTemp);
		}
		setofquestionsSelected4 = new Setofquestions();

	}

//Cau hoi duoc chon -> danh sach dap an theo cau hoi neu co
	public void selectedQuestion() {
		// select danh sach cau hoi tu questionselected
		// Cau tra loi cua cau hoi thang diem
		if (questionSelected.getQuestiontype().getId() == 2) {
			listRatingByQuestion = ratingService.find(questionSelected.getId());
			// Reset lai ds dap ap cua loai cau hoi co dap an de update ben view
			listAnswersByQuestion = new ArrayList<>();
		}
		// Cau tra loi cua cau hoi co dap an
		if (questionSelected.getQuestiontype().getId() == 3) {
			listAnswersByQuestion = answerService.find(questionSelected.getId());
			// Reset lai ds dap ap cua loai cau hoi thang diem de update ben view
			listRatingByQuestion = new ArrayList<>();
		}
		if (questionSelected.getQuestiontype().getId() == 1) {
			listAnswersByQuestion = new ArrayList<>();
			listRatingByQuestion = new ArrayList<>();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
					"Câu hỏi lấy ý kiến không có đáp án!.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

//Chon bo cau hoi se dien ra
	public void SetofquestionSelected() {
		// them try catch de xu ly neu nguoi dung nhap sai format
		try {
			if (setOfquestionsSelected3.getId() == 0) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng chọn bộ câu hỏi!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			} else {
				setofquestions1 = setofquestionService.findAllByFilter();
				for (Setofquestions setTemp : setofquestions1) {
					// Se doi thanh id thay vi ten cho de quan ly
					if (setTemp.getStatus().equals("ON")) {
						setTemp.setStatus("OFF");
						setofquestionService.update(setTemp);
					}
					if (setTemp.getId() == setOfquestionsSelected3.getId()) {
						setOfquestionsSelected3.setStatus("ON");
						setofquestionService.update(setOfquestionsSelected3);
					}
				}
				setofquestionsplaying = setOfquestionsSelected3;
				setOfquestionsSelected3 = new Setofquestions();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Chọn thành công.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

//Them bo cau hoi
	public void createSetofquestion() {
		if (this.setofquestionNameNew != null && !this.setofquestionNameNew.equals("")) {
			// Tao ra 1 doi tuong bo cau hoi xong luu xuong DB
			Setofquestions temp = new Setofquestions();
			temp.setDeleted(false);
			Date dateTemp = new Date();
			temp.setCreatedDate(dateTemp);
			temp.setName(setofquestionNameNew);
			temp.setStatus("OFF");
			setofquestionNew = setofquestionService.create(temp);
			setofquestionNameNew = null;
			setofquestions1 = setofquestionService.findAllByFilter();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thêm thành công.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
					"Vui lòng nhập thông tin trước khi tạo!.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

//sua bo cau hoi
	public void updateSetofquestions() {
		Date newDate = super.getDate();
		setofquestionsUpdated.setModifiedDate(newDate);
		setofquestionService.update(setofquestionsUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateSetof').hide()");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

//Them cau hoi
	public void createQuestion() {
		// Khong nhap gi ca
		//
		boolean inValid = false;
		FacesMessage messageError = new FacesMessage();
		// khong nhap bo cau hoi
		if (setofquestionsSelected1.getName() == null || setofquestionsSelected1.getName().equals("")) {
			messageError = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng chọn bộ câu hỏi!.");
			inValid = true;
		}
		// khong nhap ten cau hoi
		if (questionNameNew == null || questionNameNew.equals("")) {
			messageError = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng nhập tên câu hỏi!.");
			inValid = true;
		}
		// khong nhap loai cau hoi
		if (questiontypeAdd.getName() == null || questiontypeAdd.getName().equals("")) {
			messageError = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng chọn loại câu hỏi!.");
			inValid = true;
		}

		if (inValid) {
			PrimeFaces.current().dialog().showMessageDynamic(messageError);
			return;
		}

		// Tao bien tam de giu gia tri moi
		Question tempQ = new Question();
		tempQ.setSetofquestions(setofquestionsSelected1);
		tempQ.setQuestiontype(questiontypeAdd);
		Date tempDate = new Date();
		tempQ.setCreatedDate(tempDate);
		tempQ.setName(questionNameNew);
		tempQ.setDeleted(false);
		// thang diem
		if (questiontypeAdd.getId() == 2) {
			if ("".equals(answersNew[0])) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đán án!.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			tempQ = questionService.create(tempQ);
			for (int i = 0; i < answersNew.length; i++) {
				if (!answersNew[i].equals("") && answersNew[i] != null) {
					Rating tempRating = new Rating();
					tempRating.setCreatedDate(tempDate);
					tempRating.setDeleted(false);
					tempRating.setName(answersNew[i]);
					tempRating.setQuestion(tempQ);
					ratingService.create(tempRating);
				}

			}
			answersNew = new String[5];
			setofquestionsSelected1 = new Setofquestions();
			questiontypeAdd = new QuestionType();
			questionNameNew = null;

		}
		// Lay y kien
		if (questiontypeAdd.getId() == 1) {
			questionService.create(tempQ);
			setofquestionsSelected1 = new Setofquestions();
			questiontypeAdd = new QuestionType();
			questionNameNew = null;
		}
		// Co dap an
		if (questiontypeAdd.getId() == 3) {
			if ("".equals(answersNew[0])) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đán án.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			tempQ = questionService.create(tempQ);
			for (int i = 0; i < answersNew.length; i++) {
				if (!answersNew[i].equals("") && answersNew[i] != null) {
					Answer tempAnswer = new Answer();
					tempAnswer.setCreatedDate(tempDate);
					tempAnswer.setDeleted(false);
					tempAnswer.setName(answersNew[i]);
					tempAnswer.setQuestion(tempQ);
					answerService.create(tempAnswer);
				}
			}
			answersNew = new String[5];
			setofquestionsSelected1 = new Setofquestions();
			questiontypeAdd = new QuestionType();
			questionNameNew = null;
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thêm thành công.");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

//Sua cau hoi
	public void updateQuesion() {
		Date newDate = super.getDate();
		questionUpdated.setModifiedDate(newDate);
		questionService.update(questionUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateQuest').hide()");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

//sua cau tra loi
	public void updateAnswer() {
		Date newDate = super.getDate();
		answerUpdated.setModifiedDate(newDate);
		answerService.update(answerUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateAns').hide()");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	public void updateRating() {
		Date newDate = super.getDate();
		ratingUpdated.setModifiedDate(newDate);
		ratingService.update(ratingUpdated);
		PrimeFaces.current().executeScript("PF('dialogUpdateRati').hide()");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

//ket thuc sua cau tra loi
//Xoa Cau hoi
	public void deleteQuestion() {

	}

	public void cancelDelete() {
		PrimeFaces.current().executeScript("PF('dialogDelete').hide()");
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

	public String getSetofquestionNameNew() {
		return setofquestionNameNew;
	}

	public void setSetofquestionNameNew(String setofquestionNameNew) {
		this.setofquestionNameNew = setofquestionNameNew;
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

	public QuestionType getQuestiontypeAdd() {
		return questiontypeAdd;
	}

	public void setQuestiontypeAdd(QuestionType questiontypeAdd) {
		this.questiontypeAdd = questiontypeAdd;
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

	public Question getQuestionDeleteSelected() {
		return questionDeleteSelected;
	}

	public void setQuestionDeleteSelected(Question questionDeleteSelected) {
		this.questionDeleteSelected = questionDeleteSelected;
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

	public Setofquestions getSetofquestionsplaying() {
		return setofquestionsplaying;
	}

	public void setSetofquestionsplaying(Setofquestions setofquestionsplaying) {
		this.setofquestionsplaying = setofquestionsplaying;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
