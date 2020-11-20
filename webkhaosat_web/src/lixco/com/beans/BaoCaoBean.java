package lixco.com.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

import lixco.com.entities.Survey;
import lixco.com.entities.UserResultDetail;
import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;

@ManagedBean
@ViewScoped
public class BaoCaoBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Survey> allSurvey;
	private Survey surveySelected;
	private String loaiBaoCao;
	DepartmentServicePublic departmentServicePublic;

	@Override
	protected void initItem() {
		surveySelected = new Survey();
		allSurvey = SURVEY_SERVICE.findAll();
		if (allSurvey.isEmpty() || allSurvey == null) {
			allSurvey = new ArrayList<Survey>();
		}
		departmentServicePublic = new DepartmentServicePublicProxy();
	}

	private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	public void baoCaoNangLucCanDaoTao() throws IOException {
		// PrimeFaces.current().executeScript("start().click()");
		if (loaiBaoCao.equals("")) {
			loaiBaoCao = "TB";
		}
		// handle data
		// neu khong chon ky khao sat nao
		if (surveySelected == null || surveySelected.getName() == null) {
			surveySelected = allSurvey.get(0);
		}
		// y kien
		List<UserResultDetail> listKetQua = new ArrayList<>();
		// toan bo
		if (loaiBaoCao.equals("TB")) {
			listKetQua = USER_RESULT_DETAI_SERVICE.find(0L, surveySelected.getId(), 0L);
		}
		// y kien khac
		if (loaiBaoCao.equals("YKK")) {
			listKetQua = USER_RESULT_DETAI_SERVICE.find(0L, surveySelected.getId(), 0L);
			List<UserResultDetail> ykienkhac = new ArrayList<>();
			for (UserResultDetail ur : listKetQua) {
				if (ur.getNote() != null) {
					ykienkhac.add(ur);
				}
			}
			listKetQua = new ArrayList<>();
			listKetQua.addAll(ykienkhac);
		}
		// cau hoi lay y kien
		if (loaiBaoCao.equals("LYK")) {
			listKetQua = USER_RESULT_DETAI_SERVICE.findDapAnLayYKien(surveySelected.getId());
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = null;
		if (loaiBaoCao.equals("TB")) {
			sheet = workbook.createSheet("Báo cáo toàn bộ");
		}
		if (loaiBaoCao.equals("YKK")) {
			sheet = workbook.createSheet("Báo cáo ý kiến khác");
		}
		if (loaiBaoCao.equals("LYK")) {
			sheet = workbook.createSheet("Báo cáo lấy ý kiến");
		}

		int rownum = 0;
		Cell cell;
		Row row;
		XSSFCellStyle style = createStyleForTitle(workbook);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		CellStyle styleContent = workbook.createCellStyle();
		row = sheet.createRow(rownum);

		// EmpNo
		cell = row.createCell(0);
		cell.setCellValue("Mã NV");
		// xep loai// EmpName
		cell = row.createCell(1);
		cell.setCellValue("Tên NV");
		cell.setCellStyle(style);
		// Salary
		cell = row.createCell(2);
		cell.setCellValue("Phòng ban");
		cell.setCellStyle(style);

		// Grade
		cell = row.createCell(3);
		cell.setCellValue("Kỳ khảo sát");
		cell.setCellStyle(style);
		// Bonus
		cell = row.createCell(4);
		cell.setCellValue("Câu hỏi");
		cell.setCellStyle(style);
		// xep loai
		cell = row.createCell(5);
		cell.setCellValue("Kết quả");
		cell.setCellStyle(style);

		cell = row.createCell(6);
		cell.setCellValue("Thang điểm");
		cell.setCellStyle(style);

		cell = row.createCell(7);
		cell.setCellValue("Lấy ý kiến");
		cell.setCellStyle(style);

		cell = row.createCell(8);
		cell.setCellValue("Ý kiến khác");
		cell.setCellStyle(style);
		// Data
		if (!listKetQua.isEmpty() && listKetQua != null) {
			for (UserResultDetail kq : listKetQua) {
				Gson gson = new Gson();
				rownum++;
				row = sheet.createRow(rownum);

				// EmpNo (A)
				cell = row.createCell(0);
				cell.setCellValue(kq.getUser_result().getEmployeeCode());
				// EmpName (B)
				cell = row.createCell(1);
				cell.setCellValue(kq.getUser_result().getEmployeeName());
				// phong
				cell = row.createCell(2);
				// xu ly phong theo du lieu moi
				Department dep = departmentServicePublic.findByCode("code", kq.getUser_result().getDepartmentCode());
				if (dep == null) {
					cell.setCellValue(kq.getUser_result().getDepartmentName());
				} else {
					if (dep.getLevelDep().getLevel() == 2) {
						cell.setCellValue(kq.getUser_result().getDepartmentName());
					}
					if (dep.getLevelDep().getLevel() == 3) {
						cell.setCellValue(dep.getDepartment().getName());
					}
				}

				cell = row.createCell(3);
				cell.setCellValue(kq.getQuestion().getSurvey().getName());

				cell = row.createCell(4);
				cell.setCellValue(kq.getQuestion().getName());
				// Nhom nang luc

				if (kq.getRating() != null) {
					cell = row.createCell(5);
					cell.setCellValue(kq.getRating().getName());

				}
				if (kq.getThangdiem() != 0) {
					cell = row.createCell(6);
					cell.setCellValue(kq.getThangdiem());
				}
				if (kq.getLay_y_kien() != null) {
					cell = row.createCell(7);
					cell.setCellValue(kq.getLay_y_kien());
				}
				if (kq.getNote() != null) {
					cell = row.createCell(8);
					cell.setCellValue(kq.getNote());
				}
			}
		}

		String filename = "baocao.xlsx";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		externalContext.setResponseContentType("application/vnd.ms-excel");
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		workbook.write(externalContext.getResponseOutputStream());
		// cancel progress
		facesContext.responseComplete();
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<Survey> getAllSurvey() {
		return allSurvey;
	}

	public void setAllSurvey(List<Survey> allSurvey) {
		this.allSurvey = allSurvey;
	}

	public Survey getSurveySelected() {
		return surveySelected;
	}

	public void setSurveySelected(Survey surveySelected) {
		this.surveySelected = surveySelected;
	}

	public String getLoaiBaoCao() {
		return loaiBaoCao;
	}

	public void setLoaiBaoCao(String loaiBaoCao) {
		this.loaiBaoCao = loaiBaoCao;
	}
}
