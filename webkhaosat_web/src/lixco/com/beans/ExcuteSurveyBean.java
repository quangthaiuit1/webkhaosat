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
	private List<QuestionAndRating> questionAndAnswerRating; // bo cau hoi phan 1
	private List<QuestionSlider> questionAndAnswerSlider;// thanng diem
	private String[] ketquaPhanDanhGia; // dap an phan danh gia
	// test
	private Rating[] ketquaPhanDanhGiaRating;
	private Boolean[] noteRating;
	private String[] noteRatingString;

	private String[] ketquaPhanThangDiem; // dap an phan thang diem
	private String[] ketquaPhanLayYKien; // dap an phan lay y kien

	private List<Answer> answers2; // Toan bo cau tra loi cua phan 3
	private List<Rating> answers1; // Toan bo cau tra loi cua phan 1
	private Long setofId;
	private Survey surveyPlaying;
	private boolean checkNullPhan1;
	private boolean checkNullPhan2;
	private boolean checkNullPhan3;
	private boolean checkNullAll;
	private boolean checkNullDescription = false;
	private boolean notifyComplete;
	private Account accountLogin;
	private Member member;
	private int test;

	private List<String> testString;
	private boolean renderedInputText = true;

	@Override
	protected void initItem() {
		member = getAccount().getMember();
		try {
			setofId = getParamSetOfId();
			surveyPlaying = SURVEY_SERVICE.findById(setofId);
			if(surveyPlaying.getDescription() == null || StringUtils.isEmpty(surveyPlaying.getDescription())) {
				checkNullDescription = false;
			}
			// Lay y kien
			long layYKien = new Long(ConfigQuestionType.LAY_Y_KIEN);
			questionsType1 = getListQuestionByType(layYKien, setofId);
			// Danh gia
			long danhGia = new Long(ConfigQuestionType.DANH_GIA);
			questionsType2 = getListQuestionByType(danhGia, setofId);
			// Thang diem
			long thangDiem = new Long(ConfigQuestionType.THANG_DIEM);
			questionsType4 = getListQuestionByType(thangDiem, setofId);
			// Kiem tra null de check view
			if (questionsType2.isEmpty()) {
				checkNullPhan1 = true;
			}
			if (questionsType4.isEmpty()) {
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
			ketquaPhanDanhGiaRating = new Rating[questionsType2.size() + 1];
			noteRating = new Boolean[questionsType2.size() + 1];
			noteRatingString = new String[questionsType2.size() + 1];
			for (int i = 0; i < noteRating.length; i++) {
				noteRating[i] = false;
				ketquaPhanDanhGiaRating[i] = new Rating();
			}

			ketquaPhanThangDiem = new String[questionsType4.size() + 1];
			ketquaPhanLayYKien = new String[questionsType1.size() + 1];

			answers1 = RATING_SERVICE.find(0L, setofId);
//			answers2 = ANSWER_SERVICE.findAllByFilter();
			// Bo cau hoi phan danh gia
			questionAndAnswerRating = new ArrayList<>();
			questionAndAnswerSlider = new ArrayList<>();
			buildSetOfQuestionByType(questionsType2, questionsType4, answers1, questionAndAnswerRating,
					questionAndAnswerSlider);
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
	public void buildSetOfQuestionByType(List<Question> questionsType2, List<Question> questionsType4,
			List<Rating> answers1, List<QuestionAndRating> qAndR, List<QuestionSlider> qSlider) {

		for (Question q : questionsType2) {
			// Bo cau hoi danh gia
			List<Rating> tempRatings = new ArrayList<>();
			for (Rating r : answers1) {
				if (q.getId() == r.getQuestion().getId()) {
					tempRatings.add(r);
				}
			}
			QuestionAndRating tempQA = new QuestionAndRating();
			tempQA.setQuestion(q);
			tempQA.setRatings(tempRatings);
			qAndR.add(tempQA);
		}

		// Bo câu hoi thang diem
		for (Question q : questionsType4) {
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
				qSlider.add(qTemp);
			}
			if (intTemp.get(0) < intTemp.get(1)) {
				qTemp.setMin(intTemp.get(0));
				qTemp.setMax(intTemp.get(1));
				qSlider.add(qTemp);
			}
		}
	}

//Luu ket qua
	public void completeSurvey() throws Throwable {
		List<User_Result> listAddNew = new ArrayList<>();

		List<String> resultList = new ArrayList<>();
		List<Question> questionList = new ArrayList<>();
		try {
//			for (Question questTemp : questionsType2) {
//				questionList.add(questTemp);
//			}
			for (Question questTemp1 : questionsType4) {
				questionList.add(questTemp1);
			}
			boolean layYKienNotNull = false;
			for (int i = 1; i < ketquaPhanLayYKien.length; i++) {
				if (StringUtils.isNotEmpty(ketquaPhanLayYKien[i])) {
					layYKienNotNull = true;
					break;
				}
			}
			if (layYKienNotNull == true) {
				for (int i = 1; i < ketquaPhanLayYKien.length; i++) {
					if (StringUtils.isNotEmpty(ketquaPhanLayYKien[i])) {
						questionList.add(questionsType1.get(i - 1));
					}
				}
			}

			for (int j = 0; j < ketquaPhanDanhGiaRating.length; j++) {
				int lastElement = ketquaPhanDanhGiaRating.length;
				if (StringUtils.isEmpty(ketquaPhanDanhGiaRating[lastElement - 1].getName())) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
							"Vui lòng hoàn thành toàn bộ khảo sát.");
					PrimeFaces.current().dialog().showMessageDynamic(message);
					return;
				}
				if (!StringUtils.isEmpty(ketquaPhanDanhGiaRating[j].getName())) {
					if(ketquaPhanDanhGiaRating[j].getType_rating().getId() == ConfigQuestionType.DAP_AN_LAY_Y_KIEN) {
						if(StringUtils.isNotEmpty(noteRatingString[j])) {
							User_Result userResultTemp = new User_Result();
							userResultTemp.setCreatedDate(getDate());
							userResultTemp.setResult(ketquaPhanDanhGiaRating[j].getName());
							userResultTemp.setQuestion(ketquaPhanDanhGiaRating[j].getQuestion());
							userResultTemp.setEmployeeCode(member.getCode());
							userResultTemp.setEmployeeName(member.getName());
							userResultTemp.setDepartmentName(member.getDepartment().getName());
							userResultTemp.setDepartmentCode(member.getDepartment().getCode());
							userResultTemp.setNote(noteRatingString[j]);
//							USER_RESULT_SERVICE.create(userResultTemp);
							listAddNew.add(userResultTemp);
							noteRatingString[j] = "";
						}else {
							FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
									"Anh/chị vui lòng cho biết ý kiến!.");
							PrimeFaces.current().dialog().showMessageDynamic(message);
							return;
						}
					}else {
						User_Result userResultTemp = new User_Result();
						userResultTemp.setCreatedDate(getDate());
						userResultTemp.setResult(ketquaPhanDanhGiaRating[j].getName());
						userResultTemp.setQuestion(ketquaPhanDanhGiaRating[j].getQuestion());
						userResultTemp.setEmployeeCode(member.getCode());
						userResultTemp.setEmployeeName(member.getName());
						userResultTemp.setDepartmentName(member.getDepartment().getName());
						userResultTemp.setDepartmentCode(member.getDepartment().getCode());
//						USER_RESULT_SERVICE.create(userResultTemp);
						listAddNew.add(userResultTemp);
					}
				}
			}
			for (String b : ketquaPhanThangDiem) {
				if (StringUtils.isNotEmpty(b)) {
					resultList.add(b);
				}
			}
			if (layYKienNotNull == true) {
				for (String c : ketquaPhanLayYKien) {
					if (StringUtils.isNotEmpty(c)) {
						resultList.add(c);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultList.size() != questionList.size()) {
			PrimeFaces.current().executeScript("PF('dialogErrorSurvey').show();");
			return;
		}
		accountLogin = getSession();
		if (accountLogin != null) {
			List<User_Result> checkComplete = USER_RESULT_SERVICE.find(0L, 0L, accountLogin.getMember().getCode(),
					null);
			boolean isCompleted = false;
			// kiem tra da khao sat chua
			if (!checkComplete.isEmpty()) {
				isCompleted = true;
			}
			for (int i = 0; i < questionList.size(); i++) {
				User_Result userResultTemp = new User_Result();
				userResultTemp.setCreatedDate(getDate());
				userResultTemp.setResult(resultList.get(i));
				userResultTemp.setQuestion(questionList.get(i));
				userResultTemp.setEmployeeCode(accountLogin.getMember().getCode());
				userResultTemp.setEmployeeName(accountLogin.getMember().getName());
				userResultTemp.setDepartmentName(accountLogin.getMember().getDepartment().getName());
				userResultTemp.setDepartmentCode(accountLogin.getMember().getDepartment().getCode());
//				USER_RESULT_SERVICE.create(userResultTemp);
				listAddNew.add(userResultTemp);
			}
			// xoa list duoi DB
			if (isCompleted) {
				for (User_Result uDelete : checkComplete) {
					USER_RESULT_SERVICE.delete(uDelete);
				}
			}
			for (User_Result ur : listAddNew) {
				USER_RESULT_SERVICE.create(ur);
			}
			notifyComplete = true;
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
//					"Bạn đã hoàn thành khảo sát!");
//			PrimeFaces.current().dialog().showMessageDynamic(message);
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
		ketquaPhanDanhGiaRating[id] = temp;
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

	public String[] getKetquaPhanLayYKien() {
		return ketquaPhanLayYKien;
	}

	public void setKetquaPhanLayYKien(String[] ketquaPhanLayYKien) {
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

	public Rating[] getKetquaPhanDanhGiaRating() {
		return ketquaPhanDanhGiaRating;
	}

	public void setKetquaPhanDanhGiaRating(Rating[] ketquaPhanDanhGiaRating) {
		this.ketquaPhanDanhGiaRating = ketquaPhanDanhGiaRating;
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

	@Override
	protected Logger getLogger() {
		return null;
	}

}
