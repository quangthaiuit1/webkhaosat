package general;

/**
 * Danh muc menu
 */

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jboss.logging.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import lixco.com.beans.AbstractBean;
import trong.lixco.com.account.servicepublics.Menu;
import trong.lixco.com.account.servicepublics.MenuServicePublic;
import trong.lixco.com.account.servicepublics.MenuServicePublicProxy;
import trong.lixco.com.account.servicepublics.Program;
import trong.lixco.com.account.servicepublics.ProgramServicePublic;
import trong.lixco.com.account.servicepublics.ProgramServicePublicProxy;

@ManagedBean
@SessionScoped
public class MenuBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	private List<Menu> menus;
	private Menu menu;
	private ProgramServicePublic programService;
	private MenuServicePublic menuService;
	private Logger logger;



	@Override
	public void initItem() {
		menu = new Menu();
		programService = new ProgramServicePublicProxy();
		menuService = new MenuServicePublicProxy();
		setupMenuPreview();
	}

	/*
	 * Cai dat danh sach menu preview
	 */
	public void setupMenuPreview() {
		try {
			menus = new ArrayList<Menu>();
			Program program = programService.findByName("webkhaosat");
			Menu temps[] = menuService.find_Program(program);
			if (temps != null) {
				for (int j = 0; j < temps.length; j++) {
					if ("".equals(temps[j].getUrl())) {
						menus.add(temps[j]);
					} else {
						if (getUrlPermissions().size() == 0)
							getAuthorizationManager().isAllowed(getAccount());
						for (int i = 0; i < getUrlPermissions().size(); i++) {
							if (getUrlPermissions().get(i).getUrl().contains(temps[j].getUrl())) {
								menus.add(temps[j]);
								break;
							}
						}
					}
				}
			}
			model = new DefaultMenuModel();
			createMenu(menus);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Xay dung he thong menu hien thi cho moi chuong trinh
	 * 
	 * @param menus
	 *            : lay he danh sach menu cua tat ca cac chuong trinh
	 */
	public void createMenu(List<Menu> menus) {
		for (Menu menu : menus) {
			if (menu.getParent() == null) {
				boolean statusSubmenu = false;
				for (Menu checkM : menus) {
					if (menu.equals(checkM.getParent())) {
						statusSubmenu = true;
						break;
					}
				}
				if (statusSubmenu) {
					DefaultSubMenu submenu = new DefaultSubMenu(menu.getTenHienThi());
					submenu.setIcon(menu.getIcon());
					List<Object> objects = createDynamicMenu(menu);
					for (int i = 0; i < objects.size(); i++) {
						try {
							DefaultMenuItem item = (DefaultMenuItem) objects.get(i);
							submenu.addElement(item);
						} catch (Exception e) {
							DefaultSubMenu item = (DefaultSubMenu) objects.get(i);
							submenu.addElement(item);
						}
					}

					model.addElement(submenu);
				} else {
					DefaultMenuItem item = new DefaultMenuItem(menu.getTenHienThi());
					item.setHref("/webkhaosat_web"+menu.getUrl());
					item.setIcon(menu.getIcon());
					model.addElement(item);
				}
			}
		}
	}

	/**
	 * Ham cai dat cho menu he thong Ham goi de quy de tim tat ca cac menu con
	 * 
	 * @param menu
	 *            : menu cha
	 * @return danh sach menu con theo menu cha truyen vao
	 */
	public List<Object> createDynamicMenu(Menu menu) {
		List<Object> results = new ArrayList<Object>();
		// Kiem tra co menu con hay khong
		List<Menu> subs = new ArrayList<Menu>();
		for (Menu checkM : menus) {
			if (menu.equals(checkM.getParent())) {
				subs.add(checkM);
			}
		}
		for (Menu subM : subs) {
			boolean statusSubmenu = false;
			for (Menu checkM : menus) {
				if (subM.equals(checkM.getParent())) {
					statusSubmenu = true;
					break;
				}
			}
			if (statusSubmenu) {
				DefaultSubMenu item = new DefaultSubMenu(subM.getTenHienThi());
				List<Object> objSub = createDynamicMenu(subM);
				for (int i = 0; i < objSub.size(); i++) {
					try {
						DefaultMenuItem itemS = (DefaultMenuItem) objSub.get(i);
						item.addElement(itemS);
					} catch (Exception e) {
						DefaultSubMenu itemS = (DefaultSubMenu) objSub.get(i);
						item.addElement(itemS);
					}
				}
				results.add(item);
			} else {
				DefaultMenuItem item = new DefaultMenuItem(subM.getTenHienThi());
				item.setHref("/webkhaosat_web"+subM.getUrl());
				item.setIcon(subM.getIcon());
				results.add(item);
			}
		}
		return results;
	}

	private MenuModel model;

	public List<Menu> getMenus() {
		return menus;
	}

	public MenuModel getModel() {
		return model;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}
