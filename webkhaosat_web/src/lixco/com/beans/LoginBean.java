package lixco.com.beans;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jboss.logging.Logger;

@ManagedBean
@SessionScoped
public class LoginBean extends AbstractBean {

	private static final long serialVersionUID = 1L;
	private String nameDB;
	private String username;
	private String password;

	@Override
	protected void initItem() {
	}

	// Dang xuat
	public void logout() {
		try {
//			FacesContext context = FacesContext.getCurrentInstance();
//			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
//
//			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
//					.getRequest();
//			HttpSession session = req.getSession();
//			context.getExternalContext().invalidateSession();
//			context.getExternalContext().redirect(request.getContextPath() + "/login/loginadmin.jsf");
//			System.out.println("logout_____");
//			
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			FacesContext fContext = FacesContext.getCurrentInstance();
			ExternalContext extContext = fContext.getExternalContext();
			extContext.redirect("http://localhost:8180/account/pages/Start.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public String getNameDB() {
		return nameDB;
	}

	public void setNameDB(String nameDB) {
		this.nameDB = nameDB;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
