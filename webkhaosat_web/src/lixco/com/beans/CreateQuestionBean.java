package lixco.com.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import lixco.com.config.ConfigQuestionType;
import lixco.com.entities.Answer;
import lixco.com.entities.Question;
import lixco.com.entities.QuestionType;
import lixco.com.entities.Rating;
import lixco.com.entities.Survey;
import lixco.com.entities.TypeRating;
import lixco.com.services.TypeRatingService;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ResizeImage;

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
	private List<Rating> answerNewList;
	private List<Rating> answerMultipleChoice;
	private List<TypeRating> typeRatings;
	private TypeRating typeRatingSelected;
	private Rating ratingSelected;
	private Member member;
	private Notify notify;

	private Question questionNewEntity;

	// Cac loai cau hoi
	private long LAY_Y_KIEN;
	private long THANG_DIEM;
	private long DANH_GIA;
	private long MULTIPLE_CHOICE;

	@EJB
	private TypeRatingService TYPE_RATING_SERVICE;

	@Override
	protected void initItem() {
		try {
			member = getAccount().getMember();
			LAY_Y_KIEN = ConfigQuestionType.LAY_Y_KIEN;
			THANG_DIEM = ConfigQuestionType.THANG_DIEM;
			DANH_GIA = ConfigQuestionType.DANH_GIA;
			MULTIPLE_CHOICE = ConfigQuestionType.MULTIPLE_CHOICE;
			setofId = getParamSetOfId();

		} catch (Exception e) {
			e.printStackTrace();
		}
		typeRatings = TYPE_RATING_SERVICE.findAll();

		// update new : danh sach dap an khi tao moi
		answerNewList = new ArrayList<>();
		answerMultipleChoice = new ArrayList<>();
		Rating firstTemp = new Rating();
		firstTemp.setType_rating(typeRatings.get(1));
		answerNewList.add(firstTemp);
		// danh sach multiple choice khi tao moi
		answerMultipleChoice.add(firstTemp);

		ratingSelected = new Rating();

		// end

		// Danh sach dap an tao moi
		answersNew = new String[2];
		// Danh sach loai cau hoi truy van tu DB
		questionTypeList1 = QUESTIONTYPE_SERVICE.findAllByFilter();
		// code new
		questionNewEntity = new Question();
		notify = new Notify(FacesContext.getCurrentInstance());
	}

	// UPLOAD IMAGE

	public void uploadAlbum(FileUploadEvent event) {
		notify = new Notify(FacesContext.getCurrentInstance());
		try (InputStream input = event.getFile().getInputstream()) {
			byte[] file = ResizeImage.toByteArray(input);
			questionNewEntity.setImage(file);
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dlavatar').hide();");
		} catch (Exception e) {
			e.printStackTrace();
			notify.error();
		}

	}

	// END UPLOAD IMAGE

	// Them cau hoi
	public void createQuestion() {
//		System.out.println(questionNewEntity);
		
		Question tempQ = new Question();
		Survey tempSet = new Survey();
		tempSet = SURVEY_SERVICE.findById(setofId);
		tempQ.setSurvey(tempSet);
		tempQ.setQuestionType(questionTypeSelected);
		tempQ.setCreatedDate(getDate());
		tempQ.setCreatedUser(member.getCode());
		tempQ.setName(questionNameNew);
		tempQ.setDeleted(false);
		// Danh gia
		if (questionTypeTempTest == ConfigQuestionType.DANH_GIA) {

			List<Rating> ratingsTemp = new ArrayList<>();
			for (Rating r : answerNewList) {
				if (StringUtils.isNotEmpty(r.getName()) && r.getType_rating() != null) {
					ratingsTemp.add(r);
				}
			}
			// cap nhat lai danh sach co dap an
			this.answerNewList = new ArrayList<>();
			this.answerNewList.addAll(ratingsTemp);

			// check coi co dap an nao hay khong
			if (answerNewList.isEmpty()) {
				addRowNew();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đáp án!.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}

			// dap an hop le moi cho them cau hoi
			if (!this.answerNewList.isEmpty()) {
				tempQ = QUESTION_SERVICE.create(tempQ);
			}
			if (tempQ.getId() != 0) {
				for (Rating r : answerNewList) {
					r.setQuestion(tempQ);
					RATING_SERVICE.create(r);
				}
				answerNewList = new ArrayList<>();
				addRowNew();
				questionNameNew = "";
				notifyAddSuccess();
				return;
			}
			answerNewList = new ArrayList<>();
			addRowNew();
			questionNameNew = "";
			return;
		}
		// Thang diem
		if (questionTypeTempTest == ConfigQuestionType.THANG_DIEM) {
			Question tempQuest = QUESTION_SERVICE.create(tempQ);
			// kiem tra thu nhap du min max chua
			boolean check = true;
			for (int l = 0; l < answersNew.length; l++) {
				if (StringUtils.isEmpty(answersNew[l])) {
					check = false;
				}
			}
			for (int l = 0; l < answersNew.length; l++) {
				if (check) {
					Rating rtNew = new Rating();
					rtNew.setCreatedDate(getDate());
					rtNew.setCreatedUser(member.getCode());
					rtNew.setName(answersNew[l]);
					rtNew.setQuestion(tempQuest);
					// loai dap an khong y kien
					TypeRating typeRating = TYPE_RATING_SERVICE.findById(2);
					rtNew.setType_rating(typeRating);
					RATING_SERVICE.create(rtNew);
				} else {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
							"Vui lòng nhập đáp án!.");
					PrimeFaces.current().dialog().showMessageDynamic(message);
					return;
				}
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thành công.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
			questionNameNew = "";
			answersNew = new String[2];
			return;
		}

		// nhieu dap an
		if (questionTypeTempTest == ConfigQuestionType.MULTIPLE_CHOICE) {
			List<Rating> ratingsTemp = new ArrayList<>();
			// check coi co dap an nao hay khong
			if (answerMultipleChoice.isEmpty()) {
				addRowNew();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo",
						"Vui lòng nhập đáp án!.");
				PrimeFaces.current().dialog().showMessageDynamic(message);
				return;
			}

			// kiem tra dap an nao bi null thi bo
			for (Rating r : answerMultipleChoice) {
				if (StringUtils.isNotEmpty(r.getName()) && r.getType_rating() != null) {
					ratingsTemp.add(r);
				}
			}
			// cap nhat lai danh sach co dap an
			this.answerMultipleChoice = new ArrayList<>();
			this.answerMultipleChoice.addAll(ratingsTemp);

			// dap an hop le moi cho them cau hoi
			if (!this.answerMultipleChoice.isEmpty()) {
				tempQ = QUESTION_SERVICE.create(tempQ);
			}
			if (tempQ.getId() != 0) {
				for (Rating r : answerMultipleChoice) {
					r.setQuestion(tempQ);
					RATING_SERVICE.create(r);
				}
				answerMultipleChoice = new ArrayList<>();
				addRowNewAnswerMultipleChoice();
				questionNameNew = "";
				notifyAddSuccess();
				return;
			}
			answerMultipleChoice = new ArrayList<>();
			addRowNewAnswerMultipleChoice();
			questionNameNew = "";
			return;
		}

		// Lay y kien
		if (questionTypeTempTest == ConfigQuestionType.LAY_Y_KIEN) {
			QUESTION_SERVICE.create(tempQ);
			questionNameNew = "";
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

//New update
	public void handleRatingSelected(Rating item) {
		ratingSelected = item;
		if (StringUtils.isEmpty(item.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng nhập đáp án.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			PrimeFaces.current().executeScript("PF('dialogChoose').show()");
		}
	}

	public void handleRatingSelectedMultipleChoice(Rating item) {
		ratingSelected = item;
		if (StringUtils.isEmpty(item.getName())) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Vui lòng nhập đáp án.");
			PrimeFaces.current().dialog().showMessageDynamic(message);
		} else {
			PrimeFaces.current().executeScript("PF('dialogChooseMultipleChoice').show()");
		}
	}

	public void deleteRow(Rating item) {
		if (StringUtils.isNotEmpty(item.getName())) {
			List<Rating> temp = new ArrayList<>();
			for (Rating r : answerNewList) {
				if (!r.getName().equals(item.getName())) {
					temp.add(r);
				}
			}
			answerNewList = new ArrayList<>();
			answerNewList.addAll(temp);
		}
	}

	public void addRowNew() {
		Rating abc = new Rating();
		abc.setType_rating(typeRatings.get(1));
		answerNewList.add(abc);
	}

	public void addRowNewAnswerMultipleChoice() {
		Rating abc = new Rating();
		abc.setType_rating(typeRatings.get(1));
		answerMultipleChoice.add(abc);
	}

	public void handleChoose() {
		for (int i = 0; i < answerNewList.size(); i++) {
			if (answerNewList.get(i).getName().equals(ratingSelected.getName())) {
				answerNewList.get(i).setType_rating(typeRatingSelected);
			}
		}
	}

	public void handleChooseMultipleChoice() {
		for (int i = 0; i < answerMultipleChoice.size(); i++) {
			if (answerMultipleChoice.get(i).getName().equals(ratingSelected.getName())) {
				answerMultipleChoice.get(i).setType_rating(typeRatingSelected);
			}
		}
	}
//End new update
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

	public List<Rating> getAnswerNewList() {
		return answerNewList;
	}

	public void setAnswerNewList(List<Rating> answerNewList) {
		this.answerNewList = answerNewList;
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

	public long getLAY_Y_KIEN() {
		return LAY_Y_KIEN;
	}

	public void setLAY_Y_KIEN(long lAY_Y_KIEN) {
		LAY_Y_KIEN = lAY_Y_KIEN;
	}

	public long getTHANG_DIEM() {
		return THANG_DIEM;
	}

	public void setTHANG_DIEM(long tHANG_DIEM) {
		THANG_DIEM = tHANG_DIEM;
	}

	public long getDANH_GIA() {
		return DANH_GIA;
	}

	public void setDANH_GIA(long dANH_GIA) {
		DANH_GIA = dANH_GIA;
	}

	public long getMULTIPLE_CHOICE() {
		return MULTIPLE_CHOICE;
	}

	public void setMULTIPLE_CHOICE(long mULTIPLE_CHOICE) {
		MULTIPLE_CHOICE = mULTIPLE_CHOICE;
	}

	public List<Rating> getAnswerMultipleChoice() {
		return answerMultipleChoice;
	}

	public void setAnswerMultipleChoice(List<Rating> answerMultipleChoice) {
		this.answerMultipleChoice = answerMultipleChoice;
	}

	public Question getQuestionNewEntity() {
		return questionNewEntity;
	}

	public void setQuestionNewEntity(Question questionNewEntity) {
		this.questionNewEntity = questionNewEntity;
	}
}
