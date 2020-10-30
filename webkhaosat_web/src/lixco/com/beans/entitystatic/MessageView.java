package lixco.com.beans.entitystatic;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageView {
	static public void INFO(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Th�ng b�o", message));
	}

	static public void WARN(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Th�ng b�o", message));
	}

	static public void ERROR(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Th�ng b�o", message));
	}

	static public void FATAL(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "Th�ng b�o", message));
	}
}
