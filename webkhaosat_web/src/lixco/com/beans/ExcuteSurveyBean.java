package lixco.com.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionAndRating;
import lixco.com.entities.QuestionSlider;
import lixco.com.entities.Rating;
import lixco.com.entities.Survey;
import lixco.com.entities.User_Result;
import trong.lixco.com.account.servicepublics.Account;

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
	private String[] ketquaPhanThangDiem; // dap an phan thang diem
	private String[] ketquaPhanLayYKien; // dap an phan lay y kien

	private List<Answer> answers2; // Toan bo cau tra loi cua phan 3
	private List<Rating> answers1; // Toan bo cau tra loi cua phan 1
	private Long setofId;
	private Survey surveyPlaying;
	private boolean checkNullPhan1;
	private boolean checkNullPhan2;
	private boolean checkNullPhan3;
	private Account accountLogin;

	@Override
	protected void initItem() {
		try {
			setofId = getParamSetOfId();
			surveyPlaying = SURVEY_SERVICE.findById(setofId);
			// Lay y kien
			questionsType1 = getListQuestionByType(1L, setofId);
			// Danh gia
			questionsType2 = getListQuestionByType(2L, setofId);
			// Thang diem
			questionsType4 = getListQuestionByType(4L, setofId);
			// Kiem tra null de check view
			if (questionsType2 == null) {
				checkNullPhan1 = true;
			}
			if (questionsType4 == null) {
				checkNullPhan2 = true;
			}
			if (questionsType1 == null) {
				checkNullPhan3 = true;
			}

			ketquaPhanDanhGia = new String[questionsType2.size() + 1];
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
			// TODO: handle exception
		}
		// tao bien hung ket qua phan 1
	}

//Get session
	public Account getSession() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		Account accountTemp = (Account) session.getAttribute("account");
		return accountTemp;
	}

// Get param from URL
	public long getParamSetOfId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("setofid");
		return Long.parseLong(setofIdTemp);
	}

//DS cau hoi theo tung loai
	public List<Question> getListQuestionByType(long questionTypeId, Long setOfId) {
		return QUESTION_SERVICE.find(questionTypeId, setOfId);
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
		User_Result userResultTemp;

		List<String> resultList = new ArrayList<>();
		List<Question> questionList = new ArrayList<>();
		try {
			for (Question questTemp : questionsType2) {
				questionList.add(questTemp);
			}
			for (Question questTemp1 : questionsType4) {
				questionList.add(questTemp1);
			}
			for (Question questTemp3 : questionsType1) {
				questionList.add(questTemp3);
			}

			for (String a : ketquaPhanDanhGia) {
				if (StringUtils.isNotEmpty(a)) {
					resultList.add(a);
				}
			}
			for (String b : ketquaPhanThangDiem) {
				if (StringUtils.isNotEmpty(b)) {
					resultList.add(b);
				}
			}
			for (String c : ketquaPhanLayYKien) {
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
		accountLogin = getSession();
		if(accountLogin != null) {
			Survey temp = SURVEY_SERVICE.findById(setofId);
			String checkComplete = accountLogin.getMember().getCode() + ",";
			//kiem tra da khao sat chua
			if(temp.getListUserCompleted().contains(checkComplete)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Không được khảo sát lại!");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}
			for (int i = 0; i < questionList.size(); i++) {
				userResultTemp = new User_Result();
				userResultTemp.setCreatedDate(getDate());
				userResultTemp.setResult(resultList.get(i));
				userResultTemp.setQuestion(questionList.get(i));
				userResultTemp.setEmployeeCode(accountLogin.getMember().getCode());
				USER_RESULT_SERVICE.create(userResultTemp);
			}
			//kiem tra danh sach rong hay khong
			StringBuffer idEmployees = new StringBuffer();
			if (StringUtils.isNotEmpty(temp.getListUserCompleted()) && temp.getListUserCompleted() != null) {
				idEmployees.append(temp.getListUserCompleted());
				idEmployees.append(checkComplete);
				String a = idEmployees.toString();
				temp.setListUserCompleted(a);
				SURVEY_SERVICE.update(temp);
			}else {
				idEmployees.append(checkComplete);
				String a = idEmployees.toString();
				temp.setListUserCompleted(a);
				SURVEY_SERVICE.update(temp);
			}
			FacesContext.getCurrentInstance().getExternalContext().redirect("http://192.168.0.132:8380/webkhaosat_web/pages/web/index.jsf");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Bạn đã hoàn thành khảo sát!");
			PrimeFaces.current().dialog().showMessageDynamic(message);
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

	@Override
	protected Logger getLogger() {
		return null;
	}

}