package lixco.com.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import general.AuthorizationManager;
import general.PrivateConfig;
import general.UrlPermission;
import lixco.com.services.AnswerService;
import lixco.com.services.QuestionService;
import lixco.com.services.QuestionTypeService;
import lixco.com.services.RatingService;
import lixco.com.services.SurveyService;
import lixco.com.services.UserResultService;
import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.AccountServicePublic;
import trong.lixco.com.account.servicepublics.AccountServicePublicProxy;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.account.servicepublics.LockTable;
import trong.lixco.com.account.servicepublics.LockTableServicePublic;
import trong.lixco.com.account.servicepublics.LockTableServicePublicProxy;
import trong.lixco.com.account.servicepublics.MemberServicePublic;
import trong.lixco.com.account.servicepublics.MemberServicePublicProxy;
import trong.lixco.com.account.servicepublics.Program;

public abstract class AbstractBean implements Serializable {
	private static final long serialVersionUID = 9154488968915975475L;
	@Inject
	protected AuthorizationManager authorizationManager;
	private Account account;
	private PrivateConfig cf;
	AccountServicePublic accountServicePublic;

	private LockTableServicePublic lockTableServicePublic;
//Thai
	protected MemberServicePublic EMPLOYEE_SERVICE = new MemberServicePublicProxy();
	protected DepartmentServicePublic DEPARTMENT_SERVICE = new DepartmentServicePublicProxy();

	@Inject
	protected QuestionService QUESTION_SERVICE;

	@Inject
	protected AnswerService ANSWER_SERVICE;

	@Inject
	protected RatingService RATING_SERVICE; // RATING_SERVICE

	@Inject
	protected SurveyService SURVEY_SERVICE;

	@Inject
	protected QuestionTypeService QUESTIONTYPE_SERVICE;

	@Inject
	protected UserResultService USER_RESULT_SERVICE;
	
	@PostConstruct
	public void init() {
		lockTableServicePublic = new LockTableServicePublicProxy();
		accountServicePublic = new AccountServicePublicProxy();
		initItem();
	}

//Thai
	protected Date getDate() {
		return new Date();
	}

// Bo thong bao
	protected void notifySuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	protected void notifyAddSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thêm mới thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	protected void notifyUpdateSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Cập nhật thành công!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	protected void notifyDeleteSuccess() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Đã xóa!");
		PrimeFaces.current().dialog().showMessageDynamic(message);
	}

	// Get param from URL
	protected long getParamSetOfId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("setofid");
		return Long.parseLong(setofIdTemp);
	}
//End Thai

	protected abstract void initItem();

	protected abstract Logger getLogger();

	private void getSession() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		account = (Account) session.getAttribute("account");
		if (account != null)
			try {
				cf = new PrivateConfig(account.getPrivateConfig());
			} catch (Exception e) {
			}

	}

	public void writeLogInfo(String message) {
		if (account == null)
			getSession();
		getLogger().info("(" + account.getUserName() + "): " + message);
	}

	public void writeLogError(String message) {
		if (account == null)
			getSession();
		getLogger().error("(" + account.getUserName() + "): " + message);
	}

	public void writeLogWarning(String message) {
		try {
			if (account == null)
				getSession();
			getLogger().fatal("(" + account.getUserName() + "): " + message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<UrlPermission> getUrlPermissions() {
		return authorizationManager.getUrlPermissions();
	}

	// Kiem tra thang da khoa chua
	public boolean lock(Date date) {
		LockTable lt;
		try {
			int month = (date.getMonth() + 1);
			int year = (date.getYear() + 1900);
			Program pr = authorizationManager.getProgram();
			lt = lockTableServicePublic.findMonthYear(month, year, pr);
			if (lt != null) {
				return lt.isLocks();
			} else {
				return false;
			}
		} catch (RemoteException e) {
			return false;
		}
	}

	public boolean allowSave(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isSave()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isSave();
			}
		} else {
			return false;
		}
	}

	public boolean allowUpdate(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isUpdate()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isUpdate();
			}
		} else {
			return false;
		}
	}

	public boolean allowDelete(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isDelete()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isDelete();
			}
		} else {
			return false;
		}
	}

	public Map<String, Object> installConfigPersonReport() {
		Map<String, Object> importParam = new HashMap<String, Object>();
		String image = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/gfx/lixco_logo.png");
		importParam.put("logo", image);
		if (cf == null)
			getSession();
		if (cf != null) {
			if (",".equals(cf.getDecimalSeparator()))
				importParam.put("REPORT_LOCALE", new Locale("vi", "VN"));
			importParam.put("formatNumber", cf.getParttenNumber());
			importParam.put("formatDate", cf.getFormatDate());

		}
		return importParam;

	}

	public int getMax(List<Integer> list) {
		int max = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) > max) {
				max = list.get(i);
			}
		}
		return max;
	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public PrivateConfig getCf() {
		if (cf == null)
			getSession();
		return cf;
	}

	public void setCf(PrivateConfig cf) {
		this.cf = cf;
	}

	public Account getAccount() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		account = (Account) session.getAttribute("account");
		String db = (String) session.getAttribute("database");
		try {
			if (account != null) {
				return accountServicePublic.findById(account.getId());
			} else {
				return new Account();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public String getDatabase() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		String db = (String) session.getAttribute("database");
		return db;

	}

}
