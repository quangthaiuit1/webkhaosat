package general;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lixco.com.entities.AccountDatabase;
import trong.lixco.com.account.servicepublics.Account;

@javax.servlet.annotation.WebFilter("/pages/*")
public class WebFilterKPI implements Filter {

	@Inject
	private AuthorizationManager authorizationManager;
	@Inject
	private lixco.com.services.AccountDatabaseService accountDatabaseService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException,
			IOException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			HttpSession session = request.getSession();
			String db = null;
			Account account = null;
			if (session.getAttribute("account") != null)
				account = (Account) session.getAttribute("account");
			if (session.getAttribute("database") != null) {
				db = (String) session.getAttribute("database");
			}
			if (account == null && db == null) {
				// Chua co tai khoan, database dang nhap
				AccountDatabase acc = accountDatabaseService.findByName("hethong");
				try {
					String server = acc.getAddress();
					if (!checkAddressLocal())
						server = acc.getAddressPublic();
					response.sendRedirect(server+"/account/logout/");
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else {
				boolean allowAccessForm = authorizationManager.allowAccessForm(request.getRequestURI());
				// Kiem tra co duoc phep truy cap form
				
				if (allowAccessForm) {
					StringBuffer a=request.getRequestURL();
					chain.doFilter(req, res);
				} else {
					String url = "http://" + req.getServerName() + ":" + req.getServerPort() + "/kpi/error/403.jsf";
					response.sendRedirect(url);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCookie(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			String name = c.getName();
			if (name != null && name.equals(cookieName)) {
				return c.getValue();
			}
		}
		return "";
	}

	public boolean checkAddressLocal() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();

			String ipAddress = request.getHeader("X-FORWARDED-FOR");// ip
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				boolean temp = ipAddress.contains("192.168.");
				if (temp == false) {
					temp = ipAddress.contains("127.0.0.1");
				}
				return temp;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public String getURL(HttpServletRequest req) {

		String scheme = req.getScheme(); // http
		String serverName = req.getServerName(); // hostname.com
		int serverPort = req.getServerPort(); // 80
		String contextPath = req.getContextPath(); // /mywebapp
		String servletPath = req.getServletPath(); // /servlet/MyServlet
		String pathInfo = req.getPathInfo(); // /a/b;c=123
		String queryString = req.getQueryString(); // d=789

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}