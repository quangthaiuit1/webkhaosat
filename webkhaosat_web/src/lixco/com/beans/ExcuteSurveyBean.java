package lixco.com.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.config.ConfigQuestionType;
import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionAndRating;
import lixco.com.entities.QuestionSlider;
import lixco.com.entities.Rating;
import lixco.com.entities.Survey;
import lixco.com.entities.UserResultDetail;
import lixco.com.entities.User_Result;
import lixco.com.services.RatingService;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.Member;

@ManagedBean
@ViewScoped
public class ExcuteSurveyBean extends AbstractBean {

	private static final long serialVersionUID = 1L;
	private List<Question> questionsType1; // Cau hoi lay y kien
	private List<Question> questionsType2; // Cau hoi danh gia
	private List<Question> questionsType4; // Cau hoi thang diem
	private List<Question> questionsType3;// Cau hoi multiple choice
	private List<QuestionAndRating> questionAndMultipleChoiceAnswer;
	private List<QuestionAndRating> questionAndAnswerRating; // bo cau hoi phan 1
	private List<QuestionSlider> questionAndAnswerSlider;// thanng diem
	private String[] ketquaPhanDanhGia; // dap an phan danh gia
	// test
	private Boolean[] noteBooleanMultipleChoice;
	private Boolean[] noteRating;
	private String[] noteRatingMultipleChoice;
	private String[] noteRatingString;

	private String[] ketquaPhanThangDiem; // dap an phan thang diem
	private String[] ketquaMultipleChoiceString;
	private String[][] ketquaMultipleChoiceString2Chieu;
	private String dapAnLayYKienString; // dap an phan lay y kien
	private String[] dapAnLayYKienStringArray;
	private List<UserResultDetail> ketquaPhanLayYKien;

	private List<Answer> answers2; // Toan bo cau tra loi cua phan 3
	private List<Rating> answers1; // Toan bo cau tra loi cua phan 1
	private Long setofId;
	private Survey surveyPlaying;
	// danh gia
	private boolean checkNullPhan1;
	// multiple choice
	private boolean checkNullPhan2;
	// lay y kien
	private boolean checkNullPhan3;
	private boolean checkNullAll;
	private boolean checkNullDescription = false;
	private boolean notifyComplete;
	private Account accountLogin;
	private Member member;
	private int test;

	private List<String> testString;
	private boolean renderedInputText = true;

	// test
	private Rating[] testPutArrayRating;

	@Override
	protected void initItem() {

		member = getAccount().getMember();
		ketquaPhanLayYKien = new ArrayList<>();
		try {
			setofId = getParamSetOfId();
			surveyPlaying = SURVEY_SERVICE.findById(setofId);
			if (surveyPlaying.getDescription() == null || StringUtils.isEmpty(surveyPlaying.getDescription())) {
				checkNullDescription = false;
			}
			// Lay y kien
			long layYKien = new Long(ConfigQuestionType.LAY_Y_KIEN);
			questionsType1 = getListQuestionByType(layYKien, setofId);
			dapAnLayYKienStringArray = new String[questionsType1.size() + 1];
			// Danh gia
			long danhGia = new Long(ConfigQuestionType.DANH_GIA);
			questionsType2 = getListQuestionByType(danhGia, setofId);
			// Thang diem
			long thangDiem = new Long(ConfigQuestionType.THANG_DIEM);
			questionsType4 = getListQuestionByType(thangDiem, setofId);

			long multipleChoice = new Long(ConfigQuestionType.MULTIPLE_CHOICE);
			questionsType3 = getListQuestionByType(multipleChoice, setofId);

			// Kiem tra null de check view
			if (questionsType2.isEmpty()) {
				checkNullPhan1 = true;
			}
			if (questionsType3.isEmpty()) {
				checkNullPhan2 = true;
			}
			if (questionsType1.isEmpty()) {
				checkNullPhan3 = true;
			}
			if (checkNullPhan1 == true && checkNullPhan2 == true && checkNullPhan3 == true) {
				checkNullAll = true;
			}

			ketquaPhanDanhGia = new String[questionsType2.size() + 1];
			// test
			noteRating = new Boolean[questionsType2.size() + 1];
			noteRatingString = new String[questionsType2.size() + 1];
			for (int i = 0; i < noteRating.length; i++) {
				noteRating[i] = false;
			}

			// Dap an multiple choice
			ketquaMultipleChoiceString2Chieu = new String[questionsType3.size() + 1][10];
			noteRatingMultipleChoice = new String[questionsType3.size() + 1];
			noteBooleanMultipleChoice = new Boolean[questionsType3.size() + 1];
			for (int i = 0; i < noteRatingMultipleChoice.length; i++) {
				noteBooleanMultipleChoice[i] = false;
			}

			ketquaPhanThangDiem = new String[questionsType4.size() + 1];

			answers1 = RATING_SERVICE.find(0L, setofId);
			// Bo cau hoi phan danh gia
			questionAndAnswerRating = new ArrayList<>();
			questionAndMultipleChoiceAnswer = new ArrayList<>();
			questionAndAnswerSlider = new ArrayList<>();
			buildSetOfQuestionByType(questionsType2, questionsType4, questionsType3);
			// excute load lai data

		} catch (Exception e) {
			e.printStackTrace();
		}
		// tao bien hung ket qua phan 1
	}

//Get session
	public Account getSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Account accountTemp = (Account) session.getAttribute("account");
		return accountTemp;
	}

//Update view 
	public void test() {
		PrimeFaces.current().ajax().update("total:OkFine");
	}

//DS cau hoi theo tung loai
	public List<Question> getListQuestionByType(long questionTypeId, Long setOfId) {
		return QUESTION_SERVICE.find(questionTypeId, setOfId, null);
	}

//Tao bo cau hoi theo tung loai
	public void buildSetOfQuestionByType(List<Question> questionsTypeDanhGia, List<Question> questionsTypeThangDiem,
			List<Question> questionsTypeMultipleChoice) {

		// kiem tra da hoan thanh khao sat hay chua
		boolean isCompleted = false;
		User_Result checkComplete = USER_RESULT_SERVICE.findByEmployeeCode(setofId, member.getCode());
		if (checkComplete != null) {
			isCompleted = true;
		}
		// tao bo cau hoi
		for (int i = 0; i < questionsTypeDanhGia.size(); i++) {
			// Bo cau hoi danh gia
			List<Rating> tempRatings = new ArrayList<>();
			tempRatings = RATING_SERVICE.find(questionsTypeDanhGia.get(i).getId(), 0L);
			QuestionAndRating tempQA = new QuestionAndRating();
			tempQA.setQuestion(questionsTypeDanhGia.get(i));
			tempQA.setRatings(tempRatings);
			questionAndAnswerRating.add(tempQA);
			if (isCompleted) {
				// gan dap an da chon vao bo cau hoi
				UserResultDetail urd = USER_RESULT_DETAI_SERVICE.find(0L, 0L, questionsTypeDanhGia.get(i).getId())
						.get(0);
				// gan y kien vao cho o nhap y kien
				if (urd.getRating().getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
					this.ketquaPhanDanhGia[i + 1] = String.valueOf(urd.getRating().getId());
					noteRatingString[i + 1] = urd.getNote();
					noteRating[i + 1] = true;
				} else {
					this.ketquaPhanDanhGia[i + 1] = String.valueOf(urd.getRating().getId());
				}
			}
		}

		// Bo cau hoi multiple choice
		for (int i = 0; i < questionsTypeMultipleChoice.size(); i++) {
			List<Rating> tempRatings = new ArrayList<>();
			tempRatings = RATING_SERVICE.find(questionsTypeMultipleChoice.get(i).getId(), 0L);
			QuestionAndRating tempQA = new QuestionAndRating();
			tempQA.setQuestion(questionsTypeMultipleChoice.get(i));
			tempQA.setRatings(tempRatings);
			questionAndMultipleChoiceAnswer.add(tempQA);
			if (isCompleted) {
				// gan dap an da chon vao bo cau hoi
				List<UserResultDetail> urd = USER_RESULT_DETAI_SERVICE.find(0L, 0L,
						questionsTypeMultipleChoice.get(i).getId());
				for (int l = 0; l < urd.size(); l++) {
					if (urd.get(l).getRating().getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
						this.ketquaMultipleChoiceString2Chieu[i + 1][l] = String
								.valueOf(urd.get(l).getRating().getId());
						noteRatingMultipleChoice[i + 1] = urd.get(l).getNote();
						noteBooleanMultipleChoice[i + 1] = true;
					} else {
						this.ketquaMultipleChoiceString2Chieu[i + 1][l] = String
								.valueOf(urd.get(l).getRating().getId());
					}
				}
			}
		}
		if (isCompleted) {
			// Bo cau hoi lay y kien
			for (int j = 0; j < questionsType1.size(); j++) {
				List<UserResultDetail> urd = USER_RESULT_DETAI_SERVICE.find(0L, 0L, questionsType1.get(j).getId());
				if (!urd.isEmpty()) {
					dapAnLayYKienStringArray[j + 1] = urd.get(0).getLay_y_kien();
				}

			}
		}

		// Bo câu hoi thang diem
		for (Question q : questionsTypeThangDiem) {
			QuestionSlider qTemp = new QuestionSlider();
			List<Integer> intTemp = new ArrayList<>();
			for (Rating r : answers1) {
				if (q.getId() == r.getQuestion().getId()) {
					intTemp.add(Integer.parseInt(r.getName()));
				}
			}
			// hoi chuoi (*_*)
			qTemp.setQuestion(q);
			if (intTemp.get(0) > intTemp.get(1)) {
				qTemp.setMin(intTemp.get(1));
				qTemp.setMax(intTemp.get(0));
				questionAndAnswerSlider.add(qTemp);
			}
			if (intTemp.get(0) < intTemp.get(1)) {
				qTemp.setMin(intTemp.get(0));
				qTemp.setMax(intTemp.get(1));
				questionAndAnswerSlider.add(qTemp);
			}
		}
	}

//Luu ket qua
	public void completeSurvey() throws Throwable {
		List<UserResultDetail> listAddNew = new ArrayList<>();

		try {

			for (int j = 0; j < ketquaPhanDanhGia.length; j++) {
				if (j != 0) {
					if (StringUtils.isEmpty(ketquaPhanDanhGia[j])) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
								"Vui lòng hoàn thành toàn bộ khảo sát.");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						return;
					}
				}
				if (StringUtils.isNotEmpty(ketquaPhanDanhGia[j])) {
					// tim rating va check co phai dap an lay y kien hay khong
					Long idRating = Long.parseLong(ketquaPhanDanhGia[j]);
					Rating temp = RATING_SERVICE.findById(idRating);
					if (temp.getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
						if (StringUtils.isNotEmpty(noteRatingString[j])) {
							UserResultDetail userResultDetailTemp = new UserResultDetail();
							userResultDetailTemp.setCreatedDate(getDate());
							userResultDetailTemp.setRating(temp);
							userResultDetailTemp.setQuestion(temp.getQuestion());
							userResultDetailTemp.setNote(noteRatingString[j]);
							listAddNew.add(userResultDetailTemp);
							noteRatingString[j] = "";
						} else {
							FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
									"Anh/chị vui lòng cho biết ý kiến!.");
							PrimeFaces.current().dialog().showMessageDynamic(message);
							return;
						}
					} else {
						UserResultDetail userResultDetailTemp = new UserResultDetail();
						userResultDetailTemp.setCreatedDate(getDate());
						userResultDetailTemp.setQuestion(temp.getQuestion());
						userResultDetailTemp.setRating(temp);
						listAddNew.add(userResultDetailTemp);
					}
				}
			}
			// phan multiple choice
			for (int j = 0; j < ketquaMultipleChoiceString2Chieu.length; j++) {
				if (j != 0) {
					if (StringUtils.isEmpty(ketquaMultipleChoiceString2Chieu[j][0])) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
								"Vui lòng hoàn thành toàn bộ khảo sát.");
						PrimeFaces.current().dialog().showMessageDynamic(message);
						return;
					}
					// duyet tung phan tu cua moi mang
					for (int i = 0; i < ketquaMultipleChoiceString2Chieu[j].length; i++) {
						if (StringUtils.isNotEmpty(ketquaMultipleChoiceString2Chieu[j][i])) {
							Long idRating = Long.parseLong(ketquaMultipleChoiceString2Chieu[j][i]);
							Rating temp = RATING_SERVICE.findById(idRating);
							if (temp.getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
								// check dap an co phan note chua
								if (StringUtils.isNotEmpty(noteRatingMultipleChoice[j])) {
									UserResultDetail userResultDetailTemp = new UserResultDetail();
									userResultDetailTemp.setCreatedDate(getDate());
									userResultDetailTemp.setRating(temp);
									userResultDetailTemp.setQuestion(temp.getQuestion());
									userResultDetailTemp.setNote(noteRatingMultipleChoice[j]);
									listAddNew.add(userResultDetailTemp);
									noteRatingString[j] = "";
								} else {
									FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
											"Anh/chị vui lòng cho biết ý kiến!.");
									PrimeFaces.current().dialog().showMessageDynamic(message);
									return;
								}
							} else {
								UserResultDetail userResultDetailTemp = new UserResultDetail();
								userResultDetailTemp.setCreatedDate(getDate());
								userResultDetailTemp.setQuestion(temp.getQuestion());
								userResultDetailTemp.setRating(temp);
								listAddNew.add(userResultDetailTemp);
							}
						}
					}
				}
			}
			// phan lay y kien
			for (int j = 0; j < questionsType1.size(); j++) {
				if (dapAnLayYKienStringArray[j + 1] != null
						&& StringUtils.isNotEmpty(dapAnLayYKienStringArray[j + 1])) {
					UserResultDetail userResultDetailTemp = new UserResultDetail();
					userResultDetailTemp.setCreatedDate(getDate());
					userResultDetailTemp.setQuestion(questionsType1.get(j));
					userResultDetailTemp.setLay_y_kien(dapAnLayYKienStringArray[j + 1]);
					listAddNew.add(userResultDetailTemp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		accountLogin = getSession();
		if (accountLogin != null) {
			User_Result checkComplete = USER_RESULT_SERVICE.findByEmployeeCode(setofId,
					accountLogin.getMember().getCode());
			boolean isCompleted = false;
			// kiem tra da khao sat chua
			if (checkComplete != null) {
				isCompleted = true;
			}
			// xoa list duoi DB
			if (isCompleted) {
				USER_RESULT_SERVICE.delete(checkComplete);
			}

			// create da hoan thanh khao sat vao db
			User_Result entityNew = new User_Result();
			entityNew.setCreatedDate(getDate());
			entityNew.setCreatedUser(member.getName());
			entityNew.setSurvey(surveyPlaying);
			entityNew.setEmployeeCode(member.getCode());
			entityNew.setEmployeeName(member.getName());
			entityNew.setDepartmentCode(member.getDepartment().getCode());
			entityNew.setDepartmentName(member.getDepartment().getName());
			User_Result checkAdd = USER_RESULT_SERVICE.create(entityNew);
			if (checkAdd.getId() != 0) {
				for (UserResultDetail urd : listAddNew) {
					urd.setCreatedDate(getDate());
					urd.setUser_result(checkAdd);
					urd.setSurvey(surveyPlaying);
					USER_RESULT_DETAI_SERVICE.create(urd);
				}
			}
			notifyComplete = true;
			PrimeFaces.current().executeScript("PF('dialogCompleteSurvey').show();");
		}

	}

	// redirect to all survey
	public void rediretToAllSurvey() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/webkhaosat_web/pages/web/index.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// xu ly cau hoi multiple choice
	public void handleResultMultipleChoice(int id) {
		boolean checkYKienKhac = false;
		for (int i = 0; i < ketquaMultipleChoiceString2Chieu[id].length; i++) {
			long idRating = Long.parseLong(ketquaMultipleChoiceString2Chieu[id][i]);
			Rating temp = RATING_SERVICE.findById(idRating);
			if (temp.getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
				checkYKienKhac = true;
			}
		}
		if (checkYKienKhac) {
			noteBooleanMultipleChoice[id] = true;
			PrimeFaces.current().ajax().update(":total");
		} else {
			noteBooleanMultipleChoice[id] = false;
			PrimeFaces.current().ajax().update(":total");
		}
	}

	public void printABC(int id) {
		// ham ajax khong nhan array object. chi nhan array string. nen phai pass id
		// rating qua de tim dap an -> check loai dap an
		long idRating = Long.parseLong(ketquaPhanDanhGia[id]);
		Rating temp = RATING_SERVICE.findById(idRating);

		if (temp.getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
			noteRating[id] = true;
			PrimeFaces.current().ajax().update(":total");
		} else {
			noteRating[id] = false;
			PrimeFaces.current().ajax().update(":total");
		}

		// Gan dap an vao list ket qua phan danh gia
//		ketquaPhanDanhGiaRating[id] = temp;
	}

	public void ajaxHandleQuestionLayYKien(String questionName) {
		List<Question> temp = QUESTION_SERVICE.find(0L, setofId, questionName);
		boolean checkExist = false;
		int indexUpdate = 0;
		for (int i = 0; i < ketquaPhanLayYKien.size(); i++) {
			if (ketquaPhanLayYKien.get(i).getQuestion().getId() == temp.get(0).getId()) {
				checkExist = true;
				indexUpdate = i;
				break;
			}
		}
		if (checkExist) {
			ketquaPhanLayYKien.get(indexUpdate).setLay_y_kien(dapAnLayYKienString);
		} else {
			UserResultDetail userResultDetailTemp = new UserResultDetail();
			userResultDetailTemp.setCreatedDate(getDate());
			userResultDetailTemp.setQuestion(temp.get(0));
			userResultDetailTemp.setLay_y_kien(dapAnLayYKienString);
			ketquaPhanLayYKien.add(userResultDetailTemp);
		}
	}

//GET AND SET
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

	public List<Question> getQuestionsType4() {
		return questionsType4;
	}

	public void setQuestionsType4(List<Question> questionsType4) {
		this.questionsType4 = questionsType4;
	}

	public String[] getKetquaPhanDanhGia() {
		return ketquaPhanDanhGia;
	}

	public void setKetquaPhanDanhGia(String[] ketquaPhanDanhGia) {
		this.ketquaPhanDanhGia = ketquaPhanDanhGia;
	}

	public String[] getKetquaPhanThangDiem() {
		return ketquaPhanThangDiem;
	}

	public void setKetquaPhanThangDiem(String[] ketquaPhanThangDiem) {
		this.ketquaPhanThangDiem = ketquaPhanThangDiem;
	}

	public String getDapAnLayYKienString() {
		return dapAnLayYKienString;
	}

	public void setDapAnLayYKienString(String dapAnLayYKienString) {
		this.dapAnLayYKienString = dapAnLayYKienString;
	}

	public List<UserResultDetail> getKetquaPhanLayYKien() {
		return ketquaPhanLayYKien;
	}

	public void setKetquaPhanLayYKien(List<UserResultDetail> ketquaPhanLayYKien) {
		this.ketquaPhanLayYKien = ketquaPhanLayYKien;
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

	public List<QuestionAndRating> getQuestionAndAnswerRating() {
		return questionAndAnswerRating;
	}

	public void setQuestionAndAnswerRating(List<QuestionAndRating> questionAndAnswerRating) {
		this.questionAndAnswerRating = questionAndAnswerRating;
	}

	public List<QuestionSlider> getQuestionAndAnswerSlider() {
		return questionAndAnswerSlider;
	}

	public void setQuestionAndAnswerSlider(List<QuestionSlider> questionAndAnswerSlider) {
		this.questionAndAnswerSlider = questionAndAnswerSlider;
	}

	public Long getSetofId() {
		return setofId;
	}

	public void setSetofId(Long setofId) {
		this.setofId = setofId;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public boolean isCheckNullPhan1() {
		return checkNullPhan1;
	}

	public void setCheckNullPhan1(boolean checkNullPhan1) {
		this.checkNullPhan1 = checkNullPhan1;
	}

	public boolean isCheckNullPhan2() {
		return checkNullPhan2;
	}

	public void setCheckNullPhan2(boolean checkNullPhan2) {
		this.checkNullPhan2 = checkNullPhan2;
	}

	public boolean isCheckNullPhan3() {
		return checkNullPhan3;
	}

	public void setCheckNullPhan3(boolean checkNullPhan3) {
		this.checkNullPhan3 = checkNullPhan3;
	}

	public Account getAccountLogin() {
		return accountLogin;
	}

	public void setAccountLogin(Account accountLogin) {
		this.accountLogin = accountLogin;
	}

	public boolean isCheckNullAll() {
		return checkNullAll;
	}

	public void setCheckNullAll(boolean checkNullAll) {
		this.checkNullAll = checkNullAll;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public List<String> getTestString() {
		return testString;
	}

	public void setTestString(List<String> testString) {
		this.testString = testString;
	}

	public boolean isRenderedInputText() {
		return renderedInputText;
	}

	public void setRenderedInputText(boolean renderedInputText) {
		this.renderedInputText = renderedInputText;
	}

	public Boolean[] getNoteRating() {
		return noteRating;
	}

	public void setNoteRating(Boolean[] noteRating) {
		this.noteRating = noteRating;
	}

	public String[] getNoteRatingString() {
		return noteRatingString;
	}

	public void setNoteRatingString(String[] noteRatingString) {
		this.noteRatingString = noteRatingString;
	}

	public boolean isCheckNullDescription() {
		return checkNullDescription;
	}

	public void setCheckNullDescription(boolean checkNullDescription) {
		this.checkNullDescription = checkNullDescription;
	}

	public List<Question> getQuestionsType3() {
		return questionsType3;
	}

	public void setQuestionsType3(List<Question> questionsType3) {
		this.questionsType3 = questionsType3;
	}

	public List<QuestionAndRating> getQuestionAndMultipleChoiceAnswer() {
		return questionAndMultipleChoiceAnswer;
	}

	public void setQuestionAndMultipleChoiceAnswer(List<QuestionAndRating> questionAndMultipleChoiceAnswer) {
		this.questionAndMultipleChoiceAnswer = questionAndMultipleChoiceAnswer;
	}

	public Boolean[] getNoteBooleanMultipleChoice() {
		return noteBooleanMultipleChoice;
	}

	public void setNoteBooleanMultipleChoice(Boolean[] noteBooleanMultipleChoice) {
		this.noteBooleanMultipleChoice = noteBooleanMultipleChoice;
	}

	public String[] getNoteRatingMultipleChoice() {
		return noteRatingMultipleChoice;
	}

	public void setNoteRatingMultipleChoice(String[] noteRatingMultipleChoice) {
		this.noteRatingMultipleChoice = noteRatingMultipleChoice;
	}

	public String[] getKetquaMultipleChoiceString() {
		return ketquaMultipleChoiceString;
	}

	public void setKetquaMultipleChoiceString(String[] ketquaMultipleChoiceString) {
		this.ketquaMultipleChoiceString = ketquaMultipleChoiceString;
	}

	public String[][] getKetquaMultipleChoiceString2Chieu() {
		return ketquaMultipleChoiceString2Chieu;
	}

	public void setKetquaMultipleChoiceString2Chieu(String[][] ketquaMultipleChoiceString2Chieu) {
		this.ketquaMultipleChoiceString2Chieu = ketquaMultipleChoiceString2Chieu;
	}

	public Rating[] getTestPutArrayRating() {
		return testPutArrayRating;
	}

	public void setTestPutArrayRating(Rating[] testPutArrayRating) {
		this.testPutArrayRating = testPutArrayRating;
	}

	public String[] getDapAnLayYKienStringArray() {
		return dapAnLayYKienStringArray;
	}

	public void setDapAnLayYKienStringArray(String[] dapAnLayYKienStringArray) {
		this.dapAnLayYKienStringArray = dapAnLayYKienStringArray;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

}
