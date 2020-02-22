/**
 * 
 */
package general;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Notify {
	FacesContext context;

	public Notify(FacesContext context) {
		this.context = context;
	}
	// Lock
	public void lock() {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ä�Ã£ khoÃ¡!", ""));
	}
	// Lock
	public void lock(String notice) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ä�Ã£ khoÃ¡!", notice));
	}
	// Thanh cong
	public void success(String notice) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ThÃ nh cÃ´ng!", notice));
	}

	public void success() {

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ThÃ nh cÃ´ng!", ""));
	}
	/**
	 * ThÃ´ng bÃ¡o tÃ i khoáº£n nÃ y khÃ´ng thá»ƒ thá»±c hiá»‡n Ä‘Æ°á»£c hoáº·c thÃ¡ng Ä‘Ã£ khoÃ¡
	 */
	public void warningDetail() {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cáº£nh bÃ¡o!",
				"TÃ i khoáº£n nÃ y khÃ´ng cÃ³ quyá»�n thá»±c hiá»‡n hoáº·c thÃ¡ng Ä‘Ã£ khoÃ¡!"));
	}
	// Canh bao
	public void warning(String notice) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cáº£nh bÃ¡o!", notice));
	}

	// Canh bao
	public void warning(String title, String notice) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, notice));
	}

	// Xay ra loi khong xac dinh
	public void error(String notice) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Xáº£y ra lá»—i!", notice));
	}

	public void error() {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Xáº£y ra lá»—i", ""));
	}

	public void message(String notice1, String notice2) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, notice1, notice2));
	}

	public void message(String notice1) {
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, notice1, ""));
	}
}
