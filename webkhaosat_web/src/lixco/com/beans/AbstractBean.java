package lixco.com.beans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import lixco.com.services.AnswerService;
import lixco.com.services.QuestionService;
import lixco.com.services.QuestiontypeService;
import lixco.com.services.RatingService;
import lixco.com.services.SetofquestionService;
import lixco.com.services.UserService;

public class AbstractBean {
	@Inject
	protected QuestionService questionService;

	@Inject
	protected AnswerService answerService;

	@Inject
	protected RatingService ratingService; // RATING_SERVICE

	@Inject
	protected SetofquestionService setofquestionService;

	@Inject
	protected QuestiontypeService questiontypeService;
	
	@Inject 
	protected UserService userService;
	
	@PostConstruct
	public void init() {
		
	}
	public Date getDate() {
		return new Date();
	}

}
