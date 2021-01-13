package lixco.com.beans.entitystatic;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageView {
	static public void INFO(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", message));
	}

	static public void WARN(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Thông báo", message));
	}

	static public void ERROR(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thông báo", message));
	}

	static public void FATAL(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "Thông báo", message));
	}
}
