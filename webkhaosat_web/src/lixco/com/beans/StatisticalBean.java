package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lixco.com.entities.Question;
import lixco.com.entities.Rating;
import lixco.com.entities.RatingQuantity;
import lixco.com.entities.Statistical;
import lixco.com.entities.StatisticalEnd;
import lixco.com.entities.Survey;
import trong.lixco.com.servicepublic.EmployeeDTO;

@ManagedBean
@ViewScoped
public class StatisticalBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Statistical> statisticalBegin;
	private List<StatisticalEnd> statisticalEndFirst;
	private List<StatisticalEnd> statisticalEndSecond;
	private Long surveyId;
	private Survey surveyPlaying;

	private List<EmployeeDTO> employeeBySurListDTO;
	private EmployeeDTO[] employeeBySurDTO;
	private static ObjectMapper mapper = new ObjectMapper();
	private int toanboKS = 0;
	private int soluongdaKS = 0;

	@Override
	protected void initItem() {
		surveyId = getParamSetOfId();
		surveyPlaying = SURVEY_SERVICE.findById(surveyId);
		statisticalBegin = USER_RESULT_SERVICE.getStatistical(surveyId);
		statisticalEndFirst = getListStatisticalBegin(surveyId);
		statisticalEndSecond = castListStatisticalEnd(statisticalBegin, statisticalEndFirst);
		soluongdaKS = USER_RESULT_SERVICE.findByCountResult(surveyId);

		// Thong ke so lieu
		try {
			if (StringUtils.isEmpty(surveyPlaying.getUsersJson())) {
				employeeBySurListDTO = new ArrayList<EmployeeDTO>();
			} else {
				employeeBySurDTO = mapper.readValue(surveyPlaying.getUsersJson(), EmployeeDTO[].class);
				employeeBySurListDTO = new ArrayList<>();
				employeeBySurListDTO = Arrays.asList(employeeBySurDTO);
				toanboKS = employeeBySurListDTO.size();
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//Tao bo cau hoi
	public List<StatisticalEnd> getListStatisticalBegin(long surveyId) {
		List<StatisticalEnd> sts = new ArrayList<>();
		List<Question> quests = QUESTION_SERVICE.find(null, surveyId, null);
		for (Question q : quests) {
			if (q.getQuestionType().getId() != 1 && q.getQuestionType().getId() != 4) {
				StatisticalEnd stNew = new StatisticalEnd();
				stNew.setQuestion(q);
				List<Rating> rts = RATING_SERVICE.find(q.getId(), 0L);
				List<RatingQuantity> raQuas = new ArrayList<>();
				for (Rating r : rts) {
					RatingQuantity raQuaTemp = new RatingQuantity();
					raQuaTemp.setRating(r);
					raQuaTemp.setQuantity(0L);
					raQuas.add(raQuaTemp);
				}
				stNew.setRatingQuantities(raQuas);
				sts.add(stNew);
			}
		}
		return sts;
	}

// Tao ra danh sach bo thong ke
	public List<StatisticalEnd> castListStatisticalEnd(List<Statistical> statisticals, List<StatisticalEnd> sts) {

		for (int i = 0; i < sts.size(); i++) {
			for (int j = 0; j < sts.get(i).getRatingQuantities().size(); j++) {
				for (int g = 0; g < statisticals.size(); g++) {
					if (statisticals.get(g).getQuestionId() == sts.get(i).getQuestion().getId()) {
						if (sts.get(i).getRatingQuantities().get(j).getRating().getName()
								.equals(statisticals.get(g).getResult())) {
							sts.get(i).getRatingQuantities().get(j).setQuantity(statisticals.get(g).getQuantity());
						}
					}
				}
			}
		}
		return sts;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
//GET AND SET

	public List<Statistical> getStatisticalBegin() {
		return statisticalBegin;
	}

	public void setStatisticalBegin(List<Statistical> statisticalBegin) {
		this.statisticalBegin = statisticalBegin;
	}

	public List<StatisticalEnd> getStatisticalEndFirst() {
		return statisticalEndFirst;
	}

	public void setStatisticalEndFirst(List<StatisticalEnd> statisticalEndFirst) {
		this.statisticalEndFirst = statisticalEndFirst;
	}

	public List<StatisticalEnd> getStatisticalEndSecond() {
		return statisticalEndSecond;
	}

	public void setStatisticalEndSecond(List<StatisticalEnd> statisticalEndSecond) {
		this.statisticalEndSecond = statisticalEndSecond;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Survey getSurveyPlaying() {
		return surveyPlaying;
	}

	public void setSurveyPlaying(Survey surveyPlaying) {
		this.surveyPlaying = surveyPlaying;
	}

	public int getToanboKS() {
		return toanboKS;
	}

	public void setToanboKS(int toanboKS) {
		this.toanboKS = toanboKS;
	}

	public int getSoluongdaKS() {
		return soluongdaKS;
	}

	public void setSoluongdaKS(int soluongdaKS) {
		this.soluongdaKS = soluongdaKS;
	}
}
